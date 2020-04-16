package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.scheduler.BukkitRunnable;

import me.caleb.Classes.Main;

public abstract class Cooldown extends BukkitRunnable{

	protected Main plugin;
	
	public Cooldown(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public abstract void run();
	
}
