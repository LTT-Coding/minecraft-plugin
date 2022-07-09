package de.ltt.staat.bürgermeister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;

public class Waehlen implements CommandExecutor, Listener{
	
	HashMap<Player, OfflinePlayer> choosen = new HashMap<Player, OfflinePlayer>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.wahlLoc.distance(p.getLocation()) <= 3) {
				if(!Main.wähler.contains(p.getUniqueId().toString())) {
					if(Main.nominated.size() != 0) {
						if(Main.iswahl) {
							p.openInventory(Main.getWahlInv(1));
							Main.wahlPage.put(p, 1);
						}else
							p.sendMessage("§cDu kannst nur während einer Wahl wählen!");
					}else
						p.sendMessage("§cEs hat sich niemand zur Wahl aufgestellt!");
				}else
					p.sendMessage("§cDu hast bereits gewählt!");
			} else 
				p.sendMessage("§cDu kannst nur an der dafür vorgesehenen Stelle wählen!");
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void onInvKlick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§eWer soll Bürgermeister werden?")) {
				Player p = (Player) e.getWhoClicked();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					Main.wahlPage.put(p, Main.wahlPage.get(p) + 1);
					p.closeInventory();
					p.openInventory(Main.getWahlInv(Main.wahlPage.get(p)));
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					Main.wahlPage.put(p, Main.wahlPage.get(p) - 1);		
					p.closeInventory();
					p.openInventory(Main.getWahlInv(Main.wahlPage.get(p)));
				}else {
					String itemName = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
					OfflinePlayer t = Bukkit.getOfflinePlayer(itemName);
					choosen.put(p, t);
					
					Inventory inv = Bukkit.createInventory(null, 9, "§eBitte bestätige deine Auswahl");
					ItemStack acceptitem = new ItemBuilder(Material.GREEN_CONCRETE).setName("§aBestätigen").build();
					ItemStack denyitem = new ItemBuilder(Material.RED_CONCRETE).setName("§4Abbrechen").build();
					ItemStack kopf = e.getCurrentItem();
					inv.setItem(8,acceptitem);
					inv.setItem(0, denyitem);
					inv.setItem(4, kopf);
					p.openInventory(inv);
				}
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§eBitte bestätige deine Auswahl")) {
				Player p = (Player) e.getWhoClicked();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§aBestätigen")) {
					Main.wähler.add(p.getUniqueId().toString());
					p.closeInventory();
					p.sendMessage("§aDu hast §6" + choosen.get(p).getName() +"§a deine Stimme gegeben!");
					if(Main.wahlstats.containsKey(choosen.get(p).getUniqueId().toString())) {
						Main.wahlstats.put(choosen.get(p).getUniqueId().toString(), Main.wahlstats.get(choosen.get(p).getUniqueId().toString()) + 1);
					}else
						Main.wahlstats.put(choosen.get(p).getUniqueId().toString(), 1);
					List<String> wahlStatsKey = new ArrayList<>();
					List<Integer> wahlStatsValue = new ArrayList<>();
					for(String current : Main.wahlstats.keySet()) {
						wahlStatsKey.add(current);
						wahlStatsValue.add(Main.wahlstats.get(current));
					}
					Main.getPlugin().getConfig().set("Staat.wähler", Main.wähler);
					Main.getPlugin().getConfig().set("Staat.wahlStats.key", wahlStatsKey);
					Main.getPlugin().getConfig().set("Staat.wahlStats.value", wahlStatsValue);
					Main.getPlugin().saveConfig();
					
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Abbrechen")) {
					p.closeInventory();
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			
		}
	}
	

}
