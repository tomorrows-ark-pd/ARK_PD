package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCat;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.PhantomCatQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
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

        //she is the step's target, not its giver: a declined quest waits until Phantom takes it back up
        if (!q.at(PhantomCatQuestLine.STEP_FIND)) {
            sprite.showStatus(CharSprite.NEUTRAL, "...");
            return true;
        }

        QuestCat result = new QuestCat();
        if (result.doPickUp(Dungeon.hero)) {
            GLog.i(Messages.get(Dungeon.hero, "you_now_have", result.name()));
        } else {
            Dungeon.level.drop(result, this.pos).sprite.drop();
        }
        q.advance();
        die(this);
        return true;
    }

    //candidate cells per Dungeon.QuestCatPoint (0 = Rhodes 2, 1 = Rhodes 3, 2 = Rhodes 4); one is rolled at generation
    private static final int[][] CELLS = {
            {2999, 4220, 3689},
            {151, 618, 273},
            {1140, 744, 847},
    };

    public static int[] candidateCells(int point) {
        return CELLS[point].clone();
    }

    public static void spawn(Level level, int point) {
        NPC_PhantomShadow cat = new NPC_PhantomShadow();
        cat.pos = CELLS[point][Random.Int(3)];
        level.mobs.add(cat);
    }
}
