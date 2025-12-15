package regular;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eventListeners.GenericDiscordEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.http.HypixelHttpClient;
import net.hypixel.api.reply.skyblock.SkyBlockProfilesReply;
import processes.HypixelManager;
import processes.Utils;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.*;

public class Baldness {

	// Configuration class for easy value updates
	private static class BaldnessConfig {
		// Thresholds and multipliers
		static final long SKYBLOCK_LEVEL_THRESHOLD = 517;
		static final double SKYBLOCK_LEVEL_MULTIPLIER = 0.002;

		static final long CATA_LEVEL_THRESHOLD = 50;
		static final double CATA_LEVEL_MULTIPLIER = 0.03;

		static final long CLASS_LEVEL_THRESHOLD = 50;
		static final double CLASS_LEVEL_MULTIPLIER = 0.004;

		static final long M7_PB_THRESHOLD = 360;
		static final double M7_PB_MULTIPLIER = 0.005;
		static final double M7_PB_MAX = 1.2;

		static final long SLAYER_LEVEL_THRESHOLD = 9;
		static final double SLAYER_LEVEL_MULTIPLIER = 0.02;

		static final long VAMP_SLAYER_THRESHOLD = 5;
		static final double VAMP_SLAYER_MULTIPLIER = 0.02;

		static final long BESTIARY_THRESHOLD = 375;
		static final double BESTIARY_MULTIPLIER = 0.0025;

		static final double COLLECTION_THRESHOLD_MULTIPLIER = 0.015;
		static final double GOLD_COLLECTION_MULTIPLIER = 1;

		static final double MINION_MULTIPLIER = 0.02;

		static final long MAX_MAGICAL_POWER = 1865;
		static final double MP_MULTIPLIER = 0.0006;

		static final long HOTM_LEVEL_THRESHOLD = 10;
		static final double HOTM_LEVEL_MULTIPLIER = 0.1;

		static final double MITHRIL_THRESHOLD = 12.5;
		static final double GEMSTONE_THRESHOLD = 20;
		static final double GLACITE_THRESHOLD = 20;
		static final double POWDER_MULTIPLIER = 0.02;

		static final double TROPHY_FISH_MULTIPLIER = 0.055;

		static final long DOJO_THRESHOLD = 7000;
		static final double DOJO_MULTIPLIER = 0.0001;

		static final long MUSEUM_THRESHOLD = 300;
		static final double MUSEUM_MULTIPLIER = 0.002;

		static final long VISITOR_THRESHOLD = 10000;
		static final double VISITOR_MULTIPLIER = 0.0001;

		static final long CROP_MILESTONE_THRESHOLD = 46;
		static final double CROP_MILESTONE_MULTIPLIER = 0.002;

		static final long TIMECHARM_THRESHOLD = 8;
		static final double TIMECHARM_MULTIPLIER = 0.125;

		static final double RABBIT_COMMON_MULTIPLIER = 0.01;
		static final double RABBIT_RARE_MULTIPLIER = 0.03;

		static final long MAX_PET_SCORE = 476;
		static final double PET_SCORE_MULTIPLIER = 0.002;

		static final double HARP_SONG_MULTIPLIER = 0.05;

		static final double IRONMAN_MULTIPLIER = 0.9;
		static final double GODHUNTER_MULTIPLIER = 2.0;

		static final double API_OFF_PENALTY = 10.0;
		static final double NEVER_PLAYED_DUNGEONS_PENALTY = 100.0;
		static final double COLLECTION_ERROR_PENALTY = 0.15;
		static final double GARDEN_MILESTONE_PENALTY = 0.92;
		static final double HOPPITY_NO_DATA_PENALTY = 0.38;
	}

	// Helper class for baldness calculations
	private static class BaldnessCalculator {
		private final StringBuilder causes = new StringBuilder();
		private double baldness = 0.0;

		public void addBaldness(String cause, double amount) {
			causes.append(cause).append(": +").append(amount).append("\n");
			baldness += amount;
		}

		public void addBaldnessForThreshold(String name, long current, long threshold, double multiplier) {
			if (current < threshold) {
				double result = (threshold - current) * multiplier;
				addBaldness(name, result);
			}
		}

		public void addBaldnessForThreshold(String name, double current, double threshold, double multiplier) {
			if (current < threshold) {
				double result = (threshold - current) * multiplier;
				addBaldness(name, result);
			}
		}

		public String getCauses() {
			return causes.toString();
		}

		public double getBaldness() {
			return baldness;
		}

		public boolean hasBaldness() {
			return baldness > 0;
		}

		public void reset() {
			causes.setLength(0);
			baldness = 0.0;
		}
	}

	// Skill thresholds configuration
	private static final Map<String, SkillThreshold> SKILL_THRESHOLDS = new HashMap<>() {{
		put("SKILL_COMBAT", new SkillThreshold("Combat Level", 60, 0.002));
		put("SKILL_FARMING", new SkillThreshold("Farming Level", 60, 0.002));
		put("SKILL_FISHING", new SkillThreshold("Fishing Level", 50, 0.002));
		put("SKILL_MINING", new SkillThreshold("Mining Level", 60, 0.002));
		put("SKILL_FORAGING", new SkillThreshold("F*raging Level", 50, 0.002));
		put("SKILL_ENCHANTING", new SkillThreshold("Enchanting Level", 60, 0.002));
		put("SKILL_ALCHEMY", new SkillThreshold("Alchemy Level", 50, 0.002));
		put("SKILL_CARPENTRY", new SkillThreshold("Carpentry Level", 50, 0.002));
		put("SKILL_TAMING", new SkillThreshold("Taming Level", 60, 0.002));
	}};

