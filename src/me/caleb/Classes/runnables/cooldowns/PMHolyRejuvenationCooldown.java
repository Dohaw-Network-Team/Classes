package me.caleb.Classes.runnables.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.Classes.Main;
import me.caleb.Classes.listeners.classes.PeacemakerListener;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import me.caleb.Classes.utils.managers.PMAttributeManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PMHolyRejuvenationCooldown extends Cooldown{

	final int SECONDS_SHIELD_IS_UP = 7;
	
	public PMHolyRejuvenationCooldown(Main plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.hasMetadata("PMHolyR") && !p.getMetadata("PMHolyR").isEmpty()) {
				int secondsLeft = p.getMetadata("PMHolyR").get(0).asInt();
				if(secondsLeft != 0 && secondsLeft != 1 && secondsLeft != (PeacemakerListener.HR_COOLDOWN - SECONDS_SHIELD_IS_UP)) {
					p.setMetadata("PMHolyR", new FixedMetadataValue(plugin, secondsLeft - 1));
				}else if(secondsLeft == 1) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&aThe &bHoly Rejuvenation &acooldown is up!")));
					p.setMetadata("PMHolyR", new FixedMetadataValue(plugin, secondsLeft - 1));
				}else if(secondsLeft == (PeacemakerListener.HR_COOLDOWN - SECONDS_SHIELD_IS_UP)) {
					ClassManager cm = new ClassManager(plugin, p);
					PMAttributeManager am = new PMAttributeManager(plugin, p, cm.getCl(p.getName()));
					am.removeMaxHealthAttribute();
					am.applyRegularMaxHealth();
					p.setMetadata("PMHolyR", new FixedMetadataValue(plugin, secondsLeft - 1));
				}
			}
		}
	}

}
