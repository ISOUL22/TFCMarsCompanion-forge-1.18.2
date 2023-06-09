package net.soul.tfcmars.entity.client.armor;

import net.soul.tfcmars.item.custom.T3SuitItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class T3SuitRenderer extends GeoArmorRenderer<T3SuitItem> {
    public T3SuitRenderer() {
        super(new T3SuitModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
