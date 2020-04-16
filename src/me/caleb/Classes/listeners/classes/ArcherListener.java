package me.caleb.Classes.listeners.classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import me.caleb.RandomTreasure.ConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ArcherListener extends Utils implements Listener{

	private Main plugin;
	
	public ArcherListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlazeRodRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.BLAZE_ROD)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Archer")) return;
		
		try {
			prayerToTheGods(p);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof Player)) return;
		
		if(!(e.getDamager() instanceof Player)) return;
		
		Player damaged = (Player) e.getEntity();
		Player damager = (Player) e.getDamager();
		
		ClassManager cm = new ClassManager(plugin, damaged);
		
		if(ClanConfigManager.inSameClan(damaged.getName(), damager.getName())) return; 
		
		if(!cm.getCl(damaged.getName()).equalsIgnoreCase("Archer")) return;
		
		int hitCounter = 0;
		
		if(!damaged.hasMetadata("Hit_CounterCooldown") || damaged.getMetadata("Hit_CounterCooldown").isEmpty() || damaged.getMetadata("Hit_CounterCooldown").get(0).asInt() == 0) {
			
			if(damaged.getMetadata("Hit_Counter").isEmpty() || !damaged.hasMetadata("Hit_Counter")) {
				damaged.setMetadata("Hit_Counter", new FixedMetadataValue(plugin, 1));
			}else {
				hitCounter = damaged.getMetadata("Hit_Counter").get(0).asInt();
				hitCounter++;
				damaged.setMetadata("Hit_Counter", new FixedMetadataValue(plugin,hitCounter));
				if(hitCounter == 3) {
					Collection<PotionEffect> effects = damaged.getActivePotionEffects();
					damaged.setMetadata("Hit_CounterCooldown", new FixedMetadataValue(plugin, 10));
					if(effects.isEmpty()) {
						damaged.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
					}else {
						for(PotionEffect pe : effects) {
							PotionEffectType pet = pe.getType();
							if(pet.equals(PotionEffectType.SPEED)) {
								return;
							}
						}
						damaged.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
						damaged.setMetadata("Hit_Counter", new FixedMetadataValue(plugin, 0));
					}
					
					for(Player pl : Bukkit.getOnlinePlayers()) {
						pl.spawnParticle(Particle.FLASH, damaged.getLocation(), 1);
						pl.playSound(damaged.getLocation(), Sound.BLOCK_WOOL_PLACE, 100, 100);
					}
					
					damaged.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bEscapee activated!")));
				}
			}
		}
		
		
	}
	
	public void prayerToTheGods(Player p) throws ParseException {
		
		if(!p.getMetadata("Prayer_To_The_Gods_Cooldown").isEmpty()) {
			if(!ifCooldownDone(p)) {
				int minutesLeft = p.getMetadata("Prayer_To_The_Gods_Cooldown").get(0).asInt();
				Utils.sendPlayerMessage("This spell is on cooldown for another &a&l" + minutesLeft + "&r minutes", true, p, "&8&l[&aClasses&8&l]");
				return;
			}
		}

		List<Location> shrines = new ArrayList<Location>();

		Location playerLoc = p.getLocation();
		Location closestShrine = null;
		
		for(int i = 0; i < shrines.size(); i++) {
			
			double x = shrines.get(i).getX();
			double y = shrines.get(i).getY();
			double z = shrines.get(i).getZ();
			
			Location shrineLoc = new Location(playerLoc.getWorld(), x, y, z);
			
			if(i == 0) {
				closestShrine = shrineLoc;
			}else {
				
				if(closestShrine.distance(playerLoc) > shrineLoc.distance(playerLoc)) {
					closestShrine = shrineLoc;
				}
			}
			
		}
		
		int distance = (int) closestShrine.distance(playerLoc);
		
		Utils.sendPlayerMessage("The gods have answered you! The closest shrine to you is &5&l" + distance + "&r blocks away. Here are the coordinates: &bX: " + (int)closestShrine.getX() + " Y: " + (int)closestShrine.getY() + " Z: " + (int)closestShrine.getZ(), true, p, "&8&l[&aClasses&8&l]");
		setCooldown(p);

	}
	
	public LocalDate toLocal(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public void setCooldown(Player p) {	
		p.setMetadata("Prayer_To_The_Gods_Cooldown", new FixedMetadataValue(plugin, 60));	
	}
	
	public boolean ifCooldownDone(Player p) {
		
		if(p.hasMetadata("Prayer_To_The_Gods_Cooldown")) {
			if(p.getMetadata("Prayer_To_The_Gods_Cooldown").get(0).asInt() == 0) {
				return true;
			}else {
				return false;
			}
		}
		
		return false;
	
	}
	
	
}
