package midou.legacyfix.mixin;

import com.b100.utils.StringUtils;
import midou.legacyfix.utils.ApiServers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.core.util.helper.GetSkinUrlThread;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GetSkinUrlThread.class)
public class GetSkinUrlThreadMixin {

	@Redirect(
		method = "getSkinObject",
		at = @At(
			value = "INVOKE",
			target = "Lcom/b100/utils/StringUtils;getWebsiteContentAsString(Ljava/lang/String;)Ljava/lang/String;"
		),
		remap = false
	)
	private String redirectSkinRequest(String url) {
		if (url.contains("https://sessionserver.mojang.com/session/minecraft/profile/")) {
			// Redirect skin/session lookups
			String uuid = url.substring(url.lastIndexOf("/") + 1);
			return StringUtils.getWebsiteContentAsString(ApiServers.getSessionURL() + "/session/minecraft/profile/" + uuid);
		}

		// Fallback: keep untouched if it's some other URL
		return url;
	}

}
