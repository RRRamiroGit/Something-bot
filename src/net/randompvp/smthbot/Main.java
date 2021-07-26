package net.randompvp.smthbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
	
	public static String prefix = "!";

	public static void main(String[] arguments) throws LoginException {
		try {
			File myObj = new File("./" + "token");
			Scanner myReader = new Scanner(myObj);
			@SuppressWarnings("deprecation")
			JDABuilder jda = new JDABuilder(AccountType.BOT).setToken(myReader.nextLine());
			myReader.close();
			jda.addEventListeners(new Commands());
			jda.addEventListeners(new GuildMemberUpdate());
			jda.addEventListeners(new UserUpdateActivity());
			jda.build().getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("ur mum"));
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
