package org.hismeo.bendsanimator.client.mixin.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import org.hismeo.bendsanimator.client.api.IEntityModelGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonRenderer.DragonModel.class)
public class DragonModelMixin implements IEntityModelGetter {
    @Unique
    private ModelPart root;
    @Override
    public ModelPart getRoot() {
        return root;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void setRoot(ModelPart root, CallbackInfo ci){
        this.root = root;
    }
}
