package io.github.seggan.choirflowers.client.mixin;

import io.github.seggan.choirflowers.client.ChoirFlowers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "blockChanged", at = @At("HEAD"))
    private void choirflowers$onBlockChanged(BlockGetter level, BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        if (oldState == null) return;
        if (newState.is(Blocks.CHORUS_FLOWER)) {
            ChoirFlowers.startSinging(pos);
        } else if (oldState.is(Blocks.CHORUS_FLOWER)) {
            ChoirFlowers.stopSinging(pos);
        }
    }
}
