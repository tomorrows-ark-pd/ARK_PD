package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.TutorialQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_mageSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

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

        TutorialQuestLine q = Quests.get(TutorialQuestLine.class);

        //quest already given this run (in any state): completed/abandoned both gate for the run
        if (q != null) {
            if (q.ongoing()) {
                tell(q.objectiveDesc());   //reminder of the current objective
            } else {
                tell(Messages.get(this, "say2"));
            }
            return true;
        }

        //auto-start: create the questline and chain the intro into the go-see-Closure text
        final TutorialQuestLine quest = new TutorialQuestLine();
        Quests.add(quest);

        final String intro = Messages.get(this, "say");
        final String next = Messages.get(this, "quest2");
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(NPC_Mage.this, intro) {
                    @Override
                    public void hide() {
                        super.hide();
                        GameScene.show(new WndQuest(NPC_Mage.this, next));
                    }
                });
            }
        });
        return true;
    }

    private void tell(final String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(NPC_Mage.this, text));
            }
        });
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
