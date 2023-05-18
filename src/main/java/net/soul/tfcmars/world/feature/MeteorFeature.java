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
import net.soul.tfcmars.block.ModBlocks;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.noise.Metaballs3D;

public class MeteorFeature extends Feature<NoneFeatureConfiguration>
{

    public MeteorFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final var random = context.random();
        final int outer = Mth.nextInt(random, 8, 12);
        final int outerSq = outer * outer;
        final BlockPos pos = context.level().getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, context.origin());
        final WorldGenLevel level = context.level();
        if (!level.getFluidState(pos).isEmpty()) return false;

        final int ballSize = Mth.nextInt(random, 4, 6);
        final Metaballs3D noise = new Metaballs3D(Helpers.fork(random), 6, 8, -0.12f * ballSize, 0.3f * ballSize, 0.3f * ballSize);

        final BlockState air = Blocks.AIR.defaultBlockState();
        final BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        final BlockState basalt = TFCBlocks.ROCK_BLOCKS.get(Rock.BASALT).get(Rock.BlockType.RAW).get().defaultBlockState();
        final BlockState meteor = ModBlocks.METEORITE.get().defaultBlockState();
        final BlockState sandstone = TFCBlocks.SANDSTONE.get(SandBlockType.RED).get(SandstoneBlockType.RAW).get().defaultBlockState();

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = -outer; x <= outer; x++)
        {
            for (int z = -outer; z <= outer; z++)
            {
                for (int y = -outer; y <= outer; y++)
                {
                    final int fac = x * x + y * y + z * z;
                    if (fac < outerSq)
                    {
                        mutable.setWithOffset(pos, x, y + 2, z);
                        if (outerSq - fac < 3)
                        {
                            BlockState stateAt = level.getBlockState(mutable);
                            if (Helpers.isBlock(stateAt, TFCTags.Blocks.CAN_CARVE))
                            {
                                if (random.nextInt(3) == 0)
                                {
                                    setBlock(level, mutable, basalt);
                                }
                                else if (random.nextInt(3) == 0)
                                {
                                    setBlock(level, mutable, obsidian);
                                }
                                else if (random.nextInt(3) == 0)
                                {
                                    setBlock(level, mutable, air);
                                }
                            }
                        }
                        else if (level.getFluidState(pos).isEmpty())
                        {
                            setBlock(level, mutable, air);
                        }

                    }
                    if (noise.inside(x, y, z)) // make the meteorite
                    {
                        mutable.setWithOffset(pos, x, y - outer + 3, z);
                        setBlock(level, mutable, meteor);
                    }
                }
            }
        }

        for (int x = -outer; x <= outer; x++)
        {
            for (int z = -outer; z <= outer; z++)
            {
                if (random.nextInt(4) != 0)
                {
                    final int fac = x * x + z * z;
                    if (fac < outerSq)
                    {
                        int y = -outer;
                        boolean going = true;
                        while (going && y <= 1)
                        {
                            mutable.setWithOffset(pos, x, y + 2, z);
                            BlockState stateAt = level.getBlockState(mutable);
                            if (Helpers.isBlock(stateAt, TFCTags.Blocks.CAN_CARVE))
                            {
                                mutable.move(0, 1, 0);
                                if (level.getBlockState(mutable).isAir())
                                {
                                    mutable.move(0, -1, 0);
                                    setBlock(level, mutable, sandstone);
                                    going = false;
                                }
                            }
                            y++;
                        }
                    }
                }
            }
        }
        return true;
    }

}
