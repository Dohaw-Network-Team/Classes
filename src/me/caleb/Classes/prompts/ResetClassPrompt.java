package me.caleb.Classes.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import me.caleb.Clan.utils.Utils;
import me.caleb.Classes.Main;

public class ResetClassPrompt extends ValidatingPrompt{

	private String playerName;
	private Main plugin;
	
	public ResetClassPrompt(Main plugin, String playerName) {
		this.plugin = plugin;
		this.playerName = playerName;
	}
	
	@Override
	protected Prompt acceptValidatedInput(ConversationContext con, String input) {
		con.setSessionData("answer", input);
		con.setSessionData("playerName", playerName);
		return new ResetClassDone(plugin);
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		return Utils.sendMessageWithPrefix("Are you sure you with to reset your class? This resets all your progress. This means that you will lose your gold, items, and your clan (if you are owner). Keep in mind that you will NOT recieve any of your items or gold back if you choose to reset. If you wish to continue, type &a&lYes. &rIf you do not want to do this, type &4&lNo");
	}

	@Override
	protected boolean isInputValid(ConversationContext con, String input) {
		if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")) {
			return true;
		}else {
			return false;
		}
	}

}
