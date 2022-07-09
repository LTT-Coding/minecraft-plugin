package de.ltt.staat.police.grundSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;

public class SetPolicePay implements Listener{

	
	public static HashMap<Player, OfflinePlayer> pPayset = new HashMap<Player, OfflinePlayer>();
	public static HashMap<Player, Integer> rPayset = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> pages = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> GeldInt = new HashMap<Player, Integer>();
	public static boolean pRightsetter;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§1Wähle einen Modus") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				Player p = (Player) e.getWhoClicked();
				PoliceInfo mi = new PoliceInfo();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Bezahlungsstufen einstellen")) {
					if(mi.hasRight(p, PoliceRights.SETRANKPAY)) {
						Inventory inv = Bukkit.createInventory(null, 9*2, "§1Wähle eine Stufe");
						
						ItemStack skull1 = new ItemBuilder("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc").setName("§7 1").setLore(mi.getPayRanks().get(1) + "€").build();

						ItemStack skull2 = new ItemBuilder("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a").setName("§7 2").setLore(mi.getPayRanks().get(2) + "€").build();

						ItemStack skull3 = new ItemBuilder("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2").setName("§7 3").setLore(mi.getPayRanks().get(3) + "€").build();

						ItemStack skull4 = new ItemBuilder("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058").setName("§7 4").setLore(mi.getPayRanks().get(4) + "€").build();

						ItemStack skull5 = new ItemBuilder("http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763").setName("§7 5").setLore(mi.getPayRanks().get(5) + "€").build();

						ItemStack skull6 = new ItemBuilder("http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1").setName("§7 6").setLore(mi.getPayRanks().get(6) + "€").build();
						
						ItemStack skull7 = new ItemBuilder("http://textures.minecraft.net/texture/297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb").setName("§7 7").setLore(mi.getPayRanks().get(7) + "€").build();

						ItemStack skull8 = new ItemBuilder("http://textures.minecraft.net/texture/abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d").setName("§7 8").setLore(mi.getPayRanks().get(8) + "€").build();
						
						ItemStack skull0 = new ItemBuilder("http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5").setName("§7 0").setLore(mi.getPayRanks().get(0) + "€").build();
						
						ItemStack skullreset = new ItemBuilder("http://textures.minecraft.net/texture/c4e490e1658bfde4d4ef1ea7cd646c5353377905a1369b86ee966746ae25ca7").setName("§4Zurücksetzen").build();
						
						ItemStack skullincrease = new ItemBuilder("http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7")
								.setName("§6Hochsetzen").setLore("Alle Stufen um einen Betrag hochsetzen").build();
						
						ItemStack skulldecrease = new ItemBuilder("http://textures.minecraft.net/texture/c3e4b533e4ba2dff7c0fa90f67e8bef36428b6cb06c45262631b0b25db85b")
								.setName("§6Runtersetzen").setLore("Alle Stufen um einen Betrag runtersetzen").build();
						
						inv.setItem(0, skull0);
						inv.setItem(1, skull1);
						inv.setItem(2, skull2);
						inv.setItem(3, skull3);
						inv.setItem(4, skull4);
						inv.setItem(5, skull5);
						inv.setItem(6, skull6);
						inv.setItem(7, skull7);
						inv.setItem(8, skull8);
						inv.setItem(13, skullreset);
						inv.setItem(14, skullincrease);
						inv.setItem(12, skulldecrease);
						p.openInventory(inv);
					}else
						p.sendMessage(Main.KEINE_RECHTE);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Bezahlung einzeln einstellen")) {
					if(mi.hasRight(p, PoliceRights.SETPLAYERPAY)) {
						pages.put(p, 1);
						p.openInventory(getPlayerInv(1));
					}else
						p.sendMessage(Main.KEINE_RECHTE);
				}
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§1Wähle eine Person!") && e.getView().getTopInventory() == e.getClickedInventory()) {
				Player p = (Player) e.getWhoClicked();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					pages.put(p, pages.get(p) + 1);
					p.openInventory(getPlayerInv(pages.get(p)));
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					pages.put(p, pages.get(p) - 1);
					p.openInventory(getPlayerInv(pages.get(p)));
				} else {
					PoliceInfo mi = new PoliceInfo();
					String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
					OfflinePlayer t = Bukkit.getOfflinePlayer(name);
					if (mi.getMember().contains(t.getUniqueId().toString())) {
						Inventory inv = Bukkit.createInventory(null, 9 * 5, "§1Wähle die Bezahlung für diese Person");
						numpadinv(inv);
						p.openInventory(inv);
						pPayset.put(p, t);
					} else
						p.sendMessage("§cDiese Person arbeitet nicht mehr bei der §6Polizei");
				}
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§1Wähle eine Stufe") && e.getView().getTopInventory() == e.getClickedInventory()) {
				Player p = (Player) e.getWhoClicked();
				PoliceInfo mi = new PoliceInfo();
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Zurücksetzen")) {
					List<Integer> payList = new ArrayList<Integer>();
					payList.add(10);
					payList.add(100);
					payList.add(150);
					payList.add(200);
					payList.add(250);
					payList.add(250);
					payList.add(300);
					payList.add(350);
					payList.add(400);
					mi.setPayRanks(payList);
					p.closeInventory();
					p.sendMessage("§aBezahlungen wurden zurückgesetzt!");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Hochsetzen")) {
					Inventory inv = Bukkit.createInventory(null, 9*5, "§7[§6Hochsetzen§7]§1 Wähle einen Betrag");
					numpadinv(inv);
					p.openInventory(inv);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Runtersetzen")) {
					Inventory inv = Bukkit.createInventory(null, 9*5, "§7[§6Runtersetzen§7]§1 Wähle einen Betrag"); 
					numpadinv(inv);
					p.openInventory(inv);
				}else {
					Inventory inv = Bukkit.createInventory(null, 9*5, "§1Wähle eine Bezahlung für diese Stufe");
					numpadinv(inv);
					p.openInventory(inv);
					int rank = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7 ", ""));
					rPayset.put(p, rank);
				}
				e.setCancelled(true);
			}else if(e.getView().getTitle().equals("§1Die Polizei hat keine Mitarbeiter")) {
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}else if(e.getView().getTitle().equals("§7[§6Hochsetzen§7]§1 Wähle einen Betrag")
					|| e.getView().getTitle().equals("§7[§6Runtersetzen§7]§1 Wähle einen Betrag")
					|| e.getView().getTitle().equals("§1Wähle eine Bezahlung für diese Stufe")
					|| e.getView().getTitle().equals("§1Wähle die Bezahlung für diese Person")) {
				if(e.getView().getTopInventory().equals(e.getClickedInventory())) {
					e.setCancelled(true);
					Player p = (Player) e.getWhoClicked();
					PoliceInfo mi = new PoliceInfo();
					int geld = 0;
					if(GeldInt.containsKey(p))geld = GeldInt.get(p);
					if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§2Akzeptieren")) {
						if(e.getView().getTitle().equals("§1Wähle die Bezahlung für diese Person")) {
							OfflinePlayer t = pPayset.get(p);
							mi.setPayPlayer(t, geld);
						}else if(e.getView().getTitle().equals("§1Wähle eine Bezahlung für diese Stufe")) {
							List<Integer> payList = mi.getPayRanks();
							int rank = rPayset.get(p);
							payList.set(rank, geld);
							mi.setPayRanks(payList);
							p.sendMessage("§aDie Bezahlung wurde geändert!");
						}else if(e.getView().getTitle().equals("§7[§6Runtersetzen§7]§1 Wähle einen Betrag")) {
							List<Integer> payList = mi.getPayRanks();
							for(int i = 0; i < payList.size(); i++) {
								if(payList.get(i) - geld >= 0) {
									payList.set(i, payList.get(i) - geld);
								}else 
									payList.set(i, 0);
							}
							mi.setPayRanks(payList);
							p.sendMessage("§aAlle Bezahlungen wurden runtergesetzt!");
						}else if(e.getView().getTitle().equals("§7[§6Hochsetzen§7]§1 Wähle einen Betrag")) {
							List<Integer> payList = mi.getPayRanks();
							for(int i = 0; i < payList.size(); i++) {
								if(payList.get(i) + geld <= Integer.MAX_VALUE) {
									payList.set(i, payList.get(i) + geld);
								}else 
									payList.set(i, Integer.MAX_VALUE);
							}
							mi.setPayRanks(payList);
							p.sendMessage("§aAlle Bezahlungen wurden hochgesetzt!");
						}
						GeldInt.remove(p);
						p.closeInventory();
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§1Löschen")) {
						geld = 0;
						GeldInt.put(p, 0);
						for(int i = 0; i < 9; i++) {
							e.getInventory().setItem(i, null);
						}
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Abbrechen")) {
						p.closeInventory();
						GeldInt.remove(p);
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
						
					}else{
						int length = 0;
						if(geld != 0)length = String.valueOf(geld).length();
						if(length >= 9)return;
						int klicked = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7 ", ""));
						e.getInventory().setItem(length, e.getCurrentItem());
						geld = geld*10 + klicked;
						GeldInt.put(p, geld);
						
					}
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public Inventory getPlayerInv(int page) {
		PoliceInfo mi = new PoliceInfo();
		int i;
		if(mi.getMember().size() <= 45) {
			for(i = 0; i*9 < mi.getMember().size(); i++) {}
			if(i == 0) {
				Inventory inv = Bukkit.createInventory(null, 9, "§1Die Polizei hat keine Mitarbeiter");
				final ItemStack black = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
				for(int s = 0; s < 9; s++) {
					inv.setItem(s, black);
				}
				return inv;
			}
			Inventory inv = Bukkit.createInventory(null, i*9, "§1Wähle eine Person!");
			for(String current : mi.getMember()) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(current));
				ItemStack item = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				meta.setOwner(t.getName());
				meta.setDisplayName("§6" + t.getName());
				if(mi.getPayPlayer(t) == 0) {
					meta.setLore(Arrays.asList("§cKeine individuelle Bezahlung"));
				}else{
					meta.setLore(Arrays.asList( mi.getPayPlayer(t) + "$"));
				}
				item.setItemMeta(meta);
				inv.addItem(item);
			}
			return inv;
		}else {
			int startslot = (page-1)*9*5;
			double size = mi.getMember().size() - startslot;
			for(i = 0; i*9 < size && i < 5; i++) {}
			i++;
			Inventory inv = Bukkit.createInventory(null, i*9, "§1Wähle einen Spieler!");
			i = 0;
			boolean finish = false;
			for(int l = 0; l < inv.getSize() - 9 && l + startslot < mi.getMember().size(); l++) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(mi.getMember().get(l + startslot)));
				ItemStack item = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				meta.setOwner(t.getName());
				meta.setDisplayName("§6" + t.getName());
				meta.setLore(Arrays.asList( mi.getPayPlayer(t) + "$"));
				item.setItemMeta(meta);
				inv.setItem(l, item);
			}
			if(page*36 >= mi.getMember().size()) finish = true;
			if(page == 1) {
				if(!finish) {
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
	}
	
	public void numpadinv(Inventory Inv) {

		ItemStack skull1 = new ItemBuilder("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc").setName("§7 1").build();

		ItemStack skull2 = new ItemBuilder("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a").setName("§7 2").build();

		ItemStack skull3 = new ItemBuilder("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2").setName("§7 3").build();

		ItemStack skull4 = new ItemBuilder("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058").setName("§7 4").build();

		ItemStack skull5 = new ItemBuilder("http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763").setName("§7 5").build();

		ItemStack skull6 = new ItemBuilder("http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1").setName("§7 6").build();
		
		ItemStack skull7 = new ItemBuilder("http://textures.minecraft.net/texture/297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb").setName("§7 7").build();

		ItemStack skull8 = new ItemBuilder("http://textures.minecraft.net/texture/abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d").setName("§7 8").build();

		ItemStack skull9 = new ItemBuilder("http://textures.minecraft.net/texture/d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba").setName("§7 9").build();

		ItemStack skull0 = new ItemBuilder("http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5").setName("§7 0").build();

		ItemStack Del = new ItemBuilder(Material.RED_CONCRETE).setName("§6Abbrechen").build();
		
		ItemStack ACC = new ItemBuilder(Material.LIME_CONCRETE).setName("§2Akzeptieren").build();
		
		ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();

		ItemStack Löschen = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4Löschen").build();

		Inv.setItem(12, skull7);
		Inv.setItem(13, skull8);
		Inv.setItem(14, skull9);
		Inv.setItem(21, skull4);
		Inv.setItem(22, skull5);
		Inv.setItem(23, skull6);
		Inv.setItem(30, skull1);
		Inv.setItem(31, skull2);
		Inv.setItem(32, skull3);
		Inv.setItem(40, skull0);
		Inv.setItem(37, Del);
		Inv.setItem(43, ACC);
		Inv.setItem(9, Glas);
		Inv.setItem(10, Glas);
		Inv.setItem(11, Glas);
		Inv.setItem(15, Glas);
		Inv.setItem(16, Glas);
		Inv.setItem(17, Glas);
		Inv.setItem(18, Glas);
		Inv.setItem(19, Glas);
		Inv.setItem(20, Glas);
		Inv.setItem(24, Glas);
		Inv.setItem(25, Löschen);
		Inv.setItem(26, Glas);
		Inv.setItem(27, Glas);
		Inv.setItem(28, Glas);
		Inv.setItem(29, Glas);
		Inv.setItem(33, Glas);
		Inv.setItem(34, Glas);
		Inv.setItem(35, Glas);
		Inv.setItem(36, Glas);
		Inv.setItem(38, Glas);
		Inv.setItem(39, Glas);
		Inv.setItem(41, Glas);
		Inv.setItem(42, Glas);
		Inv.setItem(44, Glas);
	}
}
