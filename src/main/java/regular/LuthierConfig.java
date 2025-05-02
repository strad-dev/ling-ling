package regular;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;

import static com.mongodb.client.model.Filters.eq;

public class LuthierConfig {
	private static boolean isAdmin(GenericDiscordEvent e) {
		return e.getGuild().getMember(e.getAuthor()).getPermissions().contains(Permission.ADMINISTRATOR) || Utils.checkPermLevel(e.getAuthor().getId()) >= 1;
	}

	private static void sendLog(GenericDiscordEvent e, JSONObject data, String action) {
		EmbedBuilder builder = new EmbedBuilder()
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.addField("User: " + e.getAuthor().getName() + " `" + e.getAuthor().getId() + "`", "Action: " + action, false).setTitle("__**Luthier Log**__")
				.setColor(Color.BLUE);
		e.getJDA().getGuildById("670725611207262219").getTextChannelById("1341876485782372432").sendMessageEmbeds(builder.build()).queue();
		try {
			e.getJDA().getGuildById((String) data.get("discordID")).getTextChannelById((String) data.get("logChannel")).sendMessageEmbeds(builder.build()).queue();
		} catch(Exception exception) {
			// nothing here
		}
	}

	public static void luthierConfig(GenericDiscordEvent e, String mainAction, String editAction, String newValue) {
		JSONObject serverData = DatabaseManager.getDataByGuild(e, "Luthier Data");
		if(serverData == null && !mainAction.equals("setup")) {
			e.reply("This server does not have Luthier!");
			return;
		}
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
									.append("logChannel", e.getChannel().getId())
									.append("multiplier", 0)
									.append("cheatCD", 0)
									.append("contributors", new JSONArray())
									.append("chance", Utils.luthierChance(serverMembers))
									.append("hasWord", false)
									.append("word", "blank").append("amount", 0)
									.append("discordID", e.getGuild().getId()));
							e.reply("Successfully set up Luthier for " + e.getGuild().getName() + " in " + e.getChannel().getAsMention() + "\nLuthier Multipliers can be crafted using `!craft`\nIf you have existing multipliers, you can apply them using `!luthier add`\n\nBy default, unscrambles and logs are both sent into this channel.  If you would like to change them, use `!luthier settings`");
						} catch(Exception exception2) {
							e.reply("Something went horribly wrong!");
						}
					}
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "stats" -> {
				e.reply("**Luthier for " + e.getGuild().getName() + ":**\n" + "Channel: <#" + serverData.get("channel") + ">\n" + "Chance: `" + serverData.get("chance") + "`\n" + "Multiplier: `" + serverData.get("multiplier") + "`\n" + "Amount: `" + serverData.get("amount") + "`\n" + "Active Puzzle? `" + serverData.get("hasWord") + "`");
			}
			case "edit", "settings" -> {
				if(editAction.isEmpty()) {
					e.reply("**Luthier Settings for " + e.getGuild().getName() + ":**\n" + "Channel: <#" + serverData.get("channel") + ">\n" + "Log Channel: <#" + serverData.get("logChannel") + ">\n");
					return;
				}
				switch(editAction) {
					case "channel" -> {
						if(isAdmin(e)) {
							serverData.replace("channel", newValue);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
							e.reply("Successfully changed the channel for " + e.getGuild().getName() + " to <#" + newValue + ">");
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "logchannel" -> {
						if(isAdmin(e)) {
							serverData.replace("logChannel", newValue);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
							e.reply("Successfully changed the logging channel for " + e.getGuild().getName() + " to <#" + newValue + ">");
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "word" -> {
						if(Utils.checkPermLevel(e.getAuthor().getId()) >= 2) {
							String word = newValue.toLowerCase();
							if(word.equals("blank")) {
								serverData.replace("hasWord", false);
								serverData.replace("word", "blank");
							} else {
								serverData.replace("hasWord", true);
								serverData.replace("word", word);
							}
							DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
							e.reply("Successfully changed the word for " + e.getGuild().getName() + " to `" + word + "`");
						} else {
							Utils.permissionDenied(e);
						}
					}
					case "amount" -> {
						if(Utils.checkPermLevel(e.getAuthor().getId()) >= 2) {
							Long amount = Long.parseLong(newValue);
							serverData.replace("amount", amount);
							DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
							e.reply("Successfully changed the amount for " + e.getGuild().getName() + " to `" + amount + "`");
						} else {
							Utils.permissionDenied(e);
						}
					}
					default -> e.reply("You can only edit the `channel`, `logchannel`, `word`, and `amount`");
				}
			}
			case "cheat" -> {
				long time = System.currentTimeMillis();
				if(time < (long) serverData.get("cheatCD") && Utils.checkPermLevel(e.getAuthor().getId()) < 1) {
					long milliseconds = (long) serverData.get("cheatCD") - time;
					long hours = milliseconds / 3600000;
					milliseconds -= hours * 3600000;
					long minutes = milliseconds / 60000;
					milliseconds -= minutes * 60000;
					long seconds = milliseconds / 1000;
					milliseconds -= seconds * 1000;
					e.reply("No Cheating!  Yet... wait " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
				} else if(!(boolean) serverData.get("hasWord")) {
					e.reply("There is not an active puzzle!");
				} else {
					e.reply("# `" + e.getAuthor().getEffectiveName() + "` IS A DIRTY CHEATER!!!\nThe Word is: ||`" + serverData.get("word") + "`||");
					if(Utils.checkPermLevel(e.getAuthor().getId()) < 1) {
						serverData.replace("cheatCD", time + 43140000);
					}
					sendLog(e, serverData, "CHEATER");
				}
			}
			case "balance", "servers" -> {
				JSONObject userData = DatabaseManager.getEconomyData(e);
				long free = (long) userData.get("luthierBalance");
				long used = 0;
				JSONArray servers = (JSONArray) userData.get("luthierServers");
				if(free == 0 && servers.isEmpty()) {
					e.reply("You do not have any Luthier multipliers!");
					return;
				}
				StringBuilder builder = new StringBuilder("Luthiers Avaliable: `").append(free).append("x`\n\n").append("Servers Used In:\n");
				for(Object o : servers) {
					JSONObject server = (JSONObject) o;
					String serverID = (String) server.get("discordID");
					long amount = (long) server.get("amount");
					builder.append(e.getJDA().getGuildById(serverID).getName()).append(" `").append(serverID).append("` - `").append(amount).append("x`\n");
					used += amount;
				}
				builder.append("\nTotal Used: `").append(used).append("x`\n");
				e.reply(builder.toString());
			}
			case "contributors" -> {
				JSONArray contributors = (JSONArray) serverData.get("contributors");
				if(contributors.isEmpty()) {
					e.reply("There are no contributors to this server's Luthier!");
					return;
				}
				StringBuilder builder = new StringBuilder("Contributors to this server's Luthier:\n");
				long multiplier = 0;
				for(Object o : contributors) {
					JSONObject contributor = (JSONObject) o;
					String user = (String) contributor.get("discordID");
					long contribution = (long) contributor.get("amount");
					multiplier += contribution;
					builder.append(e.getJDA().retrieveUserById(user).complete().getEffectiveName()).append(" `").append(user).append("` - `").append(contribution).append("x`\n");
				}
				builder.append("\nTotal Multiplier: `").append(multiplier).append("x`\n");
				e.reply(builder.toString());
			}
			case "add" -> {
				JSONObject userData = DatabaseManager.getEconomyData(e);
				long balance = (long) userData.get("luthierBalance");
				long amount;
				try {
					amount = Long.parseLong(editAction);
				} catch(Exception exception) {
					try {
						amount = Long.parseLong(newValue);
					} catch(Exception exception2) {
						e.reply("Invalid or no amount provided!");
						return;
					}
				}

				if(amount < 0) {
					e.reply("You cannot add a negative amount!");
				}
				if(amount > balance) {
					amount = balance;
				}

				serverData.replace("multiplier", (long) serverData.get("multiplier") + amount);
				JSONArray contributors = (JSONArray) serverData.get("contributors");
				JSONArray userServers = (JSONArray) userData.get("luthierServers");

				boolean found = false;
				for(Object o : contributors) {
					JSONObject contributor = (JSONObject) o;
					if(contributor.get("discordID").equals(e.getAuthor().getId())) {
						contributor.replace("amount", (long) contributor.get("amount") + amount);
						found = true;
						break;
					}
				}
				if(!found) {
					JSONObject contributor = new JSONObject();
					contributor.put("discordID", e.getAuthor().getId());
					contributor.put("amount", amount);
					contributors.add(contributor);
				}

				boolean serverFound = false;
				for(Object s : userServers) {
					JSONObject server = (JSONObject) s;
					if(server.get("discordID").equals(e.getGuild().getId())) {
						server.replace("amount", (long) server.get("amount") + amount);
						serverFound = true;
						break;
					}
				}
				if(!serverFound) {
					JSONObject server = new JSONObject();
					server.put("discordID", e.getGuild().getId());
					server.put("amount", amount);
					userServers.add(server);
				}
				userData.replace("luthierBalance", (long) userData.get("luthierBalance") - amount);
				DatabaseManager.saveDataByUser(e, "Economy Data", userData);
				serverData.put("contributors", contributors);
				DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
				e.reply("Added `" + amount + "x` multiplier to " + e.getGuild().getName());
				sendLog(e, serverData, "**Add Luthier**\nGuild: " + e.getGuild().getName() + " `" + e.getGuild().getId() + "`\nAmount: `" + amount + "x`");
			}
			case "remove" -> {
				JSONObject userData = DatabaseManager.getEconomyData(e);
				long balance = (long) userData.get("luthierBalance");
				long amount;
				try {
					amount = Long.parseLong(editAction);
				} catch(Exception exception) {
					e.reply("Invalid or no amount provided!");
					return;
				}
				if(amount < 0) {
					e.reply("You cannot remove a negative amount!");
					return;
				}

				serverData.replace("multiplier", (long) serverData.get("multiplier") - amount);
				JSONArray contributors = (JSONArray) serverData.get("contributors");
				JSONArray userServers = (JSONArray) userData.get("luthierServers");

				boolean found = false;
				for(Object o : contributors) {
					JSONObject contributor = (JSONObject) o;
					if(contributor.get("discordID").equals(e.getAuthor().getId())) {
						long currentAmount = (long) contributor.get("amount");
						if(currentAmount < amount) {
							e.reply("You cannot remove more than you contributed!");
							return;
						}
						contributor.replace("amount", currentAmount - amount);
						if((long) contributor.get("amount") == 0) {
							contributors.remove(contributor);
						}
						found = true;
						break;
					}
				}
				if(!found) {
					e.reply("You haven't contributed any multipliers to this server!");
					return;
				}
				for(Object s : userServers) {
					JSONObject server = (JSONObject) s;
					if(server.get("discordID").equals(e.getGuild().getId())) {
						long currentAmount = (long) server.get("amount");
						if(currentAmount <= amount) {
							userServers.remove(server);
						} else {
							server.replace("amount", currentAmount - amount);
						}
						break;
					}
				}
				userData.replace("luthierBalance", balance + amount);
				DatabaseManager.saveDataByUser(e, "Economy Data", userData);
				serverData.put("contributors", contributors);
				DatabaseManager.saveDataByGuild(e, "Luthier Data", serverData);
				e.reply("Removed `" + amount + "x` multiplier from " + e.getGuild().getName());
				sendLog(e, serverData, "**Remove Luthier**\nGuild: " + e.getGuild().getName() + " `" + e.getGuild().getId() + "`\nAmount: `" + amount + "x`");
			}
			case "forceremove" -> {
				MongoDatabase database = DatabaseManager.getDatabase();
				MongoCollection<Document> collection = database.getCollection("Luthier Data");
				long totalRemoved = 0;
				JSONObject userData = DatabaseManager.getEconomyData(e);
				long balance = userData != null ? (long) userData.get("luthierBalance") : 0;
				JSONArray servers = (JSONArray) userData.get("luthierServers");

				for(Object s : servers) {
					JSONObject server = (JSONObject) s;
					String serverID = (String) server.get("discordID");
					Document document = collection.find(eq("discordID", serverID)).first();

					JSONObject guildData = DatabaseManager.getDataById("Luthier Data", serverID);
					JSONArray contributors = (JSONArray) guildData.get("contributors");

					for(Object o : contributors) {
						JSONObject contributor = (JSONObject) o;
						if(contributor.get("discordID").equals(e.getAuthor().getId())) {
							long amount = (long) contributor.get("amount");
							guildData.replace("multiplier", (long) guildData.get("multiplier") - amount);
							contributors.remove(contributor);
							totalRemoved += amount;
							sendLog(e, guildData, "**Force Remove Luthier**\nGuild: " + e.getJDA().getGuildById((String) guildData.get("discordID")).getName() + " `" + guildData.get("discordID") + "`\nAmount: `" + amount + "x`");
							break;
						}
					}

					guildData.put("contributors", contributors);
					collection.replaceOne(eq("discordID", serverID), Document.parse(guildData.toJSONString()));
				}

				userData.replace("luthierBalance", balance + totalRemoved);
				userData.replace("luthierServers", new JSONArray());
				DatabaseManager.saveDataByUser(e, "Economy Data", userData);
				e.reply("Removed a total of `" + totalRemoved + "x` multipliers from all servers");
			}
			default ->
					e.reply("You need to provide a valid option.  Valid options: `setup` `stats` `settings` `cheat` `servers` `balance` `contributors` `add` `remove` `forceremove`");
		}
	}
}