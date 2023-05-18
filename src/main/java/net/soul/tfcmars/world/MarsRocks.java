package net.soul.tfcmars.world;

import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import cpw.mods.util.Lazy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.settings.RockSettings;

public class MarsRocks
{
    public static final Supplier<RockSettings> BASALT = Suppliers.memoize(() -> getRock("basalt"));
    public static final Supplier<RockSettings> SHALE = Suppliers.memoize(() -> getRock("shale"));
    public static final Supplier<RockSettings> CONGLOMERATE = Suppliers.memoize(() -> getRock("conglomerate"));

    public static RockSettings getRock(String path)
    {
        return RockSettings.getDefaults().get(Helpers.identifier(path));
    }

    public static RockSettings getRock(LevelAccessor level, BlockPos pos)
    {
        return getRock(level, pos.getX(), pos.getY(), pos.getZ());
    }

    public static RockSettings getRock(LevelAccessor level, int x, int y, int z)
    {
        //final Holder<Biome> biome = level.getBiome(pos);
        if (y < 32)
        {
            return BASALT.get();
        }
        if (y < 100)
        {
            return SHALE.get();
        }
        return CONGLOMERATE.get();
    }

}
