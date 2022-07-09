package de.ltt.server.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;

import de.ltt.server.main.Main;

public class CommandListener implements Listener{
	
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().contains("/pl") || e.getMessage().contains("/plugin") || e.getMessage().contains("/plugins")) {
			if(!Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Main.COMMAND_SPERRE);
				}
		}else if(e.getMessage().contains("/kill @e")) {
			if(!e.getMessage().equals("/kill @e[type=item]")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Main.COMMAND_SPERRE);
			}else if(!Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(Main.COMMAND_SPERRE);
				}
		} else if(Main.GesperrteCMD.size() != 0) {
			for (int i = 0; i < Main.GesperrteCMD.size(); i++) {
				if(e.getMessage().startsWith(Main.GesperrteCMD.get(i).toLowerCase())) {
					if(Main.Admin.contains(e.getPlayer().getUniqueId().toString())) {
						e.setCancelled(false);
						e.getPlayer().sendMessage("§cDieser Command ist für Normale Spieler gesperrt!");
						break;
					} else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(Main.COMMAND_SPERRE);
						break;
					}
				}
			}
		}
	}
	
}
