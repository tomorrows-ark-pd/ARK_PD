package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class NervousPotion extends Item {
    public static final String AC_DRINK = "DRINK";
    {
        image = ItemSpriteSheet.TYLENOL;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DRINK);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_DRINK)) {
            if (hero.buff(NervousImpairment.class) != null) {
                hero.buff((NervousImpairment.class)).sum(-50);
            }
            this.detach(curUser.belongings.backpack);
            Sample.INSTANCE.play(Assets.Sounds.DRINK);
        }
    }


    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
