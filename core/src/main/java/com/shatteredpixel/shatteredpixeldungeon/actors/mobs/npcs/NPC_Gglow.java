package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClosureSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PRTS_Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Pink_doggiSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class NPC_Gglow extends NPC {

    {
        spriteClass = Pink_doggiSprite.class;
        HP=HT=1;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.NPC);
    }
    private static final String AC_ADD = "ADD";

    boolean first = false;

    @Override
    protected boolean act() {
        if (!first) {
            int length = Dungeon.level.length();
            int[] map = Dungeon.level.map;
            boolean[] mapped = Dungeon.level.mapped;
            boolean[] discoverable = Dungeon.level.discoverable;

            boolean noticed = false;

            for (int i=0; i < length; i++) {

                int terr = map[i];

                if (discoverable[i]) {

                    mapped[i] = true;
                    if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                        Dungeon.level.discover( i );

                        if (Dungeon.level.heroFOV[i]) {
                            GameScene.discoverTile( i, terr );
                            ScrollOfMagicMapping.discover( i );

                            noticed = true;
                        }
                    }
                }
            }
            GameScene.updateFog();

            Buff.affect(Dungeon.hero, MindVision.class, 3f);
            Dungeon.observe();

            first = true;
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "die"));
        super.die(cause);
    }

    @Override
    public boolean interact(Char c) {
        if (Dungeon.hero.CharSkin == 0) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Char.Alignment.ALLY
                        && Dungeon.level.heroFOV[mob.pos]
                        && mob instanceof SkinModel) {

                    if (!Dungeon.customSeedText.isEmpty()) {
                        Game.runOnRenderThread(new Callback() {
                            @Override
                            public void call() {
                                GameScene.show(new WndMessage(Messages.get(Closure.class, "custom_seed")));
                            }});
                        return true;
                    }

                    int a = Dungeon.skin_ch;
                    switch (a) {
                        case 0:
                            if (Badges.isUnlocked(Badges.Badge.EVILTIME_END)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_TALU)) {Badges.validatetaluskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin1", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 1:
                            if (Badges.isUnlocked(Badges.Badge.FRAGGING)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_NOVA)) { Badges.validatenovaskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin2", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 2: // 바병슼 스킨
                            if (Badges.isUnlocked(Badges.Badge.GAMES_PLAYED_2)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_BABOSKADI)) {Badges.validateskadiskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin3", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 3: // 쑤수로 스킨
                            if (Badges.isUnlocked(Badges.Badge.ALL_POTIONS_IDENTIFIED)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_SUSUU)) {Badges.validatesusuuskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin4", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 4: // 그라니 스킨
                            if (Badges.isUnlocked(Badges.Badge.GAMES_PLAYED_1)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_GRN)) {Badges.validategrnskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin5", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 5: // 제껄룩 스킨
                            if (Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_JESSI)) {Badges.validatejessiskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin6", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 6: // 꼬지리 스킨
                            if (Badges.isUnlocked(Badges.Badge.HAPPY_END)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_LAPPY)) {Badges.validatelappyskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin7", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 7: // 리프 스킨
                            if (Badges.isUnlocked(Badges.Badge.CHAMPION_1)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_LEAF)) {Badges.validateleafskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin8", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 8: // 머드락 스킨
                            if (Badges.isUnlocked(Badges.Badge.CHAMPION_2)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_MUDROCK)) {Badges.validateRockskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin9", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 9: // 별누나 스킨
                            if (Badges.isUnlocked(Badges.Badge.SLAIN_PURSUER)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_ASTESIA)) {Badges.validateAstesiaskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin10", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 10: // 상어 스킨
                            if (Badges.isUnlocked(Badges.Badge.GAMES_PLAYED_1)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_SPECTER)) {Badges.validatesameskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin11", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 11: // 상어 스킨
                            if (Badges.isUnlocked(Badges.Badge.SIESTA_PART2)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_SCHWARZ)) {Badges.validateschwazrskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin12", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 12: // 알게또 스킨
                            if (Badges.isUnlocked(Badges.Badge.GREY_CHAMPION1)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_ARCH)) {Badges.validatearchskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin13", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 13: // 토미미 스킨
                            if (Badges.isUnlocked(Badges.Badge.GAVIAL_PART2)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_TOMIMI)) {Badges.validatetomimiskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin14", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 14: // 프란카 스킨
                            if (Badges.isUnlocked(Badges.Badge.USE_HEALBOX)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_FRANKA)) {Badges.validatefrankaskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin15", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;
                        case 15: // 위디 스킨
                            sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "no")); break;
                         /*   if (Badges.isUnlocked(Badges.Badge.PIRANHAS)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_WEEDY)) {Badges.validatefrankaskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin16", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;*/
                        case 16: // 글라디아 스킨
                            if (Badges.isUnlocked(Badges.Badge.IBERIA_PART1)) {
                                if(!Badges.isUnlocked(Badges.Badge.SKIN_GLADIIA)) {Badges.validategladiiaskin();}
                                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hi"));
                                break;
                            } else {sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "no"));
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndMessage(Messages.get(Closure.class, "fail_skin17", Dungeon.hero.heroClass.title())));
                                    }});}
                            break;

                    }
                }
            }
        } else {  sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "hihi")); }
        //  sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "no"));

        ((HeroSprite) Dungeon.hero.sprite).updateArmor();
        GameScene.updateplayeravater();

        return true;
    }

    public static void spawn(Level level, int poss) {
        NPC_Gglow npc = new NPC_Gglow();
        npc.pos = poss;
        level.mobs.add(npc);
    }

}
