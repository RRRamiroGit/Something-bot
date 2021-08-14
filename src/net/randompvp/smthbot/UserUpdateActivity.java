package net.randompvp.smthbot;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserUpdateActivity extends ListenerAdapter {

	HashMap<String, String> lastActivity = new HashMap<String, String>();
	public static String serverId = Commands.fileStringReader("serverid");
	public static String channelId = Commands.fileStringReader("channelid");

	@Override
	public void onUserUpdateActivities(UserUpdateActivitiesEvent e) {
		if (!e.getGuild().getId().equals(serverId) || e.getMember().getUser().isBot())
			return;
		if (!lastActivity.containsKey(e.getMember().getId()))
			lastActivity.put(e.getMember().getId(), "");
		if (e.getMember().getActivities().size() == 0) {
			if (!lastActivity.get(e.getMember().getId()).equals("")) {
				e.getGuild().getTextChannelById(channelId)
						.sendMessage(e.getMember().getUser().getName() + " is now doing nothing").queue();
				lastActivity.put(e.getMember().getId(), "");
			}
		} else {
			if (lastActivity.get(e.getMember().getId()).equals(e.getMember().getActivities().get(0).getName()))
				return;
			String s = e.getMember().getActivities().get(0).getType().toString().toLowerCase();
			String emoji = "";
			int num = 0;
			if (e.getMember().getActivities().get(0).getType().equals(ActivityType.DEFAULT)) {
				s = " is now playing";
				emoji = ":video_game:";
			} else if (e.getMember().getActivities().get(0).getType().equals(ActivityType.CUSTOM_STATUS)) {
				if (e.getMember().getActivities().size() < 2) {
					if (!lastActivity.get(e.getMember().getId()).equals("")) {
						e.getGuild().getTextChannelById(channelId)
								.sendMessage(e.getMember().getUser().getName() + " is now doing nothing").queue();
						lastActivity.put(e.getMember().getId(), "");
					}
					return;
				}
				num = 1;
				if (lastActivity.get(e.getMember().getId()).equals(e.getMember().getActivities().get(1).getName()))
					return;
				if (e.getMember().getActivities().get(1).getType().equals(ActivityType.DEFAULT)) {
					s = " is now playing";
					emoji = ":video_game:";
				} else if (e.getMember().getActivities().get(1).getType().equals(ActivityType.LISTENING)) {
					s = " is now listening to";
					emoji = ":musical_note:";
				} else if (e.getMember().getActivities().get(1).getType().equals(ActivityType.STREAMING)) {
					s = " is now streaming to";
					emoji = ":purple_circle:";
				}
			} else if (e.getMember().getActivities().get(0).getType().equals(ActivityType.LISTENING)) {
				s = " is now listening to";
				emoji = ":musical_note:";
			} else if (e.getMember().getActivities().get(0).getType().equals(ActivityType.STREAMING)) {
				s = " is now streaming to";
				emoji = ":purple_circle:";
			}
			e.getGuild().getTextChannelById(channelId).sendMessage(emoji + " " + e.getMember().getUser().getName() + s
					+ " " + e.getMember().getActivities().get(num).getName()).queue();
			lastActivity.put(e.getMember().getId(), e.getMember().getActivities().get(num).getName());
		}
	}

}
