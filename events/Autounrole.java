package events;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Autounrole extends ListenerAdapter {
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if(e.getChannel().getId().equals("734697501059448892") && e.getGuild().getId().equals("670725611207262219") && !Objects.requireNonNull(e.getMember()).getUser().isBot()) {
            try {
                if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e5")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697401176162304"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e7")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697401805176903"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e8")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697402539311104"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e9")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697403063730177"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e6")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697403612921908"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f77f")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697404359508009"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7ea")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697405030596620"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+2764")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697406213652500"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3bb")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697414677626890"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3b9")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697415348846592"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f62b")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697415550042174"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f914")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697416598618172"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f9bb")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697417227894925"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3ba")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697417978675220"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f941")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697418746232833"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3a4")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697419501207582"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f389")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697425595531285"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4e3")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697426216026223"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f916")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697426635718667"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4ca")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("747954053660540928"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4a9")) {
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("750876814842527754"))).queue();
                }
            } catch (Exception exception) {
                if (e.getReactionEmote().getEmote().getId().equals("688452024009883669")) { //twoset
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697423703769129"))).queue();
                } else if (e.getReactionEmote().getEmote().getId().equals("688449820410773532")) { //lingling40hrs
                    e.getGuild().removeRoleFromMember(e.getMember(), Objects.requireNonNull(e.getGuild().getRoleById("734697424936763432"))).queue();
                }
            }
        } else if(e.getChannel().getId().equals("743628243529236551") && !Objects.requireNonNull(e.getUser()).isBot()) {
            try {
                if (e.getReactionEmote().getAsCodepoints().equals("U+1f4e1")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("743630302122999868"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4e5")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("743630338252734484"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3bb")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("719242743372840960"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f389")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("747878836821033002"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4a9")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749142984469250110"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f3aa")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749746678365683794"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f52a")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749746588355788801"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f534")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147938546253895"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+2b55")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147417982664796"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e0")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147133692739685"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e1")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147873739800587"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e2")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147603597262900"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e9")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749148475098398720"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f332")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147803846180864"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f49a")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749753849547456623"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f535")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147268422041631"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f30a")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749148900585111573"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e3")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749148307984482324"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f7e4")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("749147684535009352"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f4e3")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("760397196935561246"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f522")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("764915586505113610"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f468")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("770665490111791154"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f469")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("770665666168487988"))).queue();
                } else if (e.getReactionEmote().getAsCodepoints().equals("U+1f937")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("770705353385050124"))).queue();
                }
            } catch (Exception exception) {
                if (e.getReactionEmote().getId().equals("630833216755859466")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("756012819811401730"))).queue();
                } else if (e.getReactionEmote().getId().equals("664907911390167060")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("762882312978825246"))).queue();
                } else if (e.getReactionEmote().getId().equals("760749754178732043")) {
                    e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), Objects.requireNonNull(e.getGuild().getRoleById("766497504027475989"))).queue();
                }
            }
        }
    }
}