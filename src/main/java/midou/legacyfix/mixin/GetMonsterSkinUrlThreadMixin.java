package midou.legacyfix.mixin;

import com.b100.utils.StringUtils;
import midou.legacyfix.utils.ApiServers;
import net.minecraft.core.util.helper.GetMonsterSkinUrlThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GetMonsterSkinUrlThread.class)
public class GetMonsterSkinUrlThreadMixin {

	@Redirect(
		method = "getSkinObject",
		at = @At(
			value = "INVOKE",
			target = "Lcom/b100/utils/StringUtils;getWebsiteContentAsString(Ljava/lang/String;)Ljava/lang/String;"
		),
		remap = false
	)
		private String redirectSkinRequest (String url){
			if (url.contains("https://sessionserver.mojang.com/session/minecraft/profile/")) {
				String uuid = url.substring(url.lastIndexOf("/") + 1);
				return StringUtils.getWebsiteContentAsString(ApiServers.getSessionURL() + "/session/minecraft/profile/" + uuid);
			}
			if (url.contains("https://api.mojang.com/users/profiles/minecraft/")) {
				String name = url.substring(url.lastIndexOf("/") + 1);
				return StringUtils.getWebsiteContentAsString(ApiServers.getAccountURL() + "/users/profiles/minecraft/" + name);
			}
			return StringUtils.getWebsiteContentAsString(url);
		}

	}
