package delta.games.lotro.lore.quests.geo.io.xml;

import java.awt.geom.Point2D;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.games.lotro.lore.quests.geo.AchievableGeoPoint;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;

/**
 * Writer for achievable geo data in XML.
 * @author DAM
 */
public class AchievableGeoDataXMLWriter
{
  /**
   * Write objective condition geo datas.
   * @param hd Output transformer.
   * @param condition Objective condition.
   * @throws SAXException If an error occurs.
   */
  public static void writeObjectiveConditionGeoData(TransformerHandler hd, ObjectiveCondition condition) throws SAXException
  {
    List<AchievableGeoPoint> points=condition.getPoints();
    if (!points.isEmpty())
    {
      for(AchievableGeoPoint point : points)
      {
        AttributesImpl pointAttrs=new AttributesImpl();
        // DID
        int did=point.getDataId();
        if (did>0)
        {
          pointAttrs.addAttribute("","",AchievableGeoDataXMLConstants.POINT_DID_ATTR,XmlWriter.CDATA,String.valueOf(did));
        }
        // Key
        String key=point.getKey();
        if (key!=null)
        {
          pointAttrs.addAttribute("","",AchievableGeoDataXMLConstants.POINT_KEY_ATTR,XmlWriter.CDATA,key);
        }
        // Map index
        int mapIndex=point.getMapIndex();
        if (mapIndex>0)
        {
          pointAttrs.addAttribute("","",AchievableGeoDataXMLConstants.POINT_MAP_INDEX_ATTR,XmlWriter.CDATA,String.valueOf(mapIndex));
        }
        // Position
        Point2D.Float lonLat=point.getLonLat();
        pointAttrs.addAttribute("","",AchievableGeoDataXMLConstants.POINT_LONGITUDE_ATTR,XmlWriter.CDATA,String.valueOf(lonLat.x));
        pointAttrs.addAttribute("","",AchievableGeoDataXMLConstants.POINT_LATITUDE_ATTR,XmlWriter.CDATA,String.valueOf(lonLat.y));
        hd.startElement("","",AchievableGeoDataXMLConstants.POINT_TAG,pointAttrs);
        hd.endElement("","",AchievableGeoDataXMLConstants.POINT_TAG);
      }
    }
  }
}
