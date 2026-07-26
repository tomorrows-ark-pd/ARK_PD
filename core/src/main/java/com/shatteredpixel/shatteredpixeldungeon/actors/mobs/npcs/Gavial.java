package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Dobermann_shadowSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_GavialSprite;

public class Gavial extends NPC {
    {
        spriteClass = NPC_GavialSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);
        if (Dungeon.depth > 30) {
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "questhint"));
        }
        else {

            if (Dungeon.extrastage_Gavial) {
                Dungeon.extrastage_Gavial = false;
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "no"));
            } else {
                Dungeon.extrastage_Gavial = true;
                Dungeon.extrastage_Sea = false;
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "ok"));
            }
        }
        return true;
    }

    @Override
    public String description() {
        if (Dungeon.depth > 30)    return Messages.get(this, "desc_31");
        return Messages.get(this, "desc");
    }

    public static void spawn(Level level, int ppos) {
        Gavial perro = new Gavial();
        do {
            perro.pos = ppos;
        } while (perro.pos == -1);
        level.mobs.add(perro);
    }
}
