package net.soul.tfcmars.world.feature;

import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.world.noise.Metaballs3D;

public class MarsBoulderFeature extends Feature<WeightedStateConfig>
{
    public MarsBoulderFeature(Codec<WeightedStateConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WeightedStateConfig> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final Random random = context.random();
        final IWeighted<BlockState> weighted = context.config().weighted();
        return place(level, pos, weighted, random);
    }

    private boolean place(WorldGenLevel level, BlockPos pos, IWeighted<BlockState> states, Random random)
    {
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        final int size = 3 + random.nextInt(7);
        final Metaballs3D noise = new Metaballs3D(Helpers.fork(random), 6, 8, -0.12f * size, 0.3f * size, 0.3f * size);

        if (pos.getY() + size > 127)
        {
            return false;
        }
        for (int x = -size; x <= size; x++)
        {
            for (int y = -size; y <= size; y++)
            {
                for (int z = -size; z <= size; z++)
                {
                    if (noise.inside(x, y, z))
                    {
                        mutablePos.setWithOffset(pos, x, y, z);
                        setBlock(level, mutablePos, states.get(random));
                    }
                }
            }
        }
        return true;
    }
}
