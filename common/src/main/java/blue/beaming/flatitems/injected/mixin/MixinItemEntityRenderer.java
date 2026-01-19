package blue.beaming.flatitems.injected.mixin;

import blue.beaming.flatitems.FlatItems;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntityRenderer.class) public class MixinItemEntityRenderer {
    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private void dontSpinWithoutAtLeastAskingMeFirst(PoseStack instance, Quaternionfc pose, @Local(argsOnly = true) ItemEntityRenderState iers, @Local(argsOnly = true) CameraRenderState crs) {
        if (FlatItems.settings().enabled() && !(iers.item.usesBlockLight() && !FlatItems.settings().affect3D()))
            instance.mulPose(crs.orientation);
        else instance.mulPose(pose);
    }
}
