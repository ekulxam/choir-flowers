package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.Minecraft

class ChoirFlowersClient : ClientModInitializer {
    override fun onInitializeClient() {
        TODO("Not yet implemented")
        Minecraft.getInstance().soundManager.play()
    }

    companion object {
        const val MOD_ID = "choir-flowers"
    }
}