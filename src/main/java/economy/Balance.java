package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;

public class Balance {
	public static void balance(GenericDiscordEvent e, String user) {
		if(user.isEmpty()) {
			user = e.getAuthor().getId();
		}

		JSONObject data = DatabaseManager.getDataForUser("Economy Data", user);
		if(data == null) {
			e.reply("This save file does not exist!");
			return;
		}

		if(user.equals(e.getAuthor().getId())) {
			if(!data.get("discordName").toString().equals(e.getAuthor().getEffectiveName())) {
				data.replace("discordName", e.getAuthor().getEffectiveName());
				SaveData.saveData(e, data);
			}
		}

		try {
			user = data.get("discordName").toString();
		} catch(Exception exception) {
			user = "Someone";
		}

		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.setTitle(user + "'s Profile");
		builder.addField("General Stats", "Balance: " + Utils.formatNumber(data.get("violins")) + Emoji.VIOLINS +
				"\nBank Balance: " + Utils.formatNumber(data.get("bank")) + "/" + Utils.formatNumber(Utils.maxBank((long) data.get("storage"), (long) data.get("benevolentBankers"))) + Emoji.VIOLINS +
				"\nLing Ling Medals: " + Utils.formatNumber(data.get("medals")) + Emoji.MEDALS +
				"\nHourly Income: " + Utils.formatNumber(data.get("income")) + Emoji.VIOLINS + "/hour", false);
		builder.addField("Medals", Emoji.FIRST_PLACE + Utils.formatNumber(data.get("firstPlace")) +
				"\n" + Emoji.SECOND_PLACE + Utils.formatNumber(data.get("secondPlace")) +
				"\n" + Emoji.THIRD_PLACE + Utils.formatNumber(data.get("thirdPlace")), false);
		e.replyEmbeds(builder.build());
	}
}