	private record SkillThreshold(String displayName, long threshold, double multiplier) {
	}

	// Collection thresholds configuration
	private static final Map<String, CollectionThreshold> COLLECTION_THRESHOLDS = new HashMap<>() {{
		put("CACTUS", new CollectionThreshold("Cactus Collection", 50000));
		put("CARROT_ITEM", new CollectionThreshold("Carrot Collection", 100000));
		put("INK_SACK:3", new CollectionThreshold("Cocoa Beans Collection", 100000));
		put("FEATHER", new CollectionThreshold("Feather Collection", 50000));
		put("LEATHER", new CollectionThreshold("Leather Collection", 100000));
		put("MELON", new CollectionThreshold("Melon Collection", 250000));
		put("MUSHROOM_COLLECTION", new CollectionThreshold("Mushroom Collection", 50000));
		put("MUTTON", new CollectionThreshold("Mutton Collection", 100000));
		put("NETHER_STALK", new CollectionThreshold("Nether Wart Collection", 250000));
		put("POTATO_ITEM", new CollectionThreshold("Potato Collection", 100000));
		put("PUMPKIN", new CollectionThreshold("Pumpkin Collection", 250000));
		put("RAW_CHICKEN", new CollectionThreshold("Chicken Collection", 100000));
		put("PORK", new CollectionThreshold("Pork Collection", 50000));
		put("RABBIT", new CollectionThreshold("Rabbit Collection", 50000));
		put("SEEDS", new CollectionThreshold("Seeds Collection", 25000));
		put("SUGAR_CANE", new CollectionThreshold("Cane Collection", 50000));
		put("WHEAT", new CollectionThreshold("Wheat Collection", 100000));
		put("COAL", new CollectionThreshold("Coal Collection", 100000));
		put("COBBLESTONE", new CollectionThreshold("Cobblestone Collection", 70000));
		put("DIAMOND", new CollectionThreshold("Diamond Collection", 50000));
		put("EMERALD", new CollectionThreshold("Emerald Collection", 100000));
		put("ENDER_STONE", new CollectionThreshold("End Stone Collection", 50000));
		put("GEMSTONE_COLLECTION", new CollectionThreshold("Gemstone Collection", 2000000));
		put("GLOWSTONE_DUST", new CollectionThreshold("Glowstone Collection", 25000));
		put("GRAVEL", new CollectionThreshold("Gravel Collection", 50000));
		put("HARD_STONE", new CollectionThreshold("Hard Stone Collection", 1000000));
		put("ICE", new CollectionThreshold("Ice Collection", 500000));
		put("IRON_INGOT", new CollectionThreshold("Iron Collection", 400000));
		put("INK_SACK:4", new CollectionThreshold("Lapis Collection", 250000));
		put("MITHRIL_ORE", new CollectionThreshold("Mithril Collection", 1000000));
		put("MYCEL", new CollectionThreshold("Mycelium Collection", 100000));
		put("QUARTZ", new CollectionThreshold("Quartz Collection", 50000));
		put("NETHERRACK", new CollectionThreshold("Netherrack Collection", 5000));
		put("OBSIDIAN", new CollectionThreshold("Obsidian Collection", 100000));
		put("SAND:1", new CollectionThreshold("Red Sand Collection", 100000));
		put("REDSTONE", new CollectionThreshold("Redstone Collection", 1400000));
		put("SAND", new CollectionThreshold("Sand Collection", 5000));
		put("BLAZE_ROD", new CollectionThreshold("Blaze Rod Collection", 50000));
		put("BONE", new CollectionThreshold("Bone Collection", 150000));
		put("ENDER_PEARL", new CollectionThreshold("Ender Pearl Collection", 50000));
		put("GHAST_TEAR", new CollectionThreshold("Ghast Tear Collection", 25000));
		put("SULPHUR", new CollectionThreshold("Gunpowder Collection", 50000));
		put("MAGMA_CREAM", new CollectionThreshold("Magma Cream Collection", 50000));
		put("ROTTEN_FLESH", new CollectionThreshold("Rotten Flesh Collection", 100000));
		put("SLIME_BALL", new CollectionThreshold("Slimeball Collection", 50000));
		put("STRING", new CollectionThreshold("String Collection", 50000));
		put("LOG_2", new CollectionThreshold("Acacia Collection", 25000));
		put("LOG:2", new CollectionThreshold("Birch Collection", 50000));
		put("LOG_2:1", new CollectionThreshold("Dark Oak Collection", 25000));
		put("LOG:3", new CollectionThreshold("Jungle Collection", 25000));
		put("LOG", new CollectionThreshold("Oak Collection", 30000));
		put("LOG:1", new CollectionThreshold("Spruce Collection", 50000));
		put("CLAY_BALL", new CollectionThreshold("Clay Collection", 2500));
		put("RAW_FISH:2", new CollectionThreshold("Clownfish Collection", 4000));
		put("INK_SACK", new CollectionThreshold("Ink Sack Collection", 4000));
		put("WATER_LILY", new CollectionThreshold("Lily Pad Collection", 10000));
		put("MAGMA_FISH", new CollectionThreshold("Magmafish Collection", 500000));
		put("PRISMARINE_CRYSTALS", new CollectionThreshold("Prismarine Crystal Collection", 800));
		put("PRISMARINE_SHARD", new CollectionThreshold("Prismarine Shard Collection", 800));
		put("RAW_FISH:3", new CollectionThreshold("Pufferfish Collection", 18000));
		put("RAW_FISH", new CollectionThreshold("Raw Fish Collection", 60000));
		put("RAW_FISH:1", new CollectionThreshold("Salmon Collection", 10000));
		put("SPONGE", new CollectionThreshold("Sponge Collection", 4000));
		put("GLACITE", new CollectionThreshold("Glacite Collection", 1000000));
		put("SULPHUR_ORE", new CollectionThreshold("Sulphur Collection", 100000));
		put("TUNGSTEN", new CollectionThreshold("Tungsten Collection", 1000000));
		put("UMBER", new CollectionThreshold("Umber Collection", 1000000));
		put("CHILI_PEPPER", new CollectionThreshold("Chili Pepper Collection", 20000));
		put("AGARICUS_CAP", new CollectionThreshold("Agaricus Cap Collection", 200));
		put("CADUCOUS_STEM", new CollectionThreshold("Caducous Stem Collection", 500));
		put("HALF_EATEN_CARROT", new CollectionThreshold("Half-Eaten Carrot Collection", 3500));
		put("HEMOVIBE", new CollectionThreshold("Hemovibe Collection", 400000));
		put("METAL_HEART", new CollectionThreshold("Living Metal Heart Collection", 100));
		put("WILTED_BERBERIS", new CollectionThreshold("Wilted Berberis Collection", 400));
	}};

