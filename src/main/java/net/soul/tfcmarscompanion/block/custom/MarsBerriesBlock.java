package net.soul.tfcmarscompanion.block.custom;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;


public class MarsBerriesBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MarsBerriesBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE = (Block.box(0,0,0,16,2,16));

    /* FACING */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext){
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
