package net.randompvp.smthbot;

import java.util.HashMap;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberUpdate extends ListenerAdapter {

	HashMap<String, ClientType> lastClient = new HashMap<String, ClientType>();

	@Override
	public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent e) {
		if (!e.getGuild().getId().equals(UserUpdateActivity.serverId))
			return;
		String s = e.getMember().getOnlineStatus().toString();
		String emoji = "";
		if (!lastClient.containsKey(e.getUser().getId()))
			lastClient.put(e.getUser().getId(), ClientType.UNKNOWN);
		if (e.getMember().getOnlineStatus().equals(OnlineStatus.ONLINE)) {
			s = "online";
			if (e.getMember().getActiveClients().contains(ClientType.DESKTOP)
					&& e.getMember().getOnlineStatus(ClientType.DESKTOP).equals(OnlineStatus.ONLINE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.DESKTOP)) {
				s = s + " (Desktop)";
				lastClient.put(e.getUser().getId(), ClientType.DESKTOP);
			} else if (e.getMember().getActiveClients().contains(ClientType.MOBILE)
					&& e.getMember().getOnlineStatus(ClientType.MOBILE).equals(OnlineStatus.ONLINE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.MOBILE)) {
				s = s + " (Mobile)";
				lastClient.put(e.getUser().getId(), ClientType.MOBILE);
			} else if (e.getMember().getActiveClients().contains(ClientType.WEB)
					&& e.getMember().getOnlineStatus(ClientType.WEB).equals(OnlineStatus.ONLINE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.WEB)) {
				s = s + " (Web)";
				lastClient.put(e.getUser().getId(), ClientType.WEB);
			}
			emoji = ":green_circle:";
		} else if (e.getMember().getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)) {
			s = "on do not disturb";
			if (e.getMember().getActiveClients().contains(ClientType.DESKTOP)
					&& e.getMember().getOnlineStatus(ClientType.DESKTOP).equals(OnlineStatus.DO_NOT_DISTURB)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.DESKTOP)) {
				s = s + " (Desktop)";
				lastClient.put(e.getUser().getId(), ClientType.DESKTOP);
			} else if (e.getMember().getActiveClients().contains(ClientType.MOBILE)
					&& e.getMember().getOnlineStatus(ClientType.MOBILE).equals(OnlineStatus.DO_NOT_DISTURB)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.MOBILE)) {
				s = s + " (Mobile)";
				lastClient.put(e.getUser().getId(), ClientType.MOBILE);
			} else if (e.getMember().getActiveClients().contains(ClientType.WEB)
					&& e.getMember().getOnlineStatus(ClientType.WEB).equals(OnlineStatus.DO_NOT_DISTURB)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.WEB)) {
				s = s + " (Web)";
				lastClient.put(e.getUser().getId(), ClientType.WEB);
			}
			emoji = ":red_circle:";
		} else if (e.getMember().getOnlineStatus().equals(OnlineStatus.IDLE)) {
			s = "idle";
			if (e.getMember().getActiveClients().contains(ClientType.DESKTOP)
					&& e.getMember().getOnlineStatus(ClientType.DESKTOP).equals(OnlineStatus.IDLE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.DESKTOP)) {
				s = s + " (Desktop)";
				lastClient.put(e.getUser().getId(), ClientType.DESKTOP);
			} else if (e.getMember().getActiveClients().contains(ClientType.MOBILE)
					&& e.getMember().getOnlineStatus(ClientType.MOBILE).equals(OnlineStatus.IDLE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.MOBILE)) {
				s = s + " (Mobile)";
				lastClient.put(e.getUser().getId(), ClientType.MOBILE);
			} else if (e.getMember().getActiveClients().contains(ClientType.WEB)
					&& e.getMember().getOnlineStatus(ClientType.WEB).equals(OnlineStatus.IDLE)
					&& !lastClient.get(e.getUser().getId()).equals(ClientType.WEB)) {
				s = s + " (Web)";
				lastClient.put(e.getUser().getId(), ClientType.WEB);
			}
			emoji = ":yellow_circle:";
		} else if (e.getMember().getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
			s = "offline";
			emoji = ":black_circle:";
		}
		e.getGuild().getTextChannelById(UserUpdateActivity.channelId)
				.sendMessage(emoji + " " + e.getMember().getUser().getName() + " is now " + s).queue();
	}

}
