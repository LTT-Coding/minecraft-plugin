package de.ltt.casino.allgemein;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VJeton {
	
	public static final String VILLAGER_Jeton = "§aJeton Händler";

	public VJeton(Location location) {
		Villager shop = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		shop.setCustomName(VJeton.VILLAGER_Jeton);
		shop.setCustomNameVisible(true);
		shop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 500));
	}


}
