package leveling;

import com.mongodb.client.MongoCollection;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processes.DatabaseManager;
import processes.Numbers;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

// BEETHOVEN-ONLY CLASS
public class MessageLeaderboard {
	public static void messageLeaderboard(GenericDiscordEvent e) {
		MongoCollection<Document> documents = DatabaseManager.prepareStoreAllData("Leveling Data");
		JSONObject data = Leveling.loadData(e);
		long userMessages = (long) data.get("messages");
		long place = 1;
		String[] entry = new String[]{e.getAuthor().getId() + " " + userMessages, "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0", "0 0"};
		List<Member> list = e.getGuild().getMembers();
		for(Member member : list) {
			JSONParser parser = new JSONParser();
			try {
				data = (JSONObject) parser.parse(Objects.requireNonNull(documents.find(eq("discordID", member.getId())).first()).toJson());
			} catch(Exception exception) {
				continue;
			}
			if(data == null) {
				continue;
			}
			String id = member.getId();
			long messages = (long) data.get("messages");
			if(messages == 0) {
				continue;
			}
			for(int i = 0; i < 10; i++) {
				if(messages > Long.parseLong(entry[i].split(" ")[1]) && !id.equals(e.getAuthor().getId())) {
					System.arraycopy(entry, i, entry, i + 1, 9 - i);
					entry[i] = id + " " + messages;
					break;
				}
			}
			if(messages > userMessages) {
				place++;
			}
		}
		StringBuilder board = new StringBuilder();
		for(int i = 0; i < 10; i++) {
			String id = entry[i].split(" ")[0];
			JSONObject temp = DatabaseManager.getDataForUser(e, "Leveling Data", id);
			assert temp != null;
			board.append("**").append(i + 1).append("\\. ").append(Objects.requireNonNull(e.getGuild().getMemberById(id)).getNickname())
					.append("** `").append(id).append("`: ").append(Numbers.formatNumber(Long.parseLong(entry[i].split(" ")[1]))).append(" messages").append("\n");
		}
		if(place >= 11) {
			board.append("\n**").append(place).append("\\. You**: ").append(userMessages).append(" messages");
		}
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(Color.BLUE)
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.setTitle("__**XP Leaderboard**__")
				.addField("Most Active Users This Month", board.toString(), false);
		e.replyEmbeds(builder.build());
	}
}