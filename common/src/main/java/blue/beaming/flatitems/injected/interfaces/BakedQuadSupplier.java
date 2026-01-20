package blue.beaming.flatitems.injected.interfaces;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public interface BakedQuadSupplier {
    List<BakedQuad>[] flat_items$quads();
}
