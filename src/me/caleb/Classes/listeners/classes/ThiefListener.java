package me.caleb.Classes.listeners.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.AttributeManager;
import me.caleb.Classes.utils.managers.ClassManager;
import me.caleb.Classes.utils.managers.InvConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ThiefListener extends Utils implements Listener{

	private Main plugin;
	
	final Sound STEALTH_SOUND = Sound.AMBIENT_CAVE;
	final Sound STEALTH_SOUND2 = Sound.AMBIENT_UNDERWATER_EXIT;
	final Particle STEALTH_PARTICLE = Particle.SQUID_INK;
	final int STEALTH_COOLDOWN_TIME = 90;
	final String STEALTH_METAVALUE = "Stealth";
	
	final Sound QUICKNESS_SOUND = Sound.ENTITY_ILLUSIONER_CAST_SPELL;
	final Particle QUICKNESS_PARTICLE = Particle.FLASH;
	final int QUICKNESS_COOLDOWN_TIME = 13;
	final int QUICKNESS_DURATION = 60;
	final String QUICKNESS_METAVALUE = "TQuickness";
	
	final Sound BLINDNESS_SOUND = Sound.ENTITY_BAT_HURT;
	final Particle BLINDNESS_PARTICLE = Particle.SPELL_INSTANT;
	final int BLINDNESS_COOLDOWN_TIME = 60;
	final int BLINDNESS_COOLDOWN_TIME2 = 90;
	final int BLINDNESS_DURATION = 60;
	final int BLINDNESS_DURATION2 = 120;
	final String BLINDNESS_METAVALUE = "TEyeGouge";
	
	InvConfigManager invManager;
	
	public ThiefListener(Main plugin) {
		this.plugin = plugin;
		invManager = new InvConfigManager(plugin);
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onWitherRoseRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.WITHER_ROSE)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Thief")) return;
		
		if(p.hasMetadata(STEALTH_METAVALUE) && !p.getMetadata(STEALTH_METAVALUE).isEmpty()) {
			
			if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				int secondsLeft = p.getMetadata(STEALTH_METAVALUE).get(0).asInt();
				if(secondsLeft == 0) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bStealth activated!")));
					takeOffArmor(p);
					for(Player pl : Bukkit.getOnlinePlayers()) {
						pl.spawnParticle(STEALTH_PARTICLE, p.getLocation(), 20);
					}
					p.playSound(p.getLocation(), STEALTH_SOUND, 5, 1);
				}else {
					Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds!", true, p, "&8&l[&aClasses&8&l]");
				}
			}else {
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
				p.setMetadata(STEALTH_METAVALUE, new FixedMetadataValue(plugin, STEALTH_COOLDOWN_TIME));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bYou are not stealthed anymore!")));
				putOnArmor(p);
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(STEALTH_PARTICLE, p.getLocation(), 20);
				}
				p.playSound(p.getLocation(), STEALTH_SOUND2, 5, 1);
			}
			
		}else {
			
			if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
				p.setMetadata("ArmorContents", new FixedMetadataValue(plugin, p.getInventory().getArmorContents()));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bStealth activated!")));
				takeOffArmor(p);
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(STEALTH_PARTICLE, p.getLocation(), 20);
				}
				p.playSound(p.getLocation(), STEALTH_SOUND2, 5, 1);
			}else {
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
				p.setMetadata(STEALTH_METAVALUE, new FixedMetadataValue(plugin, STEALTH_COOLDOWN_TIME));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bYou are not stealthed anymore!")));
				putOnArmor(p);
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(STEALTH_PARTICLE, p.getLocation(), 20);
				}
				p.playSound(p.getLocation(), STEALTH_SOUND2, 5, 1);
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHit(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			ClassManager cm = new ClassManager(plugin, e.getEntity());
			Player p = (Player) e.getEntity();
			if(cm.hasCl(p)){
				if(cm.getCl(e.getEntity().getName()).equalsIgnoreCase("Thief")) {
					if(((Player) e.getEntity()).hasPotionEffect(PotionEffectType.INVISIBILITY)) {

						if(e.getDamager() instanceof Player) {
							if(ClanConfigManager.inSameClan(e.getEntity().getName(), e.getDamager().getName())) {
								return;
							}
						}else if(e.getDamager() instanceof Projectile) {
							Projectile proj = (Projectile) e.getDamager();
							if(proj.getShooter() instanceof Player) {
								Player shooter = (Player) proj.getShooter();
								if(ClanConfigManager.inSameClan(e.getEntity().getName(), shooter.getName())) {
									return;
								}
							}
						}

						Player stealthPlayer = (Player) e.getEntity();
						stealthPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
						putOnArmor(stealthPlayer);

						for(Player pl : Bukkit.getOnlinePlayers()) {
							pl.spawnParticle(STEALTH_PARTICLE, stealthPlayer.getLocation(), 20);
							pl.playSound(stealthPlayer.getLocation(), STEALTH_SOUND2, 100, 100);
						}

						double initDmg = e.getDamage();
						e.setDamage(initDmg/2.5);

						stealthPlayer.setMetadata(STEALTH_METAVALUE, new FixedMetadataValue(plugin, STEALTH_COOLDOWN_TIME));
						stealthPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bYou are not stealthed anymore!")));
					}else {
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRightClickFeather(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.FEATHER)) return;
		
		if(!cm.getCl(p.getName()).equalsIgnoreCase("Thief")) return;
		
		if(p.hasMetadata(QUICKNESS_METAVALUE) && !p.getMetadata(QUICKNESS_METAVALUE).isEmpty()) {
			int secondsLeft = p.getMetadata(QUICKNESS_METAVALUE).get(0).asInt();
			if(secondsLeft == 0) {
				//If they have the speed potion effect, remove it and reapply it
				if(p.hasPotionEffect(PotionEffectType.SPEED)) {
					p.removePotionEffect(PotionEffectType.SPEED);
				}
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, QUICKNESS_DURATION, 0));
				p.setMetadata(QUICKNESS_METAVALUE, new FixedMetadataValue(plugin, QUICKNESS_COOLDOWN_TIME));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bQuickness activated!")));
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(QUICKNESS_PARTICLE, p.getLocation(), 20);
					pl.playSound(p.getLocation(), QUICKNESS_SOUND, 100, 100);
				}
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds!", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			//If they have the speed potion effect, remove it and reapply it
			if(p.hasPotionEffect(PotionEffectType.SPEED)) {
				p.removePotionEffect(PotionEffectType.SPEED);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, QUICKNESS_DURATION, 0));
			p.setMetadata(QUICKNESS_METAVALUE, new FixedMetadataValue(plugin, QUICKNESS_COOLDOWN_TIME));
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bQuickness activated!")));
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(QUICKNESS_PARTICLE, p.getLocation(), 20);
				pl.playSound(p.getLocation(), QUICKNESS_SOUND, 100, 100);
			}
		}
		
	}
	
	@EventHandler
	public void onRightClickSpiderEye(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.SPIDER_EYE)) return;
		
		if(!cm.getCl(p.getName()).equalsIgnoreCase("Thief")) return;
		
		if(p.hasMetadata(BLINDNESS_METAVALUE) && !p.getMetadata(BLINDNESS_METAVALUE).isEmpty()) {
			int secondsLeft = p.getMetadata(BLINDNESS_METAVALUE).get(0).asInt();
			if(secondsLeft == 0) {
				WitherSkull proj = p.launchProjectile(WitherSkull.class);
				proj.setVelocity(proj.getDirection().multiply(10));
				p.setMetadata(BLINDNESS_METAVALUE, new FixedMetadataValue(plugin, BLINDNESS_COOLDOWN_TIME));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds!", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			WitherSkull proj = p.launchProjectile(WitherSkull.class);
			proj.setVelocity(proj.getDirection().multiply(10));
			p.setMetadata(BLINDNESS_METAVALUE, new FixedMetadataValue(plugin, BLINDNESS_COOLDOWN_TIME));
		}
		
	}
	
	@EventHandler
	public void onWitherSkullHit(EntityDamageByEntityEvent e) {
		
		if(e.getDamager() instanceof WitherSkull) {
			WitherSkull ws = (WitherSkull) e.getDamager();
			Player shooter = (Player) ws.getShooter();
			Player damaged = (Player) e.getEntity();
			
			AttributeManager am = new AttributeManager(plugin, shooter);
			
			double initDmg = e.getDamage();
			double dmgAfterSpellPowerFactor = am.getNewSpellDmg(initDmg);
			double distance = shooter.getLocation().distance(damaged.getLocation());
			
			if(distance >= 5) {
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION2, 0));
				e.setDamage(dmgAfterSpellPowerFactor);
			}else {
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION, 0));
				e.setDamage(dmgAfterSpellPowerFactor);
				shooter.setMetadata(BLINDNESS_METAVALUE, new FixedMetadataValue(plugin, BLINDNESS_COOLDOWN_TIME2));
			}
			
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(BLINDNESS_PARTICLE, damaged.getLocation(), 20);
				pl.playSound(damaged.getLocation(), BLINDNESS_SOUND, 5, 1);
			}
			
		}else if(e.getDamager() instanceof Player) {
			
			if(e.getEntity() instanceof Player) {
				
				Player damager = (Player) e.getDamager();
				Player damaged = (Player) e.getEntity();
				
				ItemStack itemInHand = damager.getItemInHand();
				
				if(!itemInHand.getType().equals(Material.SPIDER_EYE)) return;
				
				if(damager.hasMetadata(BLINDNESS_METAVALUE) && !damager.getMetadata(BLINDNESS_METAVALUE).isEmpty()) {
					int secondsLeft = damager.getMetadata(BLINDNESS_METAVALUE).get(0).asInt();
					if(secondsLeft != 0) {
						Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds!", true, damager, "&8&l[&aClasses&8&l]");
						return;
					}
				}
				
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
				damager.setMetadata(BLINDNESS_METAVALUE, new FixedMetadataValue(plugin, BLINDNESS_COOLDOWN_TIME2));
				
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(BLINDNESS_PARTICLE, damaged.getLocation(), 20);
					pl.playSound(damaged.getLocation(), BLINDNESS_SOUND, 1, 1);
				}
				
			/*
			 * This is the code block it goes to when the entity is not a player. You could have the mobs do something when they are hit with the spider eye in the future...
			 */
			}else {
				return;
			}
	
		}	
		
	}
	
	public void takeOffArmor(Player p) {
		PlayerInventory pI = p.getInventory();
		
		invManager.storeArmor(p, pI);
		
		pI.setHelmet(null);
		pI.setChestplate(null);
		pI.setLeggings(null);
		pI.setBoots(null);
			
	}
	
	public void putOnArmor(Player p) {
		ArrayList<ItemStack> armor = invManager.getArmor(p);
		p.getInventory().setHelmet(armor.get(0));
		p.getInventory().setChestplate(armor.get(1));
		p.getInventory().setLeggings(armor.get(2));
		p.getInventory().setBoots(armor.get(3));	
	}
	
}
