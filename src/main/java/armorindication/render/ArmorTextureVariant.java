package armorindication.render;

/**
 * Represents the three possible visual outcomes for supported netherite armor items.
 *
 * <p>{@link #NONE} means the vanilla icon should be left untouched.
 * {@link #SPECIAL} means the item matches the required enchantments without Mending.
 * {@link #SPECIAL_MENDING} means the item matches the required enchantments and also has Mending.</p>
 */
public enum ArmorTextureVariant {
    NONE,
    SPECIAL,
    SPECIAL_MENDING;

    /**
     * Helps the render mixin ask a direct "should I replace the icon?" question.
     *
     * @return {@code true} when a custom texture should replace vanilla rendering
     */
    public boolean isCustom() {
        return this != NONE;
    }
}
