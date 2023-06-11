package net.soul.tfcmars.misc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.soul.tfcmars.block.custom.OxygenSource;
import net.soul.tfcmars.block.custom.PressurizerBlock;
import net.soul.tfcmars.capability.player.MarsPlayerDataCapability;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import net.dries007.tfc.util.Helpers;

public final class PlayerOxygenHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void tick(Player player)
    {
        final Level level = player.level;
        if (level.isClientSide) return;
        // detect indoors areas (should be done often to prevent player death)
        if (level.getGameTime() % 61 == 0)
        {
            final BlockPos source = findOxygenSource(level, player.blockPosition().above(), new BlockPos.MutableBlockPos(), new BoundingBox(player.blockPosition()).inflatedBy(8), PressurizerBlock.INGREDIENT, pos -> level.getBlockEntity(pos) instanceof OxygenSource src && src.isSupplyingOxygen());
            player.getCapability(MarsPlayerDataCapability.CAPABILITY).ifPresent(cap -> {
                if (source != null && !cap.isIndoors())
                {
                    LOGGER.info("Found source!");
                    cap.setIndoors(true);
                }
                else if (source == null && cap.isIndoors())
                {
                    LOGGER.info("Lost source!");
                    cap.setIndoors(false);
                }
            });
        }
        // tick oxygen if we are not indoors
        if (level.getGameTime() % 20 == 0)
        {
            player.getCapability(MarsPlayerDataCapability.CAPABILITY).ifPresent(cap -> {
                final int ox = cap.getOxygenTicks();
                if (!cap.isIndoors())
                {
                    if (ox > 0)
                    {
                        cap.setOxygenTicks(ox - 1);
                        cap.sync();
                    }
                }
                else if (ox < 10)
                {
                    cap.setOxygenTicks(20);
                }
                player.displayClientMessage(Helpers.literal("Oxygen: " + cap.getOxygenTicks()), true);
            });
        }
    }

    @Nullable
    private static BlockPos findOxygenSource(Level level, BlockPos startPos, BlockPos.MutableBlockPos mutable, BoundingBox bounds, Predicate<BlockState> wallPredicate, Predicate<BlockPos> wantedBlockPredicate)
    {
        final int maxSize = bounds.getXSpan() * bounds.getYSpan() * bounds.getZSpan();
        final Set<BlockPos> filled = new HashSet<>();
        final LinkedList<BlockPos> queue = new LinkedList<>();
        filled.add(startPos);
        queue.addFirst(startPos);

        while (!queue.isEmpty())
        {
            if (filled.size() > maxSize)
            {
                return null; // this means the floodfill failed to contain itself
            }
            BlockPos testPos = queue.removeFirst();
            for (Direction direction : Helpers.DIRECTIONS)
            {
                mutable.set(testPos).move(direction);
                if (!bounds.isInside(mutable))
                {
                    return null; // we are way outside the realm of possibility...
                }
                if (!filled.contains(mutable))
                {
                    final BlockState stateAt = level.getBlockState(mutable);
                    if (!wallPredicate.test(stateAt)) // proper walls prevent adding new blocks to the floodfill, essentially bounding it
                    {
                        if (wantedBlockPredicate.test(mutable))
                        {
                            return mutable.immutable();
                        }
                        // Valid flood fill location
                        BlockPos posNext = mutable.immutable();
                        queue.addFirst(posNext);
                        filled.add(posNext);
                    }

                }
            }
        }
        return null;
    }
}
