package me.caleb.Classes.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Clan.utils.Utils;
import me.caleb.Classes.Main;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;

public class GeneralListener extends me.caleb.Classes.utils.Utils implements Listener{

	private Main plugin;
	LuckPermsApi lpapi = LuckPerms.getApi();
	
	public GeneralListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	//Test this to make sure it properly works
	@EventHandler (priority = EventPriority.LOW)
	public void onTakeDamage(EntityDamageByEntityEvent e) {
		Entity attacked = e.getEntity();
		Entity attacker = (e.getDamager() instanceof Player) ? e.getDamager() : null;

		if(!(attacked instanceof Player)) return;

		if (attacker instanceof Arrow || attacker instanceof Snowball || attacker instanceof Fireball) {
			Projectile en = (Projectile) e.getDamager();
			if(!(en.getShooter() instanceof Player)){
				return;
			}else{
				attacker = (Player) en.getShooter();
			}
		}

		if(attacked instanceof Player && attacker instanceof Player){
			if(ClanConfigManager.inSameClan(attacked.getName(), attacker.getName())) {
				e.setCancelled(true);
			}else {
				return;
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String clan = ClanConfigManager.getPlayerClan(p.getName());
		User user = lpapi.getUser(p.getName());
		String message = e.getMessage();
		String playerPrefix = user.getCachedData().getMetaData(Contexts.allowAll()).getPrefix();
		
		if(playerPrefix != null && clan != null) {
			e.setFormat(Utils.chat("&8<" + clan + "> &r" + playerPrefix + " &7" + p.getName() + " &r" + e.getMessage()));
		}else if(playerPrefix == null && clan != null){
			e.setFormat(Utils.chat("&8<" + clan + ">" + " &7" + p.getName() + " &r" + e.getMessage()));
		}else if(playerPrefix != null && clan == null) {
			e.setFormat(Utils.chat(playerPrefix + " &7" + p.getName() + " &r" + e.getMessage()));
		}else {
			return;
		}		
	}
	
}
