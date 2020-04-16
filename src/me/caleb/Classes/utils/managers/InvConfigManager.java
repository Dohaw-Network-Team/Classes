package me.caleb.Classes.utils.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.caleb.Classes.Main;

public class InvConfigManager extends ConfigManager{

	public InvConfigManager(Main plugin) {
		super(plugin, "playerInv.yml");
	}
	
	public void storeArmor(Player p, PlayerInventory inv) {
		
		ItemStack helm = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack leggings = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		
		ArrayList<String> armor = new ArrayList<String>();
		armor.add(helm.getType().name());
		armor.add(chest.getType().name());
		armor.add(leggings.getType().name());
		armor.add(boots.getType().name());
		
		config.set("Players." + p.getUniqueId() + ".Armor", armor);	
		
		try {
			getCustomConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ItemStack> getArmor(Player p) {
		
		ArrayList<ItemStack> playerArmor = new ArrayList<ItemStack>();
		List<String> stringArmor = getList("Players." + p.getUniqueId() + ".Armor");
		
		for(String armorPiece : stringArmor) {
			playerArmor.add(new ItemStack(Material.matchMaterial(armorPiece),1));
		}
		
		config.set("Players." + p.getUniqueId() + ".Armor", null);
		config.set("Players." + p.getUniqueId(), null);
		
		try {
			getCustomConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return playerArmor;
		
	}

}
