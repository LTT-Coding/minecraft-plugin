package de.ltt.other;import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class MeClick implements Listener{
	
	@EventHandler
	public void onInvKlick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§eWähle eine Aktion")) {
				Player p = (Player) e.getWhoClicked();
				PlayerInfo pi = new PlayerInfo(p);
				if(pi.haspacked()) {
					Player t = PlayerInteractInv.packMap.get(p);
					if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6" + t.getName())) {
						
						Inventory Inv = Bukkit.createInventory(null, 9, "§eWas möchtest du mit §6" + t.getName() + "§e machen");
						
						ItemStack freilassenItem = new ItemBuilder(Material.LEGACY_LEASH).setName("§6Freilassen").build();
						ItemStack fesselnItem = new ItemBuilder(Material.LEGACY_LEASH).setName("§6Fesseln").build();
						Inv.addItem(freilassenItem);
						Inv.addItem(fesselnItem);
						p.openInventory(Inv);
						return;
					}
				}
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Hinsetzen")) {
					if(!pi.isSitting() && p.getVehicle() == null) {
						if(!p.getLocation().subtract(0, 0.5, 0).getBlock().isEmpty() && !p.getLocation().subtract(0, 0.5, 0).getBlock().isLiquid()) {
							Location loc = p.getLocation();
							loc.subtract(0, 1.75, 0);
							ArmorStand armor = p.getWorld().spawn(loc, ArmorStand.class);
							armor.setPassenger(p);
							armor.setVisible(false);
							pi.setSitting(true);
							armor.setGravity(false);
						}else
							p.sendMessage("§cDu kannst dies im Fallen nicht machen!");
					}else
						p.sendMessage("§cDu Sitzt bereits");
					p.closeInventory();
				}
				
			e.setCancelled(true);
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

}
