package leveling;

import eventListeners.GenericDiscordEvent;

public class Settings {
	public static void settings(GenericDiscordEvent e) {
		String[] message = e.getMessage().getContentRaw().split(" ");
	}
}