package dev;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;
import java.util.Objects;

public class Give {
	public static void give(GenericDiscordEvent e, String receiver, long amount, String item) {
		if(amount == -1) {
			e.reply("You have to give a number, I can't give an unknown amount of items.  Go study physics or something.");
			return;
		}
		if(amount == -2) {
			e.reply("You can only give an integer amount of items, stupid.");
			return;
		}
		if(receiver.isEmpty()) {
			e.reply("You didn't even provide a user, dummy.");
			return;
		}
		if(item.isEmpty()) {
			e.reply("You have to give out something, idiot, I can't give out nothing.");
			return;
		}
		JSONObject data = DatabaseManager.getDataForUser(e, "Economy Data", receiver);
		if(data == null) {
			e.reply("This save file doesn't exist!");
			return;
		}
		long oldAmount;
		try {
			oldAmount = (long) data.get(item);
		} catch(Exception exception) {
			e.reply("You should know that " + item + " IS NOT AN INTEGER ITEM, GO GROW A BRAIN");
			return;
		}
		String user;
		try {
			user = Objects.requireNonNull(e.getJDA().getUserById(receiver)).getName();
		} catch(Exception exception) {
			user = "Someone";
		}
		data.replace(item, oldAmount + amount);
		EmbedBuilder builder = new EmbedBuilder().setColor(Color.BLUE).setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.addField("Moderator: " + e.getAuthor().getName(), "User: <@" + receiver + ">\nItem type: " + item +
						"\nAmount given: " + Utils.formatNumber(amount), false).setTitle("__**Currency Alteration Info**__");
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("863135059712409632")).sendMessageEmbeds(builder.build()).queue();
		DatabaseManager.saveDataForUser(e, "Economy Data", receiver, data);
		Objects.requireNonNull(e.getJDA().getUserById(receiver)).openPrivateChannel().queue((channel) -> channel.sendMessage("**SPAWNED BY AN ADMIN LOL**\nA Mod/Admin has given you " +
				Utils.formatNumber(amount) + " " + item).queue());
		e.reply("Successfully gave " + Utils.formatNumber(amount) + " " + item + " to " + user);
	}
}