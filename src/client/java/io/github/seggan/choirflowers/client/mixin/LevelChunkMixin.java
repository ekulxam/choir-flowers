package io.github.seggan.choirflowers.client.mixin;

import io.github.seggan.choirflowers.client.ChoirFlowersManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void choirflowers$onSetBlockState(BlockPos pos, BlockState newState, int flags, CallbackInfoReturnable<BlockState> cir) {
        BlockState oldState = cir.getReturnValue();
        if (oldState != null) {
            if (newState.is(Blocks.CHORUS_FLOWER)) {
                ChoirFlowersManager.startSinging(pos);
            } else if (oldState.is(Blocks.CHORUS_FLOWER)) {
                ChoirFlowersManager.stopSinging(pos);
            }
        }
    }
}
