package me.caleb.Classes.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.caleb.Classes.Main;

public class HitCounter extends BukkitRunnable{

	private static Main plugin;
	private static int seconds = 0;
	
	public HitCounter(Main plugin) {
		HitCounter.plugin = plugin;
	}
	
	@Override
	public void run() {
		seconds++;
		resetHitCounter();
	}
	
	public static void resetHitCounter() {
		if(seconds == 2) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				
				if(p.hasMetadata("Hit_Counter") && !p.getMetadata("Hit_Counter").isEmpty()) {
					p.setMetadata("Hit_Counter", new FixedMetadataValue(plugin, 0));
				}
				
				if(p.hasMetadata("BHit_Counter") && !p.getMetadata("BHit_Counter").isEmpty()) {
					p.setMetadata("BHit_Counter", new FixedMetadataValue(plugin, 0));
				}
				
			}	
			seconds = 0;
		}
	}

}
