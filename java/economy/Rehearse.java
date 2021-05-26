package economy;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class Rehearse {
    public Rehearse(GuildMessageReceivedEvent e, String[] data) {
        if (Boolean.parseBoolean(data[19])) {
            long time = System.currentTimeMillis();
            Random random = new Random();
            long violins = Long.parseLong(data[0]);
            long violinsEarned = Long.parseLong(data[75]);
            if (time < Long.parseLong(data[7])) {
                long milliseconds = Long.parseLong(data[7]) - time;
                long hours = milliseconds / 3600000;
                milliseconds -= hours * 3600000;
                long minutes = milliseconds / 60000;
                milliseconds -= minutes * 60000;
                long seconds = milliseconds / 1000;
                milliseconds -= seconds * 1000;
                e.getChannel().sendMessage("You don't have the time to go to rehearsal that often, wait " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds!").queue();
            } else {
                long base = Calculate.CalculateAmount(e, data, random.nextInt(501) + 1750);
                double num = random.nextDouble();
                if (num > 0.75) {
                    violins += base;
                    e.getChannel().sendMessage("You rehearsed with your orchestra and earned " + base + ":violin:").queue();
                    violinsEarned += base;
                } else if (num > 0.5) {
                    num = random.nextDouble();
                    if (num > 0.5) {
                        data[51] = String.valueOf(Long.parseLong(data[51]) + 3);
                        e.getChannel().sendMessage("You found 3:rice: after rehearsal but didn't get any violins.").queue();
                    } else if (num > 0.1) {
                        data[62] = String.valueOf(Long.parseLong(data[62]) + 2);
                        e.getChannel().sendMessage("You found 2:bubble_tea: after you went to rehearsal.").queue();
                    } else {
                        data[63] = String.valueOf(Long.parseLong(data[63]) + 1);
                        e.getChannel().sendMessage("Ling Ling enjoyed your rehearsal session and blessed you.").queue();
                    }
                } else if (num > 0.2) {
                    num = random.nextDouble();
                    if (num > 0.65) {
                        e.getChannel().sendMessage("Your teacher approved your rehearsal.  Your tiger mom saw the comment, and gave you " + (long) (base * 0.1) + ":violin: in addition to the " + base + ":violin: that you earned.").queue();
                        base *= 1.1;
                    } else if (num > 0.40) {
                        e.getChannel().sendMessage("Your tiger mom approved your rehearsal.  She gave you " + (long) (base * 0.5) + ":violin: in addition to the " + base + ":violin: that you earned.").queue();
                        base *= 1.5;
                    } else if (num > 0.2) {
                        base *= 2;
                        e.getChannel().sendMessage("Brett and Eddy approved of your rehearsal and doubled the amount of violins you earned.  You got " + base + ":violin:").queue();
                    } else if (num > 0.05) {
                        base *= 5;
                        e.getChannel().sendMessage("It's raining violins!  You earned " + base + ":violin: you lucky dog").queue();
                    } else {
                        base *= 25;
                        e.getChannel().sendMessage("Ling Ling enjoyed your rehearsal but was displeased with some parts.  Nonetheless, he (she?) granted you with " + base + ":violin:").queue();
                    }
                    violins += base;
                    violinsEarned += base;
                } else {
                    long income = Long.parseLong(data[12]);
                    num = random.nextDouble();
                    if (num > 0.75) {
                        violins -= income / 100;
                        base *= 0.95;
                        violins += base;
                        e.getChannel().sendMessage("Oh no!  Your E String snapped during the rehearsal!  You had to borrow the concertmaster's violin, and only earned " + base + ":violin:  You eventually had to pay " + (income / 100) + ":violin: for a new E String.").queue();
                    } else if (num > 0.55) {
                        base *= 0.9;
                        violins += base;
                        e.getChannel().sendMessage("Your violin randomly went out of tune during the rehearsal.  You had to spend 4 minutes tuning it and were only able to earn " + base + ":violin:").queue();
                    } else if (num > 0.4) {
                        base *= 0.95;
                        e.getChannel().sendMessage("The orchestra had music stand problems, and page turning wasn't the best either.  You only earned " + base + ":violin:").queue();
                    } else if (num > 0.25) {
                        base *= 0.5;
                        violins += base;
                        violins -= income / 10;
                        e.getChannel().sendMessage("You hurt your wrist during the rehearsal and only got half of the effectiveness.  You earned " + base + ":violin: but ended up paying " + (income / 10) + ":violin: in hospital fees.").queue();
                    } else if (num > 0.15) {
                        base *= 0.2;
                        violins += base;
                        e.getChannel().sendMessage("Your bridge fell off during the rehearsal.  You spend the next half-hour trying to get it back on, and you only earned " + base + ":violin:").queue();
                    } else if (num > 0.05) {
                        violins -= income;
                        e.getChannel().sendMessage("You had a memory blank during rehearsal.  Your tiger mom took all of your earnings, in addition to another " + income + " for shaming yourself.").queue();
                    } else if (num > 0.015) {
                        e.getChannel().sendMessage("Your chin rest popped off your violin!  You take your violin to the luthier, who informs you that the violin will have to stay overnight.  You are not able to practise for 12 hours.").queue();
                        data[64] = String.valueOf(Long.parseLong(data[64]) + 43200000);
                        data[1] = String.valueOf(Long.parseLong(data[1]) + 43200000);
                    } else if (num > 0.005) {
                        e.getChannel().sendMessage("You decided to fake your solo.  Of course it didn't work and Ling Ling fined you " + violins * (income / 150000) + ":violin:").queue();
                        violins -= violins * (income / 150000);
                        base = 0;
                    } else {
                        e.getChannel().sendMessage("You dropped your violin.  How shameful.  All cooldowns except daily and gamble have had one day added to them, and you were fined " + violins * (income / 75000) + ":violin: for being careless in public.").queue();
                        violins -= violins * (income / 75000);
                        base = 0;
                        time += 86400000;
                        data[1] = String.valueOf(time);
                        data[7] = String.valueOf(time);
                        data[8] = String.valueOf(time);
                        data[64] = String.valueOf(time);
                    }
                    violinsEarned += base;
                }
                violins += base;
                violinsEarned += base;
                data[0] = String.valueOf(violins);
                data[75] = String.valueOf(violinsEarned);
                if (Boolean.parseBoolean(data[50])) {
                    data[7] = String.valueOf(time + 64740000);
                } else {
                    data[7] = String.valueOf(time + 86340000);
                }
                data[72] = String.valueOf(Long.parseLong(data[72]) + 1);
                new SaveData(e, data);
            }
        } else {
            e.getChannel().sendMessage("You must have an orchestra to rehearse with to use this command!").queue();
        }
    }
}