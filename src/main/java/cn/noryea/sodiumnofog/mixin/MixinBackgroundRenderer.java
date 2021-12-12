package cn.noryea.sodiumnofog.mixin;

import cn.noryea.sodiumnofog.SodiumNoFogClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinBackgroundRenderer {

	@Inject(method = "applyFog", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.setShaderFogEnd(F)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void removeFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo cbi, CameraSubmersionType cameraSubmersionType, Entity entity) {
		if (SodiumNoFogClient.enable) {
			if ((fogType == BackgroundRenderer.FogType.FOG_TERRAIN) && (cameraSubmersionType == CameraSubmersionType.NONE) && !(entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS))) {
				RenderSystem.setShaderFogStart(10000000.0F);
				RenderSystem.setShaderFogEnd(12873326.0F);
			}
		}
	}
}
