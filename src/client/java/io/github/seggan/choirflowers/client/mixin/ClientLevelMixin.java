package io.github.seggan.choirflowers.client.mixin;

import io.github.seggan.choirflowers.client.ChoirFlowersManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
    private void choirflowers$onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        if (newState.is(Blocks.CHORUS_FLOWER)) {
            ChoirFlowersManager.startSinging(pos);
        } else if (oldState.is(Blocks.CHORUS_FLOWER)) {
            ChoirFlowersManager.stopSinging(pos);
        }
    }
}
