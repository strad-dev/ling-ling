package leveling;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.entities.Message;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;

import java.util.*;
// BEETHOVEN-ONLY CLASS

public class Leveling {
	public static JSONObject loadData(GenericDiscordEvent e) {
		JSONParser parser = new JSONParser();
		JSONObject data = DatabaseManager.getDataByUser(e, "Leveling Data");
		if(data == null) {
			MongoCollection<Document> collection = DatabaseManager.prepareStoreAllData("Leveling Data");
			InsertOneResult result = collection.insertOne(new Document()
					.append("level", 0L)
					.append("xp", 0L)
					.append("time", 0L)
					.append("messages", 0L)
					.append("discordID", e.getAuthor().getId())
					.append("discordName", e.getAuthor().getEffectiveName()));
			return loadData(e);
		}

		try {
			if(data.get("discordName").toString().isEmpty() || !data.get("discordName").toString().equals(e.getAuthor().getEffectiveName())) {
				data.replace("discordName", e.getAuthor().getEffectiveName());
			}
		} catch(Exception exception) {
			data.replace("discordName", e.getAuthor().getEffectiveName());
		}
		return data;
	}

	public static void saveData(GenericDiscordEvent e, JSONObject data) {
		DatabaseManager.saveDataByUser(e, "Leveling Data", data);
	}

	public static boolean doesMessageCount(GenericDiscordEvent e) {
		String message = e.getMessage().getContentRaw();

		if(message.length() < 15) {
			return false;
		} else if(message.split(" ").length < 3) {
			return false;
		}

		List<Message> list = e.getChannel().getHistory().retrievePast(100).complete();
		try {
			if(list.get(1).getAuthor().equals(e.getAuthor()) && !list.get(2).getAuthor().equals(e.getAuthor())) {
				if(message.length() < 25) {
					return false;
				} else {
					return message.split(" ").length >= 5;
				}
			} else if(list.get(1).getAuthor().equals(e.getAuthor()) && list.get(2).getAuthor().equals(e.getAuthor())) {
				return false;
			}
			for(int i = 1; i < 100; i++) {
				String text = list.get(i).getContentRaw();
				if(text.length() >= 15 && text.split(" ").length >= 3) {
					if(text.contains(message) || message.contains(text)) {
						return false;
					}
				}
				if(text.equals(message)) {
					return false;
				}
			}
		} catch(Exception exception) {
			return false;
		}

		Map<String, Integer> map = new HashMap<>();
		String[] messageArray = removePunctuation(message).toLowerCase().split(" ");
		for(String s : messageArray) {
			if(map.containsKey(s)) {
				map.replace(s, map.get(s) + 1);
			} else {
				map.put(s, 1);
			}
		}
		int benchmark = (int) Math.ceil((double) messageArray.length / 10);
		for(String s : map.keySet()) {
			if(map.get(s) > benchmark) {
				return false;
			}
		}

		Random random = new Random();
		if(e.getAuthor().getId().equals("799074539157979136") && !random.nextBoolean()) {
			return false;
		}
		if(Objects.requireNonNull(e.getGuild().getMember(e.getAuthor())).getRoles().contains(e.getGuild().getRoleById("991908732692865114")) && !e.getAuthor().getId().equals("619989388109152256")) {
			return random.nextBoolean();
		}
		return true;
	}

	public static String removePunctuation(String s) {
		return s.replaceAll("\\p{Punct}", "");
	}

	public static void leveling(GenericDiscordEvent e, double multiplier) {
		JSONObject data = loadData(e);
		long level = (long) data.get("level");
		long xp = (long) data.get("xp");
		long time = (long) data.get("time");
		if(doesMessageCount(e)) {
			try {
				data.replace("messages", (long) data.get("messages") + 1);
			} catch(Exception exception) {
				data.put("messages", 1L);
			}
		}
		JSONObject miscData = DatabaseManager.getMiscData();
		assert miscData != null;
		long minXp = (long) miscData.get("levelMin");
		long maxXp = (long) miscData.get("levelMax");
		long cooldown = (long) miscData.get("levelCooldown");

		Random random = new Random();
		if(System.currentTimeMillis() > time) {
			xp = (long) (xp + (random.nextInt((int) (maxXp - minXp + 1)) + minXp) * multiplier);
			data.replace("time", System.currentTimeMillis() + 30 * 1000L);
			boolean levelUp = false;
			while(xp > (level + 1) * 100 && level < 100) {
				levelUp = true;
				level++;
				xp -= level * 100;
				if(level == 100) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("734697410273607751"))).queue();
				} else if(level == 80) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("845121274958184499"))).queue();
				} else if(level == 60) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("845121187741958166"))).queue();
				} else if(level == 40) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("734697411074719765"))).queue();
				} else if(level == 20) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("734697411783688245"))).queue();
				} else if(level == 10) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("734697412865818645"))).queue();
				} else if(level == 5) {
					e.getGuild().addRoleToMember(Objects.requireNonNull(e.getAuthor()), Objects.requireNonNull(e.getGuild().getRoleById("734697413901680691"))).queue();
				}
			}
			if(levelUp && level < 100) {
				e.reply("Congratulations <@" + e.getAuthor().getId() + ">!  You advanced to Level " + level + "!");
				data.replace("level", level);
			} else if(levelUp && level == 100) {
				e.reply("Congratulations <@" + e.getAuthor().getId() + ">!  You advanced to **Level " + level + "!  MAX LEVEL**");
				data.replace("level", level);
			}
			data.replace("xp", xp);
		}
		saveData(e, data);
	}
}