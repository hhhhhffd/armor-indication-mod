package armorindication.render;

import armorindication.ArmorIndicationClient;
import armorindication.util.NetheriteArmorEnchantmentUtil;
import java.util.Map;
import java.util.Optional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

/**
 * Resolves the concrete texture file that should replace the vanilla netherite armor icon.
 *
 * <p>The manager only answers "which PNG should be drawn?".
 * The decision about whether a stack is eligible lives in {@link NetheriteArmorEnchantmentUtil},
 * which keeps enchantment logic and resource lookup nicely separated.</p>
 */
public final class NetheriteArmorTextureManager {
    /**
     * Static mapping from each supported armor item to its base and Mending-specific texture.
     */
    private static final Map<Item, TextureSet> TEXTURES = Map.of(
        Items.NETHERITE_HELMET, new TextureSet(
            texture("netherite_helmet_special.png"),
            texture("netherite_helmet_special_mending.png")
        ),
        Items.NETHERITE_CHESTPLATE, new TextureSet(
            texture("netherite_chestplate_special.png"),
            texture("netherite_chestplate_special_mending.png")
        ),
        Items.NETHERITE_LEGGINGS, new TextureSet(
            texture("netherite_leggings_special.png"),
            texture("netherite_leggings_special_mending.png")
        ),
        Items.NETHERITE_BOOTS, new TextureSet(
            texture("netherite_boots_special.png"),
            texture("netherite_boots_special_mending.png")
        )
    );

    /**
     * Hidden constructor because this class is a pure registry/lookup helper.
     */
    private NetheriteArmorTextureManager() {
    }

    /**
     * Returns the exact replacement texture for the supplied stack if the enchantment rules allow it.
     *
     * @param stack the stack being rendered in a handled GUI screen
     * @return the replacement PNG identifier, or an empty result when vanilla rendering must be preserved
     */
    public static Optional<Identifier> getReplacementTexture(ItemStack stack) {
        ArmorTextureVariant variant = NetheriteArmorEnchantmentUtil.resolveTextureVariant(stack);
        if (!variant.isCustom()) {
            return Optional.empty();
        }

        TextureSet textureSet = TEXTURES.get(stack.getItem());
        if (textureSet == null) {
            return Optional.empty();
        }

        return Optional.of(switch (variant) {
            case SPECIAL -> textureSet.special();
            case SPECIAL_MENDING -> textureSet.specialMending();
            case NONE -> throw new IllegalStateException("Non-custom variant should have returned earlier");
        });
    }

    /**
     * Builds a resource identifier for a PNG under assets/armor_indication/textures/item.
     *
     * @param fileName the physical PNG file name
     * @return a full texture identifier ready for DrawContext#drawTexture
     */
    private static Identifier texture(String fileName) {
        return Identifier.of(ArmorIndicationClient.MOD_ID, "textures/item/" + fileName);
    }

    /**
     * Small immutable holder for the two texture options of a single armor piece.
     *
     * @param special base custom icon used when Mending is absent
     * @param specialMending custom icon used when Mending is present
     */
    private record TextureSet(Identifier special, Identifier specialMending) {
    }
}
