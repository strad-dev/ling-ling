package leveling;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

import java.util.Objects;
// BEETHOVEN-ONLY CLASS

public class Rank {
	public static void rank(GenericDiscordEvent e) {
		JSONObject data;
		String[] message = e.getMessage().getContentRaw().split(" ");
		String user;
		if(message.length == 2) {
			user = e.getAuthor().getId();
			data = Leveling.loadData(e);
		} else {
			user = message[2];
			data = DatabaseManager.getDataById("Leveling Data", user);
			if(data == null) {
				e.reply("This save file does not exist!");
				return;
			}
		}
		try {
			user = Objects.requireNonNull(e.getGuild().getMemberById(user)).getEffectiveName();
		} catch(Exception exception) {
			e.reply("The user you wanted to look up does not exist!");
		}
		e.reply("**__" + user + "'s Stats__**\nLevel `" + data.get("level") + "`\n`" + data.get("xp") + "/" + ((long) data.get("level") + 1) * 100 + "` XP");
	}
}