package io.wispforest.affinity.block;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.stream.Stream;

public class CopperPlatedAetherFluxNodeBlock extends AbstractAetherFluxNodeBlock {

    private static final VoxelShape EMPTY_SHAPE;
    private static final VoxelShape SHAPE_WITH_SHARD;

    static {
        EMPTY_SHAPE = Stream.of(
                createCuboidShape(12, 2, 7, 13, 9, 9),
                createCuboidShape(2, 0, 2, 14, 2, 14),
                createCuboidShape(4, 2, 4, 12, 11, 12),
                createCuboidShape(10, 11, 4, 12, 19, 6),
                createCuboidShape(4, 11, 4, 6, 19, 6),
                createCuboidShape(4, 11, 10, 6, 19, 12),
                createCuboidShape(4, 17, 6, 12, 19, 10),
                createCuboidShape(6, 17, 4, 10, 19, 6),
                createCuboidShape(6, 17, 10, 10, 19, 12),
                createCuboidShape(6, 19, 6, 10, 20, 10),
                createCuboidShape(10, 11, 10, 12, 19, 12),
                createCuboidShape(5, 9, 11, 11, 12, 13),
                createCuboidShape(5, 9, 3, 11, 12, 5),
                createCuboidShape(11, 9, 3, 13, 12, 13),
                createCuboidShape(3, 9, 3, 5, 12, 13),
                createCuboidShape(3, 2, 7, 4, 9, 9),
                createCuboidShape(7, 2, 3, 9, 9, 4),
                createCuboidShape(7, 2, 12, 9, 9, 13)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

        SHAPE_WITH_SHARD = VoxelShapes.combineAndSimplify(EMPTY_SHAPE, createCuboidShape(7, 11, 7, 9, 17, 9), BooleanBiFunction.OR);
    }

    @Override
    protected VoxelShape getEmptyShape() {
        return EMPTY_SHAPE;
    }

    @Override
    protected VoxelShape getShapeWithShard() {
        return SHAPE_WITH_SHARD;
    }
}
