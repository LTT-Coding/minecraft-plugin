package de.ltt.server.reflaction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.ltt.other.AFKCommand;
import de.ltt.server.listener.AFKListener;
import de.ltt.server.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Broadcaster {
	
	
	BukkitRunnable vote;
	
	public Broadcaster() {
		
	}
	
	public void startVote() {
		vote = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player current : Bukkit.getOnlinePlayers()) {
					current.sendMessage("§6Wir bitten dich jeden tag für uns zu Voten");
					current.sendMessage("§6Das geht under folgenden Links:");
					TextComponent tc = new TextComponent();
					tc.setText("§e» https://minecraft-server.eu/vote/index/214C3");
					tc.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://minecraft-server.eu/vote/index/214C3"));
					tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Vote").create()));
					tc.setColor(ChatColor.GOLD);
					current.spigot().sendMessage(tc);
					TextComponent tc2  = new TextComponent();
					tc2.setText("§e» https://www.minecraft-serverlist.net/vote/53658");
					tc2.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.minecraft-serverlist.net/vote/53658"));
					tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Vote").create()));
					tc2.setColor(ChatColor.GOLD);
					current.spigot().sendMessage(tc2);
				}
			}
		};
		vote.runTaskTimer(Main.getPlugin(), 0, 72000);
	}
	
	public void stopVote() {
		
	}
	
	public void startAFK() {
		Main.savedLoc.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
        	  Main.savedLoc.put(p, p.getLocation());
        }
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), () -> {
			for(Player current : Main.savedLoc.keySet()) 
				if(PlayerInfo.getChars(current).size() != 0)
				if(!Main.longerAFK.contains(current)) {
					Location saved = Main.savedLoc.get(current);
					Location loc = current.getLocation();
					boolean inactive = false;
					inactive = !AFKListener.notAfk.contains(current);
					if(!AFKListener.notAfk.contains(current))AFKListener.notAfk.add(current);
					if(inactive || AFKCommand.temporaryAFK.contains(current)) {
						if(Main.shortAFK.contains(current)) {
							Main.shortAFK.remove(current);
							Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
								@Override
								public void run() {
									current.teleport(Main.AFKLoc);
								}
							});
							Main.shortAFK.add(current);
							Main.longerAFK.add(current);
						}else if(AFKCommand.temporaryAFK.contains(current)){
							AFKCommand.temporaryAFK.remove(current);
							Main.shortAFK.add(current);
						}else {
							Main.shortAFK.add(current);
							current.sendMessage("§eDu bist nun AFK!");
							for(Player p : Bukkit.getOnlinePlayers()) 
								if(p.getWorld().equals(current.getWorld())) {
									if(p.getLocation().distance(current.getLocation()) < 5 && p != current) 
										p.sendMessage("§6" + current.getName() + "§e ist in Gedanken versunken!");
								}
						}
					}
					Main.savedLoc.put(current, loc);
				}
	    }, (long)(20 * 60 * 1.5), (long)(20 * 60 * 1.5));
	}

}
