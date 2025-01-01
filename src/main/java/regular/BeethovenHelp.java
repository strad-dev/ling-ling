package regular;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
// BEETHOVEN-ONLY CLASS
public class BeethovenHelp {
    public static void help(GenericDiscordEvent e, String[] message) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**__Beethoven Bot Help__**")
                .setFooter("Beethoven (1770-1827)", e.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.BLUE);
        try {
            switch(message[2]) {
                case "gstart" -> builder.addField("**GStart Command**", """
								Syntax: `beethoven gstart [time] [winners] [hasRequirement] [item]`
								Usage: Start a giveaway that will automatically choose a winner at the end of the allotted time.
								Restrictions: Only usable in <#734697492490354768>
								Example: `beethoven gstart 1440 2 true Ling Ling Medal`""", false)
                        .addField("**Command Parameters**", """
								`time` - Integer; the amount of time the giveaway will last in minutes
								`winners` - Integer; the number of winners that will be selected when the timer ends
								`hasRequirement` - Boolean; controls whether the requirement warning is shown
								`item` - String (multiple); the item that is being given away""", false);
                case "forceupdategiveaways" -> builder.addField("**ForceUpdateGiveaways Command**", "Syntax: `beethoven forceupdategiveaways`" +
                        "\nUsage: Forces currently running giveaways to be updated.", false);
                case "rerollgiveaway" -> builder.addField("**RerollGiveaway Command**", """
								Syntax: `beethoven rerollgiveaway [id] [winners]`
								Usage: Rerolls a giveaway.
								Restrictions: Only usable in <#734697492490354768>
								Example: `beethoven rerollgiveaway 863623564618629150 1`""", false)
                        .addField("**Command Parameters**", "`id` - Message ID; the ID of the message that the giveaway is contained in" +
                                "\n`winners` - Integer; the number of winners that will be selected from the current people that have reacted", false);
                case "slowmode" -> builder.addField("**Slowmode Command**", """
								Syntax: `beethoven slowmode [time]`
								Usage: Set the slowmode of the current channel in seconds.
								Restrictions: Only usable by <@&735308459452661802>
								Example: `beethoven slowmode 1`""", false)
                        .addField("**Command Parameters**", "`time` - Integer; number of seconds that the slowmode should be set for", false);
                case "rank" -> builder.addField("Rank Command", "Syntax: `beethoven rank`" +
                        "\nUsage: Shows you your level and XP server.", false);
                case "setlevelingdata" -> builder.addField("SetLevelingData Command", """
						Syntax: `beethoven setleveling [user] [field] [newValue]`
						Usage: Sets the leveling info of a user.
						Restrictions: Usable only by members with the `ADMINISTRATOR` permission.
						Example: `beethoven setlevelingdata 488487157372157962 level 10` `beethoven setlevelingdata 488487157372157962 messages 999`""", false);
                case "leaderboard" -> builder.addField("Leaderboard Command", """
						Syntax: `beethoven leaderboard`
						Usage: Shows the 10 people w    ith the most levels and XP server.
						Aliases: `lb` `levels`""", false);
                case "checkmessages" -> builder.addField("CheckMessages Command", """
						Syntax: `beethoven checkmessages [user]`
						Usage: Shows you the amount of messages someone has sent this month.
						Aliases: `checkmsgs`
						Example: `beethoven checkmessages 488487157372157962`""", false);
                case "resetmessages" -> builder.addField("ResetMessages Command", """
						Syntax: `beethoven resetmesssages`
						Usage: Resets all messages.
						Restrictions: Usable only by Developers""", false);
                case "messageleaderboard" -> builder.addField("MessageLeaderboard Command", """
						Syntax: `beethoven messageleaderboard`
						Usage: Shows the 10 people with the most messages this month.
						Aliases: `msgleaderboard` `messagelb` `msglb` `messages` `msgs`""", false);
                case "forcerestartlingling" -> builder.addField("ForceRestartLingLing Command", """
						Syntax: `beethoven forcerestartlingling`
						Forces Ling Ling to restart.
						Restrictions: Usable only by Developers""", false);
                case "staffreport" -> builder.addField("StaffReport Command", "Syntax: `beethoven staffreport`" +
                        "\nShows the amount of messages each staff member has sent, and whether they meet the required message count for the current month.", false);
                case "gamble" -> builder.addField("Gamble Command", """
						Syntax: `beethoven gamble <type> <amount | max>`
						Usage: Bets the amount using the gamemode specified.  Writing `max` in place of the amount will bet the maximum allowed.
						Gambling Options: `rng` `slots` `scratch`
						Cooldown: 10s
						Example: `beethoven gamble slots 40001
						Alias: `bet`""", false);
                case "pingdeadchat" -> builder.addField("PingDeadChat Command", """
						Syntax: `beethoven pingdeadchat`
						Pings <@&934618203152347187>
						Global Cooldown: 24H""", false);
                case "checkdm" -> builder.addField("CheckDM Command", """
						Syntax: `beethoven checkdm <user>`
						Usage: Sends a pre-generated message telling the mentioned user to check their DM.  Highly effective.
						Example: `beethoven checkdm 488487157372157962`""", false);
                case "annoy" -> builder.addField("Annoy Command", """
						Syntax: `beethoven annoy @user`
						Usage: Annoys the targeted user.  **REQUIRES A PING**
						Example: `beethoven annoy <@488487157372157962>`
						Global Cooldown: 1H""", false);
                case "requestgiveaway" -> builder.addField("RequestGiveaway Command", """
						Syntax: `beethoven requestgiveaway <message>`
						Usage: Request to host a giveaway!  Make sure you include how much you want to give away, how long your giveaway should be, and how many winners there are.
						Example: `beethoven requestgiveaway 1 million violins, 24 hours, 1 winner`""", false);
				case "updatehypixel" -> builder.addField("UpdateHypixel Command", """
						Syntax: `beethoven updatehypixel`
						Usage: Forcibly updates the guild roles for all members.""", false);
				case "link" -> builder.addField("Link Command", """
						Syntax: `beethoven link <IGN>`
						Usage: Gives (or removes) the <@&1090691229789982800> role.""", false);
				case "baldness" -> builder.addField("Baldness Command", """
						Syntax: `beethoven baldness <IGN> [profile]`
						Usage: Outputs the Baldness Factor for this SkyBlock player.
						Example: `beethoven baldness akc0303` `beethoven baldness beethoven_ pomegranate`
						""", false);
				case "levelsettings" -> builder.addField("LevelSettings Command", """
						Synatx: `beethoven levelsettings [min | max | cooldown] [amount]`
						Usage: Checks or updates the leveling settings.
						Example: `beethoven levelsettings min 15` `beethoven levelsettings cooldown 20`""", false);
				default -> builder.addField("Command Doesn't Exist!", "`beethoven " + message[2] + "` doesn't exist you idiot.", false);
            }
        } catch(Exception exception) {
            builder.addField("**Commands**", """
					**Run `beethoven help [command]` for more info on a command!**

					**__Utility Commands__**
					`gstart` `forceupdategiveaways`
					`rerollgiveaway` `slowmode`
					`pingdeadchat` `checkdm`
					`annoy` `forcerestartlingling`
					`requestgiveaway`
					`updatehypixel`
					`link` `baldness`

					**__Leveling Commands__**
					`rank` `setlevelingdata`
					`leaderboard` `checkmessages`
					`resetmessages` `messageleaderboard`
					`staffreport` `levelsettings`""", false);
        }
        e.replyEmbeds(builder.build());
    }
}