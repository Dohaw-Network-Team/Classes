package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class IWKnockbackCooldown extends Cooldown{

	public IWKnockbackCooldown(Main plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("KnockbackCooldown") && !p.getMetadata("KnockbackCooldown").isEmpty()) {
				int cooldownSeconds = p.getMetadata("KnockbackCooldown").get(0).asInt();
				if(cooldownSeconds != 0 && cooldownSeconds != 1) {
					p.setMetadata("KnockbackCooldown", new FixedMetadataValue(plugin, cooldownSeconds-1));
				}else if(cooldownSeconds == 1) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&aThe &bKnockback &acooldown is up!")));
					p.setMetadata("KnockbackCooldown", new FixedMetadataValue(plugin, cooldownSeconds-1));
				}
			}
		}
	}

}
