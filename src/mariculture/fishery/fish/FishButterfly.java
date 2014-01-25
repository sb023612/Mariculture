package mariculture.fishery.fish;

import java.util.Random;

import mariculture.api.core.EnumBiomeType;
import mariculture.api.fishery.EnumRodQuality;
import mariculture.api.fishery.Fishing;
import mariculture.api.fishery.fish.EnumFishGroup;
import mariculture.api.fishery.fish.FishSpecies;
import mariculture.core.Core;
import mariculture.core.lib.MaterialsMeta;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FishButterfly extends FishSpecies {
	public FishButterfly(int id) {
		super(id);
	}

	@Override
	public EnumFishGroup getGroup() {
		return EnumFishGroup.NEMO;
	}

	@Override
	public int getLifeSpan() {
		return 37;
	}

	@Override
	public int getFertility() {
		return 185;
	}

	@Override
	public boolean isDominant() {
		return true;
	}
	
	@Override
	public void addFishProducts() {
		addProduct(new ItemStack(Core.materials, 1, MaterialsMeta.DROP_WATER), 10D);
		addProduct(new ItemStack(Item.feather), 4D);
	}

	@Override
	public int getTankLevel() {
		return 3;
	}

	@Override
	public int getCatchChance() {
		return 7;
	}
	
	@Override
	public double getFishOilVolume() {
		return 0.110;
	}

	@Override
	public int[] getChestGenChance() {
		return new int[] { 1, 1, 2 };
	}
	
	@Override
	public int getFishMealSize() {
		return 1;
	}
}
