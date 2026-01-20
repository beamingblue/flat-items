package blue.beaming.flatitems.injected.mixin;

import blue.beaming.flatitems.FlatItems;
import blue.beaming.flatitems.injected.interfaces.BakedQuadSupplier;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemEntityRenderer.class) public class MixinItemEntityRenderer {
    // Disgusting, unidiomatic code x3 I hope that it's at least faster than abusing Streams.
    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private void census(PoseStack instance, Quaternionfc pose, @Local(argsOnly = true) ItemEntityRenderState iers, @Local(argsOnly = true) CameraRenderState crs) {
        if (!FlatItems.settings().enabled() || iers.count == 0) {
            cancel(instance, pose);
            return;
        }

        final var affect = FlatItems.settings().affect3D();
        var is3D = false;
        List<BakedQuad>[] lists = ((BakedQuadSupplier) iers.item).flat_items$quads();

        for (final var ql : lists) for (final var q : ql) {
            final var vd = Mth.abs(Float.intBitsToFloat(q.vertices()[2]) - Float.intBitsToFloat(q.vertices()[18]));
            is3D = vd > 0.0625f || vd < -0.0625f;
            if (!affect && is3D) {
                cancel(instance, pose);
                return;
            } else if (is3D) {
                unspin(instance, iers, crs);
                if (!FlatItems.settings().renderSides()) flatten(lists);
                if (FlatItems.settings().enlarge3D()) instance.scale(2f, 2f, 2f);
                return;
            }
        }

        unspin(instance, iers, crs);
        if (!FlatItems.settings().renderSides()) flatten(lists);
    }

    @Unique private static void unspin(PoseStack pose, ItemEntityRenderState iers, CameraRenderState crs) {
        FlatItems.settings().facing().unspin(pose, iers, crs);
    }

    @Unique private static void flatten(List<BakedQuad>[] quadLists) {
        for (final var ql : quadLists) {
            final var it = ql.listIterator();
            while (it.hasNext()) {
                final var q = it.next();
                it.remove();
                if (q.direction() == Direction.SOUTH) {
                    it.add(new BakedQuad(q.vertices(), q.tintIndex(), q.direction(), q.sprite(), false, q.lightEmission()));
                }
            }
        }
    }

    @Unique private static void cancel(PoseStack pose, Quaternionfc orientation) {
        pose.mulPose(orientation);
    }
}
