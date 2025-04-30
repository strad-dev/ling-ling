package eventListeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
// BEETHOVEN-ONLY CLASS

public class RoleRemoved extends ListenerAdapter {
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
		if(e.getGuild().getId().equals("670725611207262219") && e.getRoles().contains(e.getGuild().getRoleById("852752096733429781"))) {
			JSONObject data = DatabaseManager.getDataById("Economy Data", e.getMember().getId());
			if(data == null) {
				return;
			}
			data.replace("isBooster", false);
			DatabaseManager.saveDataForUser("Economy Data", e.getMember().getId(), data);
		}
	}
}