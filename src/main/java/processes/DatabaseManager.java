package processes;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import economy.MarketComparator;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("DuplicatedCode")
public class DatabaseManager {

	private static MongoDatabase database;
	//private static MongoDatabase databasePunishments;
	@SuppressWarnings("FieldCanBeLocal")
	private static MongoClient mongoClient;

	public static MongoDatabase getDatabase() {
		return database;
	}

	public static ArrayList<Document> getAllEconomyData() {
		MongoCollection<Document> collection = database.getCollection("Economy Data");
		ArrayList<Document> results = new ArrayList<>();
		FindIterable<Document> iterable = collection.find();
		iterable.into(results);
		return results;
	}

	public static ArrayList<Document> getAllData(String name) {
		MongoCollection<Document> collection = database.getCollection(name);
		ArrayList<Document> results = new ArrayList<>();
		FindIterable<Document> iterable = collection.find();
		iterable.into(results);
		return results;
	}

	public static MongoCollection<Document> prepareStoreAllEconomyData() {
		return database.getCollection("Economy Data");
	}

	public static MongoCollection<Document> prepareStoreAllData(String name) {
		return database.getCollection(name);
	}

	public static JSONObject getEconomyData(GenericDiscordEvent e) {
		MongoCollection<Document> collection = database.getCollection("Economy Data");
		Document document = collection.find(eq("discordID", e.getAuthor().getId())).first();
		if(document == null) {
			e.reply("You do not have a save!  Run `/start` to get one!");
			return null;
		} else {
			try {
				JSONParser parser = new JSONParser();
				return (JSONObject) parser.parse(document.toJson());
			} catch(Exception exception) {
				return null;
			}
		}
	}

	public static ArrayList<String> getItemData(String item) {
		MongoCollection<Document> collection = database.getCollection("Market");
		Document document = collection.find(eq("item", item)).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			ArrayList<String> temp = new ArrayList<>(Arrays.asList(((String) ((JSONObject) parser.parse(document.toJson())).get("data")).split("\n")));
			if(temp.isEmpty()) {
				return null;
			}
			temp.sort(new MarketComparator());
			return temp;
		} catch(Exception exception) {
			return null;
		}
	}

	public static JSONObject getDataByUser(GenericDiscordEvent e, String collection1) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		Document document = collection.find(eq("discordID", e.getAuthor().getId())).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			return (JSONObject) parser.parse(document.toJson());
		} catch(Exception exception) {
			return null;
		}
	}

	public static JSONObject getDataForUser(String collection1, String target) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		Document document = collection.find(eq("discordID", target)).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			return (JSONObject) parser.parse(document.toJson());
		} catch(Exception exception) {
			return null;
		}
	}

	public static JSONObject getDataByGuild(GenericDiscordEvent e, String collection1) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		Document document = collection.find(eq("discordID", e.getGuild().getId())).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			return (JSONObject) parser.parse(document.toJson());
		} catch(Exception exception) {
			return null;
		}
	}

	public static JSONObject getMiscData() {
		MongoCollection<Document> collection = database.getCollection("Misc Files");
		Document document = collection.find(eq("discordID", -1)).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			return (JSONObject) parser.parse(document.toJson());
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static JSONObject getData(String collectionName, String field, Object item) {
		MongoCollection<Document> collection = database.getCollection(collectionName);
		Document document = collection.find(eq(field, item)).first();
		try {
			JSONParser parser = new JSONParser();
			assert document != null;
			return (JSONObject) parser.parse(document.toJson());
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static void saveDataByUser(GenericDiscordEvent e, String collection1, JSONObject newData) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		collection.replaceOne(eq("discordID", e.getAuthor().getId()), Document.parse(newData.toJSONString()));
	}

	public static void saveDataForUser(String collection1, String target, JSONObject newData) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		collection.replaceOne(eq("discordID", target), Document.parse(newData.toJSONString()));
	}

	public static void saveDataByGuild(GenericDiscordEvent e, String collection1, JSONObject newData) {
		MongoCollection<Document> collection = database.getCollection(collection1);
		collection.replaceOne(eq("discordID", e.getGuild().getId()), Document.parse(newData.toJSONString()));
	}

	public static void saveMiscData(JSONObject newData) {
		MongoCollection<Document> collection = database.getCollection("Misc Files");
		collection.replaceOne(eq("discordID", -1), Document.parse(newData.toJSONString()));
	}

	public static void saveMarketData(String item, ArrayList<String> newData) {
		MongoCollection<Document> collection = database.getCollection("Market");
		JSONObject newMarketData;
		try {
			newMarketData = getData("Market", "item", item);
		} catch(Exception exception) {
			exception.printStackTrace();
			return;
		}
		StringBuilder result = new StringBuilder();
		for(String string : newData) {
			if(!string.isEmpty()) {
				result.append(string).append("\n");
			}
		}
		if(!result.isEmpty()) {
			result.deleteCharAt(result.length() - 1);
		}
		assert newMarketData != null;
		newMarketData.replace("data", result.toString());
		collection.replaceOne(eq("item", item), Document.parse(newMarketData.toJSONString()));
	}

	/*public static MongoCollection<Document> getPunishmentData(GenericDiscordEvent e, String target) {
		return databasePunishments.getCollection(target);
	}*/

	public static void connectToDatabase(boolean beta, String connectionString) {
		// Create a new client and connect to the server
		try {
			ServerApi serverApi = ServerApi.builder()
					.version(ServerApiVersion.V1)
					.build();
			MongoClientSettings settings = MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString(connectionString))
					/*.applyToConnectionPoolSettings(builder ->
							builder.maxConnectionIdleTime(1, TimeUnit.HOURS)
									.maxConnectionLifeTime(1, TimeUnit.HOURS)
									.build())*/
					.serverApi(serverApi)
					.build();

			mongoClient = MongoClients.create(settings);

			// Send a ping to confirm a successful connection
			if(beta) {
				database = mongoClient.getDatabase("Ling_Ling_Beta");
			} else {
				database = mongoClient.getDatabase("Ling_Ling");
			}
			database.runCommand(new Document("ping", 1));
			System.out.println("Connected to main database.");

			/*databasePunishments = mongoClient.getDatabase("Punishment_Logs");
			databasePunishments.runCommand(new Document("ping", 1));
			System.out.println("Connected to punisment database.");*/
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failure connecting to database!  Maybe the IP isn't whitelisted?");
			System.exit(-1);
		}
	}
}