package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.Utils;

import java.util.HashMap;
import java.util.Map;

public class Craft {
	private static void craftItems(GenericDiscordEvent e, JSONObject data, String what, Map<String, Long> recipe, long craftAmount) {
		for(String key : recipe.keySet()) {
			long craftable = (long) data.get(key) / recipe.get(key);
			if(craftable < craftAmount) {
				craftAmount = craftable;
			}
		}

		StringBuilder result;
		if(craftAmount == 0) {
			result = new StringBuilder("You did not have enough materials to craft any ").append(getEmoji(what)).append("  Check that you have enough Raw Materials...\n");
			for(String key : recipe.keySet()) {
				result.append("\n").append(Utils.formatNumber(data.get(key)))
						.append("/").append(Utils.formatNumber(recipe.get(key)))
						.append(" ").append(getEmoji(key));
			}
		} else {
			result = new StringBuilder("You crafted ").append(Utils.formatNumber(craftAmount)).append(getEmoji(what)).append(" for...\n");
			for(String key : recipe.keySet()) {
				data.replace(key, (long) data.get(key) - (recipe.get(key) * craftAmount));
				result.append('\n').append(Utils.formatNumber(recipe.get(key) * craftAmount)).append(getEmoji(key)).append(" ");
			}
			data.replace(what, (long) data.get(what) + craftAmount);
		}
		e.reply(result.toString());
	}

	private static String getEmoji(String key) {
		switch(key) {
			case "grains" -> {
				return Emoji.GRAINS;
			}
			case "plastic" -> {
				return Emoji.PLASTIC;
			}
			case "water" -> {
				return Emoji.WATER;
			}
			case "teaBase" -> {
				return Emoji.TEABAG;
			}
			case "wood" -> {
				return Emoji.WOOD;
			}
			case "pineSap" -> {
				return Emoji.SAP;
			}
			case "steel" -> {
				return Emoji.STEEL;
			}
			case "horseHair" -> {
				return Emoji.HORSE_HAIR;
			}
			case "rice" -> {
				return Emoji.RICE;
			}
			case "tea" -> {
				return Emoji.TEA;
			}
			case "rosin" -> {
				return Emoji.ROSIN;
			}
			case "string" -> {
				return Emoji.STRING;
			}
			case "bowHair" -> {
				return Emoji.BOW_HAIR;
			}
			case "violinService" -> {
				return Emoji.SERVICE;
			}
			case "luthierBalance" -> {
				return "Luthier Multipliers.";
			}
			case "essence" -> {
				return ":sparkles:";
			}
			default -> {
				return "Error 404 - Yell at the developer for being stupid.";
			}
		}
	}

