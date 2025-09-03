package midou.legacyfix.mixin;

import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionString;
import midou.legacyfix.utils.ApiServers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		GameSettings self = (GameSettings) (Object) this;

		// replace the default uuidServer OptionString with your URL
		self.uuidServer = new OptionString(self, "uuidServer", ApiServers.getAccountURL() + "/users/profiles/minecraft/%s");
	}
}
