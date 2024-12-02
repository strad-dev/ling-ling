package regular;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Help {
	public static void help(GenericDiscordEvent e, String page) {
		EmbedBuilder builder = new EmbedBuilder().setColor(Color.BLUE).setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl()).setTitle("__**Ling Ling Bot Help**__");
		if(page.isEmpty()) {
			builder.addField("Help List", """
					Use `!help [page]` to view further commands!
					
					`1` - Fun Commands
					`2` - Utility Commands
					`3` - Economy Commands
					`4` - Income Commands
					`5` - Bot Moderation Commands
					`6` - Dev-Only Commands""", false);
		} else {
			switch(page) {
				case "1" -> builder.addField("Help List Page 1 - Fun\nRun `!help <commandName>` to view a command in depth",
						"""
								`joke`
								`kill`
								`emojify`""", false);
				case "2" -> builder.addField("Help List Page 2 - Utility\nRun `!help <commandName>` to view a command in depth",
						"""
								**`rules`**
								`staff`
								`poll`
								`guide`
								`invite`
								`faq`
								`support`
								`website`
								`botstats`
								`settings`
								`link`""", false);
				case "3" -> builder.addField("Help List Page 3 - Economy Commands\nRun `!help <commandName>` to view a command in depth.",
						"""
								**__`Profile Commands`__**
								`start`\t`profile`
								`stats`\t`inventory`
								`achievements`

								**__`Gameplay Commands`__**
								`cooldowns`\t`upgrades`
								`buy`\t`use`
								`claim`\t`gift`
								`craft`\t`market`
								`leaderboard`

								**__`Bank Commands`__**
								`deposit`\t`withdraw`
								`loan`\t`payloan`""", false);
				case "4" -> builder.addField("Help List Page 4 - Income Commands\nRun `!help <commandName>` to view a command in depth.",
						"""
								`scales`
								`resetstreak`
								`practice`
								`rehearse`
								`perform`
								`teach`
								`daily`
								`hourly`
								`gamble`
								`rob`""", false);
				case "5" -> builder.addField("Help List Page 5 - Bot Moderation Commands\nOnly Bot Mods and Bot Admins have access to these commands.",
						"""
								**__`Bot Mod Commands`__**
								`give`
								`warn`
								`resetsave`

								**__`Bot Admin Commands`__**
								`luthier`
								`resetincomes`
								`ban`
								`unban`
								`updateluthierchance`""", false);
				case "6" -> builder.addField("Help List Page 6 - Dev-Only Commands\nOnly Developers have access to these commands.\nThis page exists for people who are curious.",
						"""
								`resetdaily`
								`updateusers`
								`updateroles`
								`setpermlevel`
								`globalstats`
								`sudo`
								**`forcestop`**""", false);
				
				// PAGE 1
				case "joke" -> builder.addField("Joke Command", "Syntax: `!joke`" +
						"\nUsage: Returns a music-related joke", false);
				case "kill" -> builder.addField("Kill Command", """
						Syntax: `!kill <thing>`
						Usage: Totally kills the target
						Example: `!kill violas`""", false);
				case "emojify" -> builder.addField("Emojify Command", """
						Syntax: `!emojify [message]`
						Usage: Returns your message using regional indicator emojis.
						Restrictions: You can only use numbers, letters, spaces, question marks, and exclamation points.  Incompatible with mentions.
						Example: `!emojify go practise`""", false);
				
				// PAGE 2
				case "rules" -> builder.addField("Rules Command", "Syntax: `!rules`" +
						"\nUsage: View the bot's rules.  By using the bot, you agree to following these rules and can be subject to punishment for breaking them knowingly or unknowingly.", false);
				case "staff" -> builder.addField("Staff Command", "Syntax: `!staff`" +
						"\nUsage: Shows the bot's moderation staff.  You may DM any of them for help at any time.", false);
				case "poll" -> builder.addField("Poll Command", """
						Syntax: `!poll "<PollName>" "<Options; separated; by; semicolons>`"
						Usage: Creates a simple poll where reactions are used to vote.
						Example: `!poll "Which instrument is better?" "Violin; Viola; Cello; Bass"`""", false);
				case "guide" -> builder.addField("Guide Command", "Syntax: `!guide`" +
						"\nUsage: Gives the link to the beginner's guide.", false);
				case "invite" -> builder.addField("Invite Command", "Syntax: `!invite`" +
						"\nUsage: Gives you instructions on how to invite the bot to your server.", false);
				case "faq" -> builder.addField("FAQ Command", """
						Syntax: `!faq <item>`
						Usage: Shows answers  to frequently asked questions.
						Example: `!faq rob`""", false);
				case "support" -> builder.addField("Support Command", "Syntax: `!support`" +
						"\nUsage: Gives a link to the support server.", false);
				case "website" -> builder.addField("Website Command", "Syntax: `!website`" +
						"\nUsage: Gives a link to view the bot's website.  Does not guarantee a non-shit website.", false);
				case "botstats" -> builder.addField("BotStats Command", "Syntax: `!botstats`" +
						"\nUsage: Shows the number of servers the bot is in and the number of profiles that have been created.", false);
				case "settings" -> builder.addField("Settings Command", "Syntax: `!settings [setting] <(setting) newValue>`" +
						"\nUsage: Change your settings.", false);

				// PAGE 3
				case "start" -> builder.addField("Start Command", "Syntax: `!start`" +
						"\nUsage: Creates a profile for the user.  This can only be used once.", false);
				case "profile" -> builder.addField("Profile Command", "Syntax: `!b [user]`" +
						"\nUsage: Shows your profile or the profile of another user.", false);
				case "stats" -> builder.addField("Stats Command", "Syntax: `!stats [user]`" +
						"\nUsage: Shows your detailed stats or the stats of another user.", false);
				case "inventory" -> builder.addField("Inventory Command", "Syntax: `!inv <page> [user]`" +
						"\nUsage: Shows your inventory or the inventory of another user.", false);
				case "achievements" -> builder.addField("Achievements Command", """
						Syntax: `!a`
						Usage: Shows your achievements!""", false);
				case "cooldowns" -> builder.addField("Cooldowns Command", "Syntax: `!c`" +
						"\nUsage: Shows you the cooldowns of most commands.", false);
				case "upgrades" -> builder.addField("Upgrades Command", """
						Syntax: `!up <page>`
						Usage: Shows you a page of the shop.  Must include a page number.
						Example: `!upgrades 2`""", false);
				case "buy" -> builder.addField("Buy Command", """
						Syntax: `!buy <itemID>`
						Usage: Buys an upgrade.
						Example: `!buy practising`""", false);
				case "use" -> builder.addField("Use Command", """
						Syntax: `!use <id> [num]`
						Usage: Uses usable items like Rice.
						Example: `!use rice 5`""", false);
				case "market" -> builder.addField("Market Command", """
						Syntax: `!market <buy | sell | view | offers | cancel> <(buy/sell/view) item> [(buy/sell) amount] [(sell) price]`
						Usage: Interacts with the Publik Market.
						Examples: `!market view grains` `!market buy grains 15` `!market sell grains 100 10` `!market cancel`""", false);
				case "craft" -> builder.addField("Craft Command", """
						Syntax: `!craft <item> [amount | max]`
						Usage: Crafts items using raw materials.
						Example: `!craft rice 5`""", false);
				case "claim" -> builder.addField("Claim Command", """
						Syntax: `!claim`
						Usage: Claims a Free Box.
						Cooldown: 12h""", false);
				case "gift" -> builder.addField("Gift Command", """
						Syntax: `!gift <user>`
						Usage: Gifts the user a Gift Box.
						Cooldown: 23.75h
						Example: `!gift 488487157372157962`""", false);
				case "leaderboard" -> builder.addField("Leaderboard Command", """
						Syntax: `!lb <type>`
						Usage: Shows the leaderboard for a specific data point.
						Example: `!lb violins`""", false);
				case "deposit" -> builder.addField("Deposit Command", "Syntax: `!dep <amount | max>`" +
						"\nUsage: Deposits violins into your bank account.  Use `max` to deposit the most possible.", false);
				case "withdraw" -> builder.addField("Withdraw Command", "Syntax: `!with <amount | max>`" +
						"\nUsage: Withdraws violins from your bank account.  Use `all` to withdraw everything.`", false);
				case "loan" -> builder.addField("Loan Command", "Syntax: `!loan <amount | max>`" +
						"\nUsage: Take out a loan, maximum amount is based on your hourly income.  You can also use this to check how much you owe.", false);
				case "payloan" -> builder.addField("PayLoan Command", "Syntax: `!payloan <amount | max>`" +
						"\nUsage: Pays back a specified amount, or everything in your wallet.", false);
				
				// PAGE 4
				case "scales" -> builder.addField("Scales Command", """
						Syntax: `!s`
						Usage: Practise scales earn some violins!
						Cooldown: 90s/60s""", false);
				case "resetstreak" -> builder.addField("ResetStreak Command", """
						Syntax: `!resetstreak`
						Usage: Resets your Scale Streak to 0 and allows you to restart it on your own terms.""", false);
				case "practice" -> builder.addField("Practice Command", """
						Syntax: `!p`
						Usage: Practise to earn some violins!
						Cooldown: 60m/40m""", false);
				case "rehearse" -> builder.addField("Rehearse Command", """
						Syntax: `!r`
						Usage: Rehearse with an orchestra to earn loads of violins!
						Restrictions: Usable only by people with an Orchestra.
						Cooldown: 12h/8h""", false);
				case "perform" -> builder.addField("Perform Command", """
						Syntax: `!pf`
						Usage: Perform your solo to earn an INSANE amount of violins!  Gain even more by hiring an orchestra and upgrading your Concert Hall!
						Cooldown: 60h/40h""", false);
				case "teach" -> builder.addField("Teach Command", """
						Syntax: `!t`
						Usage: Teach a student to earn more violins!  Not affected by Efficient Practising, instead it has its own upgrades to improve the payout.
						Restrictions: Usable only by people with a Teaching Certificate.
						Cooldown: 1h""", false);
				case "daily" -> builder.addField("Daily Command", """
						Syntax: `!d`
						Usage: Get a daily dose of violins!  Run the command many days in a row to start gaining a streak and get even more violins!
						Cooldown: 23.75h""", false);
				case "hourly" -> builder.addField("Hourly Command", """
						Syntax: `!h`
						Usage: Claim all hourly incomes you are eligible for!
						Cooldown: 0-60m""", false);
				case "gamble" -> builder.addField("Gamble Command", """
						Syntax: `!bet <type> <amount | max>`
						Usage: Bets the amount using the gamemode specified.  You can only bet up to 10x your hourly income.  Writing `max` in place of the amount will bet the maximum allowed.
						Gambling Options: `rng` `slots` `scratch`
						Cooldown: 10s
						Example: `!gamble slots 4000`""", false);
				case "rob" -> builder.addField("Rob Command", """
						Syntax: `!rob <user>`
						Usage: Robs the user.  Beware that the more violins you have than the target, the harder it is to succeed!
						Cooldown: 16h
						Example: `!rob 488487157372157962`""", false);
				case "answer" -> builder.addField("Answer Command", """
						Syntax: `!answer <yourGuess>`
						Usage: Give your answer for a Luthier question.
						Example: `!answer sacrinterestinglegious`""", false);
				
				// PAGE 5
				case "give" -> builder.addField("Give Command", """
						Syntax: `!give <id> <amount> <type>`
						Usage: Gives the user an amount of the specified currency.  Only works with integer-based data.
						Restrictions: Usable only by Bot Moderators and above.""", false);
				case "warn" -> builder.addField("Warn Command", """
						Syntax: `!warn <user> [reason]`
						Usage: Gives a user a bot warning.
						Restrictions: Usable only by Bot Moderators and above.
						Example: `!warn 488487157372157962 not practising forty hours a day`""", false);
				case "resetsave" -> builder.addField("ResetSave Command", """
						Syntax: `!resetsave <user> [reason]`
						Usage: Resets the save of a user.
						Restrictions: Usable only by Bot Moderators and above.
						Example: `!resetsave 488487157372157962 abusing bugs`""", false);
				case "luthier" -> builder.addField("Luthier Command", """
						Syntax: `!luthier <setup | edit> <(edit) channel | multiplier | word> <(edit) newValue>`
						Usage: Sets up and edits a server's Luthier.
						Restrictions: Usable only by Bot Admins and above.""", false);
				case "resetincomes" -> builder.addField("ResetIncomes Command", """
						Syntax: `!resetincomes`
						Usage: Manually resets all incomes.
						Restricitons: Usable only by Bot Admins and above.""", false);
				case "ban" -> builder.addField("BAN Command", """
						Syntax: `!ban <user> [reason]`
						Usage: Bans the user from using the economy system.
						Restrictions: Usable only by Bot Admins and above.
						Example: `!ban 488487157372157962 abusing bugs AND not practising`""", false);
				case "unban" -> builder.addField("Unban Command", """
						Syntax: `!unban <user> <wipe> [reason]`
						Usage: Unbans a user from using the economy system.  Choice to wipe save or not.
						Restrictions: Usable only by Bot Admins and above.
						Example: `!unban 488487157372157962 false appealed successfully`""", false);
				case "updateluthierchance" -> builder.addField("UpdateLuthierChance Command", """
						Syntax: `!updateluthierchance`
						Usage: Updates the luthier spawn chance manually
						Restrictions: Usable only by Admins and above.""", false);
				
				// PAGE 6
				case "resetdaily" -> builder.addField("ResetDaily Command", """
						Syntax: `!resetdaily`
						Usage: Gives everyone more time to run `!daily`
						Restrictions: Usable only by Developers.""", false);
				case "updateusers" -> builder.addField("UpdateUsers Command", """
						Syntax: `!updateusers <dataType> <name> [value]`
						Usage: Adds variables to the save file.
						Restrictions: Usable only by Developers.""", false);
				case "updateroles" -> builder.addField("UpdateRoles Command", """
						Syntax: `!updateroles`
						Usage: Updates role multipliers/booster status of users in the support server.
						Restrictions: Usable only by Developers in the Support Server.""", false);
				case "setpermlevel" -> builder.addField("SetPermLevel Command", """
						Syntax: `!setpermlevel <id> <level>`
						Usage: Sets the permission level of the specified user.  `0` for none, `1` for mod, `2` for Admin
						Restrictions: Usable only by Developers.""", false);
				case "globalstats" -> builder.addField("GlobalStats Command", """
						Syntax: `!globalstats`
						Usage: Shows the global statistics of the bot.  Aka combining the stats from all users.
						Restrictions: Usable only by Developers, due to crash concerns.""", false);
				case "sudo" -> builder.addField("Sudo Command", """
						Syntax: `!sudo <user> <command>`
						Usage: Runs the given command as if the given user ran it.  Mostly to help with debugging.
						Restrictions: Usable only by Developers.""", false);
				case "forcestop" -> builder.addField("ForceStop Command", """
						Syntax: `!forcestop`
						Usage: Forces the bot to stop.  **USE IN EMERGENCY CIRCUMSTANCES**
						Restrictions: Usable only by Developers.""", false);
				default -> builder.addField("Help List", "The page/command you are looking for doesn't exist!  Run `!help` to see a list of pages.", false);
			}
		}
		e.replyEmbeds(builder.build());
	}
}
