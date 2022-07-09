package de.ltt.t�rSysteme;

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

public class PolizeiT�r implements CommandExecutor, Listener{

	public static List<Player> PolizeiT�r = new ArrayList<Player>();
	public static List<Player> PolizeiT�rRemove = new ArrayList<Player>();
	public final int KartenNummer = 10000029;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length == 1) {
					if (args[0].equals("add")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						PolizeiT�r.add(p);
					} else if (args[0].equals("remove")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						PolizeiT�rRemove.add(p);
					} else if(args[0].equals("finish")) {
						PolizeiT�r.remove(p);
						p.sendMessage("�eAlle T�ren wurden registiert");
					}else
						p.sendMessage("�cBitte benutzte /polizeidoor add/remove/finish");
				} else
					p.sendMessage("�cBitte benutzte /polizeidoor add/remove/finish");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void T�rRegistrieren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (PolizeiT�r.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (!Main.PolizeiT�rLoc.contains(OverDoorLoc)) {
					if (!Main.PolizeiT�rLoc.contains(e.getClickedBlock().getLocation())) {
						if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
							SQLData.addPolizeiDoor(OverDoorLoc);
							e.setCancelled(true);
							p.sendMessage("�aDu hast die Polizei T�r erfolgreich registriert");
						} else {
							SQLData.addPolizeiDoor(e.getClickedBlock().getLocation());
							e.setCancelled(true);
							p.sendMessage("�aDu hast die Polizei T�r erfolgreich registriert");
						}
					} else
						p.sendMessage("�cDie T�r wurde bereits als Polizei T�r registriert!");
				} else
					p.sendMessage("�cDie T�r wurde bereits als Polizei T�r registriert!");
			}
		} else 	if (PolizeiT�rRemove.contains(p)) {
			if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
				PolizeiT�rRemove.remove(p);
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.PolizeiT�rLoc.contains(e.getClickedBlock().getLocation())
						|| Main.PolizeiT�rLoc.contains(OverDoorLoc)) {
					if (OverDoorLoc.getBlock().getType().toString().toLowerCase().contains("_door")) {
						e.setCancelled(true);
						p.sendMessage("�aDie Polizei T�r ist nun nicht mehr registriert");
						SQLData.removePolizeiDoor(OverDoorLoc);
					} else {
						e.setCancelled(true);
						p.sendMessage("�aDie Polizei T�r ist nun nicht mehr registriert");
						SQLData.removePolizeiDoor(e.getClickedBlock().getLocation());
					}
				} else
					p.sendMessage("�cDie T�r ist keine Polizei T�r!");
			}
		}
	}																									   

	@EventHandler
	public void PolizeiT�rKlick(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location door = e.getClickedBlock().getLocation();
			if(Main.PolizeiT�rLoc.contains(door)) {
				if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
					FrakT�rSystem.checkDoor(door);	
				} else
					e.setCancelled(true);
			} else if(Main.PolizeiT�rLoc.contains(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()))) {
				if(p.getItemInHand().getType() == Material.HEART_OF_THE_SEA && p.getItemInHand().getItemMeta().getCustomModelData() == KartenNummer) {
					FrakT�rSystem.checkDoor(new Location(door.getWorld(), door.getX(), door.getY() + 1, door.getZ()));
				} else
					e.setCancelled(true);
			}
		} else if(Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				Location OverDoorLoc = e.getClickedBlock().getLocation();
				OverDoorLoc.setY(OverDoorLoc.getY() + 1);
				if (Main.PolizeiT�rLoc.contains(e.getClickedBlock().getLocation())
						|| Main.PolizeiT�rLoc.contains(OverDoorLoc)) {
					if (e.getClickedBlock().getType().toString().toLowerCase().contains("_door")) {
						if (Main.PolizeiT�rLoc.contains(OverDoorLoc)) {
							SQLData.removePolizeiDoor(OverDoorLoc);
							p.sendMessage("�aDie Polizei T�r ist nun nicht mehr registriert!");
						} else {
							SQLData.removePolizeiDoor(e.getClickedBlock().getLocation());
							p.sendMessage("�aDie Polizei T�r ist nun nicht mehr registriert!");
						}
					}
				}
			}
		}
	}

}
