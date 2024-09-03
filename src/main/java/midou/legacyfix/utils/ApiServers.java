package midou.legacyfix.utils;

public class ApiServers {
	public static String getAuthURL() {
		return getPropertyWithFallback("minecraft.api.auth.host", "https://authserver.mojang.com");
	}

	public static String getAccountURL() {
		return getPropertyWithFallback("minecraft.api.account.host", "https://api.mojang.com");
	}

	public static String getSessionURL() {
		return getPropertyWithFallback("minecraft.api.session.host", "https://sessionserver.mojang.com");
	}

	public static String getServicesURL() {
		return getPropertyWithFallback("minecraft.api.services.host", "https://api.minecraftservices.com");
	}

	private static String getPropertyWithFallback(String key, String fallback) {
		String value = System.getProperty(key);
		return (value != null) ? value : fallback;
	}
}

