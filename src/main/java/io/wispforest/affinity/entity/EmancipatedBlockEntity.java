package io.wispforest.affinity.entity;

import io.wispforest.affinity.endec.CodecUtils;
import io.wispforest.affinity.endec.nbt.NbtEndec;
import io.wispforest.affinity.object.AffinityEntities;
import io.wispforest.endec.Endec;
import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.impl.KeyedEndec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmancipatedBlockEntity extends Entity {

    public static final TrackedDataHandler<Optional<NbtCompound>> OPTIONAL_NBT = TrackedDataHandler.ofOptional(PacketByteBuf::writeNbt, PacketByteBuf::readNbt);

    private static final KeyedEndec<Integer> MAX_AGE_KEY = Endec.INT.keyed("max_age", 15);
    private static final KeyedEndec<Float> ANIMATION_SCALE_KEY = Endec.FLOAT.keyed("animation_scale", 1f);
    private static final KeyedEndec<NbtCompound> EMANCIPATED_BLOCK_ENTITY_DATA_KEY = NbtEndec.COMPOUND.keyed("emancipated_block_entity", (NbtCompound) null);
    private static final KeyedEndec<BlockState> EMANCIPATED_STATE_KEY = CodecUtils.toEndec(BlockState.CODEC).keyed("emancipated_state",Blocks.AIR.getDefaultState());

    @Nullable
    @Environment(EnvType.CLIENT)
    public BlockEntity renderBlockEntity;

    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(EmancipatedBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> ANIMATION_SCALE = DataTracker.registerData(EmancipatedBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Optional<NbtCompound>> EMANCIPATED_BLOCK_ENTITY_DATA = DataTracker.registerData(EmancipatedBlockEntity.class, OPTIONAL_NBT);
    private BlockState emancipatedState = Blocks.AIR.getDefaultState();

    public EmancipatedBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public static EmancipatedBlockEntity spawn(World world, BlockPos emancipatedPos, BlockState emancipatedState, @Nullable BlockEntity emancipatedBlockEntity, int decayTime, float animationScale) {
        var emancipated = AffinityEntities.EMANCIPATED_BLOCK.create(world);
        emancipated.setPos(emancipatedPos.getX() + .5, emancipatedPos.getY(), emancipatedPos.getZ() + .5);
        emancipated.setEmancipatedState(emancipatedState);
        emancipated.setMaxAge(decayTime);
        emancipated.setAnimationScale(animationScale);

        if (emancipatedBlockEntity != null) {
            emancipated.setEmancipatedBlockEntityData(emancipatedBlockEntity.createNbtWithId());
        }

        world.spawnEntity(emancipated);
        return emancipated;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(MAX_AGE, 15);
        this.dataTracker.startTracking(ANIMATION_SCALE, 1f);
        this.dataTracker.startTracking(EMANCIPATED_BLOCK_ENTITY_DATA, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient && this.age >= this.maxAge()) {
            this.discard();
        }
    }

    public BlockState emancipatedState() {
        return this.emancipatedState;
    }

    public void setEmancipatedState(BlockState emancipatedState) {
        this.emancipatedState = emancipatedState;
    }

    public @Nullable NbtCompound emancipatedBlockEntityData() {
        return this.dataTracker.get(EMANCIPATED_BLOCK_ENTITY_DATA).orElse(null);
    }

    private void setEmancipatedBlockEntityData(@Nullable NbtCompound emancipatedBlockEntity) {
        this.dataTracker.set(EMANCIPATED_BLOCK_ENTITY_DATA, Optional.ofNullable(emancipatedBlockEntity));
    }

    public int maxAge() {
        return this.dataTracker.get(MAX_AGE);
    }

    public void setMaxAge(int maxAge) {
        this.dataTracker.set(MAX_AGE, maxAge);
    }

    public float animationScale() {
        return this.dataTracker.get(ANIMATION_SCALE);
    }

    public void setAnimationScale(float animationScale) {
        this.dataTracker.set(ANIMATION_SCALE, animationScale);
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable("entity.affinity.emancipated_block_state", this.emancipatedState.getBlock().getName());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put(EMANCIPATED_STATE_KEY, this.emancipatedState);
        nbt.putIfNotNull(SerializationContext.empty(), EMANCIPATED_BLOCK_ENTITY_DATA_KEY, this.emancipatedBlockEntityData());
        nbt.put(MAX_AGE_KEY, this.maxAge());
        nbt.put(ANIMATION_SCALE_KEY, this.animationScale());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.emancipatedState = nbt.get(EMANCIPATED_STATE_KEY);
        this.setEmancipatedBlockEntityData(nbt.get(EMANCIPATED_BLOCK_ENTITY_DATA_KEY));
        this.setMaxAge(nbt.get(MAX_AGE_KEY));
        this.setAnimationScale(nbt.get(ANIMATION_SCALE_KEY));
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.emancipatedState));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.emancipatedState = Block.getStateFromRawId(packet.getEntityData());
    }
}
