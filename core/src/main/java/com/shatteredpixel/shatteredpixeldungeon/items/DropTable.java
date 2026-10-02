/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//per-run set of items excluded from the random drop table, configured before the run enters floor 1
public class DropTable {

    private static HashSet<Class<? extends Item>> disabled = new HashSet<>();

    //max fraction of a group that may be disabled; safety rests on the n-2 ceiling, not this value
    public static float DISABLE_RATIO = 0.25f;

    public static final Generator.Category[] GROUPS = {
            Generator.Category.WEP_T1,
            Generator.Category.WEP_T2,
            Generator.Category.WEP_T3,
            Generator.Category.WEP_T4,
            Generator.Category.WEP_T5,
            Generator.Category.RING
    };

    public static boolean isDisabled(Class<?> cls) {
        return disabled.contains(cls);
    }

    //only naturally-dropping entries are toggleable
    public static List<Class<?>> droppable(Generator.Category cat) {
        ArrayList<Class<?>> result = new ArrayList<>();
        if (cat == null || cat.classes == null || cat.probs == null) return result;
        for (int i = 0; i < cat.probs.length && i < cat.classes.length; i++) {
            if (cat.probs[i] > 0) result.add(cat.classes[i]);
        }
        return result;
    }

    public static int disabledCount(Generator.Category cat) {
        int count = 0;
        for (Class<?> cls : droppable(cat)) {
            if (disabled.contains(cls)) count++;
        }
        return count;
    }

    public static int disableCap(Generator.Category cat) {
        int n = droppable(cat).size();
        if (n <= 2) return 0;
        int cap = (int) (n * DISABLE_RATIO);
        cap = Math.max(cap, 1);
        //n-2, not n-1: reroll loops that reject the input item's own class need a second survivor to terminate
        cap = Math.min(cap, n - 2);
        return cap;
    }

    public static boolean canDisable(Generator.Category cat) {
        return disabledCount(cat) < disableCap(cat);
    }

    public static int totalDroppable() {
        int total = 0;
        for (Generator.Category cat : GROUPS) {
            total += droppable(cat).size();
        }
        return total;
    }

    public static int totalDisabled() {
        int total = 0;
        for (Generator.Category cat : GROUPS) {
            total += disabledCount(cat);
        }
        return total;
    }

    private static Generator.Category groupOf(Class<?> cls) {
        for (Generator.Category cat : GROUPS) {
            if (droppable(cat).contains(cls)) return cat;
        }
        return null;
    }

    //enabled=false disables the item; no-op once locked, or if the group's cap is already reached
    public static void set(Class<? extends Item> cls, boolean enabled) {
        if (cls == null || locked()) return;
        if (enabled) {
            disabled.remove(cls);
        } else {
            Generator.Category cat = groupOf(cls);
            if (cat == null) return;
            if (disabled.contains(cls)) return;
            if (!canDisable(cat)) return;
            disabled.add(cls);
        }
    }

    public static void reset() {
        disabled.clear();
    }

    public static boolean locked() {
        return Dungeon.levelHasBeenGenerated(1, 0);
    }

    //returns probs by identity when nothing is masked, keeping an unmodified run bit-identical to vanilla
    public static float[] mask(Generator.Category cat, float[] probs) {
        if (disabled.isEmpty() || cat == null || probs == null) return probs;
        float[] masked = null;
        for (int i = 0; i < probs.length && i < cat.classes.length; i++) {
            if (probs[i] > 0 && disabled.contains(cat.classes[i])) {
                if (masked == null) masked = probs.clone();
                masked[i] = 0;
            }
        }
        return masked == null ? probs : masked;
    }

    private static final String DISABLED = "droptable_disabled";

    public static void storeInBundle(Bundle b) {
        if (disabled.isEmpty()) return;
        b.put(DISABLED, disabled.toArray(new Class<?>[0]));
    }

    //re-establishes the group-membership and cap invariants set() enforces; a save from another
    //version may name a class that has since left its group, or hold more than the current cap
    @SuppressWarnings("unchecked")
    public static void restoreFromBundle(Bundle b) {
        disabled.clear();
        if (!b.contains(DISABLED)) return;
        Class<?>[] stored = b.getClassArray(DISABLED);
        if (stored == null) return;
        for (Class<?> cls : stored) {
            if (cls == null || !Item.class.isAssignableFrom(cls)) continue;
            Generator.Category cat = groupOf(cls);
            if (cat == null || disabledCount(cat) >= disableCap(cat)) continue;
            disabled.add((Class<? extends Item>) cls);
        }
    }
}
