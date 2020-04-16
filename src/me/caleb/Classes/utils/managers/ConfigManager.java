package me.caleb.Classes.utils.managers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.caleb.Clan.utils.Utils;
import me.caleb.Classes.Main;

public class ConfigManager {

	public File file;
	public FileConfiguration config;
	public Main plugin;
	public String fileName;
	
	public ConfigManager(Main plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		file = new File(plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getCustomConfig() {
		return config;
	}
	
	public void saveCustomConfig() {
		try {
			config.save(file);
		}catch(IOException e) {
			plugin.getLogger().warning("Unable to save " + fileName);
		}
	}
	
	public void reloadConfig() {
		file = new File(plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(file);
		Utils.sendConsoleMessage(fileName + " has been reloaded!");
	}
	
	public void addPlayer(String playerName, String cl) {
		
		FileConfiguration defaultConfig = plugin.getConfig();
		
		List<String> attributes = defaultConfig.getStringList("Classes." + cl + ".Attributes");
		List<String> itemList = defaultConfig.getStringList("Classes." + cl + ".Items");
		
		final String PATH = "Players." + playerName;
		config.set(PATH + ".Class", cl);
		config.set(PATH + ".Attributes", attributes);
		config.set(PATH + ".Items", itemList);	
		
		try {
			getCustomConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void removePlayer(String playerName) {
		
		final String PATH = "Players." + playerName;
		config.set(PATH + ".Class", null);
		config.set(PATH + ".Attributes", null);
		config.set(PATH + ".Items", null);	
		
		config.set(PATH, null);
		
		saveCustomConfig();
	}
	
	public boolean isPlayer(String playerName) {
		String cl = config.getString("Players." + playerName + ".Class");
		if(cl != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<String> getList(String path){
		return config.getStringList(path);
	}
	
	public String getValue(String path) {
		return config.getString(path);
	}

	public void setValue(String path, Object val){
		config.set(path, val);
	}
	
}
