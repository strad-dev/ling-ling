package eventListeners;

import dev.*;
import economy.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.HourlyIncome;
import processes.Luthier;
import processes.StartBot;
import regular.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class CreateThreadMessage implements Runnable {
	private static GenericDiscordEvent e;

	private static String[] message;

	public static void setGenericDiscordEvent(GenericDiscordEvent e1, String[] message1) {
		e = e1;
		message = message1;
	}

	public static long CheckPermLevel(GenericDiscordEvent e) {
		if(e.getAuthor().getId().equals("619989388109152256") || e.getAuthor().getId().equals("488487157372157962")) {
			return 3;
		} else {
			JSONObject data = DatabaseManager.getDataForUser(e, "Economy Data", e.getAuthor().getId());
			if(data == null) {
				return 0;
			} else {
				return (long) data.get("perms");
			}
		}
	}

	public void run() {
		System.out.println("[DEBUG] New Thread: " + Thread.currentThread().threadId() + "\n        Command: " + Arrays.toString(message));
		if(message[0].equals("<@733409243222507670>") || message[0].equals("<@772582345944334356>")) {
			// ALL COMMANDS
			String commandName;
			try {
				commandName = message[1];
			} catch(Exception exception) {
				commandName = "";
			}

			switch(commandName) {
				case "debug" -> {
					Runtime runtime = Runtime.getRuntime();
					runtime.gc();
					e.reply("Open threads: " + Thread.activeCount() + "\n\nTotal RAM: " + runtime.totalMemory() +
							"\nMax RAM: " + runtime.maxMemory() + "\nRAM in Use: " + (runtime.totalMemory() - runtime.freeMemory()) +
							"\nFree RAM: " + runtime.freeMemory());
				}
				// NON-ECON COMMANDS
				case "help" -> {
					String page;
					try {
						page = message[2];
					} catch(Exception exception) {
						page = "";
					}
					Help.help(e, page);
				}
				case "faq" -> {
					String page;
					try {
						page = message[2];
					} catch(Exception exception) {
						page = "";
					}
					FAQ.faq(e, page);
				}
				case "website" ->
						e.reply("There is no website.  But you can enjoy a nice spreadsheet here: https://docs.google.com/spreadsheets/d/118BxHRJbCEd7aTeMgoxy7D2Nya5RaWdRLLZj0_WTvOI/edit?gid=1763214700#gid=1763214700");
				case "support" -> e.reply("Join the support server at discord.gg/gNfPwa8");
				case "vote" ->
						e.reply("Vote here to earn an extra Free Box!\n<https://top.gg/bot/733409243222507670/vote>");
				case "guide" ->
						e.reply("The Beginner Guide can be found at <https://docs.google.com/document/d/1Oo8m8XuGsIOyMzJhllUN9SpOJI8hSUeQt5RbyPY9qMI/edit?usp=sharing>, written by `bubblepotatochips`");
				case "kill" -> {
					StringBuilder target = new StringBuilder();
					String[] message = e.getMessage().getContentRaw().split(" ");
					try {
						int i;
						if(message[0].equals("!kill")) {
							i = 1;
						} else {
							i = 2;
						}
						for(; i < message.length; i++) {
							target.append(message[i]).append(" ");
						}
						target.deleteCharAt(target.length() - 1);
					} catch(Exception exception) {
						target = new StringBuilder("Nobody");
					}
					Kill.kill(e, target.toString());
				}
				case "joke" -> Joke.joke(e);
				case "poll" -> {
					e.getChannel().deleteMessageById(e.getMessage().getId()).queue();
					String message1 = e.getMessage().getContentRaw();
					StringBuilder title = new StringBuilder();
					String choices;
					int current;
					try {
						current = message1.indexOf('\"') + 1;
						while(message1.charAt(current) != '\"') {
							title.append(message1.charAt(current));
							current++;
						}
					} catch(Exception exception) {
						title = new StringBuilder("No Title");
						current = 5;
					}
					current += 2;
					try {
						choices = message1.substring(current);
					} catch(Exception exception) {
						choices = "";
					}
					Poll.poll(e, title.toString(), choices);
				}
				case "emojify" -> {
					StringBuilder message1 = new StringBuilder();
					try {
						for(int i = 2; i < message.length; i++) {
							message1.append(message[i]).append(' ');
						}
						message1.deleteCharAt(message1.length() - 1);
					} catch(Exception exception) {
						message1 = new StringBuilder();
					}
					e.getChannel().deleteMessageById(e.getMessage().getId()).queue();
					Emojify.emojify(e, String.valueOf(message1));
				}
				case "invite" -> e.reply("You can add the bot to your server using the below link:" +
						"\n<https://discord.com/api/oauth2/authorize?client_id=733409243222507670&permissions=67398720&scope=bot%20applications.commands>");
				case "staff" -> e.reply("""
						```fix
						███████╗████████╗ █████╗ ███████╗███████╗
						██╔════╝╚══██╔══╝██╔══██╗██╔════╝██╔════╝
						███████╗   ██║   ███████║█████╗  █████╗
						╚════██║   ██║   ██╔══██║██╔══╝  ██╔══╝
						███████║   ██║   ██║  ██║██║     ██║
						╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝     ╚═╝
						```
						**Developer**: `Stradivarius Violin#6156`
						**Bot Admins**: `JMusical#5262` `jacqueline#1343`
						**Bot Mods**: `Penguin Irina#6514` `akc0303#5743`
						""");
				case "rules" -> e.reply("""
						```fix
						██████╗ ██╗   ██╗██╗     ███████╗███████╗
						██╔══██╗██║   ██║██║     ██╔════╝██╔════╝
						██████╔╝██║   ██║██║     █████╗  ███████╗
						██╔══██╗██║   ██║██║     ██╔══╝  ╚════██║
						██║  ██║╚██████╔╝███████╗███████╗███████║
						╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚══════╝╚══════╝
						```
						1. Do not spam commands, excessively ping users, or send messages to trigger luthier or take any sort of action that may cause the bot to crash.
						2. Do not abuse bugs or exploits.  If a bug/exploit is found, **IMMEDIATELY** report it to `Stradivarius Violin#6156`.
						3. Do not overexcessively or inappropriately use `!checkdm` or `!kill`.  These were written to poke fun at others, not to annoy/hurt them.
						4. Do not ask mods/admins/devs for information that can easily be found by reading the patch notes, using `!help`, or using `!faq`.
						5. **All bot mods/admins reserve the right to punish users when breaking bot rules is involved.**  Attempting to stop bot mods/admins from using bot moderation commands will result in your punishment as well.
						
						**Privacy Notice** - Ling Ling **does not store any data** except for data explicitly related to the economy system, which are variables all generated by the bot itself, with the exception of IDs provided by Discord (eg. Server ID for Luthier & Prefixes, User ID for saves), which are necessary to allow the bot to distinguish between different users and servers.
						
						tl;dr: ling ling doesn't store any data except your discord user id and stuff it makes by itself so that it works properly
						""");
				case "botstats" -> {
					int serverCount = e.getJDA().getGuilds().size();
					ArrayList<Document> documents = DatabaseManager.getAllEconomyData();
					e.reply("Servers: " + serverCount + "\nUsers: " + documents.size());
				}
				case "settings" -> {
					String option;
					String newValue;
					try {
						option = message[2];
					} catch(Exception exception) {
						option = "none";
					}
					try {
						newValue = message[3];
					} catch(Exception exception) {
						newValue = "";
					}
					UserSettings.userSettings(e, option, newValue);
				}
				case "market" -> {
					String action;
					String item;
					String amount;
					long price;
					try {
						action = message[2];
					} catch(Exception exception) {
						action = "";
					}
					try {
						item = message[3];
					} catch(Exception exception) {
						item = "none";
					}
					try {
						amount = message[4];
					} catch(Exception exception) {
						amount = "1";
					}
					try {
						price = Long.parseLong(message[5]);
					} catch(Exception exception) {
						price = -1;
					}

					Market.market(e, item, action, amount, price);
				}
				case "craft" -> {
					String craftAmount;
					String name;
					try {
						name = message[2];
					} catch(Exception exception) {
						name = "";
					}
					try {
						craftAmount = message[3];
					} catch(Exception exception) {
						craftAmount = "1";
					}
					Craft.craft(e, craftAmount, name);
				}

				// ECON COMMANDS
				case "start" -> Start.start(e, e.getAuthor().getId(), false);
				case "upgrades", "up", "u", "shop" -> {
					int page;
					try {
						page = Integer.parseInt(message[2]);
					} catch(Exception exception) {
						page = -1;
					}
					Upgrades.upgrades(e, page);
				}
				case "buy" -> {
					String item;
					try {
						item = message[2];
					} catch(Exception exception) {
						item = "";
					}
					Buy.buy(e, item);
				}
				case "cooldowns", "c" -> {
					Cooldowns.cooldowns(e);
				}
				case "use" -> {
					String item;
					String amount;
					try {
						item = message[2];
					} catch(Exception exception) {
						item = "";
					}
					try {
						amount = message[3];
					} catch(Exception exception) {
						amount = "1";
					}
					Use.use(e, item, amount);
				}
				case "scales", "s" -> Scales.scales(e);
				case "resetstreak" -> ResetStreak.resetStreak(e);
				case "practice", "p" -> Practise.practise(e);
				case "rehearse", "r" -> Rehearse.rehearse(e);
				case "perform", "pf" -> Perform.perform(e);
				case "daily", "d" -> Daily.daily(e);
				case "teach", "t" -> Teach.teach(e);
				case "hourly", "h" -> HourlyIncome.hourlyIncome(e);
				case "cheat" -> {
					System.out.println(StartBot.isBeta());
					if(StartBot.isBeta()) {
						JSONObject data = LoadData.loadData(e);
						data.replace("violins", 1000000000000000L);
						data.replace("medals", 100000L);
						data.replace("voteBox", 1000L);
						data.replace("giftBox", 1000L);
						data.replace("kits", 1000L);
						data.replace("linglingBox", 1000L);
						data.replace("crazyBox", 1000L);
						data.replace("RNGesusBox", 1000L);
						SaveData.saveData(e, data);
						e.reply("Here you go!");
					} else {
						e.reply("This instance of the bot is not undergoing beta testing!  Stop trying to cheat!");
					}
				}
				case "gamble", "bet" -> {
					String game;
					String amount;
					try {
						game = message[2];
					} catch(Exception exception) {
						game = "";
					}
					try {
						amount = message[3];
					} catch(Exception exception) {
						amount = "-1";
					}
					Gamble.gamble(e, game, amount);
				}
				case "rob" -> {
					String user;
					try {
						user = message[2];
					} catch(Exception exception) {
						user = e.getAuthor().getId();
					}
					Rob.rob(e, user);
				}
				case "inventory", "inv" -> {
					int page;
					String user;
					try {
						page = Integer.parseInt(message[2]);
					} catch(Exception exception) {
						page = -1;
					}
					try {
						user = message[3];
					} catch(Exception exception) {
						user = e.getAuthor().getId();
					}
					Inventory.inventory(e, user, page);
				}
				case "profile", "balance", "bal", "b" -> {
					String user;
					try {
						user = message[2];
					} catch(Exception exception) {
						user = e.getAuthor().getId();
					}
					Balance.balance(e, user);
				}
				case "stats" -> {
					String user;
					try {
						user = message[2];
					} catch(Exception exception) {
						user = e.getAuthor().getId();
					}
					Stats.stats(e, user);
				}
				case "claim" -> Vote.vote(e);
				case "gift" -> {
					String user;
					try {
						user = message[2];
					} catch(Exception exception) {
						user = e.getAuthor().getId();
					}
					Gift.gift(e, user);
				}
				case "deposit", "dep" -> {
					String amount;
					try {
						amount = message[2];
					} catch(Exception exception) {
						amount = "";
					}
					Deposit.deposit(e, amount);
				}
				case "withdraw", "with" -> {
					String amount;
					try {
						amount = message[2];
					} catch(Exception exception) {
						amount = "";
					}
					Withdraw.withdraw(e, amount);
				}
				case "loan" -> {
					String amount;
					try {
						amount = message[2];
					} catch(Exception exception) {
						amount = "";
					}
					Loan.loan(e, amount);
				}
				case "payloan" -> {
					String amount;
					try {
						amount = message[2];
					} catch(Exception exception) {
						amount = "";
					}
					PayLoan.payLoan(e, amount);
				}
				case "answer" -> {
					StringBuilder answer = new StringBuilder();
					try {
						for(int i = 2; i < message.length; i++) {
							answer.append(message[i]).append(" ");
						}
						answer.deleteCharAt(answer.length() - 1);
					} catch(Exception exception) {
						answer = new StringBuilder("none");
					}
					Luthier.luthier(e, DatabaseManager.getDataByGuild(e, "Luthier Data"), answer.toString());
				}
				case "leaderboard", "lb" -> {
					JSONObject data = LoadData.loadData(e);
					try {
						switch(message[2]) {
							case "violins" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Richest Users", "violins", (long) data.get("violins"), (String) data.get("color"));
							case "streak" ->
									Leaderboard.leaderboard(e, ":calendar:", "Longest Daily Streaks", "streak", (long) data.get("streak"), (String) data.get("color"));
							case "medals" ->
									Leaderboard.leaderboard(e, Emoji.MEDALS, "Most Worthy Users", "medals", (long) data.get("medals"), (String) data.get("color"));
							case "income" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS + "/hour", "Highest Hourly Incomes", "income", (long) data.get("income"), (String) data.get("color"));
							case "winnings" ->
									Leaderboard.leaderboard(e, ":moneybag:", "Best Gamblers", "winnings", (long) data.get("winnings"), (String) data.get("color"));
							case "million" ->
									Leaderboard.leaderboard(e, ":tickets:", "Luckiest Users", "millions", (long) data.get("millions"), (String) data.get("color"));
							case "rob" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Most Heartless Users", "robbed", (long) data.get("robbed"), (String) data.get("color"));
							case "scales" ->
									Leaderboard.leaderboard(e, ":scales:", "Most Scales Played", "scalesPlayed", (long) data.get("scalesPlayed"), (String) data.get("color"));
							case "hours" ->
									Leaderboard.leaderboard(e, ":clock2:", "Most Hours Practised", "hoursPractised", (long) ((double) data.get("hoursPractised")), (String) data.get("color"));
							case "rehearsals" ->
									Leaderboard.leaderboard(e, ":musical_score:", "Most Rehearsals Attended", "rehearsals", (long) data.get("rehearsals"), (String) data.get("color"));
							case "performances" ->
									Leaderboard.leaderboard(e, ":microphone:", "Most Performances", "performances", (long) data.get("performances"), (String) data.get("color"));
							case "earnings" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Most Hardworking Users", "earnings", (long) data.get("earnings"), (String) data.get("color"));
							case "teach" ->
									Leaderboard.leaderboard(e, ":teacher:", "Most Influential Users", "hoursTaught", (long) ((double) data.get("hoursTaught")), (String) data.get("color"));
							case "luthier" ->
									Leaderboard.leaderboard(e, ":question:", "Best Unscramblers", "luthiers", (long) data.get("luthiers"), (String) data.get("color"));
							case "gift" ->
									Leaderboard.leaderboard(e, Emoji.GIFT_BOX, "Most Generous Users", "giftsGiven", (long) data.get("giftsGiven"), (String) data.get("color"));
							case "free" ->
									Leaderboard.leaderboard(e, ":money_mouth:", "Most Stingy Users", "votes", (long) data.get("votes"), (String) data.get("color"));
							case "rng" ->
									Leaderboard.leaderboard(e, ":slot_machine:", "Truly Luckiest Users", "RNGesusWeight", (long) data.get("RNGesusWeight"), (String) data.get("color"));
							case "magicfind" ->
									Leaderboard.leaderboard(e, ":star:", "Statistically Luckiest Users", "magicFind", (long) data.get("magicFind"), (String) data.get("color"));
							case "moneyearned" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Best Entrepreneurs", "moneyEarned", (long) data.get("moneyEarned"), (String) data.get("color"));
							case "moneyspent" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Biggest Spenders", "moneySpent", (long) data.get("moneySpent"), (String) data.get("color"));
							case "penalty" ->
									Leaderboard.leaderboard(e, Emoji.VIOLINS, "Baldest Bozos", "penaltiesIncurred", (long) data.get("penaltiesIncurred"), (String) data.get("color"));
							case "scalestreak" ->
									Leaderboard.leaderboard(e, ":scales:", "Most in Need of Touching Grass", "scaleStreakRecord", (long) data.get("scaleStreakRecord"), (String) data.get("color"));
							default -> e.reply("""
									You must provide a valid leaderboard type.  Valid types...

									`violins`: Richest Users
									`income`: Highest Hourly Incomes
									`streak`: Longest Daily Streaks
									`medals`: Users with Most Ling Ling Medals
									`winnings`: Users with Highest Net Gamble Winnings
									`million`: Users with Most Million Violin Tickets
									`rob`: Users with Highest Violins Robbed
									`scales`: Users with Most Scales Played
									`hours`: Users with Most Hours Practised
									`rehearsals`: Users with Most Rehearsals Attended
									`performances`: Users with Most Performances
									`teach`: Users with the Most Hours Taught
									`earnings`: Users who Earned the Most Violins
									`luthier`: Users with Most Luthier Unscrambles
									`gift`: Users that have given the most Gifts
									`free`: Users that have claimed the most Free Boxes
									`rng`: Users with highest RNGesus Weight
									`magicfind`: Users with the most Magic Find
									`moneyearned`: Users who have earned the most money from Market
									`moneyspent`: Users who have spent the most money on Market
									`scalestreak`: Users who have had the longest Scale Streaks""");
						}
					} catch(Exception exception) {
						e.reply("""
								**__Leaderboard Types__**

								`violins`: Richest Users
								`income`: Highest Hourly Incomes
								`streak`: Longest Daily Streaks
								`medals`: Users with Most Ling Ling Medals
								`winnings`: Users with Highest Net Gamble Winnings
								`million`: Users with Most Million Violin Tickets
								`rob`: Users with Highest Violins Robbed
								`scales`: Users with Most Scales Played
								`hours`: Users with Most Hours Practised
								`rehearsals`: Users with Most Rehearsals Attended
								`performances`: Users with Most Performances
								`teach`: Users with the Most Hours Taught
								`earnings`: Users who Earned the Most Violins
								`luthier`: Users with Most Luthier Unscrambles
								`gift`: Users that have given the most Gifts
								`free`: Users that have claimed the most Free Boxes
								`rng`: Users with highest RNGesus Weight
								`magicfind`: Users with the most Magic Find
								`moneyearned`: Users who have earned the most money from Market
								`moneyspent`: Users who have spent the most money on Market
								`scalestreak`: Users who have had the longest Scale Streaks""");
					}
				}
				case "achievements", "a" -> {
					int page = -1;
					try {
						page = Integer.parseInt(message[2]);
					} catch(Exception exception) {
						try {
							if(message[2].equals("c")) {
								page = 2147483647;
							}
						} catch(Exception exception1) {
							// nothing
						}
					}
					Achievement.achievement(e, page);
				}

				// DEV COMMANDS
				case "give" -> {
					if(CheckPermLevel(e) >= 1) {
						String receiver;
						long add;
						String item;
						try {
							receiver = message[2];
						} catch(Exception exception) {
							receiver = "";
						}
						try {
							add = Long.parseLong(message[3]);
						} catch(NullPointerException exception) {
							add = -1;
						} catch(Exception exception) {
							add = -2;
						}
						try {
							if(e.getMessage().getContentRaw().split(" ")[0].equals("!give")) {
								item = e.getMessage().getContentRaw().split(" ")[3];
							} else {
								item = e.getMessage().getContentRaw().split(" ")[4];
							}
						} catch(Exception exception) {
							item = "";
						}
						Give.give(e, receiver, add, item);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "warn" -> {
					if(CheckPermLevel(e) >= 1) {
						String idToModerate;
						String reason;
						try {
							idToModerate = message[2];
						} catch(Exception exception) {
							idToModerate = "";
						}
						try {
							reason = message[3];
						} catch(Exception exception) {
							reason = "None";
						}
						Warn.warn(e, idToModerate, reason);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "resetsave" -> {
					if(CheckPermLevel(e) >= 1) {
						String idToModerate;
						String reason;
						try {
							idToModerate = message[2];
						} catch(Exception exception) {
							idToModerate = "";
						}
						try {
							reason = message[3];
						} catch(Exception exception) {
							reason = "None";
						}
						ResetSave.resetSave(e, idToModerate, reason);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "ban" -> {
					if(CheckPermLevel(e) >= 2) {
						String idToModerate;
						String reason;
						try {
							idToModerate = message[2];
						} catch(Exception exception) {
							idToModerate = "";
						}
						try {
							reason = message[3];
						} catch(Exception exception) {
							reason = "None";
						}
						Ban.ban(e, idToModerate, reason);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "unban" -> {
					if(CheckPermLevel(e) >= 2) {
						String idToModerate;
						String reason;
						Boolean reset;
						try {
							idToModerate = message[2];
						} catch(Exception exception) {
							idToModerate = "";
						}
						try {
							reason = message[3];
						} catch(Exception exception) {
							reason = "None";
						}
						try {
							reset = Boolean.valueOf(message[4]);
						} catch(Exception exception) {
							reset = null;
						}
						Unban.unban(e, idToModerate, reason, reset);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "luthier" -> {
					if(CheckPermLevel(e) >= 2) {
						String actionType;
						String editOption;
						StringBuilder newValue = new StringBuilder();
						try {
							actionType = message[2];
						} catch(Exception exception) {
							actionType = "";
						}
						try {
							editOption = message[3];
						} catch(Exception exception) {
							editOption = "";
						}
						try {
							for(int i = 4; i < message.length; i++) {
								newValue.append(message[i]).append(" ");
							}
							newValue.deleteCharAt(newValue.length() - 1);
						} catch(Exception exception) {
							newValue = new StringBuilder();
						}
						AdminLuthier.adminLuthier(e, actionType, editOption, newValue.toString());
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "resetincomes" -> {
					if(CheckPermLevel(e) >= 2) {
						e.reply(ResetIncomes.resetIncomes());
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "updateluthierchance" -> {
					if(CheckPermLevel(e) >= 2) {
						UpdateLuthierChance.updateLuthierChance(e, true);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "updateusers" -> {
					if(CheckPermLevel(e) == 3) {
						String dataType;
						String name;
						String value;
						try {
							dataType = message[2];
						} catch(Exception exception) {
							dataType = "";
						}
						try {
							name = e.getMessage().getContentRaw().split(" ")[2];
						} catch(Exception exception) {
							name = "";
						}
						try {
							value = message[4];
						} catch(Exception exception) {
							value = "";
						}
						UpdateUsers.updateUsers(e, dataType, name, value);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "forcestop" -> {
					e.getMessage().delete().queue();
					if(CheckPermLevel(e) == 3 && e.getMessage().getContentRaw().split(" ")[2].equals("@#$%FUCK")) {
						e.reply("Forcing bot to stop...");
						System.exit(0);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command, or you entered the wrong Password.");
					}
				}
				case "updateroles" -> {
					if(CheckPermLevel(e) == 3) {
						UpdateRoles.updateRoles(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.");
					}
				}
				case "setpermlevel" -> {
					if(CheckPermLevel(e) == 3) {
						String target;
						int newRank;
						try {
							target = message[2];
						} catch(Exception exception) {
							target = "";
						}
						try {
							newRank = Integer.parseInt(message[3]);
						} catch(Exception exception) {
							newRank = -1;
						}
						SetPermLevel.setPermLevel(e, target, newRank);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not nave permission to run this command.");
					}
				}
				case "globalstats" -> {
					if(CheckPermLevel(e) == 3) {
						GlobalStats.gobalStats(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not nave permission to run this command.");
					}
				}
				case "resetdaily" -> {
					if(CheckPermLevel(e) == 3) {
						MoreDailyTime.moreDailyTime(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not nave permission to run this command.");
					}
				}
				case "sudo" -> {
					if(CheckPermLevel(e) == 3) {
						try {
							e.setAuthor(e.getJDA().retrieveUserById(message[2]).complete());
						} catch(Exception exception) {
							e.reply("Invalid user provided!");
							return;
						}
						for(int i = 3; i < message.length; i ++) {
							message[i - 2] = message[i];
						}
						message = Arrays.copyOfRange(message, 0, message.length - 2);
						run();
						return;
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not nave permission to run this command.");
					}
				}
				case "custom" -> //noinspection RedundantLabeledSwitchRuleCodeBlock
				{
					/*File[] files = new File("Ling Ling Bot Data\\Economy Data").listFiles();
					assert files != null;
					for(File file : files) {
						JSONObject data;
						try(FileReader reader = new FileReader(file)) {
							data = (JSONObject) parser.parse(reader);
						} catch(Exception exception) {
							continue;
						}
						data.put("itemsSold", 0L);
						data.put("itemsBought", 0L);
						data.put("moneySpent", 0L);
						data.put("moneyEarned", 0L);
						data.put("taxPaid", 0L);
						try(FileWriter writer = new FileWriter(file)) {
							writer.write(data.toJSONString());
						} catch(Exception exception) {
							// nothing here lol
						}
					}
					/*List<Guild> list = e.getJDA().getGuilds();
					for(Guild g : list) {
						List<TextChannel> channels = g.getTextChannels();
						for(TextChannel channel : channels) {
							try {
								channel.sendMessage("""
										""");
								break;
							} catch(Exception exception) {
								exception.printStackTrace();
								//oof
							}
						}
					}*/
					e.reply("No Update Here!");
				}
			}
		}
		System.out.println("        Thread " + Thread.currentThread().threadId() + " Finished.");
	}
}

public class OldReceiver extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent e) {
		GenericDiscordEvent e1 = new GenericDiscordEvent(e);

		// Apply Vote Rewards
		if(e.getChannel().getId().equals("863135059712409632") && e.getJDA().getSelfUser().getId().equals("733409243222507670") && e.getMessage().getContentRaw().contains("<@733409243222507670>")) {
			String[] message = e.getMessage().getContentRaw().toLowerCase().split(" ");
			String id = message[1];
			JSONObject data = DatabaseManager.getDataForUser(e1, "Economy Data", id);
			String messageToSend = RNGesus.voteRewards(e1, data);
			if(data != null) {
				DatabaseManager.saveDataForUser(e1, "Economy Data", id, data);
			}
			Objects.requireNonNull(e.getJDA().getUserById(id)).openPrivateChannel().complete().sendMessage(messageToSend).queue();
			return;
		}

		// HourlyIncome.checkHourly(e1);

		//LUTHIER
		try {
			Luthier.luthier(e1, DatabaseManager.getDataByGuild(e1, "Luthier Data"), "");
		} catch(Exception exception) {
			//nothing here lol
		}

		// IF NOT BETA TESTING AND IN BETA CHANNEL, IGNORE
		if(!e.getAuthor().isBot() && !e.getMessage().getContentRaw().isEmpty()) {
			if(StartBot.isBeta() || !e.getChannel().getId().equals("867617298918670366")) {
				if(e1.getMessage().getContentRaw().toLowerCase().contains("bad bot")) {
					e1.reply("sowwy strad :(");
				} else if(e1.getMessage().getContentRaw().toLowerCase().contains("good bot")) {
					e1.reply("senkyoo strad :)");
				} else if(e1.getMessage().getContentRaw().toLowerCase().contains("right bot?")) {
					e1.reply("yes master");
				}

				String[] message = e.getMessage().getContentRaw().toLowerCase().split(" ");
				try {
					CreateThreadMessage.setGenericDiscordEvent(e1, message);
					Thread object = new Thread(new CreateThreadMessage());
					object.start();
				} catch(StringIndexOutOfBoundsException exception) {
					// do nothing
				}
			}
		}
	}
}