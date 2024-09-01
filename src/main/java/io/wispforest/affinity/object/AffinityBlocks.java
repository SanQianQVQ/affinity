package io.wispforest.affinity.object;

import io.wispforest.affinity.Affinity;
import io.wispforest.affinity.block.impl.*;
import io.wispforest.affinity.block.template.BlockItemProvider;
import io.wispforest.affinity.blockentity.impl.*;
import io.wispforest.affinity.misc.ArcaneFadeFluid;
import io.wispforest.affinity.object.rituals.AffinityRitualSocleTypes;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class AffinityBlocks implements BlockRegistryContainer {

    public static final Block BREWING_CAULDRON = new BrewingCauldronBlock();
    public static final Block COPPER_PLATED_AETHUM_FLUX_NODE = new CopperPlatedAethumFluxNodeBlock();
    public static final Block AETHUM_FLUX_CACHE = new AethumFluxCacheBlock();
    public static final Block CREATIVE_AETHUM_FLUX_CACHE = new CreativeAethumFluxCacheBlock();
    public static final Block STONE_BANDED_AETHUM_FLUX_NODE = new StoneBandedAethumFluxNodeBlock();
    public static final Block SUNDIAL = new SundialBlock();
    public static final Block ARBOREAL_ANNIHILATION_APPARATUS = new ArborealAnnihilationApparatusBlock();
    public static final Block MATTER_HARVESTING_HEARTH = new MatterHarvestingHearthBlock();
    public static final Block BLANK_RITUAL_SOCLE = new BlankRitualSocleBlock();
    public static final Block RUDIMENTARY_RITUAL_SOCLE = new RitualSocleBlock(AffinityRitualSocleTypes.RUDIMENTARY);
    public static final Block REFINED_RITUAL_SOCLE = new RitualSocleBlock(AffinityRitualSocleTypes.REFINED);
    public static final Block SOPHISTICATED_RITUAL_SOCLE = new RitualSocleBlock(AffinityRitualSocleTypes.SOPHISTICATED);
    public static final Block ASP_RITE_CORE = new AspRiteCoreBlock();
    public static final Block SPIRIT_INTEGRATION_APPARATUS = new SpiritIntegrationApparatusBlock();
    public static final Block RITUAL_SOCLE_COMPOSER = new RitualSocleComposerBlock();
    public static final Block AFFINE_INFUSER = new AffineInfuserBlock();
    public static final Block CROP_REAPER = new CropReaperBlock();
    public static final Block WORLD_PIN = new WorldPinBlock();
    public static final Block SUNSHINE_MONOLITH = new SunshineMonolithBlock();
    public static final Block ARCANE_TREETAP = new ArcaneTreetapBlock();
    public static final Block ASSEMBLY_AUGMENT = new AssemblyAugmentBlock();
    public static final Block MANGROVE_BASKET = new MangroveBasketBlock();
    public static final Block STAFF_PEDESTAL = new StaffPedestalBlock();
    public static final Block OUIJA_BOARD = new OuijaBoardBlock();
    public static final Block ITEM_TRANSFER_NODE = new ItemTransferNodeBlock();
    public static final Block AETHUM_PROBE = new AethumProbeBlock();
    public static final Block FIELD_COHERENCE_MODULATOR = new FieldCoherenceModulatorBlock();
    public static final Block HOLOGRAPHIC_STEREOPTICON = new HolographicStereopticonBlock();
    public static final Block EMERALD_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK));
    public static final Block VOID_BEACON = new VoidBeaconBlock();
    public static final Block GRAVITON_TRANSDUCER = new GravitonTransducerBlock();
    public static final Block ETHEREAL_AETHUM_FLUX_NODE = new EtherealAethumFluxNodeBlock();
    public static final Block ETHEREAL_AETHUM_FLUX_INJECTOR = new EtherealAethumFluxInjectorBlock();
    public static final Block LOCAL_DISPLACEMENT_GATEWAY = new LocalDisplacementGatewayBlock();
    public static final Block INFUSED_STONE = new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS));

    public static final AffineCandleBlock AFFINE_CANDLE = new AffineCandleBlock();

    @NoBlockItem
    public static final Block RANTHRACITE_WIRE = new RanthraciteWireBlock();

    public static final Block PECULIAR_CLUMP = new PeculiarClumpBlock();

    public static BoatEntity.Type AZALEA_BOAT_TYPE;
    public static final BlockSetType AZALEA_BLOCK_SET_TYPE = BlockSetTypeRegistry.registerWood(Affinity.id("azalea"));
    public static final WoodType AZALEA_WOOD_TYPE = WoodTypeRegistry.register(Affinity.id("azalea"), AZALEA_BLOCK_SET_TYPE);

    public static final Block AZALEA_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
    public static final Block AZALEA_WOOD = new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD));
    public static final Block STRIPPED_AZALEA_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG));
    public static final Block STRIPPED_AZALEA_WOOD = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD));
    public static final Block AZALEA_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
    public static final Block AZALEA_STAIRS = new StairsBlock(AZALEA_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS));
    public static final Block AZALEA_SLAB = new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB));
    public static final Block AZALEA_FENCE = new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE));
    public static final Block AZALEA_FENCE_GATE = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), AZALEA_WOOD_TYPE);
    public static final Block AZALEA_DOOR = new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), AZALEA_BLOCK_SET_TYPE);
    public static final Block AZALEA_TRAPDOOR = new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR), AZALEA_BLOCK_SET_TYPE);
    public static final Block AZALEA_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), AZALEA_BLOCK_SET_TYPE);
    public static final Block AZALEA_BUTTON = new ButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), AZALEA_BLOCK_SET_TYPE, 30, true);
    public static final Block AZALEA_CHEST = new AzaleaChestBlock(FabricBlockSettings.copyOf(Blocks.CHEST), () -> Entities.AZALEA_CHEST);

    @NoBlockItem public static final Block AZALEA_SIGN = new SignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), AZALEA_WOOD_TYPE);
    @NoBlockItem public static final Block AZALEA_HANGING_SIGN = new HangingSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), AZALEA_WOOD_TYPE);
    @NoBlockItem public static final Block AZALEA_WALL_HANGING_SIGN = new WallHangingSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), AZALEA_WOOD_TYPE);
    @NoBlockItem public static final Block AZALEA_WALL_SIGN = new WallSignBlock(FabricBlockSettings.copyOf(Blocks.OAK_SIGN), AZALEA_WOOD_TYPE);

    @NoBlockItem public static final FluidBlock ARCANE_FADE = new ArcaneFadeBlock();
    public static final Block THE_SKY = new TheSkyBlock();
    public static final Block INVERSION_STONE = new Block(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block BUDDING_AZALEA_LEAVES = new BuddingAzaleaLeavesBlock();
    public static final Block UNFLOWERING_AZALEA_LEAVES = new UnfloweringAzaleaLeavesBlock();

    @Override
    public void afterFieldProcessing() {
        StrippableBlockRegistry.register(AZALEA_LOG, STRIPPED_AZALEA_LOG);
        StrippableBlockRegistry.register(AZALEA_WOOD, STRIPPED_AZALEA_WOOD);
    }

    public static class Entities implements AutoRegistryContainer<BlockEntityType<?>> {

        public static final BlockEntityType<BrewingCauldronBlockEntity> BREWING_CAULDRON =
                make(BrewingCauldronBlockEntity::new, AffinityBlocks.BREWING_CAULDRON);

        public static final BlockEntityType<AethumFluxNodeBlockEntity> AETHUM_FLUX_NODE =
                make(AethumFluxNodeBlockEntity::new,
                        AffinityBlocks.COPPER_PLATED_AETHUM_FLUX_NODE,
                        AffinityBlocks.STONE_BANDED_AETHUM_FLUX_NODE);

        public static final BlockEntityType<AethumFluxCacheBlockEntity> AETHUM_FLUX_CACHE =
                make(AethumFluxCacheBlockEntity::new, AffinityBlocks.AETHUM_FLUX_CACHE);

        public static final BlockEntityType<CreativeAethumFluxCacheBlockEntity> CREATIVE_AETHUM_FLUX_CACHE =
                make(CreativeAethumFluxCacheBlockEntity::new, AffinityBlocks.CREATIVE_AETHUM_FLUX_CACHE);

        public static final BlockEntityType<RitualSocleBlockEntity> RITUAL_SOCLE =
                make(RitualSocleBlockEntity::new,
                        AffinityBlocks.RUDIMENTARY_RITUAL_SOCLE,
                        AffinityBlocks.REFINED_RITUAL_SOCLE,
                        AffinityBlocks.SOPHISTICATED_RITUAL_SOCLE);

        public static final BlockEntityType<BlankRitualSocleBlockEntity> BLANK_RITUAL_SOCLE =
                make(BlankRitualSocleBlockEntity::new, AffinityBlocks.BLANK_RITUAL_SOCLE);

        public static final BlockEntityType<AspRiteCoreBlockEntity> ASP_RITE_CORE =
                make(AspRiteCoreBlockEntity::new, AffinityBlocks.ASP_RITE_CORE);
        public static final BlockEntityType<SpiritIntegrationApparatusBlockEntity> SPIRIT_INTEGRATION_APPARATUS =
                make(SpiritIntegrationApparatusBlockEntity::new, AffinityBlocks.SPIRIT_INTEGRATION_APPARATUS);

        public static final BlockEntityType<AffineInfuserBlockEntity> AFFINE_INFUSER =
                make(AffineInfuserBlockEntity::new, AffinityBlocks.AFFINE_INFUSER);

        public static final BlockEntityType<SundialBlockEntity> SUNDIAL =
                make(SundialBlockEntity::new, AffinityBlocks.SUNDIAL);

        public static final BlockEntityType<ArborealAnnihilationApparatusBlockEntity> ARBOREAL_ANNIHILATION_APPARATUS =
                make(ArborealAnnihilationApparatusBlockEntity::new, AffinityBlocks.ARBOREAL_ANNIHILATION_APPARATUS);

        public static final BlockEntityType<CropReaperBlockEntity> CROP_REAPER =
                make(CropReaperBlockEntity::new, AffinityBlocks.CROP_REAPER);

        public static final BlockEntityType<AffineCandleBlockEntity> AFFINE_CANDLE =
                make(AffineCandleBlockEntity::new, AffinityBlocks.AFFINE_CANDLE);

        public static final BlockEntityType<WorldPinBlockEntity> WORLD_PIN =
                make(WorldPinBlockEntity::new, AffinityBlocks.WORLD_PIN);

        public static final BlockEntityType<SunshineMonolithBlockEntity> SUNSHINE_MONOLITH =
                make(SunshineMonolithBlockEntity::new, AffinityBlocks.SUNSHINE_MONOLITH);

        public static final BlockEntityType<AssemblyAugmentBlockEntity> ASSEMBLY_AUGMENT =
                make(AssemblyAugmentBlockEntity::new, AffinityBlocks.ASSEMBLY_AUGMENT);

        public static final BlockEntityType<MangroveBasketBlockEntity> MANGROVE_BASKET =
                make(MangroveBasketBlockEntity::new, AffinityBlocks.MANGROVE_BASKET);

        public static final BlockEntityType<StaffPedestalBlockEntity> STAFF_PEDESTAL =
                make(StaffPedestalBlockEntity::new, AffinityBlocks.STAFF_PEDESTAL);

        public static final BlockEntityType<ItemTransferNodeBlockEntity> ITEM_TRANSFER_NODE =
                make(ItemTransferNodeBlockEntity::new, AffinityBlocks.ITEM_TRANSFER_NODE);

        public static final BlockEntityType<AethumProbeBlockEntity> AETHUM_PROBE =
                make(AethumProbeBlockEntity::new, AffinityBlocks.AETHUM_PROBE);

        public static final BlockEntityType<OuijaBoardBlockEntity> OUIJA_BOARD =
                make(OuijaBoardBlockEntity::new, AffinityBlocks.OUIJA_BOARD);

        public static final BlockEntityType<MatterHarvestingHearthBlockEntity> MATTER_HARVESTING_HEARTH =
                make(MatterHarvestingHearthBlockEntity::new, AffinityBlocks.MATTER_HARVESTING_HEARTH);

        public static final BlockEntityType<VoidBeaconBlockEntity> VOID_BEACON =
                make(VoidBeaconBlockEntity::new, AffinityBlocks.VOID_BEACON);

        public static final BlockEntityType<FieldCoherenceModulatorBlockEntity> FIELD_COHERENCE_MODULATOR =
                make(FieldCoherenceModulatorBlockEntity::new, AffinityBlocks.FIELD_COHERENCE_MODULATOR);

        public static final BlockEntityType<HolographicStereopticonBlockEntity> HOLOGRAPHIC_STEREOPTICON =
                make(HolographicStereopticonBlockEntity::new, AffinityBlocks.HOLOGRAPHIC_STEREOPTICON);

        public static final BlockEntityType<GravitonTransducerBlockEntity> GRAVITON_TRANSDUCER =
                make(GravitonTransducerBlockEntity::new, AffinityBlocks.GRAVITON_TRANSDUCER);

        public static final BlockEntityType<EtherealAethumFluxNodeBlockEntity> ETHEREAL_AETHUM_FLUX_NODE =
                make(EtherealAethumFluxNodeBlockEntity::new, AffinityBlocks.ETHEREAL_AETHUM_FLUX_NODE);

        public static final BlockEntityType<EtherealAethumFluxInjectorBlockEntity> ETHEREAL_AETHUM_FLUX_INJECTOR =
                make(EtherealAethumFluxInjectorBlockEntity::new, AffinityBlocks.ETHEREAL_AETHUM_FLUX_INJECTOR);

        public static final BlockEntityType<LocalDisplacementGatewayBlockEntity> LOCAL_DISPLACEMENT_GATEWAY =
                make(LocalDisplacementGatewayBlockEntity::new, AffinityBlocks.LOCAL_DISPLACEMENT_GATEWAY);

        public static final BlockEntityType<AzaleaChestBlockEntity> AZALEA_CHEST =
                make(AzaleaChestBlockEntity::new, AffinityBlocks.AZALEA_CHEST);

        @Override
        public void afterFieldProcessing() {
            Affinity.AETHUM_MEMBER.registerSelf(AETHUM_FLUX_NODE);
            Affinity.AETHUM_NODE.registerSelf(AETHUM_FLUX_NODE);

            Affinity.AETHUM_MEMBER.registerSelf(AETHUM_FLUX_CACHE);
            Affinity.AETHUM_MEMBER.registerSelf(CREATIVE_AETHUM_FLUX_CACHE);
            Affinity.AETHUM_MEMBER.registerSelf(BREWING_CAULDRON);
            Affinity.AETHUM_MEMBER.registerSelf(SUNDIAL);
            Affinity.AETHUM_MEMBER.registerSelf(ARBOREAL_ANNIHILATION_APPARATUS);
            Affinity.AETHUM_MEMBER.registerSelf(ASP_RITE_CORE);
            Affinity.AETHUM_MEMBER.registerSelf(SPIRIT_INTEGRATION_APPARATUS);
            Affinity.AETHUM_MEMBER.registerSelf(AFFINE_INFUSER);
            Affinity.AETHUM_MEMBER.registerSelf(CROP_REAPER);
            Affinity.AETHUM_MEMBER.registerSelf(AFFINE_CANDLE);
            Affinity.AETHUM_MEMBER.registerSelf(SUNSHINE_MONOLITH);
            Affinity.AETHUM_MEMBER.registerSelf(STAFF_PEDESTAL);
            Affinity.AETHUM_MEMBER.registerSelf(MATTER_HARVESTING_HEARTH);
            Affinity.AETHUM_MEMBER.registerSelf(VOID_BEACON);
            Affinity.AETHUM_MEMBER.registerSelf(FIELD_COHERENCE_MODULATOR);
            Affinity.AETHUM_MEMBER.registerSelf(GRAVITON_TRANSDUCER);
            Affinity.AETHUM_MEMBER.registerSelf(ETHEREAL_AETHUM_FLUX_NODE);
        }

        @Override
        public Registry<BlockEntityType<?>> getRegistry() {
            return Registries.BLOCK_ENTITY_TYPE;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<BlockEntityType<?>> getTargetFieldType() {
            return (Class<BlockEntityType<?>>) (Object) BlockEntityType.class;
        }

        private static <T extends BlockEntity> BlockEntityType<T> make(FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
            return FabricBlockEntityTypeBuilder.create(factory, blocks).build();
        }
    }

    public static class Fluids implements AutoRegistryContainer<Fluid> {

        public static final FlowableFluid ARCANE_FADE = new ArcaneFadeFluid.Still();
        public static final FlowableFluid ARCANE_FADE_FLOWING = new ArcaneFadeFluid.Flowing();

        @Override
        public Registry<Fluid> getRegistry() {
            return Registries.FLUID;
        }

        @Override
        public Class<Fluid> getTargetFieldType() {
            return Fluid.class;
        }
    }

    @Override
    public void postProcessField(String namespace, Block value, String identifier, Field field) {
        if (field.isAnnotationPresent(NoBlockItem.class)) return;

        BiFunction<Block, OwoItemSettings, Item> factory = value instanceof BlockItemProvider provider
                ? provider::createBlockItem
                : BlockItem::new;

        Registry.register(Registries.ITEM, new Identifier(namespace, identifier),
                factory.apply(value, new OwoItemSettings()));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface Tab {
        int value();
    }
}

