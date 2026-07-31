/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.DropTable;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Locale;

//per-run drop table editor, opened from Purestream; read-only once the run enters the dungeon
public class WndDropTable extends Window {

    private static final int WIDTH_P = 126;
    private static final int HEIGHT_P = 180;

    private static final int WIDTH_L = 216;
    private static final int HEIGHT_L = 130;

    private static final int TTL_HEIGHT = 14;
    private static final int BTN_HEIGHT = 16;
    private static final int TXT_HEIGHT = 9;

    private final boolean editable;

    private ScrollingGridPane grid;
    private RenderedTextBlock statusText;
    private RenderedTextBlock countText;

    //grid entries are built once and reused; a refresh only re-creates the six headers
    private final ArrayList<ArrayList<ToggleItem>> groupItems = new ArrayList<>();

    private float scrollPos = 0;
    private float gridTop;
    private float gridHeight;
    private float statusY;
    private float countY;

    public WndDropTable() {
        super();

        editable = !DropTable.locked();

        int w = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
        int h = PixelScene.landscape() ? HEIGHT_L : HEIGHT_P;
        resize(w, h);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(w);
        title.setPos((w - title.width()) / 2f, (TTL_HEIGHT - title.height()) / 2f);
        PixelScene.align(title);
        add(title);

        countText = PixelScene.renderTextBlock(6);
        countText.maxWidth(w);
        add(countText);

        statusText = PixelScene.renderTextBlock(6);
        statusText.maxWidth(w);
        statusText.visible = false;
        add(statusText);

        countY = h - TXT_HEIGHT;

        float bottom = countY;
        if (editable) {
            RedButton btnEnableAll = new RedButton(Messages.get(this, "enable_all"), 8) {
                @Override
                protected void onClick() {
                    enableAll();
                }
            };
            btnEnableAll.setRect(0, bottom - 1 - BTN_HEIGHT, w, BTN_HEIGHT);
            PixelScene.align(btnEnableAll);
            add(btnEnableAll);
            bottom = btnEnableAll.top() - 1;
        } else {
            bottom -= 1;
        }

        statusY = bottom - TXT_HEIGHT;
        statusText.setPos(0, statusY);
        PixelScene.align(statusText);

        gridTop = TTL_HEIGHT;
        gridHeight = Math.max(TXT_HEIGHT, statusY - 1 - gridTop);

        grid = new ScrollingGridPane() {
            @Override
            public synchronized void update() {
                super.update();
                scrollPos = content.camera.scroll.y;
            }
        };
        add(grid);

        buildItems();
        rebuildGrid();
        updateCount();

        if (!editable) {
            setStatus(Messages.get(this, "locked_notice"));
        }
    }

    private void buildItems() {
        for (Generator.Category cat : DropTable.GROUPS) {

            ArrayList<ToggleItem> items = new ArrayList<>();

            for (Class<?> cls : DropTable.droppable(cat)) {
                Item item = (Item) Reflection.newInstance(cls);
                if (item == null) continue;

                //anonymize keeps the true name but hides the gem sprite of an unidentified ring
                if (item instanceof Ring) ((Ring) item).anonymize();

                ToggleItem gridItem = new ToggleItem(cat, asItemClass(cls),
                        new ItemSprite(item.image, item.glowing()), item.glowing(),
                        Messages.titleCase(item.name()));

                if (item.icon != -1) {
                    Image secondIcon = new Image(Assets.Sprites.ITEM_ICONS);
                    secondIcon.frame(ItemSpriteSheet.Icons.film.get(item.icon));
                    gridItem.addSecondIcon(secondIcon);
                }

                gridItem.applyState();
                items.add(gridItem);
            }

            groupItems.add(items);
        }
    }

    //Group.clear() only unparents its members, so the cached items survive and can be re-added
    private void rebuildGrid() {

        grid.clear();

        for (int i = 0; i < DropTable.GROUPS.length; i++) {
            Generator.Category cat = DropTable.GROUPS[i];

            grid.addHeader(Messages.get(this, "header",
                    catName(cat), DropTable.disabledCount(cat), DropTable.disableCap(cat)));

            for (ToggleItem item : groupItems.get(i)) {
                grid.addItem(item);
            }
        }

        grid.setRect(0, gridTop, width, gridHeight);
        grid.scrollTo(0, scrollPos);
    }

    private void updateCount() {
        countText.text(Messages.get(this, "count",
                DropTable.totalDroppable() - DropTable.totalDisabled(), DropTable.totalDroppable()));
        countText.setPos(0, countY);
        PixelScene.align(countText);
    }

    //RenderedTextBlock.text("") never reaches build(), so an empty line has to be hidden instead
    private void setStatus(String msg) {
        if (msg == null || msg.isEmpty()) {
            statusText.visible = false;
            return;
        }
        statusText.text(msg);
        statusText.setPos(0, statusY);
        PixelScene.align(statusText);
        statusText.visible = true;
    }

    private void enableAll() {
        for (ArrayList<ToggleItem> items : groupItems) {
            for (ToggleItem item : items) {
                DropTable.set(item.cls, true);
                item.applyState();
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.CLICK);
        setStatus(null);
        rebuildGrid();
        updateCount();
    }

    private String catName(Generator.Category cat) {
        return Messages.get(this, "cat_" + cat.name().toLowerCase(Locale.ENGLISH));
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Item> asItemClass(Class<?> cls) {
        return (Class<? extends Item>) cls;
    }

    private class ToggleItem extends ScrollingGridPane.GridItem {

        private final Generator.Category cat;
        private final Class<? extends Item> cls;
        private final ItemSprite.Glowing glow;
        private final String name;

        public ToggleItem(Generator.Category cat, Class<? extends Item> cls, ItemSprite sprite,
                          ItemSprite.Glowing glow, String name) {
            super(sprite);
            this.cat = cat;
            this.cls = cls;
            this.glow = glow;
            this.name = name;
            setScale(0.5f);
        }

        //mirrors the journal's unseen treatment: blacked out icon over a red cell
        private void applyState() {
            boolean disabled = DropTable.isDisabled(cls);
            if (icon instanceof ItemSprite) {
                ((ItemSprite) icon).glow(disabled ? null : glow);
            } else {
                icon.resetColor();
            }
            if (disabled) {
                icon.lightness(0);
                if (secondIcon != null) secondIcon.lightness(0);
                hardLightBG(2f, 1f, 1f);
            } else {
                if (secondIcon != null) secondIcon.resetColor();
                hardLightBG(1f, 1f, 1f);
            }
        }

        @Override
        public boolean onClick(float x, float y) {
            if (!inside(x, y)) return false;

            if (!editable) {
                GameScene.show(new WndMessage(Messages.get(WndDropTable.class, "locked")));
                return true;
            }

            boolean disabled = DropTable.isDisabled(cls);
            if (!disabled && !DropTable.canDisable(cat)) {
                GameScene.show(new WndMessage(Messages.get(WndDropTable.class, "cap_reached",
                        catName(cat), DropTable.disableCap(cat))));
                return true;
            }

            DropTable.set(cls, disabled);
            applyState();
            Sample.INSTANCE.play(Assets.Sounds.CLICK);
            setStatus(Messages.get(WndDropTable.class,
                    disabled ? "item_enabled" : "item_disabled", name));

            //safe to restructure the grid here: ScrollingGridPane.onClick breaks as soon as we return true
            rebuildGrid();
            updateCount();
            return true;
        }
    }
}
