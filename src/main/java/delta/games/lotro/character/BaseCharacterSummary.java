package delta.games.lotro.character;

import delta.games.lotro.account.AccountReference;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;

/**
 * Base class for LOTRO character summaries.
 * @author DAM
 */
public class BaseCharacterSummary extends CharacterReference implements BasicCharacterAttributes
{
  private String _server;
  private AccountReference _accountID;
  private CharacterSex _sex;
  private RaceDescription _race;

  /**
   * Constructor.
   */
  public BaseCharacterSummary()
  {
    super();
    _server="";
    _accountID=null;
    _race=null;
  }

  /**
   * Copy constructor.
   * @param source Source character.
   */
  public BaseCharacterSummary(BaseCharacterSummary source)
  {
    super(source);
    _server=source._server;
    _accountID=source._accountID;
    _sex=source._sex;
    _race=source._race;
  }

  /**
   * Get the character's server.
   * @return the character's server.
   */
  public String getServer()
  {
    return _server;
  }

  /**
   * Set the character's server.
   * @param server the server to set. 
   */
  public void setServer(String server)
  {
    if (server==null)
    {
      server="";
    }
    _server=server;
  }

  /**
   * Get the account ID of this character.
   * @return an account ID.
   */
  public AccountReference getAccountID()
  {
    return _accountID;
  }

  /**
   * Set the character's account.
   * @param accountID external ID of the account. 
   */
  public void setAccountID(AccountReference accountID)
  {
    _accountID=accountID;
  }

  /**
   * Get the character's sex.
   * @return the character's sex.
   */
  public CharacterSex getCharacterSex()
  {
    return _sex;
  }

  /**
   * Set the character's sex.
   * @param characterSex the sex to set.
   */
  public void setCharacterSex(CharacterSex characterSex)
  {
    _sex=characterSex;
  }

  /**
   * Get the character's race.
   * @return the character's race.
   */
  public RaceDescription getRace()
  {
    return _race;
  }

  /**
   * Set the character's race.
   * @param race the race to set.
   */
  public void setRace(RaceDescription race)
  {
    _race=race;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(super.toString());
    sb.append("Server [").append(_server).append("], ");
    sb.append("Account [").append(_accountID).append("], ");
    sb.append("Race [").append(_race).append("], ");
    sb.append("Sex [").append(_sex).append("], ");
    sb.append("Race [").append(_race).append("], ");
    return sb.toString();
  }
}
