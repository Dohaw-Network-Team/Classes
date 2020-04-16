package me.caleb.Classes.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {

	private File file;
	private FileConfiguration customConfig;
	private String fileName;
	
	
	public CustomConfig(String fileName) {
		this.fileName = fileName;
	}
	
	public  void setup() {
		
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("Classes").getDataFolder(), fileName);
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		customConfig = YamlConfiguration.loadConfiguration(file);
		
	}
	
	public FileConfiguration get() {
		return customConfig;
	}
	
	public void save() {
		try {
			customConfig.save(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload() {
		customConfig = YamlConfiguration.loadConfiguration(file);
	}
	
}
