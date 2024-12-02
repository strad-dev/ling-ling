package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Numbers;

import java.awt.*;

public class Achievement {
	private static JSONObject data;
	private static final long[] ACHIEVEMNT_NUMS_100B = new long[]{10000000, 100000000, 1000000000, 10000000000L, 100000000000L};
	private static final long[] ACHIEVEMNT_NUMS_2B = new long[]{5000000, 25000000, 100000000, 500000000, 2000000000};
	private static final long[] ACHIEVEMNT_NUMS_1B = new long[]{1000000, 10000000, 100000000, 250000000, 1000000000};
	private static final long[] ACHIEVEMNT_NUMS_200K = new long[]{1000, 5000, 20000, 10000, 200000};
	private static final long[] ACHIEVEMNT_NUMS_10K = new long[]{100, 250, 1000, 2500, 10000};
	private static final long[] ACHIEVEMNT_NUMS_5K = new long[]{40, 150, 500, 1500, 5000};
	private static final long[] ACHIEVEMNT_NUMS_1500 = new long[]{25, 75, 200, 500, 1500};
	private static final long[] ACHIEVEMNT_NUMS_1000 = new long[]{10, 30, 100, 300, 1000};
	private static final long[] ACHIEVEMNT_NUMS_500 = new long[]{15, 40, 100, 200, 500};
	private static final long[] ACHIEVEMNT_NUMS_365 = new long[]{7, 28, 84, 168, 365};
	private static final long[] ACHIEVEMNT_NUMS_SCALE_CHALLENGE = new long[]{60, 150, 360, 720, 1380};

