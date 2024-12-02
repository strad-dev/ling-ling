package regular;

import eventListeners.GenericDiscordEvent;
import processes.Numbers;

public class Emojify {
	public static void emojify(GenericDiscordEvent e, String message) {
		if(message.isEmpty()) {
			e.reply("how are we going to emojify nothing");
			return;
		}
		if(Numbers.containsBadLanguage(message)) {
			message = "NICE TRY";
		}
		message = message.toLowerCase();
		StringBuilder send = new StringBuilder();
		for(int i = 0; i < message.length(); i++) {
			char current = message.charAt(i);
			if(current == ' ') {
				send.append(":blue_square: ");
			} else if(current == '1') {
				send.append(":one: ");
			} else if(current == '2') {
				send.append(":two: ");
			} else if(current == '3') {
				send.append(":three: ");
			} else if(current == '4') {
				send.append(":four: ");
			} else if(current == '5') {
				send.append(":five: ");
			} else if(current == '6') {
				send.append(":six: ");
			} else if(current == '7') {
				send.append(":seven: ");
			} else if(current == '8') {
				send.append(":eight: ");
			} else if(current == '9') {
				send.append(":nine: ");
			} else if(current == '0') {
				send.append(":zero: ");
			} else if(current == '?') {
				send.append(":grey_question: ");
			} else if(current == '!') {
				send.append(":grey_exclamation: ");
			} else if(current >= 65 && current <= 90 || current >= 97 && current <= 122) {
				send.append(":regional_indicator_").append(current).append(": ");
			} else {
				e.reply("You can only use spaces, numbers, letters, ?, and !");
				return;
			}
		}
		send.append("\n`").append(e.getAuthor().getEffectiveName()).append("`");
		try {
			e.getChannel().sendMessage(send.toString()).queue();
		} catch(Exception exception) {
			e.reply("Your message ended up being too long, try shortening it.");
		}
		
	}
}
