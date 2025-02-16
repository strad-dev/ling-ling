package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.Utils;

import java.awt.*;
import java.util.Random;

public class Gamble {
	public static void gamble(GenericDiscordEvent e, String game, String temp) {
		JSONObject data = LoadData.loadData(e);
		long bet;
		long time = System.currentTimeMillis();
		long violins = (long) data.get("violins");
		long gambleL = (long) data.get("luck");
		long income = Math.min((long) data.get("income"), 100000);
		long higherMulti = (long) data.get("moreMulti");
		long max = income * (10 + higherMulti);
		long winnings = (long) data.get("winnings");
		Random random = new Random();
		try {
			if(temp.equals("max")) {
				bet = Math.min(violins, max);
			} else {
				try {
					bet = Long.parseLong(temp);
				} catch(Exception exception) {
					e.reply("You can't bet something that isn't a number, how dumb do you think I am?");
					return;
				}
			}
		} catch(Exception exception) {
			e.reply("You must bet something, I'm not giving away free violins.");
			return;
		}
		long cooldown = (long) data.get("betCD");
		if(time < cooldown) {
			long milliseconds = cooldown - time;
			long seconds = milliseconds / 1000;
			milliseconds -= seconds * 1000;
			e.reply("Don't bet your violins like Paganini, wait " + seconds + " seconds " + milliseconds + " milliseconds!");
		} else if(bet > violins) {
			e.reply("You can't bet more than you have, don't try to outsmart me.");
		} else if(bet < 0) {
			e.reply("You can't bet a negative number, don't try breaking me.");
		} else if(max < 4000) {
			e.reply("You cannot gamble yet!  You must have at least `400`" + Emoji.VIOLINS + "/hour income to be able to gamble.");
		} else if(bet < 4000) {
			e.reply("If you're going to bet less than `4000`" + Emoji.VIOLINS + ", go away and stop wasting my time.");
		} else if(bet > max) {
			e.reply("You cannot bet more than `" + max + "`" + Emoji.VIOLINS + ".  To raise this cap, upgrade your hourly income!");
		} else {
			try {
				double multi = 0.005 * gambleL;
				data.replace("betCD", time + 9500);
				switch(game) {
					case "rng" -> {
						double chance = random.nextDouble();
						if(chance > 0.5) {
							violins -= bet;
							winnings -= bet;
							if((boolean) data.get("extraInfo")) {
								e.reply("You lost " + Utils.formatNumber(bet) + Emoji.VIOLINS +
										"\n*The generator rolled `" + chance + "`, you need less than `0.5` to win.*" +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							} else {
								e.reply("You lost " + Utils.formatNumber(bet) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							}
						} else {
							violins += (long) (bet * (1 + multi));
							winnings += (long) (bet * (1 + multi));
							if((boolean) data.get("extraInfo")) {
								e.reply("You won " + Utils.formatNumber(bet) + Emoji.VIOLINS +
										"\nYour `" + multi * 100 + "%` multiplier earned you an extra " + Utils.formatNumber((long) (bet * multi)) + Emoji.VIOLINS +
										"\n*The generator rolled `" + chance + "`.*\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							} else {
								e.reply("You won " + Utils.formatNumber(bet) + Emoji.VIOLINS +
										"\nYour `" + multi * 100 + "%` multiplier earned you an extra " + Utils.formatNumber((long) (bet * multi)) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							}
						}
						data.replace("violins", violins);
						data.replace("winnings", winnings);
						Achievement.calculateAchievement(e, data, "earnings", "Gambling Addict");
						SaveData.saveData(e, data);
					}
					case "slots" -> {
						String[] emojis = new String[3];
						long payout = bet;
						int[] slots = {random.nextInt(6), random.nextInt(6), random.nextInt(6)};
						for(int i = 0; i < slots.length; i++) {
							switch(slots[i]) {
								case 0 -> emojis[i] = ":trumpet:";
								case 1 -> emojis[i] = "<:violin:1019787510295048272>";
								case 2 -> emojis[i] = "<:lingling40hrs:688449820410773532>";
								case 3 -> emojis[i] = "<:twoset:852784233819013150>";
								case 4 -> emojis[i] = "<:linglingclock:747499551451250730>";
								case 5 -> emojis[i] = "<a:StradSpam:772894512154279945>";
							}
						}
						if(slots[0] == slots[1] && slots[1] == slots[2]) {
							switch(slots[0]) {
								case 0 -> payout *= (long) 2.5;
								case 1 -> payout *= 5;
								case 2 -> payout *= 10;
								case 3 -> payout *= 15;
								case 4 -> payout *= 25;
								case 5 -> payout *= 40;
							}
						} else if(slots[0] != slots[1] && slots[1] != slots[2] && slots[2] != slots[0]) {
							payout = -1;
						}
						EmbedBuilder builder = new EmbedBuilder()
								.setColor(Color.decode((String) data.get("color")))
								.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
								.setTitle("__**Slots for " + data.get("discordName") + "**__");
						String name = ":arrow_right: " + emojis[0] + " " + emojis[1] + " " + emojis[2] + " :arrow_left:\n_ _";
						if(payout != -1) {
							violins += (long) (payout * (1 + multi));
							winnings += (long) (payout * (1 + multi));
							builder.addField(name, ":white_check_mark: You **win**!  Payout: " + Utils.formatNumber(payout) + Emoji.VIOLINS +
									"\nYour `" + multi * 100 + "%` multiplier earned you an extra " + Utils.formatNumber((long) (payout * multi)) + Emoji.VIOLINS +
									"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS, false);

						} else {
							violins -= bet;
							winnings -= bet;
							builder.addField(name, ":x: You **lose**!  You lost " + Utils.formatNumber(bet) + Emoji.VIOLINS +
									"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS, false);
						}
						e.replyEmbeds(builder.build());
						data.replace("violins", violins);
						data.replace("winnings", winnings);
						Achievement.calculateAchievement(e, data, "earnings", "Gambling Addict");
						SaveData.saveData(e, data);
					}
					case "scratch" -> {
						long[] payouts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
						long numTickets = bet / 100;
						long payout = 0;
						boolean hasMillion = false;
						for(long i = 0; i < numTickets; i++) {
							double chance = random.nextDouble();
							if(chance > 0.5) { //50%
								payout -= 5;
								payouts[0]++;
							} else if(chance > 0.25) { //25%
								payouts[1]++;
							} else if(chance > 0.1) { //15%
								payout += 2;
								payouts[2]++;
							} else if(chance > 0.05) { //5%
								payout += 5;
								payouts[3]++;
							} else if(chance > 0.02) { //3%
								payout += 10;
								payouts[4]++;
							} else if(chance > 0.01) { //1%
								payout += 25;
								payouts[5]++;
							} else if(chance > 0.005) { //0.5%
								payout += 50;
								payouts[6]++;
							} else if(chance > 0.002) { //0.3%
								payout += 100;
								payouts[7]++;
							} else if(chance > 0.001) { //0.1%
								payout += 200;
								payouts[8]++;
							} else if(chance > 0.000001) { //0.1%
								payout += 500;
								payouts[9]++;
							} else { // 1 in a million
								if(!hasMillion) {
									payout += 1000000;
									e.reply("**Very Rare Drop!**\nYou hit the 1 000 000" + Emoji.VIOLINS + " jackpot!");
									data.replace("RNGesusWeight", (long) data.get("RNGesusWeight") + 3);
									data.replace("millions", (long) data.get("millions") + 1);
									hasMillion = true;
								}
							}
						}
						if(payout > 0) {
							violins += (long) (payout * (1 + multi));
							winnings += (long) (payout * (1 + multi));
							if((boolean) data.get("extraInfo")) {
								e.reply("You scratched " + Utils.formatNumber(numTickets) + " tickets and gained " + Utils.formatNumber(payout) + Emoji.VIOLINS +
										"\nYour `" + multi * 100 + "%` multiplier earned you an extra " + Utils.formatNumber((long) (payout * multi)) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS +
										"\n\n**__Ticket Breakdown__**\nLose 5" + Emoji.VIOLINS + ": `" +
										payouts[0] + "`\nNo Prize: `" +
										payouts[1] + "`\nGain 2" + Emoji.VIOLINS + ": `" +
										payouts[2] + "`\nGain 5" + Emoji.VIOLINS + ": `" +
										payouts[3] + "`\nGain 10" + Emoji.VIOLINS + ": `" +
										payouts[4] + "`\nGain 25" + Emoji.VIOLINS + ": `" +
										payouts[5] + "`\nGain 50" + Emoji.VIOLINS + ": `" +
										payouts[6] + "`\nGain 100" + Emoji.VIOLINS + ": `" +
										payouts[7] + "`\nGain 200" + Emoji.VIOLINS + ": `" +
										payouts[8] + "`\nGain 500" + Emoji.VIOLINS + ": `" +
										payouts[9] + "`");
							} else {
								e.reply("You scratched " + Utils.formatNumber(numTickets) + " tickets and gained " + Utils.formatNumber(payout) + Emoji.VIOLINS +
										"\nYour `" + multi * 100 + "%` multiplier earned you an extra " + Utils.formatNumber((long) (payout * multi)) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							}
						} else {
							violins += payout;
							winnings += payout;
							if((boolean) data.get("extraInfo")) {
								e.reply("You scratched " + Utils.formatNumber(numTickets) + " tickets and lost " + Utils.formatNumber(payout * -1) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS +
										"\n\n**__Ticket Breakdown__**\nLose 5" + Emoji.VIOLINS + ": `" +
										payouts[0] + "`\nNo Prize: `" +
										payouts[1] + "`\nGain 2" + Emoji.VIOLINS + ": `" +
										payouts[2] + "`\nGain 5" + Emoji.VIOLINS + ": `" +
										payouts[3] + "`\nGain 10" + Emoji.VIOLINS + ": `" +
										payouts[4] + "`\nGain 25" + Emoji.VIOLINS + ": `" +
										payouts[5] + "`\nGain 50" + Emoji.VIOLINS + ": `" +
										payouts[6] + "`\nGain 100" + Emoji.VIOLINS + ": `" +
										payouts[7] + "`\nGain 200" + Emoji.VIOLINS + ": `" +
										payouts[8] + "`\nGain 500" + Emoji.VIOLINS + ": `" +
										payouts[9] + "`");
							} else {
								e.reply("You scratched " + Utils.formatNumber(numTickets) + " tickets and lost " + Utils.formatNumber(payout * -1) + Emoji.VIOLINS +
										"\nYou now have " + Utils.formatNumber(violins) + Emoji.VIOLINS);
							}
						}
						data.replace("violins", violins);
						data.replace("winnings", winnings);
						Achievement.calculateAchievement(e, data, "earnings", "Gambling Addict");
						SaveData.saveData(e, data);
					}
				}
			} catch(Exception exception) {
				e.reply("You must choose one of the three gambling options.");
			}
		}
	}
}