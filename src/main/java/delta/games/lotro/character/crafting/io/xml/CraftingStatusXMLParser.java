package delta.games.lotro.character.crafting.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.crafting.CraftingLevelStatus;
import delta.games.lotro.character.crafting.CraftingLevelTierStatus;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.Vocation;
import delta.games.lotro.crafting.Vocations;

/**
 * Parser for the crafting status of a character stored in XML.
 * @author DAM
 */
public class CraftingStatusXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed data or <code>null</code>.
   */
  public CraftingStatus parseXML(File source)
  {
    CraftingStatus status=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      status=parseStatus(root);
    }
    return status;
  }

  private CraftingStatus parseStatus(Element root)
  {
    // Name
    String name=DOMParsingTools.getStringAttribute(root.getAttributes(),CraftingStatusXMLConstants.CRAFTING_NAME_ATTR,"");
    CraftingStatus status=new CraftingStatus(name);

    Element vocationTag=DOMParsingTools.getChildTagByName(root,CraftingStatusXMLConstants.VOCATION_TAG);
    if (vocationTag!=null)
    {
      String vocationId=DOMParsingTools.getStringAttribute(vocationTag.getAttributes(),CraftingStatusXMLConstants.VOCATION_ID_ATTR,"");
      Vocation vocation=Vocations.getInstance().getVocationById(vocationId);
      status.setVocation(vocation);
    }

    List<Element> professionTags=DOMParsingTools.getChildTagsByName(root,CraftingStatusXMLConstants.PROFESSION_TAG);
    for(Element professionTag : professionTags)
    {
      NamedNodeMap professionAttrs=professionTag.getAttributes();
      String professionId=DOMParsingTools.getStringAttribute(professionAttrs,CraftingStatusXMLConstants.PROFESSION_ID_ATTR,null);
      Profession profession=Profession.getByKey(professionId);
      if (profession!=null)
      {
        ProfessionStatus professionStatus=status.getProfessionStatus(profession,true);
        List<Element> levelTags=DOMParsingTools.getChildTagsByName(professionTag,CraftingStatusXMLConstants.LEVEL_TAG);
        for(Element levelTag : levelTags)
        {
          NamedNodeMap levelAttrs=levelTag.getAttributes();
          String levelId=DOMParsingTools.getStringAttribute(levelAttrs,CraftingStatusXMLConstants.LEVEL_TIER_ATTR,null);
          Integer levelTier=NumericTools.parseInteger(levelId);
          if (levelTier!=null)
          {
            CraftingLevelStatus levelStatus=professionStatus.getLevelStatus(levelTier.intValue());
            parseCraftingLevelTier(levelTag,CraftingStatusXMLConstants.PROFICIENCY_TAG,levelStatus.getProficiency());
            parseCraftingLevelTier(levelTag,CraftingStatusXMLConstants.MASTERY_TAG,levelStatus.getMastery());
          }
        }
      }
    }
    return status;
  }

  private void parseCraftingLevelTier(Element levelTag, String tagName, CraftingLevelTierStatus tierStatus)
  {
    Element tierTag=DOMParsingTools.getChildTagByName(levelTag,tagName);
    if (tierTag!=null)
    {
      NamedNodeMap attrs=tierTag.getAttributes();
      // XP
      int xp=DOMParsingTools.getIntAttribute(attrs,CraftingStatusXMLConstants.XP_ATTR,0);
      tierStatus.setAcquiredXP(xp);
      // Completed
      boolean completed=DOMParsingTools.getBooleanAttribute(attrs,CraftingStatusXMLConstants.COMPLETED_ATTR,false);
      tierStatus.setCompleted(completed);
      // Completion date
      long date=DOMParsingTools.getLongAttribute(attrs,CraftingStatusXMLConstants.COMPLETION_DATE_ATTR,0);
      tierStatus.setCompletionDate(date);
    }
  }
}
