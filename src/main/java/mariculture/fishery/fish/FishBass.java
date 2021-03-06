package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.BRACKISH;
import static mariculture.api.core.Environment.Salinity.FRESH;
import static mariculture.api.core.Environment.Salinity.SALINE;
import static mariculture.core.lib.ItemLib.dropletDestroy;
import static mariculture.core.lib.ItemLib.dropletPlant;
import static mariculture.core.lib.ItemLib.dropletWater;
import static mariculture.core.lib.ItemLib.gunpowder;

import java.util.Random;

import mariculture.api.core.Environment.Height;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import mariculture.fishery.EntityBass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FishBass extends FishSpecies {
    @Override
    public int[] setSuitableTemperature() {
        return new int[] { 10, 15 };
    }

    @Override
    public Salinity[] setSuitableSalinity() {
        return new Salinity[] { FRESH, BRACKISH, SALINE };
    }

    @Override
    public boolean isDominant() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return 16;
    }

    @Override
    public int getFertility() {
        return 2000;
    }

    @Override
    public int getWaterRequired() {
        return 45;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 4D);
        addProduct(dropletDestroy, 5.0D);
        addProduct(dropletPlant, 3.0D);
        addProduct(gunpowder, 7.5D);
    }

    @Override
    public double getFishOilVolume() {
        return 4.325D;
    }

    @Override
    public ItemStack getLiquifiedProduct() {
        return new ItemStack(gunpowder);
    }

    @Override
    public int getFishMealSize() {
        return 5;
    }

    @Override
    public int getFoodStat() {
        return 1;
    }

    @Override
    public float getFoodSaturation() {
        return 0.1F;
    }

    @Override
    public int getFoodDuration() {
        return 1;
    }

    @Override
    public boolean canAlwaysEat() {
        return true;
    }

    @Override
    public ItemStack onRightClick(World world, EntityPlayer player, ItemStack stack, Random rand) {
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityBass(world, player));
        }

        return stack;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.GOOD;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return Height.isOverground(height) ? 15D : 5D;
    }
}
