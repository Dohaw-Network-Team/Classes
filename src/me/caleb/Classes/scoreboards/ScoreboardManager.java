package me.caleb.Classes.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.caleb.Classes.Main;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardManager implements Listener{

	private Main plugin;
	
	public ScoreboardManager(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
/*
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		BukkitRunnable schedular;
		
		schedular.runTaskTimer(plugin, delay, period)
		
		
	}*/
	
	public static ChatColor getClassColor(String cl) {
		
		switch(cl) {
		
			case "Archer":
				return ChatColor.GREEN;
			case "Ice_wizard":
				return ChatColor.AQUA;
			case "Brute":
				return ChatColor.RED;
			case "Peacemaker":
				return ChatColor.YELLOW;
			case "Thief":
				return ChatColor.GOLD;
		
		}
		
		return null;
	}
	
}
