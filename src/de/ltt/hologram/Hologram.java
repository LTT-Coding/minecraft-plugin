package de.ltt.hologram;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;

public class Hologram implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender,  Command command,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("all")) {
						for (Entity entity : Bukkit.getWorld(p.getWorld().getName()).getEntities()) {
							if(entity.isCustomNameVisible()) {
								if(entity instanceof ArmorStand) {
									SQLData.removeAllHolo();
									entity.remove();
					
							
								}
							}
						}
					}else if(args[0].equalsIgnoreCase("list")) {
						p.sendMessage("§6============Hologram============");
						for (int i = 0; i < Main.holo.size(); i++) {
							p.sendMessage("§6" + i + ". §r" + Bukkit.getEntity(UUID.fromString(Main.holo.get(i))).getCustomName());
						}
						p.sendMessage("§6================================");
					} else 
						p.sendMessage("§cBitte benutz /holo <add/remove> <Name> || /holo <all/list/removeR> || /holo <tp> <Nummer>!");
					
				} else if(args.length >= 2) {
					if(args[0].equalsIgnoreCase("add")) {
						String ArmorStandName = "";
						for (int i = 1; i < args.length; i++) {
							ArmorStandName = String.valueOf(ArmorStandName) + args[i] + "";
						}
						Location pLoc = p.getLocation();
						pLoc.setY(pLoc.getY() - 1);
						ArmorStand ArmorS = (ArmorStand) p.getWorld().spawn(pLoc, ArmorStand.class);
						ArmorS.setCustomName(ChatColor.translateAlternateColorCodes('&', ArmorStandName));
						ArmorS.setCustomNameVisible(true);
						ArmorS.setGravity(false);
						ArmorS.setVisible(false);
						p.sendMessage("§aDas Hologram wurde erfolgreich erstellt");
						SQLData.addHolo(ArmorS.getUniqueId().toString());
						
					} else if(args[0].equalsIgnoreCase("remove")) {
						try {
							for (Entity entity : Bukkit.getWorld(p.getWorld().getName()).getEntities()) {
								if (entity.isCustomNameVisible()) {
									if(entity instanceof ArmorStand) {
										int Zahl = Integer.parseInt(args[1]);
										if(!(Main.holo.size() <= Zahl)) {
									if (entity.getUniqueId().toString().equalsIgnoreCase(Main.holo.get(Zahl))) {
										SQLData.removeHolo(entity.getUniqueId().toString());
										entity.remove();
										break;
										
									}
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					} else if(args[0].equalsIgnoreCase("tp")) {
						String ArmorStandName = "";
						for (int i = 1; i < args.length; i++) {
							ArmorStandName = String.valueOf(ArmorStandName) + args[i] + "";
						}
						for (Entity entity : Bukkit.getWorld(p.getWorld().getName()).getEntities()) {
							if (entity.isCustomNameVisible()) {
								if(entity instanceof ArmorStand) {
									int Zahl = Integer.parseInt(args[1]);
								 if(!(Main.holo.size() <= Zahl)) {
									 if (entity.getUniqueId().equals(UUID.fromString(Main.holo.get(Zahl)))) {
											 	Location En = entity.getLocation();
											 	En.setY(En.getY() + 0.5);
												p.teleport(En);
												p.sendMessage("§6Du wurdest zum Hologram Teleportiert");
										}
								 } else
									 p.sendMessage("§cDie Zahl gibt es nicht!");
							}
							}
						}
					}else if(args[0].equalsIgnoreCase("removeR")) {
						try {
							int count = 0;
							for (Entity entity : Bukkit.getWorld(p.getWorld().getName()).getEntities()) {
								if (entity instanceof ArmorStand) {
									if (entity.getLocation().distance(p.getLocation()) <= Integer.parseInt(args[1])) {
										entity.remove();
										count++;
									}
								}
							}
							p.sendMessage("§eEs wurdem alle Rüstungsständer im Radius von §6" + args[1] + "m§e gelöscht");
							p.sendMessage("§6" + count + "§e Objekte wurden beseitigt!");
						} catch (Exception e) {
							p.sendMessage("§cDer angegebene Radius ist keine Zahl!");
						}
					}else
						p.sendMessage("§cBitte benutz /holo <add/remove> <Name> || /holo <all/list/removeR> || /holo <tp> <Nummer>!");
				} else
					p.sendMessage("§cBitte benutz /holo <add/remove> <Name> || /holo <all/list/removeR> || /holo <tp> <Nummer>!");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
