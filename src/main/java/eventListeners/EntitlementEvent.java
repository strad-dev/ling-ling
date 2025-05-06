package eventListeners;

import net.dv8tion.jda.api.events.entitlement.EntitlementCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import processes.DatabaseManager;

public class EntitlementEvent extends ListenerAdapter {
	@Override
	public void onEntitlementCreate(EntitlementCreateEvent e) {
		String id = e.getEntitlement().getUserId();
		DatabaseManager.getDataById("Economy Data", id);
		if(e.getEntitlement().getId().equals("1263695170365296712")) {

		}
	}
}