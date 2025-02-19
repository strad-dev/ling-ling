package regular;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.Permission;
import org.bson.Document;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import static com.mongodb.client.model.Filters.eq;

public class LuthierConfig {
	private static boolean isAdmin(GenericDiscordEvent e) {
		return e.getGuild().getMember(e.getAuthor()).getPermissions().contains(Permission.ADMINISTRATOR) || Utils.CheckPermLevel(e.getAuthor().getId()) >= 1;
	}

	public static void luthierConfig(GenericDiscordEvent e, String mainAction, String editAction, String newValue) {
		String id = e.getGuild().getId();
		switch(mainAction) {
			case "setup" -> {
				if(isAdmin(e)) {
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
									.append("chance", Utils.luthierChance(serverMembers))
									.append("hasWord", false)
									.append("word", "blank")
									.append("amount", 0)
									.append("discordID", e.getGuild().getId()));
							e.reply("Successfully set up Luthier for " + e.getGuild().getName() + " in "
									+ e.getChannel().getAsMention() + "\nLuthier Multipliers can be crafted using `!craft`\nIf you have existing multipliers, you can apply them using `!luthier add`");
						} catch(Exception exception2) {
							e.reply("Something went horribly wrong!");
						}
					}
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "stats" -> {
				JSONObject data = DatabaseManager.getDataByGuild(e, "Luthier Data");
				if(data == null) {
					e.reply("Luthier has not been set up!");
					return;
				}
				e.reply("**Luthier for " + e.getGuild().getName() + ":**\n" +
						"Channel: <#" + data.get("channel") + ">\n" +
						"Chance: " + data.get("chance") + "\n" +
						"Multiplier: " + data.get("multiplier") + "\n" +
						"Amount: " + data.get("amount") + "\n");
			}
			case "settings" -> {
				if(editAction.isEmpty()) {
					e.reply("You need to provide an option.");
					return;
				}
				JSONObject data = DatabaseManager.getDataByGuild(e, "Luthier Data");
				if(data == null) {
					e.reply("This server does not have Luthier!");
					return;
				}
				switch(editAction) {
					case "channel" -> {
						if(isAdmin(e)) {
							data.replace("channel", newValue);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
							e.reply("Successfully changed the channel for " + e.getGuild().getName() + " to <#" + newValue + ">");
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "logchannel" -> {
						if(isAdmin(e)) {
							data.replace("logchannel", newValue);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
							e.reply("Successfully changed the logging channel for " + e.getGuild().getName() + " to <#" + newValue + ">");
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "word" -> {
						if(Utils.CheckPermLevel(e.getAuthor().getId()) >= 2) {
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
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "amount" -> {
						if(Utils.CheckPermLevel(e.getAuthor().getId()) >= 2) {
							Long amount = Long.parseLong(newValue);
							data.replace("amount", amount);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
							e.reply("Successfully changed the amount for " + e.getGuild().getName() + " to `" + amount + "`");
						} else {
							Utils.permissionDenied(e);
						}
					}
					default -> e.reply("You can only edit the `channel`, `logchannel`, `multiplier`, and `word`");
				}
			}
			default ->
					e.reply("You need to provide an option.  Valid options: `setup` `stats` `settings` `cheat` `servers` `balance` `contributers` `add` `remove` `forceremove`");
		}
	}
}