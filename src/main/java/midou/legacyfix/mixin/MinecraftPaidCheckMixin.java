package midou.legacyfix.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import midou.legacyfix.utils.ApiServers;


@Mixin(Minecraft.class)
public class MinecraftPaidCheckMixin {

	@ModifyConstant(
		method = "lambda$startCheckPaidThread$2", // What the fuck ? Why ?
		constant = @Constant(stringValue = "http://session.minecraft.net/game/joinserver.jsp?user="),
		remap = false
	)
	private String replaceSessionUrl(String original) {
		return ApiServers.getSessionURL() + "/game/joinserver.jsp?user=";
	}
}
