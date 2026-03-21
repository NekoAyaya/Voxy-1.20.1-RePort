package me.cortex.voxy.client;

import me.cortex.voxy.client.config.VoxyConfig;
import me.cortex.voxy.commonImpl.VoxyCommon;

public final class ClientSessionEvents {
    private ClientSessionEvents() {
    }

    public static volatile boolean inSession = false;

    public static void sessionStart() {
        if (inSession) {
            throw new IllegalStateException("Cannot start a new session while already in session");
        }
        inSession = true;

        if (VoxyCommon.getInstance() != null) {
            throw new IllegalStateException("Voxy instance should not be active before session start");
        }

        if (VoxyCommon.isAvailable() && VoxyConfig.CONFIG.enabled) {
            VoxyCommon.createInstance();
        }
    }

    public static void sessionEnd() {
        if (!inSession) {
            throw new IllegalStateException("Cannot end session while not in session");
        }
        inSession = false;
        VoxyCommon.shutdownInstance();
    }
}
