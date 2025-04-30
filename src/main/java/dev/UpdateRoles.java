package dev;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

import java.util.List;

public class UpdateRoles {
	public static void updateRoles(GenericDiscordEvent e) {
		Guild g = e.getGuild();
		if(g.getId().equals("670725611207262219")) {
			List<Member> list = g.getMembers();
			for(Member member : list) {
				List<Role> list2 = member.getRoles();
				JSONObject data = DatabaseManager.getDataById("Economy Data", member.getId());
				if(data == null) {
					continue;
				}
				if(list2.contains(g.getRoleById("852752096733429781"))) {
					data.replace("isBooster", true);
				}
				if(list2.contains(g.getRoleById("734697410273607751"))) {
					data.replace("serverLevel", 1.25);
				} else if(list2.contains(g.getRoleById("845121274958184499"))) {
					data.replace("serverLevel", 1.20);
				} else if(list2.contains(g.getRoleById("845121187741958166"))) {
					data.replace("serverLevel", 1.15);
				} else if(list2.contains(g.getRoleById("734697411074719765"))) {
					data.replace("serverLevel", 1.11);
				} else if(list2.contains(g.getRoleById("734697411783688245"))) {
					data.replace("serverLevel", 1.075);
				} else if(list2.contains(g.getRoleById("734697412865818645"))) {
					data.replace("serverLevel", 1.045);
				} else if(list2.contains(g.getRoleById("734697413901680691"))) {
					data.replace("serverLevel", 1.02);
				} else {
					data.replace("serverLevel", 1);
				}
				DatabaseManager.saveDataForUser("Economy Data", member.getId(), data);
			}
			e.reply("Successfully force-updated role multipliers!");
		} else {
			e.reply("You can't use this command here!  Go to the support server stupid.");
		}
	}
}