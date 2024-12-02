package leveling;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Numbers;

import java.awt.*;

// BEETHOVEN-ONLY CLASS
public class Settings {
	public static void settings(GenericDiscordEvent e) {
		String[] message = e.getMessage().getContentRaw().split(" ");
		JSONObject data = DatabaseManager.getMiscData();

		String option;
		try {
			assert data != null;
			option = message[2];
		} catch(Exception exception) {
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("**__Leveling Settings__**")
					.setFooter("Beethoven (1770-1827)", e.getJDA().getSelfUser().getAvatarUrl())
					.addField("**Current Settings**", "Min: " + Numbers.formatNumber(data.get("levelMin")) +
							"\nMax: " + Numbers.formatNumber(data.get("levelMax")) +
							"\nCooldown: " + Numbers.formatNumber(data.get("levelCooldown")) + " milliseconds", true)
					.setColor(Color.BLUE);
			e.replyEmbeds(builder.build());
			return;
		}
		try {
			switch(option) {
				case "min" -> data.replace("levelMin", Long.parseLong(message[3]));
				case "max" -> data.replace("levelMax", Long.parseLong(message[3]));
				case "cooldown" -> data.replace("levelCooldown", Long.parseLong(message[3]));
				default -> {
					e.reply("Not a valid option!  Valid options: `min` `max` `cooldown`");
				}
			}
		} catch(Exception exception) {
			e.reply("Did not provide an Integer, or did not provide an input.  Bald idiot.");
		}
		DatabaseManager.saveMiscData(data);
		e.reply("Changed setting `" + message[2] + "` to `" + message[3] + "`");
	}
}