package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;

public class Stats {
	public static void stats(GenericDiscordEvent e, String user) {
		JSONObject data = DatabaseManager.getDataById("Economy Data", user);
		if(data == null) {
			e.reply("This save file does not exist!");
			return;
		}
		if(user.equals("768056391814086676")) {
			user = "**NARWHAL**";
		} else {
			try {
				user = data.get("discordName").toString();
			} catch(Exception exception) {
				user = "Someone";
			}
		}
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.setTitle(user + "'s Stats")
				.addField("**__RNGesus__**", "**Net Winnings**: " + Utils.formatNumber(data.get("winnings")) +
						"\n**Million-Dollar Tickets Drawn**: " + Utils.formatNumber(data.get("millions")) +
						"\n**Magic Find**: " + Utils.formatNumber(data.get("magicFind")) +
						"\n**RNGesus Weight**: " + Utils.formatNumber(data.get("RNGesusWeight")), true)
				.addBlankField(true)
				.addField("**__Robbing__**", "**Amount Earned from Robbing**: " + Utils.formatNumber(data.get("robbed")) +
						"\n**Amount Lost to Robbers**: " + Utils.formatNumber(data.get("lostToRob")), true)
				.addField("**__Commands__**", "**Hours Practised**: " + Utils.formatNumber((long) (double) data.get("hoursPractised")) +
						"\n**Scales Played**: " + Utils.formatNumber(data.get("scalesPlayed")) +
						"\n**Rehearsals Attended**: " + Utils.formatNumber(data.get("rehearsals")) +
						"\n**Performances Given**: " + Utils.formatNumber(data.get("performances")) +
						"\n**Hours Taught: **" + Utils.formatNumber((long) (double) data.get("hoursTaught")), true)
				.addBlankField(true)
				.addField("**__Lootboxes__**", "**Gifts Given**: " + Utils.formatNumber(data.get("giftsGiven")) +
						"\n**Gifts Received**: " + Utils.formatNumber(data.get("giftsReceived")) +
						"\n**Free Boxes Claimed**: " + Utils.formatNumber(data.get("votes")), true)
				.addField("**__Market__**", "**Items Purchased**: " + Utils.formatNumber(data.get("itemsBought")) +
						"\n**Items Sold**: " + Utils.formatNumber(data.get("itemsSold")) +
						"\n**Money Earned**: " + Utils.formatNumber(data.get("moneyEarned")) +
						"\n**Money Spent**: " + Utils.formatNumber(data.get("moneySpent")) +
						"\n**Taxes Paid**: " + Utils.formatNumber(data.get("taxPaid")), true)
				.addBlankField(true)
				.addField("**__Miscellaneous__**", "**Highest Daily Streak**: " + Utils.formatNumber(data.get("maxStreak")) +
						"\n**Highest Scale Streak**: " + Utils.formatNumber(data.get("scaleStreakRecord")) +
						"\n**Luthiers Unscrambled**: " + Utils.formatNumber(data.get("luthiers")) +
						"\n**Cheats Used**: " + Utils.formatNumber(data.get("cheater")) +
						"\n**Violins Earned**: " + Utils.formatNumber(data.get("earnings")) +
						"\n**Interest Earned**: " + Utils.formatNumber(data.get("interestEarned")) +
						"\n**Penalties Paid**: " + Utils.formatNumber(data.get("penaltiesIncurred")), true);
		e.replyEmbeds(builder.build());
	}
}