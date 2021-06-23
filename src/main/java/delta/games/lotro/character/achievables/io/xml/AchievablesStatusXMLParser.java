package delta.games.lotro.character.achievables.io.xml;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Parser for the achievables status stored in XML.
 * @author DAM
 */
public class AchievablesStatusXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(AchievablesStatusXMLParser.class);

  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed status or <code>null</code>.
   */
  public AchievablesStatusManager parseXML(File source)
  {
    AchievablesStatusManager status=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      status=parseStatus(root);
    }
    return status;
  }

  private AchievablesStatusManager parseStatus(Element root)
  {
    AchievablesStatusManager status=new AchievablesStatusManager();
    List<Element> deedStatusTags=DOMParsingTools.getChildTagsByName(root,AchievablesStatusXMLConstants.DEED_STATUS_TAG,false);
    for(Element deedStatusTag : deedStatusTags)
    {
      parseAchievableStatus(status,deedStatusTag);
    }
    return status;
  }

  private void parseAchievableStatus(AchievablesStatusManager status, Element deedStatusTag)
  {
    NamedNodeMap attrs=deedStatusTag.getAttributes();
    String key=DOMParsingTools.getStringAttribute(attrs,AchievablesStatusXMLConstants.STATUS_KEY_ATTR,null);
    if (key==null)
    {
      // No deed key!
      LOGGER.warn("No deed key!");
      return;
    }
    // Create status
    Achievable achievable=DeedsManager.getInstance().getDeed(key);
    if (achievable==null)
    {
      achievable=QuestsManager.getInstance().getQuest(Integer.parseInt(key));
    }
    if (achievable==null)
    {
      // Unknown achievable!
      LOGGER.warn("Unknown achievable: "+key);
      return;
    }
    AchievableStatus newStatus=status.get(achievable,true);
    if (newStatus==null)
    {
      // Unknown achievable!
      LOGGER.warn("Unknown achievable: "+key);
      return;
    }
    // State
    String stateStr=DOMParsingTools.getStringAttribute(attrs,AchievablesStatusXMLConstants.STATUS_STATE_ATTR,null);
    if (stateStr!=null)
    {
      AchievableElementState state=parseState(stateStr);
      newStatus.setState(state);
    }
    else
    {
      boolean completed=DOMParsingTools.getBooleanAttribute(attrs,AchievablesStatusXMLConstants.STATUS_COMPLETED_ATTR,false);
      newStatus.setCompleted(completed);
    }
    // Completion date
    String completionDateStr=DOMParsingTools.getStringAttribute(attrs,AchievablesStatusXMLConstants.STATUS_COMPLETION_DATE_ATTR,null);
    if (completionDateStr!=null)
    {
      Long completionDate=NumericTools.parseLong(completionDateStr);
      newStatus.setCompletionDate(completionDate);
    }
    // Objectives status
    parseObjectivesStatus(deedStatusTag,newStatus);
    // Update internal states
    newStatus.updateInternalState();
  }

  /**
   * Load achievable objectives status from the given XML stream.
   * @param deedStatusTag Status tag.
   * @param status Status to write.
   */
  private void parseObjectivesStatus(Element deedStatusTag, AchievableStatus status)
  {
    List<Element> objectiveStatusTags=DOMParsingTools.getChildTagsByName(deedStatusTag,AchievablesStatusXMLConstants.OBJECTIVE_STATUS_TAG);
    for(Element objectiveStatusTag : objectiveStatusTags)
    {
      NamedNodeMap objectiveAttrs=objectiveStatusTag.getAttributes();
      // Find objective by index
      int objectiveIndex=DOMParsingTools.getIntAttribute(objectiveAttrs,AchievablesStatusXMLConstants.OBJECTIVE_STATUS_INDEX_ATTR,-1);
      AchievableObjectiveStatus objectiveStatus=status.getObjectiveStatus(objectiveIndex);
      if (objectiveStatus==null)
      {
        LOGGER.warn("Objective not found: achievable ID="+status.getAchievableId()+" - index="+objectiveIndex);
        continue;
      }
      // State
      String stateStr=DOMParsingTools.getStringAttribute(objectiveAttrs,AchievablesStatusXMLConstants.OBJECTIVE_STATUS_STATE_ATTR,null);
      AchievableElementState state=parseState(stateStr);
      objectiveStatus.setState(state);
      // Conditions
      parseConditionsStatus(objectiveStatusTag,objectiveStatus);
    }
  }

  /**
   * Load objective conditions status from the given XML stream.
   * @param objectiveStatusTag Status tag.
   * @param objectiveStatus Status to write.
   */
  private void parseConditionsStatus(Element objectiveStatusTag, AchievableObjectiveStatus objectiveStatus)
  {
    List<Element> conditionStatusTags=DOMParsingTools.getChildTagsByName(objectiveStatusTag,AchievablesStatusXMLConstants.CONDITION_STATUS_TAG);
    for(Element conditionStatusTag : conditionStatusTags)
    {
      NamedNodeMap conditionAttrs=conditionStatusTag.getAttributes();
      // Find condition by index
      int conditionIndex=DOMParsingTools.getIntAttribute(conditionAttrs,AchievablesStatusXMLConstants.CONDITION_STATUS_INDEX_ATTR,-1);
      ObjectiveConditionStatus conditionStatus=objectiveStatus.getConditionStatus(conditionIndex);
      if (conditionStatus==null)
      {
        LOGGER.warn("Condition not found: objective index="+objectiveStatus.getObjective().getIndex()+" - condition index="+conditionIndex);
        continue;
      }
      // State
      String stateStr=DOMParsingTools.getStringAttribute(conditionAttrs,AchievablesStatusXMLConstants.CONDITION_STATUS_STATE_ATTR,null);
      AchievableElementState state=parseState(stateStr);
      conditionStatus.setState(state);
      // Count
      int countValue=DOMParsingTools.getIntAttribute(conditionAttrs,AchievablesStatusXMLConstants.CONDITION_STATUS_COUNT_ATTR,-1);
      Integer count=(countValue>=0)?Integer.valueOf(countValue):null;
      conditionStatus.setCount(count);
      // Keys
      String keys=DOMParsingTools.getStringAttribute(conditionAttrs,AchievablesStatusXMLConstants.CONDITION_STATUS_KEYS_ATTR,null);
      if (keys!=null)
      {
        String[] keysArray=keys.split(",");
        for(String key : keysArray)
        {
          conditionStatus.addKey(key);
        }
      }
    }
  }

  private AchievableElementState parseState(String stateStr)
  {
    AchievableElementState ret=null;
    if (stateStr!=null)
    {
      try
      {
        ret=AchievableElementState.valueOf(stateStr);
      }
      catch(Exception e)
      {
        // Ignored
      }
    }
    if (ret==null)
    {
      ret=AchievableElementState.UNDEFINED;
    }
    return ret;
  }
}
