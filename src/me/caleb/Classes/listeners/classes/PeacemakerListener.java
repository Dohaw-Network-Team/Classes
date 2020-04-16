package me.caleb.Classes.listeners.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;
import me.caleb.Classes.utils.managers.PMAttributeManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PeacemakerListener extends Utils implements Listener{

	private Main plugin;
	
	final String TOL_METAVALUE = "PMTOL";
	final int TOL_COOLDOWN = 6;
	final Sound TOL_SOUND = Sound.ENTITY_VILLAGER_CELEBRATE;
	final Particle TOL_PARTICLE = Particle.VILLAGER_HAPPY;
	final double TOL_HEAL_AMOUNT = .5;
	
	final String LP_METAVALUE = "PMLegPain";
	final int LP_COOLDOWN = 15;
	final Sound LP_SOUND = Sound.ITEM_ARMOR_EQUIP_CHAIN;
	final Particle LP_PARTICLE = Particle.SPELL_INSTANT;
	
	final String SF_METAVALUE = "PMSteadyFront";
	public static final int SF_COOLDOWN = 25;
	final Sound SF_SOUND = Sound.BLOCK_ANVIL_PLACE;
	
	final Particle SF_PARTICLE = Particle.TOTEM;
	final static Particle SF_PARTICLE2 = Particle.SQUID_INK;
	final static int WALL_WIDTH = 5;
    final static int WALL_HEIGHT = 3;
	
    final String HR_METAVALUE = "PMHolyR";
    public static final int HR_COOLDOWN = 30;
    final Particle HR_PARTICLE = Particle.SPELL;
    final Sound HR_SOUND = Sound.ENTITY_EVOKER_CAST_SPELL;
    final int HR_REGEN_DURATION = 10;
    final double HR_SPEED_DURATION = 5.5;
    
    /*
     * Percent of max health that is added on to your current health
     */
    final double HR_PERCENTAGE = .25;
    
	public PeacemakerListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onGoldCarrotRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.GOLDEN_CARROT)) return;

		if(!cm.hasCl(p)) return;

		if(!cm.getCl(p.getName()).equalsIgnoreCase("Peacemaker")) return;
		
		if(p.hasMetadata(TOL_METAVALUE) && !p.getMetadata(TOL_METAVALUE).isEmpty()) {
			int secondsLeft = p.getMetadata(TOL_METAVALUE).get(0).asInt();
			if(secondsLeft == 0) {
				List<Entity> nearbyEntities = p.getNearbyEntities(30,30,30);
				nearbyEntities.get(0).remove();
				if(nearbyEntities.size() == 0) {
					Utils.sendPlayerMessage("There is nobody within a 30 block range of you!", true, p, "&8&l[&aClasses&8&l]");
				}else {
					TOL(nearbyEntities, p);
				}
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds!", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			List<Entity> nearbyEntities = p.getNearbyEntities(30,30,30);
			if(nearbyEntities.size() == 0) {
				Utils.sendPlayerMessage("There is nobody within a 30 block range of you!", true, p, "&8&l[&aClasses&8&l]");
			}else {
				TOL(nearbyEntities, p);
			}
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

		if(!cm.hasCl(p.getPlayer())) return;
		if(!cm.getCl(p.getName()).equalsIgnoreCase("Peacemaker")) return;
		
		//Makes it to where they don't actually lose their snowball
		e.setCancelled(true);
		
		if(!p.getMetadata(LP_METAVALUE).isEmpty() && p.hasMetadata(LP_METAVALUE)) {
			int cooldownTimer = p.getMetadata(LP_METAVALUE).get(0).asInt();
			if(cooldownTimer == 0) {
				Snowball snowball = p.launchProjectile(Snowball.class);
				snowball.setVelocity(snowball.getVelocity().multiply(3));
				p.setMetadata(LP_METAVALUE, new FixedMetadataValue(plugin, LP_COOLDOWN));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + cooldownTimer + " seconds", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			p.launchProjectile(Snowball.class);
			p.setMetadata(LP_METAVALUE, new FixedMetadataValue(plugin, LP_COOLDOWN));
		}
		
	}
	
	@EventHandler
	public void onNautilusShellRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.NAUTILUS_SHELL)) return;
		
		if(!cm.getCl(p.getName()).equalsIgnoreCase("Peacemaker")) return;
		
		PMAttributeManager am = new PMAttributeManager(plugin, p, cm.getCl(p.getName()));
		
		if(p.hasMetadata(HR_METAVALUE) && !p.getMetadata(HR_METAVALUE).isEmpty()) {
			int secondsLeft = p.getMetadata(HR_METAVALUE).get(0).asInt();
			if(secondsLeft == 0) {
				
				double additive = p.getMaxHealth() * HR_PERCENTAGE;
				
				am.removeMaxHealthAttribute();
				am.applyHolyShield(additive);
				p.setMetadata(HR_METAVALUE, new FixedMetadataValue(plugin, HR_COOLDOWN));
				
				for(Player pl : Bukkit.getOnlinePlayers()) {
					pl.spawnParticle(HR_PARTICLE, p.getLocation(), 10);
					pl.playSound(p.getLocation(), HR_SOUND, 1, 1);
				}
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, HR_REGEN_DURATION * 20, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (HR_SPEED_DURATION * 20), 2));
				
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds", true, p, "&8&l[&aClasses&8&l]");
			}
			
		}else {
			double additive = p.getMaxHealth() * HR_PERCENTAGE;
			
			am.removeMaxHealthAttribute();
			am.applyHolyShield(additive);
			p.setMetadata(HR_METAVALUE, new FixedMetadataValue(plugin, HR_COOLDOWN));
			
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(HR_PARTICLE, p.getLocation(), 10);
				pl.playSound(p.getLocation(), HR_SOUND, 1, 1);
			}
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, HR_REGEN_DURATION * 20, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (HR_SPEED_DURATION * 20), 2));
			
		}
		
	}
	
	@EventHandler
	public void onBricksRightClick(PlayerInteractEvent e) {
		
		Action action = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ClassManager cm = new ClassManager(plugin, p);
		
		if(!(e.getPlayer() instanceof Player)) return;
		
		if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if(!item.getType().equals(Material.BRICKS)) return;
		
		if(!cm.getCl(p.getName()).equalsIgnoreCase("Peacemaker")) return;
		
		if(!p.getMetadata(SF_METAVALUE).isEmpty() && p.hasMetadata(SF_METAVALUE)) {
			int secondsLeft = p.getMetadata(SF_METAVALUE).get(0).asInt();
			if(secondsLeft == 0) {
				String direction = getCardinalDirection(p);
				placeBricks(p, direction);
				p.setMetadata(SF_METAVALUE, new FixedMetadataValue(plugin, SF_COOLDOWN));
			}else {
				Utils.sendPlayerMessage("This spell is on cooldown for another " + secondsLeft + " seconds", true, p, "&8&l[&aClasses&8&l]");
			}
		}else {
			String direction = getCardinalDirection(p);
			placeBricks(p, direction);
			p.setMetadata(SF_METAVALUE, new FixedMetadataValue(plugin, SF_COOLDOWN));
		}
		
	}
	
	public Player getLowestPlayer(List<Entity> nearestEntities, Player you) {
		
		Player lowestPlayer = you;
		
		for(int x = 0; x < nearestEntities.size(); x++) {
			if(nearestEntities.get(x) instanceof Player) {
				
				Player currentPlayer = (Player) nearestEntities.get(x);
				double currentPlayerHp = currentPlayer.getHealth();
				double lowestPlayerHp = lowestPlayer.getHealth();
				
				if(ClanConfigManager.inSameClan(you.getName(), currentPlayer.getName())) {
					if(currentPlayerHp < lowestPlayerHp) {
						lowestPlayer = currentPlayer;
					}else {
						continue;
					}	
				}	
			}
		}
		
		return lowestPlayer;
		
	}
	
	public void TOL(List<Entity> nearbyEntities, Player p) {
		
		Player lowestPlayer = getLowestPlayer(nearbyEntities, p);
		
		double lowestPlayerMax = lowestPlayer.getMaxHealth();
		double lowestPlayerCurrent = lowestPlayer.getHealth();
		double newHealth = lowestPlayerCurrent + (lowestPlayerMax * TOL_HEAL_AMOUNT);
		
		if(newHealth > lowestPlayerMax) {
			if(lowestPlayer.getUniqueId().equals(p.getUniqueId())) {
				Utils.sendPlayerMessage("You have healed yourself for &4&l" + Utils.roundToPoint1((lowestPlayerMax - lowestPlayerCurrent)) + " HP!", true, p, "&8&l[&aClasses&8&l]");
			}else {
				Utils.sendPlayerMessage("You have healed &a&l" + lowestPlayer.getName() + "&r for &4&l" + Utils.roundToPoint1((lowestPlayerMax - lowestPlayerCurrent)) + " HP!", true, p, "&8&l[&aClasses&8&l]");
			}
			lowestPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&b&l[&a&l+" + "&4&l" + Utils.roundToPoint1((lowestPlayerMax - lowestPlayerCurrent)) + " HP!" + "&b&l]")));
			lowestPlayer.setHealth(lowestPlayerMax);
		}else if(newHealth < lowestPlayerMax){
			if(lowestPlayer.getUniqueId().equals(p.getUniqueId())) {
				Utils.sendPlayerMessage("You have healed yourself for &4&l" + Utils.roundToPoint1((newHealth - lowestPlayerCurrent)) + " HP!", true, p, "&8&l[&aClasses&8&l]");
			}else {
				Utils.sendPlayerMessage("You have healed &a&l" + lowestPlayer.getName() + "&r for &4&l" + Utils.roundToPoint1((newHealth - lowestPlayerCurrent)) + " HP!", true, p, "&8&l[&aClasses&8&l]");
			}
			lowestPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&b&l[&a&l+" + "&4&l" + Utils.roundToPoint1((newHealth - lowestPlayerCurrent)) + " HP!" + "&b&l]")));
			lowestPlayer.setHealth(newHealth);
		}else {
			return;
		}
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			pl.playSound(p.getLocation(), TOL_SOUND, 5, 1);
			pl.spawnParticle(TOL_PARTICLE, p.getLocation(), 10);
		}
		
		p.setMetadata(TOL_METAVALUE, new FixedMetadataValue(plugin, TOL_COOLDOWN));
		
	}
	
	public void placeBricks(Player p, String direction) {
		
		final int SPACE_AMOUNT = 3;
		
		Location loc = p.getLocation();
		
		switch (direction) {
			case "N":
				
				loc.setX(loc.getX() - SPACE_AMOUNT);
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() + ((WALL_WIDTH - 1) / 2));
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setZ(loc.getZ() - 1);		
					}
					loc.setZ(loc.getZ() + (WALL_WIDTH));
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "S":
				
				loc.setX(loc.getX() + SPACE_AMOUNT);
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() - ((WALL_WIDTH - 1) / 2));
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setZ(loc.getZ() + 1);		
					}
					loc.setZ(loc.getZ() - (WALL_WIDTH));
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "E":
				
				loc.setZ(loc.getZ() - SPACE_AMOUNT);
				
				playSFSoundAndParticles(loc);
				
				loc.setX(loc.getX() - ((WALL_WIDTH - 1) / 2));
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() + 1);		
					}
					loc.setX(loc.getX() - (WALL_WIDTH));
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "W":
				
				loc.setZ(loc.getZ() + SPACE_AMOUNT);
				
				playSFSoundAndParticles(loc);
				
				loc.setX(loc.getX() + ((WALL_WIDTH - 1) / 2));
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() - 1);		
					}
					loc.setX(loc.getX() + (WALL_WIDTH));
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "NE":
				
				loc.setX(loc.getX() - (SPACE_AMOUNT - 1));
				loc.setZ(loc.getZ() - (SPACE_AMOUNT - 1));
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() + 2);
				loc.setX(loc.getX() - 2);
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() + 1);
						loc.setZ(loc.getZ() - 1);
					}
					loc.setZ(loc.getZ() + WALL_WIDTH);
					loc.setX(loc.getX() - WALL_WIDTH);
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "SE":
				
				loc.setX(loc.getX() + (SPACE_AMOUNT - 1));
				loc.setZ(loc.getZ() - (SPACE_AMOUNT - 1));
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() - 2);
				loc.setX(loc.getX() - 2);
				
				saveSFStartLocation(p, loc, direction);
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() + 1);
						loc.setZ(loc.getZ() + 1);
					}
					loc.setZ(loc.getZ() - WALL_WIDTH);
					loc.setX(loc.getX() - WALL_WIDTH);
					loc.setY(loc.getY() + 1);
				}
				break;
				
			case "NW":
				
				loc.setX(loc.getX() - (SPACE_AMOUNT - 1));
				loc.setZ(loc.getZ() + (SPACE_AMOUNT - 1));
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() + 2);
				loc.setX(loc.getX() + 2);
				
				saveSFStartLocation(p, loc, direction);

				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() - 1);
						loc.setZ(loc.getZ() - 1);
					}
					loc.setZ(loc.getZ() + WALL_WIDTH);
					loc.setX(loc.getX() + WALL_WIDTH);
					loc.setY(loc.getY() + 1);
				}
				break;
			
			case "SW":
				
				loc.setX(loc.getX() + (SPACE_AMOUNT - 1));
				loc.setZ(loc.getZ() + (SPACE_AMOUNT - 1));
				
				playSFSoundAndParticles(loc);
				
				loc.setZ(loc.getZ() - 2);
				loc.setX(loc.getX() + 2);
				
				saveSFStartLocation(p, loc, direction);
				
				
				for(int y = 0; y < WALL_HEIGHT; y++) {
					for(int z = 0; z < WALL_WIDTH; z++) {
						if(loc.getBlock().getType().equals(Material.AIR)) {
							loc.getBlock().setType(Material.BRICKS);
						}
						loc.setX(loc.getX() - 1);
						loc.setZ(loc.getZ() + 1);
					}
					loc.setZ(loc.getZ() - WALL_WIDTH);
					loc.setX(loc.getX() + WALL_WIDTH);
					loc.setY(loc.getY() + 1);
				}
				break;
				
			default:
				break;
		}
		
	}
	
	public void saveSFStartLocation(Player p, Location loc, String direction) {
		p.setMetadata("SF_STARTLOC_WORLD", new FixedMetadataValue(plugin, loc.getWorld().getName()));
		p.setMetadata("SF_DIRECTION", new FixedMetadataValue(plugin, direction));
		p.setMetadata("SF_STARTLOC_X", new FixedMetadataValue(plugin, loc.getX()));
		p.setMetadata("SF_STARTLOC_Y", new FixedMetadataValue(plugin, loc.getY()));
		p.setMetadata("SF_STARTLOC_Z", new FixedMetadataValue(plugin, loc.getZ()));
	}
	
	public void playSFSoundAndParticles(Location l) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(l, SF_SOUND, 2, 1);
			p.spawnParticle(SF_PARTICLE, l, 10);
		}
	}
	
	public static void removeSF(Player p) {
		
		if(p.hasMetadata("SF_STARTLOC_X") && !p.getMetadata("SF_STARTLOC_X").isEmpty()) {
			
			double startX = p.getMetadata("SF_STARTLOC_X").get(0).asDouble();
			double startY = p.getMetadata("SF_STARTLOC_Y").get(0).asDouble();
			double startZ = p.getMetadata("SF_STARTLOC_Z").get(0).asDouble();
			
			World w = Bukkit.getWorld(p.getMetadata("SF_STARTLOC_WORLD").get(0).asString());
			String direction = p.getMetadata("SF_DIRECTION").get(0).asString();
			
			Location startLoc = new Location(w, startX, startY, startZ);
			Location currentLoc = startLoc;
			
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.spawnParticle(SF_PARTICLE2, startLoc, 5);
			}
			
			switch(direction) {
			
				case "N":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setZ(currentLoc.getZ() - 1);		
						}
						currentLoc.setZ(currentLoc.getZ() + (WALL_WIDTH));
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
				
				case "S":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setZ(currentLoc.getZ() + 1);		
						}
						currentLoc.setZ(currentLoc.getZ() - (WALL_WIDTH));
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
					
				case "E":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() + 1);		
						}
						currentLoc.setX(currentLoc.getX() - (WALL_WIDTH));
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
				
				case "W":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() - 1);		
						}
						currentLoc.setX(currentLoc.getX() + (WALL_WIDTH));
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
					
				case "NE":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z< WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() + 1);
							currentLoc.setZ(currentLoc.getZ() - 1);
						}
						currentLoc.setZ(currentLoc.getZ() + WALL_WIDTH);
						currentLoc.setX(currentLoc.getX() - WALL_WIDTH);
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
					
				case "SE":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() + 1);
							currentLoc.setZ(currentLoc.getZ() + 1);
						}
						currentLoc.setZ(currentLoc.getZ() - WALL_WIDTH);
						currentLoc.setX(currentLoc.getX() - WALL_WIDTH);
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
					
				case "NW":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() - 1);
							currentLoc.setZ(currentLoc.getZ() - 1);
						}
						currentLoc.setZ(currentLoc.getZ() + WALL_WIDTH);
						currentLoc.setX(currentLoc.getX() + WALL_WIDTH);
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
				
				case "SW":
					
					for(int y = 0; y < WALL_HEIGHT; y++) {
						for(int z = 0; z < WALL_WIDTH; z++) {
							if(currentLoc.getBlock().getType().equals(Material.BRICKS)) {
								currentLoc.getBlock().setType(Material.AIR);
							}
							currentLoc.setX(currentLoc.getX() - 1);
							currentLoc.setZ(currentLoc.getZ() + 1);
						}
						currentLoc.setZ(currentLoc.getZ() - WALL_WIDTH);
						currentLoc.setX(currentLoc.getX() + WALL_WIDTH);
						currentLoc.setY(currentLoc.getY() + 1);
					}
					break;
					
				default:
					break;
			}	
		}
	}
	
}
