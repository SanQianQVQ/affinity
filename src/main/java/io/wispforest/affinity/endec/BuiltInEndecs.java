package io.wispforest.affinity.endec;

import com.mojang.datafixers.util.Function3;
import io.wispforest.affinity.endec.nbt.NbtEndec;
import io.wispforest.endec.Endec;
import io.wispforest.endec.SerializationAttributes;
import io.wispforest.endec.format.gson.GsonEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Function;

public final class BuiltInEndecs {

    private BuiltInEndecs() {}

    public static final Endec<Identifier> IDENTIFIER = Endec.STRING.xmap(Identifier::new, Identifier::toString);
    public static final Endec<ItemStack> ITEM_STACK = NbtEndec.COMPOUND.xmap(ItemStack::fromNbt, stack -> stack.writeNbt(new NbtCompound()));
    public static final Endec<Text> TEXT = GsonEndec.INSTANCE.xmap(Text.Serializer::fromJson, Text.Serializer::toJsonTree);

    public static final Endec<Vec3i> VEC3I = vectorEndec("Vec3i", Endec.INT, Vec3i::new, Vec3i::getX, Vec3i::getY, Vec3i::getZ);
    public static final Endec<Vec3d> VEC3D = vectorEndec("Vec3d", Endec.DOUBLE, Vec3d::new, Vec3d::getX, Vec3d::getY, Vec3d::getZ);
    public static final Endec<Vector3f> VECTOR3F = vectorEndec("Vector3f", Endec.FLOAT, Vector3f::new, Vector3f::x, Vector3f::y, Vector3f::z);

    public static final Endec<BlockPos> BLOCK_POS = Endec
            .ifAttr(
                    SerializationAttributes.HUMAN_READABLE,
                    vectorEndec("BlockPos", Endec.INT, BlockPos::new, BlockPos::getX, BlockPos::getY, BlockPos::getZ)
            ).orElse(
                    Endec.LONG.xmap(BlockPos::fromLong, BlockPos::asLong)
            );

    public static final Endec<ChunkPos> CHUNK_POS = Endec
            .ifAttr(
                    SerializationAttributes.HUMAN_READABLE,
                    Endec.INT.listOf().validate(ints -> {
                        if (ints.size() != 2) {
                            throw new IllegalStateException("ChunkPos array must have two elements");
                        }
                    }).xmap(
                            ints -> new ChunkPos(ints.get(0), ints.get(1)),
                            chunkPos -> List.of(chunkPos.x, chunkPos.z)
                    )
            )
            .orElse(Endec.LONG.xmap(ChunkPos::new, ChunkPos::toLong));

    public static final Endec<BlockHitResult> BLOCK_HIT_RESULT = StructEndecBuilder.of(
            VEC3D.fieldOf("pos", BlockHitResult::getPos),
            Endec.forEnum(Direction.class).fieldOf("side", BlockHitResult::getSide),
            BLOCK_POS.fieldOf("block_pos", BlockHitResult::getBlockPos),
            Endec.BOOLEAN.fieldOf("inside_block", BlockHitResult::isInsideBlock),
            Endec.BOOLEAN.fieldOf("missed", $ -> $.getType() == HitResult.Type.MISS),
            (pos, side, blockPos, insideBlock, missed) -> !missed
                    ? new BlockHitResult(pos, side, blockPos, insideBlock)
                    : BlockHitResult.createMissed(pos, side, blockPos)
    );

    public static final Endec<PacketByteBuf> PACKET_BYTE_BUF = Endec.BYTES
            .xmap(bytes -> {
                var buffer = PacketByteBufs.create();
                buffer.writeBytes(bytes);

                return buffer;
            }, buffer -> {
                var bytes = new byte[buffer.readableBytes()];
                buffer.readBytes(bytes);

                return bytes;
            });

    // --- Constructors for MC types ---

    public static <T> Endec<T> ofRegistry(Registry<T> registry) {
        return IDENTIFIER.xmap(registry::get, registry::getId);
    }

    public static <T> Endec<TagKey<T>> unprefixedTagKey(RegistryKey<? extends Registry<T>> registry) {
        return IDENTIFIER.xmap(id -> TagKey.of(registry, id), TagKey::id);
    }

    public static <T> Endec<TagKey<T>> prefixedTagKey(RegistryKey<? extends Registry<T>> registry) {
        return Endec.STRING.xmap(
                s -> TagKey.of(registry, new Identifier(s.substring(1))),
                tag -> "#" + tag.id()
        );
    }

    private static <C, V> Endec<V> vectorEndec(String name, Endec<C> componentEndec, Function3<C, C, C, V> constructor, Function<V, C> xGetter, Function<V, C> yGetter, Function<V, C> zGetter) {
        return componentEndec.listOf().validate(ints -> {
            if (ints.size() != 3) {
                throw new IllegalStateException(name + " array must have three elements");
            }
        }).xmap(
                components -> constructor.apply(components.get(0), components.get(1), components.get(2)),
                vector -> List.of(xGetter.apply(vector), yGetter.apply(vector), zGetter.apply(vector))
        );
    }
}
