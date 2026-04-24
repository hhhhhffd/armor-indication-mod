package armorindication.util;

import armorindication.render.ArmorTextureVariant;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Set;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Centralizes the enchantment rules that decide whether a netherite armor stack should use a
 * custom GUI icon.
 *
 * <p>The class intentionally works with {@link ItemStack#getEnchantments()} and
 * {@link ItemEnchantmentsComponent}, which is the modern 1.21.4 component-based API rather than
 * older NBT-centric helpers.</p>
 */
public final class NetheriteArmorEnchantmentUtil {
    /**
     * Only the four netherite armor pieces participate in the custom icon logic.
     */
    private static final Set<Item> SUPPORTED_ITEMS = Set.of(
        Items.NETHERITE_HELMET,
        Items.NETHERITE_CHESTPLATE,
        Items.NETHERITE_LEGGINGS,
        Items.NETHERITE_BOOTS
    );

    /**
     * Hidden constructor because the utility is purely static.
     */
    private NetheriteArmorEnchantmentUtil() {
    }

    /**
     * Resolves the exact texture variant that should be used for the supplied stack.
     *
     * <p>The rules are intentionally literal:
     * Protection must be exactly level V, Unbreaking must be exactly level V, Thorns must be
     * completely absent, and Mending switches the result from the base special texture to the
     * dedicated Mending texture.</p>
     *
     * @param stack the stack currently being rendered in a GUI slot
     * @return the matching texture variant or {@link ArmorTextureVariant#NONE} when vanilla should stay
     */
    public static ArmorTextureVariant resolveTextureVariant(ItemStack stack) {
        // Empty stacks and unsupported items must always fall back to vanilla rendering.
        if (stack.isEmpty() || !SUPPORTED_ITEMS.contains(stack.getItem())) {
            return ArmorTextureVariant.NONE;
        }

        ItemEnchantmentsComponent enchantments = stack.getEnchantments();

        // Thorns is an unconditional blocker regardless of any other enchantments on the stack.
        if (getLevel(enchantments, Enchantments.THORNS) > 0) {
            return ArmorTextureVariant.NONE;
        }

        // The requested combination requires Protection V and Unbreaking V on the same stack.
        if (getLevel(enchantments, Enchantments.PROTECTION) != 5) {
            return ArmorTextureVariant.NONE;
        }
        if (getLevel(enchantments, Enchantments.UNBREAKING) != 5) {
            return ArmorTextureVariant.NONE;
        }

        // Mending changes only the chosen custom texture; it is not mandatory for the base variant.
        return getLevel(enchantments, Enchantments.MENDING) > 0
            ? ArmorTextureVariant.SPECIAL_MENDING
            : ArmorTextureVariant.SPECIAL;
    }

    /**
     * Reads the level of a specific enchantment directly from the component-based enchantment set.
     *
     * <p>Matching by {@link RegistryKey} keeps the code stable across registry entry instances and
     * avoids older helper methods that were more common before the modern component system.</p>
     *
     * @param enchantments the component extracted from the current stack
     * @param enchantmentKey the enchantment key to look for
     * @return the stored level, or {@code 0} if the enchantment is absent
     */
    private static int getLevel(ItemEnchantmentsComponent enchantments, RegistryKey<Enchantment> enchantmentKey) {
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> enchantmentEntry : enchantments.getEnchantmentEntries()) {
            if (enchantmentEntry.getKey().matchesKey(enchantmentKey)) {
                return enchantmentEntry.getIntValue();
            }
        }

        return 0;
    }
}
