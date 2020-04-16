package me.caleb.Classes.listeners.classes;

import java.util.Collection;

import me.caleb.Classes.utils.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BruteListener implements Listener{

	private Main plugin;
	
	public BruteListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDyeRightClick(PlayerInteractEvent e) {
		
		final PotionEffect pe = new PotionEffect(PotionEffectType.SPEED, 70, 2);
		final int cdtime = 15;
		final String metaValue = "BQuickneningCooldown";
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.WHITE_WOOL)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Brute")) return;
		
		if(p.hasMetadata(metaValue) && !p.getMetadata(metaValue).isEmpty()) {
			int cooldownTime = p.getMetadata(metaValue).get(0).asInt();
			if(cooldownTime == 0) {
				
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(Particle.FLASH, p.getLocation(), 100);
					pl.playSound(p.getLocation(), Sound.BLOCK_WOOL_STEP, 100, 100);
				}
				
				if(p.hasPotionEffect(PotionEffectType.SPEED)) {
					p.removePotionEffect(PotionEffectType.SPEED);
				}
				
				p.addPotionEffect(pe);
				p.setMetadata(metaValue, new FixedMetadataValue(plugin, cdtime));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bQuickening activated!")));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + cooldownTime + " seconds", true, p, "&8&l[&aClasses&8&l]");
				return;
			}
		}else {
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(Particle.FLASH, p.getLocation(), 1);
				pl.playSound(p.getLocation(), Sound.BLOCK_WOOL_PLACE, 100, 100);
			}
			p.addPotionEffect(pe);
			p.setMetadata(metaValue, new FixedMetadataValue(plugin, cdtime));
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bQuickening activated!")));
		}
		
	}
	
	@EventHandler
	public void onBlazePowderRightClick(PlayerInteractEvent e) {
		
		final PotionEffect pe = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 2);
		final int cdtime = 60;
		final String metaValue = "RelentlessStandCooldown";
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.BLAZE_POWDER)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Brute")) return;
		
		if(p.hasMetadata(metaValue) && !p.getMetadata(metaValue).isEmpty()) {
			int cooldownTime = p.getMetadata(metaValue).get(0).asInt();
			
			if(cooldownTime == 0) {
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(Particle.VILLAGER_ANGRY, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 100);
					pl.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 100, 100);
				}
				p.addPotionEffect(pe);
				p.setMetadata(metaValue, new FixedMetadataValue(plugin, cdtime));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bRelentless Stand activated!")));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + cooldownTime + " seconds", true, p, "&8&l[&aClasses&8&l]");
				return;
			}
		}else {
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(Particle.VILLAGER_ANGRY, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 100);
				pl.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 100, 100);
			}
			p.addPotionEffect(pe);
			p.setMetadata(metaValue, new FixedMetadataValue(plugin, cdtime));
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bRelentless Stand activated!")));
		}
		
	}
	
	/*
	 * If a Brute hits a player 5 times within a second, that person is slowed down
	 */
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {

		if(!(e.getEntity() instanceof Player)) return;
		
		if(!(e.getDamager() instanceof Player)) return;
		
		Player damaged = (Player) e.getEntity();
		Player damager = (Player) e.getDamager();

		ClassManager cm = new ClassManager(plugin, damager);

		if(!cm.hasCl(damager)) return;

		if(!cm.getCl(damager.getName()).equalsIgnoreCase("Brute")) return;

		if(ClanConfigManager.inSameClan(damaged.getName(), damager.getName())) return;
		
		int hitCounter;
		final PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 60, 1);
		
		if(!damaged.hasMetadata("BHit_Counter") || damaged.getMetadata("BHit_Counter").isEmpty()) {
			damaged.setMetadata("BHit_Counter", new FixedMetadataValue(plugin, 0));
			return;
		}else {
			hitCounter = (damaged.getMetadata("BHit_Counter").get(0).asInt() + 1);
			if(hitCounter == 5) {
				Collection<PotionEffect> effects = damaged.getActivePotionEffects();
				for(PotionEffect p : effects) {
					PotionEffectType pet = p.getType();
					if(pet.equals(PotionEffectType.SLOW)) {
						effects.remove(p);
					}
				}
				damaged.addPotionEffect(pe);
				damaged.setMetadata("BHit_Counter", new FixedMetadataValue(plugin, 0));
				damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bThe player is slowed down!")));
			}else {
				damaged.setMetadata("BHit_Counter", new FixedMetadataValue(plugin, hitCounter));
			}	
		}	
	}
}
