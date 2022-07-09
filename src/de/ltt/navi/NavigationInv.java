package de.ltt.navi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;

public class NavigationInv implements CommandExecutor, Listener{

	
	public static HashMap<Player, Integer> page = new HashMap<Player, Integer>();
	public static HashMap<Player, String> search  = new HashMap<Player, String>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Inventory inv = Bukkit.createInventory(null, 9 * 3, "§eNavigation");
			PlayerInfo pi = new PlayerInfo(p);
			ItemStack stopRoute;
			ItemMeta stopMeta;
			if (pi.isActiveRoute()) {
				stopRoute = new ItemBuilder(Material.HEART_OF_THE_SEA).setCustomModelData(10000034).build();
				stopMeta = stopRoute.getItemMeta();
				stopMeta.setDisplayName("§4Route Abbrechen");
			} else {
				stopRoute = new ItemBuilder(Material.HEART_OF_THE_SEA).setCustomModelData(10000033).build();
				stopMeta = stopRoute.getItemMeta();
				stopMeta.setDisplayName("§7Route Abbrechen");
			}
			stopRoute.setItemMeta(stopMeta);
			inv.setItem(18, stopRoute);
			ItemStack next = new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§6Nächste Punkte").setCustomModelData(10000029).build();
			inv.setItem(9*1+3, next);
			
			ItemStack search = new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§6Suchen").setCustomModelData(10000032).build();
			inv.setItem(9*1+4, search);
			
			ItemStack popular = new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§6Beliebt").setCustomModelData(10000031).build();
			inv.setItem(9*1+5, popular);
			
