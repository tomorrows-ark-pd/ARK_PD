/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Heamyo extends MeleeWeapon {

    public static final String AC_DUMMY = "DUMMY";
    public static final String AC_POS = "POS";
    public static final String AC_JOURNAL_REVEAL = "JOURNAL_REVEAL";
    public static final String AC_JOURNAL_RESET = "JOURNAL_RESET";
    public static final String AC_SPAWN = "SPAWN";

    {
        image = ItemSpriteSheet.HEAMYO;
        hitSound = Assets.Sounds.GOLD;

        tier = 1;
        ACC = 12000f;
        DLY = 0.1f; //0.67x speed
        RCH = 300;

        defaultAction = AC_POS;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DUMMY);
        actions.add(AC_POS);
        actions.add(AC_JOURNAL_REVEAL);
        actions.add(AC_JOURNAL_RESET);
        actions.add(AC_SPAWN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_DUMMY)) {
            curUser = hero;
            GameScene.selectCell(dummyPlacer);
        } else if (action.equals(AC_POS)) {
            curUser = hero;
            GameScene.selectCell(posPrinter);
        } else if (action.equals(AC_JOURNAL_REVEAL)) {
            Catalog.debugRevealAll();
            Bestiary.debugRevealAll();
            GLog.i("journal revealed");
        } else if (action.equals(AC_JOURNAL_RESET)) {
            Catalog.debugReset();
            Bestiary.debugReset();
            GLog.i("journal reset");
        } else if (action.equals(AC_SPAWN)) {
            curUser = hero;
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndSpawn());
                }
            });
        }
    }

    //drops one random item of the category on each valid cell around the hero
    private static void spawnAround(Generator.Category cat) {
        int spawned = 0;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int cell = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[i];
            if (cell < 0 || cell >= Dungeon.level.length()) continue;
            if (Dungeon.level.solid[cell]) continue;
            if (!Dungeon.level.passable[cell] && !Dungeon.level.avoid[cell]) continue;
            if (Dungeon.level.map[cell] == Terrain.CHASM) continue;

            Item item = Generator.random(cat);
            if (item == null) continue;
            item.identify();

            Heap heap = Dungeon.level.drop(item, cell);
            if (heap.sprite != null) heap.sprite.drop(Dungeon.hero.pos);
            spawned++;
        }
        GLog.i("spawned " + spawned + "x " + cat.name());
    }

    private static class WndSpawn extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_HEIGHT = 18;
        private static final int GAP = 2;

        private static final Generator.Category[] CATS = {
                Generator.Category.WEP_T1,
                Generator.Category.WEP_T2,
                Generator.Category.WEP_T3,
                Generator.Category.WEP_T4,
                Generator.Category.WEP_T5,
                Generator.Category.RING
        };

        private static final String[] LABELS = {
                "1티어 무기", "2티어 무기", "3티어 무기", "4티어 무기", "5티어 무기", "반지"
        };

        public WndSpawn() {
            super();

            RenderedTextBlock title = PixelScene.renderTextBlock("주변에 아이템 소환", 9);
            title.hardlight(TITLE_COLOR);
            title.maxWidth(WIDTH);
            title.setPos((WIDTH - title.width()) / 2f, 2);
            PixelScene.align(title);
            add(title);

            float pos = title.bottom() + GAP * 2;

            for (int i = 0; i < CATS.length; i++) {
                final Generator.Category cat = CATS[i];
                RedButton btn = new RedButton(LABELS[i], 8) {
                    @Override
                    protected void onClick() {
                        hide();
                        spawnAround(cat);
                    }
                };
                btn.setRect(0, pos, WIDTH, BTN_HEIGHT);
                PixelScene.align(btn);
                add(btn);
                pos = btn.bottom() + GAP;
            }

            resize(WIDTH, (int) pos);
        }
    }

    private final CellSelector.Listener dummyPlacer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            Char existing = Actor.findChar(cell);
            if (existing instanceof Skeleton) {
                existing.die(null);
            } else if (existing != null) {
                GLog.w("Cell occupied by " + existing.getClass().getSimpleName());
            } else if (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) {
                Skeleton dummy = new Skeleton();
                dummy.pos = cell;
                Dungeon.level.occupyCell(dummy);
                GameScene.add(dummy);
            }
        }

        @Override
        public String prompt() {
            return "훈련인형 소환/삭제";
        }
    };

    private final CellSelector.Listener posPrinter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            int x = cell % Dungeon.level.width();
            int y = cell / Dungeon.level.width();
            int terrain = Dungeon.level.map[cell];
            GLog.i("pos: " + cell + " (x=" + x + ", y=" + y + "), terrain: " + terrainName(terrain) + " (" + terrain + ")");
        }

        @Override
        public String prompt() {
            return "좌표 출력할 셀 선택";
        }
    };

    private static String terrainName(int terrain) {
        switch (terrain) {
            case Terrain.CHASM:
                return "CHASM";
            case Terrain.EMPTY:
                return "EMPTY";
            case Terrain.GRASS:
                return "GRASS";
            case Terrain.EMPTY_WELL:
                return "EMPTY_WELL";
            case Terrain.WALL:
                return "WALL";
            case Terrain.DOOR:
                return "DOOR";
            case Terrain.OPEN_DOOR:
                return "OPEN_DOOR";
            case Terrain.ENTRANCE:
                return "ENTRANCE";
            case Terrain.EXIT:
                return "EXIT";
            case Terrain.EMBERS:
                return "EMBERS";
            case Terrain.LOCKED_DOOR:
                return "LOCKED_DOOR";
            case Terrain.PEDESTAL:
                return "PEDESTAL";
            case Terrain.WALL_DECO:
                return "WALL_DECO";
            case Terrain.BARRICADE:
                return "BARRICADE";
            case Terrain.EMPTY_SP:
                return "EMPTY_SP";
            case Terrain.HIGH_GRASS:
                return "HIGH_GRASS";
            case Terrain.FURROWED_GRASS:
                return "FURROWED_GRASS";
            case Terrain.SECRET_DOOR:
                return "SECRET_DOOR";
            case Terrain.SECRET_TRAP:
                return "SECRET_TRAP";
            case Terrain.TRAP:
                return "TRAP";
            case Terrain.INACTIVE_TRAP:
                return "INACTIVE_TRAP";
            case Terrain.EMPTY_DECO:
                return "EMPTY_DECO";
            case Terrain.LOCKED_EXIT:
                return "LOCKED_EXIT";
            case Terrain.UNLOCKED_EXIT:
                return "UNLOCKED_EXIT";
            case Terrain.SIGN:
                return "SIGN";
            case Terrain.WELL:
                return "WELL";
            case Terrain.STATUE:
                return "STATUE";
            case Terrain.STATUE_SP:
                return "STATUE_SP";
            case Terrain.BOOKSHELF:
                return "BOOKSHELF";
            case Terrain.ALCHEMY:
                return "ALCHEMY";
            case Terrain.WATER:
                return "WATER";
            case Terrain.SEA_TERROR:
                return "SEA_TERROR";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public int max(int lvl) {
        return 100000000;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        //  defender.sprite.killAndErase();
        //   defender.destroy();

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int defenseFactor(Char owner) {
        return 600 + 300 * buffedLvl();    //6 extra defence, plus 3 per level;
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 6 + 3 * buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 6);
        }
    }
}