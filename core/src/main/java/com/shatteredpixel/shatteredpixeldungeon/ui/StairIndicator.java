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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;

//shortcut that taps the level's entrance (up) or exit (down) once that cell has been seen
public class StairIndicator extends Tag {

	public static final int WIDTH = 18;
	public static final int HEIGHT = 14;

	private final boolean up;
	private Image icon;

	public StairIndicator( boolean up ) {
		super( 0xCDD5C0 );
		this.up = up;

		setSize( WIDTH, HEIGHT );

		icon.angle = up ? -90 : 90;

		visible = false;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		icon = Icons.get( Icons.ARROW );
		icon.origin.set( icon.width() / 2f, icon.height() / 2f );
		add( icon );
	}

	@Override
	protected void layout() {
		super.layout();

		icon.x = x + 2 + (width - 2 - icon.width()) / 2f;
		icon.y = y + (height - icon.height()) / 2f;
		PixelScene.align( icon );
	}

	private int stairCell() {
		return up ? Dungeon.level.entrance() : Dungeon.level.exit();
	}

	private boolean stairKnown() {
		int cell = stairCell();
		return cell >= 0 && cell < Dungeon.level.length()
				&& (Dungeon.level.visited[cell] || Dungeon.level.mapped[cell]);
	}

	@Override
	public void update() {
		boolean show = Dungeon.hero != null && Dungeon.hero.isAlive() && Dungeon.level != null && stairKnown();
		if (show != visible) {
			visible = show;
			if (visible) flash();
		}
		super.update();
	}

	@Override
	protected void onClick() {
		super.onClick();
		if (!stairKnown()) return;
		//drop any pending targeting so the tap goes to normal hero movement
		if (Dungeon.hero.ready) GameScene.ready();
		GameScene.handleCell( stairCell() );
	}
}
