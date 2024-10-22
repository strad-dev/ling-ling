package economy;

import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONObject;
import processes.DatabaseManager;
import processes.Numbers;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
				result.append("\n").append(Numbers.formatNumber(data.get(key))).deleteCharAt(result.length() - 1)
						.append("/").append(recipe.get(key)).append("`");
			}
		} else {
			result = new StringBuilder("You crafted ").append(Numbers.formatNumber(craftAmount)).append(getEmoji(what)).append(" for...\n");
			for(String key : recipe.keySet()) {
				data.replace(key, (long) data.get(key) - (recipe.get(key) * craftAmount));
				result.append('\n').append(Numbers.formatNumber(recipe.get(key) * craftAmount)).append(getEmoji(key));
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
			default -> {
				return "Error 404 - Yell at the developer for being stupid.";
			}
		}
	}

	public static void craft(GenericDiscordEvent e, String temp, String item) {
		JSONObject data = LoadData.loadData(e);
		if(item.isEmpty()) {
			EmbedBuilder builder = new EmbedBuilder().setTitle("**All Crafting Recipes**")
					.addField("**Rice** " + Emoji.RICE, "`" + data.get("grains") + "/20`" + Emoji.GRAINS +
							"\n`" + data.get("wood") + "/10`" + Emoji.WOOD +
							"\n`" + data.get("water") + "/10`" + Emoji.WATER +
							"\nID: `rice`", true)
					.addField("**Bubble Tea** " + Emoji.TEA, "`" + data.get("plastic") + "/10`" + Emoji.PLASTIC +
							"\n`" + data.get("teaBase") + "/10`" + Emoji.TEABAG +
							"\n`" + data.get("water") + "/20`" + Emoji.WATER +
							"\nID: `tea`", true)
					.addField("**Rosin** " + Emoji.ROSIN, "`" + data.get("pineSap") + "/20`" + Emoji.SAP +
							"\nID: `rosin`", true)
					.addField("**New Strings** " + Emoji.STRING, "`" + data.get("steel") + "/40`" + Emoji.STEEL +
							"\nID: `string`", true)
					.addBlankField(true)
					.addField("**Bow Hair** " + Emoji.BOW_HAIR, "`" + data.get("horseHair") + "/60`" + Emoji.HORSE_HAIR +
							"\nID: `bowHair`", true)
					.addField("**Violin Service** " + Emoji.SERVICE, "`" + data.get("grains") + "/20`" + Emoji.GRAINS +
							"\n`" + data.get("plastic") + "/20`" + Emoji.PLASTIC +
							"\n`" + data.get("water") + "/20`" + Emoji.WATER +
							"\n`" + data.get("teaBase") + "/20`" + Emoji.TEABAG +
							"\n`" + data.get("wood") + "/80`" + Emoji.WOOD +
							"\n`" + data.get("pineSap") + "/20`" + Emoji.SAP +
							"\n`" + data.get("steel") + "/20`" + Emoji.STEEL +
							"\n`" + data.get("horseHair") + "/20`" + Emoji.HORSE_HAIR +
							"\nID: `violinService`", true)
					.addBlankField(true)
					.addField("**1x Luthier** " + Emoji.SERVICE, "`" + data.get("grains") + "/250`" + Emoji.GRAINS +
							"\n`" + data.get("plastic") + "/250`" + Emoji.PLASTIC +
							"\n`" + data.get("water") + "/250`" + Emoji.WATER +
							"\n`" + data.get("teaBase") + "/250`" + Emoji.TEABAG +
							"\n`" + data.get("wood") + "/250`" + Emoji.WOOD +
							"\n`" + data.get("pineSap") + "/250`" + Emoji.SAP +
							"\n`" + data.get("steel") + "/250`" + Emoji.STEEL +
							"\n`" + data.get("horseHair") + "/250`" + Emoji.HORSE_HAIR +
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
			case "bowhair", "bowHair" -> {
				item = "bowHair";
				recipe.put("horseHair", 60L);
			}
			case "violinservice", "violinService" -> {
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

			// TODO finish refactoring when luthier is a consumable item
			case "luthier" -> {
				if(Objects.requireNonNull(e.getGuild()).getId().equals("670725611207262219")) {
					e.reply("Strad find you trying to sneakily mess with the only buffed Luthier around.  " +
							"He kicks you out, and slaps you with a fine equal to 24x your hourly income." +
							"\nDon't try to increase the Luthier of the support server.  It won't work or end well.");
					data.replace("violins", (long) data.get("violins") - (long) data.get("income") * 24);
					SaveData.saveData(e, data);
					return;
				}
				JSONObject luthierData = DatabaseManager.getDataByGuild(e, "Luthier Data");
				if(luthierData == null) {
					e.reply("You look for a luthier shop to donate to, but cannot find any.  Try another server!" +
							"\nAlternatively, ask a Bot Mod/Bot Admin to set up Luthier for you.");
					return;
				}
				long grains = (long) data.get("grains");
				long plastic = (long) data.get("plastic");
				long water = (long) data.get("water");
				long teaBase = (long) data.get("teaBase");
				long wood = (long) data.get("wood");
				long pineSap = (long) data.get("pineSap");
				long steel = (long) data.get("steel");
				long horseHairs = (long) data.get("horseHair");
				long multiplier = (long) luthierData.get("multiplier");
				long i = 0;
				for(; i < craftAmount; i++) {
					if(grains < 250 || plastic < 250 || water < 250 || teaBase < 250 || wood < 250 || pineSap < 250 || steel < 250 || horseHairs < 250) {
						if(i == 0) {
							e.reply("You were unable to craft any Luthiers!  Check that you have enough Raw Materials:" +
									"\n`" + Numbers.formatNumber(grains) + "/250`" + Emoji.GRAINS +
									"\n`" + Numbers.formatNumber(plastic) + "/250`" + Emoji.PLASTIC +
									"\n`" + Numbers.formatNumber(water) + "/250`" + Emoji.WATER +
									"\n`" + Numbers.formatNumber(teaBase) + "/250`" + Emoji.TEABAG +
									"\n`" + Numbers.formatNumber(pineSap) + "/250`" + Emoji.SAP +
									"\n`" + Numbers.formatNumber(wood) + "/250`" + Emoji.WOOD +
									"\n`" + Numbers.formatNumber(steel) + "/250`" + Emoji.STEEL +
									"\n`" + Numbers.formatNumber(horseHairs) + "/250`" + Emoji.HORSE_HAIR);
							return;
						}
						break;
					}
					grains -= 250;
					plastic -= 250;
					water -= 250;
					teaBase -= 250;
					wood -= 250;
					pineSap -= 250;
					steel -= 250;
					horseHairs -= 250;
					multiplier++;
				}
				data.replace("grains", grains);
				data.replace("plastic", plastic);
				data.replace("water", water);
				data.replace("teaBase", teaBase);
				data.replace("wood", wood);
				data.replace("pineSap", pineSap);
				data.replace("steel", steel);
				data.replace("horseHair", horseHairs);
				luthierData.replace("multiplier", multiplier);
				e.reply("You crafted and used `" + Numbers.formatNumber(i) + "`x Luthier for `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.GRAINS + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.PLASTIC + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.WATER + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.TEABAG + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.WOOD + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.SAP + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.STEEL + ", `" +
						Numbers.formatNumber(i * 250) + "`" + Emoji.HORSE_HAIR);
				Objects.requireNonNull(e.getGuild().getTextChannelById((String) luthierData.get("channel")))
						.sendMessage("**:tada: <@" + e.getAuthor().getId() + "> just buffed this server's luthier by " + Numbers.formatNumber(i) + "x!  New Multiplier: " + Numbers.formatNumber(multiplier) + "x** :tada:");
				EmbedBuilder builder = new EmbedBuilder()
						.setFooter("Ling Ling Bot", e.getJDA().getSelfUser().getAvatarUrl())
						.setColor(Color.BLUE)
						.setTitle("**__Luthier Crafted__**")
						.addField("Crafter: " + data.get("discordName") + " `" + e.getAuthor().getId() + "`", "Server: " + e.getGuild().getName() + " `" + e.getGuild().getId() + "`" + "\nAmount Crafted: " + Numbers.formatNumber(i), false);
				DatabaseManager.saveDataByGuild(e, "Luthier Data", luthierData);
				return;
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
