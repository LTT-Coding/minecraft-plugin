package de.ltt.other.Teleport;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class TP implements CommandExecutor{

	HashMap<Player, Location> undoTP = new HashMap<Player, Location>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length <= 4) {
					if(args[0].equalsIgnoreCase("undo")) {
						p.teleport(undoTP.get(p));
						p.sendMessage("§eDu wurdest zu deine letzte Location zurück Teleportiert");
					} else 
					if(args.length == 4) {
						try {
							Player t = Bukkit.getPlayer(args[0]);
							undoTP.put(t, t.getLocation());
							Location loc = new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
							t.teleport(loc);
							p.sendMessage("§eDu hast " + t.getName() + " zu X: " + args[1] + " Y: " + args[2] + " Z: "+ args[3] +" Teleportiert");
							t.sendMessage("§eDu wurdest Teleportiert");
						} catch (Exception e) {
							p.sendMessage("§cBitte benutze /tp <name> <X> <Y> <Z>");
						}
					} else if(args[1].equalsIgnoreCase("@e")) {
						try {
							Player t = Bukkit.getPlayer(args[0]);
							for (Entity p2 : Bukkit.getWorld(p.getWorld().getName()).getEntities()) {
								p2.teleport(t);
							}
							t.sendMessage("§eAlle Entitys wurden zu dir Teleportiert");
						} catch (Exception e) {
							p.sendMessage("§cBitte benutze /tp <name> @e");
						}
					} else if(args[1].equalsIgnoreCase("@r")) {
						try {
							Player t = Bukkit.getPlayer(args[0]);
							Player t2 = (Player) Bukkit.getOnlinePlayers().toArray()[
							                                                new java.util.Random().nextInt(Bukkit.getOnlinePlayers().size())];
							undoTP.put(t, t.getLocation());
							t2.teleport(t);
						 t.sendMessage("§eDu wurdest Teleportiert");
						 t2.sendMessage("§eDu wurdest Teleportiert");
						} catch (Exception e) {
							p.sendMessage("§cBitte benutze /tp <name> @r");

						}
					} else if(args.length == 2) {
						if(args[1].equalsIgnoreCase("@p")) {
							try {
								Player t = Bukkit.getPlayer(args[0]);
								undoTP.put(t, t.getLocation());
								for (int i = 0; i < 1000000; i++) {
									if (Bukkit.getOnlinePlayers().isEmpty()) {
										p.sendMessage("§eKein Spieler sind Online");
										return false;
									}
									for (Player p2 : Bukkit.getOnlinePlayers()) {
										if(p.getLocation().distance(p2.getLocation()) <= i) {
											p2.teleport(t);
											t.sendMessage("§eDu wurdest Teleportiert");
											p2.sendMessage("§eDu wurdest Teleportiert");
										}
									}
								}
							} catch (Exception e) {
								p.sendMessage("§cBitte benutze /tp <name> @p");
							}
						} else if(args[1].equalsIgnoreCase("@a")) {
							try {
								Player t = Bukkit.getPlayer(args[0]);
								for (Player p2 : Bukkit.getOnlinePlayers()) {
									undoTP.put(p2, p2.getLocation());
									p2.teleport(t);
									p2.sendMessage("§eDu wurdest Teleportiert");
								}
								t.sendMessage("§eAlle Spieler wurden zu dir Teleportiert");
							} catch (Exception e) {
								p.sendMessage("§cBitte benutze /tp <name> @a");
							}
						} else if(args[1] != null) {
							try {
								Player t = Bukkit.getPlayer(args[0]);
								Player t2 = Bukkit.getPlayer(args[1]);
								undoTP.put(t, t.getLocation());
								t.teleport(t2);
								if(p.getName().equals(args[0])) {
									p.sendMessage("§eDu hast dich zu " + t2.getName() +" Teleportiert");
									t2.sendMessage("§eDu wurdest Teleportiert");
								} else if(p.getName().equals(args[1])) {
									p.sendMessage("§eDu hast dich zu " + t.getName() +" Teleportiert");
									t.sendMessage("§eDu wurdest Teleportiert");
								} else {
									p.sendMessage("§eDu hast " + t.getName() + " zu " + t2.getName() + " Teleportiert");
									t.sendMessage("§eDu wurdest Teleportiert");
									t2.sendMessage("§eDu wurdest Teleportiert");
								}
							} catch (Exception e) {
								p.sendMessage("§cBitte benutze /tp <name> <name>");
							}
						} else 
							p.sendMessage("§cBitte benutze /tp");
					} else 
						 p.sendMessage("§cBitte benutze /tp");
					
				}  else 
					p.sendMessage("§cBitte benutze /tp");
			}
		}
		return false;
	}

}
