package net.soul.tfcmars.block.custom;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import com.eerussianguy.firmalife.common.blocks.FLStateProperties;
import com.eerussianguy.firmalife.common.util.Mechanics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.soul.tfcmars.blockentity.PressurizerBlockEntity;
import net.soul.tfcmars.misc.MarsBlockTags;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredients;
import net.dries007.tfc.util.Helpers;

public class PressurizerBlock extends ExtendedBlock implements EntityBlockExtension
{
    public static void denyAll(Level level, BlockPos pos)
    {
        withBlockEntity(level, pos, p -> p.updateValidity(false));
    }

    private static Set<BlockPos> check(Level level, BlockPos pos, BlockState state)
    {
        if (level.getBlockEntity(pos) instanceof PressurizerBlockEntity pressurizer)
        {
            final Set<BlockPos> set = getRoom(level, pos, pressurizer.getPositions().isEmpty() ? -1 : pressurizer.getPositions().size());
            pressurizer.setPositions(set);
            pressurizer.updateValidity(true);
            updateState(level, pos, state, true);
            return set;
        }
        else
        {
            denyAll(level, pos);
            updateState(level, pos, state, false);
        }
        return Collections.emptySet();
    }

    private static Set<BlockPos> getRoom(Level level, BlockPos pos, int lastSize)
    {
        return Mechanics.floodfill(level, pos, new BlockPos.MutableBlockPos(), new BoundingBox(pos).inflatedBy(15), INGREDIENT, s -> true, false, lastSize, Helpers.DIRECTIONS);
    }

    public static final BlockIngredient INGREDIENT = BlockIngredients.of(MarsBlockTags.BASE_INSULATION);

    private static void withBlockEntity(Level level, BlockPos pos, Consumer<PressurizerBlockEntity> consumer)
    {
        if (level.getBlockEntity(pos) instanceof PressurizerBlockEntity pressurizer)
        {
            consumer.accept(pressurizer);
        }
    }

    private static void updateState(Level level, BlockPos pos, BlockState state, boolean valid)
    {
        if (state.getValue(STASIS) != valid)
        {
            level.setBlockAndUpdate(pos, state.cycle(STASIS));
        }
    }

    public static final BooleanProperty STASIS = FLStateProperties.STASIS;

    public PressurizerBlock(ExtendedProperties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(STASIS, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        final Set<BlockPos> set = check(level, pos, state);
        if (!set.isEmpty())
        {
            player.displayClientMessage(Helpers.translatable("tfcmars.tooltip.room_found", set.size()), true);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random)
    {
        check(level, pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean pIsMoving)
    {
        denyAll(level, pos);
        super.onRemove(state, level, pos, newState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(STASIS));
    }

}
