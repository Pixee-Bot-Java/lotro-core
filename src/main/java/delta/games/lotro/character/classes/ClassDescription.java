package delta.games.lotro.character.classes;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.CharacterClass;

/**
 * Class description.
 * @author DAM
 */
public class ClassDescription
{
  private CharacterClass _class;
  private int _iconId;
  private int _smallIconId;
  private String _abbreviation;
  private String _description;
  private String _tacticalDpsStatName;
  private List<ClassTrait> _traits;
  private TraitTree _traitTree;
  private List<ClassSkill> _skills;

  /**
   * Constructor.
   * @param characterClass Character class.
   */
  public ClassDescription(CharacterClass characterClass)
  {
    _class=characterClass;
    _iconId=0;
    _smallIconId=0;
    _traits=new ArrayList<ClassTrait>();
    _skills=new ArrayList<ClassSkill>();
  }

  /**
   * Get the described character class.
   * @return a character class.
   */
  public CharacterClass getCharacterClass()
  {
    return _class;
  }

  /**
   * Get the ID of the icon for this class.
   * @return an icon ID.
   */
  public int getIconId()
  {
    return _iconId;
  }

  /**
   * Set the ID of the icon for this class.
   * @param iconId the icon ID to set.
   */
  public void setIconId(int iconId)
  {
    _iconId=iconId;
  }

  /**
   * Get the ID of the small icon for this class.
   * @return an icon ID.
   */
  public int getSmallIconId()
  {
    return _smallIconId;
  }

  /**
   * Set the ID of the small icon for this class.
   * @param smallIconId the icon ID to set.
   */
  public void setSmallIconId(int smallIconId)
  {
    _smallIconId=smallIconId;
  }

  /**
   * Get the abbreviation for this class.
   * @return an abbreviation.
   */
  public String getAbbreviation()
  {
    return _abbreviation;
  }

  /**
   * Set the abbreviation for this class.
   * @param abbreviation Abbreviation to set.
   */
  public void setAbbreviation(String abbreviation)
  {
    _abbreviation=abbreviation;
  }

  /**
   * Get the description for this class.
   * @return a description.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description for this class.
   * @param description Description to set.
   */
  public void setDescription(String description)
  {
    _description=description;
  }

  /**
   * Get the name of the tactical DPS stat.
   * @return a stat name
   */
  public String getTacticalDpsStatName()
  {
    return _tacticalDpsStatName;
  }

  /**
   * Set the name of the tactical DPS stat.
   * @param tacticalDpsStatName name to use.
   */
  public void setTacticalDpsStatName(String tacticalDpsStatName)
  {
    _tacticalDpsStatName=tacticalDpsStatName;
  }

  /**
   * Add a class trait.
   * @param trait Trait to add.
   */
  public void addTrait(ClassTrait trait)
  {
    _traits.add(trait);
  }

  /**
   * Get all the traits of this class.
   * @return A list of class traits.
   */
  public List<ClassTrait> getTraits()
  {
    return _traits;
  }

  /**
   * Get the class traits for a given character level.
   * @param level Character level.
   * @return A possibly empty but not <code>null</code> list of traits.
   */
  public List<TraitDescription> getTraitsForLevel(int level)
  {
    List<TraitDescription> traits=new ArrayList<TraitDescription>();
    for(ClassTrait classTrait : _traits)
    {
      int requiredLevel=classTrait.getRequiredLevel();
      if (level>=requiredLevel)
      {
        traits.add(classTrait.getTrait());
      }
    }
    return traits;
  }

  /**
   * Get the trait tree for this class.
   * @return a trait tree.
   */
  public TraitTree getTraitTree()
  {
    return _traitTree;
  }

  /**
   * Set the trait tree for this class.
   * @param traitTree Trait tree to set.
   */
  public void setTraitTree(TraitTree traitTree)
  {
    _traitTree=traitTree;
  }

  /**
   * Add a class skill.
   * @param skill Skill to add.
   */
  public void addSkill(ClassSkill skill)
  {
    _skills.add(skill);
  }

  /**
   * Get all the skills of this class.
   * @return A list of class skills.
   */
  public List<ClassSkill> getSkills()
  {
    return _skills;
  }
}