	private record CollectionThreshold(String displayName, long threshold) {
	}

	// Minion configuration
	private static final Set<String> REQUIRED_MINIONS = new HashSet<>() {{
		add("COBBLESTONE_12");
		add("OBSIDIAN_12");
		add("GLOWSTONE_12");
		add("GRAVEL_11");
		add("SAND_11");
		add("RED_SAND_12");
		add("MYCELIUM_12");
		add("CLAY_11");
		add("ICE_12");
		add("SNOW_12");
		add("COAL_12");
		add("IRON_12");
		add("GOLD_12");
		add("DIAMOND_12");
		add("LAPIS_12");
		add("REDSTONE_12");
		add("EMERALD_12");
		add("QUARTZ_12");
		add("ENDER_STONE_11");
		add("MITHRIL_12");
		add("HARD_STONE_12");
		add("WHEAT_12");
		add("MELON_12");
		add("PUMPKIN_12");
		add("CARROT_12");
		add("POTATO_12");
		add("MUSHROOM_12");
		add("CACTUS_12");
		add("COCOA_12");
		add("SUGAR_CANE_12");
		add("NETHER_WARTS_12");
		add("FLOWER_12");
		add("FISHING_11");
		add("ZOMBIE_11");
		add("REVENANT_12");
		add("VOIDLING_11");
		add("INFERNO_11");
		add("VAMPIRE_11");
		add("SKELETON_11");
		add("CREEPER_11");
		add("SPIDER_11");
		add("TARANTULA_11");
		add("CAVESPIDER_11");
		add("BLAZE_12");
		add("MAGMA_CUBE_12");
		add("ENDERMAN_11");
		add("GHAST_12");
		add("SLIME_11");
		add("COW_12");
		add("PIG_12");
		add("CHICKEN_12");
		add("SHEEP_12");
		add("RABBIT_12");
		add("OAK_11");
		add("SPRUCE_11");
		add("BIRCH_11");
		add("DARK_OAK_11");
		add("ACACIA_11");
		add("JUNGLE_11");
	}};

	// Trophy fish configuration
	private static final Set<String> REQUIRED_DIAMOND_TROPHY_FISH = new HashSet<>() {{
		add("blobfish_diamond");
		add("gusher_diamond");
		add("obfuscated_fish_1_diamond");
		add("flyfish_diamond");
		add("lava_horse_diamond");
		add("mana_ray_diamond");
		add("vanille_diamond");
		add("volcanic_stonefish_diamond");
		add("golden_fish_diamond");
		add("skeleton_fish_diamond");
		add("steaming_hot_flounder_diamond");
		add("sulphur_skitter_diamond");
		add("obfuscated_fish_2_diamond");
		add("soul_fish_diamond");
		add("moldfin_diamond");
		add("slugfish_diamond");
		add("obfuscated_fish_3_diamond");
		add("karate_fish_diamond");
	}};

	// Rabbit configuration
	private static final Set<String> COMMON_RABBITS = new HashSet<>() {{
		add("aurora");
		add("celestia");
		add("orion");
		add("starfire");
		add("vega");
	}};

	private static final Set<String> RARE_RABBITS = new HashSet<>() {{
		add("dante");
		add("einstein");
		add("galaxy");
		add("king");
		add("mu");
		add("napoleon");
		add("omega");
		add("sigma");
		add("zest_zephyr");
		add("zeta");
		add("zorro");
	}};

