package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCat;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.PhantomCatQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_PhantomSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;

import java.util.Arrays;

public class NPC_Phantom extends NPC {
    {
        spriteClass = NPC_PhantomSprite.class;
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

        PhantomCatQuestLine q = Quests.get(PhantomCatQuestLine.class);

        if (q == null) {
            //pre-port save: a cat already in the pack would otherwise be stranded, so adopt it
            q = Dungeon.hero.belongings.getItem(QuestCat.class) != null
                    ? PhantomCatQuestLine.adoptOrphanCat()
                    : new PhantomCatQuestLine();
            Quests.add(q);
        }

        //he handed out step 0. Step 1's giver is the shadow, gone once she hands the cat over, so it falls back to him.
        //no early return, so a resume at the turn-in step still pays out in the same conversation
        boolean cameHome = q.resumeAt(PhantomCatQuestLine.STEP_RETURN);
        q.resumeAt(PhantomCatQuestLine.STEP_FIND);

        if (!q.ongoing()) {
            sprite.showStatus(CharSprite.NEUTRAL, "?");
            return true;
        }

        if (DeviceCompat.isDebug() && q.at(PhantomCatQuestLine.STEP_FIND)) logWhereSheIs();

        //capture before the turn-in: he owes the premise even when the payoff lands in the same breath
        boolean firstBriefing = q.brief();

        if (!q.tryTurnIn()) {
            tell(Messages.get(this, "quest"));   //offer, or still looking for her
            return true;
        }

        //abandoning after finding her took her from the pack, so she is told as having walked home herself
        String payoff = Messages.get(this, cameHome ? "result_returned" : firstBriefing ? "result_early" : "result");
        //never met him before: premise first, then the payoff
        if (firstBriefing) tell(Messages.get(this, "quest"), payoff);
        else tell(payoff);
        return true;
    }

    //debug builds only: her cell is rolled at generation and never stored, so name the floor and candidates
    private static void logWhereSheIs() {
        int point = Dungeon.QuestCatPoint;
        int branch = point + 2;
        boolean generated = Dungeon.levelHasBeenGenerated(0, branch);
        GLog.w("[DEBUG] cat: Rhodes " + branch + ", cell one of " + Arrays.toString(NPC_PhantomShadow.candidateCells(point))
                + " | floor generated: " + generated + " | spawn gate open: " + PhantomCatQuestLine.catStillOut());

        if (Dungeon.branch != branch) return;
        for (Mob m : Dungeon.level.mobs) {
            if (!(m instanceof NPC_PhantomShadow)) continue;
            int w = Dungeon.level.width();
            int dx = m.pos % w - Dungeon.hero.pos % w, dy = m.pos / w - Dungeon.hero.pos / w;
            GLog.w("[DEBUG] she is on this floor at cell " + m.pos + ": " + dx + " x, " + dy + " y from you");
            return;
        }
        GLog.w("[DEBUG] this is her floor but she is not here (already picked up, or never spawned)");
    }

    //shows each text in turn, the next opening as the previous is dismissed
    private void tell(final String... texts) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                showFrom(texts, 0);
            }
        });
    }

    private void showFrom(final String[] texts, final int i) {
        if (i >= texts.length) return;
        GameScene.show(new WndQuest(NPC_Phantom.this, texts[i]) {
            @Override
            public void hide() {
                super.hide();
                showFrom(texts, i + 1);
            }
        });
    }

    public static void spawn(Level level, int ppos) {
        NPC_Phantom cat = new NPC_Phantom();
        do {
            cat.pos = ppos;
        } while (cat.pos == -1);
        level.mobs.add(cat);
    }
}
