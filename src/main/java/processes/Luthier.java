package processes;

import economy.Achievement;
import economy.Emoji;
import economy.RNGesus;
import eventListeners.GenericDiscordEvent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.json.simple.JSONObject;

import java.util.Objects;
import java.util.Random;

public class Luthier {
	public static void luthier(GenericDiscordEvent e, JSONObject data, String answer) {
		if(data != null) {
			Random random = new Random();
			if(!(boolean) data.get("hasWord")) {
				double chance = (double) data.get("chance");
				if(random.nextDouble() <= chance && !e.getChannel().getId().equals(data.get("channel").toString())) {
					TextChannel channel = Objects.requireNonNull(e.getGuild()).getTextChannelById(data.get("channel").toString());
					String[] words = {"pianississimo", "fortississimo", "lento", "fortepiano", "a tempo", "allargando", "piu mosso", "meno mosso", "arpeggio", "chromatic",
							"counterpoint", "basso continuo", "staccato", "legato", "accent", "violin", "piano", "cello", "bass", "flute",
							"piccolo", "clarinet", "debussy", "ravel", "prokofiev", "stravinsky", "oboe", "bassoon", "trumpet", "french horn",
							"trombone", "tuba", "percussion", "orchestra", "liszt", "conductor", "piece", "bach", "vivaldi", "adagio",
							"handel", "mozart", "haydn", "beethoven", "chopin", "paganini", "mendelssohn", "rachmaninoff", "tchaikovsky", "sibelius",
							"mahler", "eroica", "pastoral", "ode to joy", "schubert", "schumann", "symphony", "concerto", "sacrilegious", "brett",
							"eddy", "twoset", "practice", "ling ling", "concertmaster", "espressivo", "meno", "molto", "pesante", "piu",
							"poco", "scherzando", "sempre", "senza", "subito", "sostenuto", "tenuto", "ray chen", "hilary hahn", "lamentable",
							"amazing", "interesting", "r/lingling40hrs", "jacqueline", "stradivarius", "guarneri", "rubato", "con brio", "dolce", "sacrinterestinglegious",
							"repertoire", "caprice", "col legno", "pianissimo", "forte", "sonatina", "clementi", "czerny", "exposition", "development",
							"recapitulation", "rondo", "polyphony", "homophony", "brahms", "fortissimo", "crescendo", "andantino", "diminuendo", "allegretto",
							"turn", "ritardando", "accelerando", "waltz", "clair de lune", "prelude", "partita", "sonata", "fugue", "baroque",
							"classical", "romantic", "larghissimo", "mordent", "largo", "andante", "moderato", "grave", "allegro", "opus",
							"tre corde", "una corda", "con sordino", "senza sord", "vivace", "presto", "prestissimo", "appoggiatura", "breve", "semibreve",
							"minim", "crotchet", "quaver", "semiquaver", "demisemiquaver", "hemidemisemiquaver", "fermata", "tonic", "supertonic", "mediant",
							"subdominant", "dominant", "submediant", "leading tone", "major scale", "natural minor scale", "harmonic minor scale", "melodic minor scale", "major triad", "minor triad",
							"augmented triad", "devil's tritone", "interval", "major seventh", "dominant seventh", "minor seventh", "half diminished seventh", "diminished seventh", "suspended chord"};
					String word = words[random.nextInt(words.length)];
					String original = word;
					long money = 0;
					for(int i = 0; i < word.length(); i++) {
						money += random.nextInt(401) + 1800;
					}
					money *= (long) data.get("multiplier");
					char[] scrambler = new char[word.length()];
					int i = 0;
					StringBuilder send = new StringBuilder().append(original);
					while(send.toString().equals(original) || Utils.containsBadLanguage(send.toString())) {
						while(!word.isEmpty()) {
							int num = random.nextInt(word.length());
							StringBuilder newWord = new StringBuilder();
							char temp = word.charAt(num);
							scrambler[i] = temp;
							for(int j = 0; j < word.length(); j++) {
								if(j != num) {
									newWord.append(word.charAt(j));
								}
							}
							word = newWord.toString();
							i++;
						}
						send = new StringBuilder();
						for(i = 0; i < scrambler.length; i++) {
							send.append(scrambler[i]);
						}
					}
					assert channel != null;
					if(money == 0) {
						money = 1;
						channel.sendMessage("Olaf needs your help!  Unscramble `" + send + "` to get `1` " + Emoji.VIOLINS).queue();
					} else {
						channel.sendMessage("Olaf is giving away violins!  Unscramble `" + send + "` to get " + Utils.formatNumber(money) + Emoji.VIOLINS).queue();
					}
					data.replace("hasWord", true);
					data.replace("amount", money);
					data.replace("word", original);
					DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
				}
			} else if((boolean) data.get("hasWord") && !e.getAuthor().isBot() && !answer.isEmpty()) {
				if(answer.equals("none")) {
					e.reply("You have to give an answer, stupid.");
					return;
				}
				String target = data.get("word").toString();
				long gain = (long) data.get("amount");
				if(answer.equalsIgnoreCase(target)) {
					if(e.getChannel().getId().equals(data.get("channel").toString())) {
						JSONObject userData = DatabaseManager.getDataByUser(e, "Economy Data");
						if(userData == null) {
							e.reply("You don't even have a profile, where would you store your violins???  Run `/start` **in a bot command channel** to get one!");
							return;
						}
						String name = e.getAuthor().getEffectiveName();
						if(name.contains("@everyone") || name.contains("@here") || name.contains("<@&") || name.contains("nigg")) {
							name = "A user who thought they were trying to be funny";
						}
						userData.replace("violins", (long) userData.get("violins") + gain);
						userData.replace("earnings", (long) userData.get("earnings") + gain);
						userData.replace("luthiers", (long) userData.get("luthiers") + 1);
						e.reply("**" + name + "** unscrambled `" + target + "` and gained " + Utils.formatNumber(gain) + Emoji.VIOLINS);
						RNGesus.lootbox(e, userData);
						Achievement.calculateAchievement(e, userData, "luthiers", "English Major");
						DatabaseManager.saveDataByUser(e, "Economy Data", userData);
						data.replace("hasWord", false);
						data.replace("word", "blank");
						DatabaseManager.saveDataByGuild(e, "Luthier Data", data);
					} else {
						e.reply("Please answer in the dedicated Luthier channel.  <#" + data.get("channel") + ">");
					}
				} else {
					try {
						e.getMessage().addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("U+274C")).queue();
					} catch(Exception exception) {
						e.replyPrivate("Wrong answer!");
						e.getChannel().sendMessage("**" + e.getAuthor().getEffectiveName() + "**: " + answer).complete().addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("U+274C")).queue();
					}
				}
			}
		}
	}
}