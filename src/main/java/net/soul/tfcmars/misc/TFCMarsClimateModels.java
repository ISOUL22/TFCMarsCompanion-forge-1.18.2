package net.soul.tfcmars.misc;

import java.util.function.Supplier;

import net.minecraftforge.common.util.Lazy;
import net.soul.tfcmars.TFCMars;

import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;

public final class TFCMarsClimateModels
{
    public static final Supplier<ClimateModelType> MARS = register("mars", MarsClimateModel::new);

    public static void registerModels()
    {
        MARS.get();
    }

    private static Supplier<ClimateModelType> register(String id, Supplier<ClimateModel> model)
    {
        return Lazy.of(() -> Climate.register(TFCMars.identifier(id), model));
    }
}
