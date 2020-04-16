package me.caleb.Classes.utils.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.ItemParser;
import me.caleb.Classes.utils.Utils;

public class ClassManager extends Utils{
	
	private Player p;
	private Entity e;
	private Main plugin;
	private static FileConfiguration config;	
	
	public ClassManager(Main plugin, Player p) {
		this.plugin = plugin;
		this.p = p;
		config = plugin.getConfig();
	}
	
	public ClassManager(Main plugin, Entity e) {
		this.plugin = plugin;
		this.e = e;
		config = plugin.getConfig();
	}
	
	public void giveClass(String cl) {

		if(isClass(cl)) {
			ConfigManager cm = new ConfigManager(plugin, "players.yml");
			AttributeManager am = new AttributeManager(plugin, p, cl);
			if(cm.isPlayer(p.getName())) {
				p.sendMessage(Utils.chat("You already have a class! To change classes, do &6/cl reset"));
				return;
			}else {
				cm.addPlayer(p.getName(), cl);
				giveItems(cl);
				am.applyAttributes();
				me.caleb.Clan.utils.Utils.sendPlayerMessage("You have been given the &b&l" + cl + "&r class!", true, p);
				cm.reloadConfig();
			}
		}else {
			me.caleb.Clan.utils.Utils.sendPlayerMessage("This is not a class!", true, p);
			return;
		}
	}
	
	public boolean isClass(String cl) {
		String className = ""; 
		//Makes the first letter uppercase and the rest lowercase
		className = cl.substring(0,1).toUpperCase() + cl.substring(1).toLowerCase();
		
		List<String> classList = config.getStringList("ClassList");
		if(classList.contains(className)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void giveItems(String cl) {
		
		List<String> itemList = config.getStringList("Classes." + Utils.firstUppercaseRestLowercase(cl) + ".Items");
		
		for(String line : itemList) {
			ItemParser ip = new ItemParser(line, cl);
			p.getInventory().addItem(ip.getItem());
		}
		
	}

	public boolean hasCl(Player p){
		ConfigManager cm = new ConfigManager(plugin, "players.yml");
		if(cm.getValue("Players." + p.getName() + ".Class") != null){
			return true;
		}else{
			return false;
		}
	}

	//Get class
	public String getCl(String playerName) {
		ConfigManager cm = new ConfigManager(plugin, "players.yml");
		return cm.getValue("Players." + playerName + ".Class");
	}

	public void reset() {
		
		ConfigManager cm = new ConfigManager(plugin, "players.yml");
		AttributeManager am = new AttributeManager(plugin, p, getCl(p.getName()));
		
		am.removeAttributes();
		cm.removePlayer(p.getName());
		p.getInventory().clear();
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		//Send the player to the class choosing room
		//Take the players money
		//Send player back to level 1
		//Reset the players' attributes
		
	}
	
}
