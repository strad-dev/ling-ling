package economy;

import eventListeners.GenericDiscordEvent;
import org.json.simple.JSONObject;
import processes.Numbers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Use {

	public static void generateArray(Map<String, Long> addItems, long rolls, long range, long min) {
		long[] temp = {0, 0, 0, 0, 0, 0, 0, 0}; //Grains, Plastic, Water, TeaBase, Wood, PineSap, Steel, HorseHair
		Random random = new Random();
		for(int i = 0; i < rolls; i++) {
			temp[random.nextInt(8)] += random.nextLong(range) + min;
		}
		try {
			addItems.replace("grains", addItems.get("grains") + temp[0]);
			addItems.replace("plastic", addItems.get("plastic") + temp[1]);
			addItems.replace("water", addItems.get("water") + temp[2]);
			addItems.replace("teaBase", addItems.get("teaBase") + temp[3]);
			addItems.replace("wood", addItems.get("wood") + temp[4]);
			addItems.replace("pineSap", addItems.get("pineSap") + temp[5]);
			addItems.replace("steel", addItems.get("steel") + temp[6]);
			addItems.replace("horseHair", addItems.get("horseHair") + temp[7]);
		} catch(Exception exception) {
			addItems.put("grains", temp[0]);
			addItems.put("plastic", temp[1]);
			addItems.put("water", temp[2]);
			addItems.put("teaBase", temp[3]);
			addItems.put("wood", temp[4]);
			addItems.put("pineSap", temp[5]);
			addItems.put("steel", temp[6]);
			addItems.put("horseHair", temp[7]);
		}
	}

	public static String generateMessage(Map<String, Long> addItems) {
		return Numbers.formatNumber(addItems.get("grains")) + Emoji.GRAINS
				+ Numbers.formatNumber(addItems.get("plastic")) + Emoji.PLASTIC
				+ Numbers.formatNumber(addItems.get("water")) + Emoji.WATER
				+ Numbers.formatNumber(addItems.get("teaBase")) + Emoji.TEABAG
				+ Numbers.formatNumber(addItems.get("wood")) + Emoji.WOOD
				+ Numbers.formatNumber(addItems.get("pineSap")) + Emoji.SAP
				+ Numbers.formatNumber(addItems.get("steel")) + Emoji.STEEL
				+ Numbers.formatNumber(addItems.get("horseHair")) + Emoji.HORSE_HAIR;
	}

	public static void addTime(JSONObject data, String item, long amount) {
		long original = (long) data.get(item);
		if(System.currentTimeMillis() < original) {
			data.replace(item, (long) data.get(item) + amount);
		} else {
			data.replace(item, System.currentTimeMillis() + amount);
		}
	}

	public static String pickItem(String item) {
		switch(item) {
			case "rice" -> {
				return "rice";
			}
			case "tea" -> {
				return "tea";
			}
			case "blessings", "blessing" -> {
				return "blessings";
			}
			case "vote", "free", "votebox", "freebox", "voteBox" -> {
				return "voteBox";
			}
			case "gift", "giftbox", "giftBox" -> {
				return "giftBox";
			}
			case "kits", "kit" -> {
				return "kits";
			}
			case "linglingBox", "linglingbox", "lingling", "llbox" -> {
				return "linglingBox";
			}
			case "crazyBox", "crazy", "crazybox" -> {
				return "crazyBox";
			}
			case "RNGesusBox", "rngesus", "rngesusbox" -> {
				return "RNGesusBox";
			}
			case "rosin" -> {
				return "rosin";
			}
			case "string" -> {
				return "string";
			}
			case "bowHair", "bowhair", "hair" -> {
				return "bowHair";
			}
			case "violinService", "violinservice", "service" -> {
				return "violinService";
			}
			default -> {
				return "nothing";
			}
		}
	}

	public static void use(GenericDiscordEvent e, String item1, String temp) {
		if(item1.isEmpty()) {
			e.reply("You can't use nothing.");
			return;
		}
		String item = pickItem(item1);
		JSONObject data = LoadData.loadData(e);
		Random random = new Random();
		Map<String, Long> addItems = new HashMap<>(0);
		long income = (long) data.get("income");
		double bonusItemDuration = ((long) data.get("qualityItems")) * 0.05 + 1;
		long itemAmount = (long) data.get(item);
		long useAmount;
		long extraRolls = income / 100000;
		long extraItems = income / 50000;
		double magicFindBonus = 1 + ((long) data.get("magicFind")) * 0.01;
		try {
			useAmount = Long.parseLong(temp);
		} catch(Exception exception) {
			if(temp.equals("max")) {
				useAmount = 2147483647;
			} else {
				e.reply("You have to either input `max` or an integer.");
				return;
			}
		}
		if(useAmount < 1) {
			e.reply("You can't use a negative amount of items.  Grow a brain.");
			return;
		}
		if(useAmount > itemAmount) {
			useAmount = itemAmount;
		}
		switch(item) {
			case "rice" -> {
				if((long) data.get(item) <= 0) {
					e.reply("You scourge your pantry but find no rice.  Then you remember you don't have any more.");
					return;
				} else if(income == 0) {
					e.reply("Very unwise of you to use this item when you have zero income as you would get zero violins.  Grow a brain.");
					return;
				} else {
					long baseAmount = income * 2 * useAmount;
					addItems.put("violins", (long) (baseAmount * magicFindBonus));
					e.reply("You ate " + Numbers.formatNumber(useAmount) + Emoji.RICE + "  The God of Rice gave you " + Numbers.formatNumber(baseAmount) + Emoji.VIOLINS +
							"\nYour Magic Find gave you a bonus of " + Numbers.formatNumber((long) (baseAmount * (magicFindBonus - 1))) + Emoji.VIOLINS);
				}
			}
			case "tea" -> {
				if((long) data.get("tea") <= 0) {
					e.reply("You scourge your fridge but find no more bubble tea.  Then you remember you don't have any more.");
					return;
				} else if(income == 0) {
					e.reply("Very unwise of you to use this item when you have zero income as you would get zero violins.  Grow a brain.");
					return;
				} else {
					long baseAmount = income * 6 * useAmount;
					addItems.put("violins", (long) (baseAmount * magicFindBonus));
					e.reply("You drank " + Numbers.formatNumber(useAmount) + Emoji.TEA + "  Brett and Eddy approved and gave you " + Numbers.formatNumber(baseAmount) + Emoji.VIOLINS +
							"\nYour Magic Find gave you a bonus of " + Numbers.formatNumber((long) (baseAmount * (magicFindBonus - 1))) + Emoji.VIOLINS);
				}
			}
			case "blessings" -> {
				if((long) data.get("blessings") <= 0) {
					e.reply("You already used all your blessings, run more commands to get back into Ling Ling's good graces!");
					return;
				} else if(income == 0) {
					e.reply("Very unwise of you to use this item when you have zero income as you would get zero violins.  Grow a brain.");
					return;
				} else {
					long add = 0;
					long bonus = 0;
					for(int i = 0; i < useAmount; i++) {
						double num = random.nextDouble();
						if(num > 0.5) {
							add++;
						} else if(num > 0.2) {
							add += 2;
						} else {
							add += 3;
						}
						if(random.nextDouble() < magicFindBonus - 1) {
							bonus++;
							add++;
						}
					}
					addItems.put("medals", add);
					String reply = "Ling Ling blessed you thanks to your " + Numbers.formatNumber(useAmount) + " performances!  You received " + Numbers.formatNumber(add) + Emoji.MEDALS;
					if(bonus > 0) {
						reply += "\nYour Magic Find created " + Numbers.formatNumber(bonus) + Emoji.MEDALS + " for you out of thin air!";
					}
					e.reply(reply);
				}
			}
			case "voteBox" -> {
				if((long) data.get("voteBox") <= 0) {
					e.reply("You already used all your Free Boxes!  Wait to get more!");
					return;
				} else {
					addItems.put("voteBox", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 3 + extraRolls, 3, 3 + extraItems); // initial: 3-5 x 3
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							long thisBonusHours = random.nextInt(3) + 1;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 1);
							} else {
								addItems.put("rice", 1L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 1);
							} else {
								addItems.put("tea", 1L);
							}
						}
						if(random.nextDouble() < 0.03 * magicFindBonus) {
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + 1);
							} else {
								addItems.put("medals", 1L);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 1);
							} else {
								addItems.put("blessings", 1L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("freeIncome")) {
								addItems.replace("freeIncome", addItems.get("freeIncome") + 1);
								addItems.replace("income", addItems.get("income") + 100);
							} else {
								addItems.put("freeIncome", 1L);
								addItems.put("income", 100L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.FREE_BOX + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("freeIncome")) {
						message += "\n**CRAZY RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("freeIncome")) + " income voucher(s), granting you a total of " +
								Numbers.formatNumber(addItems.get("income")) + Emoji.VIOLINS + "/hour extra income!";
					}
					e.reply(message);
				}
			}
			case "giftBox" -> {
				if((long) data.get("giftBox") <= 0) {
					e.reply("You already used all your Gift Boxes!  Convince your friends to gift you for more!");
					return;
				} else {
					addItems.put("giftBox", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 4 + extraRolls, 3, 4 + extraItems); // initial: 4-6 x 4
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							long thisBonusHours = random.nextInt(3) + 2;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 2);
							} else {
								addItems.put("rice", 2L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 2);
							} else {
								addItems.put("tea", 2L);
							}
						}
						if(random.nextDouble() < 0.04 * magicFindBonus) {
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + 1);
							} else {
								addItems.put("medals", 1L);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 2);
							} else {
								addItems.put("blessings", 2L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("benevolentBankers")) {
								addItems.replace("benevolentBankers", addItems.get("benevolentBankers") + 1);
							} else {
								addItems.put("benevolentBankers", 1L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.GIFT_BOX + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("benevolentBankers")) {
						message += "\n**CRAZY RARE DROP!**  In front of you appears " +
								Numbers.formatNumber(addItems.get("benevolentBankers")) + " banker(s)!  They are nice, and grant you a total of +" +
								Numbers.formatNumber(addItems.get("benevolentBankers")) + "% extra Bank Space!";
					}
					e.reply(message);
				}
			}
			case "kits" -> {
				if((long) data.get("kits") <= 0) {
					e.reply("You already used all your Musician Kits!");
					return;
				} else {
					addItems.put("kits", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 5 + extraRolls, 3, 6 + extraItems); // initial: 6-8 x 5
						if(random.nextDouble() < 0.15 * magicFindBonus) {
							long thisBonusHours = random.nextInt(3) + 2;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 3);
							} else {
								addItems.put("rice", 3L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 3);
							} else {
								addItems.put("tea", 3L);
							}
						}
						if(random.nextDouble() < 0.05 * magicFindBonus) {
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + 1);
							} else {
								addItems.put("medals", 1L);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 3);
							} else {
								addItems.put("blessings", 3L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("qualityItems")) {
								addItems.replace("qualityItems", addItems.get("qualityItems") + 1);
							} else {
								addItems.put("qualityItems", 1L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.MUSICIAN_KIT + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("qualityItems")) {
						message += "\n**CRAZY RARE DROP!**  " + Numbers.formatNumber(addItems.get("qualityItems")) +
								" bottle(s) of Item Quality Boost Potion appear in front of you!  Your items now last " +
								Numbers.formatNumber(addItems.get("qualityItems") * 5) + "% longer.";
					}
					e.reply(message);
				}
			}
			case "linglingBox" -> {
				if((long) data.get("linglingBox") <= 0) {
					e.reply("You already used all your Ling Ling Boxes!");
					return;
				} else {
					addItems.put("linglingBox", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 6 + extraRolls, 3, 8 + extraItems); // initial: 8-10 x 6
						if(random.nextDouble() < 0.15 * magicFindBonus) {
							long thisBonusHours = random.nextInt(4) + 3;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 4);
							} else {
								addItems.put("rice", 4L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 4);
							} else {
								addItems.put("tea", 4L);
							}
						}
						if(random.nextDouble() < 0.05 * magicFindBonus) {
							long medals = random.nextInt(2) + 1;
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + medals);
							} else {
								addItems.put("medals", medals);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 4);
							} else {
								addItems.put("blessings", 4L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("bonusMedals")) {
								addItems.replace("bonusMedals", addItems.get("bonusMedals") + 1);
							} else {
								addItems.put("bonusMedals", 1L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.LING_LING_BOX + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("bonusMedals")) {
						message += "\n**CRAZY RARE DROP!**  Ling Ling has shown favor upon you, and is granting you +" +
								Numbers.formatNumber(addItems.get("bonusMedals")) + Emoji.MEDALS + " bonus medals each time you run `daily`!";
					}
					e.reply(message);
				}
			}
			case "crazyBox" -> {
				if((long) data.get("crazyBox") <= 0) {
					e.reply("You already used all your Crazy Person Boxes!");
					return;
				} else {
					addItems.put("crazyBox", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 7 + extraRolls, 3, 10 + extraItems); // initial: 10-12 x 7
						if(random.nextDouble() < 0.2 * magicFindBonus) {
							long thisBonusHours = random.nextInt(4) + 3;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 5);
							} else {
								addItems.put("rice", 5L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 5);
							} else {
								addItems.put("tea", 5L);
							}
						}
						if(random.nextDouble() < 0.05 * magicFindBonus) {
							long medals = random.nextInt(3) + 1;
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + medals);
							} else {
								addItems.put("medals", medals);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 5);
							} else {
								addItems.put("blessings", 5L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("bonusInterest")) {
								addItems.replace("bonusInterest", addItems.get("bonusInterest") + 1);
							} else {
								addItems.put("bonusInterest", 1L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.CRAZY_BOX + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("bonusInterest")) {
						message += "\n**CRAZY RARE DROP!**  The Bank of TwoSet is imporessed by your dedication and grants you +`" + addItems.get("bonusInterest") * 0.1 + "`% extra interest!";
					}
					e.reply(message);
				}
			}
			case "RNGesusBox" -> {
				if((long) data.get("RNGesusBox") <= 0) {
					e.reply("You already used all your RNGesus Boxes!");
					return;
				} else {
					addItems.put("RNGesusBox", -useAmount);
					long bonusHours = 0;
					for(int i = 0; i < useAmount; i++) {
						generateArray(addItems, 8 + extraRolls, 4, 12 + extraItems); // initial: 12-15 x 8
						if(random.nextDouble() < 0.2 * magicFindBonus) {
							long thisBonusHours = random.nextInt(5) + 4;
							bonusHours += thisBonusHours;
							if(addItems.containsKey("violins")) {
								addItems.replace("violins", addItems.get("violins") + income * thisBonusHours);
							} else {
								addItems.put("violins", income * bonusHours);
							}
						}
						if(random.nextDouble() < 0.25 * magicFindBonus) {
							if(addItems.containsKey("rice")) {
								addItems.replace("rice", addItems.get("rice") + 6);
							} else {
								addItems.put("rice", 6L);
							}
						}
						if(random.nextDouble() < 0.1 * magicFindBonus) {
							if(addItems.containsKey("tea")) {
								addItems.replace("tea", addItems.get("tea") + 6);
							} else {
								addItems.put("tea", 6L);
							}
						}
						if(random.nextDouble() < 0.05 * magicFindBonus) {
							long medals = random.nextInt(4) + 1;
							if(addItems.containsKey("medals")) {
								addItems.replace("medals", addItems.get("medals") + medals);
							} else {
								addItems.put("medals", medals);
							}
						}
						if(random.nextDouble() < 0.02 * magicFindBonus) {
							if(addItems.containsKey("blessings")) {
								addItems.replace("blessings", addItems.get("blessings") + 6);
							} else {
								addItems.put("blessings", 6L);
							}
						}
						if(random.nextDouble() < 0.002 * magicFindBonus) {
							if(addItems.containsKey("RNGesusItemThatDoesAbsolutelyNothingLMAO")) {
								addItems.replace("RNGesusItemThatDoesAbsolutelyNothingLMAO", addItems.get("RNGesusItemThatDoesAbsolutelyNothingLMAO") + 1);
							} else {
								addItems.put("RNGesusItemThatDoesAbsolutelyNothingLMAO", 1L);
							}
						}
					}
					String message = "You opened " + Numbers.formatNumber(useAmount) + Emoji.RNGESUS_BOX + "  You received the following items...\n\n" + generateMessage(addItems) + "\n";
					if(addItems.containsKey("violins")) {
						message += "\n**BONUS!**  You received an additional " + Numbers.formatNumber(bonusHours) + " hours of income totaling to " +
								Numbers.formatNumber(income * bonusHours) + Emoji.VIOLINS;
					}
					if(addItems.containsKey("rice")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("rice")) + Emoji.RICE + " in the box!";
					}
					if(addItems.containsKey("tea")) {
						message += "\n**BONUS!**  You found " + Numbers.formatNumber(addItems.get("tea")) + Emoji.TEA + " in the box!";
					}
					if(addItems.containsKey("medals")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("medals")) + Emoji.MEDALS + " sitting at the bottom of the box!";
					}
					if(addItems.containsKey("blessings")) {
						message += "\n**RARE DROP!**  You found " + Numbers.formatNumber(addItems.get("blessings")) + Emoji.BLESSING + " in the box!";
					}
					if(addItems.containsKey("RNGesusItemThatDoesAbsolutelyNothingLMAO")) {
						message += "\n\n# INSANE DROP\nYou found " + Numbers.formatNumber(addItems.get("RNGesusItemThatDoesAbsolutelyNothingLMAO")) +
								" \"RNGesus Item That Does Absolutely Nothing LMAO\"!  Now you can flex this utterly useless item in your inventory! https://imgur.com/a/SSjcgz3";
					}
					e.reply(message);
				}
			}
			case "rosin" -> {
				if((long) data.get("rosin") <= 0) {
					e.reply("You look in your violin case, but find no rosin left.  You are disappointed.");
					return;
				} else {
					addItems.put("rosin", -useAmount);
					addTime(data, "rosinExpire", (long) (useAmount * 180000000 * bonusItemDuration));
					e.reply(Emoji.ROSIN + "  You apply rosin to your bow.  Your rosin now lasts for an additional " +
							Numbers.formatNumber((long) (50 * useAmount * bonusItemDuration)) + " hours before needing to be reapplied.");
				}
			}
			case "string" -> {
				if((long) data.get("string") <= 0) {
					e.reply("You scourge your stocks, but you can't find violin strings.  You then remember that you have to buy some, then promptly forget.");
					return;
				} else {
					addItems.put("string", -useAmount);
					addTime(data, "stringsExpire", (long) (useAmount * 360000000 * bonusItemDuration));
					e.reply(Emoji.STRING + "  You change the strings on your violin.  Your strings now last for an additional " +
							Numbers.formatNumber((long) (100 * useAmount * bonusItemDuration)) + " hours before needing to be changed.");
				}
			}
			case "bowHair" -> {
				if((long) data.get("bowHair") <= 0) {
					e.reply("You scourge your stocks, but you can't find extra bow hair.  You then remember that you have to buy some, then promptly forget.");
					return;
				} else {
					addItems.put("bowHair", -useAmount);
					addTime(data, "bowHairExpire", (long) (useAmount * 540000000 * bonusItemDuration));
					e.reply(Emoji.BOW_HAIR + "  You asked Olaf to rehair your bow, to which he agreed.  Your bow now lasts for an additional " +
							Numbers.formatNumber((long) (150 * useAmount * bonusItemDuration)) + " hours before needing to be rehaired.");
				}
			}
			case "violinService" -> {
				if((long) data.get("violinService") <= 0) {
					e.reply("You go to Olaf to get your violin repaired, but forget to bring materials.  He kicks you out of the store.");
					return;
				} else {
					addItems.put("violinService", -useAmount);
					addTime(data, "serviceExpire", (long) (useAmount * 900000000 * bonusItemDuration));
					e.reply(Emoji.SERVICE + "  You asked Olaf to service your violin, to which he agreed.  Your violins now lasts for an additional " +
							Numbers.formatNumber((long) (250 * useAmount * bonusItemDuration)) + " hours before needing to be reserviced.");
				}
			}
			default -> {
				e.reply("You can't use something that doesn't exist, that doesn't make sense.");
				return;
			}
		}
		addItems.put(item, -useAmount);
		for(Map.Entry<String, Long> entry : addItems.entrySet()) {
			data.replace(entry.getKey(), (long) data.get(entry.getKey()) + entry.getValue());
			if(entry.getKey().equals("violins")) {
				data.replace("earnings", (long) data.get("earnings") + entry.getValue());
			}
		}
		SaveData.saveData(e, data);
	}
}