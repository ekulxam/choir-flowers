package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.Minecraft

class ChoirFlowersClient : ClientModInitializer {
    override fun onInitializeClient() {
        Minecraft.getInstance().soundManager.soundEngine.channelAccess
    }
}