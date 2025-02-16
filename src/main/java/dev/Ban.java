package dev;

import economy.Start;
import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

import java.util.Objects;

public class Ban {
	public static void ban(GenericDiscordEvent e, String idToModerate, String reason) {
		try {
			if(idToModerate.isEmpty()) {
				throw new IllegalArgumentException();
			}
			Long.parseLong(idToModerate);
		} catch(Exception exception) {
			e.reply("You didn't provide an ID!");
			return;
		}

		if(idToModerate.equals(e.getAuthor().getId())) {
			e.reply("Imagine trying to ban yourself, how dumb are you???");
		} else if(idToModerate.equals("619989388109152256") || idToModerate.equals("488487157372157962")) {
			e.reply("Imagine trying to ban a developer smh");
		} else {
			JSONObject data = DatabaseManager.getDataForUser("Economy Data", idToModerate);
			if(data == null) {
				Start.start(e, idToModerate, true);
			} else {
				data.replace("banned", true);
				DatabaseManager.saveDataForUser("Economy Data", idToModerate, data);
			}
			String name;
			try {
				name = Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).getName();
			} catch(Exception exception) {
				name = "Someone";
			}
			Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("734697505543159879"))
					.sendMessage("""
							:hammer: **THE BAN HAMMER HAS SPOKEN** :hammer:
							            Someone has been banned.
												
							https://i.imgur.com/KTyk7EC.mp4
							""").queue();
			LogCase.logCase(e, "BAN", idToModerate, reason);
			try {
				Objects.requireNonNull(e.getJDA().getUserById(idToModerate)).openPrivateChannel()
						.queue((channel) -> channel.sendMessage("You were banned.  Reason: " + reason).queue());
			} catch(Exception exception) {
				// nothing here lol
			}
			e.reply(":hammer: " + name + " was successfully banned!");
		}
	}
}