package de.ltt.firms;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;

public class SetRights implements Listener{
	
	public static HashMap<Player, OfflinePlayer> pRightset = new HashMap<Player, OfflinePlayer>();
	public static HashMap<Integer, Player> pRightsetter = new HashMap<Integer, Player>();
	
	@EventHandler
	public void onInventoryKlick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
		try {
			if(e.getView().getTitle().equals("§eWähle einen Spieler aus!")) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")); 
				if(!t.getUniqueId().toString().equals(fi.getOwner())) {
					if(!t.getUniqueId().toString().equals(p.getUniqueId().toString())) {
						if(fi.getMember().contains(t.getUniqueId().toString())) {
							if(!pRightsetter.containsKey(pi.getJob())) {
								int i;
								for(i = 0; i*9< fi.getRights().size(); i++) {
									
								}
								Inventory Inv = Bukkit.createInventory(null, i*9, "§eStelle die Rechte ein");
								for(String current: fi.getRights()) {
									if(current.equals(Rights.MAIN_SETRIGHTS.toString())) {
										if(p.getUniqueId().toString().equals(fi.getOwner())) {
											ItemStack item;
											if(fi.hasRankString(t, current)) {
												item = new ItemStack(Material.LIME_CONCRETE, 1);
											}else {
												item = new ItemStack(Material.RED_CONCRETE, 1);
											}
											ItemMeta meta = item.getItemMeta();
											meta.setDisplayName("§a" + Main.RightStrings.get(current));
											item.setItemMeta(meta);
											Inv.addItem(item);
										}
									}else {
										ItemStack item;
										if(fi.hasRankString(t, current)) {
											item = new ItemStack(Material.LIME_CONCRETE, 1);
										}else {
											item = new ItemStack(Material.RED_CONCRETE, 1);
										}
										ItemMeta meta = item.getItemMeta();
										meta.setDisplayName("§a" + Main.RightStrings.get(current));
										item.setItemMeta(meta);
										Inv.addItem(item);
									}
								}
								p.openInventory(Inv);
								pRightset.put(p, t);
								pRightsetter.put(pi.getJob(), p);
							}else
								p.sendMessage("§cJemand aus deiner Firma ist bereits in diesem Menü!");
						}else
							p.sendMessage("§cDieser Spieler ist nicht mehr in der Firma!");
					}else
						p.sendMessage("§cDu kannst deine eigenen Rechte nicht ändern!");
				}else
					p.sendMessage("§cDie Rechte des Firmenbesitzers können nicht geändert werden!");
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§eStelle die Rechte ein") && e.getClickedInventory() == e.getView().getTopInventory()) {
				OfflinePlayer t = pRightset.get(p);
				List<String> rights = fi.getpRights(t);
				if(fi.hasrank(p, Rights.MAIN_SETRIGHTS)) {
					for(String current: Main.RightStrings.keySet()) {
						if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§a" + Main.RightStrings.get(current))) {
							if(fi.hasRankString(t, current)) {
								ItemStack item = p.getOpenInventory().getItem(e.getSlot());
								item.setType(Material.RED_CONCRETE);
								p.getOpenInventory().setItem(e.getSlot(), item);
								rights.remove(current);
							}else {
								ItemStack item = p.getOpenInventory().getItem(e.getSlot());
								item.setType(Material.LIME_CONCRETE);
								p.getOpenInventory().setItem(e.getSlot(), item);
								rights.add(current);
							}
							break;
						}
					}
					fi.setpRights(t, rights);
				}else {
					p.closeInventory();
					p.sendMessage("§cDazu hast du keine Rechte mehr!");
				}
				e.setCancelled(true);
			}
			
		} catch (Exception e2) {
		}
		
	}
	
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		try {
			Player p = (Player) e.getPlayer();
			PlayerInfo pi = new PlayerInfo(p);
			if(pRightsetter.containsKey(pi.getJob())) {
				if(e.getView().getTitle().equals("§eStelle die Rechte ein")) {
					pRightsetter.remove(pi.getJob());
					p.sendMessage("§7Du hast das Menü verlassen!");
				}
			}
		} catch (Exception e2) {
		}
	}
	

}
