package de.ltt.server.reflaction;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.ltt.server.main.Main;

public class ChatInfo {

	
	private static HashMap<Player, ChatType> chats = new HashMap<>();
	private static HashMap<Player, BukkitRunnable> removeTask = new HashMap<>();
	
	public static void addChat(Player p, ChatType type) {
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				chats.remove(p);
				if(removeTask.containsKey(p)) {
					removeTask.get(p).cancel();
					removeTask.remove(p);
				}
				chats.put(p, type);
			}
		}, 1);
	}
	
	public static void addChat(Player p, ChatType type, long wait) {
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				chats.remove(p);
				if(removeTask.containsKey(p)) {
					removeTask.get(p).cancel();
					removeTask.remove(p);
				}
				chats.put(p, type);
				removeTask.put(p, new BukkitRunnable() {
					@Override
					public void run() {
						chats.remove(p);
						
						if(removeTask.containsKey(p)) {
							removeTask.get(p).cancel();
							removeTask.remove(p);
							p.sendMessage("§4Deine die Zeit ist abgelaufen!");
							p.sendMessage("§cVorgang wurde abgebrochen!");
						}
					}
				});
				removeTask.get(p).runTaskLater(Main.getPlugin(), wait);				
			}
		}, 1);
	}
	
	public static void removeChat(Player p) {
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				chats.remove(p);
				if(removeTask.containsKey(p)) {
					removeTask.get(p).cancel();
					removeTask.remove(p);
				}
				
			}
		}, 1);
		
	}
	
	public static boolean isChat(Player p) {
		if(chats.containsKey(p)) {
			return true;
		}
		return false;
	}
	
	public static ChatType getChat(Player p) {
		if(chats.containsKey(p)) {
			return chats.get(p);
		}
		return ChatType.NULL;
	}
	
	
}
