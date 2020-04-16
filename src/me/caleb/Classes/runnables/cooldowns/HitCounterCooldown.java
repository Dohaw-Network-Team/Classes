package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.caleb.Classes.Main;

public class HitCounterCooldown extends Cooldown{
	
	public HitCounterCooldown(Main plugin) {
		super(plugin);
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("Hit_CounterCooldown") && !p.getMetadata("Hit_CounterCooldown").isEmpty()) {
				int secondsLeft = p.getMetadata("Hit_CounterCooldown").get(0).asInt();
				//If the cooldown isn't done
				if(secondsLeft != 0) {
					p.setMetadata("Hit_CounterCooldown", new FixedMetadataValue(plugin, (secondsLeft-1)));
				}
			}
		}
	}

}
