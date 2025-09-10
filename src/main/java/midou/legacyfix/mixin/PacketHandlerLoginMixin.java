package midou.legacyfix.mixin;

import midou.legacyfix.LegacyFix;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import net.minecraft.core.net.packet.PacketLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import midou.legacyfix.utils.ApiServers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;

@Mixin(PacketHandlerLogin.class)
public class PacketHandlerLoginMixin {
	@Inject(method = "handleLogin", at = @At("HEAD"), cancellable = true, remap = false)
	private void onHandleLogin(PacketLogin loginPacket, CallbackInfo ci) {
		PacketHandlerLogin self = (PacketHandlerLogin) (Object) this;
		MinecraftServer server = ((PacketHandlerLoginAccessor) self).getMcServer();
		if (!server.onlineMode) {
			return; // We don't need to modify anything in this case.
		}

		CompletableFuture<Boolean> verifiedFuture = new CompletableFuture<>(); // Async hell incoming

		new Thread(() -> {
			try {
				String serverId = PacketHandlerLogin.getServerId(self);
				String username = URLEncoder.encode(loginPacket.username, "UTF-8");
				boolean verified = hasJoined(username, serverId, null);
				if (verified) { // IP is always null, no idea what's the real purpose of this.
					            // But since we are just going from legacy to modern, and
								// it's optional, it's fiiiiiiine.
					PacketHandlerLogin.setLoginPacket(self, loginPacket);
				} else {
					self.kickUser("Failed to verify Username!");
				}
				verifiedFuture.complete(verified);
			} catch (Exception e) {
				e.printStackTrace();
				LegacyFix.LOGGER.error(e.getMessage());
				self.kickUser("Failed to verify Username! [ Internal error: " + e.getMessage() + "]");
				verifiedFuture.complete(false);
			}
		}).start();

		// Block until we successfully login.
		try {
			verifiedFuture.get();
		} catch (Exception e) {
			LegacyFix.LOGGER.error("Login interrupted! Details : {}", String.valueOf(e));
			self.kickUser("Failed to verify Username! [ Internal error: " + e.getMessage() + "]");
		}
		ci.cancel(); // Cancel the original function.
	}
	@Unique
	private static boolean hasJoined(String username, String serverId, String ip) throws IOException {
		String urlStr = ApiServers.getSessionURL()
						+ "/session/minecraft/hasJoined?username=" + username
						+ "&serverId=" + serverId
						+ (ip != null ? "&ip=" + ip : ""); // Just in case I ever find out what to do with it.
		LegacyFix.LOGGER.info("The url is: {}", urlStr);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		connection.connect();
		int responseCode = connection.getResponseCode();
		connection.disconnect();
		LegacyFix.LOGGER.info("The response code is: {}", responseCode);
		return responseCode == HttpURLConnection.HTTP_OK; // equals 200 (OK)
	}

}
