package net.soul.tfcmars.block.custom;

import java.util.Random;
import com.eerussianguy.firmalife.common.blocks.FLStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.soul.tfcmars.blockentity.VentBlockEntity;

import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;

public class VentBlock extends HorizontalDirectionalBlock implements EntityBlockExtension
{
    public static final BooleanProperty STASIS = FLStateProperties.STASIS;
    private final ExtendedProperties properties;

    public VentBlock(ExtendedProperties properties)
    {
        super(properties.properties());
        this.properties = properties;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STASIS, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        if (level.getBlockEntity(pos) instanceof VentBlockEntity vent)
        {
            vent.checkPressure();
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
    {
        if (state.getValue(STASIS))
        {
            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D)
            {
                level.playLocalSound(x, y, z, SoundEvents.ARMOR_STAND_FALL, SoundSource.BLOCKS, 0.5F, 1.0F, false);
            }

            final Direction direction = state.getValue(FACING);
            final Direction.Axis direction$axis = direction.getAxis();
            double offset = 0.52D;
            double horizontalScale = random.nextDouble() * 0.6D - 0.3D;
            double dx = direction$axis == Direction.Axis.X ? direction.getStepX() * offset : horizontalScale;
            double dz = direction$axis == Direction.Axis.Z ? direction.getStepZ() * offset : horizontalScale;
            double verticalScale = random.nextDouble() * 6.0D / 16.0D;

            level.addParticle(ParticleTypes.SMOKE, x + dx, y + verticalScale, z + dz, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public ExtendedProperties getExtendedProperties()
    {
        return properties;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(FACING, STASIS));
    }
}
