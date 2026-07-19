package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.TutorialQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClosureSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Closure extends NPC {

    private static final String[] LINE_KEYS = {"free1", "free2", "free3"};

    {
        spriteClass = ClosureSprite.class;
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

        //tutorial step 0: chains into the Purestream objective.
        TutorialQuestLine q = Quests.get(TutorialQuestLine.class);
        if (q != null && q.at(0)) {
            q.advance();
            final String done = Messages.get(this, "quest_done");
            final String next = Messages.get(this, "quest_next");
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Closure.this, done) {
                        @Override
                        public void hide() {
                            super.hide();
                            GameScene.show(new WndQuest(Closure.this, next));
                        }
                    });
                }
            });
            return true;
        }

        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, Random.element(LINE_KEYS)));
        return true;
    }

    public static void spawn(Level level, int ppos) {
        Closure npc = new Closure();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }

}
