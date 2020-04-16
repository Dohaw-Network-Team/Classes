package me.caleb.Classes.listeners;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.AttributeManager;
import me.caleb.Classes.utils.managers.ClassManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class AttributeManagerListener extends Utils implements Listener{

	private Main plugin;
	private int streakValue;
	
	final String BOW_STREAK = "bow_streak";
	
	public AttributeManagerListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerArrowHit(EntityDamageByEntityEvent e) {

		//If the player hit is a player
		if(!e.isCancelled()){
			if(e.getEntity() instanceof Player) {
				if(e.getDamager().getType() == EntityType.ARROW) {
					Arrow arrow = (Arrow) e.getDamager();
					if(arrow.getShooter() instanceof Player) {

						AttributeManager amShooter = new AttributeManager(plugin, (Player) arrow.getShooter());
						AttributeManager amDmgTaker = new AttributeManager(plugin, e.getEntity());
						Player p = (Player) arrow.getShooter();
						ClassManager cm = new ClassManager(plugin, (Player) arrow.getShooter());
						double initDmg = 0;

						//If they aren't an archer, then there initial damage is cut in half
						if(!cm.getCl(p.getName()).equalsIgnoreCase("Archer")) {
							initDmg = (e.getFinalDamage() / 2.5);
						}else {
							if(p.hasMetadata(BOW_STREAK) && !p.getMetadata(BOW_STREAK).isEmpty()) {

								streakValue = p.getMetadata(BOW_STREAK).get(0).asInt();

								if(streakValue == 4) {

									double mult = ThreadLocalRandom.current().nextDouble(1.5, 1.8);

									p.setMetadata(BOW_STREAK, new FixedMetadataValue(plugin,1));

									initDmg = e.getFinalDamage() * mult;

								}else if(streakValue == 3){
									hotStreakAura(p);
									p.setMetadata(BOW_STREAK, new FixedMetadataValue(plugin,streakValue + 1));
									initDmg = e.getFinalDamage();
								}else{
									p.setMetadata(BOW_STREAK, new FixedMetadataValue(plugin,streakValue + 1));
									initDmg = e.getFinalDamage();
								}

							}else {
								p.setMetadata(BOW_STREAK, new FixedMetadataValue(plugin,0));
							}

						}

						double dmgAfterRangedDmgFactor = amShooter.getNewRangedDmg(initDmg);
						double dmgAfterToughnessFactor = amDmgTaker.factorInToughness(dmgAfterRangedDmgFactor);

						e.setDamage(dmgAfterToughnessFactor);

						if(streakValue == 4) {
							p.sendMessage(Utils.chat("You have hit &a&l" + e.getEntity().getName() + "&r for about &4&l" + Utils.roundToPoint5(dmgAfterToughnessFactor) + " HP"));
						}else {
							p.sendMessage(Utils.chat("You have hit &a&l" + e.getEntity().getName() + "&r for about &a&l" + Utils.roundToPoint5(dmgAfterToughnessFactor) + " HP"));
						}

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&4&l[Streak: " + streakValue + "]")));

						if(Utils.roundToPoint5(dmgAfterToughnessFactor) >= 6) {
							criticalArrowHit(e.getEntity());
						}

					}else {
						return;
					}
				}else {
					return;
				}
			}else {
				if(e.getDamager().getType() == EntityType.ARROW) {

					Arrow arrow = (Arrow) e.getDamager();
					if(arrow.getShooter() instanceof Player) {

						AttributeManager amShooter = new AttributeManager(plugin, (Player) arrow.getShooter());
						Player p = (Player) arrow.getShooter();
						ClassManager cm = new ClassManager(plugin, (Player) arrow.getShooter());
						double initDmg = 0;

						if(p.hasMetadata("bow_streak") && !p.getMetadata("bow_streak").isEmpty()) {

							streakValue = p.getMetadata("bow_streak").get(0).asInt();

							if(streakValue == 4) {

								double mult = ThreadLocalRandom.current().nextDouble(1.5, 1.8);

								p.setMetadata("bow_streak", new FixedMetadataValue(plugin,0));

								initDmg = e.getFinalDamage() * mult;

							}else if(streakValue == 3){
								hotStreakAura(p);
								p.setMetadata("bow_streak", new FixedMetadataValue(plugin,streakValue + 1));
								initDmg = e.getFinalDamage();
							}else{
								p.setMetadata("bow_streak", new FixedMetadataValue(plugin,streakValue + 1));
								initDmg = e.getFinalDamage();
							}

						}else {
							p.setMetadata("bow_streak", new FixedMetadataValue(plugin,0));
						}

						double dmgAfterRangedDmgFactor = amShooter.getNewRangedDmg(initDmg);

						e.setDamage(dmgAfterRangedDmgFactor);

						if(streakValue == 4) {
							p.sendMessage(Utils.chat("You have hit &a&l" + e.getEntity().getName() + "&r for about &4&l" + Utils.roundToPoint5(dmgAfterRangedDmgFactor) + " HP"));
						}else {
							p.sendMessage(Utils.chat("You have hit &a&l" + e.getEntity().getName() + "&r for about &a&l" + Utils.roundToPoint5(dmgAfterRangedDmgFactor) + " HP"));
						}

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.chat("&4&l[Streak: " + streakValue + "]")));

						if(Utils.roundToPoint5(dmgAfterRangedDmgFactor) >= 6) {
							criticalArrowHit(e.getEntity());
						}

					}

				}else {
					return;
				}
			}
		}


	}
	
	public void criticalArrowHit(Entity e) {
		Location damagedLoc = e.getLocation();
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			Location playerLoc = p.getLocation();
			if(damagedLoc.distance(playerLoc) < 35) {
				p.playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 100);
			}
			p.spawnParticle(Particle.EXPLOSION_NORMAL, e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), 50);
			
		}	
	}
	
	public void hotStreakAura(Player shooter) {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.spawnParticle(Particle.FLAME, shooter.getLocation().getX(), shooter.getLocation().getY(), shooter.getLocation().getZ(), 100);
		}
		shooter.playSound(shooter.getLocation(), Sound.BLOCK_CAMPFIRE_CRACKLE, 100, 100);
	}
	
	
	/*
	IMPLEMENT ATTRIBUTES FOR ENTITIES SO THAT THEY CAN DO THIS METHOD AS WELL BECAUSE AT THE MOMENT, ONLY PLAYERS CAN HAVE AN ATTRIBUTE MANAGER
	 */
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player)) return;

		if(!(e.getDamager() instanceof Player)) return;

		Player damager = (Player) e.getDamager();
		Player damaged = (Player) e.getEntity();
		
		AttributeManager amDamager = new AttributeManager(plugin, damager);
		AttributeManager amDmgTaker = new AttributeManager(plugin, damaged);
		ClassManager cm = new ClassManager(plugin, damager);

		double initDmg = 0;

		if(cm.hasCl(damager) && cm.hasCl(damaged)){
			if(!cm.getCl(damager.getName()).equalsIgnoreCase("Brute")) {
				initDmg = (e.getFinalDamage() / 2.65);
			}else {
				initDmg = e.getFinalDamage();
			}
		}else{
			return;
		}

		double dmgAfterStrenthFactor = amDamager.getNewHitDmg(initDmg);
		double dmgAfterToughnessFactor = amDmgTaker.factorInToughness(dmgAfterStrenthFactor);
		
		e.setDamage(dmgAfterToughnessFactor);
	}
	
	/*
	 * Later make it check to see if the player is in your faction or not.
	 * If so, then it does no damage to them
	 */
	@EventHandler
	public void onPlayerHarmfulPotionIntake(EntityDamageByEntityEvent e) {
		Entity damaged = e.getEntity();
		
		if(!(e.getDamager().getType().equals(EntityType.SPLASH_POTION))) return;
		
		if(!(damaged instanceof Player)) return;
		
		ThrownPotion pot = (ThrownPotion) e.getDamager();
		PotionType potType = getPotionType(pot);
		
		if(!potType.equals(PotionType.INSTANT_DAMAGE)) return;
		
		AttributeManager am = new AttributeManager(plugin, damaged);
		double initDmg = e.getFinalDamage();
		double newDmg = am.factorInToughness(initDmg);
		e.setDamage(newDmg);
		
	}
	
	@EventHandler
	public void onPoisonDamage(EntityDamageEvent e) {
		Entity damaged = e.getEntity();
		
		if(!e.getCause().equals(DamageCause.POISON)) return;
		
		if(!(damaged instanceof Player)) return;
		
		double initDmg = e.getFinalDamage();
		AttributeManager am = new AttributeManager(plugin, damaged);
		double newDmg = am.factorInResistance(initDmg, AttributeManager.potionTypes.POISON);
		e.setDamage(newDmg);
		
	}
	
	public PotionType getPotionType(ThrownPotion pot) {
		final PotionMeta META = (PotionMeta) pot.getItem().getItemMeta();
		final PotionData DATA = META.getBasePotionData();
		return DATA.getType();
	}
	
	@EventHandler
	public void onIceStrikeHit(EntityDamageByEntityEvent e) {
		
		if(!(e.getDamager() instanceof Fireball)) return;
		
		Fireball f = (Fireball) e.getDamager();
		
		if(!(e.getEntity() instanceof Player)) return;
		
		if(!(f.getShooter() instanceof Player)) return;
		
		Player damaged = (Player) e.getEntity();
		Player shooter = (Player) f.getShooter();
		double initDmg = e.getFinalDamage();
		
		AttributeManager amShooter = new AttributeManager(plugin, shooter);
		AttributeManager amDamaged = new AttributeManager(plugin, damaged);
		double dmgAfterSpellPower = amShooter.getNewSpellDmg(initDmg);
		double dmgAfterToughnessFactor = amDamaged.factorInToughness(dmgAfterSpellPower);
		
		e.setDamage(dmgAfterToughnessFactor);
		
	}
	
	//Struggled to adjust slowness to the potion. Will come back to this later down the road.
	
	/*
	@EventHandler
	public void onPotionSplash(PotionSplashEvent e) {

		PotionType potType = getPotionType(e.getPotion());
		
		if(!potType.equals(PotionType.SLOWNESS)) return;
		
		Collection<LivingEntity> playersAffected = e.getAffectedEntities();
		ThrownPotion pot = e.getPotion();
		PotionMeta pM = (PotionMeta) pot.getItem().getItemMeta();
		

		for(LivingEntity p : playersAffected) {
			p.getActivePotionEffects().add(new PotionEffect());
		}
		
	}*/
	
	
	
	
}
