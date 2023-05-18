package net.soul.tfcmars.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.soul.tfcmars.world.MarsRocks;

import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.entities.TFCFallingBlockEntity;
import net.dries007.tfc.common.recipes.LandslideRecipe;
import net.dries007.tfc.util.Helpers;

public class MarsErosionFeature extends Feature<NoneFeatureConfiguration>
{
    public MarsErosionFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();

        final ChunkPos chunkPos = new ChunkPos(pos);
        final int chunkX = chunkPos.getMinBlockX(), chunkZ = chunkPos.getMinBlockZ();
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        final BlockState sandstone = TFCBlocks.SANDSTONE.get(SandBlockType.RED).get(SandstoneBlockType.RAW).get().defaultBlockState();

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                // Top down iteration, attempt to either fix unstable locations, or remove the offending blocks.
                final int baseHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkX + x, chunkZ + z);
                boolean prevBlockCanLandslide = false;
                int lastSafeY = baseHeight;
                Block prevBlockHardened = null;

                mutablePos.set(chunkX + x, baseHeight, chunkZ + z);

                for (int y = baseHeight; y >= context.chunkGenerator().getMinY(); y--)
                {
                    mutablePos.setY(y);

                    BlockState stateAt = level.getBlockState(mutablePos);
                    final boolean isSandAt = Helpers.isBlock(stateAt, BlockTags.SAND);
                    LandslideRecipe recipe = stateAt.isAir() ? null : LandslideRecipe.getRecipe(stateAt);
                    boolean stateAtIsFragile = stateAt.isAir() || TFCFallingBlockEntity.canFallThrough(level, mutablePos, stateAt);
                    if (prevBlockCanLandslide)
                    {
                        // Continuing a collapsible downwards
                        // If the block is also collapsible, we just continue until we reach either the bottom (solid) or something to collapse through
                        if (recipe == null)
                        {
                            // This block is sturdy, preventing the column from collapsing
                            // However, we need to make sure we can't collapse *through* this block
                            if (stateAtIsFragile)
                            {
                                // We can collapse through the current block. aka, from [y + 1, lastSafeY) need to collapse
                                // If we would only collapse one block, we remove it. Otherwise, we replace the lowest block with hardened stone
                                if (lastSafeY > y + 2)
                                {
                                    // More than one block to collapse, so we can support instead
                                    mutablePos.setY(y + 1);

                                    level.setBlock(mutablePos, isSandAt ? sandstone : MarsRocks.getRock(level, x, y + 1, z).hardened().defaultBlockState(), 2);
                                }
                                else
                                {
                                    // See if we can delete the block above (if the above of that is air)
                                    // We then choose either a solid or full block by passing in a positive or negative value to the aquifer's computeState
                                    mutablePos.setY(y + 2);
                                    final boolean blockAboveIsAir = level.getBlockState(mutablePos).isAir();

                                    mutablePos.setY(y + 1);
                                    final BlockState airOrLiquidState = Blocks.AIR.defaultBlockState();

                                    if (blockAboveIsAir)
                                    {
                                        level.setBlock(mutablePos, airOrLiquidState, 2);
                                    }
                                    else
                                    {
                                        // Otherwise, we have to support the block, and the only way we can is by placing stone.
                                        mutablePos.setY(y + 1);
                                        level.setBlock(mutablePos, isSandAt ? sandstone : MarsRocks.getRock(level, x, y + 1, z).hardened().defaultBlockState(),2);
                                    }
                                }
                            }
                            prevBlockCanLandslide = false;
                            lastSafeY = y;
                        }
                    }
                    else
                    {
                        // Last block is sturdy
                        if (recipe == null)
                        {
                            // This block is sturdy
                            lastSafeY = y;
                        }
                        else
                        {
                            // This block can collapse. lastSafeY will already be y + 1, so all we need to mark is the prev flag for next iteration
                            prevBlockCanLandslide = true;
                        }
                    }

                    // Update stone from raw -> hardened
                    if (stateAtIsFragile)
                    {
                        if (prevBlockHardened != null)
                        {
                            mutablePos.setY(y + 1);
                            level.setBlock(mutablePos, prevBlockHardened.defaultBlockState(), 2);
                        }
                        prevBlockHardened = null;
                    }
                    else
                    {
                        prevBlockHardened = isSandAt ? sandstone.getBlock() : MarsRocks.getRock(level, pos).hardened();
                    }
                }
            }
        }
        return true;
    }
}
