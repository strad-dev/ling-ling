package economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.FileReader;
import java.util.Objects;

public class Inventory {
	public Inventory(GuildMessageReceivedEvent e) {
		JSONObject data;
		String[] message = e.getMessage().getContentRaw().split(" ");
		String user;
		if(message.length == 1) {
			user = e.getAuthor().getId();
			data = LoadData.loadData(e);
		} else {
			user = message[1];
			JSONParser parser = new JSONParser();
			try(FileReader reader = new FileReader("Ling Ling Bot Data\\Economy Data\\" + user + ".json")) {
				data = (JSONObject) parser.parse(reader);
				reader.close();
			} catch(Exception exception) {
				e.getChannel().sendMessage("This save file does not exist!").queue();
				throw new IllegalArgumentException();
			}
		}
		try {
			user = Objects.requireNonNull(e.getJDA().getUserById(user)).getName();
		} catch(Exception exception) {
			user = "Someone";
		}
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.BLUE)
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.setTitle(user + "'s Inventory")
				.addField("Rice :rice:", "Count: " + data.get("rice") + "\nUsage: Gives you 2 hours of income.\nID: `rice`", true)
				.addField("Bubble Tea :bubble_tea:", "Count: " + data.get("tea") + "\nUsage: Gives you 6 hours of income.\nID: `tea`", true)
				.addField("Ling Ling Blessing :angel:", "Count: " + data.get("blessings") + "\nUsage: Gives you 24 hours of income and 1-3 Ling Ling Medals.\nID: `blessing`", true)
				.addField("Vote Box :ballot_box:", "Count: " + data.get("voteBox") + "\nUsage: Gives you random items, as decided by RNGesus.\nID: `vote`", true)
				.addField("Gift Box :gift:", "Count: " + data.get("giftBox") + "\nUsage: Gives you semi-valuable random items, as decided by RNGesus.\nID: `gift`", true)
				.addField("**Donator Boxes**", data.get("kits") + " Standard Musician Kits\n" + data.get("linglingBox") + " Ling Ling Boxes\n" + data.get("crazyBox") + " Crazy Person Boxes\nUsage: Gives you valuable random items, as decided by RNGesus.  You can only get Medals once per day\nID: `kit` `llbox` `crazybox`", true);
		e.getChannel().sendMessageEmbeds(builder.build()).queue();
	}
}