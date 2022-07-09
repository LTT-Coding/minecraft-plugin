package de.ltt.other.KopfInv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ItemSkulls;

public class KopfInv implements CommandExecutor, Listener {

	public static HashMap<Player, Integer> kopfPage = new HashMap<Player, Integer>();
	public static List<Player> RemoveKopfPlayer = new ArrayList<Player>();
	public static HashMap<String, String> kopfName = SQLData.getKöpfeMap();
	public static HashMap<Player, String> searchMap = new HashMap<Player, String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length == 3) {
							if (!kopfName.containsKey(args[1])) {
								if (!kopfName.containsValue(args[2])) {
									SQLData.addKopf(args[2], args[1]);
									p.sendMessage("§aDu hast erfolgreich einen Kopf hinzugefügt!");
								} else
									p.sendMessage("§cEs gibt bereits einen Kopf mit diesem Namen!");
							} else
								p.sendMessage("§cDiesen Kopf gibt es bereits!");
						} else
							p.sendMessage("§cBitte benutze /kopf add/remove/get <ID> §c||§6 /kopf open §c||§6 /kopf search/spieler <Name>!");
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (args.length == 1) {
							RemoveKopfPlayer.add(p);
							p.sendMessage("§aKlicke auf den Kopf den du Löschen möchtest!");
							p.openInventory(getKopfInv(1));
							kopfPage.put(p, 1);
						} else
							p.sendMessage("§cBitte benutze /kopf add/remove/get <ID> §c||§6 /kopf open §c||§6 /kopf search/spieler <Name>!");
					} else if (args[0].equalsIgnoreCase("open")) {
						if (Main.KopfInv.size() != 0) {
							p.sendMessage("§5An dieser Stelle:");
							p.sendMessage("§dVIELEN DANK FÜR DAS EINRICHTEN DER GANZEN WOHNUNGEN! <3");
							p.openInventory(getKopfInv(1));
							kopfPage.put(p, 1);
						} else
							p.sendMessage("§cEs wurden keine Köpfe hinzugefügt!");
					} else if(args[0].equalsIgnoreCase("search")) {
						if(args.length == 2) {
							p.openInventory(getSearchInv(1, args[1]));
							kopfPage.put(p, 1);
							searchMap.put(p, args[1]);
						}else
							p.sendMessage("§cBitte benutze /kopf add/remove/get <ID> §c||§6 /kopf open §c||§6 /kopf search/spieler <Name>!");
					} else if(args[0].equalsIgnoreCase("searchadd")) {
						if(!Main.KopfSearch.contains(args[1])) {
							SQLData.addSearch(args[1]);
							p.sendMessage("§6Suchbegriff wurde hinzugefügt");
						} else 
							p.sendMessage("§cDiesen Suchbegriff gibt es schon");
					} else if(args[0].equalsIgnoreCase("searchremove")) {
						if(Main.KopfSearch.contains(args[1])) {
							SQLData.removeSearch(args[1]);
							p.sendMessage("§6Suchbegriff wurde entfernt");
						} else 
							p.sendMessage("§cDiesen Suchbegriff gibt es nicht");
					} else if(args[0].equalsIgnoreCase("get")) {
						ItemStack item = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + args[1]);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("§6Kopf");
						item.setItemMeta(meta);
						p.getInventory().addItem(item);
						p.sendMessage("§6Du hast ein Kopf erhalten");
					} else if(args[0].equalsIgnoreCase("spieler")) {
						ItemStack KopfSpieler = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
						SkullMeta KopfMeta = (SkullMeta) KopfSpieler.getItemMeta();
						KopfMeta.setOwner(args[1]);
						KopfMeta.setDisplayName("§6" + args[1]);
						KopfSpieler.setItemMeta(KopfMeta);
						KopfSpieler.setAmount(1);
						p.getInventory().addItem(KopfSpieler);
						p.sendMessage("§6Du hast ein Spielerkopf erhalten");
					} else 
							p.sendMessage("§cBitte benutze /kopf add/remove/get <ID> §c||§6 /kopf open §c||§6 /kopf search/spieler <Name>!");
				} else
					p.sendMessage("§cBitte benutze /kopf add/remove/get <ID> §c||§6 /kopf open §c||§6 /kopf search/spieler <Name>!");

			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void onKlickInv(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§eKöpfe")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					kopfPage.put(p, kopfPage.get(p) + 1);
					if (RemoveKopfPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
						RemoveKopfPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					kopfPage.put(p, kopfPage.get(p) - 1);
					if (RemoveKopfPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
						RemoveKopfPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
					}
				} else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					if (!RemoveKopfPlayer.contains(p)) {
						p.getInventory().addItem(e.getCurrentItem());
					} else {
						Main.KopfInv.remove(e.getCurrentItem().getItemMeta().getDisplayName());
						for (String current : Main.KopfInv) {
							if (kopfName.get(current).equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
								SQLData.removeKopf(current);
							}
						}
						RemoveKopfPlayer.remove(p);
						p.sendMessage("§aDu hast erfolgreich ein Kopf entfernt");
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
					}

				}

				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§eKöpfe die den Suchbegriff enthalten")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					kopfPage.put(p, kopfPage.get(p) + 1);
					if (RemoveKopfPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getSearchInv(kopfPage.get(p), searchMap.get(p)));
						RemoveKopfPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getSearchInv(kopfPage.get(p), searchMap.get(p)));
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					kopfPage.put(p, kopfPage.get(p) - 1);
					if (RemoveKopfPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getSearchInv(kopfPage.get(p), searchMap.get(p)));
						RemoveKopfPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getSearchInv(kopfPage.get(p), searchMap.get(p)));
					}
				} else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					if (!RemoveKopfPlayer.contains(p)) {
						p.getInventory().addItem(e.getCurrentItem());
					} else {
						Main.KopfInv.remove(e.getCurrentItem().getItemMeta().getDisplayName());
						for (String current : Main.KopfInv) {
							if (kopfName.get(current).equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
								SQLData.removeKopf(current);
							}
						}
						RemoveKopfPlayer.remove(p);
						p.sendMessage("§aDu hast erfolgreich ein Kopf entfernt");
						p.closeInventory();
						p.openInventory(getKopfInv(kopfPage.get(p)));
					}

				}

				e.setCancelled(true);
			}
		} catch (Exception e2) {
		}

	}

	@EventHandler
	public void onCloseInv(InventoryClickEvent e) {
		if (RemoveKopfPlayer.contains(e.getWhoClicked())) {
			RemoveKopfPlayer.remove(e.getWhoClicked());
		}
	}

	public static Inventory getKopfInv(int page) {
		int i;
		if (Main.KopfInv.size() <= 45) {

			for (i = 0; i * 9 < Main.KopfInv.size(); i++) {
			}

			Inventory KöpfeInv = Bukkit.createInventory(null, i * 9, "§eKöpfe");
			
			for(String current : Main.KopfInv) {
				ItemStack item = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + current);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(kopfName.get(current));
				item.setItemMeta(meta);
				KöpfeInv.addItem(item);
			}
			return KöpfeInv;
		} else {
			int startslot = (page - 1) * 9 * 5;
			double size = Main.KopfInv.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory KöpfeInv = Bukkit.createInventory(null, i * 9, "§eKöpfe");
			i = 0;
			boolean finish = false;
			for (int l = 0; l < KöpfeInv.getSize() - 9 && l + startslot < Main.KopfInv.size(); l++) {
				ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + Main.KopfInv.get(l + startslot));
				ItemMeta skull1Meta = skull1.getItemMeta();
				skull1Meta.setDisplayName(kopfName.get(Main.KopfInv.get(l + startslot)));
				skull1.setItemMeta(skull1Meta);
				skull1.setAmount(1);
				KöpfeInv.setItem(l, skull1);
			}
			if (page * 36 >= Main.KopfInv.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					KöpfeInv.setItem(KöpfeInv.getSize() - 5, skull1);
				}
			} else {

				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				KöpfeInv.setItem(KöpfeInv.getSize() - 6, skull2);
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					KöpfeInv.setItem(KöpfeInv.getSize() - 4, skull1);
				}
			}
			return KöpfeInv;
		}

	}

	public static Inventory getSearchInv(int page, String search) {
		int i;
		List<String> searched = new ArrayList<String>();
		for (String current : Main.KopfInv) {
			if (kopfName.get(current).toLowerCase().contains(search.toLowerCase())) {
				searched.add(current);
			}
		}
		if (searched.size() <= 45) {
			if(searched.size() > 0) {
				for (i = 0; i * 9 < searched.size(); i++) {
				}
				Inventory KöpfeInv = Bukkit.createInventory(null, i * 9, "§eKöpfe die den Suchbegriff enthalten");

				for (String current : searched) {
					ItemStack item = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + current);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(kopfName.get(current));
					item.setItemMeta(meta);
					KöpfeInv.addItem(item);
				}
				return KöpfeInv;
			}else {
				Inventory KöpfeInv = Bukkit.createInventory(null, 9, "§eKöpfe die den Suchbegriff enthalten");
				return KöpfeInv;
			}
		} else {
			int startslot = (page - 1) * 9 * 5;
			double size = searched.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory KöpfeInv = Bukkit.createInventory(null, i * 9, "§eKöpfe die den Suchbegriff enthalten");
			i = 0;
			boolean finish = false;

			
			for(int l = 0; l < KöpfeInv.getSize() - 9 && l + startslot < searched.size(); l++) { 
			ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + searched.get(l + startslot)); 
			ItemMeta skull1Meta = skull1.getItemMeta();
			skull1Meta.setDisplayName(kopfName.get(searched.get(l + startslot)) + "");
			skull1.setItemMeta(skull1Meta); skull1.setAmount(1); KöpfeInv.setItem(l,skull1); 

			}
			if (page * 36 >= Main.KopfInv.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					KöpfeInv.setItem(KöpfeInv.getSize() - 5, skull1);
				}
			} else {

				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				KöpfeInv.setItem(KöpfeInv.getSize() - 6, skull2);
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					KöpfeInv.setItem(KöpfeInv.getSize() - 4, skull1);
				}
			}
			return KöpfeInv;
		}

	}

}
