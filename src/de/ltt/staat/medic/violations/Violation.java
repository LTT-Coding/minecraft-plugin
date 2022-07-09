package de.ltt.staat.medic.violations;

import org.bukkit.entity.Player;

public interface Violation {

	public void hurt(boolean hurt, Player p);
	public void message(Player p);
	public String toString();
	public int getIntensity();
	public Position getPosition();
	public boolean hasSpecial();
}
