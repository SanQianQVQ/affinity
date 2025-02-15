package io.wispforest.affinity.recipe;

import io.wispforest.affinity.blockentity.template.RitualCoreBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RitualRecipe<I extends RitualCoreBlockEntity.SocleInventory> implements Recipe<I> {

    protected final Identifier id;
    protected final List<Ingredient> socleInputs;

    public final int duration;
    public final int fluxCostPerTick;

    protected RitualRecipe(Identifier id, List<Ingredient> socleInputs, int duration, int fluxCostPerTick) {
        this.id = id;
        this.socleInputs = socleInputs;
        this.duration = duration;
        this.fluxCostPerTick = fluxCostPerTick;
    }

    protected boolean doShapelessMatch(List<Ingredient> expected, Collection<ItemStack> stacks) {
        var ingredientStacks = new ArrayList<ItemStack>();

        for (var stack : stacks) {
            if (stack.isEmpty()) continue;
            ingredientStacks.add(stack);
        }

        if (ingredientStacks.size() != expected.size()) return false;

        for (var ingredient : expected) {
            if (!ingredient.requiresTesting()) continue;
            return ShapelessMatch.isMatch(ingredientStacks, expected);
        }

        var matcher = new RecipeMatcher();
        for (var input : ingredientStacks) matcher.addInput(input, 1);
        return matcher.match(new MatchingRecipe(DefaultedList.copyOf(Ingredient.EMPTY, expected.toArray(Ingredient[]::new))), null);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(Ingredient.EMPTY, this.socleInputs.toArray(Ingredient[]::new));
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    private record MatchingRecipe(DefaultedList<Ingredient> ingredients) implements Recipe<Inventory> {

        @Override
        public DefaultedList<Ingredient> getIngredients() {
            return this.ingredients;
        }

        @Override
        public boolean matches(Inventory inventory, World world) {
            return false;
        }

        @Override
        public ItemStack craft(Inventory inventory, DynamicRegistryManager drm) {
            return null;
        }

        @Override
        public boolean fits(int width, int height) {
            return false;
        }

        @Override
        public ItemStack getOutput(DynamicRegistryManager drm) {
            return null;
        }

        @Override
        public Identifier getId() {
            return null;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return null;
        }

        @Override
        public RecipeType<?> getType() {
            return null;
        }
    }
}
