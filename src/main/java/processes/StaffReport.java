package processes;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.util.List;

// BEETHOVEN-ONLY CLASS
public class StaffReport {
	private static final JSONParser parser = new JSONParser();
	private static JSONObject currentData;

	public static void addRole(GenericDiscordEvent e, StringBuilder builder, String id, long requirement) {
		List<Member> staff = e.getGuild().getMembersWithRoles(e.getGuild().getRoleById(id));
		builder.append("\n<@&").append(id).append("> - **Required Messages: ").append(requirement).append("**\n");
		for(Member member : staff) {
			if(member.getId().equals("619989388109152256") || member.getId().equals("846097308504293393")) {
				continue;
			}
			builder.append(member.getNickname()).append(" `").append(member.getId()).append("`: ");
			currentData = DatabaseManager.getDataById("Leveling Data", member.getId());
			if(currentData == null) {
				continue;
			}
			try {
				long messages = (long) currentData.get("messages");
				if(messages < 0.8 * requirement) {
					builder.append(messages).append(" messages :x:\n");
				} else if(messages < requirement) {
					builder.append(messages).append(" messages :warning:\n");
				} else {
					builder.append(messages).append(" messages :white_check_mark:\n");
				}
			} catch(Exception exception) {
				builder.append("0 messages :x:\n");
			}
		}
	}

	public static void staffReport(GenericDiscordEvent e) {
		StringBuilder builder = new StringBuilder();
		addRole(e, builder, "734697396126351390", 150);
		addRole(e, builder, "734697394893094934", 180);
		addRole(e, builder, "734697390556184606", 240);
		currentData = DatabaseManager.getDataById("Leveling Data", "619989388109152256");
		if(currentData == null) {
			e.getMessage().reply("I am bad.  This is an Error.  Aborting Process...").queue();
			return;
		}
		builder.append("\n<@&734697389126058055>\nStradivarius Violin#6156 `619989388109152256`: ").append(currentData.get("messages")).append(" messages");
		EmbedBuilder embed = new EmbedBuilder()
				.setColor(Color.BLUE)
				.setTitle("**__Staff Message Report__**")
				.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
				.addField("**Current Staff Messages**", builder.toString(), false);
		e.replyEmbeds(embed.build());
	}
}
