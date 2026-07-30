package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MiniShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MerchantsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Catastrofe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfWarp;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Avantgardeform;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BlastSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalArmord;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.OathofFire;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SaltBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WeaponTransform;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KollamSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

public class MiniShopRoom extends SecretRoom {
    private ArrayList<Item> itemsToSpawn;
    private Map<Item, Integer> priceOverrides = new IdentityHashMap<>();

    @Override
    public int minWidth() {
        return Math.max(6, (int) (Math.sqrt(itemCount()) + 2));
    }

    @Override
    public int minHeight() {
        return Math.max(6, (int) (Math.sqrt(itemCount()) + 2));
    }

    public int itemCount() {
        if (itemsToSpawn == null) itemsToSpawn = generateItems();
        return itemsToSpawn.size();
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        placeShopkeeper(level);

        placeItems(level);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

    }

    protected void placeShopkeeper(Level level) {

        int pos = level.pointToCell(center());

        Mob shopkeeper = new MiniShopkeeper();
        shopkeeper.pos = pos;
        level.mobs.add(shopkeeper);

    }

    protected void placeItems(Level level) {

        if (itemsToSpawn == null) {
            itemsToSpawn = generateItems();
        }

        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == top) {
            itemPlacement.y++;
        } else if (itemPlacement.y == bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == left) {
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }

        for (Item item : itemsToSpawn) {

            if (itemPlacement.x == left + 1 && itemPlacement.y != top + 1) {
                itemPlacement.y--;
            } else if (itemPlacement.y == top + 1 && itemPlacement.x != right - 1) {
                itemPlacement.x++;
            } else if (itemPlacement.x == right - 1 && itemPlacement.y != bottom - 1) {
                itemPlacement.y++;
            } else {
                itemPlacement.x--;
            }

            int cell = level.pointToCell(itemPlacement);

            if (level.heaps.get(cell) != null) {
                do {
                    cell = level.pointToCell(random());
                } while (level.heaps.get(cell) != null || level.findMob(cell) != null);
            }

            Heap heap = level.drop(item, cell);
            heap.type = Heap.Type.FOR_SALE;
            Integer price = priceOverrides.get(item);
            if (price != null) heap.priceOverride = price;
        }

    }

    //gold value of one point of alchemy energy, used to price crafted goods sold here
    private static final int ENERGY_GOLD_VALUE = 10;

    //shallow depths use at least this depth multiplier, so early-game prices aren't too cheap
    private static final int MIN_DEPTH_MULTIPLIER = 2;

    private static int craftedPrice(Item item, int energyCost) {
        int augmentedValue = item.value() + energyCost * ENERGY_GOLD_VALUE;
        int markup = Dungeon.isChallenged(Challenges.NO_HERBALISM) ? 8 : 6;
        int price = augmentedValue * markup * (Dungeon.depth / 5 + 1);
        return Math.round(price / 10f) * 10;
    }

    // pricier at shallow depths
    private static int staplePrice(Item item) {
        int markup = Dungeon.isChallenged(Challenges.NO_HERBALISM) ? 7 : 5;
        int depthMultiplier = Math.max(MIN_DEPTH_MULTIPLIER, Dungeon.depth / 5 + 1);
        return item.value() * markup * depthMultiplier;
    }

    private static final Recipe[] SIGNATURE_RECIPES = new Recipe[]{
            new PhaseShift.Recipe(),
            new ChaosCatalyst.Recipe(),
            new AlchemicalCatalyst.Recipe(),
            new Alchemize.Recipe(),
            new AquaBlast.Recipe(),
            new Avantgardeform.Recipe(),
            new BeaconOfReturning.Recipe(),
            new BlastSpell.Recipe(),
            new CurseInfusion.Recipe(),
            new FeatherFall.Recipe(),
            new KollamSword.Recipe(),
            new MagicalArmord.Recipe(),
            new MagicalInfusion.Recipe(),
            new OathofFire.Recipe(),
            new PortableCover.Recipe(),
            new ReclaimTrap.Recipe(),
            new Recycle.Recipe(),
            new SaltBlast.Recipe(),
            new ScrollOfWarp.Recipe(),
            new WeaponTransform.Recipe(),
            new WildEnergy.Recipe(),
    };

    private void addSignatureItem(ArrayList<Item> itemsToSpawn) {
        Recipe recipe = SIGNATURE_RECIPES[Random.Int(SIGNATURE_RECIPES.length)];
        ArrayList<Item> noIngredients = new ArrayList<>();
        Item item = recipe.sampleOutput(noIngredients);
        item.cursed = false;
        item.identify();
        priceOverrides.put(item, craftedPrice(item, recipe.cost(noIngredients)));
        itemsToSpawn.add(item);
    }

    private void addElixirOrBrew(ArrayList<Item> itemsToSpawn) {
        Item item = Generator.randomUsingDefaults(Generator.Category.ELIXIR_BREW);
        priceOverrides.put(item, craftedPrice(item, 6));
        itemsToSpawn.add(item);
    }

    private void addExotic(ArrayList<Item> itemsToSpawn, Generator.Category category) {
        Item item = Generator.randomUsingDefaults(category);
        priceOverrides.put(item, craftedPrice(item, 0));
        itemsToSpawn.add(item);
    }

    protected ArrayList<Item> generateItems() {

        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        itemsToSpawn.add(new MerchantsBeacon());

        //matches how ShopRoom preps its rare-artifact roll; cursed=false/cursedKnown=true
        //guarantees it can never appear discounted, same as ShopRoom's own rare-artifact prep
        Item catastrofe = new Catastrofe();
        catastrofe.cursed = false;
        catastrofe.cursedKnown = true;
        priceOverrides.put(catastrofe, staplePrice(catastrofe));
        itemsToSpawn.add(catastrofe);

        addElixirOrBrew(itemsToSpawn);
        addElixirOrBrew(itemsToSpawn);
        addElixirOrBrew(itemsToSpawn);
        addExotic(itemsToSpawn, Generator.Category.EXOTIC_POTION);
        addExotic(itemsToSpawn, Generator.Category.EXOTIC_SCROLL);
        addSignatureItem(itemsToSpawn);
        addSignatureItem(itemsToSpawn);

        if (Random.Int(8) == 0) {
            itemsToSpawn.add(new ScrollOfTransmutation());
        } else itemsToSpawn.add(new PotionOfHealing());

        return itemsToSpawn;
    }
}
