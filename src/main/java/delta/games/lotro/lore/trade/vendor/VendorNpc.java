package delta.games.lotro.lore.trade.vendor;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.npc.NpcDescription;
import delta.games.lotro.utils.Proxy;

/**
 * Vendor aspect of an NPC.
 * @author DAM
 */
public class VendorNpc implements Identifiable
{
  // Parent NPC
  private NpcDescription _npc;
  private List<SellList> _sellLists;
  private boolean _buys;
  private List<Integer> _discounts;
  private boolean _sellsWearableItems;
  private float _sellFactor;

  /**
   * Constructor.
   * @param npc Associated NPC.
   */
  public VendorNpc(NpcDescription npc)
  {
    _npc=npc;
    _sellLists=new ArrayList<SellList>();
    _buys=false;
    _discounts=new ArrayList<Integer>();
    _sellsWearableItems=false;
    _sellFactor=1;
  }

  /**
   * Get the identifier of the parent NPC.
   * @return a NPC identifier.
   */
  public int getIdentifier()
  {
    return _npc.getIdentifier();
  }

  /**
   * Get the associated NPC.
   * @return a NPC.
   */
  public NpcDescription getNpc()
  {
    return _npc;
  }

  /**
   * Indicates if this vendor does sell the specified item.
   * @param itemId Item identifier.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean sells(int itemId)
  {
    for(SellList list : _sellLists)
    {
      for(Proxy<Item> item : list.getItems())
      {
        if (item.getId()==itemId)
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Add a sell list.
   * @param sellList List to add.
   */
  public void addSellList(SellList sellList)
  {
    _sellLists.add(sellList);
  }

  /**
   * Get the sell lists.
   * @return a list of sell lists.
   */
  public List<SellList> getSellLists()
  {
    return _sellLists;
  }

  /**
   * Indicates if this vendor also buys items.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean buys()
  {
    return _buys;
  }

  /**
   * Set the value of the 'buys' flag.
   * @param buys Value to set.
   */
  public void setBuys(boolean buys)
  {
    _buys=buys;
  }

  /**
   * Add a discount.
   * @param discountId Discount to add.
   */
  public void addDiscount(int discountId)
  {
    _discounts.add(Integer.valueOf(discountId));
  }

  /**
   * Get the discounts.
   * @return a list discounts.
   */
  public List<Integer> getDiscounts()
  {
    return _discounts;
  }

  /**
   * Indicates if this vendor sells wearable items.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean sellsWearableItems()
  {
    return _sellsWearableItems;
  }

  /**
   * Set the value of the 'sells wearable items' flag.
   * @param sellsWearableItems Value to set.
   */
  public void setSellsWearableItems(boolean sellsWearableItems)
  {
    _sellsWearableItems=sellsWearableItems;
  }

  /**
   * Get the sell factor.
   * @return a factor.
   */
  public float getSellFactor()
  {
    return _sellFactor;
  }

  /**
   * Set the sell factor.
   * @param sellFactor Factor to set.
   */
  public void setSellFactor(float sellFactor)
  {
    _sellFactor=sellFactor;
  }

  /**
   * Dump the contents of this NPC barter data as a readable string.
   * @return A displayable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_npc).append(EndOfLine.NATIVE_EOL);
    sb.append("buys=").append(_buys);
    sb.append(", sells wearable items=").append(_sellsWearableItems);
    sb.append(", sell factor=").append(_sellFactor);
    sb.append(", discounts=").append(_discounts);
    sb.append(EndOfLine.NATIVE_EOL);
    for(SellList sellList : _sellLists)
    {
      for(Proxy<Item> entry : sellList.getItems())
      {
        sb.append("\t\t").append(entry).append(EndOfLine.NATIVE_EOL);
      }
    }
    return sb.toString().trim();
  }
}
