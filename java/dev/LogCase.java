package dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Objects;

public class LogCase {
	public LogCase(GuildMessageReceivedEvent e, String type, String id, String reason) {
		File file = new File("Ling Ling Bot Data\\Moderation\\" + id);
		if(!file.exists()) {
			file.mkdirs();
		}
		int caseNum = Objects.requireNonNull(file.listFiles()).length;
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.BLUE)
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.addField("Moderator: " + e.getAuthor().getName(), "User: <@" + id + ">\nReason: " + reason, false)
				.setTitle("__**Case " + caseNum + ": " + type + "**__");
		Objects.requireNonNull(Objects.requireNonNull(e.getJDA().getGuildById("670725611207262219")).getTextChannelById("863135059712409632")).sendMessageEmbeds(builder.build()).queue();
		JSONObject data = new JSONObject();
		data.put("user", id);
		data.put("moderator", e.getAuthor().getName());
		data.put("type", type);
		data.put("reason", reason);
		try(FileWriter writer = new FileWriter(file.getAbsolutePath() + "\\" + caseNum + ".json")) {
			writer.write(data.toJSONString());
			writer.close();
		} catch(Exception exception) {
			e.getChannel().sendMessage("Something went terribly wrong!").queue();
		}
	}
}