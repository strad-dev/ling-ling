package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

public class ResetStreak {
	public static void resetStreak(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		e.reply("You have reset your Scale Streak.  You can restart it at any time by running `!s`\nYou reached a streak of " + Utils.formatNumber(data.get("scaleStreak")));
		data.replace("scaleStreak", 0);
		data.replace("scaleStreakExpire", 0);
		SaveData.saveData(e, data);
	}
}