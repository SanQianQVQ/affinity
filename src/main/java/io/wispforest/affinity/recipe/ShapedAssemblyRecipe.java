package io.wispforest.affinity.recipe;

import com.google.gson.JsonObject;
import io.wispforest.affinity.mixin.access.ShapedRecipeAccessor;
import io.wispforest.affinity.object.AffinityRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ShapedAssemblyRecipe extends ShapedRecipe {

    public ShapedAssemblyRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, category, width, height, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AffinityRecipeTypes.Serializers.ASSEMBLY_SHAPED;
    }

    @Override
    public RecipeType<?> getType() {
        return AffinityRecipeTypes.ASSEMBLY;
    }

    public static class Serializer extends ShapedRecipe.Serializer {

        @Override
        public ShapedAssemblyRecipe read(Identifier identifier, JsonObject jsonObject) {
            final var recipe = super.read(identifier, jsonObject);
            return new ShapedAssemblyRecipe(recipe.getId(), recipe.getGroup(), recipe.getCategory(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), ((ShapedRecipeAccessor) recipe).affinity$getOutput());
        }

        @Override
        public ShapedAssemblyRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            final var recipe = super.read(identifier, packetByteBuf);
            return new ShapedAssemblyRecipe(recipe.getId(), recipe.getGroup(), recipe.getCategory(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), ((ShapedRecipeAccessor) recipe).affinity$getOutput());
        }
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }
}
