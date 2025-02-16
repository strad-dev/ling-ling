package regular;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import org.bson.Document;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Numbers;

import static com.mongodb.client.model.Filters.eq;

public class LuthierConfig {
	public static void luthierConfig(GenericDiscordEvent e, String mainAction, String editAction, String newValue) {
		String id = e.getGuild().getId();
		if(mainAction.isEmpty()) {
			JSONObject data = DatabaseManager.getDataByGuild(e, "Luthier Data");
			if(data == null) {
				e.reply("Luthier has not been set up!");
				return;
			}
			e.reply("**Luthier for " + e.getGuild().getName() + ":**\n" +
					"Channel: <#" + data.get("channel") + ">\n" +
					"Chance: " + data.get("chance") + "\n" +
					"Multiplier: " + data.get("multiplier") + "\n" +
					"Word: " + data.get("word") + "\n" +
					"Amount: " + data.get("amount") + "\n");
			return;
		}
		switch(mainAction) {
			case "setup" -> {
				MongoDatabase database = DatabaseManager.getDatabase();
				MongoCollection<Document> collection = database.getCollection("Luthier Data");
				Document document = collection.find(eq("discordID", id)).first();
				if(document != null) {
					e.reply("This server already has Luthier!");
				} else {
					int serverMembers = e.getGuild().getMemberCount();
					try {
						InsertOneResult result = collection.insertOne(new Document()
								.append("channel", e.getChannel().getId())
								.append("multiplier", 0)
								.append("chance", Numbers.luthierChance(serverMembers))
								.append("hasWord", false)
								.append("word", "blank")
								.append("amount", 0)
								.append("discordID", e.getGuild().getId()));
						e.reply("Successfully set up Luthier for " + e.getGuild().getName() + " in "
								+ e.getChannel().getAsMention() + ".\nLuthier Multipliers can be crafted using `/craft`");
					} catch(Exception exception2) {
						e.reply("Something went horribly wrong!");
					}
				}
			}
			case "edit" -> {
				if(editAction.isEmpty()) {
					e.reply("You need to provide an option, what are you, stupid?");
					return;
				}
				JSONObject data = DatabaseManager.getDataByGuild(e, "Luthier Data");
				if(data == null) {
					e.reply("This server does not have Luthier!");
					return;
				}
				switch(editAction) {
					case "channel" -> {
						data.replace("channel", newValue);
						DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
						e.reply("Successfully changed the channel for " + e.getGuild().getName() + " to <#" + newValue + ">");
					}
					case "word" -> {
						String word = newValue.toLowerCase();
						if(word.equals("blank")) {
							data.replace("hasWord", false);
							data.replace("word", "blank");
						} else {
							data.replace("hasWord", true);
							data.replace("word", word);
						}
						DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
						e.reply("Successfully changed the word for " + e.getGuild().getName() + " to `" + word + "`");
					}
					case "multiplier" -> {
						long newMulti;
						try {
							newMulti = (long) data.get("multiplier") + Long.parseLong(newValue);
						} catch(Exception exception) {
							e.reply("You didn't provide a valid integer.  Dumbass.");
							return;
						}
						data.replace("multiplier", newMulti);
						DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
						e.reply("Successfully changed the multipler for " + e.getGuild().getName() + " by " + newValue + "x.  New multiplier: " + newMulti);
					}
					default -> e.reply("You can only edit the `channel`, `multiplier`, and `word`.");
				}
			}
			default -> e.reply("You can only `setup` and `edit` luthier.");
		}
	}
}