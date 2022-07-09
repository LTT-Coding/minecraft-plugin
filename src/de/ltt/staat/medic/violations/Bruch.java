package de.ltt.staat.medic.violations;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bruch implements Violation{

	private Position pos;
	private boolean hasSplinter;
	
	public Bruch(Position pos, boolean hasSplinter) {
		this.pos = pos;
		this.hasSplinter = hasSplinter;
	}
	
	@Override
	public void hurt(boolean hurt, Player p) {
		if(hurt) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4, false, true));
			message(p);
		}else {
			p.removePotionEffect(PotionEffectType.SLOW);
		}
	}

	@Override
	public void message(Player p) {
		p.sendMessage("§eDein " + pos.getTranslation() + " tut sehr weh!");
	}

	@Override
	public int getIntensity() {
		return 0;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public boolean hasSpecial() {
		return hasSplinter;
	}
	
	public String toString() {
		String name;
		name = "Bruch;" + pos.toString() + ";" +  hasSplinter;
		return name;
	}
	
	public static Bruch fromString(String string) {
		if(!string.startsWith("Bruch;"))return null;
		string = string.replace("Bruch;", "");
		char[] charset = string.toCharArray();
		String pos = "";
		String hasSplinter = "";
		int current = 0;
		String loop = "";
		for(int i = 0; i < charset.length && charset[i] != '\0'; i++) {
			if (charset[i] == ';') {
				switch(current) {
				case 0:
					pos = loop;
					break;
				case 1:
					hasSplinter = loop;
					break;
				}
				current++;
				loop = "";
			} else {
				loop += charset[i];
			}
				
		}
		Position posF = Position.valueOf(pos);
		boolean hasSplinterF = Boolean.valueOf(hasSplinter);
		return new Bruch(posF, hasSplinterF);
	}



}
