package cn.noryea.sodiumnofog.mixin;


import cn.noryea.sodiumnofog.SodiumNoFogClient;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;

import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;


import net.minecraft.client.option.GameOptions;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;


@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGUI {

    @Shadow(remap = false)
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Redirect(method = "general", remap = false,
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=options.gamma"),
                    to = @At(value = "CONSTANT", args = "stringValue=options.guiScale")
            ),
            at = @At(value = "INVOKE", remap = false,
                    target = "me/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder.add (" +
                            "Lme/jellysquid/mods/sodium/client/gui/options/Option;" +
                            ")Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;"),
            allow = 1)
    private static OptionGroup.Builder addOption(OptionGroup.Builder builder, Option<?> candidate) {
        builder.add(candidate);
        builder.add(createRemoveFogButton(vanillaOpts));

        return builder;
    }

    private static OptionImpl<GameOptions, Boolean> createRemoveFogButton(MinecraftOptionsStorage opts) {

        return OptionImpl.createBuilder(boolean.class, opts)
                .setName(new TranslatableText("options.sodium-no-fog.removeFog"))
                .setTooltip(new TranslatableText("options.sodium-no-fog.removeFog.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> {
                    SodiumNoFogClient.enable = value;
                }, options -> {
                    return SodiumNoFogClient.enable;
                })
                .setImpact(OptionImpact.HIGH)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_UPDATE})
                .build();
    }
}
