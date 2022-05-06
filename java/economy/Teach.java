package economy;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import processes.Numbers;

import java.util.Random;

public class Teach {
	public Teach(MessageReceivedEvent e) {
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
				e.getMessage().reply("Chill, you can't teach two students at once!  Wait " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!").mentionRepliedUser(false).queue();
			} else {
				long base = random.nextInt(10001) + 35000;
				base *= Math.pow(1.15, (long) data.get("students"));
				base *= Math.pow(1.1, (long) data.get("lessonCharge"));
				base *= Math.pow(1.05, (long) data.get("training"));
				if((boolean) data.get("longerLessons")) {
					base *= 2;
					e.getMessage().reply("You taught a student for an hour and earned " + Numbers.FormatNumber(base) + ":violin:").mentionRepliedUser(false).queue();
					data.replace("hoursTaught", (double) data.get("hoursTaught") + 1);
				} else {
					e.getMessage().reply("You taught a student for a half-hour and earned " + Numbers.FormatNumber(base) + ":violin:").mentionRepliedUser(false).queue();
					data.replace("hoursTaught", (double) data.get("hoursTaught") + 0.5);
				}
				data.replace("teachCD", time + 3540000);
				data.replace("earnings", (long) data.get("earnings") + base);
				Numbers.CalculateLoan(data, base);
				RNGesus.Lootbox(e, data);
				new SaveData(e, data);
			}
		} else {
			e.getMessage().reply("You must be certified to teach before you can use this command!").mentionRepliedUser(false).queue();
		}
	}
}
