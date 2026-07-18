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
import com.shatteredpixel.shatteredpixeldungeon.sprites.Delivery_dronSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class DeliveryDrone extends NPC {
    {
        spriteClass = Delivery_dronSprite.class;
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

        //tutorial step 3: offer to deliver the cargo. Delivery is confirmed via a dialogue
        //option; on confirm, consume the cargo, advance (drops 50 gold + completes the
        //questline) and show the reward window.
        final TutorialQuestLine q = Quests.get(TutorialQuestLine.class);
        if (q != null && q.at(3)) {
            final QuestCargo cargo = Dungeon.hero.belongings.getItem(QuestCargo.class);
            if (cargo != null) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(
                                Messages.get(DeliveryDrone.this, "name"),
                                Messages.get(DeliveryDrone.this, "deliver_prompt"),
                                Messages.get(DeliveryDrone.this, "deliver"),
                                Messages.get(DeliveryDrone.this, "cancel")
                        ) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0 && q.at(3)
                                        && Dungeon.hero.belongings.getItem(QuestCargo.class) != null) {
                                    cargo.detachAll(Dungeon.hero.belongings.backpack);
                                    q.advance();
                                    GameScene.show(new WndQuest(DeliveryDrone.this, Messages.get(DeliveryDrone.this, "reward")));
                                }
                            }
                        });
                    }
                });
                return true;
            }
        }

        //flavor NPC otherwise (or hero isn't holding the cargo)
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "idle"));
        return true;
    }

    public static void spawn(Level level, int ppos) {
        DeliveryDrone npc = new DeliveryDrone();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }

}
