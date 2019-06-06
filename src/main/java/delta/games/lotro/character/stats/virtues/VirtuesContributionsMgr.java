package delta.games.lotro.character.stats.virtues;

import org.apache.log4j.Logger;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.progression.ProgressionsManager;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.utils.maths.Progression;

/**
 * Manager for contributions of virtues to player stats.
 * @author DAM
 */
public final class VirtuesContributionsMgr
{
  private static final Logger LOGGER=Logger.getLogger(VirtuesContributionsMgr.class);

  private static final int RANK_TO_LEVEL_PROGRESSION_ID=1879387583;

  private Progression _rankToLevelProgression;

  private static final VirtuesContributionsMgr _instance=new VirtuesContributionsMgr();

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static VirtuesContributionsMgr get()
  {
    return _instance;
  }

  /**
   * Constructor.
   */
  private VirtuesContributionsMgr()
  {
    _rankToLevelProgression=ProgressionsManager.getInstance().getProgression(RANK_TO_LEVEL_PROGRESSION_ID);
    if (_rankToLevelProgression==null)
    {
      LOGGER.warn("Could not load progression "+RANK_TO_LEVEL_PROGRESSION_ID+" (virtue rank to level)");
    }
  }

  /**
   * Get the contribution for a given virtue and rank.
   * @param virtueId Virtue identifier.
   * @param rank Rank (starting at 1).
   * @param passive Passive stats or active stats?
   * @return A stats set or <code>null</code> if not found.
   */
  public BasicStatsSet getContribution(VirtueId virtueId, int rank, boolean passive)
  {
    BasicStatsSet stats=null;
    VirtuesManager virtuesMgr=VirtuesManager.getInstance();
    VirtueDescription virtue=virtuesMgr.getVirtueByKey(virtueId.name());
    if (virtue!=null)
    {
      if (rank>0)
      {
        int level=_rankToLevelProgression.getValue(rank).intValue();
        StatsProvider statsProvider;
        if (passive)
        {
          statsProvider=virtue.getPassiveStatsProvider();
        }
        else
        {
          statsProvider=virtue.getStatsProvider();
        }
        stats=statsProvider.getStats(1,level);
      }
    }
    else
    {
      LOGGER.warn("Virtue description not found: "+virtueId);
    }
    if (stats==null)
    {
      stats=new BasicStatsSet();
    }
    return stats;
  }

  /**
   * Get stats contribution for a set of virtues.
   * @param virtues Virtues set.
   * @param includeActive Include stats for active virtues or not.
   * @return A stats set.
   */
  public BasicStatsSet getContribution(VirtuesSet virtues, boolean includeActive)
  {
    BasicStatsSet ret=new BasicStatsSet();
    for(VirtueId virtue : VirtueId.values())
    {
      int rank=virtues.getVirtueRank(virtue);
      BasicStatsSet passiveContrib=getContribution(virtue,rank,true);
      ret.addStats(passiveContrib);
      if (includeActive)
      {
        boolean selected=virtues.isSelected(virtue);
        if (selected)
        {
          BasicStatsSet activeContrib=getContribution(virtue,rank,false);
          ret.addStats(activeContrib);
        }
      }
    }
    return ret;
  }
}
