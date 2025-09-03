package midou.legacyfix.mixin;

import midou.legacyfix.utils.ApiServers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.core.util.helper.UUIDHelper;

@Mixin(UUIDHelper.class)
public class UUIDParserMixin {
	@Shadow @Mutable
	public static String urlUUID;

	static {
		urlUUID = ApiServers.getAccountURL() + "/users/profiles/minecraft/%s";
	}
}
