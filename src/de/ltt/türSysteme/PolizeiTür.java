package de.ltt.türSysteme;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class PolizeiTür implements CommandExecutor, Listener{

	public static List<Player> PolizeiTür = new ArrayList<Player>();
	public static List<Player> PolizeiTürRemove = new ArrayList<Player>();
	public final int KartenNummer = 10000029;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equals("add")) {
						p.sendMessage("§eKlicke auf die entsprechende Tür");
						PolizeiTür.add(p);
					} else if (args[0].equals("remove")) {
						p.sendMessage("§eKlicke auf die entsprechende Tür");
						PolizeiTürRemove.add(p);
					} else if(args[0].equals("finish")) {
						PolizeiTür.remove(p);
						p.sendMessage("§eAlle Türen wurden registiert");
					}else
						p.sendMessage("§cBitte benutzte /polizeidoor add/remove/finish");
				} else
					p.sendMessage("§cBitte benutzte /polizeidoor add/remove/finish");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void TürRegistrieren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (PolizeiTür.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (!Main.PolizeiTürLoc.contains(OverDoorLoc)) {
					if (!Main.PolizeiTürLoc.contains(e.getClickedBlock().getLocation())) {
						if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
							SQLData.addPolizeiDoor(OverDoorLoc);
							e.setCancelled(true);
							p.sendMessage("§aDu hast die Polizei Tür erfolgreich registriert");
						} else {
							SQLData.addPolizeiDoor(e.getClickedBlock().getLocation());
							e.setCancelled(true);
							p.sendMessage("§aDu hast die Polizei Tür erfolgreich registriert");
						}
					} else
						p.sendMessage("§cDie Tür wurde bereits als Polizei Tür registriert!");
				} else
					p.sendMessage("§cDie Tür wurde bereits als Polizei Tür registriert!");
			}
		} else 	if (PolizeiTürRemove.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				PolizeiTürRemove.remove(p);
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.PolizeiTürLoc.contains(e.getClickedBlock().getLocation())
						|| Main.PolizeiTürLoc.contains(OverDoorLoc)) {
					if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
						e.setCancelled(true);
						p.sendMessage("§aDie Polizei Tür ist nun nicht mehr registriert");
						SQLData.removePolizeiDoor(OverDoorLoc);
					} else {
						e.setCancelled(true);
						p.sendMessage("§aDie Polizei Tür ist nun nicht mehr registriert");
						SQLData.removePolizeiDoor(e.getClickedBlock().getLocation());
					}
				} else
					p.sendMessage("§cDie Tür ist keine Polizei Tür!");
			}
		}
	}																									   

	@EventHandler
	public void PolizeiTürKlick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location door = e.getClickedBlock().getLocation();
			if(Main.PolizeiTürLoc.contains(door)) {
				if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
					FrakTürSystem.checkDoor(door);	
				} else
					e.setCancelled(true);
			} else if(Main.PolizeiTürLoc.contains(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()))) {
				if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
					FrakTürSystem.checkDoor(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()));
				} else
					e.setCancelled(true);
			}
		} else if(Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.PolizeiTürLoc.contains(e.getClickedBlock().getLocation())
						|| Main.PolizeiTürLoc.contains(OverDoorLoc)) {
					if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
						if (Main.PolizeiTürLoc.contains(OverDoorLoc)) {
							SQLData.removePolizeiDoor(OverDoorLoc);
							p.sendMessage("§aDie Polizei Tür ist nun nicht mehr registriert!");
						} else {
							SQLData.removePolizeiDoor(e.getClickedBlock().getLocation());
							p.sendMessage("§aDie Polizei Tür ist nun nicht mehr registriert!");
						}
					}
				}
			}
		}
	}

}
