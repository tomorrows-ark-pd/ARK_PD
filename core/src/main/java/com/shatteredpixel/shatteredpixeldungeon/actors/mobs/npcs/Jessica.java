package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.NormalMagazine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.QuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_jessicatSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Jessica extends NPC {
    {
        spriteClass = NPC_jessicatSprite.class;
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

        Quest q = Quests.get(Quest.class);

        //first meeting: register the quest and make the offer
        if (q == null) {
            Quests.add(new Quest());
            tell(Messages.get(this, "quest"));
            return true;
        }

        //declined earlier: abandoning turns the offer down rather than using it up, so re-offer
        if (q.reopen()) {
            tell(Messages.get(this, "quest"));
            return true;
        }

        if (q.ongoing()) {
            //turn-in is all-or-nothing, so branch on it rather than pre-checking the pack
            if (q.tryTurnIn()) tell(Messages.get(this, "result"));
            else tell(Messages.get(this, "quest"));   //still short of the magazine; nothing consumed
        } else {
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "say"));
        }
        return true;
    }

    private void tell(final String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(Jessica.this, text));
            }
        });
    }

    @Override
    protected boolean act() {
        sprite.turnTo(pos, 0);
        return super.act();
    }

    public static void spawn(Level level, int ppos) {
        Jessica cat = new Jessica();
        do {
            cat.pos = ppos;
        } while (cat.pos == -1);
        level.mobs.add(cat);
    }

    //--- journal quest: bring one standard magazine, get 800 gold ---
    public static class Quest extends QuestLine {

        private static final Class<? extends Item> REQUIRED = NormalMagazine.class;
        private static final int REQUIRED_COUNT = 1;

        /**
         * Consume the required items and complete the step; false (and nothing consumed) if short.
         */
        public boolean tryTurnIn() {
            if (!ongoing() || !consume(REQUIRED, REQUIRED_COUNT)) return false;
            advance();
            return true;
        }

        @Override
        public Image icon() {
            return new ItemSprite(ItemSpriteSheet.AMMO1);
        }

        @Override
        protected int stepCount() {
            return 1;
        }

        @Override
        public String progressText() {
            return Math.min(heldCount(REQUIRED), REQUIRED_COUNT) + "/" + REQUIRED_COUNT;
        }

        @Override
        protected ArrayList<Reward> stepRewards(int step) {
            return items(new Gold(800));
        }
    }
}
