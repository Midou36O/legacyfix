package midou.legacyfix.mixin;

import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.net.packet.Packet2Handshake;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.core.util.helper.RSA;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import midou.legacyfix.utils.ApiServers;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {

	@Inject(method = "handleHandshake", at = @At(value = "INVOKE", target = "Ljava/net/URL;<init>(Ljava/lang/String;)V"), cancellable = true, remap = false)
	private void onHandleHandshake(Packet2Handshake packet2handshake, CallbackInfo ci) {
		NetClientHandlerAccessor accessor = (NetClientHandlerAccessor) this;

		if (accessor.getMinecraft() == null || accessor.getMinecraft().session == null) {
			throw new IllegalStateException("Minecraft or session is not set");
		}

		if (!packet2handshake.username.equals("-")) {
			try {
				String user = accessor.getMinecraft().session.username;
				String sessionId = accessor.getMinecraft().session.sessionId;
				String serverId = packet2handshake.username;

				if (user == null || sessionId == null || serverId == null) {
					accessor.getNetworkManager().networkShutdown("disconnect.loginFailedInfo", new Object[]{"Missing parameters"});
					throw new IllegalArgumentException("Missing parameters");
				}

				// Extract the accessToken from sessionId
				String accessToken = sessionId.split(":")[1];

				// Fetch the UUID using the configurable account URL
				String uuid = getUuid(user);
				if (uuid == null) {
					accessor.getNetworkManager().networkShutdown("disconnect.loginFailedInfo", new Object[]{"Couldn't find UUID of " + user});
					ci.cancel();
					return;
				}

				// Prepare the authentication request using the modern session URL
				URL authUrl = new URL(ApiServers.getSessionURL() + "/session/minecraft/join");
				HttpURLConnection connection = (HttpURLConnection) authUrl.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");

				String payload = "{"
					+ "\"accessToken\": \"" + accessToken + "\","
					+ "\"selectedProfile\": \"" + uuid + "\","
					+ "\"serverId\": \"" + serverId + "\""
					+ "}";
				try (OutputStream os = connection.getOutputStream()) {
					os.write(payload.getBytes(StandardCharsets.UTF_8));
				}

				// Handle the response
				int responseCode = connection.getResponseCode();
				BufferedReader bufferedReader;
				if (responseCode == 204) {  // 204 No Content indicates success
					bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					bufferedReader.close();
					RSA.RSAKeyChain = RSA.generateKeyPair();
					accessor.getNetworkManager().addToSendQueue(new Packet1Login(accessor.getMinecraft().session.username, 29184, RSA.getPublicKey(RSA.RSAKeyChain.getPublic())));
				} else {
					bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
					String errorResponse = bufferedReader.readLine();
					bufferedReader.close();
					accessor.getNetworkManager().networkShutdown("disconnect.loginFailedInfo", new Object[]{errorResponse});
				}

				connection.disconnect();
				ci.cancel(); // Prevent the original method from executing further
			} catch (Exception e) {
				e.printStackTrace();
				accessor.getNetworkManager().networkShutdown("disconnect.genericReason", new Object[]{"Internal client error: " + e.toString()});
				ci.cancel();
			}
		}
	}

	// Method to fetch UUID using the configurable account URL
	private String getUuid(String username) throws IOException {
		String accountUrl = ApiServers.getAccountURL(); // Use the configurable URL
		try (InputStream in = new URL(accountUrl + "/users/profiles/minecraft/" + username).openStream()) {
			JsonObject json = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
			return json.get("id").getAsString();
		} catch (Exception e) {
			// Log or handle exceptions as needed
			return null;
		}
	}
}
