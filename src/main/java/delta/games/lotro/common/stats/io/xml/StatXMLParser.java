package delta.games.lotro.common.stats.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.i18n.SingleLocaleLabelsManager;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.stats.FloatStatDescription;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatType;
import delta.games.lotro.utils.i18n.I18nFacade;

/**
 * Parser for stat descriptions stored in XML.
 * @author DAM
 */
public class StatXMLParser
{
  private SingleLocaleLabelsManager _i18n;

  /**
   * Constructor.
   */
  public StatXMLParser()
  {
    _i18n=I18nFacade.getLabelsMgr("stats");
  }

  /**
   * Parse a stat descriptions XML file.
   * @param source Source file.
   * @return List of parsed stat descriptions.
   */
  public List<StatDescription> parseStatDescriptionsFile(File source)
  {
    List<StatDescription> descriptions=new ArrayList<StatDescription>();
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      List<Element> classTags=DOMParsingTools.getChildTagsByName(root,StatXMLConstants.STAT_TAG);
      for(Element classTag : classTags)
      {
        StatDescription description=parseStatDescription(classTag);
        descriptions.add(description);
      }
    }
    return descriptions;
  }

  /**
   * Build a stat description from an XML tag.
   * @param root Root XML tag.
   * @return A stat description.
   */
  private StatDescription parseStatDescription(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    StatDescription ret=null;
    FloatStatDescription floatDescription=null;
    // Type
    String typeStr=DOMParsingTools.getStringAttribute(attrs,StatXMLConstants.STAT_TYPE_ATTR,null);
    if (typeStr!=null)
    {
      StatType type=StatType.valueOf(typeStr);
      ret=new StatDescription();
      ret.setType(type);
    }
    else
    {
      floatDescription=new FloatStatDescription();
      ret=floatDescription;
    }
    // ID
    int id=DOMParsingTools.getIntAttribute(attrs,StatXMLConstants.STAT_ID_ATTR,0);
    ret.setIdentifier(id);
    // Index
    int index=DOMParsingTools.getIntAttribute(attrs,StatXMLConstants.STAT_INDEX_ATTR,-1);
    if (index>=0)
    {
      ret.setIndex(Integer.valueOf(index));
    }
    // Name
    String name=_i18n.getLabel(String.valueOf(id));
    ret.setInternalName(name);
    // Key
    String key=DOMParsingTools.getStringAttribute(attrs,StatXMLConstants.STAT_KEY_ATTR,null);
    ret.setKey(key);
    // Legacy key
    String legacyKey=DOMParsingTools.getStringAttribute(attrs,StatXMLConstants.STAT_LEGACY_KEY_ATTR,null);
    ret.setLegacyKey(legacyKey);
    // Legacy name
    String legacyName=_i18n.getLabel("legacy:"+id);
    ret.setLegacyName(legacyName);
    // Is percentage
    boolean isPercentage=DOMParsingTools.getBooleanAttribute(attrs,StatXMLConstants.STAT_IS_PERCENTAGE_ATTR,false);
    ret.setPercentage(isPercentage);
    // Float specifics
    if (floatDescription!=null)
    {
      // - Max digits below 1
      int nbMaxDigitsBelow1=DOMParsingTools.getIntAttribute(attrs,StatXMLConstants.STAT_MAX_DIGITS_BELOW1_ATTR,2);
      floatDescription.setMaxDigitsBelow1(nbMaxDigitsBelow1);
      // - Max digits above 1
      int nbMaxDigitsAbove1=DOMParsingTools.getIntAttribute(attrs,StatXMLConstants.STAT_MAX_DIGITS_ABOVE1_ATTR,0);
      floatDescription.setMaxDigitsAbove1(nbMaxDigitsAbove1);
    }
    return ret;
  }
}
