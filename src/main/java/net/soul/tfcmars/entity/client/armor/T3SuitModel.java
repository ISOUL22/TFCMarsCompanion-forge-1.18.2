package net.soul.tfcmars.entity.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.item.custom.T3SuitItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class T3SuitModel extends AnimatedGeoModel<T3SuitItem> {
    @Override
    public ResourceLocation getModelLocation(T3SuitItem t3SuitItem) {
        return new ResourceLocation(TFCMars.MOD_ID, "geo/t3_suit.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(T3SuitItem t3SuitItem) {
        return new ResourceLocation(TFCMars.MOD_ID, "textures/models/armor/t3_suit_base-2.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T3SuitItem t3SuitItem) {
        return new ResourceLocation(TFCMars.MOD_ID, "animations/armor_animation.json");
    }
}
