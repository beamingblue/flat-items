package blue.beaming.flatitems.injected.mixin;

import blue.beaming.flatitems.FlatItems;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemRenderer.class) public class MixinItemRenderer {
    /*@Redirect(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFII)V"))
    private static void isFacingSouth(VertexConsumer instance, PoseStack.Pose pose, BakedQuad quad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay) {
        if (quad.direction() == Direction.SOUTH) instance.putBulkData(pose, quad, red, green, blue, alpha, packedLight, packedOverlay);
    }*/
    @ModifyArg(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;[III)V"), index = 2)
    private static List<BakedQuad> isFacingNorth(List<BakedQuad> quads, @Local(argsOnly = true) ItemDisplayContext context, @Local(argsOnly = true) PoseStack pose) {
        if (!FlatItems.settings().enabled()
                || context.firstPerson()
                || context == ItemDisplayContext.GUI
                || FlatItems.settings().renderSides())
            return quads;

        if (!FlatItems.settings().affect3D()) {
            for (BakedQuad quad : quads) if (quad.direction() != Direction.SOUTH
                    && quad.direction() != Direction.NORTH
                    && Mth.abs(Float.intBitsToFloat(quad.vertices()[2]) - Float.intBitsToFloat(quad.vertices()[18])) > 0.0625f) {
                return quads;
            }
        }

        var l = new ArrayList<>(quads);
        l.removeIf(q -> q.direction() != Direction.SOUTH);
        return l;
    }
}