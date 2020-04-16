package me.caleb.Classes.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.caleb.Classes.Main;
import me.caleb.Classes.utils.Utils;
import me.caleb.Classes.utils.managers.ClassManager;

public class HealthAndClassSB extends BukkitRunnable{

	private Main plugin;
	
	public HealthAndClassSB(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			ClassManager cm = new ClassManager(plugin, p);
			Scoreboard b = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective o = b.registerNewObjective("test", "dummy");
			b.getObjective("test").getScore(p).setScore((int) p.getHealth());
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
			p.setScoreboard(b);
		}
		
	}
	
	

}
