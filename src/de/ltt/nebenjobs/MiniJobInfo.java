package de.ltt.nebenjobs;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class MiniJobInfo {
	
	private static final int PaperTransportMoney = 1;

	private static HashMap<Player, MiniJobInfo> jobTypes = new HashMap<Player, MiniJobInfo>();
	private Player p;
	private JobType type;
	private int remeaning;
	private int full;
	
	public MiniJobInfo(Player p) {
		
	}
	
	public MiniJobInfo(JobType type, Player p) {
		
	}
	
	public static boolean hasJob(Player p) {
		if(jobTypes.containsKey(p)) return true;
		return false;
	}
	
	/*public static boolean isMiniJobPossible(JobType type) {
		switch(type) {
		case GELDTRANSPORT:
			break;
		case PAPERTRANSPORT:
			break;
		}
	}*/
}
