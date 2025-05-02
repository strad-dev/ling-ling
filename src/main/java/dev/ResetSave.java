package dev;

import com.mongodb.client.MongoCollection;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class ResetSave {
	public static void resetSave(GenericDiscordEvent e, String idToModerate, String reason) {
		try {
			if(idToModerate.isEmpty()) {
				throw new IllegalArgumentException();
			}
			Long.parseLong(idToModerate);
		} catch(Exception exception) {
			e.reply("You didn't provide an ID!");
			return;
		}

		JSONObject data = DatabaseManager.getDataById("Economy Data", idToModerate);
		if(data != null) {
			if(idToModerate.equals(e.getAuthor().getId())) {
				e.reply("Imagine trying to reset your own save, how dumb are you???");
			} else if(idToModerate.equals("619989388109152256") || idToModerate.equals("488487157372157962")) {
				e.reply("Imagine trying to reset the save of a developer smh");
			} else {
				String name;
				try {
					name = Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).getName();
				} catch(Exception exception) {
					name = "Someone";
				}
				MongoCollection<Document> collection = DatabaseManager.prepareStoreAllEconomyData();
				collection.deleteOne(eq("discordID", data.get("discordID")));
				LogCase.logCase(e, "Save Reset", idToModerate, reason);
				try {
					Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).openPrivateChannel()
							.queue((channel) -> channel.sendMessage("Your save was reset.  Reason: " + reason + "\nContinuation of this action may result in a bot ban.").queue());
				} catch(Exception exception) {
					// nothing here lol
				}
				e.reply(":wastebasket: " + name + "'s save was successfully reset!");
			}
		} else {
			e.reply("This save doesn't even exist, idiot.");
		}
	}
}