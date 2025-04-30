package leveling;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;

import java.util.Objects;
// BEETHOVEN-ONLY CLASS
public class CheckMessages {
	public static void checkMessages(GenericDiscordEvent e) {
		JSONObject data;
		String[] message = e.getMessage().getContentRaw().split(" ");
		String user;
		if(message.length == 2) {
			user = e.getAuthor().getId();
			data = Leveling.loadData(e);
		} else {
			user = message[2];
			JSONParser parser = new JSONParser();
				data = DatabaseManager.getDataById("Leveling Data", user);
			if(data == null) {
				e.reply("This save file does not exist!");
				return;
			}
		}
		e.reply("Messages this month for `" + Objects.requireNonNull(e.getGuild().getMemberById(user)).getNickname() + "`: `" + data.get("messages") + "` messages");
	}
}