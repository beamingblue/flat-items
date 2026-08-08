package blue.beaming.flatitems.injected.interfaces;

import net.minecraft.client.resources.model.geometry.BakedQuad;

import java.util.List;

public interface BakedQuadSupplier {
    List<BakedQuad>[] flat_items$quads();
}
