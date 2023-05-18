package net.soul.tfcmars.world.feature;

import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.soul.tfcmars.world.MarsRocks;

import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.EnvironmentHelpers;
import net.dries007.tfc.world.settings.RockSettings;

public class MarsLooseRockFeature extends Feature<NoneFeatureConfiguration>
{
    public MarsLooseRockFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final Random random = context.random();

        final RockSettings rock = MarsRocks.getRock(level, pos);

        return rock.loose().map(loose -> {
            final BlockState stateAt = level.getBlockState(pos);
            final BlockState rockState = FluidHelpers.fillWithFluid(loose.defaultBlockState(), stateAt.getFluidState().getType());

            if (EnvironmentHelpers.isWorldgenReplaceable(stateAt) && rockState != null && rockState.canSurvive(level, pos))
            {
                setBlock(level, pos, rockState.setValue(TFCBlockStateProperties.COUNT_1_3, 1 + random.nextInt(2)));
                return true;
            }
            return false;
        }).orElse(false);
    }
}
