package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.Classes.Main;
import me.caleb.Classes.listeners.classes.PeacemakerListener;
import me.caleb.Classes.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PMSFCooldown extends Cooldown{
	
	final int secondsWallUp = 4;
	
	public PMSFCooldown(Main plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("PMSteadyFront") && !p.getMetadata("PMSteadyFront").isEmpty()) {
				int secondsLeft = p.getMetadata("PMSteadyFront").get(0).asInt();
				/*
				 * 6 = Cooldown time (10 seconds) - Time the wall will be up (4 seconds)
				 */
				if(secondsLeft != 0 && secondsLeft != 1 && secondsLeft != (PeacemakerListener.SF_COOLDOWN - secondsWallUp)) {
					p.setMetadata("PMSteadyFront", new FixedMetadataValue(plugin, secondsLeft - 1));
				}else if(secondsLeft == 1) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&aThe &bSteady Front &acooldown is up!")));
					p.setMetadata("PMSteadyFront", new FixedMetadataValue(plugin, secondsLeft - 1));
				}else if(secondsLeft == (PeacemakerListener.SF_COOLDOWN - secondsWallUp)) {
					PeacemakerListener.removeSF(p);
					p.setMetadata("PMSteadyFront", new FixedMetadataValue(plugin, secondsLeft - 1));
				}
			}
		}
	}

}
