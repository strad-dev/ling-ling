package regular;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import processes.Utils;

public class Poll {
	
	public static void poll(GenericDiscordEvent e, String title, String choices) {
		if(Utils.containsBadLanguage(title)) {
			title = "NICE TRY";
		}
		StringBuilder send = new StringBuilder("**POLL: ").append(title).append("**\n");
		
		if(choices.isEmpty()) {
			e.reply("You must provide choices.  Can't have people vote on nothing, you know.");
			return;
		}
		if(Utils.containsBadLanguage(choices)) {
			choices = "NICE TRY";
		}
		
		String[] splitChoices = choices.split(";");
		if(splitChoices.length > 20) {
			e.reply("Please limit your polls to 20 options or less.");
			return;
		}
		char currentChar = 'a';
		for(String splitChoice : splitChoices) {
			send.append(":regional_indicator_").append(currentChar).append(": ").append(splitChoice).append("\n");
			currentChar ++;
		}

		send.append("\nPoll created by ").append(e.getAuthor().getEffectiveName());
		Message message = e.getChannel().sendMessage(send.toString()).complete();
		int hex = 127462;
		for(int j = 0; j < splitChoices.length; j++) {
			Emoji emoji = Emoji.fromUnicode("U+" + Integer.toHexString(hex));
			message.addReaction(emoji).queue();
			hex++;
		}
	}
}
