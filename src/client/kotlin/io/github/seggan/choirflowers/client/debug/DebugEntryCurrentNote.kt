package io.github.seggan.choirflowers.client.debug

import io.github.seggan.choirflowers.client.MOD_ID
import io.github.seggan.choirflowers.client.SingingChorusFlower
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.resources.Identifier
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

object DebugEntryCurrentNote : DebugScreenEntry {
    override fun display(
        displayer: DebugScreenDisplayer,
        level: Level?,
        clientChunk: LevelChunk?,
        serverChunk: LevelChunk?
    ) {
        val camera = Minecraft.getInstance().cameraEntity ?: return

        val hitResult = camera.pick(20.0, 0.0f, false)
        if (hitResult.type != HitResult.Type.BLOCK) return
        val flower = SingingChorusFlower.getInstance((hitResult as BlockHitResult).blockPos) ?: return
        displayer.addToGroup(ID, "Current note: ${flower.pitch}")
        displayer.addToGroup(ID, "Scale: ${flower.scale}")
    }

    val ID = Identifier.fromNamespaceAndPath(MOD_ID, "current_note")!!
}