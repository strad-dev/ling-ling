package processes;

import eventListeners.GenericDiscordEvent;
import eventListeners.ILoveJava;
import leveling.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import regular.*;

import java.util.Objects;
// BEETHOVEN-ONLY CLASS

class CreateThreadBeethoven implements Runnable {
	private static GenericDiscordEvent e;

	public static void setGenericDiscordEvent(GenericDiscordEvent e1) {
		e = e1;
	}

	@Override
	public void run() {
		String[] message = e.getMessage().getContentRaw().toLowerCase().split(" ");
		if(message.length > 1) {
			System.out.println("[DEBUG] New Thread: " + Thread.currentThread().threadId() + "\n        Command: beethoven " + message[1]);
			switch(message[1]) {
				case "link" -> {
					if(message.length < 3) {
						e.reply("You must provide a name.");
						return;
					}
					Link.link(e, message[2]);
				}
				case "baldness" -> {
					if(message.length < 3) {
						e.reply("You must provide a name.");
						return;
					}
					String profile;
					try {
						profile = message[3];
					} catch(Exception exception) {
						profile = "";
					}
					Baldness.baldness(e, message[2], profile);
				}
				case "help" -> BeethovenHelp.help(e, message);
				case "gstart" -> {
					if(e.getChannel().getId().equals("734697492490354768")) {
						Giveaways.giveaways(e, message);
					} else {
						e.reply("This command can only be run in <#734697492490354768>!");
					}
				}
				case "staffreport" -> StaffReport.staffReport(e);
				case "pingdeadchat" -> {
					e.getChannel().deleteMessageById(e.getMessage().getId()).queue();
					long sinceLast = System.currentTimeMillis() - e.getChannel().getHistory().retrievePast(2).complete().get(1).getTimeCreated().toEpochSecond() * 1000;
					if(sinceLast > 7200000) {
						e.reply("<@&934618203152347187>\nRequested by " + Objects.requireNonNull(e.getGuild().getMember(e.getAuthor())).getNickname());
					} else {
						e.reply(":x: You cannot ping this until there has been at least **2 hours** since the previous message.");
					}
				}
				case "forceupdategiveaways" -> CheckGiveaways.checkGiveaways(e);
				case "rerollgiveaway" -> RerollGiveaway.rerollGiveaway(e);
				case "slowmode" -> {
					if(Objects.requireNonNull(e.getGuild().getMemberById(e.getAuthor().getId())).getRoles().contains(e.getGuild().getRoleById("735308459452661802"))) {
						TextChannel channel = (TextChannel) e.getChannel();
						channel.getManager().setSlowmode(Integer.parseInt(message[2])).queue();
						e.reply("Successfully set the slowmode to `" + message[2] + "` second(s)");
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command!");
					}
				}
				case "checkmessages", "checkmsgs" -> CheckMessages.checkMessages(e);
				case "messageleaderboard", "messagelb", "messages", "msgs", "msglb", "msgleaderboard" ->
						MessageLeaderboard.messageLeaderboard(e);
				case "resetmessages" -> {
					if(e.getAuthor().getId().equals("619989388109152256")) {
						ResetMessages.resetMessages(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command!");
					}
				}
				case "rank" -> Rank.rank(e);
				case "leaderboard", "lb", "levels" -> Leaderboard.leaderboard(e);
				case "setlevelingdata" -> SetLevel.setLevelingData(e);
				case "forcerestartlingling" -> {
					if(e.getAuthor().getId().equals("619989388109152256") || e.getAuthor().getId().equals("488487157372157962")) {
						e.reply("Forcing Ling Ling to restart...");
						ILoveJava.forceRestart();
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command!");
					}
				}
				case "checkdm" -> CheckDM.checkDM(e);
				case "annoy" -> Annoy.annoy(e);
				case "requestgiveaway" -> RequestGiveaway.requestGiveaway(e);
				case "updatehypixel" -> {
					if(e.getAuthor().getId().equals("619989388109152256") || e.getAuthor().getId().equals("488487157372157962")) {
						UpdateHypixel.updateHypixel(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command!");
					}
				}
				case "levelsettings" -> {
					if(e.getAuthor().getId().equals("619989388109152256") || e.getAuthor().getId().equals("488487157372157962")) {
						Settings.settings(e);
					} else {
						e.reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command!");
					}
				}
				default ->
						e.reply("**__Don't interrupt my composing unless you actually need something__ :face_with_symbols_over_mouth:**");
			}
			System.out.println("        Thread " + Thread.currentThread().threadId() + " Finished.");
		} else {
			e.reply("**__Don't interrupt my composing unless you actually need something__ :face_with_symbols_over_mouth:**");
		}
	}
}

public class Commands {
	public static void commands(GenericDiscordEvent e) {
		CreateThreadBeethoven.setGenericDiscordEvent(e);
		Thread object = new Thread(new CreateThreadBeethoven());
		object.start();
	}
}