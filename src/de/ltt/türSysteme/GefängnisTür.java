package de.ltt.türSysteme;

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

public class GefängnisTür implements CommandExecutor, Listener{

	public static List<Player> selectTrigger = new ArrayList<Player>();
	public static HashMap<Player, Location> triggerMap = new HashMap<Player, Location>();
	public static List<Player> areaSelect = new ArrayList<Player>();
	public static HashMap<Player, Location> selectedBlocks = new HashMap<Player, Location>();
	public static HashMap<Player, List<Location>> selesctedBlocks = new HashMap<Player, List<Location>>();
	public static List<Player> clickBlock = new ArrayList<>();
	
	private final int key = 10000002;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("add")) {
						selectTrigger.add(p);
						p.sendMessage("§eWähle den Auslöser für die Gefängnistür aus!");
					}
				}else
					p.sendMessage("§cBenutze §6/prisondoor add/remove §c um eime Gefängnistür hinzuzufügen oder zu entfernen!");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(!clickBlock.contains(e.getPlayer())) {
				clickBlock.add(e.getPlayer());
				Player p = e.getPlayer();
				if(selectTrigger.contains(e.getPlayer())) {
					selectTrigger.remove(p);
					triggerMap.put(p, e.getClickedBlock().getLocation());
					p.sendMessage("§aAuslöser wurde registriert!");
					p.sendMessage("§eWähle die Gitter aus!");
					areaSelect.add(p);
				}else if(areaSelect.contains(p)) {
					if(!selectedBlocks.containsKey(p)) {
						selectedBlocks.put(p, e.getClickedBlock().getLocation());
						p.sendMessage("§3Wähle die zweite Stelle aus!");
					}else {
						Location loc1 = selectedBlocks.get(p);
						Location loc2 = e.getClickedBlock().getLocation();
						if(loc1 != loc2) {
								if(loc1.getZ() == loc2.getZ()) {
									int sx;
									int lx;
									int sy;
									int ly;
									if(loc1.getX() > loc2.getX()) {
										lx = loc1.getBlockX();
										sx = loc2.getBlockX();
									}else {
										lx = loc2.getBlockX();
										sx = loc1.getBlockX();
									}
									if(loc1.getY() > loc2.getY()) {
										ly = loc1.getBlockY();
										sy = loc2.getBlockY();
									}else {
										ly = loc2.getBlockY();
										sy = loc1.getBlockY();
									}
									areaSelect.remove(p);
									selectedBlocks.remove(p);
									SQLData.addPrisonDoor(triggerMap.get(p), DirectionType.Z, lx, sx, ly, sy, e.getClickedBlock().getLocation().getZ());
									p.sendMessage("§eGefängnistür erfolgreich registriert!");
								}else if(loc1.getX() == loc2.getX()) {
									int sz;
									int lz;
									int sy;
									int ly;
									if(loc1.getZ() > loc2.getZ()) {
										lz = loc1.getBlockZ();
										sz = loc2.getBlockZ();
									}else {
										lz = loc2.getBlockZ();
										sz = loc1.getBlockZ();
									}
									if(loc1.getY() > loc2.getY()) {
										ly = loc1.getBlockY();
										sy = loc2.getBlockY();
									}else {
										ly = loc2.getBlockY();
										sy = loc1.getBlockY();
									}
									areaSelect.remove(p);
									selectedBlocks.remove(p);
									SQLData.addPrisonDoor(triggerMap.get(p), DirectionType.X, lz, sz, ly, sy, e.getClickedBlock().getLocation().getX());
									p.sendMessage("§eGefängnistür erfolgreich registriert!");
								}else
									p.sendMessage("§cWähle einen 2-Dimensionalen Bereich aus!");
						}else 
							p.sendMessage("§cWähle zwei verschiedene Blöcke aus!");
					}
				}else if(Main.triggerMap.containsKey(e.getClickedBlock().getLocation())) {
						if(e.getPlayer().getItemInHand().getItemMeta().getCustomModelData() == key) {
							List<Double> doubles = Main.triggerMap.get(e.getClickedBlock().getLocation());
							double lXZ = doubles.get(0);
							double sXZ = doubles.get(1);
							double lY = doubles.get(2);
							double sY = doubles.get(3);
							double XZ = doubles.get(4);
							if(Main.directionMap.get(e.getClickedBlock().getLocation()).equals(DirectionType.X.toString())) {
								if(new Location(e.getClickedBlock().getLocation().getWorld(), XZ , sY , sXZ).getBlock().getType().equals(Material.IRON_BARS)) {
									for(int i = (int)sY; i <= lY; i++) {
										for(int l = (int)sXZ; l <= lXZ; l++) {
											new Location(e.getClickedBlock().getLocation().getWorld(), XZ, i, l).getBlock().setType(Material.AIR);
											Main.KlickHören(e.getClickedBlock().getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, (byte) 5);
										}
									}
								}else {
									for(int i = (int)sY; i <= lY; i++) {
										for(int l = (int)sXZ; l <= lXZ; l++) {
											new Location(e.getClickedBlock().getLocation().getWorld(), XZ, i, l).getBlock().setType(Material.IRON_BARS);
											Main.KlickHören(e.getClickedBlock().getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, (byte) 5);
										}
									}
								}
							}else {
								if(new Location(e.getClickedBlock().getLocation().getWorld(), sXZ , sY , XZ).getBlock().getType().equals(Material.IRON_BARS)) {
									for(int i = (int)sY; i <= lY; i++) {
										for(int l = (int)sXZ; l <= lXZ; l++) {
											new Location(e.getClickedBlock().getLocation().getWorld(), l, i, XZ).getBlock().setType(Material.AIR);
											Main.KlickHören(e.getClickedBlock().getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, (byte) 5);
										}
									}
								}else {
									for(int i = (int)sY; i <= lY; i++) {
										for(int l = (int)sXZ; l <= lXZ; l++) {
											new Location(e.getClickedBlock().getLocation().getWorld(), l, i, XZ).getBlock().setType(Material.IRON_BARS);
											Main.KlickHören(e.getClickedBlock().getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, (byte) 5);
										}
									}
								}
							}
						} 
					e.setCancelled(true);
				}
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
					@Override
					public void run() {
						clickBlock.remove(p);
					}
				}, 5);
			}
		}
	}

}
