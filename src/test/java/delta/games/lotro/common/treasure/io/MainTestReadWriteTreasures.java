package delta.games.lotro.common.treasure.io;

import java.io.File;

import delta.games.lotro.common.treasure.LootsManager;
import delta.games.lotro.common.treasure.io.xml.TreasureXMLParser;
import delta.games.lotro.common.treasure.io.xml.TreasureXMLWriter;
import delta.games.lotro.config.DataFiles;
import delta.games.lotro.config.LotroCoreConfig;

/**
 * Test to check read/write of treasures.
 * @author DAM
 */
public class MainTestReadWriteTreasures
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File in=LotroCoreConfig.getInstance().getFile(DataFiles.LOOTS);
    LootsManager lootMgr=new TreasureXMLParser().parseXML(in);
    File to=LotroCoreConfig.getInstance().getFile(DataFiles.LOOTS);
    File to2=new File(to.getParentFile(),"newLoot.xml");
    TreasureXMLWriter.writeLootsFile(to2,lootMgr);
  }
}
