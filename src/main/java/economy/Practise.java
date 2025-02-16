package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Utils;

import java.util.Random;

public class Practise {
	public static void practise(GenericDiscordEvent e) {
		JSONObject data = LoadData.loadData(e);
		long time = System.currentTimeMillis();
		Random random = new Random();
		long violins = (long) data.get("violins");
		if(time < (long) data.get("practiceCD")) {
			long milliseconds = (long) data.get("practiceCD") - time;
			long minutes = milliseconds / 60000;
			milliseconds -= minutes * 60000;
			long seconds = milliseconds / 1000;
			milliseconds -= seconds * 1000;
			e.reply("Take a break, you already practised a lot!  Wait " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!");
		} else {
			boolean badEvent = false;
			long base = Utils.calculateAmount(data, random.nextInt(101) + 350);
			double num = random.nextDouble();
			if(num > 0.5) {
				e.reply("You practised for an hour and earned " + Utils.formatNumber(base) + Emoji.VIOLINS);
				data.replace("hoursPractised", (double) data.get("hoursPractised") + 1.0);
			} else if(num > 0.15) {
				num = random.nextDouble();
				if(num > 0.65) {
					e.reply("Your teacher approved your practise session.  Your tiger mom saw the comment, and gave you " +
							Utils.formatNumber((long) (base * 0.1)) + Emoji.VIOLINS + " in addition to the " +
							Utils.formatNumber(base) + Emoji.VIOLINS + " that you earned.");
					base = (long) (base * 1.1);
				} else if(num > 0.40) {
					e.reply("Your tiger mom approved your practise session.  She gave you " +
							Utils.formatNumber((long) (base * 0.5)) + Emoji.VIOLINS + " in addition to the " +
							Utils.formatNumber(base) + Emoji.VIOLINS + " that you earned.");
					base = (long) (base * 1.25);
				} else if(num > 0.2) {
					base = (long) (base * 1.5);
					e.reply("Brett and Eddy approved of your practise session and doubled the amount of violins you earned.  You got " +
							Utils.formatNumber(base) + Emoji.VIOLINS);
				} else if(num > 0.05) {
					base *= 2;
					e.reply("It's raining violins!  You earned " + Utils.formatNumber(base) + Emoji.VIOLINS + " you lucky dog");
				} else {
					base *= 3;
					e.reply("**Rare Drop!**\nLing Ling enjoyed your practise session but was displeased with some parts.  Nonetheless, he (she?) granted you with " +
							Utils.formatNumber(base) + Emoji.VIOLINS);
					data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 1);
				}
				data.replace("hoursPractised", (double) data.get("hoursPractised") + 1.0);
			} else {
				num = random.nextDouble();
				long income = (long) data.get("income");
				if(num > 0.75) {
					data.replace("violins", violins - income / 100);
					base = 0;
					e.reply("Oh no!  Your E String snapped while you were practising!  You had to go to the store to get it replaced, and were not able to get any practising done.  " +
							"You earned `0`" + Emoji.VIOLINS + " and had to pay " + Utils.formatNumber(income / 100) + Emoji.VIOLINS + " for a new E String.");
				} else if(num > 0.55) {
					base = (long) (base * 0.9);
					data.replace("hoursPractised", (double) data.get("hoursPractised") + 0.9);
					e.reply("Your violin randomly went out of tune while you were practising.  You had to spend 6 minutes tuning it and were only able to earn " +
							Utils.formatNumber(base) + Emoji.VIOLINS);
				} else if(num > 0.4) {
					base = (long) (base * 0.95);
					data.replace("hoursPractised", (double) data.get("hoursPractised") + 0.75);
					e.reply("You had problems with your music stand, and page turning wasn't the best this session.  You earned " +
							Utils.formatNumber(base) + Emoji.VIOLINS);
				} else if(num > 0.25) {
					base = (long) (base * 0.5);
					base -= income / 10;
					data.replace("hoursPractised", (double) data.get("hoursPractised") + 0.5);
					e.reply("You hurt your wrist while practising and only got half of the effectiveness.  You earned " +
							Utils.formatNumber(base) + Emoji.VIOLINS + " but ended up paying " +
							Utils.formatNumber(income / 10) + Emoji.VIOLINS + " in hospital fees.");
				} else if(num > 0.15) {
					base = (long) (base * 0.2);
					data.replace("hoursPractised", (double) data.get("hoursPractised") + 0.25);
					if(random.nextDouble() < 0.01) {
						e.reply("**OOF**\nYour bridge fell off 15 minutes longo your session.  You spend the next 45 minutes trying to get it back on, and you only earned " +
								Utils.formatNumber(base) + Emoji.VIOLINS);
					} else {
						e.reply("**OOF**\nYour bridge fell off 15 minutes into your session.  You spend the next 45 minutes trying to get it back on, and you only earned " +
								Utils.formatNumber(base) + Emoji.VIOLINS);
					}
				} else if(num > 0.05) {
					base = -1 * income;
					e.reply("**OOF**\nYou were caught playing Minecraft while practising.  Your tiger mom took all your earnings, in addition to another " +
							Utils.formatNumber(income) + Emoji.VIOLINS + " for being distracted.");
				} else if(num > 0.015) {
					e.reply(":regional_indicator_l:\nYour chin rest popped off your violin!  You take your violin to the luthier, " +
							"who informs you that the violin will have to stay overnight.  You are not able to practise for 12 hours.");
					time += 43200000;
					data.replace("scaleCD", time);
					data.replace("practiceCD", time);
					badEvent = true;
					base = 0;
				} else if(num > 0.005) {
					e.reply(":regional_indicator_l:\nYou decided to fake your practise session.  Ling Ling caught you in the act, and fined you " +
							Utils.formatNumber((long) (violins * 0.8)) + Emoji.VIOLINS + " but Strad was going to remove these types of fines so your violins are fine.");
					// data.replace("violins", (long) (violins * 0.2));
					badEvent = true;
					base = 0;
				} else {
					e.reply(":skull:\nYou dropped your violin.  How shameful.  All cooldowns except daily and gamble have had one day added to them, and you were fined " +
							Utils.formatNumber((long) (violins * 0.9)) + Emoji.VIOLINS + " for being careless but Strad was going to remove these types of fines so your violins are fine.");
					time += 86400000;
					// data.replace("violins", (long) (violins * 0.1));
					data.replace("scaleCD", time);
					data.replace("practiceCD", time);
					data.replace("rehearseCD", time);
					data.replace("performCD", time);
					badEvent = true;
					base = 0;
				}
			}
			Utils.calculateLoan(data, base);
			data.replace("earnings", (long) data.get("earnings") + base);
			if(!badEvent) {
				if((boolean) data.get("timeCrunch")) {
					data.replace("practiceCD", time + 2340000);
				} else {
					data.replace("practiceCD", time + 3540000);
				}
			}
			RNGesus.lootbox(e, data);
			Achievement.calculateAchievement(e, data, "hoursPractised", "40 Hours A Day");
			SaveData.saveData(e, data);
		}
	}
}