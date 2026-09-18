package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCat;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.PhantomCatQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_PhantomSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

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
            if (Dungeon.hero.belongings.getItem(QuestCat.class) != null) {
                q = PhantomCatQuestLine.adoptOrphanCat();
                Quests.add(q);
            } else {
                Quests.add(new PhantomCatQuestLine());
                tell(Messages.get(this, "quest"));
                return true;
            }
        }

        //declined earlier: resume on the step it was left at rather than using the quest up.
        //no early return, so a resume at the turn-in step still hands her over in the same conversation
        q.reopen();

        if (q.tryTurnIn()) {
            tell(Messages.get(this, "result"));
        } else if (q.ongoing()) {
            tell(Messages.get(this, "quest"));   //still looking for her
        } else {
            sprite.showStatus(CharSprite.NEUTRAL, "?");
        }
        return true;
    }

    private void tell(final String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndMessage(text));
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
