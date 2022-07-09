package de.ltt.t�rSysteme;

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
public class MedicT�r implements CommandExecutor, Listener {

	public static List<Player> MedicT�r = new ArrayList<Player>();
	public static List<Player> MedicT�rRemove = new ArrayList<Player>();
	public final int KartenNummer = 10000002;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equals("add")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						MedicT�r.add(p);
					} else if (args[0].equals("remove")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						MedicT�rRemove.add(p);
					}else if(args[0].equals("finish")) {
						MedicT�r.remove(p);
						p.sendMessage("�eAlle T�ren wurden registiert");
					} else
						p.sendMessage("�cBitte benutzte /medicdoor add/remove/finish");
				} else
					p.sendMessage("�cBitte benutzte /medicdoor add/remove/finish");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void T�rRegistrieren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (MedicT�r.contains(p)) {
			try {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (!Main.MedicT�rLoc.contains(OverDoorLoc)) {
					if (!Main.MedicT�rLoc.contains(e.getClickedBlock().getLocation())) {
						if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
							e.setCancelled(true);
							p.sendMessage("�aDu hast die Medic T�r erfolgreich registriert");
							SQLData.addMedicDoor(OverDoorLoc);
						} else {
							e.setCancelled(true);
							p.sendMessage("�aDu hast die Medic T�r erfolgreich registriert");
							SQLData.addMedicDoor(e.getClickedBlock().getLocation());
						}
					} else
						p.sendMessage("�cDie T�r wurde bereits als Medic T�r registriert!");
				} else
					p.sendMessage("�cDie T�r wurde bereits als Medic T�r registriert!");
			}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else 	if (MedicT�rRemove.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				MedicT�rRemove.remove(p);
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.MedicT�rLoc.contains(e.getClickedBlock().getLocation()) || Main.MedicT�rLoc.contains(OverDoorLoc)) {
					if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
						e.setCancelled(true);
						p.sendMessage("�aDie Medic T�r ist nun nicht mehr registriert");
						SQLData.removeMedicDoor(OverDoorLoc);
					} else {
						e.setCancelled(true);
						p.sendMessage("�aDie Medic T�r ist nun nicht mehr registriert");
						SQLData.removeMedicDoor(e.getClickedBlock().getLocation());
					}
				} else
					p.sendMessage("�cDie T�r ist keine Medic T�r!");
			}
		}
	}																									   

	@EventHandler
	public void MedicT�rKlick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location door = e.getClickedBlock().getLocation();
					if(Main.MedicT�rLoc.contains(door)) {
						if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
							FrakT�rSystem.checkDoor(door);	
						} else
							e.setCancelled(true);
					} else if(Main.MedicT�rLoc.contains(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()))) {
						if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
							FrakT�rSystem.checkDoor(door);	
						} else
							e.setCancelled(true);
					}
		} else if(Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.MedicT�rLoc.contains(e.getClickedBlock().getLocation()) || Main.MedicT�rLoc.contains(OverDoorLoc)) {
					if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")|| e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
						if (Main.MedicT�rLoc.contains(OverDoorLoc)) {
							SQLData.removeMedicDoor(OverDoorLoc);
							p.sendMessage("�aDie Medic T�r ist nun nicht mehr registriert!");
						} else {
							SQLData.removeMedicDoor(e.getClickedBlock().getLocation());
							p.sendMessage("�aDie Medic T�r ist nun nicht mehr registriert!");
						}
					}
				}
			}
		}
	}
}
