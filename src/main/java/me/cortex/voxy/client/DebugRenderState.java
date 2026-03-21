package me.cortex.voxy.client;

import me.cortex.voxy.client.core.util.GPUTiming;
import net.minecraft.client.Minecraft;

public final class DebugRenderState {
    private static boolean f3GpuDebugEnabled;

    private DebugRenderState() {
    }

    public static void updateFromDebugHud(boolean debugHudVisible) {
        if (f3GpuDebugEnabled == debugHudVisible) {
            return;
        }
        f3GpuDebugEnabled = debugHudVisible;

        GPUTiming.INSTANCE.setEnabled(debugHudVisible);
        RenderStatistics.enabled = debugHudVisible;

        var minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.levelRenderer != null) {
            minecraft.levelRenderer.allChanged();
        }
    }
}
