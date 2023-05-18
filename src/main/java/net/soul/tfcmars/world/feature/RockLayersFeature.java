package net.soul.tfcmars.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.soul.tfcmars.world.MarsRocks;

import net.dries007.tfc.util.Helpers;

public class RockLayersFeature extends Feature<NoneFeatureConfiguration>
{
    public RockLayersFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();

        final int[] heights = new int[16 * 16];
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                heights[x + (16 * z)] = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x + pos.getX(), z + pos.getZ());
            }
        }
        for (int y = context.level().getMinBuildHeight(); y < context.level().getMaxBuildHeight(); y++)
        {
            final BlockState raw = MarsRocks.getRock(level, cursor).raw().defaultBlockState();
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    if (y > heights[x + (16 * z)])
                    {
                        continue;
                    }
                    cursor.setWithOffset(pos, x, 0, z).setY(y);
                    if (Helpers.isBlock(level.getBlockState(cursor), Blocks.STONE))
                    {
                        level.setBlock(cursor, raw, 2);
                    }
                }
            }

        }
        return true;
    }
}
