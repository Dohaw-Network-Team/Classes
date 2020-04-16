package me.caleb.Classes.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import me.caleb.Classes.Main;
import me.caleb.Classes.prompts.ResetClassPrompt;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import me.caleb.Classes.utils.managers.ConfigManager;

public class Commands extends Utils implements CommandExecutor{

	private Main plugin;
	FileConfiguration config;
	
	public Commands(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("class").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		//cl give (Class) (Player)
		if(args[0].equalsIgnoreCase("give") && args.length == 3) {
			if(Bukkit.getServer().getPlayerExact(args[2]) != null) {
				Player pAffected = Bukkit.getPlayer(args[2]);
				ClassManager cm = new ClassManager(plugin, pAffected);
				cm.giveClass(args[1]);
			}else {
				sender.sendMessage("This player is not online!");
			}
		}else if(args[0].equalsIgnoreCase("reset") && args.length == 1) {
			ConfigManager cm = new ConfigManager(plugin, "players.yml");
			if(cm.isPlayer(p.getName())) {
				ConversationFactory factory = new ConversationFactory(plugin);
				Conversation conv = factory.withFirstPrompt(new ResetClassPrompt(plugin,p.getName())).withLocalEcho(false).withEscapeSequence("No").buildConversation(p);
				conv.begin();
			}else {
				me.caleb.Clan.utils.Utils.sendPlayerMessage("You do not have a class right now!", true, p);
			}	
		}else {
			
			
		}
		return false;
	}

}
