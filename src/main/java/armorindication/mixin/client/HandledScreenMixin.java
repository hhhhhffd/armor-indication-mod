package armorindication.mixin.client;

import armorindication.render.NetheriteArmorTextureManager;
import java.util.Optional;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Replaces the vanilla slot icon inside handled GUI screens with a custom texture when a stack
 * matches the requested enchantment combination.
 *
 * <p>The mixin redirects the concrete {@link DrawContext#drawItem} calls performed by
 * {@link HandledScreen#drawSlot(DrawContext, Slot)}. This is the actual slot icon path used by
 * Minecraft 1.21.4, so vanilla overlays and slot logic continue to work while only the icon itself
 * is swapped out.</p>
 */
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    /**
     * Replaces the normal slot icon draw call with a custom PNG when the current stack qualifies.
     *
     * <p>If the stack does not match the requested enchantment rules, the original vanilla draw
     * call is executed unchanged.</p>
     */
    @Redirect(
        method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;III)V"
        )
    )
    private void armorIndication$redirectSlotDrawItem(
        DrawContext context,
        ItemStack stack,
        int x,
        int y,
        int seed
    ) {
        if (!armorIndication$drawCustomTexture(context, stack, x, y)) {
            context.drawItem(stack, x, y, seed);
        }
    }

    /**
     * Replaces the slot icon draw used for slots that disable dynamic display, which vanilla draws
     * through {@link DrawContext#drawItemWithoutEntity(ItemStack, int, int, int)}.
     */
    @Redirect(
        method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;III)V"
        )
    )
    private void armorIndication$redirectSlotDrawItemWithoutEntity(
        DrawContext context,
        ItemStack stack,
        int x,
        int y,
        int seed
    ) {
        if (!armorIndication$drawCustomTexture(context, stack, x, y)) {
            context.drawItemWithoutEntity(stack, x, y, seed);
        }
    }

    /**
     * Keeps the dragged/cursor item visually consistent with slot rendering by redirecting the
     * private helper that vanilla uses for carried stacks inside handled screens.
     */
    @Redirect(
        method = "drawItem(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;II)V"
        )
    )
    private void armorIndication$redirectCarriedItemDraw(
        DrawContext context,
        ItemStack stack,
        int x,
        int y
    ) {
        if (!armorIndication$drawCustomTexture(context, stack, x, y)) {
            context.drawItem(stack, x, y);
        }
    }

    /**
     * Draws the replacement icon if the stack matches the configured enchantment rules.
     *
     * @param context current GUI draw context
     * @param stack stack being rendered
     * @param x left coordinate of the icon
     * @param y top coordinate of the icon
     * @return {@code true} if a custom texture was drawn and vanilla icon rendering should be skipped
     */
    private static boolean armorIndication$drawCustomTexture(DrawContext context, ItemStack stack, int x, int y) {
        Optional<Identifier> replacementTexture = NetheriteArmorTextureManager.getReplacementTexture(stack);
        if (replacementTexture.isEmpty()) {
            return false;
        }

        // The texture fully replaces the vanilla GUI icon, so we draw a 16x16 PNG in the slot
        // area and let the surrounding vanilla logic continue with overlays and highlights.
        context.drawTexture(RenderLayer::getGuiTextured, replacementTexture.get(), x, y, 0.0F, 0.0F, 16, 16, 16, 16);
        return true;
    }
}
