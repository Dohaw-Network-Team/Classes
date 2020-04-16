package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PrayerToTheGodsCooldown extends Cooldown{

	private static int seconds = 0;
	
	public PrayerToTheGodsCooldown(Main plugin) {
		super(plugin);
	}
	
	@Override
	public void run() {
		
		if(seconds == 60) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				ClassManager cm = new ClassManager(plugin, p);
				if(cm.isClass("Archer")) {
					if(p.hasMetadata("Prayer_To_The_Gods_Cooldown") && !p.getMetadata("Prayer_To_The_Gods_Cooldown").isEmpty()) {
						int minutesLeft = p.getMetadata("Prayer_To_The_Gods_Cooldown").get(0).asInt();
						if(minutesLeft != 0) {
							p.setMetadata("Prayer_To_The_Gods_Cooldown", new FixedMetadataValue(plugin,(minutesLeft-1)));
						}	
					}
				}
			}
			seconds = 0;
		}
		seconds++;
		
	} 
	
	
}
