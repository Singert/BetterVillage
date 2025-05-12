package com.singer.bettervillage;

import com.singer.bettervillage.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

/**
 * BetterVillage 主类
 */
@Mod(BetterVillage.MODID)
public class BetterVillage {
    public static final String MODID = "bettervillage";
    private static final Logger LOGGER = LogUtils.getLogger();

    // 注册方块、物品、创意标签
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 村庄纪念碑方块和物品注册
    public static final RegistryObject<Block> VILLAGE_MONUMENT_BLOCK = BLOCKS.register("village_monument",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    public static final RegistryObject<Item> VILLAGE_MONUMENT_ITEM = ITEMS.register("village_monument",
            () -> new BlockItem(VILLAGE_MONUMENT_BLOCK.get(), new Item.Properties()));

    // 村民徽章道具（可食用）
    public static final RegistryObject<Item> VILLAGER_BADGE = ITEMS.register("villager_badge",
            () -> new Item(new Item.Properties().food(
                    new FoodProperties.Builder().alwaysEat().nutrition(1).saturationMod(2f).build()
            )));

    // 创意模式标签
    public static final RegistryObject<CreativeModeTab> BETTERVILLAGE_TAB = CREATIVE_MODE_TABS.register("bettervillage_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> VILLAGER_BADGE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(VILLAGER_BADGE.get());
                        output.accept(VILLAGE_MONUMENT_ITEM.get());
                    }).build()
    );

    public BetterVillage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册各类游戏元素
        registerContent(modEventBus);

        // 注册事件监听
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addToCreativeTab);
        MinecraftForge.EVENT_BUS.register(this);

        // 注册配置文件（可选）
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerContent(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[BetterVillage] 初始化完成");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(VILLAGE_MONUMENT_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[BetterVillage] 服务器启动事件触发");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[BetterVillage] 客户端初始化完成");
            LOGGER.info("MINECRAFT USER >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}