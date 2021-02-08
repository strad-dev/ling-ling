import eventListeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        JDA jda;    
        try (BufferedReader rdr = new BufferedReader(new FileReader("C:\\Users\\ying\\Desktop\\Ling Ling Bot Data\\token.txt"))) {
            jda = JDABuilder.createDefault(rdr.readLine())
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new Autoroles())
                    .addEventListeners(new Autounrole())
                    .addEventListeners(new Join())
                    .addEventListeners(new Leave())
                    .addEventListeners(new Receiver())
                    .addEventListeners(new BotJoin())
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .build();
            jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            jda.getPresence().setActivity(Activity.playing("violin forty hours a day."));
        } catch(Exception e) {
            throw new FileNotFoundException();
        }
    }
}

//serversettings format: autoresponse VIOLA leveling

//levelsettings format: minXP, maxXP, cooldown (sec)
//channelblacklist format: channelID, channelID, channelID, etc.
//rolerewards format: level, role\nlevel, role\nlevel, role\n etc.

// [0] violins workCooldown workLevel gambleCooldown gambleLevel robCooldown robLevel rehearseCooldown performCooldown own1 own2 activeInsurance
// [12] hourlyIncome violinQuality skillLevel lessonQuality stringQuality bowQuality hasMath
// [19] hasOrchestra, piccolo, flute, oboe, clarinet, bassoon, contrabassoon, horn, trumpet, trombone, tuba, timpani, percussion, first, second, cello, bass, piano, harp, S, A, T, B, soloists
// [43] hallLevel, conductor, advertising, ticket, streak, dailyCooldown, dailyExpire, faster
// [51] rice, thirdP, secondP, firstP, medals, extraIncome, extraCommandIncome, higherWinrate, higherRobrate, stealShield, violinDuplicator, tea, blessing

//ex. 100 16000000000 50 16000000000 25 16000000000 15 16000000000 16000000000 true false 1 10000 5 5 5 5 5 true true true 1 1 1 1 true 1 1 1 1 1 1 20 20 15 5 2 true 20 20 20 20 4 5 5 20 5 0 16000000000 16000000000 true 0 3 2 1 10 true true false true true false