package armorindication;

import net.fabricmc.api.ClientModInitializer;

/**
 * Client bootstrap for the mod.
 *
 * <p>The actual rendering behavior is injected through mixins, so the initializer does not need
 * to register runtime callbacks. Keeping this entrypoint explicit still makes the project complete
 * and keeps the mod metadata honest about being a client-only Fabric mod.</p>
 */
public final class ArmorIndicationClient implements ClientModInitializer {
    /**
     * Shared namespace used by textures and metadata files.
     */
    public static final String MOD_ID = "armor_indication";

    /**
     * Fabric calls this method once on the logical client during startup.
     *
     * <p>No code is required here because the GUI replacement is entirely driven by the mixin
     * pipeline. The method is intentionally empty and documented so the intent is obvious.</p>
     */
    @Override
    public void onInitializeClient() {
    }
}