	// Harp songs configuration
	private static final Map<String, String> HARP_SONGS = new HashMap<>() {{
		put("song_hymn_joy_perfect_completions", "Beethoven Symphony No. 9 in D Minor (Choral); IV. Finale");
		put("song_frere_jacques_perfect_completions", "Frere Jacques");
		put("song_amazing_grace_perfect_completions", "Amazing Grace");
		put("song_brahms_perfect_completions", "Brahms' Lullaby");
		put("song_happy_birthday_perfect_completions", "Happy Birthday");
		put("song_greensleeves_perfect_completions", "Greensleeves");
		put("song_jeopardy_perfect_completions", "Jeopardy");
		put("song_minuet_perfect_completions", "Bach Minuet");
		put("song_joy_world_perfect_completions", "Joy to the World");
		put("song_pure_imagination_perfect_completions", "Pure Imagination");
		put("song_vie_en_rose_perfect_completions", "La Vie en Rose");
		put("song_fire_and_flames_perfect_completions", "Campfire");
		put("song_pachelbel_perfect_completions", "Pachelbel Canon in D Major");
	}};

	public static void baldness(GenericDiscordEvent e, String playerName, String fruit) {
		try {
			// Get player data
			PlayerData playerData = getPlayerData(playerName, fruit);
			if (playerData == null) {
				e.reply("An error occurred! Check that you typed the Minecraft name correctly.");
				return;
			}

			EmbedBuilder builder = new EmbedBuilder()
					.setColor(Color.BLUE)
					.setFooter("Ling Ling", e.getJDA().getSelfUser().getAvatarUrl())
					.setTitle("Baldness Factor for " + playerName);

			double totalBaldness = 0.0;
			boolean isBaldHunter = playerName.equalsIgnoreCase("GodHunter775");

			// Calculate baldness for each category
			totalBaldness += calculateSkyBlockLevelBaldness(builder, playerData);
			totalBaldness += calculateSkillsBaldness(builder, playerData);
			totalBaldness += calculateDungeonsBaldness(builder, playerData);
			totalBaldness += calculateCombatBaldness(builder, playerData);
			totalBaldness += calculateCollectionsBaldness(builder, playerData);
			totalBaldness += calculateMinionsBaldness(builder, playerData);
			totalBaldness += calculateMagicalPowerBaldness(builder, playerData);
			totalBaldness += calculateMiningBaldness(builder, playerData);
			totalBaldness += calculateFishingBaldness(builder, playerData);
			totalBaldness += calculateDojoBaldness(builder, playerData);
			totalBaldness += calculateMuseumBaldness(builder, playerData);
			totalBaldness += calculateGardenBaldness(builder, playerData);
			totalBaldness += calculateRiftBaldness(builder, playerData);
			totalBaldness += calculateHoppityBaldness(builder, playerData);
			totalBaldness += calculatePetScoreBaldness(builder, playerData);
			totalBaldness += calculateHarpBaldness(builder, playerData);

			// Apply multipliers
			totalBaldness = applyMultipliers(builder, totalBaldness, playerData.ironman, isBaldHunter);

			e.replyEmbeds(builder.build());

			String rank = getBaldnessRank(totalBaldness);
			e.sendMessage("# **Final Baldness Score for `" + playerName + "`**: **||" + totalBaldness + "||**\n# **Baldness Rank**: **||" + rank + "||**");

		} catch (ExecutionException exception) {
			e.reply("This user does not exist!");
		} catch (Exception exception) {
			exception.printStackTrace();
			e.reply("Something went wrong! Contact the dev to get him to sort out the issue.");
		}
	}

	private static PlayerData getPlayerData(String playerName, String fruit) throws Exception {
		String uuid = HypixelManager.getMojang().getUUIDOfUsername(playerName);
		HypixelHttpClient client = HypixelManager.getClient();
		HypixelAPI api = new HypixelAPI(client);
		SkyBlockProfilesReply reply = api.getSkyBlockProfiles(uuid).get();
		JsonArray profiles = reply.getProfiles();

		JsonObject profile = null;
		String profileUUID = "";
		boolean ironman = false;

		if (fruit.isEmpty()) {
			for (int i = 0; i < profiles.size(); i++) {
				JsonObject p = profiles.get(i).getAsJsonObject();
				if (p.get("selected").getAsBoolean()) {
					if (p.has("game_mode") && p.get("game_mode").getAsString().equals("ironman")) {
						ironman = true;
					}
					profileUUID = p.get("profile_id").getAsString();
					profile = p.getAsJsonObject("members").getAsJsonObject(uuid);
					break;
				}
			}
		} else {
			for (int i = 0; i < profiles.size(); i++) {
				JsonObject p = profiles.get(i).getAsJsonObject();
				if (p.get("cute_name").getAsString().equalsIgnoreCase(fruit)) {
					profileUUID = p.get("profile_id").getAsString();
					profile = p.getAsJsonObject("members").getAsJsonObject(uuid);
					break;
				}
			}
		}

		if (profile == null) return null;

		// Get museum and garden data
		JsonObject museum = getMuseumData(profileUUID, uuid);
		JsonObject garden = getGardenData(profileUUID);

		return new PlayerData(profile, museum, garden, ironman);
	}

	private static JsonObject getMuseumData(String profileUUID, String uuid) throws Exception {
		Dotenv env = Dotenv.load();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.hypixel.net/v2/skyblock/museum?key=" + env.get("HYPIXEL_KEY") + "&profile=" + profileUUID))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return JsonParser.parseString(response.body()).getAsJsonObject()
				.getAsJsonObject("members").getAsJsonObject(uuid);
	}

