package com.singer.bettervillage.event;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.singer.bettervillage.DeepSeekChatAPI;

@Mod.EventBusSubscriber(modid = "betterVillage", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerInteractHandler {

    @SubscribeEvent
    public static void onRightClickVillager(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof Villager)) return;

        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        String reply = DeepSeekChatAPI.chat("你是谁？");
        player.sendSystemMessage(Component.literal("村民说：" + reply));
    }
}
