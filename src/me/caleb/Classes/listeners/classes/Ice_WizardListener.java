package me.caleb.Classes.listeners.classes;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Ice_WizardListener extends Utils implements Listener{

	private Main plugin;
	
	public Ice_WizardListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	//When the ice strike is done
	@EventHandler
	public void onIceRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);

		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.PACKED_ICE)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Ice_Wizard")) return;
		
		if(p.hasMetadata("betweenFireballs") && !p.getMetadata("betweenFireballs").isEmpty()) {
			boolean betweenShots = p.getMetadata("betweenFireballs").get(0).asBoolean();
			
			//If they shot one shot in between the tick interval.
			//This prevents the spamming of ice strikes
			if(betweenShots == false) {

				Item droppedItem = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.PACKED_ICE));
				droppedItem.getLocation().setDirection(p.getLocation().getDirection());
				droppedItem.setVelocity(p.getLocation().getDirection().multiply(1.5));
				//droppedItem.setGravity(false);
				droppedItem.setPickupDelay(50);

				final double ICE_STRIKE_RADIUS = 2;

				new BukkitRunnable(){

					@Override
					public void run() {
						Bukkit.broadcastMessage(droppedItem.getLocation().getY() + "");
						if(!droppedItem.isOnGround() && (droppedItem.getLocation().getY() > 0.05 || droppedItem.getLocation().getY() < -0.05)){
							List<Entity> nearbyEntities = droppedItem.getNearbyEntities(ICE_STRIKE_RADIUS, ICE_STRIKE_RADIUS, ICE_STRIKE_RADIUS);
							Bukkit.broadcastMessage(nearbyEntities.toString());
							//Removes yourself as a possible nearby entity
							nearbyEntities.remove((Entity)p);
							if(!nearbyEntities.isEmpty()){
								Bukkit.broadcastMessage(nearbyEntities.toString());
								droppedItem.remove();
								Bukkit.broadcastMessage("Something has been hit!");
								this.cancel();
							}else{
								Bukkit.broadcastMessage(nearbyEntities.toString());
							}
						}else{
							Bukkit.broadcastMessage("It's probably at a velocity of 0");
							this.cancel();
						}
					}
				}.runTaskTimer(plugin, 0L, 1L);

				//fireball.setVelocity(fireball.getDirection().multiply(15));
				//fireball.setSilent(true);
				p.setMetadata("betweenFireballs", new FixedMetadataValue(plugin, true));
			}else {
				return;
			}	
		}else {
			p.setMetadata("betweenFireballs", new FixedMetadataValue(plugin, false));
		}
		
	}
	
	@EventHandler
	public void onTntRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.TNT)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Ice_Wizard")) return;
		
		final int cooldownTime = 15;
		final int blockRadius = 3;
		final double vectorMult = 1.5;
		final String metaValue = "KnockbackCooldown";
		
		List<Entity> nearbyEntities = (List<Entity>) p.getWorld().getNearbyEntities(p.getLocation(), blockRadius, blockRadius, blockRadius);

		if(!p.getMetadata(metaValue).isEmpty() && p.hasMetadata(metaValue)) {
			int seconds = p.getMetadata(metaValue).get(0).asInt();
			if(seconds == 0) {
				calculateKnockbackDamage(nearbyEntities, p);
				for(Entity ne : nearbyEntities) {
					if(!ne.getName().equalsIgnoreCase(p.getName())) {
						Vector directionLooking = p.getLocation().getDirection();
						directionLooking.multiply(vectorMult);
						directionLooking.setY(ne.getLocation().getDirection().getY()+.6);
						ne.setVelocity(directionLooking);
					}
				}
				
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation() , 100);
					pl.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 75, 50);
				}
				
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bKnockback activated!")));
				p.setMetadata(metaValue, new FixedMetadataValue(plugin, cooldownTime));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + seconds + " seconds", true, p, "&8&l[&aClasses&8&l]");
				return;
			}
		}else {
			calculateKnockbackDamage(nearbyEntities, p);
			for(Entity ne : nearbyEntities) {
				if(!ne.getName().equalsIgnoreCase(p.getName())) {
					Vector directionLooking = p.getLocation().getDirection();
					directionLooking.multiply(vectorMult);
					directionLooking.setY(ne.getLocation().getDirection().getY()+.6);
					ne.setVelocity(directionLooking);
				}
			}
			
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation() , 100);
				pl.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 75, 50);
			}
			
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&bKnockback activated!")));
			p.setMetadata(metaValue, new FixedMetadataValue(plugin, cooldownTime));
		}
		
		
	}
	
	@EventHandler
	public void onSnowballRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.SNOWBALL)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Ice_Wizard")) return;
		
		e.setCancelled(true);
		final int cooldownTime = 15;
		final String metaValue = "IWLegWeakening";
		
		if(!p.getMetadata(metaValue).isEmpty() && p.hasMetadata(metaValue)) {
			int cooldownTimer = p.getMetadata(metaValue).get(0).asInt();
			if(cooldownTimer == 0) {
				Snowball snowball = p.launchProjectile(Snowball.class);
				snowball.setVelocity(snowball.getVelocity().multiply(5));
				p.setMetadata(metaValue, new FixedMetadataValue(plugin, cooldownTime));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + cooldownTimer + " seconds", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			p.launchProjectile(Snowball.class);
			p.setMetadata(metaValue, new FixedMetadataValue(plugin, cooldownTime));
		}
		
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onSnowballHit(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof Player)) return;
	
		if(!(e.getDamager() instanceof Snowball)) return;

		Snowball s = (Snowball) e.getDamager();
		if(!(s.getShooter() instanceof Player)) return;

		Player thrower = (Player) s.getShooter();
		ClassManager cm = new ClassManager(plugin, thrower);

		if(!cm.hasCl(thrower)) return;
		if(!cm.getCl(thrower.getName()).equalsIgnoreCase("Ice_Wizard")) return;

		Player damaged = (Player) e.getEntity();
		
		final int duration = 60;
		final int level = 1;
		
		damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration ,(level - 1)));
	
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.spawnParticle(Particle.SPELL_WITCH, damaged.getLocation(), 10);
			p.playSound(damaged.getLocation(), Sound.BLOCK_SAND_BREAK, 100, 50);
		}
		
	}
	
	public void calculateKnockbackDamage(List<Entity> nearbyEntities, Player p) {
		
		for(Entity en : nearbyEntities) {
			
			if(en.getName().equalsIgnoreCase(p.getName())) continue;
			
			LivingEntity entity = (LivingEntity) en;
			
			Location playerLoc = p.getLocation();
			Location entityLoc = en.getLocation();
			
			double distance = playerLoc.distance(entityLoc);
			
			/*
			 * The most damage i want to do is 3 hearts
			 * 
			 * Every half a block increases the damage by 1 hp
			 * 
			 */
			
			if(distance <= 3.0) {
				if(distance >= 2.8) {
					entity.damage(0.5);
				}else if(distance >= 2.5) {
					entity.damage(1.5);
				}else if(distance >= 2) {
					entity.damage(2.5);
				}else if(distance >= 1.5) {
					entity.damage(3.5);
				}else if(distance >= 1) {
					entity.damage(4.5);
				}else if(distance >= 0.5) {
					entity.damage(5.5);
				}else if(distance < 0.5) {
					entity.damage(6);
				}
			}
		}
	}
	
	
	
}
