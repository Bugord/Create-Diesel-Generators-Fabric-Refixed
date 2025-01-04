package com.jesz.createdieselgenerators;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jesz.createdieselgenerators.blocks.ct.SpriteShifts;
import com.jesz.createdieselgenerators.config.ConfigRegistry;
import com.jesz.createdieselgenerators.entity.EntityRegistry;
import com.jesz.createdieselgenerators.fluids.FluidRegistry;
import com.jesz.createdieselgenerators.items.ChemicalSprayerItemRenderer;
import com.jesz.createdieselgenerators.items.ItemRegistry;
import com.jesz.createdieselgenerators.items.LighterItemRenderer;
import com.jesz.createdieselgenerators.other.CDGPartialModel;
import com.jesz.createdieselgenerators.ponder.PonderIndex;
import com.jesz.createdieselgenerators.sounds.SoundRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.impl.ForgeConfigAPIPort;
import io.github.fabricators_of_create.porting_lib.util.ItemRendererHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.config.ModConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CreateDieselGeneratorsClient implements ClientModInitializer {

    public static Map<String, String> lighterSkins = new HashMap<>();



    @Override
    public void onInitializeClient() {
        PartialModels.init();
        SpriteShifts.init();
        //ModLoadingContext.registerConfig(CreateDieselGenerators.ID,  ModConfig.Type.CLIENT, ConfigRegistry.CLIENT_SPEC);
        ForgeConfigRegistry.INSTANCE.register(CreateDieselGenerators.ID, ModConfig.Type.CLIENT, ConfigRegistry.CLIENT_SPEC);
        ModelLoadingRegistry.INSTANCE.registerModelProvider(CreateDieselGeneratorsClient::onModelRegistry);
        //TODO??? CDGPartialModel::onModelBake;


        //BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.CHEMICAL_SPRAYER, new ChemicalSprayerItemRenderer());
        //BuiltinItemRendererRegistry.INSTANCE.register(ItemRegistry.LIGHTER, new LighterItemRenderer());

        PonderIndex.register();

        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), FluidRegistry.ETHANOL.get(), FluidRegistry.ETHANOL.get());
        //consumer.accept(SimpleCustomRenderer.create(this, new ChemicalSprayerItemRenderer()));
    }

    private static void onModelRegistry(ResourceManager resourceManager, Consumer<ResourceLocation> resourceLocationConsumer) {
        lighterSkins.clear();
        Minecraft.getInstance().getResourceManager().getNamespaces().stream().toList().forEach(n -> {
            Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(n, "lighter_skins.json"));
            if(resource.isEmpty())
                return;
            JsonParser parser = new JsonParser();
            try {
                JsonElement data = parser.parse(resource.get().openAsReader());
                data.getAsJsonArray().forEach(jsonElement -> {
                    lighterSkins.put(jsonElement.getAsJsonObject().getAsJsonPrimitive("name").getAsString(), jsonElement.getAsJsonObject().getAsJsonPrimitive("id").getAsString());
                });
            }catch (IOException ignored) {}
        });
        PartialModels.initSkins();
    }

    public static void onModelRegistry(){

        //CDGPartialModel.onModelRegistry(event);
    }
}
