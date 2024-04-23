package delta.games.lotro.lore.items.sets.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.i18n.SingleLocaleLabelsManager;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.io.xml.StatsProviderXMLParser;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.effects.io.xml.ItemEffectsXmlIO;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSet.SetType;
import delta.games.lotro.lore.items.sets.SetBonus;
import delta.games.lotro.utils.i18n.I18nFacade;
import delta.games.lotro.utils.i18n.I18nRuntimeUtils;

/**
 * Parser for items sets descriptions stored in XML.
 * @author DAM
 */
public class ItemsSetXMLParser
{
  private SingleLocaleLabelsManager _i18n;

  /**
   * Constructor.
   */
  public ItemsSetXMLParser()
  {
    _i18n=I18nFacade.getLabelsMgr("itemsSets");
  }

  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed items set or <code>null</code>.
   */
  public List<ItemsSet> parseXML(File source)
  {
    List<ItemsSet> sets=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      sets=parseSets(root);
    }
    return sets;
  }

  private List<ItemsSet> parseSets(Element root)
  {
    List<ItemsSet> ret=new ArrayList<ItemsSet>();
    List<Element> itemsSetTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.ITEMS_SET_TAG);
    for(Element itemsSetTag : itemsSetTags)
    {
      ItemsSet set=parseSet(itemsSetTag);
      ret.add(set);
    }
    return ret;
  }

  private ItemsSet parseSet(Element root)
  {
    ItemsSet ret=new ItemsSet();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_ID_ATTR,0);
    ret.setIdentifier(id);
    // Name
    String name=_i18n.getLabel(String.valueOf(id));
    ret.setName(name);
    // Type
    String setTypeStr=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_TYPE_ATTR,null);
    SetType setType=SetType.ITEMS;
    if (setTypeStr!=null)
    {
      setType=SetType.valueOf(setTypeStr);
    }
    ret.setSetType(setType);
    // Set level
    int setLevel=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_LEVEL_ATTR,-1);
    ret.setSetLevel(setLevel);
    boolean useAverageItemLevelForSetLevel=DOMParsingTools.getBooleanAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_USE_AVERAGE_ITEM_LEVEL_ATTR,false);
    ret.setUseAverageItemLevelForSetLevel(useAverageItemLevelForSetLevel);
    // Required level
    int requiredLevel=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_REQUIRED_LEVEL_ATTR,1);
    ret.setRequiredMinLevel(requiredLevel);
    // Character max level
    int characterMaxLevel=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_MAX_LEVEL_ATTR,0);
    if (characterMaxLevel!=0)
    {
      ret.setRequiredMaxLevel(Integer.valueOf(characterMaxLevel));
    }
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_DESCRIPTION_ATTR,"");
    description=I18nRuntimeUtils.getLabel(_i18n,description);
    ret.setDescription(description);

    // Items
    List<Element> itemTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.ITEM_TAG);
    if (itemTags!=null)
    {
      for(Element itemTag : itemTags)
      {
        int memberId=DOMParsingTools.getIntAttribute(itemTag.getAttributes(),ItemsSetXMLConstants.ITEM_ID_ATTR,0);
        Item member=ItemsManager.getInstance().getItem(memberId);
        if (member!=null)
        {
          ret.addMember(member);
        }
      }
    }
    // Bonuses
    List<Element> bonusTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.BONUS_TAG);
    for(Element bonusTag : bonusTags)
    {
      NamedNodeMap bonusAttrs=bonusTag.getAttributes();
      int piecesCount=DOMParsingTools.getIntAttribute(bonusAttrs,ItemsSetXMLConstants.BONUS_NB_ITEMS_ATTR,0);
      if (piecesCount!=0)
      {
        SetBonus bonus=new SetBonus(piecesCount);
        // Stats
        StatsProvider statsProvider=StatsProviderXMLParser.parseStatsProvider(bonusTag,_i18n);
        bonus.setStatsProvider(statsProvider);
        // Effects
        ItemEffectsXmlIO.readSetBonusEffects(bonusTag,bonus);
        ret.addBonus(bonus);
      }
    }
    return ret;
  }
}
