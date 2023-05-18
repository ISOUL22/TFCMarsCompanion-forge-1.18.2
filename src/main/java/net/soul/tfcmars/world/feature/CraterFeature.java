package net.soul.tfcmars.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.noise.Metaballs2D;

public class CraterFeature extends Feature<NoneFeatureConfiguration>
{
    public CraterFeature(Codec<NoneFeatureConfiguration> pCodec)
    {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        final var random = context.random();
        final int ballSize = Mth.nextInt(random, 5, 13);
        final Metaballs2D noise = new Metaballs2D(Helpers.fork(random), 6, 8, -0.12f * ballSize, 0.3f * ballSize, 0.3f * ballSize);

        final BlockPos pos = context.level().getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, context.origin());

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        final BlockState air = Blocks.AIR.defaultBlockState();

        float radius = ballSize * ballSize;
        final float sub = ballSize / 4f;
        for (int y = 0; y > -4; y--)
        {
            for (int x = -ballSize; x < ballSize; x++)
            {
                for (int z = -ballSize; z < ballSize; z++)
                {
                    if (noise.inside(x, z) && x * x + z * z < radius)
                    {
                        cursor.setWithOffset(pos, x, y, z);
                        setBlock(level, cursor, air);
                    }
                }
            }
            radius -= sub;
        }

        return true;
    }
}
