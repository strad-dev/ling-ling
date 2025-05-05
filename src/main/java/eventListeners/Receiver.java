package eventListeners;

import dev.UpdateLuthierChance;
import leveling.Leveling;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import processes.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

// BEETHOVEN-ONLY CLASS
public class Receiver extends ListenerAdapter {
	public void onMessageReceived(@NotNull MessageReceivedEvent e) {
		GenericDiscordEvent e1 = new GenericDiscordEvent(e);

		// background functions - ignore if beta testing
		if(!StartBot.isBeta()) {
			if(e.getChannel().getId().equals("863135059712409632") || (e.getChannel().getId().equals("1029498872441077860") && !e1.getMessage().getContentRaw().equals("Done!")) || e.getChannel().getId().equals("734697496688853012")) {
				CheckGiveaways.checkGiveaways(e1);

				UpdateLuthierChance.updateLuthierChance(e1, false);

				ArrayList<Document> documents = DatabaseManager.getAllEconomyData();
				if(e.getJDA().getSelfUser().getId().equals("846097308504293393")) {
					int users = documents.size();
					VoiceChannel channel = Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getVoiceChannelById("839877827838476289");
					assert channel != null;
					channel.getManager().setName(users + " Ling Ling Wannabes").queue();
				}
			}
			if(e.getChannel().getId().equals("734697521758339163")) {
				CheckCounting.checkCounting(e1);
			}
			if(e.getChannel().getId().equals("836710100127318057")) {
				CheckReply.checkReply(e1);
			}
			if(e.getChannel().getId().equals("734697496688853012") && e.getAuthor().getId().equals("235148962103951360")) {
				TimeOut.timeOut(e1);
			}
			if(e.getChannel().getId().equals("798751619344367626") && e.getAuthor().getId().equals("235148962103951360")
					&& Objects.requireNonNull(e.getMessage().getEmbeds().getFirst().getTitle()).contains("Suggestion")) {
				e.getChannel().sendMessage("<@&930189721685094491>").queue();
			}
			if(e.getChannel().getId().equals("759092196976099348") && e.getAuthor().isBot() && e.getMessage().getContentRaw().contains("POLL:")) {
				e.getChannel().sendMessage("<@&747954053660540928> <@&750876814842527754>").queue();
			}
		}

		if(!e.getAuthor().isBot()) {
			// Beethoven Commands
			Autoresponse.autoresponse(e1);
			if(!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById("736287976224587838"))) {
				WordAutomod.wordAutomod(e1);
			}
			// IGNORE BEETHOVEN COMMANDS IF BETA UNLESS IN CORRECT CHANNEL
			if(e.getMessage().getContentRaw().toLowerCase().split(" ")[0].equals("beethoven") &&
					(StartBot.isBeta() && Utils.isBetaChannel(e.getChannel().getId()) || !StartBot.isBeta() && !Utils.isBetaChannel(e.getChannel().getId()))) {
				Commands.commands(e1);
			}
			double multipler = 0.0;
			switch(e.getChannel().getId()) {
				case "734697505543159879", "845122401352417350", "845397074156847124", "1086924606352404550", "1101190200438300756" ->
						multipler = 1.0;
				case "834897888340869151", "748059191440179204", "734697521758339163", "836710100127318057" ->
						multipler = 0.5;
			}
			// IGNORE LEVELING IF BETA
			if(multipler != 0.0 && !StartBot.isBeta()) {
				Leveling.leveling(e1, multipler);
			}

			if(e1.getChannel().getId().equals("763099851550097408")) {
				Luthier.luthier(e1, DatabaseManager.getDataByGuild(e1, "Luthier Data"), e1.getMessage().getContentRaw());
			}

			// Ling Ling Commands
			// IF BETA TESTING, IGNORE NON-TESTING CHANNELS
			// IF NOT BETA TESTING, IGNORE TESTING CHANNELS
			String[] message = e.getMessage().getContentRaw().toLowerCase().split(" ");
			try {
				if(message[0].charAt(0) == '!') {
					if(StartBot.isBeta() && Utils.isBetaChannel(e.getChannel().getId()) || !StartBot.isBeta() && !Utils.isBetaChannel(e.getChannel().getId())) {
						message[0] = message[0].substring(1);
						String[] realMessage = new String[message.length + 1];
						realMessage[0] = "<@733409243222507670>";
						System.arraycopy(message, 0, realMessage, 1, message.length);
						CreateThreadMessage.setGenericDiscordEvent(e1, realMessage);
						Thread object = new Thread(new CreateThreadMessage());
						object.start();
					}
				}
			} catch(StringIndexOutOfBoundsException exception) {
				// do nothing
			}
		}
		Random random = new Random();
		if(e.getChannel().getName().contains("announcement") || random.nextDouble() <= 0.025) {
			if(!e.getAuthor().getId().equals("733409243222507670") || e.getAuthor().getId().equals("733409243222507670") && !e.getMessage().getContentRaw().toLowerCase().contains("poll")) {
				try {
					e.getChannel().addReactionById(e.getChannel().getLatestMessageId(), Emoji.fromUnicode("U+1F1FB")).queue();
					e.getChannel().addReactionById(e.getChannel().getLatestMessageId(), Emoji.fromUnicode("U+1F1EE")).queue();
					e.getChannel().addReactionById(e.getChannel().getLatestMessageId(), Emoji.fromUnicode("U+1F1F4")).queue();
					e.getChannel().addReactionById(e.getChannel().getLatestMessageId(), Emoji.fromUnicode("U+1F1F1")).queue();
					e.getChannel().addReactionById(e.getChannel().getLatestMessageId(), Emoji.fromUnicode("U+1F1E6")).queue();
				} catch(Exception exception) {
					// nothing here
				}
			}
		}
	}
}