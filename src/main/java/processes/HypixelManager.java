package processes;

import net.hypixel.api.apache.ApacheHttpClient;
import net.hypixel.api.http.HypixelHttpClient;
import org.json.simple.JSONObject;
import org.shanerx.mojang.Mojang;

import java.util.UUID;

public class HypixelManager {
	private static HypixelHttpClient client;
	private static Mojang mojang;

	public static void connectToHypixel(boolean beta, String hypixelKey) {
		JSONObject data = DatabaseManager.getMiscData();
		try {
			assert data != null;
			client = new ApacheHttpClient(UUID.fromString(hypixelKey));
		} catch(Exception exception) {
			System.out.println("Unable to connect to Hypixel API.");
			exception.printStackTrace();
			System.exit(-1);
		}

		mojang = new Mojang().connect();
	}

	public static HypixelHttpClient getClient() {
		return client;
	}

	public static Mojang getMojang() {
		return mojang;
	}
}