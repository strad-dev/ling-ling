package eventListeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
// BEETHOVEN-ONLY CLASS

public class RoleAdded extends ListenerAdapter {
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
		if(e.getGuild().getId().equals("670725611207262219") && !e.getMember().getUser().isBot()) {
			JSONObject data = DatabaseManager.getDataById("Economy Data", e.getMember().getId());
			if(data == null) {
				return;
			}
			if(e.getRoles().contains(e.getGuild().getRoleById("852752096733429781"))) {
				data.replace("isBooster", true);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("734697413901680691"))) {
				data.replace("serverLevel", 1.02);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("734697412865818645"))) {
				data.replace("serverLevel", 1.045);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("734697411783688245"))) {
				data.replace("serverLevel", 1.075);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("734697411074719765"))) {
				data.replace("serverLevel", 1.11);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("845121187741958166"))) {
				data.replace("serverLevel", 1.15);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("845121274958184499"))) {
				data.replace("serverLevel", 1.20);
			} else if(e.getRoles().contains(e.getGuild().getRoleById("734697410273607751"))) {
				data.replace("serverLevel", 1.25);
			}
			DatabaseManager.saveDataById("Economy Data", e.getMember().getId(), data);
		}
	}
}