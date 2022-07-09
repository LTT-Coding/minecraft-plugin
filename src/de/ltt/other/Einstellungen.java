package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class Einstellungen implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);
			Inventory inv  = Bukkit.createInventory(null, 9, "§eEinstellungen");
			if(pi.getMailBoxJoin()) {
				inv.addItem(new ItemBuilder(Material.LIME_CONCRETE).setName("§6Mailbox").build());
			}else
				inv.addItem(new ItemBuilder(Material.RED_CONCRETE).setName("§6Mailbox").build());
			p.openInventory(inv);
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			Player p = (Player) e.getWhoClicked();
			PlayerInfo pi = new PlayerInfo(p);
			if(e.getView().getTitle().equals("§eEinstellungen") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Mailbox")) {
					if(pi.getMailBoxJoin()) {
						pi.setMailBoxJoin(false);
						e.getCurrentItem().setType(Material.RED_CONCRETE);
					}else {
						pi.setMailBoxJoin(true);
						e.getCurrentItem().setType(Material.LIME_CONCRETE);
					}
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {
		}
	}
	

}
