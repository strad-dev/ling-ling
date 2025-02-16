package regular;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.http.HypixelHttpClient;
import net.hypixel.api.reply.skyblock.SkyBlockProfilesReply;
import processes.DatabaseManager;
import processes.HypixelManager;
import processes.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Baldness {
	public static void baldness(GenericDiscordEvent e, String playerName, String fruit) {
		try {
			String uuid = HypixelManager.getMojang().getUUIDOfUsername(playerName);
			HypixelHttpClient client = HypixelManager.getClient();
			HypixelAPI api = new HypixelAPI(client);
			SkyBlockProfilesReply reply = api.getSkyBlockProfiles(uuid).get();
			JsonArray object = reply.getProfiles();
			JsonObject profile = null;
			String profileUUID = "";
			boolean ironman = false;
			if(fruit.isEmpty()) {
				for(int i = 0; i < object.size(); i++) {
					profile = object.get(i).getAsJsonObject();
					if(profile.get("selected").getAsBoolean()) {
						if(profile.has("game_mode")) {
							if(profile.get("game_mode").getAsString().equals("ironman")) {
								ironman = true;
							}
						}
						profileUUID = profile.get("profile_id").getAsString();
						profile = profile.getAsJsonObject("members").getAsJsonObject(uuid);
						break;
					}
				}
			} else {
				for(int i = 0; i < object.size(); i++) {
					profile = object.get(i).getAsJsonObject();
					if(profile.get("cute_name").getAsString().equalsIgnoreCase(fruit)) {
						profileUUID = profile.get("profile_id").getAsString();
						profile = profile.getAsJsonObject("members").getAsJsonObject(uuid);
						break;
					}
				}
				if(profileUUID.isEmpty()) {
					e.reply("This profile does not exist for this user!");
					return;
				}
			}
			if(profile == null) {
				e.reply("An error occured!  Check that you typed the Minecraft name correctly.");
				return;
			}

			// noinspection deprecation
			URL url = new URL("https://api.hypixel.net/v2/skyblock/museum?key=" + DatabaseManager.getMiscData().get("hypixelKey") + "&profile=" + profileUUID);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JsonObject museum = JsonParser.parseString(String.valueOf(response)).getAsJsonObject().getAsJsonObject("members").getAsJsonObject(uuid);

			// noinspection deprecation
			url = new URL("https://api.hypixel.net/v2/skyblock/garden?key=" + DatabaseManager.getMiscData().get("hypixelKey") + "&profile=" + profileUUID);
			connection = url.openConnection();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			response = new StringBuilder();

			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JsonObject garden = JsonParser.parseString(String.valueOf(response)).getAsJsonObject().getAsJsonObject("garden");


			EmbedBuilder builder = new EmbedBuilder()
					.setColor(Color.BLUE)
					.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
					.setTitle("Baldness Factor for " + playerName);
			double baldness = 0.0;

			boolean isBaldHunter = playerName.equalsIgnoreCase("GodHunter775");

			/*
				██╗     ███████╗██╗   ██╗███████╗██╗
				██║     ██╔════╝██║   ██║██╔════╝██║
				██║     █████╗  ██║   ██║█████╗  ██║
				██║     ██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║
				███████╗███████╗ ╚████╔╝ ███████╗███████╗
				╚══════╝╚══════╝  ╚═══╝  ╚══════╝╚══════╝
			 */

			long level = profile.getAsJsonObject("leveling").get("experience").getAsLong() / 100;
			if(level < 462) {
				double toAdd = (462 - level) * 0.002;
				builder.addField("**__SkyBlock Level__**", "**Baldness for SkyBlock Level**: " + toAdd, false);
			}

			/*
				███████╗██╗  ██╗██╗██╗     ██╗     ███████╗
				██╔════╝██║ ██╔╝██║██║     ██║     ██╔════╝
				███████╗█████╔╝ ██║██║     ██║     ███████╗
				╚════██║██╔═██╗ ██║██║     ██║     ╚════██║
				███████║██║  ██╗██║███████╗███████╗███████║
				╚══════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
			 */

			String causes = "";
			double thisBaldness = 0.0;

			JsonObject skills;
			try {
				skills = profile.getAsJsonObject("player_data").getAsJsonObject("experience");
				long combatLevel = Utils.skillLevel(skills.get("SKILL_COMBAT").getAsLong());
				long farmingLevel = Utils.skillLevel(skills.get("SKILL_FARMING").getAsLong());
				long fishingLevel = Utils.skillLevel(skills.get("SKILL_FISHING").getAsLong());
				long miningLevel = Utils.skillLevel(skills.get("SKILL_MINING").getAsLong());
				long foragingLevel = Utils.skillLevel(skills.get("SKILL_FORAGING").getAsLong());
				long enchantingLevel = Utils.skillLevel(skills.get("SKILL_ENCHANTING").getAsLong());
				long alchemyLevel = Utils.skillLevel(skills.get("SKILL_ALCHEMY").getAsLong());
				long carpentryLevel = Utils.skillLevel(skills.get("SKILL_CARPENTRY").getAsLong());
				long tamingLevel = Utils.skillLevel(skills.get("SKILL_TAMING").getAsLong());

				if(combatLevel < 60) {
					double result = (60 - combatLevel) * 0.002;
					causes += "Combat Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(farmingLevel < 60) {
					double result = (60 - farmingLevel) * 0.002;
					causes += "Farming Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(fishingLevel < 50) {
					double result = (50 - fishingLevel) * 0.002;
					causes += "Fishing Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(miningLevel < 60) {
					double result = (60 - miningLevel) * 0.002;
					causes += "Mining Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(foragingLevel < 50) {
					double result = (50 - foragingLevel) * 0.002;
					causes += "F*raging Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(enchantingLevel < 60) {
					double result = (60 - enchantingLevel) * 0.002;
					causes += "Enchanting Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(alchemyLevel < 50) {
					double result = (50 - alchemyLevel) * 0.002;
					causes += "Alchemy Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(carpentryLevel < 50) {
					double result = (50 - carpentryLevel) * 0.002;
					causes += "Carpentry Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(tamingLevel < 60) {
					double result = (60 - tamingLevel) * 0.002;
					causes += "Taming Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(thisBaldness > 0) {
					builder.addField("**__Skills__**", causes + "**Final Baldness for Skills**: " + thisBaldness, false);
					baldness += thisBaldness;
				}
			} catch(Exception exception) {
				builder.addField("**__Skills__**", "API Off: +10\n**Final Baldness for Skills**: 10", false);
				baldness += 10;
			}

			/*
				██████╗ ██╗   ██╗███╗   ██╗ ██████╗ ███████╗ ██████╗ ███╗   ██╗███████╗
				██╔══██╗██║   ██║████╗  ██║██╔════╝ ██╔════╝██╔═══██╗████╗  ██║██╔════╝
				██║  ██║██║   ██║██╔██╗ ██║██║  ███╗█████╗  ██║   ██║██╔██╗ ██║███████╗
				██║  ██║██║   ██║██║╚██╗██║██║   ██║██╔══╝  ██║   ██║██║╚██╗██║╚════██║
				██████╔╝╚██████╔╝██║ ╚████║╚██████╔╝███████╗╚██████╔╝██║ ╚████║███████║
				╚═════╝  ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝
			 */

			causes = "";
			thisBaldness = 0.0;

			try {
				JsonObject cata = profile.getAsJsonObject("dungeons").getAsJsonObject("dungeon_types");
				long cataLevel = Utils.cataLevel(cata.getAsJsonObject("catacombs").get("experience").getAsLong());
				if(cataLevel < 50) {
					double result = (50 - cataLevel) * 0.03;
					causes += "Cata Level: +" + result + "\n";
					thisBaldness += result;
				}

				JsonObject classes = profile.getAsJsonObject("dungeons").getAsJsonObject("player_classes");
				long healLevel = Utils.cataLevel(classes.getAsJsonObject("healer").get("experience").getAsLong());
				long mageLevel = Utils.cataLevel(classes.getAsJsonObject("mage").get("experience").getAsLong());
				long bersLevel = Utils.cataLevel(classes.getAsJsonObject("berserk").get("experience").getAsLong());
				long archLevel = Utils.cataLevel(classes.getAsJsonObject("archer").get("experience").getAsLong());
				long tankLevel = Utils.cataLevel(classes.getAsJsonObject("tank").get("experience").getAsLong());

				if(healLevel < 50) {
					double result = (50 - healLevel) * 0.004;
					causes += "Healer Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(mageLevel < 50) {
					double result = (50 - mageLevel) * 0.004;
					causes += "Mage Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(bersLevel < 50) {
					double result = (50 - bersLevel) * 0.004;
					causes += "Berserk Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(archLevel < 50) {
					double result = (50 - archLevel) * 0.004;
					causes += "Archer Level: +" + result + "\n";
					thisBaldness += result;
				}

				if(tankLevel < 50) {
					double result = (50 - tankLevel) * 0.004;
					causes += "Tank Level: +" + result + "\n";
					thisBaldness += result;
				}

				try {
					long m7PB = ((long) cata.getAsJsonObject("master_catacombs").getAsJsonObject("fastest_time").get("7").getAsDouble()) / 1000;
					if(m7PB > 360) {
						double result = (m7PB - 360) * 0.005;
						if(result < 0) {
							result = 0;
						} else if(result > 1.2) {
							result = 1.2;
						}
						causes += "Bad M7 PB: +" + result + "\n";
						thisBaldness += result;
					}
				} catch(Exception exception) {
					causes += "Bad M7 PB: +" + 1.2 + "\n";
					thisBaldness += 1.2;
				}
			} catch(Exception exception) {
				causes += "Never Played Dungeons: +100\n";
				thisBaldness += 100;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Dungeons__**", causes + "**Final Baldness for Dungeons**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			/*
				 ██████╗ ██████╗ ███╗   ███╗██████╗  █████╗ ████████╗
				██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝
				██║     ██║   ██║██╔████╔██║██████╔╝███████║   ██║
				██║     ██║   ██║██║╚██╔╝██║██╔══██╗██╔══██║   ██║
				╚██████╗╚██████╔╝██║ ╚═╝ ██║██████╔╝██║  ██║   ██║
				 ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═════╝ ╚═╝  ╚═╝   ╚═╝
			 */

			causes = "";
			thisBaldness = 0.0;

			JsonObject slayers = profile.getAsJsonObject("slayer").getAsJsonObject("slayer_bosses");
			long zombieLevel;
			try {
				zombieLevel = Utils.slayerLevel(slayers.getAsJsonObject("zombie").get("xp").getAsLong());
			} catch(Exception exception) {
				zombieLevel = 0;
			}
			long spiderLevel;
			try {
				spiderLevel = Utils.slayerLevel(slayers.getAsJsonObject("spider").get("xp").getAsLong());
			} catch(Exception exception) {
				spiderLevel = 0;
			}
			long wolfLevel;
			try {
				wolfLevel = Utils.slayerLevel(slayers.getAsJsonObject("wolf").get("xp").getAsLong());
			} catch(Exception exception) {
				wolfLevel = 0;
			}
			long emanLevel;
			try {
				emanLevel = Utils.slayerLevel(slayers.getAsJsonObject("enderman").get("xp").getAsLong());
			} catch(Exception exception) {
				emanLevel = 0;
			}

			long blazeLevel;
			try {
				blazeLevel = Utils.slayerLevel(slayers.getAsJsonObject("blaze").get("xp").getAsLong());
			} catch(Exception exception) {
				blazeLevel = 0;
			}

			long vampLevel;
			try {
				vampLevel = Math.min(5, Utils.skillLevel(slayers.getAsJsonObject("vampire").get("xp").getAsLong() / 2)); // inaccurate but it works
			} catch(Exception exception) {
				vampLevel = 0;
			}
			if(zombieLevel < 9) {
				double result = (9 - zombieLevel) * 0.02;
				causes += "Zombie Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(spiderLevel < 9) {
				double result = (9 - spiderLevel) * 0.02;
				causes += "Spider Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(wolfLevel < 9) {
				double result = (9 - wolfLevel) * 0.02;
				causes += "Wolf Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(emanLevel < 9) {
				double result = (9 - emanLevel) * 0.02;
				causes += "Enderman Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(blazeLevel < 9) {
				double result = (9 - blazeLevel) * 0.02;
				causes += "Blaze Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(vampLevel < 5) {
				double result = (5 - vampLevel) * 0.02;
				causes += "Vampire Slayer Level: +" + result + "\n";
				thisBaldness += result;
			}

			long milestone;
			try {
				milestone = profile.getAsJsonObject("bestiary").getAsJsonObject("milestone").get("last_claimed_milestone").getAsLong();
			} catch(Exception exception) {
				milestone = 0;
			}

			if(milestone < 339) {
				double result = (339 - milestone) * 0.0025;
				causes += "Bestiary Milestone: +" + result + "\n";
				thisBaldness += result;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Combat__**", causes + "**Final Baldness for Combat**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			/*
				 ██████╗ ██████╗ ██╗     ██╗     ███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗███████╗
				██╔════╝██╔═══██╗██║     ██║     ██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║██╔════╝
				██║     ██║   ██║██║     ██║     █████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║███████╗
				██║     ██║   ██║██║     ██║     ██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║╚════██║
				╚██████╗╚██████╔╝███████╗███████╗███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║███████║
				 ╚═════╝ ╚═════╝ ╚══════╝╚══════╝╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝
			 */

			causes = "";
			thisBaldness = 0.0;

			try {
				JsonObject collections = profile.getAsJsonObject("collection");
				try {
					if(collections.get("CACTUS").getAsLong() < 50000) {
						causes += "Cactus Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("CARROT_ITEM").getAsLong() < 100000) {
						causes += "Carrot Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("INK_SACK:3").getAsLong() < 100000) {
						causes += "Cocoa Beans Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("FEATHER").getAsLong() < 50000) {
						causes += "Feather Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LEATHER").getAsLong() < 100000) {
						causes += "Leather Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MELON").getAsLong() < 250000) {
						causes += "Melon Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MUSHROOM_COLLECTION").getAsLong() < 50000) {
						causes += "Mushroom Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MUTTON").getAsLong() < 100000) {
						causes += "Mutton Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("NETHER_STALK").getAsLong() < 250000) {
						causes += "Nether Wart Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("POTATO_ITEM").getAsLong() < 100000) {
						causes += "Potato Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("PUMPKIN").getAsLong() < 250000) {
						causes += "Pumpkin Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RAW_CHICKEN").getAsLong() < 100000) {
						causes += "Chicken Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("PORK").getAsLong() < 50000) {
						causes += "Pork Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RABBIT").getAsLong() < 50000) {
						causes += "Rabbit Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SEEDS").getAsLong() < 25000) {
						causes += "Seeds Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SUGAR_CANE").getAsLong() < 50000) {
						causes += "Cane Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("WHEAT").getAsLong() < 100000) {
						causes += "Wheat Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("COAL").getAsLong() < 100000) {
						causes += "Coal Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("COBBLESTONE").getAsLong() < 70000) {
						causes += "Cobblestone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("DIAMOND").getAsLong() < 50000) {
						causes += "Diamond Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("EMERALD").getAsLong() < 100000) {
						causes += "Emerald Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("ENDER_STONE").getAsLong() < 50000) {
						causes += "End Stone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("GEMSTONE_COLLECTION").getAsLong() < 2000000) {
						causes += "Gemstone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("GLOWSTONE_DUST").getAsLong() < 25000) {
						causes += "Glowstone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("GRAVEL").getAsLong() < 50000) {
						causes += "Gravel Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("HARD_STONE").getAsLong() < 1000000) {
						causes += "Hard Stone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("ICE").getAsLong() < 500000) {
						causes += "Ice Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("IRON_INGOT").getAsLong() < 400000) {
						causes += "Iron Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("INK_SACK:4").getAsLong() < 250000) {
						causes += "Lapis Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MITHRIL_ORE").getAsLong() < 1000000) {
						causes += "Mithril Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MYCEL").getAsLong() < 100000) {
						causes += "Mycelium Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("QUARTZ").getAsLong() < 50000) {
						causes += "Quartz Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("NETHERRACK").getAsLong() < 5000) {
						causes += "Netherrack Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("OBSIDIAN").getAsLong() < 100000) {
						causes += "Obsidian Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SAND:1").getAsLong() < 100000) {
						causes += "Red Sand Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("REDSTONE").getAsLong() < 1400000) {
						causes += "Redstone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SAND").getAsLong() < 5000) {
						causes += "Sand Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("BLAZE_ROD").getAsLong() < 50000) {
						causes += "Blaze Rod Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("BONE").getAsLong() < 150000) {
						causes += "Bone Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("ENDER_PEARL").getAsLong() < 50000) {
						causes += "Ender Pearl Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("GHAST_TEAR").getAsLong() < 25000) {
						causes += "Ghast Tear Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SULPHUR").getAsLong() < 50000) {
						causes += "Gunpowder Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MAGMA_CREAM").getAsLong() < 50000) {
						causes += "Magma Cream Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("ROTTEN_FLESH").getAsLong() < 100000) {
						causes += "Rotten Flesh Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SLIME_BALL").getAsLong() < 50000) {
						causes += "Slimeball Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("STRING").getAsLong() < 50000) {
						causes += "String Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG_2").getAsLong() < 25000) {
						causes += "Acacia Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG:2").getAsLong() < 50000) {
						causes += "Birch Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG_2:1").getAsLong() < 25000) {
						causes += "Dark Oak Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG:3").getAsLong() < 25000) {
						causes += "Jungle Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG").getAsLong() < 30000) {
						causes += "Oak Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("LOG:1").getAsLong() < 50000) {
						causes += "Spruce Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("CLAY_BALL").getAsLong() < 2500) {
						causes += "Clay Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RAW_FISH:2").getAsLong() < 4000) {
						causes += "Clownfish Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("INK_SACK").getAsLong() < 4000) {
						causes += "Ink Sack Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("WATER_LILY").getAsLong() < 10000) {
						causes += "Lily Pad Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("MAGMA_FISH").getAsLong() < 500000) {
						causes += "Magmafish Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("PRISMARINE_CRYSTALS").getAsLong() < 800) {
						causes += "Prismarine Crystal Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("PRISMARINE_SHARD").getAsLong() < 800) {
						causes += "Prismarine Shard Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RAW_FISH:3").getAsLong() < 18000) {
						causes += "Pufferfish Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RAW_FISH").getAsLong() < 60000) {
						causes += "Raw Fish Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("RAW_FISH:1").getAsLong() < 10000) {
						causes += "Salmon Collection: +0.015\n";
						thisBaldness += 0.015;
					}

					if(collections.get("SPONGE").getAsLong() < 4000) {
						causes += "Sponge Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "An Error Occured!  Very Bald: +0.15\n";
					thisBaldness += 0.15;
				}

				try {
					if(collections.get("GLACITE").getAsLong() < 1000000) {
						causes += "Glacite Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Glacite Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("SULPHUR_ORE").getAsLong() < 100000) {
						causes += "Sulphur Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Sulphur Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("TUNGSTEN").getAsLong() < 1000000) {
						causes += "Tungsten Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Tungsten Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("UMBER").getAsLong() < 1000000) {
						causes += "Umber Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Umber Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("CHILI_PEPPER").getAsLong() < 20000) {
						causes += "Chili Pepper Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Chili Pepper Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("AGARICUS_CAP").getAsLong() < 200) {
						causes += "Agaricus Cap Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Agaricus Cap Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("CADUCOUS_STEM").getAsLong() < 500) {
						causes += "Caducous Stem Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Caducous Stem Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("HALF_EATEN_CARROT").getAsLong() < 3500) {
						causes += "Half-Eaten Carrot Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Half-Eaten Carrot Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("HEMOVIBE").getAsLong() < 400000) {
						causes += "Hemovibe Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Hemovibe Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("METAL_HEART").getAsLong() < 100) {
						causes += "Living Metal Heart Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Living Metal Heart Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					if(collections.get("WILTED_BERBERIS").getAsLong() < 400) {
						causes += "Wilted Berberis Collection: +0.015\n";
						thisBaldness += 0.015;
					}
				} catch(Exception exception) {
					causes += "Wilted Berberis Collection: +0.015\n";
					thisBaldness += 0.015;
				}

				try {
					long gold = collections.get("GOLD_INGOT").getAsLong();
					if(gold < 100000000) {
						if(gold < 25000) {
							causes += "Gold Collection: +0.015\n";
							thisBaldness += 0.015;
						}
						double result = 1 - (Math.sqrt(2 * gold) / (Math.sqrt(2) * 10000));
						causes += "Bad Gold Collection: + " + result + "\n";
						thisBaldness += result;
					}
				} catch(Exception exception) {
					causes += "Terrible Gold Collection: +1.015\n";
					thisBaldness += 1.015;
				}

				if(thisBaldness > 0) {
					try {
						builder.addField("**__Collections__**", causes + "**Final Baldness for Collections**: " + thisBaldness, false);
					} catch(Exception exception) {
						builder.addField("**__Collections__**", "Too Many Unmaxed Collections\n**Final Baldness for Collections**: " + thisBaldness, false);
					}
					baldness += thisBaldness;
				}
			} catch(Exception exception) {
				builder.addField("**__Collections__**", "API Off: +10\n**Final Baldness for Collections**: 10", false);
				baldness += 10;
			}

			/*
				███╗   ███╗██╗███╗   ██╗██╗ ██████╗ ███╗   ██╗███████╗
				████╗ ████║██║████╗  ██║██║██╔═══██╗████╗  ██║██╔════╝
				██╔████╔██║██║██╔██╗ ██║██║██║   ██║██╔██╗ ██║███████╗
				██║╚██╔╝██║██║██║╚██╗██║██║██║   ██║██║╚██╗██║╚════██║
				██║ ╚═╝ ██║██║██║ ╚████║██║╚██████╔╝██║ ╚████║███████║
				╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝
			 */

			causes = "";
			thisBaldness = 0.0;

			String minions;
			try {
				minions = profile.getAsJsonObject("player_data").getAsJsonArray("crafted_generators").toString();
			} catch(Exception exception) {
				minions = "";
			}

			if(!minions.contains("COBBLESTONE_12")) {
				causes += "Cobblestone Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("OBSIDIAN_12")) {
				causes += "Obsidian Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("GLOWSTONE_12")) {
				causes += "Glowstone Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("GRAVEL_11")) {
				causes += "Gravel Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SAND_11")) {
				causes += "Sand Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("RED_SAND_12")) {
				causes += "Red Sand Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("MYCELIUM_12")) {
				causes += "Mycelium Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CLAY_11")) {
				causes += "Clay Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("ICE_12")) {
				causes += "Ice Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SNOW_12")) {
				causes += "Snow Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("COAL_12")) {
				causes += "Coal Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("IRON_12")) {
				causes += "Iron Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("GOLD_12")) {
				causes += "Gold Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("DIAMOND_12")) {
				causes += "Diamond Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("LAPIS_12")) {
				causes += "Lapis Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("REDSTONE_12")) {
				causes += "Redstone Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("EMERALD_12")) {
				causes += "Emerald Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("QUARTZ_12")) {
				causes += "Quartz Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("ENDER_STONE_11")) {
				causes += "Cobblestone Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("MITHRIL_12")) {
				causes += "Mithril Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("HARD_STONE_12")) {
				causes += "Hard Stone Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("WHEAT_12")) {
				causes += "Wheat Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("MELON_12")) {
				causes += "Melon Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("PUMPKIN_12")) {
				causes += "Pumpkin Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CARROT_12")) {
				causes += "Carrot Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("POTATO_12")) {
				causes += "Potato Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("MUSHROOM_12")) {
				causes += "Mushroom Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CACTUS_12")) {
				causes += "Cactus Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("COCOA_12")) {
				causes += "Cocoa Beans Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SUGAR_CANE_12")) {
				causes += "Cane Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("NETHER_WARTS_12")) {
				causes += "Nether Wart Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("FLOWER_12")) {
				causes += "Flower Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("FISHING_11")) {
				causes += "Fishing Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("ZOMBIE_11")) {
				causes += "Zombie Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("REVENANT_12")) {
				causes += "Revenant Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("VOIDLING_11")) {
				causes += "Voidling Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("INFERNO_11")) {
				causes += "Inferno Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("VAMPIRE_11")) {
				causes += "Vampire Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SKELETON_11")) {
				causes += "Skeleton Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CREEPER_11")) {
				causes += "Creeper Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SPIDER_11")) {
				causes += "Spider Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("TARANTULA_11")) {
				causes += "Tarantula Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CAVESPIDER_11")) {
				causes += "Cave Spider Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("BLAZE_12")) {
				causes += "Blaze Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("MAGMA_CUBE_12")) {
				causes += "Magma Cube Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("ENDERMAN_11")) {
				causes += "Enderman Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("GHAST_12")) {
				causes += "Ghast Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SLIME_11")) {
				causes += "Slime Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("COW_12")) {
				causes += "Cow Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("PIG_12")) {
				causes += "Pig Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("CHICKEN_12")) {
				causes += "Chicken Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SHEEP_12")) {
				causes += "Sheep Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("RABBIT_12")) {
				causes += "Rabbit Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("OAK_11")) {
				causes += "Oak Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("SPRUCE_11")) {
				causes += "Spruce Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("BIRCH_11")) {
				causes += "Birch Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("DARK_OAK_11")) {
				causes += "Dark Oak Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("ACACIA_11")) {
				causes += "Acacia Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(!minions.contains("JUNGLE_11")) {
				causes += "Jungle Minion: +0.02\n";
				thisBaldness += 0.02;
			}

			if(thisBaldness > 0) {
				try {
					builder.addField("**__Minions__**", causes + "**Final Baldness for Minions**: " + thisBaldness, false);
				} catch(Exception exception) {
					builder.addField("**__Minions__**", "Too Many Uncrafted Minions\n**Final Baldness for Minions**: " + thisBaldness, false);
				}
				baldness += thisBaldness;
			}

			/*
				███╗   ███╗██████╗
				████╗ ████║██╔══██╗
				██╔████╔██║██████╔╝
				██║╚██╔╝██║██╔═══╝
				██║ ╚═╝ ██║██║
				╚═╝     ╚═╝╚═╝
			 */

			long mp = profile.getAsJsonObject("accessory_bag_storage").get("highest_magical_power").getAsLong();
			long maxMP = 1701;
			if(mp < maxMP) {
				builder.addField("**__Magical Power__**", "Missing " + (maxMP - mp) + " MP: +" + (maxMP - mp) * 0.0006, false);
				baldness += (maxMP - mp) * 0.0006;
			}

			/*
				███╗   ███╗██╗███╗   ██╗██╗███╗   ██╗ ██████╗
				████╗ ████║██║████╗  ██║██║████╗  ██║██╔════╝
				██╔████╔██║██║██╔██╗ ██║██║██╔██╗ ██║██║  ███╗
				██║╚██╔╝██║██║██║╚██╗██║██║██║╚██╗██║██║   ██║
				██║ ╚═╝ ██║██║██║ ╚████║██║██║ ╚████║╚██████╔╝
				╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═╝╚═╝  ╚═══╝ ╚═════╝
			 */

			causes = "";
			thisBaldness = 0.0;

			JsonObject hotm = profile.getAsJsonObject("mining_core");
			long hotmLevel;
			try {
				hotmLevel = Utils.hotmLevel(hotm.get("experience").getAsLong());
			} catch(Exception exception) {
				hotmLevel = 0;
			}
			double mithrilMillions;
			try {
				mithrilMillions = (double) (hotm.get("powder_mithril").getAsLong() + hotm.get("powder_spent_mithril").getAsLong()) / 1000000;
			} catch(Exception exception) {
				mithrilMillions = 0;
			}

			double gemstoneMillions;
			try {
				gemstoneMillions = (double) (hotm.get("powder_gemstone").getAsLong() + hotm.get("powder_spent_gemstone").getAsLong()) / 1000000;
			} catch(Exception exception) {
				gemstoneMillions = 0;
			}

			double glaciteMillions;
			try {
				glaciteMillions = (double) (hotm.get("powder_glacite").getAsLong() + hotm.get("powder_spent_glacite").getAsLong()) / 1000000;
			} catch(Exception exception) {
				glaciteMillions = 0;
			}

			if(hotmLevel < 10) {
				double result = (10 - hotmLevel) * 0.1;
				causes += "HOTM Level: +" + result + "\n";
				thisBaldness += result;
			}

			if(mithrilMillions < 12.5) {
				double result = (12.5 - mithrilMillions) * 0.02;
				causes += "Mithril Powder: +" + result + "\n";
				thisBaldness += result;
			}

			if(gemstoneMillions < 20) {
				double result = (20 - gemstoneMillions) * 0.02;
				causes += "Gemstone Powder: +" + result + "\n";
				thisBaldness += result;
			}

			if(glaciteMillions < 20) {
				double result = (20 - glaciteMillions) * 0.02;
				causes += "Glacite Powder: +" + result + "\n";
				thisBaldness += result;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Mining__**", causes + "**Final Baldness for Mining**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			/*
				███████╗██╗███████╗██╗  ██╗██╗███╗   ██╗ ██████╗
				██╔════╝██║██╔════╝██║  ██║██║████╗  ██║██╔════╝
				█████╗  ██║███████╗███████║██║██╔██╗ ██║██║  ███╗
				██╔══╝  ██║╚════██║██╔══██║██║██║╚██╗██║██║   ██║
				██║     ██║███████║██║  ██║██║██║ ╚████║╚██████╔╝
				╚═╝     ╚═╝╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝
			 */

			causes = "";
			thisBaldness = 0.0;

			JsonObject trophyFish = profile.getAsJsonObject("trophy_fish");

			if(!trophyFish.has("blobfish_diamond")) {
				causes += "Diamond Blobfish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("gusher_diamond")) {
				causes += "Diamond Gusher: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("obfuscated_fish_1_diamond")) {
				causes += "Diamond Obf 1: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("flyfish_diamond")) {
				causes += "Diamond Flyfish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("lava_horse_diamond")) {
				causes += "Diamond Lavahorse: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("mana_ray_diamond")) {
				causes += "Diamond Mana Ray: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("vanille_diamond")) {
				causes += "Diamond Vanille: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("volcanic_stonefish_diamond")) {
				causes += "Diamond Volcanic Stonefish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("golden_fish_diamond")) {
				causes += "Diamond Goldfish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("skeleton_fish_diamond")) {
				causes += "Diamond Skeleton Fish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("steaming_hot_flounder_diamond")) {
				causes += "Diamond Steaming-Hot Founder: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("sulphur_skitter_diamond")) {
				causes += "Diamond Sulphur Skitter: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("obfuscated_fish_2_diamond")) {
				causes += "Diamond Obf 2: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("soul_fish_diamond")) {
				causes += "Diamond Soul Fish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("moldfin_diamond")) {
				causes += "Diamond Moldfin: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("slugfish_diamond")) {
				causes += "Diamond Slugfish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("obfuscated_fish_3_diamond")) {
				causes += "Diamond Obf 3: +0.055\n";
				thisBaldness += 0.055;
			}

			if(!trophyFish.has("karate_fish_diamond")) {
				causes += "Diamond Karate Fish: +0.055\n";
				thisBaldness += 0.055;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Fishing__**", causes + "**Final Baldness for Fishing**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			/*
				██████╗            ██╗ ██████╗
				██╔══██╗▄ ██╗▄     ██║██╔═══██╗
				██║  ██║ ████╗     ██║██║   ██║
				██║  ██║▀╚██╔▀██   ██║██║   ██║
				██████╔╝  ╚═╝ ╚█████╔╝╚██████╔╝
				╚═════╝        ╚════╝  ╚═════╝
			 */

			long total = 0;
			try {
				JsonObject dojo = profile.getAsJsonObject("nether_island_player_data").getAsJsonObject("dojo");
				if(dojo.has("dojo_points_mob_kb")) {
					total += dojo.get("dojo_points_mob_kb").getAsLong();
				}

				if(dojo.has("dojo_points_wall_jump")) {
					total += dojo.get("dojo_points_wall_jump").getAsLong();
				}

				if(dojo.has("dojo_points_archer")) {
					total += dojo.get("dojo_points_archer").getAsLong();
				}

				if(dojo.has("dojo_points_sword_swap")) {
					total += dojo.get("dojo_points_sword_swap").getAsLong();
				}

				if(dojo.has("dojo_points_snake")) {
					total += dojo.get("dojo_points_snake").getAsLong();
				}

				if(dojo.has("dojo_points_fireball")) {
					total += dojo.get("dojo_points_fireball").getAsLong();
				}

				if(dojo.has("dojo_points_lock_head")) {
					total += dojo.get("dojo_points_lock_head").getAsLong();
				}
			} catch(Exception exception) {
				// nothing here
			}

			if(total < 7000) {
				builder.addField("**__D*jo__**", "Lack of Black Belt: +" + (7000 - total) * 0.0001, false);
				baldness += (7000 - total) * 0.0001;
			}

			/*
				███╗   ███╗██╗   ██╗███████╗███████╗██╗   ██╗███╗   ███╗
				████╗ ████║██║   ██║██╔════╝██╔════╝██║   ██║████╗ ████║
				██╔████╔██║██║   ██║███████╗█████╗  ██║   ██║██╔████╔██║
				██║╚██╔╝██║██║   ██║╚════██║██╔══╝  ██║   ██║██║╚██╔╝██║
				██║ ╚═╝ ██║╚██████╔╝███████║███████╗╚██████╔╝██║ ╚═╝ ██║
				╚═╝     ╚═╝ ╚═════╝ ╚══════╝╚══════╝ ╚═════╝ ╚═╝     ╚═╝
			 */

			long donations;
			try {
				donations = museum.getAsJsonObject("items").size();
			} catch(Exception exception) {
				donations = 0;
			}
			if(donations < 300) {
				builder.addField("**__Museum__**", "Missing " + (300 - donations) + " Museum Donos: +" + (300 - donations) * 0.002, false);
				baldness += (300 - donations) * 0.002;
			}

			/*
				███████╗ █████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗ ██████╗
				██╔════╝██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║██╔════╝
				█████╗  ███████║██████╔╝██╔████╔██║██║██╔██╗ ██║██║  ███╗
				██╔══╝  ██╔══██║██╔══██╗██║╚██╔╝██║██║██║╚██╗██║██║   ██║
				██║     ██║  ██║██║  ██║██║ ╚═╝ ██║██║██║ ╚████║╚██████╔╝
				╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝
			 */

			causes = "";
			thisBaldness = 0;

			long visitors;
			try {
				visitors = garden.getAsJsonObject("commission_data").get("total_completed").getAsLong();
			} catch(Exception exception) {
				visitors = 0;
			}
			if(visitors < 10000) {
				double result = (10000 - visitors) * 0.0001;
				causes += "Visitors: +" + result + "\n";
				thisBaldness += result;
			}

			try {
				JsonObject milestones = garden.getAsJsonObject("resources_collected");
				long wheatMilestone = Utils.cropMilestone(milestones.get("WHEAT").getAsLong(), 1);
				long carrotMilestone = Utils.cropMilestone(milestones.get("CARROT_ITEM").getAsLong(), 3.25);
				long potatoMilestone = Utils.cropMilestone(milestones.get("POTATO_ITEM").getAsLong(), 3.25);
				long melonMilestone = Utils.cropMilestone(milestones.get("MELON").getAsLong(), 5);
				long pumpkinMilestone = Utils.cropMilestone(milestones.get("PUMPKIN").getAsLong(), 1);
				long cocoaMilestone = Utils.cropMilestone(milestones.get("INK_SACK:3").getAsLong(), 3);
				long caneMilestone = Utils.cropMilestone(milestones.get("SUGAR_CANE").getAsLong(), 2);
				long cactusMilestone = Utils.cropMilestone(milestones.get("CACTUS").getAsLong(), 2);
				long mushroomMilestone = Utils.cropMilestone(milestones.get("MUSHROOM_COLLECTION").getAsLong(), 1);
				long wartMilestone = Utils.cropMilestone(milestones.get("NETHER_STALK").getAsLong(), 3);

				if(wheatMilestone < 46) {
					double result = (46 - wheatMilestone) * 0.002;
					causes += "Wheat Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(carrotMilestone < 46) {
					double result = (46 - carrotMilestone) * 0.002;
					causes += "Carrot Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(potatoMilestone < 46) {
					double result = (46 - potatoMilestone) * 0.002;
					causes += "Potato Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(melonMilestone < 46) {
					double result = (46 - melonMilestone) * 0.002;
					causes += "Melon Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(pumpkinMilestone < 46) {
					double result = (46 - pumpkinMilestone) * 0.002;
					causes += "Pumpkin Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(cocoaMilestone < 46) {
					double result = (46 - cocoaMilestone) * 0.002;
					causes += "Cocoa Beans Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(caneMilestone < 46) {
					double result = (46 - caneMilestone) * 0.002;
					causes += "Cane Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(cactusMilestone < 46) {
					double result = (46 - cactusMilestone) * 0.002;
					causes += "Cactus Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(mushroomMilestone < 46) {
					double result = (46 - mushroomMilestone) * 0.002;
					causes += "Mushroom Milestone: +" + result + "\n";
					thisBaldness += result;
				}

				if(wartMilestone < 46) {
					double result = (46 - wartMilestone) * 0.002;
					causes += "Nether Wart Milestone: +" + result + "\n";
					thisBaldness += result;
				}
			} catch(Exception exception) {
				causes += "Has not unlocked all Garden milestones: +0.92\n";
				thisBaldness += 0.92;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Garden__**", causes + "**Final Baldness for Garden**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			/*
				██████╗ ██╗███████╗████████╗
				██╔══██╗██║██╔════╝╚══██╔══╝
				██████╔╝██║█████╗     ██║
				██╔══██╗██║██╔══╝     ██║
				██║  ██║██║██║        ██║
				╚═╝  ╚═╝╚═╝╚═╝        ╚═╝
			 */

			try {
				long timecharms = profile.getAsJsonObject("rift").getAsJsonObject("gallery").getAsJsonArray("secured_trophies").size();
				if(timecharms < 7) {
					builder.addField("**__Rift Timecharms__**", "Missing " + (7 - timecharms) + " Timecharms: +" + (7 - timecharms) * 0.15, false);
					baldness += (7 - timecharms) * 0.15;
				}
			} catch(Exception exception) {
				builder.addField("**__Rift Timecharms__**", "Missing 7 Timecharms: +1.05", false);
				baldness += 1.05;
			}

			/*
				██╗  ██╗ ██████╗ ██████╗ ██████╗ ██╗████████╗██╗   ██╗
				██║  ██║██╔═══██╗██╔══██╗██╔══██╗██║╚══██╔══╝╚██╗ ██╔╝
				███████║██║   ██║██████╔╝██████╔╝██║   ██║    ╚████╔╝
				██╔══██║██║   ██║██╔═══╝ ██╔═══╝ ██║   ██║     ╚██╔╝
				██║  ██║╚██████╔╝██║     ██║     ██║   ██║      ██║
				╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚═╝   ╚═╝      ╚═╝
			 */

			causes = "";
			thisBaldness = 0.0;

			try {
				JsonObject rabbits = profile.getAsJsonObject("events").getAsJsonObject("easter").getAsJsonObject("rabbits");

				if(!rabbits.has("aurora")) {
					causes += "Aurora Rabbit: +0.01\n";
					thisBaldness += 0.01;
				}

				if(!rabbits.has("celestia")) {
					causes += "Celestia Rabbit: +0.01\n";
					thisBaldness += 0.01;
				}

				if(!rabbits.has("orion")) {
					causes += "Orion Rabbit: +0.01\n";
					thisBaldness += 0.01;
				}

				if(!rabbits.has("starfire")) {
					causes += "Starfire Rabbit: +0.01\n";
					thisBaldness += 0.01;
				}

				if(!rabbits.has("vega")) {
					causes += "Vega Rabbit: +0.01\n";
					thisBaldness += 0.01;
				}

				if(!rabbits.has("dante")) {
					causes += "Dante Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("einstein")) {
					causes += "Einstein Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("galaxy")) {
					causes += "Galaxy Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("king")) {
					causes += "King Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("mu")) {
					causes += "Mu Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("napoleon")) {
					causes += "Napoleon Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("omega")) {
					causes += "Omega Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("sigma")) {
					causes += "Sigma Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("zest_zephyr")) {
					causes += "Zest Zephyr Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("zeta")) {
					causes += "Zeta Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(!rabbits.has("zorro")) {
					causes += "Zorro Rabbit: +0.03\n";
					thisBaldness += 0.03;
				}

				if(thisBaldness > 0) {
					builder.addField("**__Hoppity__**", causes + "**Final Baldness for Hoppity**: " + thisBaldness, false);
					baldness += thisBaldness;
				}
			} catch(Exception exception) {
				builder.addField("**__Hoppity__**", "No Data!\n**Final Baldness for Hoppity**: 0.38", false);
				baldness += 0.38;
			}

			/*
				██████╗ ███████╗████████╗███████╗
				██╔══██╗██╔════╝╚══██╔══╝██╔════╝
				██████╔╝█████╗     ██║   ███████╗
				██╔═══╝ ██╔══╝     ██║   ╚════██║
				██║     ███████╗   ██║   ███████║
				╚═╝     ╚══════╝   ╚═╝   ╚══════╝
			 */

			long score = profile.getAsJsonObject("leveling").get("highest_pet_score").getAsLong();
			long maxPetScore = 445;
			if(score < maxPetScore) {
				builder.addField("**__Pet Score__**", "Missing " + (maxPetScore - score) + " Pet Score: +" + (maxPetScore - score) * 0.002, false);
				baldness += (maxPetScore - score) * 0.002;
			}

			/*
				███╗   ███╗███████╗██╗      ██████╗ ██████╗ ██╗   ██╗
				████╗ ████║██╔════╝██║     ██╔═══██╗██╔══██╗╚██╗ ██╔╝
				██╔████╔██║█████╗  ██║     ██║   ██║██║  ██║ ╚████╔╝
				██║╚██╔╝██║██╔══╝  ██║     ██║   ██║██║  ██║  ╚██╔╝
				██║ ╚═╝ ██║███████╗███████╗╚██████╔╝██████╔╝   ██║
				╚═╝     ╚═╝╚══════╝╚══════╝ ╚═════╝ ╚═════╝    ╚═╝
			 */

			causes = "";
			thisBaldness = 0;

			JsonObject melody = profile.getAsJsonObject("quests").getAsJsonObject("harp_quest");

			if(melody == null) {
				melody = new JsonObject();
			}

			if(!melody.has("song_hymn_joy_perfect_completions")) {
				causes += "Beethoven Symphony No. 9 in D Minor (Choral); IV. Finale: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_frere_jacques_perfect_completions")) {
				causes += "Frere Jacques: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_amazing_grace_perfect_completions")) {
				causes += "Amazing Grace: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_brahms_perfect_completions")) {
				causes += "Brahms' Lullaby: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_happy_birthday_perfect_completions")) {
				causes += "Happy Birthday: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_greensleeves_perfect_completions")) {
				causes += "Greensleeves: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_jeopardy_perfect_completions")) {
				causes += "Jeopardy: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_minuet_perfect_completions")) {
				causes += "Bach Minuet: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_joy_world_perfect_completions")) {
				causes += "Joy to the World: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_pure_imagination_perfect_completions")) {
				causes += "Pure Imagination: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_vie_en_rose_perfect_completions")) {
				causes += "La Vie en Rose: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_fire_and_flames_perfect_completions")) {
				causes += "Campfire: +0.05\n";
				thisBaldness += 0.05;
			}

			if(!melody.has("song_pachelbel_perfect_completions")) {
				causes += "Pachelbel Canon in D Major: +0.05\n";
				thisBaldness += 0.05;
			}

			if(thisBaldness > 0) {
				builder.addField("**__Harp__**", causes + "**Final Baldness for Harp**: " + thisBaldness, false);
				baldness += thisBaldness;
			}

			causes = "";

			if(ironman) {
				causes += "Ironman (Respect): x0.9\n";
				baldness *= 0.9;
			}

			if(isBaldHunter) {
				causes += "Is GodHunter775: x2\n";
				baldness *= 2;
			}

			builder.addField("**__Multipliers__**", causes, false);

			e.replyEmbeds(builder.build());

			String rank;
			if(baldness < 2) {
				rank = "Not Bald";
			} else if(baldness < 5) {
				rank = "Slightly Bald";
			} else if(baldness < 9) {
				rank = "Bald";
			} else if(baldness < 14) {
				rank = "Very Bald";
			} else {
				rank = "Extremely Bald";
			}

			e.sendMessage("# **Final Baldness Score for `" + playerName + "`**: **||" + baldness + "||**\n# **Baldness Rank**: **||" + rank + "||**");
		} catch(ExecutionException exception) {
			e.reply("This user doees not exist!");
		} catch(Exception exception) {
			exception.printStackTrace();
			e.reply("Something went wrong!  Contact the dev to get him to sort out the issue.");
		}
	}
}