	public static void craft(GenericDiscordEvent e, String temp, String item) {
		JSONObject data = LoadData.loadData(e);
		if(item.isEmpty()) {
			EmbedBuilder builder = new EmbedBuilder().setTitle("**All Crafting Recipes**")
					.addField("**Rice** " + Emoji.RICE, Utils.formatNumber(data.get("grains")) + "/`20`" + Emoji.GRAINS +
							"\n" + Utils.formatNumber(data.get("wood")) + "/`10`" + Emoji.WOOD +
							"\n" + Utils.formatNumber(data.get("water")) + "/`10`" + Emoji.WATER +
							"\nID: `rice`", true)
					.addField("**Bubble Tea** " + Emoji.TEA, Utils.formatNumber(data.get("plastic")) + "/`10`" + Emoji.PLASTIC +
							"\n" + Utils.formatNumber(data.get("teaBase")) + "/`10`" + Emoji.TEABAG +
							"\n" + Utils.formatNumber(data.get("water")) + "/`20`" + Emoji.WATER +
							"\nID: `tea`", true)
					.addField("**Rosin** " + Emoji.ROSIN, Utils.formatNumber(data.get("pineSap")) + "/`20`" + Emoji.SAP +
							"\nID: `rosin`", true)
					.addField("**New Strings** " + Emoji.STRING, Utils.formatNumber(data.get("steel")) + "/`40`" + Emoji.STEEL +
							"\nID: `string`", true)
					.addBlankField(true)
					.addField("**Bow Hair** " + Emoji.BOW_HAIR, Utils.formatNumber(data.get("horseHair")) + "/`60`" + Emoji.HORSE_HAIR +
							"\nID: `bowHair`", true)
					.addField("**Violin Service** " + Emoji.SERVICE, Utils.formatNumber(data.get("grains")) + "/`20`" + Emoji.GRAINS +
							"\n" + Utils.formatNumber(data.get("plastic")) + "/`20`" + Emoji.PLASTIC +
							"\n" + Utils.formatNumber(data.get("water")) + "/`20`" + Emoji.WATER +
							"\n" + Utils.formatNumber(data.get("teaBase")) + "/`20`" + Emoji.TEABAG +
							"\n" + Utils.formatNumber(data.get("wood")) + "/`80`" + Emoji.WOOD +
							"\n" + Utils.formatNumber(data.get("pineSap")) + "/`20`" + Emoji.SAP +
							"\n" + Utils.formatNumber(data.get("steel")) + "/`20`" + Emoji.STEEL +
							"\n" + Utils.formatNumber(data.get("horseHair")) + "/`20`" + Emoji.HORSE_HAIR +
							"\nID: `violinService`", true)
					.addBlankField(true)
					.addField("**1x Luthier**", Utils.formatNumber(data.get("grains")) + "/`1000`" + Emoji.GRAINS +
							"\n" + Utils.formatNumber(data.get("plastic")) + "/`1 000`" + Emoji.PLASTIC +
							"\n" + Utils.formatNumber(data.get("water")) + "/`1 000`" + Emoji.WATER +
							"\n" + Utils.formatNumber(data.get("teaBase")) + "/`1 000`" + Emoji.TEABAG +
							"\n" + Utils.formatNumber(data.get("wood")) + "/`1 000`" + Emoji.WOOD +
							"\n" + Utils.formatNumber(data.get("pineSap")) + "/`1 000`" + Emoji.SAP +
							"\n" + Utils.formatNumber(data.get("steel")) + "/`1 000`" + Emoji.STEEL +
							"\n" + Utils.formatNumber(data.get("horseHair")) + "/`1 000`" + Emoji.HORSE_HAIR +
							"\n" + Utils.formatNumber(data.get("essence")) + "/`2 500`:sparkles:" +
							"\nID: `luthier`", true);
			e.replyEmbeds(builder.build());
			return;
		}
		long craftAmount;
		try {
			craftAmount = Long.parseLong(temp);
		} catch(Exception exception) {
			if(temp.equals("max")) {
				craftAmount = 2147483647;
			} else {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
		}
		if(craftAmount < 1) {
			e.reply("You can't craft a negative amount of items.  Grow a brain.");
			return;
		}
		Map<String, Long> recipe = new HashMap<>();
		switch(item) {
			case "rice" -> {
				recipe.put("grains", 20L);
				recipe.put("wood", 10L);
				recipe.put("water", 10L);
			}
			case "tea" -> {
				recipe.put("plastic", 10L);
				recipe.put("teaBase", 10L);
				recipe.put("water", 20L);
			}
			case "rosin" -> {
				recipe.put("pineSap", 20L);
			}
			case "string" -> {
				recipe.put("steel", 40L);
			}
			case "hair", "bowhair", "bowHair" -> {
				item = "bowHair";
				recipe.put("horseHair", 60L);
			}
			case "service", "violinservice", "violinService" -> {
				item = "violinService";
				recipe.put("grains", 20L);
				recipe.put("plastic", 20L);
				recipe.put("water", 20L);
				recipe.put("teaBase", 20L);
				recipe.put("wood", 80L);
				recipe.put("pineSap", 20L);
				recipe.put("steel", 20L);
				recipe.put("horseHair", 20L);
			}
			case "luthier" -> {
				item = "luthierBalance";
				recipe.put("grains", 1000L);
				recipe.put("plastic", 1000L);
				recipe.put("water", 1000L);
				recipe.put("teaBase", 1000L);
				recipe.put("wood", 1000L);
				recipe.put("pineSap", 1000L);
				recipe.put("steel", 1000L);
				recipe.put("horseHair", 1000L);
				recipe.put("essence", 2500L);
			}
			default -> {
				e.reply("This crafting recipe does not exist!  Run `/craft` with no arguments to see all recipes.");
				return;
			}
		}
		craftItems(e, data, item, recipe, craftAmount);
		SaveData.saveData(e, data);
	}
}
