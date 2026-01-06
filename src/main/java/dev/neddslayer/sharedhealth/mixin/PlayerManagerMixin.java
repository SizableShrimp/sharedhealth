package dev.neddslayer.sharedhealth.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.neddslayer.sharedhealth.components.SharedComponentsInitializer.SHARED_HEALTH;

@Mixin(PlayerManager.class)
abstract class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At("TAIL"))
    private void afterRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity newPlayer = cir.getReturnValue();

        newPlayer.setHealth(SHARED_HEALTH.get(newPlayer.getServerWorld().getScoreboard()).getHealth());
    }

    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/CustomPayloadS2CPacket;<init>(Lnet/minecraft/util/Identifier;" +
                                                                            "Lnet/minecraft/network/PacketByteBuf;)V"))
    private void handlePlayerConnection(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        player.setHealth(SHARED_HEALTH.get(player.getServerWorld().getScoreboard()).getHealth());
    }
}