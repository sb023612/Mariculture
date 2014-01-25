package mariculture.plugins.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mariculture.api.core.IAnvilHandler.RecipeAnvil;
import mariculture.api.core.MaricultureHandlers;
import mariculture.core.Mariculture;
import mariculture.core.lib.Modules;
import mariculture.core.helpers.EnchantHelper;
import mariculture.core.helpers.OreDicHelper;
import mariculture.core.helpers.cofh.ItemHelper;
import mariculture.core.lib.Text;
import mariculture.magic.JewelryHandler;
import mariculture.magic.Magic;
import mariculture.magic.jewelry.ItemJewelry;
import mariculture.magic.jewelry.ItemRing;
import mariculture.magic.jewelry.ItemBracelet;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class NEIAnvilRecipeHandler extends NEIBase {
	public static final HashMap<ItemStack, RecipeJewelry> jewelry = new HashMap();
	
	public static class RecipeJewelry {
		int hits;
		ItemStack input;
		public RecipeJewelry(ItemStack input, int hits) {
			this.hits = hits;
			this.input = input;
		}
	}
	
	public class CachedAnvilRecipe extends CachedRecipe {
		int hits;
		PositionedStack input;
		PositionedStack output;

		public CachedAnvilRecipe(ItemStack input, ItemStack output, int hits) {
			this.hits = hits;
			this.input = new PositionedStack(input, 62, 12);
			this.output = new PositionedStack(output, 128, 11);
		}

		@Override
		public PositionedStack getResult() {
			return output;
		}

		@Override
		public PositionedStack getIngredient() {
			return input;
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("blacksmithanvil") && getClass() == NEIAnvilRecipeHandler.class) {
			HashMap<String, RecipeAnvil> recipes = MaricultureHandlers.anvil.getRecipes();
			for (Entry<String, RecipeAnvil> recipe : recipes.entrySet()) {
				arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getValue().output, recipe.getValue().hits));
			}
			
			if(Modules.magic.isActive()) {
				for(Entry<ItemStack, RecipeJewelry> recipe: jewelry.entrySet())  {
					arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getKey(), recipe.getValue().hits));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		HashMap<String, RecipeAnvil> recipes = MaricultureHandlers.anvil.getRecipes();
		for (Entry<String, RecipeAnvil> recipe : recipes.entrySet()) {
			if(OreDicHelper.convert(result).equals(OreDicHelper.convert(recipe.getValue().output))) {
				arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getValue().output, recipe.getValue().hits));
			}
		}
		
		if(Modules.magic.isActive()) {
			for(Entry<ItemStack, RecipeJewelry> recipe: jewelry.entrySet())  {
				if(result.hasTagCompound() && recipe.getKey().hasTagCompound()) {
					ItemStack other = recipe.getKey();
					if(other.stackTagCompound.getInteger("Part1") == result.stackTagCompound.getInteger("Part1") &&
							other.stackTagCompound.getInteger("Part2") == result.stackTagCompound.getInteger("Part2")) {
						arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getKey(), recipe.getValue().hits));
					}
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		HashMap<String, RecipeAnvil> recipes = MaricultureHandlers.anvil.getRecipes();
		for (Entry<String, RecipeAnvil> recipe : recipes.entrySet()) {
			if(OreDicHelper.convert(ingredient).equals(OreDicHelper.convert(recipe.getValue().input))) {
				arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getValue().output, recipe.getValue().hits));
			}
		}
		
		if(Modules.magic.isActive()) {
			for(Entry<ItemStack, RecipeJewelry> recipe: jewelry.entrySet())  {
				if(NEIServerUtils.areStacksSameTypeCrafting(ingredient, recipe.getValue().input)) {
					arecipes.add(new CachedAnvilRecipe(recipe.getValue().input, recipe.getKey(), recipe.getValue().hits));
				}
			}
		}
	}

	@Override
	public void drawExtras(int id) {
		CachedAnvilRecipe cache = (CachedAnvilRecipe) arecipes.get(id);
		Minecraft.getMinecraft().fontRenderer.drawString(Text.GREY + cache.hits + " Hits", 78, 38, 0);
	}
	
	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(88, 13, 22, 16), "blacksmithanvil"));
	}

	@Override
	public String getRecipeName() {
		return "Blacksmith's Anvil";
	}

	@Override
	public String getGuiTexture() {
		return new ResourceLocation(Mariculture.modid, "textures/gui/nei/anvil.png").toString();
	}

	@Override
	public String getOverlayIdentifier() {
		return "blacksmithanvil";
	}
	
	@Override
	public boolean isOverItem(GuiRecipe gui, int id) {
		return false;
	}
}
