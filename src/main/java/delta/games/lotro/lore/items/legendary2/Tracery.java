package delta.games.lotro.lore.items.legendary2;

import delta.common.utils.NumericTools;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.common.Named;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.lore.items.Item;

/**
 * Tracery.
 * @author DAM
 */
public class Tracery implements Identifiable,Named
{
  /**
   * Tier pattern.
   */
  private static final String TIER_PATTERN=":Tier";

  private Item _item;
  private SocketType _type;
  private Integer _tier;
  private int _minItemLevel;
  private int _maxItemLevel;
  private int _increment;

  /**
   * Constructor.
   * @param item Associated item.
   * @param type Socket type.
   * @param minItemLevel Minimum item level.
   * @param maxItemLevel Maximum item level.
   * @param increment Level up increment.
   */
  public Tracery(Item item, SocketType type, int minItemLevel, int maxItemLevel, int increment)
  {
    _item=item;
    _type=type;
    initTier();
    _minItemLevel=minItemLevel;
    _maxItemLevel=maxItemLevel;
    _increment=increment;
  }

  @Override
  public int getIdentifier()
  {
    return (_item!=null)?_item.getIdentifier():0;
  }

  /**
   * Get the name of this tracery.
   * @return a name.
   */
  public String getName()
  {
    return (_item!=null)?_item.getName():"";
  }

  /**
   * Get the associated item.
   * @return the associated item.
   */
  public Item getItem()
  {
    return _item;
  }

  /**
   * Get the socket type.
   * @return a socket type.
   */
  public SocketType getType()
  {
    return _type;
  }

  /**
   * Get the tier of this tracery.
   * @return A tier or <code>null</code> if none.
   */
  public Integer getTier()
  {
    return _tier;
  }

  private void initTier()
  {
    if (_item==null)
    {
      return;
    }
    String category=_item.getSubCategory();
    if (category!=null)
    {
      int index=category.indexOf(TIER_PATTERN);
      if (index!=-1)
      {
        String tierStr=category.substring(index+TIER_PATTERN.length());
        _tier=NumericTools.parseInteger(tierStr);
      }
    }
  }

  /**
   * Get the minimum item level.
   * @return an item level.
   */
  public int getMinItemLevel()
  {
    return _minItemLevel;
  }

  /**
   * Get the maximum item level.
   * @return an item level.
   */
  public int getMaxItemLevel()
  {
    return _maxItemLevel;
  }

  /**
   * Get the level-up increment.
   * @return level-up increment.
   */
  public int getLevelUpIncrement()
  {
    return _increment;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder("Tracery ID=").append(getIdentifier());
    sb.append(", name=").append(getName());
    sb.append(", type=").append(_type);
    sb.append(", min Item Level=").append(_minItemLevel);
    sb.append(", max Item Level=").append(_maxItemLevel);
    sb.append(", increment=").append(_increment);
    if (_item!=null)
    {
      sb.append(", min Char Level=").append(_item.getMinLevel());
      sb.append(", max Char Level=").append(_item.getMaxLevel());
    }
    return sb.toString();
  }
}
