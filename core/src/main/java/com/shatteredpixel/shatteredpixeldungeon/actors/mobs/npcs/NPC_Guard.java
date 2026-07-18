package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCargo;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.TutorialQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_guardSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class NPC_Guard extends NPC {
    {
        spriteClass = NPC_guardSprite.class;
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

        TutorialQuestLine q = Quests.get(TutorialQuestLine.class);

        //tutorial step 2: hand out the cargo and advance to the delivery step (no reward)
        if (q != null && q.at(2)) {
            giveCargo();
            q.advance();
            tell(Messages.get(this, "say") + "\n\n" + Messages.get(this, "quest"));
            return true;
        }

        //tutorial step 3 (delivery): re-give the cargo if it was lost, else just remind
        if (q != null && q.at(3)) {
            if (Dungeon.hero.belongings.getItem(QuestCargo.class) == null) {
                giveCargo();
            }
            tell(Messages.get(this, "quest"));
            return true;
        }

        //quest not at this NPC's step: idle status line ("too early")
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "hey"));
        return true;
    }

    private void giveCargo() {
        QuestCargo cargo = new QuestCargo();
        if (!cargo.collect()) {
            Dungeon.level.drop(cargo, Dungeon.hero.pos);
        }
    }

    private void tell(final String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(NPC_Guard.this, text));
            }
        });
    }

    public static void spawn(Level level, int ppos) {
        NPC_Guard npc = new NPC_Guard();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }

}
