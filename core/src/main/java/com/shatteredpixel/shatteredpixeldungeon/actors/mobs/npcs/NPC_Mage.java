package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Delivery_dronSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_AstesiaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_mageSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PRTS_Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Pink_doggiSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class NPC_Mage extends NPC {
    {
        spriteClass = NPC_mageSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);
        sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "info1"));

        return true;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    public static void spawn(Level level, int ppos) {
        NPC_Mage npc = new NPC_Mage();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }

}
