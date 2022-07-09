package de.ltt.staat.police.policeComputer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.staat.police.grundSystem.PoliceInfo;
import de.ltt.staat.police.grundSystem.PoliceRights;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenBook;

public class ClickOnComputer implements Listener {

	int PolizeiKarte = 100000009;
	public static HashMap<Player, Integer> AktenPage = new HashMap<Player, Integer>();
	public static HashMap<Player, ItemStack> openAkte = new HashMap<Player, ItemStack>();
	public static HashMap<Player, String> addAkte = new HashMap<Player, String>();
	public static HashMap<Player, Integer> CharAktenPage = new HashMap<Player, Integer>();
	public static HashMap<Player, OfflinePlayer> CharAktenName = new HashMap<Player, OfflinePlayer>();
	public static HashMap<Player, ItemStack> ItemInHand = new HashMap<Player, ItemStack>();

	String URL = "[Hier Link]";

	Material ortungItem = Material.PAPER;
	Material aktenItem = Material.PAPER;
	Material registerItem = Material.PAPER;
	Material karteItem = Material.PAPER;
	Material autoItem = Material.PAPER;
	Material handyItem = Material.PAPER;
	Material CategoryItem = Material.PAPER;

	@EventHandler
	public void onClickComputer(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (Main.PoliceComputerLoc.contains(e.getClickedBlock().getLocation())) {
				try {
					if (e.getPlayer().getItemInHand().getType() == Material.HEART_OF_THE_SEA
							&& e.getItem().getItemMeta().getCustomModelData() == PolizeiKarte) {
						Player p = e.getPlayer();

						String openPC = "§6" + p.getName() + " hat grade den Polizei Computer geöffnet";

						Inventory inv = Bukkit.createInventory(null, 1 * 9, "§1Polizei Computer");

						ItemStack ortung = new ItemBuilder(ortungItem).setName("§6Ortung").build();
						ItemStack karte = new ItemBuilder(karteItem).setName("§6Karte").build();
						ItemStack akten = new ItemBuilder(aktenItem).setName("§6Akten").build();
						ItemStack register = new ItemBuilder(registerItem).setName("§6Register").build();
						ItemStack glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();

						inv.setItem(1, karte);
						inv.setItem(3, ortung);
						inv.setItem(5, register);
						inv.setItem(7, akten);

						for (int i = 0; i < 1 * 9; i++) {
							if (i != 1 && i != 3 && i != 7 && i != 5) {
								inv.setItem(i, glas);
							}
						}

						p.openInventory(inv);
						Main.BroadcastLoc(p.getLocation(), 4, openPC);
					}
				} catch (Exception e2) {
				}
			}
		}
	}

	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTitle().equals("§1Polizei Computer")
					&& e.getInventory() == e.getView().getTopInventory()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Ortung")) {
					Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§1Ortungs Wahl");

					ItemStack auto = new ItemBuilder(autoItem).setName("§6Auto").setLore("§5Soon").build();
					ItemStack handy = new ItemBuilder(handyItem).setName("§6Handy").build();
					ItemStack glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();

					inv.setItem(1, auto);
					inv.setItem(3, handy);

					for (int i = 0; i < 1 * 9; i++) {
						if (i != 1 && i != 3) {
							inv.setItem(i, glas);
						}
					}

					p.openInventory(inv);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Karte")) {
					p.sendMessage("§6Hier ist der Link zur Karte: " + URL);
					p.closeInventory();
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Akten")) {
					p.closeInventory();
					p.openInventory(getAktenInv(1));
					AktenPage.put(p, 1);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Register")) {

				}

				e.setCancelled(true);

			} else if (e.getView().getTitle().equals("§1Akten") && e.getInventory() == e.getView().getTopInventory()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					AktenPage.put(p, AktenPage.get(p) + 1);
					p.closeInventory();
					p.openInventory(getAktenInv(AktenPage.get(p)));
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					AktenPage.put(p, AktenPage.get(p) - 1);
					p.closeInventory();
					p.openInventory(getAktenInv(AktenPage.get(p)));
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eSuche")) {
					// TODO: SOON Suche
					p.sendMessage("§5Soon");
				} else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					PlayerInfo pi = new PlayerInfo(
							Main.getOfflineChar(e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")));
					if (CharAktenName.containsKey(p))
						CharAktenName.remove(p);
					CharAktenName.put(p,
							Main.getOfflineChar(e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "")));
					p.closeInventory();
					p.openInventory(pi.openAkteInv(1));
					CharAktenPage.put(p, 1);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAkte hinzufügen")) {
					p.closeInventory();
					/* Akte Hinzufügen */ if (p.getItemInHand() != null) {
						ItemInHand.put(p, p.getItemInHand());
						p.setItemInHand(new ItemBuilder(Material.AIR).build());
					}
					p.setItemInHand(new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Akte").build());
					p.sendMessage("§eÖffne das Buch um eine Akte zu schreiben");
					p.sendMessage("§eWenn du fertig bist drücke auf Signieren und gebe ein Title an");
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().contains("§1Akten von ")
					&& e.getInventory() == e.getView().getTopInventory()) {
				PlayerInfo pi = new PlayerInfo(CharAktenName.get(p));
				PoliceInfo PI = new PoliceInfo();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Nächste Seite")) {
					CharAktenPage.put(p, CharAktenPage.get(p) + 1);
					p.closeInventory();
					p.openInventory(pi.openAkteInv(CharAktenPage.get(p)));
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Vorherige Seite")) {
					CharAktenPage.put(p, CharAktenPage.get(p) - 1);
					p.closeInventory();
					p.openInventory(pi.openAkteInv(CharAktenPage.get(p)));
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eSuche")) {
					// TODO: SOON Suche
					p.sendMessage("§5Soon");
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eFilter")) {
					// TODO: SOON Filter
					p.sendMessage("§5Soon");
				} else if (e.getCurrentItem().getType() == Material.BOOK) {
					p.closeInventory();
					if (PI.hasRight(p, PoliceRights.AKTELESEN)) {
						if (PI.hasRight(p, PoliceRights.AKTEBEARBEITEN) || PI.hasRight(p, PoliceRights.AKTELÖSCHEN)) {
							Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§1Akten Optionen");

							inv.setItem(0, new ItemBuilder(Material.BOOK).setName("§6Lesen").build());
							if (PI.hasRight(p, PoliceRights.AKTELÖSCHEN))
								inv.setItem(4, new ItemBuilder(Material.BARRIER).setName("§cLöschen").build());
							if (PI.hasRight(p, PoliceRights.AKTEBEARBEITEN))
								inv.setItem(2, new ItemBuilder(Material.LEGACY_BOOK_AND_QUILL).setName("§6Bearbeiten")
										.build());
							openAkte.put(p, e.getCurrentItem());
							p.openInventory(inv);
						} else
							p.openBook(e.getCurrentItem());
					} else
						p.sendMessage("§cZugriff verweigert");
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAkte hinzufügen")) {
					
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().contains("§1Akten Optionen")
					&& e.getInventory() == e.getView().getTopInventory()) {
				PlayerInfo pi = new PlayerInfo(CharAktenName.get(p));
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Lesen")) {
					p.closeInventory();
					p.openBook(openAkte.get(p));
					openAkte.remove(p);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Bearbeiten")) {
					// TODO: Soon
					p.sendMessage("§6Soon");
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cLöschen")) {
					pi.removeAkte(openAkte.get(p).getItemMeta().getDisplayName());
					p.closeInventory();
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {
		}

	}

	@EventHandler
	public void closeBook(PlayerEditBookEvent e) {
		if (e.isSigning()) {
			e.getPlayer().sendMessage("japlol");
		} else {
			PlayerInfo pi = new PlayerInfo(CharAktenName.get(e.getPlayer()));
			PlayerInfo piCreate = new PlayerInfo(e.getPlayer());
			// pi.addAkte(e.getPreviousBookMeta().getPages(), piCreate.getFullName());
		}

	}

	@EventHandler
	public void closeBook(PlayerDropItemEvent e) {
		if (!Main.Admin.contains(e.getPlayer().getUniqueId().toString()))
			if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6Akte"))
				e.setCancelled(true);
	}

	public static Inventory getAktenInv(int page) {
		int i;
		if (Main.PersonenAkten.size() <= 45) {
			for (i = 0; i * 9 < Main.PersonenAkten.size(); i++) {
			}

			Inventory wahlInv = Bukkit.createInventory(null, (i * 9) + 18, "§1Akten");

			for (int j = 0; j <= 8; j++) {
				if (j == 7) {
					ItemStack Suche = new ItemBuilder(Material.LEVER).setName("§eSuche").build();
					wahlInv.setItem(7, Suche);
				} else if (j == 6) {
					ItemStack Filter = new ItemBuilder(Material.HOPPER).setName("§eFilter").build();
					wahlInv.setItem(6, Filter);
				} else {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				}
			}

			for (String current : Main.PersonenAkten) {
				ItemStack Kopf = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta KopfM = (SkullMeta) Kopf.getItemMeta();
				KopfM.setDisplayName("§6" + current);
				Kopf.setItemMeta(KopfM);
				wahlInv.addItem(Kopf);
			}

			for (int j = (i * 9 + 18) - 9; j < (i * 9 + 18); j++) {
				if (j != (i * 9 + 18) - 1) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				} else {
					ItemStack Glas = new ItemBuilder(Material.GREEN_CONCRETE).setName("§aAkte hinzufügen").build();
					wahlInv.setItem(j, Glas);
				}
			}
			return wahlInv;
		} else {
			int startslot = ((page - 1) * 9 * 5) + 18;
			double size = Main.PersonenAkten.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory wahlInv = Bukkit.createInventory(null, i * 9, "§1Akten");
			i = 0;
			boolean finish = false;
			for (int l = 0; l < wahlInv.getSize() - 9 && l + startslot < Main.PersonenAkten.size(); l++) {
				ItemStack Kopf = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta KopfM = (SkullMeta) Kopf.getItemMeta();
				KopfM.setDisplayName("§6" + Main.PersonenAkten.get(l + startslot));
				Kopf.setItemMeta(KopfM);
				wahlInv.setItem(l, Kopf);
			}
			for (int j = (i * 9 + 18) - 9; j < (i * 9 + 18); j++) {
				ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
				wahlInv.setItem(j, Glas);
			}
			if (page * 36 >= Main.PersonenAkten.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					for (int j = 0; j <= 8; j++) {
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
						wahlInv.setItem(j, Glas);
					}
					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					wahlInv.setItem(wahlInv.getSize() - 5, skull1);

					for (int j = 0; j < 8; j++) {
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
						wahlInv.setItem(j, Glas);
					}
				}
			} else {

				for (int j = 0; j <= 8; j++) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				}
				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				wahlInv.setItem(wahlInv.getSize() - 6, skull2);
				for (int j = 0; j < 8; j++) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
					wahlInv.setItem(j, Glas);
				}
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					wahlInv.setItem(wahlInv.getSize() - 4, skull1);
				}
			}
			return wahlInv;
		}

	}

}
