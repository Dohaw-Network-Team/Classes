package me.caleb.Classes.listeners;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
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
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.Context;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.plugin.RegisteredServiceProvider;

public class GeneralListener extends me.caleb.Classes.utils.Utils implements Listener{

	private Main plugin;
	RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
	LuckPerms lpapi = provider.getProvider();
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
		User user = lpapi.getUserManager().getUser(p.getUniqueId());
		ContextManager cm = lpapi.getContextManager();
		QueryOptions queryOptions = cm.getQueryOptions(user).orElse(cm.getStaticQueryOptions());
		CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
		String playerPrefix = metaData.getPrefix();
		
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
