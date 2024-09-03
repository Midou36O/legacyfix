package midou.legacyfix.mixin;

import net.minecraft.server.net.thread.ThreadLoginVerifier;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import midou.legacyfix.utils.ApiServers;

@Mixin(ThreadLoginVerifier.class)
public class ThreadLoginVerifierMixin {

	@Inject(method = "run", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void onRun(CallbackInfo ci) {
		ThreadLoginVerifierAccessor accessor = (ThreadLoginVerifierAccessor) this;

		try {
			Packet1Login loginPacket = accessor.getLoginPacket();
			NetLoginHandler loginHandler = accessor.getLoginHandler();
			String serverId = NetLoginHandler.getServerId(loginHandler);
			String username = URLEncoder.encode(loginPacket.username, "UTF-8");

			// Modern API URL
			String urlStr = ApiServers.getSessionURL() + "/session/minecraft/hasJoined?username=" + username + "&serverId=" + URLEncoder.encode(serverId, "UTF-8");
			if (hasJoined(username, serverId, null)) {
				NetLoginHandler.setLoginPacket(loginHandler, loginPacket);
			} else {
				loginHandler.kickUser("Failed to verify username!");
			}

			ci.cancel(); // Prevent the original method from executing further

		} catch (Exception e) {
			e.printStackTrace();
			accessor.getLoginHandler().kickUser("Failed to verify username! [internal error " + e + "]");
			ci.cancel();
		}
	}

	// Static method to perform the URL check
	private static boolean hasJoined(String username, String serverId, String ip) throws IOException {
		HttpURLConnection connection;

		URL url = new URL(ApiServers.getSessionURL() + "/session/minecraft/hasJoined?username=" + username + "&serverId=" + serverId + (ip != null ? "&ip=" + ip : ""));
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(false);

		connection.connect();
		int responseCode = connection.getResponseCode();
		connection.disconnect();

		return responseCode == HttpURLConnection.HTTP_OK;
	}
}
