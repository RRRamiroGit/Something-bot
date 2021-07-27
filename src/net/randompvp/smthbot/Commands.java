package net.randompvp.smthbot;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	String[] game = { "CEA", "2", "3", "ODST", "Reach", "4" };
	boolean isEasyEnabled = true;
	boolean isLegEnabled = true;
	String[] reachMissions = { "WC", "ONI", "NF", "TotS", "LNoS", "Exo", "NA", "Package", "PoA" };
	String[] oneMissions = { "PoA", "Halo", "T&R", "SC", "AotCR", "343GS", "Library", "TB", "Keys", "Maw" };
	String[] twoMissions = { "Armory", "CS", "Outskirts", "Metro", "Arbiter", "Orical", "DH", "Regret", "SI", "QZ",
			"Gravemind", "Uprising", "HC", "tGJ" };
	String[] threeMissions = { "S117", "CN", "Tsavo", "Storm", "FG", "Ark", "Covenant", "Cortana", "Halo" };
	String[] odstMissions = { "P2D", "TP", "UR", "KB", "ONI", "NMPDGQ", "KS", "DH", "CH" };
	String[] fourMissions = { "Dawn", "Requiem", "Forerunner", "Infinity", "Reclaimer", "Shutdown", "Composer",
			"Midnight" };

	HashMap<String, String> customCommands = fileHashReader("ccommands");
	HashMap<String, String> customTriggers = fileHashReader("ctriggers");
	String urlName = fileStringReader("url");
	String websiteDir = fileStringReader("websitedir");

	public HashMap<String, String> fileHashReader(String s) {
		try {
			File myObj = new File("./" + s);
			Scanner myReader = new Scanner(myObj);
			String data = myReader.nextLine();
			while (myReader.hasNextLine()) {
				data = data + "\n" + myReader.nextLine();
			}
			myReader.close();
			String[] keyValuePairs = data.split("\\*");
			HashMap<String, String> map = new HashMap<String, String>();
			for (String pair : keyValuePairs) {
				String[] entry = pair.split("=");
				if (entry.length > 1)
					map.put(entry[0].trim(), (entry[1].trim()));
			}
			return map;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}

	public static String fileStringReader(String s) {
		try {
			File myObj = new File("./" + s);
			Scanner myReader = new Scanner(myObj);
			String data = myReader.nextLine();
			myReader.close();
			return data;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}

	HashMap<String, String> haloEmoji = haloEmoji();
	Random r = new Random();

	public HashMap<String, String> haloEmoji() {
		String data = "CEA=<:Halo_CEA:809900484851924992>, 2=<:Halo_2:809968528127688705>, 3=<:Halo_3:809968602185990195>, ODST=<:Halo_ODST:809900561277255720>, Reach=<:Halo_Reach:809968668380758027>, 4=<:Halo_4:809968729811058769>";
		String[] keyValuePairs = data.split(",");
		HashMap<String, String> map = new HashMap<String, String>();
		for (String pair : keyValuePairs) {
			String[] entry = pair.split("=");
			map.put(entry[0].trim(), (entry[1].trim()));
		}
		return map;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (e.getAuthor().isBot())
			return;
		StringBuilder sb = new StringBuilder();
		String[] args = e.getMessage().getContentRaw().split(" ");
		if (args[0].equalsIgnoreCase(Main.prefix + "addcommand")) {
			if (args.length == 1) {
				e.getMessage().reply("Usage: ```!addcommand <command> <response>```").queue();
			} else if (e.getMessage().getContentRaw().contains("*") || e.getMessage().getContentRaw().contains("=")) {
				e.getMessage().reply("Sorry, commands may not contain equal signs or asterisks.").queue();
			} else if (args.length == 2) {
				e.getMessage().reply("Usage: ```!addcommand " + args[1] + " <response>```").queue();
			} else if (args.length >= 3) {
				for (int i = 2; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String allArgs = sb.toString().trim();
				customCommands.put(args[1].toLowerCase(), allArgs);
				e.getMessage().reply("Made command `!" + args[1] + "` with response `" + allArgs + "`").queue();
				writeFile("ccommands", customCommands);
				System.out.println(e.getAuthor().getName() + " made a command in " + e.getChannel().getName() + " in "
						+ e.getGuild().getName() + " that is " + args[1] + " with the response " + allArgs);
			}
		} else if (args[0].equalsIgnoreCase(Main.prefix + "removecommand")) {
			if (args.length == 1) {
				e.getMessage().reply("Usage: `!removecommand <command>`").queue();
				return;
			}
			if (args.length == 2) {
				if (customCommands.containsKey(args[1].toLowerCase())) {
					customCommands.remove(args[1].toLowerCase());
					e.getMessage().reply("Removed command `!" + args[1] + "` successfully").queue();
					writeFile("ccommands", customCommands);
					System.out.println(e.getAuthor().getName() + " removed a command in " + e.getChannel().getName()
							+ " in " + e.getGuild().getName() + " that is " + args[1]);
				} else {
					e.getMessage().reply("Command `!" + args[1] + "` does not exist!").queue();
				}
			} else {
				e.getMessage().reply("Usage: `!removecommand <command>`").queue();
			}
		} else if (args[0].equalsIgnoreCase(Main.prefix + "addtrigger")) {
			if (args.length <= 2) {
				e.getMessage().reply("Usage: `!addtrigger <trigger>; <message>`").queue();
			} else {
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String allArgs = sb.toString().trim();
				String[] keyV = allArgs.split("; ");
				if (!allArgs.contains("; ")) {
					e.getMessage().reply("Usage: `!addtrigger <trigger>; <message>`").queue();
				} else if (customTriggers.containsKey(keyV[0])) {
					e.getMessage().reply("There is already a trigger for this!").queue();
				} else if (allArgs.contains("*") || allArgs.contains("=")) {
					e.getMessage().reply("Sorry, but triggers may not contain equal signs or asterisks.").queue();
				} else {
					customTriggers.put(keyV[0].toLowerCase(), keyV[1]);
					e.getChannel()
							.sendMessage("Made trigger `" + keyV[0].toLowerCase() + "` with response `" + keyV[1] + "`")
							.queue();
					writeFile("ctriggers", customTriggers);
					System.out.println("\"" + e.getAuthor().getName() + "\" made a trigger in \""
							+ e.getChannel().getName() + "\" in \"" + e.getGuild().getName() + "\" that is \""
							+ keyV[0].toLowerCase() + "\" with the response \"" + keyV[1] + "\"");
				}
			}
			return;
		} else if (args[0].equalsIgnoreCase(Main.prefix + "removetrigger")) {
			if (args.length == 1) {
				e.getMessage().reply("Usage: `!removetrigger <trigger>`").queue();
			} else {
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String allArgs = sb.toString().trim();
				if (!customTriggers.containsKey(allArgs.toLowerCase())) {
					e.getMessage().reply("This trigger does not exist!").queue();
				} else {
					customTriggers.remove(allArgs.toLowerCase());
					e.getMessage().reply("Removed trigger `" + allArgs + "` successfully").queue();
					writeFile("ctriggers", customTriggers);
					System.out.println(
							"\"" + e.getAuthor().getName() + "\" removed a trigger in \"" + e.getChannel().getName()
									+ "\" in \"" + e.getGuild().getName() + "\" that is \"" + allArgs + "\"");
				}
			}
		}
		for (Entry<String, String> ee : customTriggers.entrySet()) {
			String m = " " + e.getMessage().getContentRaw().toLowerCase();
			if (m.contains(" " + ee.getKey())) {
				e.getMessage().reply(ee.getValue()).queue();
				break;
			}
		}
		if (e.getMessage().getContentRaw().startsWith(Main.prefix)) {
			if (customCommands.containsKey(args[0].substring(1).toLowerCase())) {
				e.getMessage().reply(customCommands.get(args[0].substring(1).toLowerCase())).queue();
			}
		}
		if (args[0].equalsIgnoreCase(Main.prefix + "random")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Random Halo level");
			eb.setColor(Color.GREEN);
			if (args.length == 1) {
				eb.setDescription(randomMission());
			} else {
				try {
					String randomMissions = "";
					int ef = Integer.parseInt(args[1]);
					if (ef > 20)
						ef = 20;
					for (int i = 0; i < ef; i++)
						randomMissions = randomMissions + "\n" + randomMission();
					eb.setDescription(randomMissions);
				} catch (NumberFormatException ex) {
					eb.setDescription(randomMission());
				}
			}
			e.getMessage().reply(eb.build()).queue();
		}
		if (args[0].equalsIgnoreCase(Main.prefix + "yoink")) {
			if (args.length != 2) {
				e.getMessage().reply("Usage: !yoink @<user>").queue();
				return;
			}
			User u = null;
			try {
				u = e.getMessage().getMentionedUsers().get(0);
			} catch (Exception ex) {
				e.getMessage().reply("Error while getting mentioned user (Did you mention anybody??)").queue();
				return;
			}
			if (u.isBot()) {
				e.getMessage().reply("You may not yoink bots.").queue();
				return;
			}
			u.openPrivateChannel().queue(d -> {
				d.sendMessage("You have been yoinked by " + e.getAuthor().getName()).queue();
			});
			e.getMessage().reply("You have yoinked " + e.getMessage().getMentionedUsers().get(0).getName()).queue();
		}
		if (args[0].equalsIgnoreCase(Main.prefix + "ping")) {
			long t = System.currentTimeMillis();
			e.getMessage().reply("Pong!").queue(m -> {
				m.editMessage("Ping: " + String.valueOf(System.currentTimeMillis() - t) + "ms").queue();
			});
		}
		if (args[0].equalsIgnoreCase(Main.prefix + "toggle")) {
			if (args.length != 2 || (!args[1].equalsIgnoreCase("easyrun") && !args[1].equalsIgnoreCase("legrun"))) {
				e.getMessage().reply("Usage: !toggle [EasyRun/LegRun]").queue();
				return;
			}
			if (args[1].equalsIgnoreCase("easyrun")) {
				if (isEasyEnabled) {
					if (isLegEnabled) {
						isEasyEnabled = false;
						e.getMessage().reply("Toggled easy runs off").queue();
					} else {
						e.getMessage().reply("Cannot toggle easy runs off if legendary runs are off").queue();
					}
				} else {
					isEasyEnabled = true;
					e.getMessage().reply("Toggled easy runs on").queue();
				}
			} else {
				if (isLegEnabled) {
					if (isEasyEnabled) {
						isLegEnabled = false;
						e.getMessage().reply("Toggled legendary runs off").queue();
					} else {
						e.getMessage().reply("Cannot toggle legendary runs off if easy runs are off").queue();
					}
				} else {
					isLegEnabled = true;
					e.getMessage().reply("Toggled legendary runs on").queue();
				}
			}
		}
		if (args[0].equalsIgnoreCase(Main.prefix + "addpage")) {
			try {
				if (args.length == 1) {
					e.getMessage().reply("Usage: !addpage type:(text/redirect/file) pagelink:(what will be after "
							+ urlName
							+ "/) [title:(page title)] [css:(css;links;seperated;by;semicolons)] [text:\"text on page in quotation marks\"] [link:(redirect link)]\nIf you choose the type to be text, it'll be plain text on the website, you may include html such as <a> tags and you can also link external css pages. Keep in mind that whatever you type will be surrounded with <p> tags. With redirect, its just a page that'll redirect to your specified link. With file, you need to upload the file of your choice. This can be an html (if it is make sure to have it be index.html), or any file really. Think that should be all you need to know, don't abuse and yea have fun ig :)")
							.queue();
					return;
				}
				String pageLink = null;
				String title = null;
				HashSet<String> css = new HashSet<String>();
				String type = null;
				String text = null;
				String link = null;
				HashSet<Integer> ignore = new HashSet<Integer>();
				for (int i = 0; i < args.length; i++) {
					String s = args[i];
					if (ignore.contains(i))
						continue;
					if (s.equalsIgnoreCase(Main.prefix + "addpage"))
						continue;
					if (s.toLowerCase().startsWith("type:")) {
						if (s.length() < 6) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						String ss = s.substring(5);
						if (ss.equals("text") || ss.equals("redirect") || ss.equals("file"))
							type = ss;
						continue;
					}
					if (s.toLowerCase().startsWith("pagelink:")) {
						if (s.length() < 10) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						pageLink = s.substring(9);
						continue;
					}
					if (s.toLowerCase().startsWith("text:\"")) {
						if (s.length() < 7) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						String ss = s.substring(6);
						for (int ii = i + 1; ii < args.length; ii++) {
							ignore.add(ii);
							ss = ss + " " + args[ii];
							if (args[ii].contains("\""))
								break;
						}
						text = ss.substring(0, ss.length() - 1);
						continue;
					}
					if (s.toLowerCase().startsWith("link:")) {
						if (s.length() < 6) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						link = s.substring(5);
						continue;
					}
					if (s.toLowerCase().startsWith("title:\"")) {
						if (s.length() < 8) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						String ss = s.substring(7);
						for (int ii = i + 1; ii < args.length; ii++) {
							ignore.add(ii);
							ss = ss + " " + args[ii];
							if (args[ii].contains("\""))
								break;
						}
						title = ss.substring(0, ss.length() - 1);
						continue;
					}
					if (s.toLowerCase().startsWith("css:")) {
						if (s.length() < 5) {
							e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
							continue;
						}
						String ss = s.substring(4);
						String[] sss = ss.split(";");
						for (String ssss : sss) {
							css.add(ssss);
						}
						continue;
					}
					e.getMessage().reply("Error at " + s + " <----, Will still try and proceed.").queue();
				}
				if (type == null) {
					e.getMessage().reply("There needs to be a specified type!").queue();
					return;
				}
				if (pageLink == null) {
					e.getMessage().reply("There needs to be a specified page link!").queue();
					return;
				}
				if (type.equalsIgnoreCase("text")) {
					String page = "<html>\n	<head>";
					if (title != null)
						page = page + "\n		<title>" + title + "</title>";
					for (String csss : css) {
						page = page + "\n		<link rel=\"stylesheet\" href=\"" + csss + "\">";
					}
					page = page + "\n	</head>\n	<body>";
					if (text == null) {
						e.getMessage().reply("You must have text.").queue();
						return;
					}
					page = page + "\n		<p>" + text + "</p>";
					if (e.getMessage().getAttachments().size() != 0) {
						for (Message.Attachment at : e.getMessage().getAttachments()) {
							if (at.isImage())
								page = page + "\n		<img src=\"" + at.getUrl() + "\">";
							if (at.isVideo())
								page = page + "\n		<video src=\"" + at.getUrl() + "\">";
						}
					}
					page = page + "\n	</body>\n</html>";
					String[] folders = pageLink.split("/");
					File f = new File(websiteDir);
					for (int i = 0; i < folders.length; i++) {
						f = new File(f, folders[i]);
						if (f.isFile()) {
							e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
									.queue();
							return;
						}
						if (!f.exists())
							f.mkdir();
					}
					f = new File(f, "index.html");
					if (f.createNewFile()) {
						FileWriter writer = new FileWriter(f);
						writer.write(page);
						writer.close();
						e.getMessage()
								.reply("Made the page successfully! Check page at https://" + urlName + "/" + pageLink)
								.queue();
					} else {
						e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
								.queue();
						return;
					}
				} else if (type.equalsIgnoreCase("redirect")) {
					if (link == null) {
						e.getMessage().reply("You must have a link to redirect to.").queue();
						return;
					}
					String page = "<meta http-equiv=\"Refresh\" content=\"0; url='" + link + "'\" />";
					String[] folders = pageLink.split("/");
					File f = new File(websiteDir);
					for (int i = 0; i < folders.length; i++) {
						f = new File(f, folders[i]);
						if (f.isFile()) {
							e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
									.queue();
							return;
						}
						if (!f.exists())
							f.mkdir();
					}
					f = new File(f, "index.html");
					if (f.createNewFile()) {
						FileWriter writer = new FileWriter(f);
						writer.write(page);
						writer.close();
						e.getMessage().reply(
								"Made the page successfully! Check page out at https://" + urlName + "/" + pageLink)
								.queue();
					} else {
						e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
								.queue();
						return;
					}
				} else if (type.equalsIgnoreCase("file")) {
					if (e.getMessage().getAttachments().size() == 0) {
						e.getMessage().reply("You must attach a file if you're using the file type!").queue();
						return;
					}
					String[] folders = pageLink.split("/");
					File f = new File(websiteDir);
					for (int i = 0; i < folders.length; i++) {
						f = new File(f, folders[i]);
						if (f.isFile()) {
							e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
									.queue();
							return;
						}
						if (!f.exists())
							f.mkdir();
					}
					f = new File(f, e.getMessage().getAttachments().get(0).getFileName());
					if (!f.exists()) {
						e.getMessage().getAttachments().get(0).downloadToFile(f);
						e.getMessage().reply("File made successfully! Check out at https://" + urlName + "/" + pageLink
								+ "/" + e.getMessage().getAttachments().get(0).getFileName()).queue();
					} else {
						e.getMessage().reply("There has been conflict with the file path! Can't make new file!")
								.queue();
						return;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				e.getMessage().reply("Error occured, bug ramiro to fix this and uh yea we gud").queue();
			}
		}
	}

	public String randomMission() {
		String randomHaloName = game[r.nextInt(game.length)];
		String randomHaloEmoji = haloEmoji.get(randomHaloName);
		String randomDifficulty = "";
		if (isEasyEnabled && isLegEnabled) {
			if (r.nextBoolean()) {
				randomDifficulty = "<:EasyRun:809968390852182068>";
			} else {
				randomDifficulty = "<:LegRun:809968455742783508>";
			}
		} else if (isEasyEnabled) {
			randomDifficulty = "<:EasyRun:809968390852182068>";
		} else {
			randomDifficulty = "<:LegRun:809968455742783508>";
		}
		String randomMission = null;
		if (randomHaloName == "Reach") {
			randomMission = reachMissions[r.nextInt(reachMissions.length)];
		} else if (randomHaloName == "CEA") {
			randomMission = oneMissions[r.nextInt(oneMissions.length)];
		} else if (randomHaloName == "2") {
			randomMission = twoMissions[r.nextInt(twoMissions.length)];
		} else if (randomHaloName == "3") {
			randomMission = threeMissions[r.nextInt(threeMissions.length)];
		} else if (randomHaloName == "ODST") {
			randomMission = odstMissions[r.nextInt(odstMissions.length)];
		} else if (randomHaloName == "4") {
			randomMission = fourMissions[r.nextInt(fourMissions.length)];
		}
		return randomHaloEmoji + " " + randomMission + " " + randomDifficulty;
	}

	void writeFile(String name, HashMap<String, String> h) {
		String s = "";
		for (Entry<String, String> ss : h.entrySet()) {
			s = s + ss.getKey() + "=" + ss.getValue() + "*";
		}
		s = s.substring(0, s.length() - 1);
		try (FileWriter filewriter = new FileWriter("./" + name)) {
			filewriter.write(s);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
