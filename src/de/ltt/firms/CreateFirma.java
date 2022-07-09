package de.ltt.firms;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
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
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.FirmTypes;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Roots;

public class CreateFirma implements CommandExecutor, Listener{
	private HashMap<Player, String> name = new HashMap<Player, String>();
	private HashMap<Player, FirmTypes> firmtypes= new HashMap<Player, FirmTypes>();
	private HashMap<Player, ItemStack> klickedItems = new HashMap<Player, ItemStack>();
	double price;
	ItemStack klickedItem;
	String firmname;
	FirmTypes firmtype;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			PlayerInfo pi = new PlayerInfo(player);
			double money = pi.getMoney();
			if(Main.getPlugin().getConfig().get("Staat.firmprice") != null) {
				price = Main.getPlugin().getConfig().getDouble("Staat.firmprice");
			}else {
				price = 100;
				Main.getPlugin().getConfig().set("Staat.firmprice", price);
				Main.getPlugin().saveConfig();
			}
			if(pi.getJob() == 0) {
				if(money >= price) {
					Inventory firmselect = Bukkit.createInventory(null, 9*2, "§eWähle einen Firmentyp");
					firmselect.setItem(7, new ItemBuilder(Material.IRON_DOOR).setName("§6Maklerbüro").setLore("§a+ Hauskauf/-verkauf", "§a+ Makler", "§c- gekaufte Häuser nicht bewohnbar").build());
					firmselect.setItem(8, new ItemBuilder(Material.PAPER).setName("§6Bürofirma").setLore("§a+ Kann Dauerverträge mit Firmen und Spielern schließen", "§c- keine besondere Spezifikation").build());
					firmselect.setItem(11, new ItemBuilder(Material.BREAD).setName("§6Minimarkt").setLore("§a+ Kann selbst Waren herstellen", "§c- Nur ein Firmengelände", "§c- nur bis zu 7 Mitarbeiter").build());
					firmselect.setItem(10, new ItemBuilder(Material.EMERALD).setName("§6Einzelhandel").setLore("§a+ Mehrere Firmengelände", "§c- Kann keine eigenen Waren herstellen").build());
					firmselect.setItem(12, new ItemBuilder(Material.ANVIL).setName("§6Weiterverarbeitung").setLore("§a+ Kann aus Verschiedenen Waren neue herstellen", "§c- Kann nur von Firmen kaufen und an Firmen Verkaufen").build());
					firmselect.setItem(16, new ItemBuilder(Material.IRON_PICKAXE).setName("§6Bergbau").setLore("§a+ Kann eigene Waren aus dem Berg generieren", "§c- Kann nur an Firmen verkaufen", "§c- Waren müssen zuerst weiterverarbeitet werden").build());
					firmselect.setItem(17, new ItemBuilder(Material.WHEAT).setName("§6Landwirtschaft").setLore("§a+ Kann eigene Waren aus der Landwirtschaft generieren", "§c- Kann nur an Firmen verkaufen", "§c- Waren müssen zuerst weiterverarbeitet werden").build());
					firmselect.setItem(3, new ItemBuilder(Material.LEGACY_CAULDRON_ITEM).setName("§6Müllentsorgung").setLore("§a+ Kann Müll bei Häusern abholen", "§a+ Kann Müll an Energieproduzenten verkaufen", "§c- Nur ein Firmengelände").build());
					firmselect.setItem(2, new ItemBuilder(Material.REDSTONE).setName("§6Energieproduzent").setLore("§a+ Kann Strom produzieren und Stromtarife festlegen", "§a+ Kann verschiedene Techniken zur Stromproduktion verwenden", "§a+ Produziert bestimmte Strommenge pro Stunde die Kunden verbrauchen können").build());
					firmselect.setItem(1, new ItemBuilder(Material.WATER_BUCKET).setName("§6Wasserversorgung").setLore("§a+ Kann Wasser aus Gewässern pumpen und Tarife festlegen", "§c- Braucht genehmigung für Wasserpumpen", "§c- Gelände zum Pumpen müssen am Wasser gelegen sein").build());
					firmselect.setItem(13, new ItemBuilder(Material.BRICK).setName("§6Handwerker").setLore("§a+ Kann Häuser aus gekauften Rohstoffen bauen", "§a+ Kann Grundstücke von Spielern zum bauen frigegeben bekommen", "§c- Muss Materialien selbst kaufen").build());
					
					ItemStack arr = ItemSkulls.getSkull("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta arrm = arr.getItemMeta();
					arrm.setDisplayName("§6Infrastruktur");
					arr.setItemMeta(arrm);
					firmselect.setItem(0, arr);
					arrm.setDisplayName("§6Handwerk");
					arr.setItemMeta(arrm);
					firmselect.setItem(9, arr);
					arrm.setDisplayName("§6Sonstiges");
					arr.setItemMeta(arrm);
					firmselect.setItem(6, arr);
					arrm.setDisplayName("§6Herstellung");
					arr.setItemMeta(arrm);
					firmselect.setItem(15, arr);
					player.openInventory(firmselect);
				}else
					player.sendMessage(Main.KEIN_GELD);
			}else if(pi.getJob() < 0) 
				player.sendMessage("§cDu arbeitest bereits beim Staat!");
			else
				player.sendMessage("§cDu arbeitest bereits in einer Firma!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	
	@EventHandler
	public void onInvKlick(InventoryClickEvent e) {
		try {
		if(e.getView().getTitle().equals("§eWähle einen Firmentyp")) {
			Player p = (Player) e.getWhoClicked();
				String name = e.getCurrentItem().getItemMeta().getDisplayName();
				if(name.equals("§6Maklerbüro")) {
					p.closeInventory();
					p.sendMessage("§eGebe den Namen der Firma ein:");
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.IMMOBILIENBÜRO;
				}else if(name.equals("§6Bürofirma")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.VERTRAGSFIRMA;
				}else if(name.equals("§6Minimarkt")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.MINIMARKT;
				}else if(name.equals("§6Einzelhandel")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.EINZELHANDEL;
				}else if(name.equals("§6Weiterverarbeitung")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.WEITERVERARBEITUNG; 
				}else if(name.equals("§6Bergbau")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.BERGBAU;
				}else if(name.equals("§6Landwirtschaft")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.LANDWIRTSCHAFT;
				}else if(name.equals("§6Müllentsorgung")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.MÜLLENTSORGUNG;
				}else if(name.equals("§6Energieproduzent")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.ENERGIEPRODUZENT;
				}else if(name.equals("§6Wasserversorgung")) {
					p.sendMessage("§eGebe den Namen der Firma ein:");
					p.closeInventory();
					klickedItem = e.getCurrentItem();
					firmtype = FirmTypes.WASSERVERSORGUNG;
				}
				ChatInfo.addChat(p, ChatType.FIRMNAME, 600);
				firmtypes.put(p, firmtype);
				klickedItems.put(p, klickedItem);
				e.setCancelled(true);
			
		}else if(e.getView().getTitle().equalsIgnoreCase("§eErstellen der Firma bestätigen...")) {
			Player p = (Player) e.getWhoClicked();
			PlayerInfo pi = new PlayerInfo(p);
			try {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aFirma erstellen")) {
					new FirmInfo().addfirm(name.get(p), firmtypes.get(p), p.getUniqueId().toString());
					pi.subtractMoney(Main.getPlugin().getConfig().getDouble("Staat.firmprice"));
					e.getWhoClicked().closeInventory();
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVorgang abbrechen")) {
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().sendMessage("§4Vorgang abgebrochen!");
				}
				e.setCancelled(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		} catch (Exception e2) {
			
		}
	}
	
	@EventHandler
	public void ChatMessage(PlayerChatEvent e) {
		if(ChatInfo.getChat(e.getPlayer()).equals(ChatType.FIRMNAME)) {
			ChatInfo.removeChat(e.getPlayer());
			firmname = e.getMessage();
			name.put(e.getPlayer(), firmname);
			e.getPlayer().sendMessage("§eName: " + e.getMessage());
			e.getPlayer().sendMessage("§eVorgang wird bearbeitet ...");
			e.getPlayer().sendTitle("§aVorgang wird bearbeitet", "§eBitte Warten", 10, 75, 10);
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e1) {
			}
			Inventory createFirma = Bukkit.createInventory(null, 9, "§eErstellen der Firma bestätigen...");
			createFirma.setItem(0, new ItemBuilder(Material.LIME_WOOL).setName("§aFirma erstellen").setLore("§e Infos zur Firma->").build());
			createFirma.setItem(3, new ItemBuilder(Material.BOOK).setName("§eName der Firma").setLore("§a" + firmname).build());
			createFirma.setItem(4, klickedItems.get(e.getPlayer()));
			createFirma.setItem(5, new ItemBuilder(Material.EMERALD).setName("§aKostenaufwand").setLore("§e" + Main.getPlugin().getConfig().getDouble("Staat.firmprice") + "€").build());
			createFirma.setItem(8, new ItemBuilder(Material.RED_WOOL).setName("§cVorgang abbrechen").build());
			e.getPlayer().openInventory(createFirma);
			e.setCancelled(true);
		}
	}

}
