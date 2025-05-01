package dev;

import com.mongodb.client.MongoCollection;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class Unban {
	public static void unban(GenericDiscordEvent e, String idToModerate, String reason, Boolean reset) {
		try {
			if(idToModerate.isEmpty()) {
				throw new IllegalArgumentException();
			}
			Long.parseLong(idToModerate);
		} catch(Exception exception) {
			e.reply("You didn't provide an ID!");
			return;
		}

		if(reset == null) {
			e.reply("You have to tell me whether you want to reset the save or not, dumb.");
			return;
		}

		JSONObject data = DatabaseManager.getDataById("Economy Data", idToModerate);
		if(data == null) {
			e.reply("File doesn't exist!  Looks like they never used the bot to begin with...");
			return;
		}
		if((Boolean) data.get("banned")) {
			String user;
			try {
				user = Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).getName();
			} catch(Exception exception) {
				user = "Someone";
			}
			if(reset) {
				MongoCollection<Document> collection = DatabaseManager.prepareStoreAllEconomyData();
				collection.deleteOne(eq("discordID", data.get("discordID")));
				reason += "\nSave Reset: Yes";
			} else {
				data.replace("banned", false);
				DatabaseManager.saveDataById("Economy Data", idToModerate, data);
				reason += "\nSave Reset: No";
			}
			LogCase.logCase(e, "Unban", idToModerate, reason);
			try {
				String finalReason = reason;
				Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).openPrivateChannel()
						.queue((channel) -> channel.sendMessage("You have been unbanned.  Reason: " + finalReason).queue());
			} catch(Exception exception) {
				// nothing here lol
			}
			e.reply(":white_check_mark: " + user + " was successfully unbanned!");
		} else {
			e.reply("That user isn't even banned, stupid.");
		}
	}
}
