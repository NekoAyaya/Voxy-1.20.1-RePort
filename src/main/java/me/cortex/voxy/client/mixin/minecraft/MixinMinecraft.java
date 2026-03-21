package me.cortex.voxy.client.mixin.minecraft;

import me.cortex.voxy.client.ClientSessionEvents;
import me.cortex.voxy.client.DebugRenderState;
import me.cortex.voxy.commonImpl.VoxyCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "tick()V", at = @At("TAIL"))
    private void voxy$syncF3RenderStatistics(CallbackInfo ci) {
        DebugRenderState.updateFromDebugHud(((Minecraft) (Object) this).options.renderDebug);
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("TAIL"), require = 0)
    private void voxy$injectWorldCloseModern(Screen screen, boolean isReloading, boolean skipGameLoad, CallbackInfo ci) {
        this.voxy$sessionEnd();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("TAIL"), require = 0)
    private void voxy$injectWorldCloseLegacy(Screen screen, CallbackInfo ci) {
        this.voxy$sessionEnd();
    }

    @Inject(method = "clearLevel()V", at = @At("TAIL"), require = 0)
    private void voxy$injectWorldCloseFallback(CallbackInfo ci) {
        this.voxy$sessionEnd();
    }

    private void voxy$sessionEnd() {
        if (ClientSessionEvents.inSession) {
            ClientSessionEvents.sessionEnd();
            VoxyCommon.onSessionLeave();
        }
    }

    /*
    @Inject(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setWorld(Lnet/minecraft/client/world/ClientWorld;)V", shift = At.Shift.BEFORE))
    private void voxy$injectInitialization(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
        if (VoxyConfig.CONFIG.enabled) {
            VoxyCommon.createInstance();
        }
    }*/
}
