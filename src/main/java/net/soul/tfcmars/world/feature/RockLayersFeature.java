package net.soul.tfcmars.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.soul.tfcmars.block.ModBlocks;
import net.soul.tfcmars.block.custom.Dust;
import net.soul.tfcmars.misc.MarsBiomeTags;
import net.soul.tfcmars.world.MarsRocks;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;

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
        final Block redSand = TFCBlocks.SAND.get(SandBlockType.RED).get();
        final Noise2D noise = new OpenSimplex2D(context.level().getSeed()).octaves(2).spread(0.02f);

        final int[] heights = new int[16 * 16];
        final Block[] dusts = new Block[16 * 16];
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                heights[x + (16 * z)] = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x + pos.getX(), z + pos.getZ());
                final float value = noise.noise(pos.getX() + x, pos.getZ() + z) + 0.1f * context.random().nextFloat() - 0.05f;
                final Holder<Biome> biome = level.getNoiseBiome(x + pos.getX(), 64, z + pos.getZ());
                final Dust theDust = getDust(biome);
                if (theDust != null)
                {
                    dusts[x + (16 * z)] = ModBlocks.DUSTS.get(theDust).get(getVariant(value)).get();
                }
                else
                {
                    dusts[x + (16 * z)] = Blocks.AIR;
                }
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
                    final BlockState stateAt = level.getBlockState(cursor);
                    if (Helpers.isBlock(stateAt, Blocks.STONE))
                    {
                        level.setBlock(cursor, raw, 2);
                    }
                    if (Helpers.isBlock(stateAt, redSand))
                    {
                        final Block sand = dusts[x + (16 * z)];
                        if (sand != Blocks.AIR)
                        {
                            level.setBlock(cursor, sand.defaultBlockState(), 2);
                        }
                    }
                }
            }

        }
        return true;
    }

    private static Dust.Variant getVariant(float value)
    {
        Dust.Variant variant;
        if (value > 0.3)
        {
            variant = Dust.Variant.DARK;
        }
        else if (value > 0.15)
        {
            variant = Dust.Variant.DUSTY;
        }
        else if (value > -0.1)
        {
            variant = Dust.Variant.SPARSE;
        }
        else if (value > -0.3)
        {
            variant = Dust.Variant.ROCKY_DARK;
        }
        else
        {
            variant = Dust.Variant.ROCKY_LIGHT;
        }
        return variant;
    }

    @Nullable
    private static Dust getDust(Holder<Biome> biome)
    {
        Dust dust = null;
        if (biome.is(MarsBiomeTags.SANDSTONE_DUST))
        {
            dust = Dust.SANDSTONE;
        }
        else if (biome.is(MarsBiomeTags.CHERT_DUST))
        {
            dust = Dust.CHERT;
        }
        else if (biome.is(MarsBiomeTags.CLAYSTONE_DUST))
        {
            dust = Dust.CLAYSTONE;
        }
        return dust;
    }
}
