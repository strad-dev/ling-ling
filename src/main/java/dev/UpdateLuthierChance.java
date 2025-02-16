package dev;

import com.mongodb.client.MongoCollection;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;
import processes.Utils;

import java.util.ArrayList;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class UpdateLuthierChance {
	public static void updateLuthierChance(GenericDiscordEvent e, boolean isHuman) {
		ArrayList<Document> documents = DatabaseManager.getAllData("Luthier Data");
		MongoCollection<Document> collection = DatabaseManager.prepareStoreAllData("Luthier Data");
		for(Document file : documents) {
			JSONParser parser = new JSONParser();
			JSONObject data;
			try {
				data = (JSONObject) parser.parse(file.toJson());
			} catch(Exception exception) {
				continue;
			}
			if(data.get("discordID").toString().contains("670725611207262219")) {
				continue;
			}
			try {
				data.replace("chance", Utils.luthierChance(Objects.requireNonNull(e.getJDA().getGuildById(data.get("discordID").toString())).getMemberCount()));
			} catch(Exception exception) {
				continue;
			}
			collection.replaceOne(eq("discordID", data.get("discordID")), Document.parse(data.toJSONString()));
		}
		if(isHuman) {
			e.reply("Successfully updated Luthier chances for all servers");
		}
	}
}