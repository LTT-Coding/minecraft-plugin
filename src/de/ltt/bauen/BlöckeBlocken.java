package de.ltt.bauen;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class BlöckeBlocken implements CommandExecutor, Listener{

	public static List<Player> BlockSperre = new ArrayList<Player>();
	public static List<Player> BlockSperreRemove = new ArrayList<Player>();
	
	@Override
	public boolean onCommand(CommandSender sender,  Command cmd,  String label, String[] args){
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equals("add")) {
						p.sendMessage("§eKlicke auf die entsprechenden Blöcke");
						BlockSperre.add(p);
					} else if (args[0].equals("remove")) {
						p.sendMessage("§eKlicke auf die entsprechenden Blöcke");
						BlockSperreRemove.add(p);
					}else if(args[0].equals("finish")) {
						if(BlockSperre.contains(p)) {
							BlockSperre.remove(p);
							p.sendMessage("§eAlle Blöcke wurden gesperrt");
						} else if(BlockSperreRemove.contains(p)) {
							BlockSperreRemove.remove(p);
							p.sendMessage("§eAlle Blöcke wurden entsperrt");
						}
					} else
						p.sendMessage("§cBitte benutzte /blocksperre add/remove/finish");
				} else
					p.sendMessage("§cBitte benutzte /blocksperre add/remove/finish");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void BlockSperren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (BlockSperre.contains(p)) {
			try {
					if (!Main.BlockSperreLoc.contains(e.getClickedBlock().getLocation())) {
							SQLData.addBlockSperre(e.getClickedBlock().getLocation());
							e.setCancelled(true);
							p.sendMessage("§aDu hast den Block erfolgreich gesperrt");
				} else
					p.sendMessage("§cDer Block wurde bereits gesperrt!");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void BlockSperrenRemove(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (BlockSperreRemove.contains(p)) {
			try {
				if (Main.BlockSperreLoc.contains(e.getClickedBlock().getLocation())) {
							SQLData.removeBlockSperre(e.getClickedBlock().getLocation());
							e.setCancelled(true);
							p.sendMessage("§aDu hast den Block erfolgreich entsperrt");
				} else
					p.sendMessage("§cDer Block wurde bereits entsperrt!");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
