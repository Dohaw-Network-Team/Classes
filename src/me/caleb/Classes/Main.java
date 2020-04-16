package me.caleb.Classes;

import java.io.File;

import me.lucko.luckperms.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.caleb.Classes.commands.Commands;
import me.caleb.Classes.listeners.AttributeManagerListener;
import me.caleb.Classes.listeners.GeneralListener;
import me.caleb.Classes.listeners.classes.ArcherListener;
import me.caleb.Classes.listeners.classes.BruteListener;
import me.caleb.Classes.listeners.classes.Ice_WizardListener;
import me.caleb.Classes.listeners.classes.PeacemakerListener;
import me.caleb.Classes.listeners.classes.ThiefListener;
import me.caleb.Classes.runnables.HealthAndClassSB;
import me.caleb.Classes.runnables.HitCounter;
import me.caleb.Classes.runnables.cooldowns.BQuickeningCooldown;
import me.caleb.Classes.runnables.cooldowns.HitCounterCooldown;
import me.caleb.Classes.runnables.cooldowns.IWIceStrikeCooldown;
import me.caleb.Classes.runnables.cooldowns.IWKnockbackCooldown;
import me.caleb.Classes.runnables.cooldowns.IWLegWeakeningCooldown;
import me.caleb.Classes.runnables.cooldowns.PMHolyRejuvenationCooldown;
import me.caleb.Classes.runnables.cooldowns.PMLegPainCooldown;
import me.caleb.Classes.runnables.cooldowns.PMSFCooldown;
import me.caleb.Classes.runnables.cooldowns.PMTOLCooldown;
import me.caleb.Classes.runnables.cooldowns.PrayerToTheGodsCooldown;
import me.caleb.Classes.runnables.cooldowns.RelentlessStandCooldown;
import me.caleb.Classes.runnables.cooldowns.TEyeGougeCooldown;
import me.caleb.Classes.runnables.cooldowns.TQuickeningCooldown;
import me.caleb.Classes.runnables.cooldowns.TStealthCooldown;
import me.caleb.Classes.scoreboards.ScoreboardManager;

public class Main extends JavaPlugin{

	private static Main instance;

	@Override
	public void onEnable() {

		this.instance = this;

		File[] files = {new File(this.getDataFolder(),"players.yml"), new File(this.getDataFolder(),"attributes.yml"), new File(this.getDataFolder(),"config.yml"), new File(this.getDataFolder(),"playerInv.yml")};
		
		for(File f : files) {
			if(!f.exists()) {
				this.saveResource(f.getName(), false);
				Bukkit.getConsoleSender().sendMessage("[Classes] Loading " + f.getName());
			}
		}
		
		new Commands(this);
		new ScoreboardManager(this);
		
		new ArcherListener(this);
		new BruteListener(this);
		new Ice_WizardListener(this);
		new ThiefListener(this);
		new PeacemakerListener(this);
		new AttributeManagerListener(this);
		new GeneralListener(this);
		
		new BQuickeningCooldown(this).runTaskTimer(this, 0L, 20L);
		new HitCounter(this).runTaskTimer(this, 0L, 20L);
		new PrayerToTheGodsCooldown(this).runTaskTimer(this, 0L, 20L);
		new HitCounterCooldown(this).runTaskTimer(this, 0L, 20L);
		new RelentlessStandCooldown(this).runTaskTimer(this, 0L, 20L);
		new IWIceStrikeCooldown(this).runTaskTimer(this, 0L, secondsToTicks(this.getConfig().getDouble("Cooldowns.Ice Strike")));
		new IWKnockbackCooldown(this).runTaskTimer(this, 0L, 20L);
		new IWLegWeakeningCooldown(this).runTaskTimer(this, 0L, 20L);
		new TStealthCooldown(this).runTaskTimer(this, 0L, 20L);
		new TQuickeningCooldown(this).runTaskTimer(this, 0L, 20L);
		new TEyeGougeCooldown(this).runTaskTimer(this, 0L, 20L);
		new PMTOLCooldown(this).runTaskTimer(this, 0L, 20L);
		new PMLegPainCooldown(this).runTaskTimer(this, 0L, 20L);
		new PMSFCooldown(this).runTaskTimer(this, 0L, 20L);
		new PMHolyRejuvenationCooldown(this).runTaskTimer(this, 0L, 20L);
		
		new HealthAndClassSB(this).runTaskTimer(this, 0L, 10L);
		
	}
	
	@Override
	public void onDisable() {
		
	}

	public static Main getInstance(){
		return instance;
	}
	
	public static LuckPerms getLP() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if(provider != null) {
			return provider.getProvider();
		}
		return null;
	}
	
	public static long secondsToTicks(double seconds) {
		return (long) (seconds*20);
	}

}
