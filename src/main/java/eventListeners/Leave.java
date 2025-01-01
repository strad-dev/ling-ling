package eventListeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;
// BEETHOVEN-ONLY CLASS

public class Leave extends ListenerAdapter {
	public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
		TextChannel channel = e.getGuild().getTextChannelById("734697493828206662");
		assert channel != null;
		int members = e.getGuild().getMemberCount() - 6;
		channel.sendMessage("<@" + Objects.requireNonNull(e.getMember()).getId() + "> `" + e.getUser().getGlobalName() + "` just left the server.  I guess they can be considered a VIOLIST now." +
				"\n*" + members + " members remain good musicians*").queue();
	}
}