			ItemStack all = new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§6Alle").setCustomModelData(10000028).build();
			inv.setItem(4, all);
			
			
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
			
			
			if(e.getView().getTitle().equals("§eNavigation") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				p.closeInventory();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Route Abbrechen")) {
					pi.stopRoute();
					p.sendMessage("§4Route wurde abgebrochen!");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Nächste Punkte")) {
					Inventory inv  = openNextInv(p.getLocation(), 1);
					page.put(p, 1);
					p.openInventory(inv);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Suchen")) {
					ChatInfo.addChat(p, ChatType.NAVISEARCH, 30*20);
					p.sendMessage("§eGebe deine Suche ein:");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Beliebt")) {
					p.openInventory(openPopularInv());
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Alle")) {
				}
				e.setCancelled(true);
				
				
			}else if(e.getView().getTitle().equals("§eNächste Navipunkte") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Nächste Seite")) {
					page.put(p, page.get(p) + 1);
					p.openInventory(openNextInv(p.getLocation(), page.get(p)));
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Vorherige Seite")) {
					page.put(p, page.get(p) - 1);
					p.openInventory(openNextInv(p.getLocation(), page.get(p)));
				}else {
					String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
					if(Main.navigationsPunkte.contains(name)) {
						pi.runRoute(Main.naviPoints.get(name));
						p.sendMessage("§aZielführung nach §6" + name + "§a gestartet!");
						SQLData.addNaviKlick(name);
						p.closeInventory();
					}else
						p.sendMessage("§cDieser Punkt existiert nicht mehr!");
				}
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§eNavipunkte mit diesem Suchbegriff") && e.getView().getTopInventory().equals(e.getClickedInventory())){
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Nächste Seite")) {
					page.put(p, page.get(p) + 1);
					p.openInventory(openSearchInv(page.get(p), search.get(p)));
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Vorherige Seite")) {
					page.put(p, page.get(p) - 1);
					p.openInventory(openSearchInv(page.get(p), search.get(p)));
				}else {
					String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
					if(Main.navigationsPunkte.contains(name)) {
						pi.runRoute(Main.naviPoints.get(name));
						p.sendMessage("§aZielführung nach §6" + name + "§a gestartet!");
						SQLData.addNaviKlick(name);
						p.closeInventory();
					}else
						p.sendMessage("§cDieser Punkt existiert nicht mehr!");
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().equals("§eBeliebte Navipunkte") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
				if (Main.navigationsPunkte.contains(name)) {
					pi.runRoute(Main.naviPoints.get(name));
					p.sendMessage("§aZielführung nach §6" + name + "§a gestartet!");
					SQLData.addNaviKlick(name);
					p.closeInventory();
				} else
					p.sendMessage("§cDieser Punkt existiert nicht mehr!");
				e.setCancelled(true);
			}
			
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayeChat(PlayerChatEvent e) {
		if(ChatInfo.getChat(e.getPlayer()).equals(ChatType.NAVISEARCH)) {
			Player p = e.getPlayer();
			p.sendMessage("§e Suche:" + e.getMessage());
			p.openInventory(openSearchInv(1, e.getMessage()));
			page.put(p, 1);
			search.put(p, e.getMessage());
			ChatInfo.removeChat(p);
		}
	}
	
	public Inventory openNextInv(Location loc, int page) {
		List<String> next = new ArrayList<String>();
		for(String current : Main.navigationsPunkte) {
			if(Main.naviPoints.get(current).distance(loc) <= 500) {
				next.add(current);
			}
		}
		if(next.size() != 0) {
			int i;
			if(next.size() <= 45) {
				for(i = 0; i*9 < next.size(); i++) {}
				Inventory inv = Bukkit.createInventory(null, i*9, "§eNächste Navipunkte");
				for(String current : next) {
					ItemStack item = new ItemStack(Main.pointMaterial.get(current));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName("§6" + current);
					item.setItemMeta(meta);
					inv.addItem(item);
				}
				return inv;
			}else {
				int startslot = (page-1)*9*5;
				double size = next.size() - startslot;
				for (i = 0; i*9 < size && i < 5; i++) {}
				i++;
				Inventory inv = Bukkit.createInventory(null, i*9, "§eNächste Navipunkte");
				i = 0;
				boolean finish = false;
				for(int l = 0; l < inv.getSize() - 9 && l + startslot < next.size(); l++) {
					String name = next.get(l + startslot);
					ItemStack item = new ItemStack(Main.pointMaterial.get(name));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName("§6" + name);
					item.setItemMeta(meta);
					inv.setItem(l, item);
				}
				if(page * 36 >= next.size()) finish = true;
				if(page == 1) {
					if(!finish){

						ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
						ItemMeta skull1Meta = skull1.getItemMeta();
						skull1Meta.setDisplayName("§7Nächste Seite");
						skull1.setItemMeta(skull1Meta);
						skull1.setAmount(1);
						inv.setItem(inv.getSize() - 5, skull1);
					}
				}else {
					
					ItemStack skull2 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
					ItemMeta skull2Meta = skull2.getItemMeta();
					skull2Meta.setDisplayName("§7Vorherige Seite");
					skull2.setItemMeta(skull2Meta);
					skull2.setAmount(1);
					inv.setItem(inv.getSize() - 6, skull2);
					if(!finish){

						ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
						ItemMeta skull1Meta = skull1.getItemMeta();
						skull1Meta.setDisplayName("§7Nächste Seite");
						skull1.setItemMeta(skull1Meta);
						skull1.setAmount(1);
						inv.setItem(inv.getSize() - 4, skull1);
					}
				}
				return inv;
			}
		}else {
			Inventory inv = Bukkit.createInventory(null, 9, "§eNächste Navipunkte");
			return inv;
		}
	}
	
	public Inventory openSearchInv(int page, String search) {
		List<String> searched = new ArrayList<String>();
		for(String current : Main.navigationsPunkte) {
			if(current.toLowerCase().contains(search.toLowerCase())) {
				searched.add(current);
			}
		}
		if(searched.size() != 0) {
			int i;
			if(searched.size() <= 45) {
				for(i = 0; i*9 < searched.size(); i++) {}
				Inventory inv = Bukkit.createInventory(null, i*9, "§eNavipunkte mit diesem Suchbegriff");
				for(String current : searched) {
					ItemStack item = new ItemStack(Main.pointMaterial.get(current));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName("§6" + current);
					item.setItemMeta(meta);
					inv.addItem(item);
				}
				return inv;
			}else {
				int startslot = (page-1)*9*5;
				double size = searched.size() - startslot;
				for (i = 0; i*9 < size && i < 5; i++) {}
				i++;
				Inventory inv = Bukkit.createInventory(null, i*9, "§eNavipunkte mit diesem Suchbegriff");
				i = 0;
				boolean finish = false;
				for(int l = 0; l < inv.getSize() - 9 && l + startslot < searched.size(); l++) {
					String name = searched.get(l + startslot);
					ItemStack item = new ItemStack(Main.pointMaterial.get(name));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName("§6" + name);
					item.setItemMeta(meta);
					inv.setItem(l, item);
				}
				if(page * 36 >= searched.size()) finish = true;
				if(page == 1) {
					if(!finish){

						ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
						ItemMeta skull1Meta = skull1.getItemMeta();
						skull1Meta.setDisplayName("§7Nächste Seite");
						skull1.setItemMeta(skull1Meta);
						skull1.setAmount(1);
						inv.setItem(inv.getSize() - 5, skull1);
					}
				}else {
					
					ItemStack skull2 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
					ItemMeta skull2Meta = skull2.getItemMeta();
					skull2Meta.setDisplayName("§7Vorherige Seite");
					skull2.setItemMeta(skull2Meta);
					skull2.setAmount(1);
					inv.setItem(inv.getSize() - 6, skull2);
					if(!finish){

						ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
						ItemMeta skull1Meta = skull1.getItemMeta();
						skull1Meta.setDisplayName("§7Nächste Seite");
						skull1.setItemMeta(skull1Meta);
						skull1.setAmount(1);
						inv.setItem(inv.getSize() - 4, skull1);
					}
				}
				return inv;
			}
		}else {
			Inventory inv = Bukkit.createInventory(null, 9, "§eNavipunkte mit diesem Suchbegriff");
			return inv;
		}
	}
	
	public Inventory openPopularInv() {
		Inventory inv = Bukkit.createInventory(null, 9, "§eBeliebte Navipunkte");
		List<Long> klick = new ArrayList<Long>();
		for(String current : Main.navigationsPunkte) {
			klick.add(Main.pointKlicks.get(current));
		}
		klick.sort(null);
		Collections.reverse(klick);
		List<String> blocked = new ArrayList<String>();
		for(int i = 0; i < klick.size(); i++) {
			if(i < 9) {
				for(String current: Main.navigationsPunkte) {
					if(!blocked.contains(current)) {
						if(Main.pointKlicks.get(current) == klick.get(i)) {
							ItemStack item = new ItemStack(Main.pointMaterial.get(current));
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§6" + current);
							item.setItemMeta(meta);
							inv.addItem(item);
							blocked.add(current);
						}
					}
				}
			}
		}
		return inv;
	}

}
