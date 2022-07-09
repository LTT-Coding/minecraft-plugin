package de.ltt.other.ModelInv;

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

public class ModelInv implements CommandExecutor, Listener {

	public static HashMap<Player, Integer> ModelPage = new HashMap<Player, Integer>();
	public static List<Player> RemoveModelPlayer = new ArrayList<Player>();
	public static HashMap<String, String> ModelName = SQLData.getModelMap();
	public static HashMap<Player, String> searchMap = new HashMap<Player, String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length == 3) {
							if (!ModelName.containsKey(args[1])) {
								if (!ModelName.containsValue(args[2])) {
									SQLData.addModel(args[2], args[1]);
									p.sendMessage("§aDu hast erfolgreich einen Model hinzugefügt!");
								} else
									p.sendMessage("§cEs gibt bereits einen Model mit diesem Namen!");
							} else
								p.sendMessage("§cDiesen Model gibt es bereits!");
						} else
							p.sendMessage(
									"§cBitte benutze /model add/remove/get <ID> §c||§6 /model open §c||§6 /model search/spieler <Name>!");
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (args.length == 1) {
							RemoveModelPlayer.add(p);
							p.sendMessage("§aKlicke auf den Model den du Löschen möchtest!");
							p.openInventory(getModelInv(1));
							ModelPage.put(p, 1);
						} else
							p.sendMessage(
									"§cBitte benutze /model add/remove/get <ID> §c||§6 /model open §c||§6 /model search/spieler <Name>!");
					} else if (args[0].equalsIgnoreCase("open")) {
						if (Main.ModelInv.size() != 0) {
							p.sendMessage("§5An dieser Stelle:");
							p.sendMessage("§dVIELEN DANK FÜR DAS EINRICHTEN DER GANZEN WOHNUNGEN! <3");
							p.openInventory(getModelInv(1));
							ModelPage.put(p, 1);
						} else
							p.sendMessage("§cEs wurden keine Model hinzugefügt!");
					} else if (args[0].equalsIgnoreCase("search")) {
						if (args.length == 2) {
							p.openInventory(getSearchInv(1, args[1]));
							ModelPage.put(p, 1);
							searchMap.put(p, args[1]);
						} else
							p.sendMessage(
									"§cBitte benutze /model add/remove/get <ID> §c||§6 /model open §c||§6 /model search/spieler <Name>!");
					} else if (args[0].equalsIgnoreCase("searchadd")) {
						if (!Main.ModelSearch.contains(args[1])) {
							SQLData.addModelSearch(args[1]);
							p.sendMessage("§6Suchbegriff wurde hinzugefügt");
						} else
							p.sendMessage("§cDiesen Suchbegriff gibt es schon");
					} else if (args[0].equalsIgnoreCase("searchremove")) {
						if (Main.ModelSearch.contains(args[1])) {
							SQLData.removeModelSearch(args[1]);
							p.sendMessage("§6Suchbegriff wurde entfernt");
						} else
							p.sendMessage("§cDiesen Suchbegriff gibt es nicht");
					} else if (args[0].equalsIgnoreCase("get")) {
						int customModel = Integer.parseInt(args[1]);
						ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("§6Model");
						meta.setCustomModelData(customModel);
						item.setItemMeta(meta);
						p.getInventory().addItem(item);
						p.sendMessage("§6Du hast ein Model erhalten");
					} else
						p.sendMessage(
								"§cBitte benutze /model add/remove/get <ID> §c||§6 /model open §c||§6 /model search/spieler <Name>!");
				} else
					p.sendMessage(
							"§cBitte benutze /model add/remove/get <ID> §c||§6 /model open §c||§6 /model search/spieler <Name>!");

			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	@EventHandler
	public void onKlickInv(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§eModel")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					ModelPage.put(p, ModelPage.get(p) + 1);
					if (RemoveModelPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getModelInv(ModelPage.get(p)));
						RemoveModelPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getModelInv(ModelPage.get(p)));
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					ModelPage.put(p, ModelPage.get(p) - 1);
					if (RemoveModelPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getModelInv(ModelPage.get(p)));
						RemoveModelPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getModelInv(ModelPage.get(p)));
					}
				} else if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
					if (!RemoveModelPlayer.contains(p)) {
						ItemStack piuck = new ItemStack(Material.HEART_OF_THE_SEA);
						ItemMeta pickm = e.getCurrentItem().getItemMeta();
						pickm.setDisplayName(" ");
						piuck.setItemMeta(pickm);
						p.getInventory().addItem(piuck);
					} else {
						Main.ModelInv.remove(e.getCurrentItem().getItemMeta().getDisplayName());
						for (String current : Main.ModelInv) {
							if (ModelName.get(current).equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
								RemoveModelPlayer.remove(p);
								SQLData.removeModel(current);
								p.sendMessage("§aDu hast erfolgreich ein Model entfernt");
								p.openInventory(getModelInv(ModelPage.get(p)));
								
							}
						}
					}

				}

				e.setCancelled(true);
			} else if (e.getView().getTitle().equals("§eModel die den Suchbegriff enthalten")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					ModelPage.put(p, ModelPage.get(p) + 1);
					if (RemoveModelPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getSearchInv(ModelPage.get(p), searchMap.get(p)));
						RemoveModelPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getSearchInv(ModelPage.get(p), searchMap.get(p)));
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					ModelPage.put(p, ModelPage.get(p) - 1);
					if (RemoveModelPlayer.contains(p)) {
						p.closeInventory();
						p.openInventory(getSearchInv(ModelPage.get(p), searchMap.get(p)));
						RemoveModelPlayer.add(p);
					} else {
						p.closeInventory();
						p.openInventory(getSearchInv(ModelPage.get(p), searchMap.get(p)));
					}
				} else if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
					if (!RemoveModelPlayer.contains(p)) {
						ItemStack piuck = new ItemStack(Material.HEART_OF_THE_SEA);
						ItemMeta pickm = e.getCurrentItem().getItemMeta();
						pickm.setDisplayName(" ");
						piuck.setItemMeta(pickm);
						p.getInventory().addItem(piuck);
					}
				}

				e.setCancelled(true);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	@EventHandler
	public void onCloseInv(InventoryClickEvent e) {
		if (RemoveModelPlayer.contains(e.getWhoClicked())) {
			RemoveModelPlayer.remove(e.getWhoClicked());
		}
	}

	public static Inventory getModelInv(int page) {
		int i;
		if (Main.ModelInv.size() <= 45) {

			for (i = 0; i * 9 < Main.ModelInv.size(); i++) {
			}

			Inventory ModelInv = Bukkit.createInventory(null, i * 9, "§eModel");

			for (String current : Main.ModelInv) {
				int a = Integer.parseInt(current);
				ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ModelName.get(current));
				meta.setCustomModelData(a);
				item.setItemMeta(meta);
				ModelInv.addItem(item);
			}
			return ModelInv;
		} else {
			int startslot = (page - 1) * 9 * 5;
			double size = Main.ModelInv.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory ModelInv = Bukkit.createInventory(null, i * 9, "§eModel");
			i = 0;
			boolean finish = false;
			for (int l = 0; l < ModelInv.getSize() - 9 && l + startslot < Main.ModelInv.size(); l++) {
				int a = Integer.parseInt(Main.ModelInv.get(l + startslot));
				ItemStack skull1 = new ItemStack(Material.HEART_OF_THE_SEA);
				ItemMeta skull1Meta = skull1.getItemMeta();
				skull1Meta.setDisplayName(ModelName.get(Main.ModelInv.get(l + startslot)));
				skull1Meta.setCustomModelData(a);
				skull1.setItemMeta(skull1Meta);
				skull1.setAmount(1);
				ModelInv.setItem(l, skull1);
			}
			if (page * 36 >= Main.ModelInv.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					ModelInv.setItem(ModelInv.getSize() - 5, skull1);
				}
			} else {

				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				ModelInv.setItem(ModelInv.getSize() - 6, skull2);
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					ModelInv.setItem(ModelInv.getSize() - 4, skull1);
				}
			}
			return ModelInv;
		}

	}

	public static Inventory getSearchInv(int page, String search) {
		int i;
		List<String> searched = new ArrayList<String>();
		for (String current : Main.ModelInv) {
			if (ModelName.get(current).toLowerCase().contains(search.toLowerCase())) {
				searched.add(current);
			}
		}
		if (searched.size() <= 45) {
			if (searched.size() > 0) {
				for (i = 0; i * 9 < searched.size(); i++) {
				}
				Inventory ModelInv = Bukkit.createInventory(null, i * 9, "§eModel die den Suchbegriff enthalten");

				for (String current : searched) {
					int a = Integer.parseInt(current);
					ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ModelName.get(current));
					meta.setCustomModelData(a);
					item.setItemMeta(meta);
					ModelInv.addItem(item);
				}
				return ModelInv;
			} else {
				Inventory ModelInv = Bukkit.createInventory(null, 9, "§eModel die den Suchbegriff enthalten");
				return ModelInv;
			}
		} else {
			int startslot = (page - 1) * 9 * 5;
			double size = searched.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory ModelInv = Bukkit.createInventory(null, i * 9, "§eModel die den Suchbegriff enthalten");
			i = 0;
			boolean finish = false;

			for (int l = 0; l < ModelInv.getSize() - 9 && l + startslot < searched.size(); l++) {
				ItemStack skull1 = new ItemStack(Material.HEART_OF_THE_SEA);
				ItemMeta skull1Meta = skull1.getItemMeta();
				int a = Integer.parseInt(searched.get(l + startslot));
				skull1Meta.setDisplayName(ModelName.get(searched.get(l + startslot)) + "");
				skull1Meta.setCustomModelData(a);
				skull1.setItemMeta(skull1Meta);
				skull1.setAmount(1);
				ModelInv.setItem(l, skull1);

			}
			if (page * 36 >= Main.ModelInv.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					ModelInv.setItem(ModelInv.getSize() - 5, skull1);
				}
			} else {

				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				ModelInv.setItem(ModelInv.getSize() - 6, skull2);
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					ModelInv.setItem(ModelInv.getSize() - 4, skull1);
				}
			}
			return ModelInv;
		}

	}

}
