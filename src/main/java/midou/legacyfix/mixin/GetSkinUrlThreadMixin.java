package midou.legacyfix.mixin;

import midou.legacyfix.utils.ApiServers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.core.util.helper.GetSkinUrlThread;

@Mixin(GetSkinUrlThread.class)
public class GetSkinUrlThreadMixin {

	// Modify the URL used for fetching the UUID
	@ModifyArg(
		method = "getSkinObject",
		at = @At(
			value = "INVOKE",
			target = "Lcom/b100/utils/StringUtils;getWebsiteContentAsString(Ljava/lang/String;)Ljava/lang/String;",
			ordinal = 0
		),
		index = 0,
		remap = false
	)
	private String modifyUUIDUrl(String url) {
		String name = url.substring(url.lastIndexOf("/") + 1); // Extract the player's name from the original URL
		return ApiServers.getAccountURL() + "/users/profiles/minecraft/" + name;
	}

	// Modify the URL used for fetching the skin using the UUID
	@ModifyArg(
		method = "getSkinObject",
		at = @At(
			value = "INVOKE",
			target = "Lcom/b100/utils/StringUtils;getWebsiteContentAsString(Ljava/lang/String;)Ljava/lang/String;",
			ordinal = 1
		),
		index = 0,
		remap = false
	)
	private String modifySkinUrl(String url) {
		String uuid = url.substring(url.lastIndexOf("/") + 1); // Extract the UUID from the original URL
		return ApiServers.getSessionURL() + "/session/minecraft/profile/" + uuid;
	}
}
