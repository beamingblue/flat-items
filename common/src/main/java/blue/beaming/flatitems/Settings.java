package blue.beaming.flatitems;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Function3;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.joml.Quaternionf;

public interface Settings {
    boolean enabled();
    boolean affect3D();
    boolean renderSides();
    boolean enlarge3D();
    Facing facing();

    enum Facing {
        PLAYER(Facing::player),
        SCREEN(Facing::screen);
        private final Function3<PoseStack, ItemEntityRenderState, CameraRenderState, Void> unspinner;

        private static Void player(PoseStack pose, ItemEntityRenderState iers, CameraRenderState crs) {
            final var p = crs.pos.subtract(iers.x, iers.y, iers.z).normalize();
            pose.mulPose(new Quaternionf().rotateYXZ((float) Math.atan2(p.x, p.z), (float) Math.asin(-p.y), 0f));
            return null;
        }

        private static Void screen(PoseStack pose, ItemEntityRenderState iers, CameraRenderState crs) {
            pose.mulPose(crs.orientation);
            return null;
        }

        Facing(Function3<PoseStack, ItemEntityRenderState, CameraRenderState, Void> unspinner) {
            this.unspinner = unspinner;
        }

        public void unspin(PoseStack pose, ItemEntityRenderState iers, CameraRenderState crs) {
            unspinner.apply(pose, iers, crs);
        }
    }
}
