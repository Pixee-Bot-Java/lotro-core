package delta.games.lotro.character.stats.buffs;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.stats.ScalableStatProvider;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatOperator;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.TieredScalableStatProvider;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Buff based on a trait.
 * @author DAM
 */
public class TraitBuff extends AbstractBuffImpl
{
  private TraitDescription _trait;

  /**
   * Constructor.
   * @param trait Trait to use.
   */
  public TraitBuff(TraitDescription trait)
  {
    _trait=trait;
  }

  @Override
  public BasicStatsSet getStats(CharacterData character, BuffInstance buff)
  {
    StatsProvider statsProvider=_trait.getStatsProvider();
    BasicStatsSet stats=new BasicStatsSet();
    int nbStats=statsProvider.getNumberOfStatProviders();
    for(int i=0;i<nbStats;i++)
    {
      StatProvider provider=statsProvider.getStatProvider(i);
      StatOperator operator=provider.getOperator();
      if (operator==StatOperator.MULTIPLY)
      {
        continue;
      }
      Float value=getStatValue(character,buff,provider);
      if (value!=null)
      {
        float statValue=value.floatValue();
        if (operator==StatOperator.SUBSTRACT)
        {
          statValue=-statValue;
        }
        StatDescription stat=provider.getStat();
        stats.setStat(stat,new FixedDecimalsInteger(statValue));
      }
    }
    return stats;
  }

  @Override
  public BasicStatsSet getStats(CharacterData character, BasicStatsSet raw, BuffInstance buff)
  {
    BasicStatsSet stats=null;
    StatsProvider statsProvider=_trait.getStatsProvider();
    int nbStats=statsProvider.getNumberOfStatProviders();
    for(int i=0;i<nbStats;i++)
    {
      StatProvider provider=statsProvider.getStatProvider(i);
      StatOperator operator=provider.getOperator();
      if (operator==StatOperator.MULTIPLY)
      {
        Float value=getStatValue(character,buff,provider);
        if (value!=null)
        {
          StatDescription stat=provider.getStat();
          FixedDecimalsInteger rawStat=raw.getStat(stat);
          FixedDecimalsInteger contrib=new FixedDecimalsInteger(rawStat.floatValue()*(value.floatValue()-1));
          if (stats==null)
          {
            stats=new BasicStatsSet();
          }
          stats.setStat(stat,contrib);
        }
      }
    }
    return stats;
  }

  @Override
  public List<Integer> getTiers()
  {
    List<Integer> ret=new ArrayList<Integer>();
    int tiers=_trait.getTiersCount();
    if (tiers>1)
    {
      for(int i=1;i<=tiers;i++)
      {
        ret.add(Integer.valueOf(i));
      }
    }
    return ret;
  }

  private Float getStatValue(CharacterData character, BuffInstance buff, StatProvider provider)
  {
    Integer tier=buff.getTier();
    int tierValue=(tier!=null)?tier.intValue():1;
    int level=character.getLevel();
    Float value=null;
    if (provider instanceof TieredScalableStatProvider)
    {
      value=provider.getStatValue(tierValue,level);
    }
    else if (provider instanceof ScalableStatProvider)
    {
      ScalableStatProvider scalableStatProvider=(ScalableStatProvider)provider;
      int nbTiers=_trait.getTiersCount();
      if (nbTiers>1)
      {
        value=scalableStatProvider.getStatValue(1,tierValue);
      }
      else
      {
        value=scalableStatProvider.getStatValue(1,level);
      }
    }
    else
    {
      value=provider.getStatValue(1,level);
    }
    return value;
  }
}
