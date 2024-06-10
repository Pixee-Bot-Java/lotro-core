package delta.games.lotro.lore.items.effects;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.EffectDisplay;
import delta.games.lotro.common.effects.EffectGenerator;

/**
 * Utility methods related to effects display.
 * @author DAM
 */
public class DisplayEffectsUtils
{
  /**
   * Show an effect generator.
   * @param storage Storage for generated strings.
   * @param generator Generator to use.
   * @param skipRawStats Indicates if stats shall be skipped or not.
   * @param level Level of effect.
   */
  public static void showEffectGenerator(List<String> storage, EffectGenerator generator, boolean skipRawStats, int level)
  {
    Effect effect=generator.getEffect();
    Float spellcraft=generator.getSpellcraft();
    if (spellcraft!=null)
    {
      level=spellcraft.intValue();
    }
    showEffect(storage,effect,level,skipRawStats);
  }

  private static void showEffect(List<String> storage, Effect effect, int level, boolean skipRawStats)
  {
    EffectDisplay display=new EffectDisplay(level);
    display.skipRawStats(skipRawStats);
    List<String> childStorage=new ArrayList<String>();
    display.displayEffect(childStorage,effect);
    if (!childStorage.isEmpty())
    {
      storage.addAll(childStorage);
    }
  }
}
