package de.ltt.staat.medic;

import java.util.HashMap;

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

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class SetMedicRights implements Listener{

	public static HashMap<Player, OfflinePlayer> pRightset = new HashMap<Player, OfflinePlayer>();
	public static HashMap<Integer, Player> pRightsetter = new HashMap<Integer, Player>();
	
	
	@EventHandler
	public void onInventoryKlick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		MedicInfo mi = new MedicInfo();
		try {
			if(e.getView().getTitle().equals("§4Wähle einen Spieler aus") && e.getClickedInventory() == e.getView().getTopInventory()) {
			if(pi.getJob() == -1 || Main.Admin.contains(p.getUniqueId().toString())) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")); 
				if(!t.getUniqueId().toString().equals(mi.getOwner())) {
					if(!t.getUniqueId().toString().equals(p.getUniqueId().toString())) {
						if(mi.hasRight(p, MedicRights.SETRIGHTS)) {
						int j;
						for (j = 0; j*9 < mi.getRights().size(); j++) {
							
						}
						Inventory Inv = Bukkit.createInventory(null, j*9, "§4Stelle die Rechte ein");
						for (String current : mi.getRights()) {
							if(current.equals(MedicRights.SETRIGHTS.toString())) {
								if(mi.getOwner().equals(p.getUniqueId().toString())) {
									if(mi.getpRights(t).contains(current)) {
										ItemStack recht = new ItemBuilder(Material.LIME_CONCRETE).setName("§6" + Main.MedicRightStrings.get(current)).build();
										Inv.addItem(recht);
									} else {
										ItemStack recht = new ItemBuilder(Material.RED_CONCRETE).setName("§6" + Main.MedicRightStrings.get(current)).build();
										Inv.addItem(recht);
									}
								}
							}else {
								if(mi.getpRights(t).contains(current)) {
									ItemStack recht = new ItemBuilder(Material.LIME_CONCRETE).setName("§6" + Main.MedicRightStrings.get(current)).build();
									Inv.addItem(recht);
								} else {
									ItemStack recht = new ItemBuilder(Material.RED_CONCRETE).setName("§6" + Main.MedicRightStrings.get(current)).build();
									Inv.addItem(recht);
								}
							}
						}
						p.openInventory(Inv);
						pRightset.put(p, t);
						pRightsetter.put(pi.getJob(), p);
						}
					} else
						p.sendMessage("§cDu kannst deine eigenen Rechte nicht einstellen!");
				}else
					p.sendMessage("§cDu kannst die Rechte des Besitzers nicht ändern!");
			} e.setCancelled(true);
				
			} else if(e.getView().getTitle().equals("§4Stelle die Rechte ein") && e.getClickedInventory() == e.getView().getTopInventory()) {
				if(mi.hasRight(p, MedicRights.SETRIGHTS)) {
					OfflinePlayer t = pRightset.get(p);
					for(String current : Main.MedicRightStrings.keySet()) {
						if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6" + Main.MedicRightStrings.get(current))) {
							if (mi.getpRights(t).contains(current)) {
								ItemStack rechte = new ItemBuilder(Material.RED_CONCRETE).setName(e.getCurrentItem().getItemMeta().getDisplayName()).build();
								p.getOpenInventory().setItem(e.getSlot(), rechte);
								mi.removepRights(t, current);
							} else {
								ItemStack rechte = new ItemBuilder(Material.LIME_CONCRETE).setName(e.getCurrentItem().getItemMeta().getDisplayName()).build();
								p.getOpenInventory().setItem(e.getSlot(), rechte);
								mi.addpRights(t, current);
							}
							break;
						}
					}
				}else {
					p.closeInventory();
					p.sendMessage("§cDauzu hast du keine Rechte mehr!");
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
				if(e.getView().getTitle().equals("§4Stelle die Rechte ein")) {
					pRightsetter.remove(pi.getJob());
					p.sendMessage("§7Du hast das Menü verlassen!");
				}
			}
		} catch (Exception e2) {
		}
	}
}
