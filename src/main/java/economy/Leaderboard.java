package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;
import processes.Utils;

import java.awt.*;
import java.util.ArrayList;

public class Leaderboard {
	public static void leaderboard(GenericDiscordEvent e, String emoji, String what, String index, long userNum, String color) {
		ArrayList<Document> documents = DatabaseManager.getAllEconomyData();
		long place = 1;
		String[] entry = {e.getAuthor().getId() + " " + userNum, "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0"};
		for(Document file : documents) {
			JSONParser parser = new JSONParser();
			JSONObject data;
			try {
				data = (JSONObject) parser.parse(file.toJson());
				if((boolean) data.get("banned")) {
					continue;
				}
			} catch(Exception exception) {
				continue;
			}
			String id = data.get("discordID").toString();
			if(id.equals(e.getAuthor().getId())) {
				continue;
			}
			long num;
			if(index.equals("hoursPractised") || index.equals("hoursTaught")) {
				double temp = (double) data.get(index);
				num = (long) temp;
			} else {
				num = (long) data.get(index);
			}
			if(num == 0) {
				continue;
			}
			for(int i = 0; i < 10; i++) {
				if(num > Long.parseLong(entry[i].split(" ")[1]) && !id.equals(e.getAuthor().getId())) {
					System.arraycopy(entry, i, entry, i + 1, 9 - i);
					entry[i] = id + " " + num;
					break;
				}
			}
			if(num > userNum) {
				place++;
			}
		}
		StringBuilder board = new StringBuilder();
		for(int i = 0; i < 10; ++i) {
			String id = entry[i].split(" ")[0];
			JSONObject temp = DatabaseManager.getDataForUser(e, "Economy Data", id);
			assert temp != null;
			board.append("**").append(i + 1).append("\\. ").append(temp.get("discordName")).append("** `").append(id).append("`: ")
					.append(Utils.formatNumber(Long.parseLong(entry[i].split(" ")[1]))).append(" ").append(emoji).append("\n");
		}

		if(place >= 11L) {
			board.append("\n**").append(place).append("\\. You**: ").append(Utils.formatNumber(userNum)).append(emoji);
		}
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.decode(color))
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.setTitle("__**Global Leaderboard**__")
				.addField("**" + what + " in the World**", board.toString(), false);
		e.replyEmbeds(builder.build());
	}
}