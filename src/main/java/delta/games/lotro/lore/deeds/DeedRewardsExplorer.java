package delta.games.lotro.lore.deeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;

/**
 * Tool to explore deed rewards.
 * @author DAM
 */
public class DeedRewardsExplorer
{
  private Set<String> _emotes;
  private Set<String> _titles;
  private Set<Integer> _itemIds;
  private List<Item> _items;
  private Set<String> _skills;
  private Set<String> _traits;

  /**
   * Constructor.
   */
  public DeedRewardsExplorer()
  {
    _titles=new HashSet<String>();
    _itemIds=new HashSet<Integer>();
    _items=new ArrayList<Item>();
    _emotes=new HashSet<String>();
    _skills=new HashSet<String>();
    _traits=new HashSet<String>();
  }

  /**
   * Perform exploration.
   * @param deeds Deeds to use.
   */
  public void doIt(List<DeedDescription> deeds)
  {
    for(DeedDescription deed : deeds)
    {
      exploreDeed(deed);
    }
    resolveItems();
    //showResults();
  }

  private void exploreDeed(DeedDescription deed)
  {
    Rewards rewards=deed.getRewards();
    // Emotes
    Emote[] emotes=rewards.getEmotes();
    if (emotes!=null)
    {
      for(Emote emote : emotes)
      {
        String emoteName=emote.getName();
        _emotes.add(emoteName);
      }
    }
    // Skills
    Skill[] skills=rewards.getSkills();
    if (skills!=null)
    {
      for(Skill skill : skills)
      {
        String skillName=skill.getName();
        _skills.add(skillName);
      }
    }
    // Titles
    Title[] titles=rewards.getTitles();
    if (titles!=null)
    {
      for(Title title : titles)
      {
        String titleName=title.getName();
        _titles.add(titleName);
      }
    }
    // Traits
    Trait[] traits=rewards.getTraits();
    if (traits!=null)
    {
      for(Trait trait : traits)
      {
        String traitName=trait.getName();
        _traits.add(traitName);
      }
    }
    // Items
    ObjectsSet objects=rewards.getObjects();
    int nbItems=objects.getNbObjectItems();
    for(int i=0;i<nbItems;i++)
    {
      ObjectItem object=objects.getItem(i);
      int id=object.getItemId();
      if (id!=0)
      {
        _itemIds.add(Integer.valueOf(id));
      }
    }
  }

  private void resolveItems()
  {
    ItemsManager items=ItemsManager.getInstance();
    for(Integer id : _itemIds)
    {
      Item item=items.getItem(id.intValue());
      if (item!=null)
      {
        _items.add(item);
      }
    }
  }

  /*
  private void showResults()
  {
    showList("Emotes",_emotes);
    showList("Items",_itemNames);
    showList("Skills",_skills);
    showList("Titles",_titles);
    showList("Traits",_traits);
  }

  private void showList(String category, Set<String> items)
  {
    System.out.println(category+" ("+items.size()+")");
    List<String> itemsList=new ArrayList<String>(items);
    Collections.sort(itemsList);
    for(String item : itemsList)
    {
      System.out.println("\t"+item);
    }
  }
  */

  /**
   * Get all traits.
   * @return a sorted list of traits.
   */
  public List<String> getTraits()
  {
    List<String> ret=new ArrayList<String>(_traits);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get all skills.
   * @return a sorted list of skills.
   */
  public List<String> getSkills()
  {
    List<String> ret=new ArrayList<String>(_skills);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get all titles.
   * @return a sorted list of titles.
   */
  public List<String> getTitles()
  {
    List<String> ret=new ArrayList<String>(_titles);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get all emotes.
   * @return a sorted list of emotes.
   */
  public List<String> getEmotes()
  {
    List<String> ret=new ArrayList<String>(_emotes);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get all items.
   * @return a list of items, sorted by name.
   */
  public List<Item> getItems()
  {
    List<Item> ret=new ArrayList<Item>(_items);
    Collections.sort(ret,new ItemNameComparator());
    return ret;
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    new DeedRewardsExplorer().doIt(deeds);
  }
}
