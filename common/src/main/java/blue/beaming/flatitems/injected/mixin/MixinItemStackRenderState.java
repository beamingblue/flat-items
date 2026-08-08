package blue.beaming.flatitems.injected.mixin;

import blue.beaming.flatitems.injected.interfaces.BakedQuadSupplier;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemStackRenderState.class) public class MixinItemStackRenderState implements BakedQuadSupplier {
    @Shadow private ItemStackRenderState.LayerRenderState[] layers;

    @Override public List<BakedQuad>[] flat_items$quads() {
        var la = new List[this.layers.length];
        for (int i = 0; i < la.length; i++) {
            la[i] = this.layers[i].prepareQuadList();
        }
        //noinspection unchecked
        return la;
    }
}
