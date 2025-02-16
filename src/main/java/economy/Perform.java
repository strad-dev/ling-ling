package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

import java.util.Random;

public class Perform {
	public static void perform(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		long time = System.currentTimeMillis();
		Random random = new Random();
		long violins = (long) data.get("violins");
		long violinsEarned = (long) data.get("earnings");
		StringBuilder builder = new StringBuilder();
		if(time < (long) data.get("performCD")) {
			long milliseconds = (long) data.get("performCD") - time;
			long days = milliseconds / 86400000;
			milliseconds -= days * 86400000;
			long hours = milliseconds / 3600000;
			milliseconds -= hours * 3600000;
			long minutes = milliseconds / 60000;
			milliseconds -= minutes * 60000;
			long seconds = milliseconds / 1000;
			milliseconds -= seconds * 1000;
			e.reply("Don't tire yourself with two performances a week!  Wait " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
		} else {
			boolean badEvent = false;
			long base = Utils.calculateAmount(data, random.nextInt(1001) + 3000);
			double num = random.nextDouble();
			if(num > 0.5) {
				builder.append("You performed your solo and earned ").append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
			} else if(num > 0.15) {
				num = random.nextDouble();
				if(num > 0.65) {
					builder.append("Your teacher approved your performance.  Your tiger mom saw the comment, and gave you ")
							.append(Utils.formatNumber((long) (base * 0.05))).append(Emoji.VIOLINS).append(" in addition to the ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" that you earned.");
					base = (long) (base * 1.1);
				} else if(num > 0.40) {
					builder.append("Your tiger mom approved your performance.  She gave you ")
							.append(Utils.formatNumber((long) (base * 0.2))).append(Emoji.VIOLINS).append(" in addition to the ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" that you earned.");
					base = (long) (base * 1.25);
				} else if(num > 0.2) {
					base = (long) (base * 1.5);
					builder.append("Brett and Eddy approved of your performance and increased the amount of violins you earned by 1.5x.  You got ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
				} else if(num > 0.05) {
					base *= 2;
					builder.append("It's raining violins!  You earned ").append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" you lucky dog");
				} else if(num > 0.01) {
					base *= 3;
					builder.append("**Rare Drop!**\nThe Berlin Philharmonic posted your performance on their homepage.  You got a HUGE ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append("\n\nDisclaimer: This does not guarantee a post IRL.");
					data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 1);
				} else {
					base *= 5;
					builder.append("**Very Rare Drop!**\nLing Ling enjoyed your performance but was displeased with some parts.  Nontheless, he (she?) granted you with ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
					data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 3);
				}
				violinsEarned += base;
			} else {
				num = random.nextDouble();
				long income = (long) data.get("income");
				if(num > 0.8) {
					base = (long) (base * 0.5);
					base -= income / 100;
					builder.append("Oh no!  Your E String snapped during the performance!  You couldn't go on, and only earned ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append("  You eventually had to pay ")
							.append(Utils.formatNumber(income / 100)).append(Emoji.VIOLINS).append(" for a new E String.");
				} else if(num > 0.6) {
					base = (long) (base * 0.9);
					builder.append("Your violin randomly went out of tune during the rehearsal.  You had to spend 4 minutes tuning it and were only able to earn ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
				} else if(num > 0.45) {
					base = (long) (base * 0.9);
					builder.append("You didn't memorize your piece and you had to use your music.  You only earned ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
				} else if(num > 0.3) {
					base = (long) (base * 0.5);
					base -= income / 10;
					builder.append("You hurt your wrist during the performance and only got half of the effectiveness.  You earned ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" but ended up paying ")
							.append(Utils.formatNumber(income / 10)).append(Emoji.VIOLINS).append(" in hospital fees.");
				} else if(num > 0.2) {
					base = (long) (base * 0.1);
					builder.append("**OOF**\nYour bridge fell off during the performance.  You spend the next half-hour trying to get it back on, and you only earned ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS);
				} else if(num > 0.1) {
					base = -1 * income;
					builder.append("**OOF**\nYou had a memory blank during the performance.  Your tiger mom took all your earnings, in addition to another ")
							.append(Utils.formatNumber(income)).append(Emoji.VIOLINS).append(" for shaming yourself.");
				} else if(num > 0.05) {
					base = (long) (base * 0.5);
					base -= income;
					builder.append("**OOF**\nYour performance was for a competition, and you only won Honorable Mention.  " +
							"Your tiger mom Kung-Paos your chicken and takes half your earnings, in addition to another ")
							.append(Utils.formatNumber(income)).append(Emoji.VIOLINS).append(" for being so mediocre.");
				} else if(num > 0.015) {
					builder.append(":regional_indicator_l:\nYour chin rest popped off your violin!  " +
							"You take your violin to the luthier, who informs you that the violin will have to stay overnight.  " +
							"You are not able to practise anything for 12 hours.");
					time += 43200000;
					data.replace("scaleCD", time);
					data.replace("practiceCD", time);
					badEvent = true;
					base = 0;
				} else if(num > 0.005) {
					builder.append(":regional_indicator_l:\nYou decided to fake your performance.  " +
							"Of course it didn't work and Ling Ling fined you").append(Utils.formatNumber((long) (violins * 0.95)))
							.append(Emoji.VIOLINS).append(" but Strad was going to remove these types of fines so your violins are fine.");
					// data.replace("violins", (long) (violins * 0.05));
					badEvent = true;
					base = 0;
				} else {
					builder.append("**wow RNGesus *__really__* hates you**\nYou dropped your violin.  How shameful.  " +
							"All cooldowns except daily and gamble have had one day added to them, and you were fined pretty much your entire balance for being careless on stage " +
							"but Strad was going to remove these types of fines so your violins are fine.");
					time += 86400000;
					// data.replace("violins", (long) (violins * 0.01));
					data.replace("scaleCD", time);
					data.replace("practiceCD", time);
					data.replace("rehearseCD", time);
					data.replace("performCD", time);
					badEvent = true;
					base = 0;
				}
			}
			violinsEarned += base;
			num = random.nextDouble();
			if(!badEvent) {
				if(num > 0.5) {
					data.replace("blessings", (long) data.get("blessings") + 1);
					builder.append("\nYour performance didn't win anything in Ling Ling Competition this time.  You walked away with ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" and `1`").append(Emoji.BLESSING);
				} else if(num > 0.20) {
					base = (long) (base * 1.5);
					data.replace("blessings", (long) data.get("blessings") + 2);
					data.replace("thirdPlace", (long) data.get("thirdPlace") + 1);
					builder.append("\n:trophy: Your performance won third place in the Ling Ling Competition.  Your earnings were multiplied by 1.5 to ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" and you walked away with `2`")
							.append(Emoji.BLESSING).append(" and a third place medal ").append(Emoji.THIRD_PLACE);
				} else if(num > 0.05) {
					base *= 2;
					data.replace("blessings", (long) data.get("blessings") + 3);
					data.replace("secondPlace", (long) data.get("secondPlace") + 1);
					builder.append("\n:trophy: Your performance won second place.  Your earnings were multiplied by 2 to ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" and you walked away with `3`")
							.append(Emoji.BLESSING).append(" and a second place medal ").append(Emoji.SECOND_PLACE);
				} else {
					base *= 3;
					data.replace("blessings", (long) data.get("blessings") + 5);
					data.replace("firstPlace", (long) data.get("firstPlace") + 1);
					builder.append("\n:trophy: Your performance won first place.  Congratulations!  Your earnings were multiplied by 3 to ")
							.append(Utils.formatNumber(base)).append(Emoji.VIOLINS).append(" and you walked away with `5`")
							.append(Emoji.BLESSING).append(" and a FIRST place medal ").append(Emoji.FIRST_PLACE);
				}
				if((boolean) data.get("timeCrunch")) {
					data.replace("performCD", time + 143940000);
				} else {
					data.replace("performCD", time + 215940000);
				}
			}
			e.reply(builder.toString());
			Utils.calculateLoan(data, base);
			data.replace("earnings", violinsEarned + base);
			data.replace("performances", (long) data.get("performances") + 1);
			RNGesus.lootbox(e, data);
			Achievement.calculateAchievement(e, data, "performances", "Virtuoso");
			SaveData.saveData(e, data);
		}
	}
}