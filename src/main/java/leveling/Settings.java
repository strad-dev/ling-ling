package leveling;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

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
					.addField("**Current Settings**", "Min: " + Utils.formatNumber(data.get("levelMin")) +
							"\nMax: " + Utils.formatNumber(data.get("levelMax")) +
							"\nCooldown: " + Utils.formatNumber(data.get("levelCooldown")) + " milliseconds", true)
					.setColor(Color.BLUE);
			e.replyEmbeds(builder.build());
			return;
		}
		try {
			switch(option) {
				case "min" -> {
					if(Long.parseLong(message[3]) > (long) data.get("levelMax")) {
						e.reply("You cannot set a minimum higher than the maximum!");
						return;
					} else {
						data.replace("levelMin", Long.parseLong(message[3]));
					}
				}
				case "max" -> {
					if(Long.parseLong(message[3]) < (long) data.get("levelMin")) {
						e.reply("You cannot set a maximum lower than the minimum!");
						return;
					} else {
						data.replace("levelMax", Long.parseLong(message[3]));
					}
				}
				case "cooldown" -> data.replace("levelCooldown", Long.parseLong(message[3]));
				default -> {
					e.reply("Not a valid option!  Valid options: `min` `max` `cooldown`");
					return;
				}
			}
		} catch(Exception exception) {
			e.reply("Did not provide an Integer, or did not provide an input.  Bald idiot.");
			return;
		}
		DatabaseManager.saveMiscData(data);
		e.reply("Changed setting `" + message[2] + "` to `" + message[3] + "`");
	}
}