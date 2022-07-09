package de.ltt.türSysteme;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;

public class IronTrapDoor implements CommandExecutor, Listener{

	public static List<Player> IronDoorPlayer = new ArrayList<Player>();
	@Override
	public boolean onCommand(CommandSender sender,  Command cmd,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(!IronDoorPlayer.contains(p)) {
					IronDoorPlayer.add(p);
					p.sendMessage("§6Klick jetzt auf die IronTrapDoor");					
				} else {
					IronDoorPlayer.remove(p);
					p.sendMessage("§6Du hast den Modus verlassen");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void s(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(IronDoorPlayer.contains(e.getPlayer())) {
				if(e.getClickedBlock().getBlockData() instanceof TrapDoor) {
					Openable irontür = (Openable) e.getClickedBlock().getBlockData();
					irontür.setOpen(true);
					e.getClickedBlock().getLocation().getBlock().setBlockData(irontür);
					e.getClickedBlock().getLocation().getBlock().getState().update();
					IronDoorPlayer.remove(e.getPlayer());
					e.getPlayer().sendMessage("§6Du hast die IronTrapDoor geöffnet");
				} else {
					e.getPlayer().sendMessage("§cDer geklickte Block ist keine IronTrapDoor");
				}
			}
		}
	}

}