	private static JsonObject getGardenData(String profileUUID) throws Exception {
		Dotenv env = Dotenv.load();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.hypixel.net/v2/skyblock/garden?key=" + env.get("HYPIXEL_KEY") + "&profile=" + profileUUID))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return JsonParser.parseString(response.body()).getAsJsonObject()
				.getAsJsonObject("garden");
	}

	private record PlayerData(JsonObject profile, JsonObject museum, JsonObject garden, boolean ironman) {
	}

	private static double calculateSkyBlockLevelBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long level = data.profile.getAsJsonObject("leveling").get("experience").getAsLong() / 100;
		calc.addBaldnessForThreshold("SkyBlock Level", level, BaldnessConfig.SKYBLOCK_LEVEL_THRESHOLD, BaldnessConfig.SKYBLOCK_LEVEL_MULTIPLIER);

		if (calc.hasBaldness()) {
			builder.addField("**__SkyBlock Level__**", "**Baldness for SkyBlock Level**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateSkillsBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		try {
			JsonObject skills = data.profile.getAsJsonObject("player_data").getAsJsonObject("experience");

			for (Map.Entry<String, SkillThreshold> entry : SKILL_THRESHOLDS.entrySet()) {
				String skillKey = entry.getKey();
				SkillThreshold threshold = entry.getValue();

				long skillLevel = Utils.skillLevel(skills.get(skillKey).getAsLong());
				calc.addBaldnessForThreshold(threshold.displayName, skillLevel, threshold.threshold, threshold.multiplier);
			}

			if (calc.hasBaldness()) {
				builder.addField("**__Skills__**", calc.getCauses() + "**Final Baldness for Skills**: " + calc.getBaldness(), false);
			}
		} catch (Exception exception) {
			builder.addField("**__Skills__**", "API Off: +" + BaldnessConfig.API_OFF_PENALTY + "\n**Final Baldness for Skills**: " + BaldnessConfig.API_OFF_PENALTY, false);
			calc.addBaldness("API Off", BaldnessConfig.API_OFF_PENALTY);
		}

		return calc.getBaldness();
	}

	private static double calculateDungeonsBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		try {
			JsonObject dungeons = data.profile.getAsJsonObject("dungeons");
			JsonObject cata = dungeons.getAsJsonObject("dungeon_types");

			// Catacombs level
			long cataLevel = Utils.cataLevel(cata.getAsJsonObject("catacombs").get("experience").getAsLong());
			calc.addBaldnessForThreshold("Cata Level", cataLevel, BaldnessConfig.CATA_LEVEL_THRESHOLD, BaldnessConfig.CATA_LEVEL_MULTIPLIER);

			// Class levels
			JsonObject classes = dungeons.getAsJsonObject("player_classes");
			String[] classNames = {"healer", "mage", "berserk", "archer", "tank"};
			String[] classDisplayNames = {"Healer Level", "Mage Level", "Berserk Level", "Archer Level", "Tank Level"};

			for (int i = 0; i < classNames.length; i++) {
				long classLevel = Utils.cataLevel(classes.getAsJsonObject(classNames[i]).get("experience").getAsLong());
				calc.addBaldnessForThreshold(classDisplayNames[i], classLevel, BaldnessConfig.CLASS_LEVEL_THRESHOLD, BaldnessConfig.CLASS_LEVEL_MULTIPLIER);
			}

			// M7 PB
			try {
				long m7PB = ((long) cata.getAsJsonObject("master_catacombs").getAsJsonObject("fastest_time").get("7").getAsDouble()) / 1000;
				if (m7PB > BaldnessConfig.M7_PB_THRESHOLD) {
					double result = (m7PB - BaldnessConfig.M7_PB_THRESHOLD) * BaldnessConfig.M7_PB_MULTIPLIER;
					result = Math.max(0, Math.min(result, BaldnessConfig.M7_PB_MAX));
					calc.addBaldness("Bad M7 PB", result);
				}
			} catch (Exception exception) {
				calc.addBaldness("Bad M7 PB", BaldnessConfig.M7_PB_MAX);
			}

		} catch (Exception exception) {
			calc.addBaldness("Never Played Dungeons", BaldnessConfig.NEVER_PLAYED_DUNGEONS_PENALTY);
		}

