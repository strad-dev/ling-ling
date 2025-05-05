package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.DatabaseManager;

public class SaveData {
	/**
	 * Saves the Economy Data for the user that ran a command.  This is an alias for DatabaseManager.saveDataByUser(GenericDiscordEvent, "Economy Data", JSONObject)
	 * @param e The GenericDiscordEvent that determines the user.
	 * @param data The new data to save.
	 */
	public static void saveData(GenericDiscordEvent e, JSONObject data) {
		Achievement.calculateAchievement(e, data, "earnings", "Moneymaker");
		Achievement.calculateAchievement(e, data, "moneyEarned", "Entrepeneur");
		DatabaseManager.saveDataByUser(e, "Economy Data", data);
	}
}