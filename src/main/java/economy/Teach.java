package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

import java.util.Random;

public class Teach {
	public static void teach(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		if((boolean) data.get("certificate")) {
			long time = System.currentTimeMillis();
			Random random = new Random();
			if(time < (long) data.get("teachCD")) {
				long milliseconds = (long) data.get("teachCD") - time;
				long minutes = milliseconds / 60000;
				milliseconds -= minutes * 60000;
				long seconds = milliseconds / 1000;
				milliseconds -= seconds * 1000;
				e.reply("Chill, you can't teach two students at once!  Wait " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
			} else {
				long base = random.nextInt(10001) + 35000;
				base = (long) (base * Math.pow(1.15, (long) data.get("students")));
				base = (long) (base * Math.pow(1.1, (long) data.get("lessonCharge")));
				base = (long) (base * Math.pow(1.05, (long) data.get("training")));
				if((boolean) data.get("longerLessons")) {
					base *= 2;
					e.reply("You taught a student for an hour and earned " + Utils.formatNumber(base) + Emoji.VIOLINS + " and `20`:sparkles:");
					data.replace("hoursTaught", (double) data.get("hoursTaught") + 1);
				} else {
					e.reply("You taught a student for a half-hour and earned " + Utils.formatNumber(base) + Emoji.VIOLINS + " and `20`:sparkles:");
					data.replace("hoursTaught", (double) data.get("hoursTaught") + 0.5);
				}
				data.replace("teachCD", time + 3540000);
				data.replace("earnings", (long) data.get("earnings") + base);
				Utils.calculateLoan(data, base);
				data.replace("essence", (long) data.get("essence") + 20);
				RNGesus.lootbox(e, data);
				Achievement.calculateAchievement(e, data, "hoursTaught", "Sensei");
				SaveData.saveData(e, data);
			}
		} else {
			e.reply("You must be certified to teach before you can use this command!");
		}
	}
}
