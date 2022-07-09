package de.ltt.server.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.PlayerInfo;

public class Chat implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent e) {
		if(!ChatInfo.isChat(e.getPlayer())) {
			if (Main.frChat.containsKey(e.getPlayer())) {
				Player target = Main.frChat.get(e.getPlayer());
				target.sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
				e.getPlayer().sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
				e.setCancelled(true);
			} else if (Main.frChat2.containsKey(e.getPlayer())) {
				Player target = Main.frChat2.get(e.getPlayer());
				target.sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
				e.getPlayer().sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
				e.setCancelled(true);
			} else if (Main.gc.contains(e.getPlayer().getUniqueId())) {
				Bukkit.broadcastMessage("§7[§5ADMIN§7] §5" + e.getPlayer().getName() + ": §3" + e.getMessage());
				e.setCancelled(true);
			} else {
				for (String current : Main.blockedURL) {
					if (e.getMessage().toLowerCase().contains(current.toLowerCase())) {
						e.setCancelled(true);
						for (String admin : Main.Admin) {
							try {
								OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(admin));
								if (p.isOnline())p.getPlayer().sendMessage(Main.URL_PREFIX + "§6" + e.getPlayer().getName() + "§e: " + e.getMessage());
							} catch (Exception e2) {	
							}
						}
						e.getPlayer().sendMessage("§cLinks oder andere URLs sind im RolePlay-Chat nicht erlaubt!");
						return;
					}
				}
				
				if (Main.AnrufChat.containsKey(e.getPlayer())) {
					Player target = Main.AnrufChat.get(e.getPlayer());
					target.sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
					e.getPlayer().sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
					for (Player current : Bukkit.getOnlinePlayers()) {
						if (!(current.getName().equals(e.getPlayer().getName()))) {
							if (e.getPlayer().getLocation().distance(current.getLocation()) >= 0
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 10) {
								current.sendMessage(e.getPlayer().getName() + ": " + e.getMessage());
							} else if (e.getPlayer().getLocation().distance(current.getLocation()) > 10
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 15) {
								current.sendMessage("§7" + e.getPlayer().getName() + ": " + e.getMessage());
							} else if (e.getPlayer().getLocation().distance(current.getLocation()) > 15
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 20) {
								current.sendMessage("§8" + e.getPlayer().getName() + ": " + e.getMessage());
							}
						}
						e.setCancelled(true);
					}
					e.setCancelled(true);
				} else if (Main.AnrufeChat2.containsKey(e.getPlayer())) {
					Player target = Main.AnrufeChat2.get(e.getPlayer());
					target.sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
					e.getPlayer().sendMessage("§1" + e.getPlayer().getName() + ": §r§9" + e.getMessage());
					for (Player current : Bukkit.getOnlinePlayers()) {
						if (!(current.getName().equals(e.getPlayer().getName()))) {
							if (e.getPlayer().getLocation().distance(current.getLocation()) >= 0
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 10) {
								current.sendMessage(e.getPlayer().getName() + ": " + e.getMessage());
							} else if (e.getPlayer().getLocation().distance(current.getLocation()) > 10
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 15) {
								current.sendMessage("§7" + e.getPlayer().getName() + ": " + e.getMessage());
							} else if (e.getPlayer().getLocation().distance(current.getLocation()) > 15
									&& e.getPlayer().getLocation().distance(current.getLocation()) <= 20) {
								current.sendMessage("§8" + e.getPlayer().getName() + ": " + e.getMessage());
							}
						}
						e.setCancelled(true);
					}
					e.setCancelled(true);
				}else {
					Main.ChatMessage(e.getPlayer().getLocation(), 0, new PlayerInfo(e.getPlayer()).getFullName() + ": " + e.getMessage());
					e.setCancelled(true);
				}
			}
		}else {
			e.setCancelled(true);
		}
	}
}
