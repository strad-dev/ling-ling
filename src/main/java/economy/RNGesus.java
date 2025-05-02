package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class RNGesus {
	private static double chance;
	private static StringBuilder message;
	private static boolean extraInfo;
	private static double increase;

	public static void sendLog(GenericDiscordEvent e, JSONObject data, String drop, boolean messageInOriginal) {
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("RNGesus Drop!")
				.addField("User: **" + data.get("discordName") + "** `" + data.get("discordID") + "`", "Drop: " + drop, false);
		switch(drop) {
			case "Musician Kit" -> {
				builder.setColor(Color.BLUE);
				if(extraInfo && messageInOriginal) {
					message.append("\n*You rolled `").append(chance).append("`, the range for obtaining this box was between `")
							.append(0.006 * increase).append("` and `").append(0.016 * increase).append("`*");
				}
				if(messageInOriginal) {
					builder.addField("Decimal rolled: `" + chance + "`", "Range: `" + 0.006 * increase + "` - `" + 0.016 * increase + "`", false);
				}
			}
			case "Ling Ling Box" -> {
				builder.setColor(Color.MAGENTA);
				if(extraInfo && messageInOriginal) {
					message.append("\n*You rolled `").append(chance).append("`, the range for obtaining this box was between `")
							.append(0.002 * increase).append("` and `").append(0.006 * increase).append("`*");
				}
				if(messageInOriginal) {
					builder.addField("Decimal rolled: `" + chance + "`", "Range: `" + 0.002 * increase + "` - `" + 0.006 * increase + "`", false);
				}
			}
			case "Crazy Person Box" -> {
				builder.setColor(Color.MAGENTA);
				if(extraInfo && messageInOriginal) {
					message.append("\n*You rolled `").append(chance).append("`, the range for obtaining this box was between `")
							.append(0.0005 * increase).append("` and `").append(0.002 * increase).append("`*");
				}
				if(messageInOriginal) {
					builder.addField("Decimal rolled: `" + chance + "`", "Range: `" + 0.0005 * increase + "` - `" + 0.002 * increase + "`", false);
				}
			}
			case "RNGesus Box" -> {
				builder.setColor(Color.RED);
				if(extraInfo && messageInOriginal) {
					message.append("\n*You rolled `").append(chance).append("`, the range for obtaining this box was between `0` and `")
							.append(0.0005 * increase).append("`*");
				}
				Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("734697505543159879"))
						.sendMessage("WOW!  `" + e.getAuthor().getEffectiveName() + "` found an **__RNGESUS BOX__**!!!").queue();
				if(messageInOriginal) {
					builder.addField("Decimal rolled: `" + chance + "`", "Range: `0` - `" + 0.0005 * increase + "`", false);
				}
			}
		}
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("1029498872441077860")).sendMessageEmbeds(builder.build()).queue();
		if(messageInOriginal) {
			e.sendMessage(message.toString());
		}
	}

	public static void lootbox(GenericDiscordEvent e, JSONObject data) {
		message = new StringBuilder();
		Random random = new Random();
		chance = random.nextDouble();
		increase = 1.0 + (long) data.get("magicFind") * 0.01;
		extraInfo = (boolean) data.get("extraInfo");
		if(chance > 0.016 * increase) {
			return;
		} else if(chance > 0.006 * increase) { // 0.01 (1 in 100)
			data.replace("kits", (long) data.get("kits") + 1);
			data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 3);
			message.append("**Rare Drop!** " + Emoji.MUSICIAN_KIT + " <@").append(data.get("discordID")).append(">\nYou found a Musician Kit while you were out and about.");
			sendLog(e, data, "Musician Kit", true);
		} else if(chance > 0.002 * increase) { // 0.004 (1 in 250)
			data.replace("linglingBox", (long) data.get("linglingBox") + 1);
			data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 4);
			message.append("**Very Rare Drop!** " + Emoji.LING_LING_BOX + " <@").append(data.get("discordID")).append(">\nYou found a Ling Ling Box sitting in your room!");
			sendLog(e, data, "Ling Ling Box", true);
		} else if(chance > 0.0005 * increase) { // 0.0015 (1 in 666	)
			data.replace("crazyBox", (long) data.get("crazyBox") + 1);
			data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 6);
			message.append("**CRAZY RARE DROP!** " + Emoji.CRAZY_BOX + " <@").append(data.get("discordID")).append(">\nYou see a CRAZY PERSON BOX appear in front of you!");
			sendLog(e, data, "Crazy Person Box", true);
		} else { // 0.0005 (1 in 2000)
			data.replace("RNGesusBox", (long) data.get("RNGesusBox") + 1);
			data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 10);
			message.append("https://imgur.com/a/SSjcgz3 " + Emoji.RNGESUS_BOX + " <@").append(data.get("discordID")).append(">\nYou see an **__RNGESUS BOX__** appear in front of you! GG!");
			sendLog(e, data, "RNGesus Box", true);
		}
		Achievement.calculateAchievement(e, data, "RNGesusWeight", "Lucky");
	}

	public static String voteRewards(GenericDiscordEvent e, JSONObject data) {
		String localMessage;
		if(data == null) {
			localMessage = """
					Thank you for voting for Ling Ling!  Unfortunately you don't have a save, but I will take your vote and run with it >:)

					Run `start` to get a save file!""";
		} else {
			Random random = new Random();
			chance = random.nextDouble();
			increase = 1.0 + (long) data.get("magicFind") * 0.01;
			extraInfo = (boolean) data.get("extraInfo");
			data.replace("voteBox", ((long) data.get("voteBox")) + 2);
			localMessage = "Thank you for voting for Ling Ling!  You have received `2`" + Emoji.FREE_BOX + "!";
			if(chance > 0.225 * increase) {
				localMessage += "";
			} else if(chance > 0.125 * increase) { // 0.1 (1 in 10)
				data.replace("voteBox", ((long) data.get("voteBox")) + 1);
				localMessage += "\n\n**BONUS** You found an extra " + Emoji.FREE_BOX + "!";
			} else if(chance > 0.075 * increase) { // 0.05 (1 in 20)
				data.replace("giftBox", ((long) data.get("giftBox")) + 1);
				localMessage += "\n\n**BONUS** You found an extra " + Emoji.GIFT_BOX + "!";
			} else if(chance > 0.035 * increase) { // 0.04 (1 in 25)
				data.replace("kits", ((long) data.get("kits")) + 1);
				localMessage += "\n\n**BONUS** You found an extra " + Emoji.MUSICIAN_KIT + "!";
				sendLog(e, data, "Musician Kit", false);
			} else if(chance > 0.015 * increase) { // 0.02 (1 in 50)
				data.replace("linglingBox", ((long) data.get("linglingBox")) + 1);
				localMessage += "\n\n**BONUS** You found an extra " + Emoji.LING_LING_BOX + "!";
				sendLog(e, data, "Ling Ling Box", false);
			} else if(chance > 0.005 * increase) { // 0.01 (1 in 100)
				data.replace("crazyBox", ((long) data.get("crazyBox")) + 1);
				localMessage += "\n\n**BONUS** You found an extra " + Emoji.CRAZY_BOX + "!";
				sendLog(e, data, "Crazy Person Box", false);
			} else { // 0.005 (1 in 200)
				data.replace("RNGesusBox", ((long) data.get("RNGesusBox")) + 1);
				localMessage += "\n\n# INSANE DROP\nYou found an extra " + Emoji.RNGESUS_BOX + "!\nhttps://imgur.com/a/SSjcgz3";
				sendLog(e, data, "RNGesus Box", false);
			}
			localMessage += "\n\nYou also earned `180`:sparkles:";
			if((boolean) data.get("banned")) {
				localMessage += "\n\nUnfortunately, you are currently banned from using the economy, but I am nice and have rewarded you anyway in the unlikely case you do get unbanned.";
			}
		}
		return localMessage;
	}
}