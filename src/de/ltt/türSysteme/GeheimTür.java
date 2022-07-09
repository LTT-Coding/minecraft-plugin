package de.ltt.t�rSysteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class GeheimT�r implements CommandExecutor, Listener{

	public static List<Player> GeheimT�r = new ArrayList<Player>();
	public static List<Player> GeheimT�rRemove = new ArrayList<Player>();
	public static HashMap<Location, Material> BlockType = new HashMap<Location, Material>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length >= 1) {
					if(args[0].equals("add")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						GeheimT�r.add(p);
					} else if(args[0].equals("remove")) {
						p.sendMessage("�eKlicke auf die entsprechende T�r");
						GeheimT�rRemove.add(p);
					} else if(args[0].equals("finish")) {
						GeheimT�r.remove(p);
						p.sendMessage("�eAlle Geheimt�ren wurden registiert");
					}else if(args[0].equals("abbrechen")){
						GeheimT�rRemove.remove(p);
						p.sendMessage("�eDu hast das Removen abgebrochen");
					}else
						p.sendMessage("�cbitte benutz /geheim add/remove/finish");
				} else
					p.sendMessage("�cbitte benutz /geheim add/remove/finish");
			} else 
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void T�rRegistrieren(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (GeheimT�r.contains(p)) {
			try {
				if (!Main.GeheimT�rLoc .contains(e.getClickedBlock().getLocation())) {
						e.setCancelled(true);
						p.sendMessage("�aDu hast die Geheimt�r erfolgreich registriert");
						SQLData.addGeheimDoor(e.getClickedBlock().getLocation());
				} else
					p.sendMessage("�cDie T�r wurde bereits als Geheim T�r registriert!");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}																									   

	@EventHandler
	public void T�rRemove(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (GeheimT�rRemove.contains(p)) {
				if (Main.GeheimT�rLoc.contains(e.getClickedBlock().getLocation())) {
						e.setCancelled(true);
						p.sendMessage("�aDie Geheim T�r ist nun nicht mehr registriert");
						SQLData.removeGeheimDoor(e.getClickedBlock().getLocation());
						GeheimT�rRemove.remove(p);
				} else
					p.sendMessage("�cDie T�r ist keine Geheim T�r!");
		}
	}
	
	@EventHandler
	public void onClickGeheim(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getPlayer().isSneaking() && e.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
				if (!GeheimT�r.contains(e.getPlayer())) {
					if (!GeheimT�rRemove.contains(e.getPlayer())) {
						if (Main.GeheimT�rLoc.contains(e.getClickedBlock().getLocation())) {
							if (!BlockType.containsKey(e.getClickedBlock().getLocation())) {
								BlockType.put(e.getClickedBlock().getLocation(),
										e.getClickedBlock().getLocation().getBlock().getType());
							}
							e.getClickedBlock().getLocation().getBlock().setType(Material.AIR);
							KlickH�ren(e.getClickedBlock().getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_OPEN);
							Location OverDoorLoc = e.getClickedBlock().getLocation();
							OverDoorLoc.setY(OverDoorLoc.getY() + 1);
							Location DownDoorLoc = e.getClickedBlock().getLocation();
							DownDoorLoc.setY(DownDoorLoc.getY() - 1);
							if (Main.GeheimT�rLoc.contains(OverDoorLoc)) {
								if (!BlockType.containsKey(OverDoorLoc)) {
									BlockType.put(OverDoorLoc, OverDoorLoc.getBlock().getType());
								}
								OverDoorLoc.getBlock().setType(Material.AIR);
							} else if (Main.GeheimT�rLoc.contains(DownDoorLoc)) {
								if (!BlockType.containsKey(DownDoorLoc)) {
									BlockType.put(DownDoorLoc, DownDoorLoc.getBlock().getType());
								}
								DownDoorLoc.getBlock().setType(Material.AIR);
							}
							Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
								@Override
								public void run() {
									try {
										Material A = BlockType.get(e.getClickedBlock().getLocation());
										e.getClickedBlock().getLocation().getBlock().setType(A);
										KlickH�ren(e.getClickedBlock().getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_OPEN);
										if (Main.GeheimT�rLoc.contains(OverDoorLoc)) {
											Material B = BlockType.get(OverDoorLoc);
											OverDoorLoc.getBlock().setType(B);
										} else if (Main.GeheimT�rLoc.contains(DownDoorLoc)) {
											Material C = BlockType.get(DownDoorLoc);
											DownDoorLoc.getBlock().setType(C);
										}
									} catch (Exception e2) {
										// TODO: handle exception
									}
								}
							}, (long) (1 * 20));
						}
					}
				} else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					if (Main.GeheimT�rLoc.contains(e.getClickedBlock().getLocation())) {
						SQLData.removeGeheimDoor(e.getClickedBlock().getLocation());
						e.getPlayer().sendMessage("�6Die Geheim T�r wurde entfernt");
					}
				}
			}
		}

	}
	
	public void KlickH�ren(Location loc, Sound s) {
		
		for (Player current : Bukkit.getOnlinePlayers()) {
			if(loc.distance(current.getLocation()) <= 6) {
				current.playSound(loc, s, 6, 2);
				
			}
			
		}
	}

}
