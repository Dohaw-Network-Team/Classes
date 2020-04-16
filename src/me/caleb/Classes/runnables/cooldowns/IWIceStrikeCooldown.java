package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.Classes.Main;

public class IWIceStrikeCooldown extends Cooldown{

	public IWIceStrikeCooldown(Main plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("betweenFireballs") && !p.getMetadata("betweenFireballs").isEmpty()) {
				p.setMetadata("betweenFireballs", new FixedMetadataValue(plugin, false));
			}
		}
	}

}
