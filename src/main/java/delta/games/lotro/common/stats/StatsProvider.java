package delta.games.lotro.common.stats;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Stats provider.
 * @author DAM
 */
public class StatsProvider
{
  private List<StatProvider> _stats;

  /**
   * Constructor.
   */
  public StatsProvider()
  {
    _stats=new ArrayList<StatProvider>();
  }

  /**
   * Add a stat provider.
   * @param statProvider Stat provider to add.
   */
  public void addStatProvider(StatProvider statProvider)
  {
    _stats.add(statProvider);
  }

  /**
   * Get the number of stats providers.
   * @return a count.
   */
  public int getNumberOfStatProviders()
  {
    return _stats.size();
  }

  /**
   * Get the stat provider at the given index.
   * @param index Index of provider, starting at 0.
   * @return A stat provider.
   */
  public StatProvider getStatProvider(int index)
  {
    return _stats.get(index);
  }

  /**
   * Compute stats for a given tier and level.
   * @param tier Tier to use, starting at 1.
   * @param level Level to use, starting at 1.
   * @return A set of stats.
   */
  public BasicStatsSet getStats(int tier, int level)
  {
    return getStats(tier,level,false);
  }

  /**
   * Compute stats for a given tier and level.
   * @param tier Tier to use, starting at 1.
   * @param level Level to use, starting at 1.
   * @param round Perform rounding to integer values or not.
   * @return A set of stats.
   */
  public BasicStatsSet getStats(int tier, int level, boolean round)
  {
    BasicStatsSet stats=new BasicStatsSet();
    for(StatProvider provider : _stats)
    {
      StatOperator operator=provider.getOperator();
      // Ignore multiplicative stats
      if (operator==StatOperator.MULTIPLY)
      {
        continue;
      }
      Float value=provider.getStatValue(tier,level);
      if (value!=null)
      {
        FixedDecimalsInteger statValue=null;
        STAT stat=provider.getStat();
        float floatValue=value.floatValue();
        if (round)
        {
          if (!stat.isPercentage())
          {
            int intValue;
            if (shallRound(stat))
            {
              intValue=Math.round(floatValue);
            }
            else
            {
              intValue=(int)(floatValue);
            }
            if (operator==StatOperator.SUBSTRACT)
            {
              intValue=-intValue;
            }
            statValue=new FixedDecimalsInteger(intValue);
          }
        }
        if (statValue==null)
        {
          if (operator==StatOperator.SUBSTRACT)
          {
            floatValue=-floatValue;
          }
          statValue=new FixedDecimalsInteger(floatValue);
        }
        stats.setStat(stat,statValue);
      }
    }
    return stats;
  }

  private boolean shallRound(STAT stat)
  {
    if (stat==STAT.ARMOUR) return false;
    if (stat==STAT.MIGHT) return false;
    if (stat==STAT.AGILITY) return false;
    if (stat==STAT.VITALITY) return false;
    if (stat==STAT.WILL) return false;
    if (stat==STAT.FATE) return false;
    return true;
  }
}