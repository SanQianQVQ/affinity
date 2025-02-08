package io.wispforest.affinity.client.render.item;

import io.wispforest.affinity.client.render.blockentity.MangroveBasketBlockEntityRenderer;
import io.wispforest.affinity.object.AffinityBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;

public class MangroveBasketItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var client = MinecraftClient.getInstance();
        client.getBlockRenderManager().renderBlockAsEntity(AffinityBlocks.MANGROVE_BASKET.getDefaultState(), matrices, vertexConsumers, light, overlay);

        var nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null) return;

        var containedState = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), nbt.getCompound("ContainedState"));
        var pos = BlockPos.ORIGIN;

        if (client.getCameraEntity() != null) {
            pos = client.getCameraEntity().getBlockPos();
        }

        var containedBlockEntity = BlockEntity.createFromNbt(pos, containedState, nbt.getCompound("ContainedBlockEntity"));
        if (containedBlockEntity == null) return;
        containedBlockEntity.setWorld(client.world);
        MangroveBasketBlockEntityRenderer.renderContents(
                client.getBlockRenderManager(),
                client.getBlockEntityRenderDispatcher(),
                containedState,
                containedBlockEntity,
                client.getTickDelta(),
                matrices,
                vertexConsumers,
                light,
                overlay
        );
    }
}
