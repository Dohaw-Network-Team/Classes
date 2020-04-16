package me.caleb.Classes.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.caleb.Classes.scoreboards.ScoreboardManager;
import net.md_5.bungee.api.ChatColor;

public class ItemParser extends Utils{

	private static String line;
	private static String cl;
	
	public ItemParser(String line, String cl) {
		ItemParser.line = line;
		ItemParser.cl = cl;
	}
	
	public String getName() {
		String itemName = "";
		String[] arrLine = line.split(" ");
		itemName = arrLine[1];
		itemName = itemName.substring(0,itemName.length() - 1).toUpperCase();
		return itemName;
	}
	
	public Map<Enchantment, Integer> getEnchants() {
		
		Map<Enchantment,Integer> enchants = new HashMap();
		String[] arrLine = line.split(" ");
		ArrayList<String> arrEnchants = new ArrayList<String>();
		
		//If the item has no enchants
		if(!hasEnchants()) return enchants;
		
		//Gets all the Enchants into an array
		for(int i = 5; i < arrLine.length;i++) {
			if(arrLine[i].trim().equalsIgnoreCase("customName:")) {
				break;
			}else {
				arrEnchants.add(arrLine[i]);	
			}	
		}
		
		for(int x = 0; x < arrEnchants.size();x+=2) {
			//The substring separates the colon from the number
			//For example: 5; -> 5
			Enchantment e = Enchantment.getByName(arrEnchants.get(x).toUpperCase());
			//Only one enchant
			if(arrEnchants.size() == 2 && !arrEnchants.get(1).substring(1,2).equalsIgnoreCase(";")) {
				enchants.put(e, Integer.parseInt(arrEnchants.get(x+1)));
			}else {
				enchants.put(e, Integer.parseInt(arrEnchants.get(x+1).substring(0,1)));
			}
			
		}
		
		return enchants;
	}
	
	public String getCustomName() {
		
		if(!line.contains("customName")) return null;
		
		int enchantsSize = getEnchants().size();
		String[] arrLine = line.split(";");
		String customNameLine = null;
		String temp[] = null;
		String customName = null;
		
		try {
			customNameLine = arrLine[2 + enchantsSize];
			temp = customNameLine.split(":");
			customName = temp[1].trim();
		}catch(Exception e) {}
		
		return customName;
		
	}
	
	public String getLore() {
		
		if(!line.contains("lore")) return null;
		
		String lore;
		String[] arrLine = line.split(";");
		
		String temp = arrLine[arrLine.length-1];
		lore = temp.substring(6).trim();

		return lore;
	}
	
	public Material getMat(String name) {
		return Material.getMaterial(name);
	}
	
	public boolean hasEnchants() {
		
		if(line.contains("enchants:")) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public int getAmount() {
		String[] arrLine = line.split(" ");
		if(arrLine[3].contains(";")) {
			return Integer.parseInt(arrLine[3].substring(0,1));
		}
		return Integer.parseInt(arrLine[3]);
	}
	
	public ItemStack getItem() {
		
		ItemStack item = null;
		
		String itemName = getName();
		String customName = getCustomName();
		int amount = getAmount();
		
		Map<Enchantment, Integer> enchants = getEnchants();
		
		Material mat = getMat(itemName);
		item = new ItemStack(mat, amount);
		ItemMeta im = item.getItemMeta();
		String lore = getLore();
		
		if(!enchants.isEmpty()) {
			for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
				im.addEnchant(entry.getKey(), entry.getValue(), false);
			}
		}
		
		if(customName != null) {
			ChatColor color = ScoreboardManager.getClassColor(cl);
			im.setDisplayName(Utils.chat(color + "&l" + customName));	
		}
		
		if(lore != null) {
			List<String> loreList = new ArrayList<String>();
			
			if(getLore().length() > 20) {
				String[] temp = getLore().split(" ");
				String tempSentence = "";
				int length = temp.length;
				for(int x = 0; x < length; x++) {
					if((length / 2) == x) {
						tempSentence += temp[x] + " ";
						loreList.add(tempSentence);
						tempSentence = "";
					}else if(x == (length-1)) {
						tempSentence += temp[x] + " ";
						loreList.add(tempSentence);
					}else {
						tempSentence += temp[x] + " ";
					}
				
				}
			}else {
				loreList.add(lore);
			}
			
			im.setLore(loreList);
		}
		
		item.setItemMeta(im);
		return item;
	}
	
}
