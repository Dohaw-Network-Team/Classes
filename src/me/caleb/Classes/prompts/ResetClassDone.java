package me.caleb.Classes.prompts;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import me.caleb.Clan.utils.Utils;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.ClassManager;

public class ResetClassDone implements Prompt{

	private Main plugin;
	
	public ResetClassDone(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		return END_OF_CONVERSATION;
	}

	@Override
	public boolean blocksForInput(ConversationContext arg0) {
		return false;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		
		Conversable c = context.getForWhom();
		String playerName = (String) context.getSessionData("playerName");
		String answer = (String) context.getSessionData("answer");
		
		if(answer.equalsIgnoreCase("yes")) {
			ClassManager cm = new ClassManager(plugin, Bukkit.getPlayer(playerName));
			cm.reset();
			return Utils.sendMessageWithPrefix("Your progress and class has been reset!");
		}	
		
		return Utils.sendMessageWithPrefix("Phew! I thought you were going to reset for a second...");
	}

}