		if (calc.hasBaldness()) {
			builder.addField("**__Dungeons__**", calc.getCauses() + "**Final Baldness for Dungeons**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateCombatBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		// Slayer levels
		JsonObject slayers = data.profile.getAsJsonObject("slayer").getAsJsonObject("slayer_bosses");
		String[] slayerTypes = {"zombie", "spider", "wolf", "enderman", "blaze"};
		String[] slayerDisplayNames = {"Zombie Slayer Level", "Spider Slayer Level", "Wolf Slayer Level", "Enderman Slayer Level", "Blaze Slayer Level"};

		for (int i = 0; i < slayerTypes.length; i++) {
			long slayerLevel = getSlayerLevel(slayers, slayerTypes[i]);
			calc.addBaldnessForThreshold(slayerDisplayNames[i], slayerLevel, BaldnessConfig.SLAYER_LEVEL_THRESHOLD, BaldnessConfig.SLAYER_LEVEL_MULTIPLIER);
		}

		// Vampire slayer (different threshold)
		long vampLevel = getVampireSlayerLevel(slayers);
		calc.addBaldnessForThreshold("Vampire Slayer Level", vampLevel, BaldnessConfig.VAMP_SLAYER_THRESHOLD, BaldnessConfig.VAMP_SLAYER_MULTIPLIER);

		// Bestiary milestone
		long milestone = getBestiaryMilestone(data.profile);
		calc.addBaldnessForThreshold("Bestiary Milestone", milestone, BaldnessConfig.BESTIARY_THRESHOLD, BaldnessConfig.BESTIARY_MULTIPLIER);

		if (calc.hasBaldness()) {
			builder.addField("**__Combat__**", calc.getCauses() + "**Final Baldness for Combat**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateCollectionsBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		try {
			JsonObject collections = data.profile.getAsJsonObject("collection");

			// Standard collections
			for (Map.Entry<String, CollectionThreshold> entry : COLLECTION_THRESHOLDS.entrySet()) {
				String collectionKey = entry.getKey();
				CollectionThreshold threshold = entry.getValue();

				try {
					long collectionAmount = collections.get(collectionKey).getAsLong();
					if (collectionAmount < threshold.threshold) {
						calc.addBaldness(threshold.displayName, BaldnessConfig.COLLECTION_THRESHOLD_MULTIPLIER);
					}
				} catch (Exception e) {
					calc.addBaldness(threshold.displayName, BaldnessConfig.COLLECTION_THRESHOLD_MULTIPLIER);
				}
			}

			// Special gold collection calculation
			try {
				long gold = collections.get("GOLD_INGOT").getAsLong();
				if (gold < 100000000) {
					if (gold < 25000) {
						calc.addBaldness("Gold Collection", BaldnessConfig.COLLECTION_THRESHOLD_MULTIPLIER);
					}
					double result = 1 - (Math.sqrt(2 * gold) / (Math.sqrt(2) * 10000));
					calc.addBaldness("Bad Gold Collection", result);
				}
			} catch (Exception exception) {
				calc.addBaldness("Terrible Gold Collection", BaldnessConfig.GOLD_COLLECTION_MULTIPLIER);
			}

		} catch (Exception exception) {
			calc.addBaldness("API Off", BaldnessConfig.API_OFF_PENALTY);
		}

		if (calc.hasBaldness()) {
			try {
				builder.addField("**__Collections__**", calc.getCauses() + "**Final Baldness for Collections**: " + calc.getBaldness(), false);
			} catch (Exception exception) {
				builder.addField("**__Collections__**", "Too Many Unmaxed Collections\n**Final Baldness for Collections**: " + calc.getBaldness(), false);
			}
		}

		return calc.getBaldness();
	}

	private static double calculateMinionsBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		String minions = getMinionString(data.profile);

		for (String requiredMinion : REQUIRED_MINIONS) {
			if (!minions.contains(requiredMinion)) {
				String displayName = getMinionDisplayName(requiredMinion);
				calc.addBaldness(displayName, BaldnessConfig.MINION_MULTIPLIER);
			}
		}

		if (calc.hasBaldness()) {
			try {
				builder.addField("**__Minions__**", calc.getCauses() + "**Final Baldness for Minions**: " + calc.getBaldness(), false);
			} catch (Exception exception) {
				builder.addField("**__Minions__**", "Too Many Uncrafted Minions\n**Final Baldness for Minions**: " + calc.getBaldness(), false);
			}
		}

		return calc.getBaldness();
	}

	private static double calculateMagicalPowerBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long mp = data.profile.getAsJsonObject("accessory_bag_storage").get("highest_magical_power").getAsLong();
		calc.addBaldnessForThreshold("Missing MP", mp, BaldnessConfig.MAX_MAGICAL_POWER, BaldnessConfig.MP_MULTIPLIER);

		if (calc.hasBaldness()) {
			long missing = BaldnessConfig.MAX_MAGICAL_POWER - mp;
			builder.addField("**__Magical Power__**", "Missing " + missing + " MP: +" + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateMiningBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		JsonObject hotm = data.profile.getAsJsonObject("mining_core");

		// HOTM Level
		long hotmLevel = getHotmLevel(hotm);
		calc.addBaldnessForThreshold("HOTM Level", hotmLevel, BaldnessConfig.HOTM_LEVEL_THRESHOLD, BaldnessConfig.HOTM_LEVEL_MULTIPLIER);

		// Powders
		double mithrilMillions = getPowderMillions(hotm, "mithril");
		double gemstoneMillions = getPowderMillions(hotm, "gemstone");
		double glaciteMillions = getPowderMillions(hotm, "glacite");

		calc.addBaldnessForThreshold("Mithril Powder", mithrilMillions, BaldnessConfig.MITHRIL_THRESHOLD, BaldnessConfig.POWDER_MULTIPLIER);
		calc.addBaldnessForThreshold("Gemstone Powder", gemstoneMillions, BaldnessConfig.GEMSTONE_THRESHOLD, BaldnessConfig.POWDER_MULTIPLIER);
		calc.addBaldnessForThreshold("Glacite Powder", glaciteMillions, BaldnessConfig.GLACITE_THRESHOLD, BaldnessConfig.POWDER_MULTIPLIER);

		if (calc.hasBaldness()) {
			builder.addField("**__Mining__**", calc.getCauses() + "**Final Baldness for Mining**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateFishingBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		JsonObject trophyFish = data.profile.getAsJsonObject("trophy_fish");

		for (String fishType : REQUIRED_DIAMOND_TROPHY_FISH) {
			if (!trophyFish.has(fishType)) {
				String displayName = getTrophyFishDisplayName(fishType);
				calc.addBaldness(displayName, BaldnessConfig.TROPHY_FISH_MULTIPLIER);
			}
		}

		if (calc.hasBaldness()) {
			builder.addField("**__Fishing__**", calc.getCauses() + "**Final Baldness for Fishing**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateDojoBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long total = getDojoPoints(data.profile);
		calc.addBaldnessForThreshold("Lack of Black Belt", total, BaldnessConfig.DOJO_THRESHOLD, BaldnessConfig.DOJO_MULTIPLIER);

		if (calc.hasBaldness()) {
			builder.addField("**__D*jo__**", "Lack of Black Belt: +" + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateMuseumBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long donations = getMuseumDonations(data.museum);
		calc.addBaldnessForThreshold("Missing Museum Donos", donations, BaldnessConfig.MUSEUM_THRESHOLD, BaldnessConfig.MUSEUM_MULTIPLIER);

		if (calc.hasBaldness()) {
			long missing = BaldnessConfig.MUSEUM_THRESHOLD - donations;
			builder.addField("**__Museum__**", "Missing " + missing + " Museum Donos: +" + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateGardenBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		// Visitors
		long visitors = getVisitorCount(data.garden);
		calc.addBaldnessForThreshold("Visitors", visitors, BaldnessConfig.VISITOR_THRESHOLD, BaldnessConfig.VISITOR_MULTIPLIER);

		// Crop milestones
		try {
			JsonObject milestones = data.garden.getAsJsonObject("resources_collected");
			String[] crops = {"WHEAT", "CARROT_ITEM", "POTATO_ITEM", "MELON", "PUMPKIN", "INK_SACK:3", "SUGAR_CANE", "CACTUS", "MUSHROOM_COLLECTION", "NETHER_STALK"};
			double[] multipliers = {1, 3.25, 3.25, 5, 1, 3, 2, 2, 1, 3};
			String[] displayNames = {"Wheat Milestone", "Carrot Milestone", "Potato Milestone", "Melon Milestone", "Pumpkin Milestone", "Cocoa Beans Milestone", "Cane Milestone", "Cactus Milestone", "Mushroom Milestone", "Nether Wart Milestone"};

			for (int i = 0; i < crops.length; i++) {
				long milestone = Utils.cropMilestone(milestones.get(crops[i]).getAsLong(), multipliers[i]);
				calc.addBaldnessForThreshold(displayNames[i], milestone, BaldnessConfig.CROP_MILESTONE_THRESHOLD, BaldnessConfig.CROP_MILESTONE_MULTIPLIER);
			}
		} catch (Exception exception) {
			calc.addBaldness("Has not unlocked all Garden milestones", BaldnessConfig.GARDEN_MILESTONE_PENALTY);
		}

		if (calc.hasBaldness()) {
			builder.addField("**__Garden__**", calc.getCauses() + "**Final Baldness for Garden**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateRiftBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long timecharms = getTimecharmsCount(data.profile);
		calc.addBaldnessForThreshold("Missing Timecharms", timecharms, BaldnessConfig.TIMECHARM_THRESHOLD, BaldnessConfig.TIMECHARM_MULTIPLIER);

		if (calc.hasBaldness()) {
			long missing = BaldnessConfig.TIMECHARM_THRESHOLD - timecharms;
			builder.addField("**__Rift Timecharms__**", "Missing " + missing + " Timecharms: +" + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateHoppityBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		try {
			JsonObject rabbits = data.profile.getAsJsonObject("events").getAsJsonObject("easter").getAsJsonObject("rabbits");

			// Common rabbits
			for (String rabbit : COMMON_RABBITS) {
				if (!rabbits.has(rabbit)) {
					String displayName = getRabbitDisplayName(rabbit);
					calc.addBaldness(displayName, BaldnessConfig.RABBIT_COMMON_MULTIPLIER);
				}
			}

			// Rare rabbits
			for (String rabbit : RARE_RABBITS) {
				if (!rabbits.has(rabbit)) {
					String displayName = getRabbitDisplayName(rabbit);
					calc.addBaldness(displayName, BaldnessConfig.RABBIT_RARE_MULTIPLIER);
				}
			}

			if (calc.hasBaldness()) {
				builder.addField("**__Hoppity__**", calc.getCauses() + "**Final Baldness for Hoppity**: " + calc.getBaldness(), false);
			}
		} catch (Exception exception) {
			builder.addField("**__Hoppity__**", "No Data!\n**Final Baldness for Hoppity**: " + BaldnessConfig.HOPPITY_NO_DATA_PENALTY, false);
			calc.addBaldness("No Data", BaldnessConfig.HOPPITY_NO_DATA_PENALTY);
		}

		return calc.getBaldness();
	}

	private static double calculatePetScoreBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		long score = data.profile.getAsJsonObject("leveling").get("highest_pet_score").getAsLong();
		calc.addBaldnessForThreshold("Missing Pet Score", score, BaldnessConfig.MAX_PET_SCORE, BaldnessConfig.PET_SCORE_MULTIPLIER);

		if (calc.hasBaldness()) {
			long missing = BaldnessConfig.MAX_PET_SCORE - score;
			builder.addField("**__Pet Score__**", "Missing " + missing + " Pet Score: +" + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double calculateHarpBaldness(EmbedBuilder builder, PlayerData data) {
		BaldnessCalculator calc = new BaldnessCalculator();

		JsonObject melody = data.profile.getAsJsonObject("quests").getAsJsonObject("harp_quest");
		if (melody == null) {
			melody = new JsonObject();
		}

		for (Map.Entry<String, String> entry : HARP_SONGS.entrySet()) {
			String songKey = entry.getKey();
			String displayName = entry.getValue();

			if (!melody.has(songKey)) {
				calc.addBaldness(displayName, BaldnessConfig.HARP_SONG_MULTIPLIER);
			}
		}

		if (calc.hasBaldness()) {
			builder.addField("**__Harp__**", calc.getCauses() + "**Final Baldness for Harp**: " + calc.getBaldness(), false);
		}

		return calc.getBaldness();
	}

	private static double applyMultipliers(EmbedBuilder builder, double baldness, boolean ironman, boolean isBaldHunter) {
		StringBuilder multipliers = new StringBuilder();

		if (ironman) {
			multipliers.append("Ironman (Respect): x").append(BaldnessConfig.IRONMAN_MULTIPLIER).append("\n");
			baldness *= BaldnessConfig.IRONMAN_MULTIPLIER;
		}

		if (isBaldHunter) {
			multipliers.append("Is GodHunter775: x").append(BaldnessConfig.GODHUNTER_MULTIPLIER).append("\n");
			baldness *= BaldnessConfig.GODHUNTER_MULTIPLIER;
		}

		builder.addField("**__Multipliers__**", multipliers.toString(), false);
		return baldness;
	}

	private static String getBaldnessRank(double baldness) {
		if (baldness < 2) return "Not Bald";
		if (baldness < 5) return "Slightly Bald";
		if (baldness < 9) return "Bald";
		if (baldness < 14) return "Very Bald";
		return "Extremely Bald";
	}

	// Helper methods
	private static long getSlayerLevel(JsonObject slayers, String slayerType) {
		try {
			return Utils.slayerLevel(slayers.getAsJsonObject(slayerType).get("xp").getAsLong());
		} catch (Exception e) {
			return 0;
		}
	}

	private static long getVampireSlayerLevel(JsonObject slayers) {
		try {
			return Math.min(5, Utils.skillLevel(slayers.getAsJsonObject("vampire").get("xp").getAsLong() / 2));
		} catch (Exception e) {
			return 0;
		}
	}

	private static long getBestiaryMilestone(JsonObject profile) {
		try {
			return profile.getAsJsonObject("bestiary").getAsJsonObject("milestone").get("last_claimed_milestone").getAsLong();
		} catch (Exception e) {
			return 0;
		}
	}

	private static String getMinionString(JsonObject profile) {
		try {
			return profile.getAsJsonObject("player_data").getAsJsonArray("crafted_generators").toString();
		} catch (Exception e) {
			return "";
		}
	}

	private static String getMinionDisplayName(String minionKey) {
		return minionKey.replace("_", " ").replace("12", "").replace("11", "").trim() + " Minion";
	}

	private static long getHotmLevel(JsonObject hotm) {
		try {
			return Utils.hotmLevel(hotm.get("experience").getAsLong());
		} catch (Exception e) {
			return 0;
		}
	}

	private static double getPowderMillions(JsonObject hotm, String powderType) {
		try {
			long current = hotm.get("powder_" + powderType).getAsLong();
			long spent = hotm.get("powder_spent_" + powderType).getAsLong();
			return (double) (current + spent) / 1000000;
		} catch (Exception e) {
			return 0;
		}
	}

	private static String getTrophyFishDisplayName(String fishKey) {
		return fishKey.replace("_diamond", "").replace("_", " ").replace("Diamond ", "Diamond ");
	}

	private static long getDojoPoints(JsonObject profile) {
		try {
			JsonObject dojo = profile.getAsJsonObject("nether_island_player_data").getAsJsonObject("dojo");
			long total = 0;
			String[] dojoTypes = {"dojo_points_mob_kb", "dojo_points_wall_jump", "dojo_points_archer", "dojo_points_sword_swap", "dojo_points_snake", "dojo_points_fireball", "dojo_points_lock_head"};

			for (String type : dojoTypes) {
				if (dojo.has(type)) {
					total += dojo.get(type).getAsLong();
				}
			}
			return total;
		} catch (Exception e) {
			return 0;
		}
	}

	private static long getMuseumDonations(JsonObject museum) {
		try {
			return museum.getAsJsonObject("items").size();
		} catch (Exception e) {
			return 0;
		}
	}

	private static long getVisitorCount(JsonObject garden) {
		try {
			return garden.getAsJsonObject("commission_data").get("total_completed").getAsLong();
		} catch (Exception e) {
			return 0;
		}
	}

	private static long getTimecharmsCount(JsonObject profile) {
		try {
			return profile.getAsJsonObject("rift").getAsJsonObject("gallery").getAsJsonArray("secured_trophies").size();
		} catch (Exception e) {
			return 0;
		}
	}

	private static String getRabbitDisplayName(String rabbitKey) {
		return rabbitKey.replace("_", " ").replace("zest zephyr", "Zest Zephyr") + " Rabbit";
	}
}