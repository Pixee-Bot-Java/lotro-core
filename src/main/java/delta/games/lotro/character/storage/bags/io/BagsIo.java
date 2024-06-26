package delta.games.lotro.character.storage.bags.io;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.io.xml.BagsXMLParser;
import delta.games.lotro.character.storage.bags.io.xml.BagsXMLWriter;
import delta.games.lotro.character.storage.summary.CharacterStorageSummary;
import delta.games.lotro.character.storage.summary.SingleStorageSummary;
import delta.games.lotro.character.storage.summary.StorageSummaryIO;

/**
 * I/O methods for bags.
 * @author DAM
 */
public class BagsIo
{
  /**
   * Load the bags for a character.
   * @param character Targeted character.
   * @return A bags manager.
   */
  public static BagsManager load(CharacterFile character)
  {
    File fromFile=getBagsFile(character);
    BagsManager bags=null;
    if (fromFile.exists())
    {
      BagsXMLParser parser=new BagsXMLParser();
      bags=parser.parseXML(fromFile);
    }
    if (bags==null)
    {
      bags=new BagsManager();
      save(character,bags);
    }
    return bags;
  }

  /**
   * Save the bags for a character.
   * @param character Targeted character.
   * @param bags Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean save(CharacterFile character, BagsManager bags)
  {
    File toFile=getBagsFile(character);
    BagsXMLWriter writer=new BagsXMLWriter();
    boolean ok=writer.write(toFile,bags,EncodingNames.UTF_8);
    saveSummary(character,bags);
    return ok;
  }

  private static void saveSummary(CharacterFile character, BagsManager bagsMgr)
  {
    CharacterStorageSummary summary=StorageSummaryIO.loadCharacterStorageSummary(character);
    SingleStorageSummary vaultSummary=summary.getBags();
    int max=bagsMgr.getCapacity();
    vaultSummary.setMax(max);
    int used=bagsMgr.getUsed();
    vaultSummary.setAvailable(max-used);
    StorageSummaryIO.save(character,summary);
  }

  /**
   * Get the path of the bags file.
   * @param character Targeted character.
   * @return A file.
   */
  public static File getBagsFile(CharacterFile character)
  {
    File rootDir=character.getRootDir();
    File bagsFile=new File(rootDir,"inventory.xml");
    return bagsFile;
  }
}
