package regular;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.bson.Document;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import static com.mongodb.client.model.Filters.eq;

public class LuthierConfig {
	private static boolean isAdmin(GenericDiscordEvent e) {
		return e.getGuild().getMember(e.getAuthor()).getPermissions().contains(Permission.ADMINISTRATOR) || Utils.checkPermLevel(e.getAuthor().getId()) >= 1;
	}

	private static void sendLog(GenericDiscordEvent e, JSONObject data, String action) {
		EmbedBuilder builder = new EmbedBuilder()
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.addField("User: " + e.getAuthor().getName(), "Action: " + action, false)
				.setTitle("__**Luthier Change**__");
		e.getJDA().getGuildById(e.getGuild().getId()).getTextChannelById((String) data.get("logchannel")).sendMessageEmbeds(builder.build()).queue();
		e.getJDA().getGuildById("670725611207262219").getTextChannelById("1341876485782372432").sendMessageEmbeds(builder.build()).queue();
	}

	public static void luthierConfig(GenericDiscordEvent e, String mainAction, String editAction, String newValue) {
		JSONObject data = DatabaseManager.getDataByGuild(e, "Luthier Data");
		if(data == null && !mainAction.equals("setup")) {
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
									.append("logchannel", 0)
									.append("multiplier", 0)
									.append("contributers", "")
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
						if(Utils.checkPermLevel(e.getAuthor().getId()) >= 2) {
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
						if(Utils.checkPermLevel(e.getAuthor().getId()) >= 2) {
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
			case "cheat" -> {
				long time = System.currentTimeMillis();
				if(time < (long) data.get("cheatCD") && Utils.checkPermLevel(e.getAuthor().getId()) < 1) {
					long milliseconds = (long) data.get("cheatCD") - time;
					long hours = milliseconds / 3600000;
					milliseconds -= hours * 3600000;
					long minutes = milliseconds / 60000;
					milliseconds -= minutes * 60000;
					long seconds = milliseconds / 1000;
					milliseconds -= seconds * 1000;
					e.reply("No Cheating!  Yet... wait " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
				} else {
					e.reply("# " + e.getAuthor().getEffectiveName() + " IS A DIRTY CHEATER!!!\nThe Word is: " + data.get("word"));
					if(Utils.checkPermLevel(e.getAuthor().getId()) < 1) {
						data.replace("cheatCD", time + 86340000);
					}
					sendLog(e, data, "CHEATER");
				}
			}
			case "balance", "servers" -> {
				JSONObject userData = DatabaseManager.getEconomyData(e);
				long free = (long) userData.get("luthierBalance");
				long used = 0;
				String temp = (String) userData.get("luthierServers");
				if(free == 0 && temp.isEmpty()) {
					e.reply("You do not have any Luthier multipliers!");
					return;
				}
				String[] servers = temp.split("\n");
				StringBuilder builder = new StringBuilder("Luthiers Avaliable: `").append(free).append("`\n\n").append("Servers Used In:");
				for(String server : servers) {
					String[] serverData = server.split(" ");
					builder.append(e.getJDA().getGuildById(serverData[0]).getName()).append(" `").append(serverData[0]).append("` - `").append(serverData[1]).append("`x\n");
					used += Long.parseLong(serverData[1]);
				}
				builder.append("\nTotal Used: `").append(used).append("`\n");
				e.reply(builder.toString());
			}
			case "contributors" -> {
				String[] contributors = ((String) data.get("contributors")).split(" ");
				StringBuilder builder = new StringBuilder("Contributors to this server's Luthier:");
				long multiplier = 0;
				for(String contributor : contributors) {
					String[] contributorData = contributor.split(" ");
					builder.append(e.getJDA().retrieveUserById(contributorData[0]).complete().getEffectiveName()).append(" `").append(contributorData[0]).append("` - `").append(contributorData[1]).append("`x\n");
					multiplier += Long.parseLong(contributorData[1]);
				}
				builder.append("\nTotal Multiplier: `").append(multiplier).append("`\n");
				e.reply(builder.toString());
			}
			case "add" -> {
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
					e.reply("You cannot add a negative amount!");
				}
				if(amount > balance) {
					e.reply("You cannot apply a higher multiplier than you have.  Your balance: `" + balance + "`x");
				}

				data.replace("multiplier", (long) data.get("multiplier") + amount);
				String serverData = (String) data.get("contributors");
				
			}
			default ->
					e.reply("You need to provide an option.  Valid options: `setup` `stats` `settings` `cheat` `servers` `balance` `contributers` `add` `remove` `forceremove`");
		}
	}
}