	public static void generateField(EmbedBuilder builder, String achievement, long[] thresholds, long actual) {
		String description;
		switch(achievement) {
			case "Moneymaker" -> description = "Earn $$$ " + Emoji.VIOLINS;
			case "Technician" -> description = "Play $$$ scales";
			case "40 Hours A Day" -> description = "Practise for $$$ hours";
			case "Well-Practised" -> description = "Attend $$$ rehearsals";
			case "Virtuoso" -> description = "Perform $$$ times";
			case "Sensei" -> description = "Teach for $$$ hours";
			case "Lucky" -> description = "Gain $$$ RNGesus Weight";
			case "AFKer" -> description = "Reach $$$ Hourly Income";
			case "Big Spender" -> description = "Spend $$$ " + Emoji.VIOLINS + " in the Market";
			case "Entrepeneur" -> description = "Earn $$$ " + Emoji.VIOLINS + " in the Market";
			case "Dedication" ->
					description = "Reach a Daily Streak of $$$ days\nCurrent streak: " + Numbers.formatNumber(data.get("streak"));
			case "English Major" -> description = "Unscramble $$$ Luthiers";
			case "Generous" -> description = "Give $$$ Gifts";
			case "Heartless" -> description = "Rob a total of $$$ " + Emoji.VIOLINS;
			case "Gambling Addict" -> description = "Earn a net total of $$$ " + Emoji.VIOLINS + " from gambling";
			case "No-Life" ->
					description = "Play $$$ scales in a 24-hour period.\nYou can reset the timer using `!resetscales`.\nCurrent streak: " + Numbers.formatNumber(data.get("scaleStreak"));
			default ->
					description = "Non-existent achievement!  Contact `stradivariusviolin` to yell at him for being stupid.";
		}
		if(actual < thresholds[0]) {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[0]));
			builder.addField(achievement + " I", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[0]) + " **(`" + String.format("%,.2f", ((actual * 100) / (double) thresholds[0])) + "%`)**", false);
		} else if(actual < thresholds[1]) {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[1]));
			builder.addField(achievement + " II", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[1]) + " **(`" + String.format("%,.2f", (actual * 100) / (double) thresholds[1]) + "%`)**", false);
		} else if(actual < thresholds[2]) {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[2]));
			builder.addField(achievement + " III", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[2]) + " **(`" + String.format("%,.2f", (actual * 100) / (double) thresholds[2]) + "%`)**", false);
		} else if(actual < thresholds[3]) {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[3]));
			builder.addField(achievement + " IV", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[3]) + " **(`" + String.format("%,.2f", (actual * 100) / (double) thresholds[3]) + "%`)**", false);
		} else if(actual < thresholds[4]) {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[4]));
			if(achievement.equals("No-Life")) {
				builder.addField("Go Touch Grass", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[4]) + " **(`" + String.format("%,.2f", (actual * 100) / (double) thresholds[4]) + "%`)**", false);
			} else {
				builder.addField(achievement + " V", description + "\n" + Numbers.formatNumber(actual) + "/" + Numbers.formatNumber(thresholds[4]) + " **(`" + String.format("%,.2f", (actual * 100) / (double) thresholds[4]) + "%`)**", false);
			}
		} else {
			description = description.replace("$$$", Numbers.formatNumber(thresholds[4]));
			if(achievement.equals("No-Life")) {
				builder.addField("Go Touch Grass", description + "\n" + Numbers.formatNumber(actual) + " :white_check_mark:\njesus christ stop now pls and ty", false);
			} else {
				builder.addField(achievement + " V", description + "\n" + Numbers.formatNumber(actual) + " :white_check_mark:", false);
			}
		}
	}

	public static void achievement(GenericDiscordEvent e, int page) {
		String user = e.getAuthor().getId();
		data = DatabaseManager.getDataForUser(e, "Economy Data", user);
		if(data == null) {
			return;
		}
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.decode((String) data.get("color")))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl());
		if(page == 1) {
			builder.setTitle(data.get("discordName") + "'s Achievements - Page 1");
			generateField(builder, "Moneymaker", ACHIEVEMNT_NUMS_100B, (long) data.get("earnings"));
			generateField(builder, "Technician", ACHIEVEMNT_NUMS_10K, (long) data.get("scalesPlayed"));
			generateField(builder, "40 Hours A Day", ACHIEVEMNT_NUMS_5K, (long) ((double) data.get("hoursPractised")));
			generateField(builder, "Well-Practised", ACHIEVEMNT_NUMS_1500, (long) data.get("rehearsals"));
			generateField(builder, "Virtuoso", ACHIEVEMNT_NUMS_500, (long) data.get("performances"));
		} else if(page == 2) {
			builder.setTitle(data.get("discordName") + "'s Achievements - Page 2");
			generateField(builder, "Sensei", ACHIEVEMNT_NUMS_5K, (long) ((double) data.get("hoursTaught")));
			generateField(builder, "Lucky", ACHIEVEMNT_NUMS_1000, (long) data.get("RNGesusWeight"));
			generateField(builder, "AFKer", ACHIEVEMNT_NUMS_200K, (long) data.get("income"));
			generateField(builder, "Big Spender", ACHIEVEMNT_NUMS_1B, (long) data.get("moneySpent"));
			generateField(builder, "Entrepeneur", ACHIEVEMNT_NUMS_1B, (long) data.get("moneyEarned"));
		} else if(page == 3) {
			builder.setTitle(data.get("discordName") + "'s Achievements - Page 3");
			generateField(builder, "Dedication", ACHIEVEMNT_NUMS_365, (long) data.get("maxStreak"));
			generateField(builder, "English Major", ACHIEVEMNT_NUMS_5K, (long) data.get("luthiers"));
			generateField(builder, "Generous", ACHIEVEMNT_NUMS_1000, (long) data.get("giftsGiven"));
			generateField(builder, "Heartless", ACHIEVEMNT_NUMS_2B, (long) data.get("robbed"));
			generateField(builder, "Gambling Addict", ACHIEVEMNT_NUMS_2B, (long) data.get("winnings"));
		} else if(page == 2147483647) {
			builder.setTitle(data.get("discordName") + "'s Challenges");
			generateField(builder, "No-Life", ACHIEVEMNT_NUMS_SCALE_CHALLENGE, (long) data.get("scaleStreakRecord"));
		} else {
			e.reply("Invalid page!  Valid pages: `1` `2` `3` `c`");
			return;
		}
		e.replyEmbeds(builder.build());
	}

	public static void calculateAchievement(GenericDiscordEvent e, JSONObject data, String index, String achievement) {
		long[] thresholds;
		long actual;
		try {
			actual = (long) data.get(index);
		} catch(Exception exception) {
			actual = (long) ((double) data.get(index));
		}
		String alreadyUnlocked = (String) data.get("achievements");
		switch(achievement) {
			case "Moneymaker" -> thresholds = ACHIEVEMNT_NUMS_100B;
			case "Heartless", "Gambling Addict" -> thresholds = ACHIEVEMNT_NUMS_2B;
			case "Big Spender", "Entrepeneur" -> thresholds = ACHIEVEMNT_NUMS_1B;
			case "AFKer" -> thresholds = ACHIEVEMNT_NUMS_200K;
			case "Technician" -> thresholds = ACHIEVEMNT_NUMS_10K;
			case "40 Hours A Day", "English Major", "Sensei" -> thresholds = ACHIEVEMNT_NUMS_5K;
			case "Well-Practised" -> thresholds = ACHIEVEMNT_NUMS_1500;
			case "Lucky", "Generous" -> thresholds = ACHIEVEMNT_NUMS_1000;
			case "Virtuoso" -> thresholds = ACHIEVEMNT_NUMS_500;
			case "Dedication" -> thresholds = ACHIEVEMNT_NUMS_365;
			case "No-Life" -> thresholds = ACHIEVEMNT_NUMS_SCALE_CHALLENGE;
			default -> {
				e.sendMessage("Non-existent achievement!  Contact `stradivariusviolin` to yell at him for being stupid.");
				return;
			}
		}
		String message = "";
		if(actual >= thresholds[4] && !alreadyUnlocked.contains(achievement + "5")) {
			if(achievement.equals("No-Life")) {
				message = ":tada: You have completed **Go Touch Grass**! :tada:\n\n" +
						"**Rewards**" +
						"\n`5 000 000 000` " + Emoji.VIOLINS +
						"\n`200` " + Emoji.BLESSING +
						"\n`50` " + Emoji.MUSICIAN_KIT +
						"\n`30` " + Emoji.LING_LING_BOX +
						"\n`100` " + Emoji.CRAZY_BOX +
						"\n`15` " + Emoji.RNGESUS_BOX +
						"\n`+1` Magic Find" +
						"\n**SPECIAL ROLE** in support server" +
						"\n**SPECIAL BADGE** on profile";
				data.replace("violins", (long) data.get("violins") + 5000000000L);
				data.replace("blessings", (long) data.get("blessings") + 200);
				data.replace("kits", (long) data.get("kits") + 50);
				data.replace("linglingBox", (long) data.get("linglingBox") + 30);
				data.replace("crazyBox", (long) data.get("crazyBox") + 100);
				data.replace("RNGesusBox", (long) data.get("RNGesusBox") + 15);
				data.replace("magicFind", (long) data.get("magicFind") + 1);
				data.replace("goOutside", true);
			} else {
				message = ":tada: You have completed **" + achievement + " V**! :tada:\n\n" +
						"**Rewards**" +
						"\n`100 000 000` " + Emoji.VIOLINS +
						"\n`16` " + Emoji.BLESSING +
						"\n`10` " + Emoji.MUSICIAN_KIT +
						"\n`5` " + Emoji.LING_LING_BOX +
						"\n`3` " + Emoji.CRAZY_BOX +
						"\n`2` " + Emoji.RNGESUS_BOX;
				data.replace("violins", (long) data.get("violins") + 100000000);
				data.replace("blessings", (long) data.get("blessings") + 16);
				data.replace("kits", (long) data.get("kits") + 10);
				data.replace("linglingBox", (long) data.get("linglingBox") + 5);
				data.replace("crazyBox", (long) data.get("crazyBox") + 3);
				data.replace("RNGesusBox", (long) data.get("RNGesusBox") + 2);
			}
			alreadyUnlocked += achievement + 5 + " ";
		} else if(actual >= thresholds[3] && !alreadyUnlocked.contains(achievement + "4")) {
			message = ":tada: You have completed **" + achievement + " IV**! :tada:\n\n" +
					"**Rewards**" +
					"\n`25 000 000` " + Emoji.VIOLINS +
					"\n`8` " + Emoji.BLESSING +
					"\n`5` " + Emoji.MUSICIAN_KIT +
					"\n`3` " + Emoji.LING_LING_BOX +
					"\n`2` " + Emoji.CRAZY_BOX +
					"\n`1` " + Emoji.RNGESUS_BOX;
			data.replace("violins", (long) data.get("violins") + 25000000);
			data.replace("blessings", (long) data.get("blessings") + 8);
			data.replace("kits", (long) data.get("kits") + 5);
			data.replace("linglingBox", (long) data.get("linglingBox") + 3);
			data.replace("crazyBox", (long) data.get("crazyBox") + 2);
			data.replace("RNGesusBox", (long) data.get("RNGesusBox") + 1);
			alreadyUnlocked += achievement + 4 + " ";
		} else if(actual >= thresholds[2] && !alreadyUnlocked.contains(achievement + "3")) {
			message = ":tada: You have completed **" + achievement + " III**! :tada:\n\n" +
					"**Rewards**" +
					"\n`5 000 000` " + Emoji.VIOLINS +
					"\n`4` " + Emoji.BLESSING +
					"\n`3` " + Emoji.MUSICIAN_KIT +
					"\n`2` " + Emoji.LING_LING_BOX +
					"\n`1` " + Emoji.CRAZY_BOX;
			data.replace("violins", (long) data.get("violins") + 5000000);
			data.replace("blessings", (long) data.get("blessings") + 4);
			data.replace("kits", (long) data.get("kits") + 3);
			data.replace("linglingBox", (long) data.get("linglingBox") + 2);
			data.replace("crazyBox", (long) data.get("crazyBox") + 1);
			alreadyUnlocked += achievement + 3 + " ";
		} else if(actual >= thresholds[1] && !alreadyUnlocked.contains(achievement + "2")) {
			message = ":tada: You have completed **" + achievement + " II**! :tada:\n\n" +
					"**Rewards**" +
					"\n`1 000 000` " + Emoji.VIOLINS +
					"\n`2` " + Emoji.BLESSING +
					"\n`2` " + Emoji.MUSICIAN_KIT +
					"\n`1` " + Emoji.LING_LING_BOX;
			data.replace("violins", (long) data.get("violins") + 1000000);
			data.replace("blessings", (long) data.get("blessings") + 2);
			data.replace("kits", (long) data.get("kits") + 2);
			data.replace("linglingBox", (long) data.get("linglingBox") + 1);
			alreadyUnlocked += achievement + 2 + " ";
		} else if(actual >= thresholds[0] && !alreadyUnlocked.contains(achievement + "1")) {
			message = ":tada: You have completed **" + achievement + " I**! :tada:\n\n" +
					"**Rewards**" +
					"\n`200 000` " + Emoji.VIOLINS +
					"\n`1` " + Emoji.BLESSING +
					"\n`1` " + Emoji.MUSICIAN_KIT;
			data.replace("violins", (long) data.get("violins") + 200000);
			data.replace("blessings", (long) data.get("blessings") + 1);
			data.replace("kits", (long) data.get("kits") + 1);
			alreadyUnlocked += achievement + 1 + " ";
		}
		if(!message.isEmpty()) {
			e.sendMessage(message);
		}
		data.replace("achievements", alreadyUnlocked);
	}
}