package com.jesz.createdieselgenerators.items;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.jesz.createdieselgenerators.CreateDieselGeneratorsClient;
import com.jesz.createdieselgenerators.PartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class LighterItemRenderer extends CustomRenderedItemModelRenderer {
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
        String skinName = CreateDieselGeneratorsClient.lighterSkins.containsKey(name) ? name : "standard";
        try {
            if (stack.getTag() == null || stack.getTag().getInt("Type") == 0)
                renderer.render(PartialModels.lighterSkinModels.get(skinName).getFirst().get(), light);
            else if (stack.getTag().getInt("Type") == 2)
                renderer.render(PartialModels.lighterSkinModels.get(skinName).getSecond().getSecond().get(), light);
            else
                renderer.render(PartialModels.lighterSkinModels.get(skinName).getSecond().getFirst().get(), light);

        } catch (NullPointerException e) {
            renderer.render(model.getOriginalModel(), light);
        }
    }
}
