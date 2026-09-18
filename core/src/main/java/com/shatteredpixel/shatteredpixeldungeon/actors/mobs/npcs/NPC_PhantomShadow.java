package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCat;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.PhantomCatQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_PhantomShadowSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class NPC_PhantomShadow extends NPC {
    {
        spriteClass = NPC_PhantomShadowSprite.class;
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

        if (c != Dungeon.hero) {
            return super.interact(c);
        }

        //stumbling on the cat first also starts the quest; don't build one Phantom already registered
        PhantomCatQuestLine q = Quests.get(PhantomCatQuestLine.class);
        if (q == null) {
            q = new PhantomCatQuestLine();
            Quests.add(q);
        }

        //picking her up re-accepts a quest that was declined before she was ever found
        q.reopen();

        //only hand her over while the quest is actually looking for her; otherwise leave her be
        if (q.at(PhantomCatQuestLine.STEP_FIND)) {
            QuestCat result = new QuestCat();
            if (result.doPickUp(Dungeon.hero)) {
                GLog.i(Messages.get(Dungeon.hero, "you_now_have", result.name()));
            } else {
                Dungeon.level.drop(result, this.pos).sprite.drop();
            }
            q.advance();
            die(this);
        }
        return true;
    }

    public static void spawn(Level level, int a) {
        int ppos = Random.Int(3);
        switch (ppos) {
            case 0: default:
                if (a ==  0) ppos = 2999;
                else if (a ==  1) ppos = 151;
                else if (a ==  2) ppos = 1140;
                break;
            case 1:
                if (a ==  0) ppos = 4220;
                else if (a ==  1) ppos = 618;
                else if (a ==  2) ppos = 744;
                break;
            case 2:
                if (a ==  0) ppos = 3689;
                else if (a ==  1) ppos = 273;
                else if (a ==  2) ppos = 847;
                break;
        }
        NPC_PhantomShadow cat = new NPC_PhantomShadow();
        do {
            cat.pos = ppos;
        } while (cat.pos == -1);
        level.mobs.add(cat);
    }
}
