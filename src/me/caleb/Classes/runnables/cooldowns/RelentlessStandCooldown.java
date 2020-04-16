package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class RelentlessStandCooldown extends Cooldown{

	public RelentlessStandCooldown(Main plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("RelentlessStandCooldown") && !p.getMetadata("RelentlessStandCooldown").isEmpty()) {
				int secondsLeft = p.getMetadata("RelentlessStandCooldown").get(0).asInt();
				if(secondsLeft != 0 && secondsLeft != 1) {
					p.setMetadata("RelentlessStandCooldown", new FixedMetadataValue(plugin, (secondsLeft-1)));
				}else if(secondsLeft == 1) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&aThe &bRelentless Stand &acooldown is up!")));
					p.setMetadata("RelentlessStandCooldown", new FixedMetadataValue(plugin, (secondsLeft-1)));
				}
			}
		}
	}

}
