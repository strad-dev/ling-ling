package regular;

import eventListeners.GenericDiscordEvent;
import processes.Utils;

import java.util.Random;

public class Kill {
	public static void kill(GenericDiscordEvent e, String target) {
		String check = target.toLowerCase();
		String author = e.getAuthor().getEffectiveName();
		for(int i = 0; i < check.length(); i ++) {
			int character = check.charAt(i);
			if(character < 32 || character > 32 && character <  97 || character > 122) {
				e.reply("nope, i fixed this loophole, no more non-letter characters");
				return;
			}
		}
		if(check.equalsIgnoreCase("i") || check.contains("ling") || check.contains("iing") || check.contains("llng") || check.contains("733409243222507670")) {
			e.reply("Nice try but you cannot kill me");
		} else if(check.contains("strad") || check.contains("straf") || check.contains("dev") || check.contains("jacqueline")
				|| check.contains("619989388109152256") || check.contains("488487157372157962")) {
			e.reply("Imagine trying to kill the devs smh");
		} else if(check.contains(author.toLowerCase()) || author.toLowerCase().contains(check) || check.contains("self")) {
			e.reply("Do not harm yourself.  I will not allow it.");
		} else if(check.contains("ludwig") || check.contains("beethoven")) {
			e.reply("killing the best composer ever is ILLEGAL.  i am banning you.");
		} else if(Utils.containsBadLanguage(check)) {
			e.reply("NICE TRY");
		} else {
			Random random = new Random();
			int i = random.nextInt(104);
			switch(i) {
				case 0 -> e.reply("death.fell.accident.water");
				case 1 -> e.reply(target + "'s ears were blasted apart with a piccolo playing 10000 Hz at fortissimo.");
				case 2 -> e.reply("Ling Ling deemed " + target + " unworthy of violin so he (she?) forced " + target + " to play the viola.  Their ego was permanently damaged.");
				case 3 -> e.reply(target + " thought it was a good idea to play the sousaphone after eating chili pepper.");
				case 4 -> e.reply(target + " was blinded because they used light mode.");
				case 5 -> e.reply(author + " started punching " + target + ".");
				case 6 -> e.reply(author + " somehow got " + target + " into their Minecraft world and then " + target + " was dumb enough to light TNT.");
				case 7 -> e.reply(target + " stuffed too much chili pepper down their throat and the results were rather explosive.");
				case 8 -> e.reply("A bomb detonated over " + target + "'s head.");
				case 9 -> e.reply(target + " destroyed Jacqueline's Stradivarius so Jacqueline destroyed " + target);
				case 10 -> e.reply(target + " tripped over a tripwire and fell into the Void.");
				case 11 -> e.reply(target + " went to the zoo and got trampled by 20 elephants.");
				case 12 -> e.reply(author + " played a very high harmonic on their violin and exploded " + target + "'s eardrums.");
				case 13 -> e.reply(target + " was run over by " + author + " because " + target + " was crossing the street while playing a sousaphone.");
				case 14 -> e.reply(target + " fell off a cliff because they were not paying attention to where they were going.  Reason?  Using Simply Piano app (sacrilegious).");
				case 15 -> e.reply(target + " was brutally murdered with a viola for displeasing the conductor.");
				case 16 -> e.reply(target + " tried to steal 2000 pounds worth of gold from " + author + " but they were caught and ended up crushing themselves under the gold.");
				case 17 -> e.reply(target + " was sneak-attacked by " + author + ".");
				case 18 -> e.reply(target + " tripped over " + author + "'s foot while reading music and walking.");
				case 19 -> e.reply(author + " clobbers " + target + " with a clarinet because " + target + " was being dumb.");
				case 20 -> e.reply(target + " abused bugs on Tacoshack for two years.  Cole just found out and their $500 000/hour Shack was deleted.");
				case 21 -> e.reply(target + " decided to fight the Ender Dragon with nothing but their fists.");
				case 22 -> e.reply(target + " hit a Zombified Piglin in an area swarming with them.  They had only an axe and got overwhelmed.");
				case 23 -> e.reply(target + " ventured into an Abandoned Mineshaft and came across 3 Dungeons and a Cave Spider spawner.  " +
						"They didn't have torches and were dumb enough not to break them.");
				case 24 -> e.reply(target + " was killed by Ling Ling because they thought Axes were better than Swords.");
				case 25 -> e.reply(target + " was playing with lava.");
				case 26 -> e.reply(target + " fought Ling Ling.  They lost to his (her?) Sharpness 32767 Sword.");
				case 27 -> e.reply(target + " attacked Beethoven, who had Throns 32767 armor.");
				case 28 -> e.reply(target + " was squashed by a falling Anvil whilst attempting to escape Herobrine");
				case 29 -> e.reply(target + " fell down a cliff.");
				case 30 -> e.reply(target + " flew into unloaded chunks and died for no reason.");
				case 31 -> e.reply(target + " thought Creepers were cute and invited one into their house.");
				case 32 -> e.reply(target + " attacked an Admin and got the ban hammer.");
				case 33 -> e.reply(target + " tried killing an iron golem.  The iron golem got the upper hand");
				case 34 -> e.reply(target + " played out of tune in front of the Berlin Philharmonic and they were ridiculed to death by the Publik.");
				case 35 -> e.reply("During a music test " + target + " was called out for cheating.");
				case 36 -> e.reply(target + " is so bad at Minecraft they managed to die in one second in Peaceful Mode.  On a Superflat world.  Full of water.");
				case 37 -> e.reply(target + " tried playing their piccolo solo but it broke so many eardrums that the conductor threw them off the stage into the audience headfirst.  Everyone laughed.");
				case 38 -> e.reply(target + " was shot by " + author + " using [ 15 Notes a Second woB-X ]");
				case 39 -> e.reply(target + " walked into a cactus whilst trying to escape " + author);
				case 40 -> e.reply(target + " experienced Kinetic Energy");
				case 41 -> e.reply(target + " died.  While flying over unloaded chunks.");
				case 42 -> e.reply(target + " was blown up by " + author + " using Ling Ling's Wrath");
				case 43 -> e.reply(target + " was killed by Intentional Game Design [MCPE-28723]");
				case 44 -> e.reply(target + " was squashed by a falling anvil");
				case 45 -> e.reply(target + " went off with a bang due to a firework fired from [ 15 Notes a Second woB-X ] by " + author);
				case 46 -> e.reply(target + " tried to swim in lava");
				case 47 -> e.reply(target + " was struck by lightning");
				case 48 -> e.reply(target + " discovered the floor was lava");
				case 49 -> e.reply(target + " walked into danger zone due to " + author);
				case 50 -> e.reply(target + " was squashed by " + author);
				case 51 -> e.reply(target + " was poked to death by a sweet berry bush whilst trying to escape " + author);
				case 52 -> e.reply(target + " was killed by [ 15 Notes a Second Chestplate ] trying to hurt " + author);
				case 53 -> e.reply(target + " was impaled by " + author + " with [ Zeus's Trident ]");
				case 54 -> e.reply(target + " didn't want to live in the same world as " + author);
				case 55 -> e.reply(target + " was roasted in dragon breath");
				case 56 -> e.reply(target + " was killed by even more magic");
				case 57 -> e.reply(target + " was too soft for this world (" + author + " helped)");
				case 58 -> e.reply(target + " stepped on a landmine and suffered major injuries.");
				case 59 -> e.reply(author + " backstabbed " + target + ".  " + target + " was foolishly holding a shield and died.");
				case 60 -> e.reply(target + " forgot to equip a Totem of Undying in a PVP battle against " + author + ".");
				case 61 -> e.reply(target + " was yeeted by the Ender Dragon.  They failed a waterbucket MLG.");
				case 62 -> e.reply(target + " forgot to put on their elytra and plunged to their death to a lava pool.");
				case 63 -> e.reply(target + " forgot to put on their armor before venturing out, and got blown up by a Creeper.");
				case 64 -> e.reply(target + " was roasted in dragon breath");
				case 65 -> e.reply(":hammer: " + target + " was successfully banned!");
				case 66 -> e.reply(target + " died and lost 9 223 372 036 854 775 807:violin:");
				case 67 -> e.reply(target + " stepped on a lego.  Just a note for those who don't know: The difference between a lego and a landmine is that one kills you and the other is a landmine.");
				case 68 -> e.reply(target + " suffered extreme lag, and got comboed and crit out by " + author);
				case 69 -> e.reply(target + " was killed by a fish in the shadow realm.");
				case 70 -> e.reply(target + " only had 7 Fairy Souls and didn't put Hot Potato Books on their Lapis Armor.  Needless to say they got one-tapped by the first mob in the Entrance.");
				case 71 -> e.reply(target + " did not have SkyBlock Level 400 and thus had low HP.  Their ego was crushed by this fact.");
				case 72 -> e.reply(target + " thought a Hyperion was good enough for Master Mode.  Turns out, they were very wrong, and got one tapped a hundred times over by the first mob.");
				case 73 -> e.reply(target + " underestimated the power of **MASTER Bonzo**'s balloons and was immediately killed when trying to out-DPS him.");
				case 74 -> e.reply("**MASTER Scarf** saw weakness in " + target + " and blew out their insides with mass wither skulls.");
				case 75 -> e.reply("The **MASTER Professor** deemed " + target + " incompetent, and unleashed his guardians on them.");
				case 76 -> e.reply(target + " skipped out on Shadow Warp, and was swarmed by **MASTER Thorn**'s mobs during the cooldowns of Implosion and Wither Shield.");
				case 77 -> e.reply(target + " did not have enough DPS to instakill **MASTER Livid**, and paid dearly with their life.");
				case 78 -> e.reply(target + " forgot their Gyrokinetic Wand, and died a gruesome death to **MASTER Sadan**'s Terracottas.");
				case 79 -> e.reply(target + " hit one of **MASTER Sadan**'s Golems, thinking to steal the Archer's Combat XP.  The Golem got the upper hand, and the Archer still got their Combat XP.");
				case 80 -> e.reply(target + " stepped out of the Tank's radius as **MASTER Sadan** unleashed his Giants.  Needless to say, a boulder ended them.");
				case 81 -> e.reply(target + " didn't realize that **MASTER Sadan** revived his Giants, and was blown to bits by all four Giants.");
				case 82 -> e.reply(target + " was a failure at getting Crystals, and died to lava.");
				case 83 -> e.reply(target + " thought that Jerry-chine Gun was the best way to get Crystals.  However, a Chad Spring Boots user yoinked their crystal.  Four times.  " +
						target + " then realized that their ways were wrong, and was killed by **MASTER Maxor** as they were contemplating their life choices.");
				case 84 -> e.reply("In an attempt to lure **MASTER Maxor** into a matter-splitting laser, " + target + " accidentally split their own matter.");
				case 85 -> e.reply(target + " decided not to kill **MASTER Storm**'s Wither Guards.  Of course, " + target +
						" was foolishly using a pet item that was not a Shelmet, got knocked out from under the pillar, and was smited instantly by **MASTER Storm**'s lightning.");
				case 86 -> e.reply(target + " thought using Wither Cloak would save them from **MASTER Storm**'s lightning.  They were woefully wrong.  " +
						"Y'all this is M7, do you really think it's this easy???");
				case 87 -> e.reply(target + " still thought that Jerry-chine Gun was the best way of getting around, and continued to use it to reach Terminals.  " +
						"This time, they had their Terminal stolen every time by the Chad Spring Boots user.  " +
						"They were killed in **MASTER Goldor**'s frenzy as they continued contemplating their life choices.");
				case 88 -> e.reply(target + " did not do the correct Terminal, and was kicked from the party.  In anger, " + target +
						" jumped into the lava, but only ended up lagging themselves out.  So now they are dead AND get no loot >:)");
				case 89 -> e.reply(target + " could not do enough damage to **MASTER Goldor** in time, leading to the entire team being pulverized in Wither Skulls.");
				case 90 -> e.reply(target + " was not paying attention to which platforms **MASTER Necron** destroyed.  They immediately burned to a crisp in lava.");
				case 91 -> e.reply(target + " could not do enough damage to **MASTER Necron** before he went into Tier 4 Voidgloom Seraph phase, leading to " + target +
						" dying due to Wither Radiation.");
				case 92 -> e.reply(target + " walked into **MASTER Necron**'s Frenzy, and immediately dropped dead.");
				case 93 -> e.reply(target + " was a complete idiot and could not tell Red from Blue.  They were ridiculed by **THE WITHER KING**, then immediately died.  " +
						"Yes, being colorblind kills you in M7.  Deal with it.");
				case 94 -> e.reply(target + " was utterly confused, and let all five **WITHERED DRAGON**s spawn.  The resulting ritual resulted in the entire team's demise.  " +
						"But hey, at least you still roll for Handle!");
				case 95 -> e.reply(target + " stood under the **POWER DRAGON**'s TNT.  This was a mistake that cost their life.");
				case 96 -> e.reply(target + " forgot they were playing the Healer class, and let themselves take fatal damage.");
				case 97 -> e.reply(target + " forgot they were playing the Tank class, and was destroyed in an instant when trying to take their teammate's damage in a DPS setup.");
				case 98 -> e.reply(target + " forgot they were NOT playing the Tank (or Healer) class, and ran headfirst into a swarm of mobs.  " +
						"The swarm of mobs won over forgetfulness and stupidity.");
				case 99 -> e.reply(target + " forgot that Right Click Mage sucks ass in Master Mode, and found out the hard way by dying repeatedly.");
				case 100 -> e.reply(target + " did not realize the Tier 4 Voidgloom Seraph had thrown a beacon, and was killed for forgetting about it.");
				case 101 -> e.reply(target + " thought the Tier 4 Voidgloom Seraph's lasers were harmless.  They were very quickly proven wrong.");
				case 102 -> e.reply(target + " made a SUPER BLUNDER <:blunder:1090392764635680818><:blunder:1090392764635680818><:blunder:1090392764635680818> in a chess game against " + author +
						" by hanging their Queen, two Rooks, two Bishops, and a Knight while simultaneously allowing Mate in 1.");
				case 103 -> e.reply(target + " fell for the Fool's Mate.  How foolish.");
				default -> e.reply("Ling Ling is a benevolent violinist god so nice try but no.");
			}
		}
	}
}