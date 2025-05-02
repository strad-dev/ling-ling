package eventListeners;

import dev.*;
import economy.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.HourlyIncome;
import processes.Luthier;
import processes.Utils;
import regular.*;

import java.util.ArrayList;
import java.util.Objects;

import static processes.Utils.checkPermLevel;

class CreateThreadSlash implements Runnable {
	private static GenericDiscordEvent e;
	private static SlashCommandInteractionEvent e1;

	public static void setGenericDiscordEvent(GenericDiscordEvent e2) {
		e = e2;
	}

	public static void setSlashEvent(SlashCommandInteractionEvent e2) {
		e1 = e2;
	}

	@Override
	public void run() {
		System.out.println("[DEBUG] New Thread: " + Thread.currentThread().threadId() + "\n        Command: " + e1.getName());
		// HourlyIncome.checkHourly(e);

		//LUTHIER
		try {
			Luthier.luthier(e, DatabaseManager.getDataByGuild(e, "Luthier Data"), "");
		} catch(Exception exception) {
			//nothing here lol
		}

		// ALL COMMANDS
		String commandName = e1.getName();
		switch(commandName) {
			// NON-ECON COMMANDS
			case "help" -> {
				String page;
				try {
					page = Objects.requireNonNull(e1.getOption("page")).getAsString();
				} catch(Exception exception) {
					page = "";
				}
				Help.help(e, page);
			}
			case "faq" -> {
				String page;
				try {
					page = Objects.requireNonNull(e1.getOption("page")).getAsString();
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
				String target;
				try {
					target = Objects.requireNonNull(e1.getOption("target")).getAsString();
				} catch(Exception exception) {
					target = "Nobody";
				}
				Kill.kill(e, target);
			}
			case "joke" -> Joke.joke(e);
			case "poll" -> {
				String title;
				String choices;
				try {
					title = Objects.requireNonNull(e1.getOption("title")).getAsString();
				} catch(Exception exception) {
					title = "No Title";
				}
				try {
					choices = Objects.requireNonNull(e1.getOption("choices")).getAsString();
				} catch(Exception exception) {
					choices = "";
				}
				Poll.poll(e, title, choices);
			}
			case "emojify" -> {
				String message;
				try {
					message = Objects.requireNonNull(e1.getOption("message")).getAsString();
				} catch(Exception exception) {
					message = "";
				}
				Emojify.emojify(e, message);
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
					option = Objects.requireNonNull(e1.getOption("option")).getAsString();
				} catch(Exception exception) {
					option = "none";
				}
				try {
					newValue = Objects.requireNonNull(e1.getOption("newvalue")).getAsString();
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
					action = Objects.requireNonNull(e1.getOption("action")).getAsString();
				} catch(Exception exception) {
					action = "";
				}
				try {
					item = Objects.requireNonNull(e1.getOption("item")).getAsString();
				} catch(Exception exception) {
					item = "none";
				}
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(Exception exception) {
					amount = "1";
				}
				try {
					price = Objects.requireNonNull(e1.getOption("price")).getAsLong();
					if(price > 2147483647) {
						price = 2147483647;
					}
				} catch(Exception exception) {
					price = -1;
				}

				Market.market(e, item, action, amount, price);
			}
			case "craft" -> {
				String craftAmount;
				String name;
				try {
					name = Objects.requireNonNull(e1.getOption("item")).getAsString();
				} catch(Exception exception) {
					name = "";
				}
				try {
					craftAmount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
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
					page = Objects.requireNonNull(e1.getOption("page")).getAsInt();
				} catch(Exception exception) {
					page = -1;
				}
				Upgrades.upgrades(e, page);
			}
			case "buy" -> {
				String item;
				try {
					item = Objects.requireNonNull(e1.getOption("item")).getAsString();
				} catch(Exception exception) {
					item = "";
				}
				Buy.buy(e, item);
			}
			case "cooldowns", "c" -> Cooldowns.cooldowns(e);
			case "use" -> {
				String item;
				String amount;
				try {
					item = Objects.requireNonNull(e1.getOption("item")).getAsString();
				} catch(NullPointerException exception) {
					item = "";
				}
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(NullPointerException exception) {
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
			case "rob" -> {
				String user;
				try {
					user = Objects.requireNonNull(e1.getOption("target")).getAsString();
				} catch(NullPointerException exception) {
					user = e.getAuthor().getId();
				}
				Rob.rob(e, user);
			}
			case "inventory", "inv" -> {
				int page;
				String user;
				try {
					page = Objects.requireNonNull(e1.getOption("page")).getAsInt();
				} catch(NullPointerException exception) {
					page = -1;
				}
				try {
					user = Objects.requireNonNull(e1.getOption("otheruser")).getAsString();
				} catch(NullPointerException exception) {
					user = e.getAuthor().getId();
				}
				Inventory.inventory(e, user, page);
			}
			case "profile", "balance", "bal", "b" -> {
				String user;
				try {
					user = Objects.requireNonNull(e1.getOption("otheruser")).getAsString();
				} catch(NullPointerException exception) {
					user = e.getAuthor().getId();
				}
				Balance.balance(e, user);
			}
			case "stats" -> {
				String user;
				try {
					user = Objects.requireNonNull(e1.getOption("otheruser")).getAsString();
				} catch(NullPointerException exception) {
					user = e.getAuthor().getId();
				}
				Stats.stats(e, user);
			}
			case "claim" -> Vote.vote(e);
			case "gift" -> {
				String user;
				try {
					user = Objects.requireNonNull(e1.getOption("otheruser")).getAsString();
				} catch(NullPointerException exception) {
					user = e.getAuthor().getId();
				}
				Gift.gift(e, user);
			}
			case "deposit", "dep" -> {
				String amount;
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(Exception exception) {
					amount = "";
				}
				Deposit.deposit(e, amount);
			}
			case "withdraw", "with" -> {
				String amount;
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(Exception exception) {
					amount = "";
				}
				Withdraw.withdraw(e, amount);
			}
			case "loan" -> {
				String amount;
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(Exception exception) {
					amount = "";
				}
				Loan.loan(e, amount);
			}
			case "payloan" -> {
				String amount;
				try {
					amount = Objects.requireNonNull(e1.getOption("amount")).getAsString();
				} catch(Exception exception) {
					amount = "";
				}
				PayLoan.payLoan(e, amount);
			}
			case "answer" -> {
				String answer;
				try {
					answer = Objects.requireNonNull(e1.getOption("guess")).getAsString();
				} catch(Exception exception) {
					answer = "none";
				}
				Luthier.luthier(e, DatabaseManager.getDataByGuild(e, "Luthier Data"), answer);
			}
			case "leaderboard", "lb" -> {
				JSONObject data = LoadData.loadData(e);
				try {
					switch(Objects.requireNonNull(e1.getOption("leaderboard")).getAsString()) {
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
						case "scalestreak" ->
								Leaderboard.leaderboard(e, ":scales:", "Most In Need of Touching Grass", "scaleStreakRecord", (long) data.get("scaleStreakRecord"), (String) data.get("color"));
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
			case "achievement", "a" -> {
				int page;
				OptionMapping temp = e1.getOption("page");
				if(temp == null) {
					page = -1;
				} else {
					try {
						page = temp.getAsInt();
					} catch(Exception exception) {
						if(temp.getAsString().equals("c")) {
							page = 2147483647;
						} else {
							page = -1;
						}
					}
				}
				Achievement.achievement(e, page);
			}

			// DEV COMMANDS
			case "give" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 1) {
					String receiver;
					long add;
					String item;
					try {
						receiver = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						receiver = "";
					}
					try {
						add = Long.parseLong(Objects.requireNonNull(e1.getOption("amount")).getAsString());
					} catch(NullPointerException exception) {
						add = -1;
					} catch(Exception exception) {
						add = -2;
					}
					try {
						item = Objects.requireNonNull(e1.getOption("item")).getAsString();
					} catch(Exception exception) {
						item = "";
					}
					Give.give(e, receiver, add, item);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "warn" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 1) {
					String idToModerate;
					String reason;
					try {
						idToModerate = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						idToModerate = "";
					}
					try {
						reason = Objects.requireNonNull(e1.getOption("reason")).getAsString();
					} catch(Exception exception) {
						reason = "None";
					}
					Warn.warn(e, idToModerate, reason);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "resetsave" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 1) {
					String idToModerate;
					String reason;
					try {
						idToModerate = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						idToModerate = "";
					}
					try {
						reason = Objects.requireNonNull(e1.getOption("reason")).getAsString();
					} catch(Exception exception) {
						reason = "None";
					}
					ResetSave.resetSave(e, idToModerate, reason);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "ban" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 2) {
					String idToModerate;
					String reason;
					try {
						idToModerate = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						idToModerate = "";
					}
					try {
						reason = Objects.requireNonNull(e1.getOption("reason")).getAsString();
					} catch(Exception exception) {
						reason = "None";
					}
					Ban.ban(e, idToModerate, reason);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "unban" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 2) {
					String idToModerate;
					String reason;
					Boolean reset;
					try {
						idToModerate = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						idToModerate = "";
					}
					try {
						reason = Objects.requireNonNull(e1.getOption("reason")).getAsString();
					} catch(Exception exception) {
						reason = "None";
					}
					try {
						reset = Objects.requireNonNull(e1.getOption("resetsave")).getAsBoolean();
					} catch(Exception exception) {
						reset = null;
					}
					Unban.unban(e, idToModerate, reason, reset);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "luthier" -> {
				String actionType;
				String editOption;
				String newValue;
				try {
					actionType = Objects.requireNonNull(e1.getOption("actiontype")).getAsString();
				} catch(Exception exception) {
					actionType = "stats";
				}
				try {
					editOption = Objects.requireNonNull(e1.getOption("editoption")).getAsString();
				} catch(Exception exception) {
					editOption = "";
				}
				try {
					newValue = Objects.requireNonNull(e1.getOption("amountorvalue")).getAsString();
				} catch(Exception exception) {
					newValue = "";
				}
				LuthierConfig.luthierConfig(e, actionType, editOption, newValue);
			}
			case "resetincomes" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 2) {
					e.reply(ResetIncomes.resetIncomes());
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "updateluthierchance" -> {
				if(checkPermLevel(e.getAuthor().getId()) >= 2) {
					UpdateLuthierChance.updateLuthierChance(e, true);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "updateusers" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3) {
					String dataType;
					String name;
					String value;
					try {
						dataType = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						dataType = "";
					}
					try {
						name = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						name = "";
					}
					try {
						value = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						value = "";
					}
					UpdateUsers.updateUsers(e, dataType, name, value);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "forcestop" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3 && Objects.requireNonNull(e1.getOption("PASSWORD")).getAsString().equals("password")) {
					e.reply("Forcing bot to stop...");
					System.exit(0);
				} else {
					e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command, or you entered the wrong Password.");
				}
			}
			case "updateroles" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3) {
					UpdateRoles.updateRoles(e);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "setpermlevel" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3) {
					String target;
					int newRank;
					try {
						target = Objects.requireNonNull(e1.getOption("user")).getAsString();
					} catch(Exception exception) {
						target = "";
					}
					try {
						newRank = Objects.requireNonNull(e1.getOption("rank")).getAsInt();
					} catch(Exception exception) {
						newRank = -1;
					}
					SetPermLevel.setPermLevel(e, target, newRank);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "globalstats" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3) {
					GlobalStats.gobalStats(e);
				} else {
					Utils.permissionDenied(e);
				}
			}
			case "resetdaily" -> {
				if(checkPermLevel(e.getAuthor().getId()) == 3) {
					MoreDailyTime.moreDailyTime(e);
				} else {
					Utils.permissionDenied(e);
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
		System.out.println("        Thread " + Thread.currentThread().threadId() + " Finished.");
	}
}

public class NewReceiver extends ListenerAdapter {
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e3) {
		CreateThreadSlash.setSlashEvent(e3);
		CreateThreadSlash.setGenericDiscordEvent(new GenericDiscordEvent(e3));
		Thread object = new Thread(new CreateThreadSlash());
		object.start();
	}
}