package de.ltt.türSysteme;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
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
public class MedicTür implements CommandExecutor, Listener {

	public static List<Player> MedicTür = new ArrayList<Player>();
	public static List<Player> MedicTürRemove = new ArrayList<Player>();
	public final int KartenNummer = 10000002;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equals("add")) {
						p.sendMessage("§eKlicke auf die entsprechende Tür");
						MedicTür.add(p);
					} else if (args[0].equals("remove")) {
						p.sendMessage("§eKlicke auf die entsprechende Tür");
						MedicTürRemove.add(p);
					}else if(args[0].equals("finish")) {
						MedicTür.remove(p);
						p.sendMessage("§eAlle Türen wurden registiert");
					} else
						p.sendMessage("§cBitte benutzte /medicdoor add/remove/finish");
				} else
					p.sendMessage("§cBitte benutzte /medicdoor add/remove/finish");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void TürRegistrieren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (MedicTür.contains(p)) {
			try {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (!Main.MedicTürLoc.contains(OverDoorLoc)) {
					if (!Main.MedicTürLoc.contains(e.getClickedBlock().getLocation())) {
						if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
							e.setCancelled(true);
							p.sendMessage("§aDu hast die Medic Tür erfolgreich registriert");
							SQLData.addMedicDoor(OverDoorLoc);
						} else {
							e.setCancelled(true);
							p.sendMessage("§aDu hast die Medic Tür erfolgreich registriert");
							SQLData.addMedicDoor(e.getClickedBlock().getLocation());
						}
					} else
						p.sendMessage("§cDie Tür wurde bereits als Medic Tür registriert!");
				} else
					p.sendMessage("§cDie Tür wurde bereits als Medic Tür registriert!");
			}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else 	if (MedicTürRemove.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				MedicTürRemove.remove(p);
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.MedicTürLoc.contains(e.getClickedBlock().getLocation()) || Main.MedicTürLoc.contains(OverDoorLoc)) {
					if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
						e.setCancelled(true);
						p.sendMessage("§aDie Medic Tür ist nun nicht mehr registriert");
						SQLData.removeMedicDoor(OverDoorLoc);
					} else {
						e.setCancelled(true);
						p.sendMessage("§aDie Medic Tür ist nun nicht mehr registriert");
						SQLData.removeMedicDoor(e.getClickedBlock().getLocation());
					}
				} else
					p.sendMessage("§cDie Tür ist keine Medic Tür!");
			}
		}
	}																									   

	@EventHandler
	public void MedicTürKlick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location door = e.getClickedBlock().getLocation();
					if(Main.MedicTürLoc.contains(door)) {
						if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
							FrakTürSystem.checkDoor(door);	
						} else
							e.setCancelled(true);
					} else if(Main.MedicTürLoc.contains(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()))) {
						if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
							FrakTürSystem.checkDoor(door);	
						} else
							e.setCancelled(true);
					}
		} else if(Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.MedicTürLoc.contains(e.getClickedBlock().getLocation()) || Main.MedicTürLoc.contains(OverDoorLoc)) {
					if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")|| e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
						if (Main.MedicTürLoc.contains(OverDoorLoc)) {
							SQLData.removeMedicDoor(OverDoorLoc);
							p.sendMessage("§aDie Medic Tür ist nun nicht mehr registriert!");
						} else {
							SQLData.removeMedicDoor(e.getClickedBlock().getLocation());
							p.sendMessage("§aDie Medic Tür ist nun nicht mehr registriert!");
						}
					}
				}
			}
		}
	}
}
