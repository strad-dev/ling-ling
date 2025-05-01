package leveling;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;
import processes.Utils;

import java.util.Objects;
// BEETHOVEN-ONLY CLASS

public class SetLevel {
	public static void setLevelingData(GenericDiscordEvent e) {
		if(Objects.requireNonNull(e.getGuild().getMember(e.getAuthor())).getRoles().contains(e.getGuild().getRoleById("735308459452661802"))) {
			String[] message = e.getMessage().getContentRaw().split(" ");
			String field = message[3];
			String id = message[2];
			JSONParser parser = new JSONParser();
			JSONObject data = DatabaseManager.getDataById("Leveling Data", id);
			if(data == null) {
				e.getMessage().reply("This save doesn't exist!").queue();
				return;
			}
			if(message[3].equals("level") || message[3].equals("xp") || message[3].equals("messages")) {
				data.replace(message[3], Long.parseLong(message[4]));
			} else {
				e.reply("You did not provide a valid field!  Valid fields: `level` `xp` `messages`");
				return;
			}
			DatabaseManager.saveDataById("Leveling Data", id, data);
			e.reply("Successfully set `" + Objects.requireNonNull(e.getGuild().getMemberById(id)).getNickname() + "`'s " + field + " to `" + message[4] + "`.");
		} else {
			Utils.permissionDenied(e);
		}
	}
}