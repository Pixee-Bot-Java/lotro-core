package delta.games.lotro.character.titles;

import java.util.Date;

import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Title status.
 * @author DAM
 */
public class TitleStatus
{
  private TitleDescription _title;
  private Long _acquisitionDate;

  /**
   * Constructor.
   * @param title Associated title.
   */
  public TitleStatus(TitleDescription title)
  {
    if (title==null)
    {
      throw new IllegalArgumentException("title is null");
    }
    _title=title;
    _acquisitionDate=null;
  }

  /**
   * Get the associated title.
   * @return the associated title.
   */
  public TitleDescription getTitle()
  {
    return _title;
  }

  /**
   * Get the identifier of the managed title.
   * @return the identifier of the managed title.
   */
  public int getTitleId()
  {
    return _title.getIdentifier();
  }

  /**
   * Get the completion date.
   * @return A date or <code>null</code> if completion date is not known.
   */
  public Long getAcquisitionDate()
  {
    return _acquisitionDate;
  }

  /**
   * Set the acquisition date.
   * @param acquisitionDate Acquisition date to set.
   */
  public void setAcquisitionDate(Long acquisitionDate)
  {
    _acquisitionDate=acquisitionDate;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    int titleId=_title.getIdentifier();
    sb.append("Title ").append(_title.getName()).append(" (").append(titleId).append("): ");
    if (_acquisitionDate!=null)
    {
      sb.append(" (").append(new Date(_acquisitionDate.longValue())).append(')');
    }
    return sb.toString();
  }
}
