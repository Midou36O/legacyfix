package midou.legacyfix;

import net.fabricmc.api.ModInitializer;
import midou.legacyfix.utils.ApiServers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class LegacyFix implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = "legacyfix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Minecraft API URLs:");
		LOGGER.info("Auth URL: " + ApiServers.getAuthURL());
		LOGGER.info("Account URL: " + ApiServers.getAccountURL());
		LOGGER.info("Session URL: " + ApiServers.getSessionURL());
		LOGGER.info("Services URL: " + ApiServers.getServicesURL());

	}

	@Override
	public void beforeGameStart() {
		// Custom logic before game starts
	}

	@Override
	public void afterGameStart() {
		// Custom logic after game starts
	}

	@Override
	public void onRecipesReady() {
		// Custom logic when recipes are ready
	}

	@Override
	public void initNamespaces() {
		// Custom logic for initializing namespaces
	}
}
