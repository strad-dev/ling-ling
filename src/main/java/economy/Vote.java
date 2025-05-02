package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;

import java.util.Random;

// CLAIM COMMAND - TOO LAZY TO RENAME
public class Vote {
	public static void vote(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		long time = System.currentTimeMillis();
		if(time < (long) data.get("voteCD")) {
			long milliseconds = (long) data.get("voteCD") - time;
			long hours = milliseconds / 3600000;
			milliseconds -= hours * 3600000;
			long minutes = milliseconds / 60000;
			milliseconds -= minutes * 60000;
			long seconds = milliseconds / 1000;
			milliseconds -= seconds * 1000;
			e.reply("You claimed your Free Box too recently!  Wait " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
		} else {
			data.replace("voteBox", (long) data.get("voteBox") + 2);
			data.replace("votes", (long) data.get("votes") + 1);
			data.replace("voteCD", System.currentTimeMillis() + 21540000);
			Random random = new Random();
			double chance = random.nextDouble();
			double increase = 1.0 + (long) data.get("magicFind") * 0.01;
			boolean extraInfo = (boolean) data.get("extraInfo");
			data.replace("voteBox", ((long) data.get("voteBox")) + 2);
			data.replace("essence", (long) data.get("essence") + 75);
			String message = "You have received `2`" + Emoji.FREE_BOX;
			if(chance > 0.225 * increase) {
				message += "";
			} else if(chance > 0.125 * increase) { // 0.1 (1 in 10)
				data.replace("voteBox", ((long) data.get("voteBox")) + 1);
				message += "\n\n**BONUS** You found an extra " + Emoji.FREE_BOX + "!";
			} else if(chance > 0.075 * increase) { // 0.05 (1 in 20)
				data.replace("giftBox", ((long) data.get("giftBox")) + 1);
				message += "\n\n**BONUS** You found an extra " + Emoji.GIFT_BOX + "!";
			} else if(chance > 0.035 * increase) { // 0.04 (1 in 25)
				data.replace("kits", ((long) data.get("kits")) + 1);
				message += "\n\n**BONUS** You found an extra " + Emoji.MUSICIAN_KIT + "!";
				RNGesus.sendLog(e, data, "Musician Kit", false);
			} else if(chance > 0.015 * increase) { // 0.02 (1 in 50)
				data.replace("linglingBox", ((long) data.get("linglingBox")) + 1);
				message += "\n\n**BONUS** You found an extra " + Emoji.LING_LING_BOX + "!";
				RNGesus.sendLog(e, data, "Ling Ling Box", false);
			} else if(chance > 0.005 * increase) { // 0.01 (1 in 100)
				data.replace("crazyBox", ((long) data.get("crazyBox")) + 1);
				message += "\n\n**BONUS** You found an extra " + Emoji.CRAZY_BOX + "!";
				RNGesus.sendLog(e, data, "Crazy Person Box", false);
			} else { // 0.005 (1 in 200)
				data.replace("RNGesusBox", ((long) data.get("RNGesusBox")) + 1);
				message += "\n\n# INSANE DROP\nYou found an extra " + Emoji.RNGESUS_BOX + "! https://imgur.com/a/SSjcgz3";
				RNGesus.sendLog(e, data, "RNGesus Box", false);
			}
			message += "\n\nYou also earned `75`:sparkles:\n\nRemember to vote for Ling Ling at <https://top.gg/bot/733409243222507670/vote>!";
			if((long) data.get("votes") % 40 == 0) {
				data.replace("medals", (long) data.get("medals") + 1);
				message += "\n\nYou have claimed Free Boxes `" + data.get("votes") + "` times!  Enjoy your Ling Ling Medal!";
			}
			e.reply(message);
			RNGesus.lootbox(e, data);
			SaveData.saveData(e, data);
		}
	}
}