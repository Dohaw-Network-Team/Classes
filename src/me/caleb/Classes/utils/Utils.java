package me.caleb.Classes.utils;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils{

	public static String firstUppercaseRestLowercase(String s) {
		return s.substring(0,1) + s.substring(1);
	}
	
	public static String valueOf(int x) {
		return String.valueOf(x);
	}
	
	public static String valueOf(double x) {
		return String.valueOf(x);
	}
	
	public static String valueOf(boolean x) {
		return String.valueOf(x);
	}

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static double roundToPoint5(double x) {
		return Math.round(x * 2) / 2.0;
	}
	
	public static double roundToPoint1(double x) {
		DecimalFormat df = new DecimalFormat("0.#");
		return Double.parseDouble(df.format(x));
	}
	
	public static void bsm(String s) {
		Bukkit.broadcastMessage(s);
	}
	
	public static void sendPlayerMessage(String s, boolean wantPrefix, Player p, String prefix) {
		if(wantPrefix) {
			p.sendMessage(chat(prefix + " &r" + s));
		}else {
			p.sendMessage(chat("&r" + s));
		}
	}
	
	public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
	
	public static ChatColor getClassColor(String cl) {
		
		switch(cl) {
		
			case "Archer":
				return ChatColor.GREEN;
			case "Ice_wizard":
				return ChatColor.AQUA;
			case "Brute":
				return ChatColor.RED;
			case "Peacemaker":
				return ChatColor.YELLOW;
			case "Thief":
				return ChatColor.GOLD;
		
		}
		
		return null;
	}
 

	
}
