package io.wispforest.affinity.endec.nbt;

import com.google.common.io.ByteStreams;
import io.wispforest.endec.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtTagSizeTracker;

import java.io.IOException;

public final class NbtEndec implements Endec<NbtElement> {

    public static final Endec<NbtElement> ELEMENT = new NbtEndec();
    public static final Endec<NbtCompound> COMPOUND = new NbtEndec().xmap(NbtCompound.class::cast, compound -> compound);

    private NbtEndec() {}

    @Override
    public void encode(SerializationContext ctx, Serializer<?> serializer, NbtElement value) {
        if (serializer instanceof SelfDescribedSerializer<?>) {
            NbtDeserializer.of(value).readAny(ctx, serializer);
            return;
        }

        try {
            var output = ByteStreams.newDataOutput();
            NbtIo.write(value, output);

            serializer.writeBytes(ctx, output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode binary NBT in NbtEndec", e);
        }
    }

    @Override
    public NbtElement decode(SerializationContext ctx, Deserializer<?> deserializer) {
        if (deserializer instanceof SelfDescribedDeserializer<?> selfDescribedDeserializer) {
            var nbt = NbtSerializer.of();
            selfDescribedDeserializer.readAny(ctx, nbt);

            return nbt.result();
        }

        try {
            return NbtIo.read(ByteStreams.newDataInput(deserializer.readBytes(ctx)), NbtTagSizeTracker.EMPTY);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse binary NBT in NbtEndec", e);
        }
    }
}
