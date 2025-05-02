package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class Daily {
	public static void daily(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		long time = System.currentTimeMillis();
		long dailyCD = (long) data.get("dailyCD");
		if(time < dailyCD) {
			long milliseconds = (long) data.get("dailyCD") - time;
			long hours = milliseconds / 3600000;
			milliseconds -= hours * 3600000;
			long minutes = milliseconds / 60000;
			milliseconds -= minutes * 60000;
			long seconds = milliseconds / 1000;
			milliseconds -= seconds * 1000;
			e.reply("I can't give out violins that fast, wait " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
		} else {
			long streak = (long) data.get("streak") + 1;
			long income = (long) data.get("income") + 1000;
			long bonusMedals = (long) data.get("bonusMedals");
			String message = "";
			if(time > dailyCD + 86400000 && streak != 0) {
				message += "Oh no!  Your streak was reset!\n";
				streak = 0;
			}
			long base = income + (streak * (income / 100));
			Utils.calculateLoan(data, base);
			data.replace("essence", (long) data.get("essence") + (income / 500));
			data.replace("earnings", (long) data.get("earnings") + base);
			data.replace("dailyCD", time + 85500000); // 23.75 hours cooldown
			message += "You received a total of " + Utils.formatNumber(base) + Emoji.VIOLINS + " and " + Utils.formatNumber((income / 500)) + ":sparkles:, with " +
					Utils.formatNumber(streak * (income / 100)) + Emoji.VIOLINS + " coming from your " + Utils.formatNumber(streak) + "-day streak!";
			if(bonusMedals > 0) {
				message += "\nLing Ling's favor grants you an additional " + Utils.formatNumber(bonusMedals) + Emoji.MEDALS;
				data.replace("medals", (long) data.get("medals") + bonusMedals);
			}
			message += "\n\nRemember to vote for Ling Ling at <https://top.gg/bot/733409243222507670/vote>!";
			if(streak > (long) data.get("maxStreak")) {
				data.replace("maxStreak", streak);
			}
			if(streak % 28 == 0 && streak != 0) {
				data.replace("medals", (long) data.get("medals") + 1);
				message += "\n\nYour streak has reached a multiple of `28`!  Enjoy your bonus Ling Ling Medal!";
			}
			e.reply(message);
			data.replace("streak", streak);
			RNGesus.lootbox(e, data);
			Achievement.calculateAchievement(e, data, "maxStreak", "Dedication");
			SaveData.saveData(e, data);
		}
	}
}