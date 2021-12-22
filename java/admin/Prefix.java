package admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class Prefix {
	public Prefix(GuildMessageReceivedEvent e) {
		char newPrefix = e.getMessage().getContentRaw().split(" ")[1].charAt(0);
		JSONObject data = new JSONObject();
		data.put("prefix", newPrefix);
		try(FileWriter writer = new FileWriter("Ling Ling Bot Data\\Settings\\Server\\" + e.getGuild().getId() + ".json")) {
			writer.write(data.toJSONString());
			writer.close();
		} catch(Exception exception) {
			try {
				File file = new File("Ling Ling Bot Data\\Settings\\Server\\" + e.getGuild().getId() + ".json");
				file.createNewFile();
				FileWriter writer = new FileWriter(file.getAbsolutePath());
				writer.write(data.toJSONString());
				writer.close();
			} catch(Exception exception1) {
				//nothing here lol
			}
		}
		e.getChannel().sendMessage("The prefix is now `" + newPrefix + "`").queue();
	}
}