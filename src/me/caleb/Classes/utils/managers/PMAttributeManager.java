package me.caleb.Classes.utils.managers;

import java.util.HashMap;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import me.caleb.Classes.Main;

public class PMAttributeManager extends AttributeManager{
	
	public PMAttributeManager(Main plugin, Player p, String cl) {
		super(plugin, p, cl);
	}

	public void removeMaxHealthAttribute() {
		for(AttributeModifier a : p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(a);
		}
	}
	
	/*
	 * Gets the players attributes and gets the max health provided by their attributes as well as adding the additive (10% of the players max health at the moment)
	 */
	public void applyHolyShield(double additive) {
		double hpAdditive = Double.parseDouble(cma.getValue("Attributes.MaxHealth." + attr.get("MaxHealth").intValue())) + additive;
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("Max_HP", hpAdditive, Operation.ADD_NUMBER));	
	}
	
	public void applyRegularMaxHealth() {
		double hpAdditive = Double.parseDouble(cma.getValue("Attributes.MaxHealth." + attr.get("MaxHealth").intValue()));
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("Max_HP", hpAdditive, Operation.ADD_NUMBER));
	}
	
	
}
