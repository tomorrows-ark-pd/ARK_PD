package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.TutorialQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_PurestreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Purestream extends NPC {
    {
        //placeholder sprite
        spriteClass = NPC_PurestreamSprite.class;
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

        //tutorial step 1: chains into the next objective.
        TutorialQuestLine q = Quests.get(TutorialQuestLine.class);
        if (q != null && q.at(1)) {
            q.advance();
            final String done = Messages.get(this, "quest_done");
            final String next = Messages.get(this, "quest_next");
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Purestream.this, done) {
                        @Override
                        public void hide() {
                            super.hide();
                            GameScene.show(new WndQuest(Purestream.this, next));
                        }
                    });
                }
            });
            return true;
        }

        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "idle"));
        return true;
    }

    public static void spawn(Level level, int ppos) {
        Purestream npc = new Purestream();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }

}
