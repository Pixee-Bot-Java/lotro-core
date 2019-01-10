package delta.games.lotro.common.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.games.lotro.LotroCoreConfig;
import delta.games.lotro.common.IdentifiableComparator;
import delta.games.lotro.common.stats.io.xml.StatXMLParser;
import delta.games.lotro.common.stats.io.xml.StatXMLWriter;

/**
 * Registry for all stats.
 * @author DAM
 */
public class StatsRegistry
{
  private static final Logger LOGGER=Logger.getLogger(StatsRegistry.class);

  private static StatsRegistry _instance=null;

  private Map<Integer,StatDescription> _mapById;
  private Map<String,StatDescription> _mapByKey;

  /**
   * Get the reference instance of this class.
   * @return the reference instance of this class.
   */
  public static StatsRegistry getInstance()
  {
    if (_instance==null)
    {
      _instance=new StatsRegistry();
      _instance.loadAll();
    }
    return _instance;
  }

  /**
   * Constructor.
   */
  public StatsRegistry()
  {
    _mapById=new HashMap<Integer,StatDescription>();
    _mapByKey=new HashMap<String,StatDescription>();
  }

  /**
   * Load all.
   */
  private void loadAll()
  {
    clear();
    LotroCoreConfig cfg=LotroCoreConfig.getInstance();
    File loreDir=cfg.getLoreDir();
    File statsRegistryFile=new File(loreDir,"stats.xml");
    loadFromFile(statsRegistryFile);
  }

  /**
   * Load some stats from a file.
   * @param inputFile Input file.
   */
  public void loadFromFile(File inputFile)
  {
    long now=System.currentTimeMillis();
    int nbStats=0;
    List<StatDescription> stats=StatXMLParser.parseStatDescriptionsFile(inputFile);
    for(StatDescription stat : stats)
    {
      addStat(stat);
      nbStats++;
    }
    long now2=System.currentTimeMillis();
    long duration=now2-now;
    LOGGER.info("Loaded "+nbStats+" stats in "+duration+"ms.");
  }

  /**
   * Get the number of stats.
   * @return the number of stats.
   */
  public int getNbStats()
  {
    return _mapById.size();
  }

  private void clear()
  {
    _mapById.clear();
    _mapByKey.clear();
  }

  /**
   * Add a stat.
   * @param stat Stat to add.
   */
  public void addStat(StatDescription stat)
  {
    Integer id=Integer.valueOf(stat.getIdentifier());
    StatDescription old=_mapById.get(id);
    if (old==null)
    {
      _mapById.put(id,stat);
      String key=stat.getKey();
      if (key!=null)
      {
        _mapByKey.put(key,stat);
      }
      String legacyKey=stat.getLegacyKey();
      if (legacyKey!=null)
      {
        _mapByKey.put(legacyKey,stat);
      }
    }
  }

  /**
   * Get a stat using a string key.
   * @param key Key to use.
   * @return A stat or <code>null</code> if not found.
   */
  public StatDescription getByKey(String key)
  {
    return _mapById.get(key);
  }

  /**
   * Get a stat using an identifier.
   * @param id Identifier to use.
   * @return A stat or <code>null</code> if not found.
   */
  public StatDescription getById(int id)
  {
    return _mapById.get(Integer.valueOf(id));
  }

  /**
   * Get a list of all known stats.
   * @return a possibly empty but not <code>null</code> list of stats.
   */
  public List<StatDescription> getAll()
  {
    return new ArrayList<StatDescription>(_mapById.values());
  }

  /**
   * Write the managed stats to a XML file.
   * @param toFile File to write to.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean writeToFile(File toFile)
  {
    int nbStats=getNbStats();
    LOGGER.info("Writing "+nbStats+" stats to: "+toFile);
    List<StatDescription> stats=getAll();
    Collections.sort(stats,new IdentifiableComparator<StatDescription>());
    return StatXMLWriter.write(toFile,stats);
  }
}
