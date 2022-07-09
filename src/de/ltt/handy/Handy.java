package de.ltt.handy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Handy implements Listener {
	double geld;
	int block;
	double money;
	double moneyInHand;
	
	double kostenSMS = 0; //Kosten für senden SMS
	double kostenCall = 0; //Kosten für Anrufen
	double kostenBank = 0; //Kosten für Bank
	
	OfflinePlayer target;
	
	HashMap<String, ItemStack> numpad = new HashMap<String, ItemStack>();
	HashMap<Player, Player> spieler = new HashMap<Player, Player>();
	HashMap<Player, Double> geldInt = new HashMap<Player, Double>();
	HashMap<Player, String> betreff = new HashMap<Player, String>();
	HashMap<Player, String> smsBetreff = new HashMap<Player, String>();
	HashMap<Player, String> smsBetreffKontakt = new HashMap<Player, String>();
	HashMap<Player, String> KontaktsmsBetreff = new HashMap<Player, String>();
	HashMap<Player, Integer> kontaktNummer = new HashMap<Player, Integer>();
	HashMap<Player, OfflinePlayer> kontaktT = new HashMap<Player, OfflinePlayer>();
	HashMap<Player, OfflinePlayer> kontaktInt = new HashMap<Player, OfflinePlayer>();
	
	@EventHandler
	public void onHandy(PlayerInteractEvent e) {
		try {
			Player p = e.getPlayer();
			if (e.getItem() != null) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (e.getItem().getItemMeta().getCustomModelData() == 10000001 
							&& e.getItem().getItemMeta().getDisplayName().equals("§6Handy")
							&& e.getItem().getType().equals(Material.HEART_OF_THE_SEA)) {
						int guthaben = Main.getPlugin().getConfig()
								.getInt("Handy.Spieler." + p.getUniqueId() + ".guthaben");

						Inventory InvHandy = Bukkit.createInventory(null, 2 * 9, "§6Handy");

						ItemStack Bank = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/bf75d1b785d18d47b3ea8f0a7e0fd4a1fae9e7d323cf3b138c8c78cfe24ee59");
						ItemStack Apps = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777");
						ItemStack Guthaben = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/7dd7e6991e5167c51724fb5491cc79c0b6629d28bade607cb959556feabb9");
						ItemStack SMS = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/b4bd9dd128c94c10c945eadaa342fc6d9765f37b3df2e38f7b056dc7c927ed");
						ItemStack Call = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/82442bbf7171b5cafca217c9ba44ce27647225df76cda9689d61a9f1c0a5f176");
						ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
						
						try {
						   if(!Main.handyOnOff.containsKey(p.getUniqueId())) {
							   SQLData.setHandyOnOff(p.getUniqueId(), false);
								ItemStack On = ItemSkulls.getSkull(
										"http://textures.minecraft.net/texture/884e92487c6749995b79737b8a9eb4c43954797a6dd6cd9b4efce17cf475846");
								SkullMeta OnM = (SkullMeta) On.getItemMeta();
								OnM.setDisplayName("§4OFF");
								On.setItemMeta(OnM);
								InvHandy.setItem(13, On);
							} if(Main.handyOnOff.get(p.getUniqueId()).equals(true)) {
								ItemStack On = ItemSkulls.getSkull(
										"http://textures.minecraft.net/texture/5e48615df6b7ddf3ad495041876d9169bdc983a3fa69a2aca107e8f251f7687");
								SkullMeta OnM = (SkullMeta) On.getItemMeta();
								OnM.setDisplayName("§aON");
								On.setItemMeta(OnM);
								InvHandy.setItem(13, On);
							} else {
								ItemStack On = ItemSkulls.getSkull(
										"http://textures.minecraft.net/texture/884e92487c6749995b79737b8a9eb4c43954797a6dd6cd9b4efce17cf475846");
								SkullMeta OnM = (SkullMeta) On.getItemMeta();
								OnM.setDisplayName("§4OFF");
								On.setItemMeta(OnM);
								InvHandy.setItem(13, On);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						
						ItemStack Kontakt = new ItemStack(Material.PLAYER_HEAD, 1);
						SkullMeta KontaktMeta = (SkullMeta) Kontakt.getItemMeta();
						KontaktMeta.setOwner(p.getName());
						KontaktMeta.setDisplayName("§6Kontaktbuch");
						Kontakt.setItemMeta(KontaktMeta);
						Kontakt.setAmount(1);

						ItemMeta BankM = Bank.getItemMeta();
						ItemMeta GuthabenM = Guthaben.getItemMeta();
						ItemMeta SMSM = SMS.getItemMeta();
						ItemMeta CallM = Call.getItemMeta();
						ItemMeta AppsM = Apps.getItemMeta();
						ItemMeta Gs = Glas.getItemMeta();

						String Nummer;
						if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
							Nummer = Main.PlayerNummer.get(p.getUniqueId().toString()).toString();
						} else {
							Nummer = "§cKeine Sim Karte eingelegt";
						}

						BankM.setDisplayName("§cBank");
						GuthabenM.setDisplayName("§eNummer: " + Nummer);
						SMSM.setDisplayName("§eSMS");
						CallM.setDisplayName("§aAnrufen");
						AppsM.setDisplayName("§6Apps");
						Gs.setDisplayName(" ");

						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§eGuthaben: " + guthaben);

						GuthabenM.setLore(lore);

						Bank.setItemMeta(BankM);
						Guthaben.setItemMeta(GuthabenM);
						SMS.setItemMeta(SMSM);
						Call.setItemMeta(CallM);
						Apps.setItemMeta(AppsM);
						Glas.setItemMeta(Gs);

						Bank.setAmount(1);
						Guthaben.setAmount(1);
						SMS.setAmount(1);
						Call.setAmount(1);
						Apps.setAmount(1);
						Glas.setAmount(1);

						InvHandy.setItem(0, Call);
						InvHandy.setItem(3, Apps);
						InvHandy.setItem(5, Kontakt);
						InvHandy.setItem(8, SMS);
						InvHandy.setItem(10, Guthaben);
						InvHandy.setItem(16, Bank);

						for (int i = 0; i < 2*9; i++) {
							if (i != 0 && i != 3 && i != 5 && i != 8 && i != 10 && i != 13 && i != 16) {
								InvHandy.setItem(i, Glas);
							}
						}
						
						p.openInventory(InvHandy);

					}
				}
			}
		} catch (Exception e2) {
		}
	}

	@EventHandler
	public void KlickonHandyItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		int guthaben = Main.getPlugin().getConfig().getInt("Handy.Spieler." + p.getUniqueId() + ".guthaben");
		try {
			if (e.getView().getTitle().equals("§6Handy")) {

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBank")) {

					if(Main.handyOnOff.get(p.getUniqueId()).equals(false)) {
						p.sendMessage("§cDein Handy ist aus!");
						
					} else if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
						geldInt.put(p, (double) 0);
						Inventory HandyBank = Bukkit.createInventory(null, 27, "§6Handy Bank");

						double Money = pi.getMoney();

						ItemStack Abbuchen = new ItemBuilder(Material.GOLD_BLOCK).setName("§6Überweisen").build();

						ItemStack Einzahlen = new ItemBuilder(Material.IRON_BLOCK).setName("§6Konto: " + Money).build();

						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();

						if (pi.getJob() != 0) {
							FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
							ItemStack FirmEinzahlen = new ItemBuilder(Material.DIAMOND_BLOCK)
									.setName("§6[Handy] Firmenkonto überweisen").setLore(fi.getFirmname()).build();
							HandyBank.setItem(12, FirmEinzahlen);
							for (int i = 0; i < 27; i++) {
								if (i != 10 && i != 16 && i != 12) {
									HandyBank.setItem(i, Glas);
								}
							}
							if (fi.hasrank(p, Rights.MAIN_CHARGEMONEY)) {
								ItemStack FirmAbbuchen = new ItemBuilder(Material.EMERALD_BLOCK)
										.setName("§6[Handy] Von Firmenkonto auf Konto überweisen")
										.setLore(String.valueOf(fi.getFirmmoney()) + "€").build();
								HandyBank.setItem(14, FirmAbbuchen);
								for (int i = 0; i < 27; i++) {
									if (i != 10 && i != 16 && i != 12 && i != 14) {
										HandyBank.setItem(i, Glas);
									}
								}
							}

						} else {
							for (int i = 0; i < 27; i++) {
								if (i != 10 && i != 16) {
									HandyBank.setItem(i, Glas);
								}
							}
						}

						HandyBank.setItem(10, Abbuchen);
						HandyBank.setItem(16, Einzahlen);

						p.openInventory(HandyBank);

					} else {
						p.sendMessage("§cUm die Bank App zu benutzen benötigst du eine Sim Karte!");
					}

					e.setCancelled(true);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("§eNummer: " + Main.PlayerNummer.get(p.getUniqueId().toString()))
						|| e.getCurrentItem().getItemMeta().getDisplayName()
								.equalsIgnoreCase("§eNummer: " + "§cKeine Sim Karte eingelegt")) {
					String Nummer;
					if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
						Nummer = Main.PlayerNummer.get(p.getUniqueId().toString()).toString();
					} else {
						Nummer = "§cKeine Sim Karte eingelegt";
					}
					p.sendMessage("§eNummer: " + Nummer);
					p.sendMessage("§eGuthaben: " + guthaben);

					e.setCancelled(true);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eSMS")) {
					if(Main.handyOnOff.get(p.getUniqueId()).equals(false)) {
						p.sendMessage("§cDein Handy ist aus!");
						
					} else if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
						Inventory HandySMS = Bukkit.createInventory(null, 9, "§6SMS");

						ItemStack SMS = new ItemBuilder(Material.LEGACY_BOOK_AND_QUILL).setName("§6Handy SMS").build();

						ItemStack BSMS = new ItemBuilder(Material.BOOK).setName("§6Erhaltene SMS: " + pi.getSmsAutor().size()).build();

						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();

						for (int i = 0; i < 9; i++) {
							if (i != 1 && i != 7) {
								HandySMS.setItem(i, Glas);
							}
						}

						HandySMS.setItem(1, SMS);
						HandySMS.setItem(7, BSMS);

						p.openInventory(HandySMS);
					} else {
						p.sendMessage("§cUm die SMS App zu benutzen benötigst du eine Sim Karte!");
					}

					e.setCancelled(true);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAnrufen")) {
					if(Main.handyOnOff.get(p.getUniqueId()).equals(false)) {
						p.sendMessage("§cDein Handy ist aus!");
						
					} else if (Main.PlayerNummer.containsKey(p.getUniqueId().toString())) {
						Inventory HandyAnrufen = Bukkit.createInventory(null, 9, "§6Anrufen");

						ItemStack call = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/82442bbf7171b5cafca217c9ba44ce27647225df76cda9689d61a9f1c0a5f176");
						SkullMeta callM = (SkullMeta) call.getItemMeta();
						callM.setDisplayName("§aAnrufen");
						call.setItemMeta(callM);

						ItemStack glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();

						for (int i = 0; i < 9; i++) {
							if (i != 4) {
								HandyAnrufen.setItem(i, glas);
							}
						}

						HandyAnrufen.setItem(4, call);

						p.openInventory(HandyAnrufen);
					} else {
						p.sendMessage("§cUm zu Telefonieren benötigst du eine Sim Karte!");
					}

					e.setCancelled(true);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Kontaktbuch")) {
					if(Main.handyOnOff.get(p.getUniqueId()).equals(false)) {
						p.sendMessage("§cDein Handy ist aus!");
						
					} else p.openInventory(pi.getKontaktbuch("§6Kontaktbuch"));
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4OFF") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aON")) {
					if(Main.handyOnOff.get(p.getUniqueId()).equals(true)) {
						SQLData.setHandyOnOff(p.getUniqueId(), false);
						p.sendMessage("§eDein Handy ist jetzt §4aus§e!");
						ItemStack OFF = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/884e92487c6749995b79737b8a9eb4c43954797a6dd6cd9b4efce17cf475846");
						SkullMeta OFFM = (SkullMeta) OFF.getItemMeta();
						OFFM.setDisplayName("§4OFF");
						OFF.setItemMeta(OFFM);
						p.getOpenInventory().setItem(e.getSlot(), OFF);
						
					} else if(Main.handyOnOff.get(p.getUniqueId()).equals(false)) {
						SQLData.setHandyOnOff(p.getUniqueId(), true);
						p.sendMessage("§eDein Handy ist jetzt §aan§e!");
						ItemStack On = ItemSkulls.getSkull(
								"http://textures.minecraft.net/texture/5e48615df6b7ddf3ad495041876d9169bdc983a3fa69a2aca107e8f251f7687");
						SkullMeta OnM = (SkullMeta) On.getItemMeta();
						OnM.setDisplayName("§aON");
						On.setItemMeta(OnM);
						p.getOpenInventory().setItem(e.getSlot(), On);
					}
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Apps")) {
					Inventory inv = Bukkit.createInventory(null, 9, "§6Apps");
					
					ItemStack Dispatch = new ItemBuilder(Material.GLOBE_BANNER_PATTERN).setName("§6Standort senden").build();
					ItemStack Rabbit = new ItemBuilder(Material.RABBIT_HIDE).setName("§6Schlag den Maulwurf").setLore("§bComing Soon").build();
					ItemStack TicTacToe = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/e94b5e3ddc7a8f33c79346860d3923c71a582fda16acdbf4ad3c0cc465fd926");
					SkullMeta TicTacToeM = (SkullMeta) TicTacToe.getItemMeta();
					TicTacToeM.setDisplayName("§6TicTacToe");
					List<String> lore = new ArrayList<String>();
					lore.add("§bComing Soon");
					TicTacToeM.setLore(lore);
					TicTacToe.setItemMeta(TicTacToeM);
					ItemStack CookieClicker = new ItemBuilder(Material.COOKIE).setName("§6CookieClicker").build();
					
					inv.addItem(Dispatch);
					inv.addItem(Rabbit);
					inv.addItem(TicTacToe);
					inv.addItem(CookieClicker);
					p.openInventory(inv);
					
				} else
					e.setCancelled(true);

				e.setCancelled(true);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@EventHandler
	public void onAppsInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		try {
			if(e.getView().getTitle().equals("§6Apps")) {
				if(e.getView().getTopInventory() == e.getClickedInventory()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Standort senden")) {
						p.openInventory(pi.getKontaktbuch("§6Standort senden"));
					} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Schlag den Maulwurf")) {
						/*Inventory Cinv = Bukkit.createInventory(null, 3*9, "§6Schlag den Maulwurf");
						ItemStack Score = new ItemBuilder(Material.EMERALD).setName("§6Score: ").build();
						ItemStack HighScore = new ItemBuilder(Material.EMERALD_BLOCK).setName("§6High Score: ").build();
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					
						Cinv.setItem(0, Score);
						Cinv.setItem(8, HighScore);
						
						for (int i = 0; i < 3*9; i++) {
							if (i != 0 && i != 8 && i != 13 && i != 10 && i != 16) {
								Cinv.setItem(i, Glas);
							}
						}
						
						p.openInventory(Cinv);*/
						
					} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6TicTacToe")) {
                        /*Inventory TTTinv = Bukkit.createInventory(null, InventoryType.DROPPER, "§6TicTacToe");
						p.openInventory(TTTinv);*/
					} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6CookieClicker")) {
						Inventory Cinv = Bukkit.createInventory(null, 3*9, "§6Cookie Clicker");
						ItemStack CookieClicker = new ItemBuilder(Material.COOKIE).setName("§6Cookie").build();
						ItemStack Score = new ItemBuilder(Material.EMERALD).setName("§6Score: " + Main.CookieClick.get(p.getUniqueId().toString())).build();
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					
						Cinv.setItem(0, Score);
						Cinv.setItem(13, CookieClicker);
						
						for (int i = 0; i < 3*9; i++) {
							if (i != 0  && i != 13) {
								Cinv.setItem(i, Glas);
							}
						}
						
						p.openInventory(Cinv);
						
					} 
						e.setCancelled(true);
				}
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	
	@EventHandler
	public void onKlickGame(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTopInventory() == e.getClickedInventory()) {
				if (e.getView().getTitle().equals("§6Standort senden")) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aKontakt hinzufügen")) {
						ChatInfo.addChat(p, ChatType.HANDYADDKONTAKT, 600);

						p.sendMessage("§eGebe den Namen der Person an");
						p.closeInventory();
						e.setCancelled(true);

					} else {
						String pName = e.getCurrentItem().getItemMeta().getDisplayName();
						String ps = pName.replace("§6", "");
						OfflinePlayer po = Bukkit.getOfflinePlayer(ps);
						kontaktInt.put(p, po);

						Inventory KontaktInv = Bukkit.createInventory(null, 9 * 1, "§6Kontakt");

						ItemStack löschen = new ItemBuilder(Material.RED_CONCRETE).setName("§4Löschen").build();

						ItemStack spielerkopf = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta spKopfM = (SkullMeta) spielerkopf.getItemMeta();
						spKopfM.setOwner(ps);
						spKopfM.setDisplayName("§6" + ps);
						spielerkopf.setItemMeta(spKopfM);

						ItemStack Standort = new ItemBuilder(Material.GLOBE_BANNER_PATTERN).setName("§6Standort").build();

						ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

						for (int i = 0; i < 9; i++) {
							KontaktInv.setItem(i, Glas);

						}

						KontaktInv.setItem(4, spielerkopf);
						KontaktInv.setItem(2, löschen);
						KontaktInv.setItem(6, Standort);

						p.closeInventory();
						p.openInventory(KontaktInv);

					}
					e.setCancelled(true);
				} else if(e.getView().getTitle().equals("§6Cookie Clicker")) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Cookie")) {
						SQLData.addCookieClicker(p.getUniqueId().toString());
						ItemStack Score = new ItemBuilder(Material.EMERALD).setName("§6Score: " + Main.CookieClick.get(p.getUniqueId().toString())).build();
						p.getOpenInventory().setItem(0, Score);
					} 
					e.setCancelled(true);
					
				}
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}
	
	@EventHandler
	public void KlickHandySMS(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		try {
			if (e.getView().getTitle().equals("§6SMS")) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Handy SMS")) {
					if (ChatInfo.getChat(p).equals(ChatType.HANDYSMS)) {
						p.sendMessage("§cDu schreibst bereits eine SMS!");
					} else {
						ChatInfo.addChat(p, ChatType.HANDYSMS, 600);

						p.sendMessage("§eGebe die Nummer des Spielers an, dem du eine SMS senden möchtest!");
						p.closeInventory();
						e.setCancelled(true);
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§6Erhaltene SMS: ")) {
					if (pi.getSmsAutor().size() != 0) {
						p.openInventory(pi.getSMSInv());
					} else {
						p.closeInventory();
						p.sendMessage("§cEs gibt keine neuen SMS!");
					}
				}

				e.setCancelled(true);
			} else if (e.getView().getTitle().contains("§eErhaltene SMS: ")) {
				p.openBook(e.getCurrentItem());
				List<String> smsAutor = pi.getSmsAutor();
				List<String> smsNachricht = pi.getSmsNachricht();
				List<String> smsTitle = pi.getSmsTitle();
				smsAutor.remove(e.getSlot());
				smsNachricht.remove(e.getSlot());
				smsTitle.remove(e.getSlot());
				Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".sms.autors", smsAutor);
				Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".sms.messages", smsNachricht);
				Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".sms.titels", smsTitle);
				Main.getPlugin().saveConfig();
				e.setCancelled(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@EventHandler
	public void SMSChatMessage(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatInfo.getChat(p).equals(ChatType.HANDYSMS)) {
			e.setCancelled(true);
			try {
				int t = Integer.parseInt(e.getMessage());
				if (Main.NummerPlayer.containsKey(t)) {
					UUID SpielerUUID = UUID.fromString(Main.NummerPlayer.get(t));
					OfflinePlayer Spieler = Bukkit.getOfflinePlayer(SpielerUUID);
					target = Spieler;
					
					p.sendMessage("§eGebe die Nachricht an");

					e.setCancelled(true);
					ChatInfo.removeChat(p);
					ChatInfo.addChat(p, ChatType.HANDYSMSBETREFF, 1200);

				} else {
					p.sendMessage("§cDie Nummer gibt es nicht");
					ChatInfo.removeChat(p);
				}

			} catch (Exception e2) {
				p.sendMessage("§cDas ist keine Nummer!");
				p.sendMessage("§4Vorgang wurde abgebrochen! ");
				ChatInfo.removeChat(p);
			}

		} else if (ChatInfo.getChat(p).equals(ChatType.HANDYSMSBETREFF)) {
			ChatInfo.removeChat(p);
			smsBetreff.put(p, e.getMessage());

			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String formattedDate = myDateObj.format(myFormatObj);
			PlayerInfo pi = new PlayerInfo(target);
			String geht = " " + smsBetreff.get(p);
			pi.addMessage(p.getUniqueId(), geht);

			if (target.isOnline()) {
				Player po = (Player) target;
				if (po.getOpenInventory().getTitle().equalsIgnoreCase("§eErhaltene SMS: " + pi.getSmsAutor().size())) {
					po.openInventory(pi.getSMSInv());
				}
				po.sendMessage("");
				po.sendMessage("§eDu hast eine Neue SMS erhalten:");
				po.sendMessage(
						"§e" + formattedDate + " Von §6" + Main.PlayerNummer.get((p.getUniqueId().toString())) + "§e:");
				po.sendMessage("§e" + smsBetreff.get(p));
				po.sendMessage("");
			}

			p.sendMessage("");
			p.sendMessage("§eDu hast die SMS gesendet:");
			p.sendMessage("§e" + formattedDate + " SMS:");
			p.sendMessage("§e" + smsBetreff.get(p));
			p.sendMessage("");

			e.setCancelled(true);
		}

	}

	@EventHandler
	public void ChatMessage(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatInfo.getChat(p).equals(ChatType.HANDYUEBERWEISEN)) {
			e.setCancelled(true);
			Player t = Bukkit.getPlayer(e.getMessage());
			if (t != null) {
				spieler.put(p, t);

				p.sendMessage("§eGebe ein Betreff an");

				e.setCancelled(true);
				ChatInfo.removeChat(p);
				ChatInfo.addChat(p, ChatType.HANDYBETREFF, 600);

			} else
				p.sendMessage("§cDer angegebene Spieler ist nicht online oder existiert nicht!");

		} else if (ChatInfo.getChat(p).equals(ChatType.HANDYBETREFF)) {
			ChatInfo.removeChat(p);
			betreff.put(p, e.getMessage());
			Player t = spieler.get(p);

			Inventory InvÜberweisenAcc = Bukkit.createInventory(null, 3 * 3, "§6Handy Überweisen fertigstellen");

			ItemStack KopfSpieler = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
			SkullMeta KopfMeta = (SkullMeta) KopfSpieler.getItemMeta();
			KopfMeta.setOwner(t.getName());
			KopfMeta.setDisplayName("§6" + t.getName() + " §e" + geldInt.get(p) + "€ §6überweisen");
			KopfSpieler.setItemMeta(KopfMeta);
			KopfSpieler.setAmount(1);

			ItemStack Del = new ItemStack(Material.RED_CONCRETE, 1);
			ItemMeta DelMeta = Del.getItemMeta();
			DelMeta.setDisplayName("§6Abbrechen");
			Del.setItemMeta(DelMeta);
			Del.setAmount(1);

			ItemStack ACC = new ItemStack(Material.LIME_CONCRETE, 1);
			ItemMeta ACCMeta = ACC.getItemMeta();
			ACCMeta.setDisplayName("§2Akzeptieren");
			ACC.setItemMeta(ACCMeta);
			ACC.setAmount(1);

			InvÜberweisenAcc.setItem(4, KopfSpieler);
			InvÜberweisenAcc.setItem(1, Del);
			InvÜberweisenAcc.setItem(7, ACC);
			InvÜberweisenAcc.setItem(0, numpad.get("Glas"));
			InvÜberweisenAcc.setItem(2, numpad.get("Glas"));
			InvÜberweisenAcc.setItem(3, numpad.get("Glas"));
			InvÜberweisenAcc.setItem(5, numpad.get("Glas"));
			InvÜberweisenAcc.setItem(6, numpad.get("Glas"));
			InvÜberweisenAcc.setItem(8, numpad.get("Glas"));

			p.openInventory(InvÜberweisenAcc);
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void KlickHandyBankonItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTitle().equals("§6Handy Bank")) {
				e.setCancelled(true);

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Überweisen")) {

					try {
						p.closeInventory();
						Inventory InvÜberweisen = Bukkit.createInventory(null, 5 * 9, "§6Handy Überweisen");
						numpadinv(InvÜberweisen);
						p.openInventory(InvÜberweisen);

					} catch (Exception e1) {
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("§6[Handy] Firmenkonto überweisen")) {
					p.closeInventory();
					Inventory InvFirmÜberweisen = Bukkit.createInventory(null, 5 * 9,
							"§6[Handy] Firmenkonto überweisen");
					numpadinv(InvFirmÜberweisen);
					p.openInventory(InvFirmÜberweisen);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("§6[Handy] Von Firmenkonto auf Konto überweisen")) {
					p.closeInventory();
					Inventory InvFirmAbbuchen = Bukkit.createInventory(null, 5 * 9,
							"§6[Handy] Von Firmenkonto auf Konto überweisen");
					numpadinv(InvFirmAbbuchen);
					p.openInventory(InvFirmAbbuchen);
				}

				e.setCancelled(true);
			}
		} catch (Exception exception) {
		}
	}

	@EventHandler
	public void HandyÜberweisenMenue(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§6Handy Überweisen")) {
				Player p = (Player) e.getWhoClicked();
				money = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".money");
				geld = geldInt.get(p);
				if (money >= geld) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 0")) {
						if (block < 9) {
							if (block > 0) {
								geld = geld * 10 + 0;
								if (money >= geld) {
									block++;
									e.getInventory().setItem(block - 1, numpad.get("0"));

								} else {
									p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
									geld = geld / 10;
								}
							}
							e.setCancelled(true);
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 1")) {
						if (block < 9) {
							geld = geld * 10 + 1;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("1"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 1 / 10;
							}
						}
						e.setCancelled(true);
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 2")) {
						if (block < 9) {
							geld = geld * 10 + 2;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("2"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 2 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 3")) {
						if (block < 9) {
							geld = geld * 10 + 3;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("3"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 3 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 4")) {
						if (block < 9) {
							geld = geld * 10 + 4;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("4"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 4 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 5")) {
						if (block < 9) {
							geld = geld * 10 + 5;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("5"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 5 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 6")) {
						if (block < 9) {
							geld = geld * 10 + 6;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("6"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 6 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 7")) {
						if (block < 9) {
							geld = geld * 10 + 7;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("7"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 7 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 8")) {
						if (block < 9) {
							geld = geld * 10 + 8;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("8"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 8 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 9")) {
						if (block < 9) {
							geld = geld * 10 + 9;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("9"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 9 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbrechen")) {

						p.closeInventory();
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
						ChatInfo.addChat(p, ChatType.HANDYUEBERWEISEN, 600);

						p.sendMessage("§eGebe den Namen des Spielers an, dem du das Geld überweisen möchtest");
						p.closeInventory();
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Löschen")) {
						geld = 0;
						block = 0;
						geldInt.put(p, (double) 0);

						for (int i = 0; i <= 8; i++) {
							e.getInventory().setItem(i, null);

						}
						e.setCancelled(true);
					}

				} else {
					p.closeInventory();
				}

				e.setCancelled(true);

			}
		} catch (Exception exception) {
		}

	}

	@EventHandler
	public void AccMenue(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTitle().equals("§6Handy Überweisen fertigstellen")) {

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbrechen")) {

					p.sendMessage("§4Vorgang abgebrochen!");

					p.closeInventory();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
					geld = geldInt.get(p);
					money = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".money");
					Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".money", money - geld);
					Player t = spieler.get(p);
					money = Main.getPlugin().getConfig().getInt("Spieler." + t.getUniqueId() + ".money");
					Main.getPlugin().getConfig().set("Spieler." + t.getUniqueId() + ".money", money + geld);
					Main.getPlugin().saveConfig();
					p.sendMessage("§aDu hast §6" + t.getName() + " §e" + geld + "€ §aüberwiesen");
					t.sendMessage("§6" + p.getName() + " §ahat dir §e" + geld + "€ §aüberwiesen");
					t.sendMessage("§6Betreff: §e" + betreff.get(p));

					p.closeInventory();
					e.setCancelled(true);

				} else
					e.setCancelled(true);

				e.setCancelled(true);
			}
		} catch (Exception e2) {

		}

	}

	public void numpadinv(Inventory Inv) {

		geld = 0;
		block = 0;

		ItemStack skull1 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc");
		ItemMeta skull1Meta = skull1.getItemMeta();
		skull1Meta.setDisplayName("§7 1");
		skull1.setItemMeta(skull1Meta);
		skull1.setAmount(1);
		numpad.put("1", skull1);

		ItemStack skull2 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a");
		ItemMeta skull2Meta = skull2.getItemMeta();
		skull2Meta.setDisplayName("§7 2");
		skull2.setItemMeta(skull2Meta);
		skull2.setAmount(1);
		numpad.put("2", skull2);

		ItemStack skull3 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2");
		ItemMeta skull3Meta = skull3.getItemMeta();
		skull3Meta.setDisplayName("§7 3");
		skull3.setItemMeta(skull3Meta);
		skull3.setAmount(1);
		numpad.put("3", skull3);

		ItemStack skull4 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058");
		ItemMeta skull4Meta = skull4.getItemMeta();
		skull4Meta.setDisplayName("§7 4");
		skull4.setItemMeta(skull4Meta);
		skull4.setAmount(1);
		numpad.put("4", skull4);

		ItemStack skull5 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763");
		ItemMeta skull5Meta = skull5.getItemMeta();
		skull5Meta.setDisplayName("§7 5");
		skull5.setItemMeta(skull5Meta);
		skull5.setAmount(1);
		numpad.put("5", skull5);

		ItemStack skull6 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1");
		ItemMeta skull6Meta = skull6.getItemMeta();
		skull6Meta.setDisplayName("§7 6");
		skull6.setItemMeta(skull6Meta);
		skull6.setAmount(1);
		numpad.put("6", skull6);

		ItemStack skull7 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb");
		ItemMeta skull7Meta = skull7.getItemMeta();
		skull7Meta.setDisplayName("§7 7");
		skull7.setItemMeta(skull7Meta);
		skull7.setAmount(1);
		numpad.put("7", skull7);

		ItemStack skull8 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d");
		ItemMeta skull8Meta = skull8.getItemMeta();
		skull8Meta.setDisplayName("§7 8");
		skull8.setItemMeta(skull8Meta);
		skull8.setAmount(1);
		numpad.put("8", skull8);

		ItemStack skull9 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba");
		ItemMeta skull9Meta = skull9.getItemMeta();
		skull9Meta.setDisplayName("§7 9");
		skull9.setItemMeta(skull9Meta);
		skull9.setAmount(1);
		numpad.put("9", skull9);

		ItemStack skull0 = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5");
		ItemMeta skull0Meta = skull0.getItemMeta();
		skull0Meta.setDisplayName("§7 0");
		skull0.setItemMeta(skull0Meta);
		skull0.setAmount(1);
		numpad.put("0", skull0);

		ItemStack Del = new ItemStack(Material.RED_CONCRETE, 1);
		ItemMeta DelMeta = Del.getItemMeta();
		DelMeta.setDisplayName("§6Abbrechen");
		Del.setItemMeta(DelMeta);
		Del.setAmount(1);
		numpad.put("Del", Del);

		ItemStack ACC = new ItemStack(Material.LIME_CONCRETE, 1);
		ItemMeta ACCMeta = ACC.getItemMeta();
		ACCMeta.setDisplayName("§2Akzeptieren");
		ACC.setItemMeta(ACCMeta);
		ACC.setAmount(1);
		numpad.put("ACC", ACC);

		ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta GlasMeta = Glas.getItemMeta();
		GlasMeta.setDisplayName(" ");
		Glas.setItemMeta(GlasMeta);
		Glas.setAmount(1);
		numpad.put("Glas", Glas);

		ItemStack Löschen = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta LöschenMeta = Löschen.getItemMeta();
		LöschenMeta.setDisplayName("§4Löschen");
		Löschen.setItemMeta(LöschenMeta);
		Löschen.setAmount(1);
		numpad.put("Löschen", Löschen);

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

	@EventHandler
	public void FirmÜberweisen(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§6[Handy] Firmenkonto überweisen")) {
				Player p = (Player) e.getWhoClicked();
				PlayerInfo pi = new PlayerInfo(p);
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				money = pi.getMoney();
				geld = geldInt.get(p);

				if (money >= geld) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 0")) {
						if (block < 9) {
							if (block > 0) {
								geld = geld * 10 + 0;
								if (money >= geld) {
									block++;
									e.getInventory().setItem(block - 1, numpad.get("0"));

								} else {
									p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
									geld = geld / 10;
								}
							}
							e.setCancelled(true);
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 1")) {
						if (block < 9) {
							geld = geld * 10 + 1;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("1"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 1 / 10;
							}
						}
						e.setCancelled(true);
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 2")) {
						if (block < 9) {
							geld = geld * 10 + 2;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("2"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 2 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 3")) {
						if (block < 9) {
							geld = geld * 10 + 3;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("3"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 3 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 4")) {
						if (block < 9) {
							geld = geld * 10 + 4;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("4"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 4 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 5")) {
						if (block < 9) {
							geld = geld * 10 + 5;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("5"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 5 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 6")) {
						if (block < 9) {
							geld = geld * 10 + 6;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("6"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 6 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 7")) {
						if (block < 9) {
							geld = geld * 10 + 7;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("7"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 7 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 8")) {
						if (block < 9) {
							geld = geld * 10 + 8;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("8"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 8 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 9")) {
						if (block < 9) {
							geld = geld * 10 + 9;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("9"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 9 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbrechen")) {

						p.closeInventory();
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
						p.sendMessage("§6" + geld + "€ wurden auf das Firmenkonto überwiesen!");
						fi.setFirmmoney(fi.getFirmmoney() + geld);
						pi.subtractMoney(geld);
						e.setCancelled(true);
						p.closeInventory();
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Löschen")) {
						geld = 0;
						block = 0;
						geldInt.put(p, (double) 0);

						for (int i = 0; i <= 8; i++) {
							e.getInventory().setItem(i, null);

						}
						e.setCancelled(true);
					}

				} else {
					p.closeInventory();
				}
			}
		} catch (Exception e2) {
		}

	}

	@EventHandler
	public void FirmAbbuchen(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§6[Handy] Von Firmenkonto auf Konto überweisen")) {
				Player p = (Player) e.getWhoClicked();
				PlayerInfo pi = new PlayerInfo(p);
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				money = fi.getFirmmoney();
				geld = geldInt.get(p);

				if (money >= geld) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 0")) {
						if (block < 9) {
							if (block > 0) {
								geld = geld * 10 + 0;
								if (money >= geld) {
									block++;
									e.getInventory().setItem(block - 1, numpad.get("0"));

								} else {
									p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
									geld = geld / 10;
								}
							}
							e.setCancelled(true);
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 1")) {
						if (block < 9) {
							geld = geld * 10 + 1;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("1"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 1 / 10;
							}
						}
						e.setCancelled(true);
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 2")) {
						if (block < 9) {
							geld = geld * 10 + 2;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("2"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 2 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);

					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 3")) {
						if (block < 9) {
							geld = geld * 10 + 3;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("3"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 3 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 4")) {
						if (block < 9) {
							geld = geld * 10 + 4;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("4"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 4 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 5")) {
						if (block < 9) {
							geld = geld * 10 + 5;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("5"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 5 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 6")) {
						if (block < 9) {
							geld = geld * 10 + 6;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("6"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 6 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 7")) {
						if (block < 9) {
							geld = geld * 10 + 7;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("7"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 7 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 8")) {
						if (block < 9) {
							geld = geld * 10 + 8;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("8"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 8 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7 9")) {
						if (block < 9) {
							geld = geld * 10 + 9;
							if (money >= geld) {
								block++;
								e.getInventory().setItem(block - 1, numpad.get("9"));

							} else {
								p.sendMessage("§4Die Eingabe übersteigt Ihren Kontostand");
								geld = geld - 9 / 10;
							}
						}
						geldInt.put(p, geld);

						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbrechen")) {

						p.closeInventory();
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
						p.sendMessage("§6" + geld + "€ wurden von dem Firmenkonto abgebucht!");
						fi.setFirmmoney(fi.getFirmmoney() - geld);
						pi.setMoney(pi.getMoney() + geld);
						e.setCancelled(true);
						p.closeInventory();
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
						e.setCancelled(true);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Löschen")) {
						geld = 0;
						block = 0;
						geldInt.put(p, (double) 0);

						for (int i = 0; i <= 8; i++) {
							e.getInventory().setItem(i, null);

						}
						e.setCancelled(true);
					}

				} else {
					p.closeInventory();
				}
			}
		} catch (Exception e2) {
		}
	}

	@EventHandler
	public void KlickAnrufen(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTitle().equals("§6Anrufen")) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAnrufen")) {
					ChatInfo.addChat(p, ChatType.HANDYANRUFEN, 600);
					p.sendMessage("§eGebe die Nummer des Spielers an, den sie Anrufen möchtest!");
					p.closeInventory();
					e.setCancelled(true);
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {

		}

	}

	@EventHandler
	public void AnrufenChatMessage(PlayerChatEvent e) {
		Player p = (Player) e.getPlayer();
		if (ChatInfo.getChat(p).equals(ChatType.HANDYANRUFEN)) {
			try {
				int t = Integer.parseInt(e.getMessage());
				if (Main.NummerPlayer.containsKey(t)) {
					UUID SpielerUUID = UUID.fromString(Main.NummerPlayer.get(t));
					OfflinePlayer Spieler = Bukkit.getOfflinePlayer(SpielerUUID);

					if (Spieler.isOnline() && Main.handyOnOff.get(Spieler.getUniqueId())) {
						Player po = (Player) Spieler;
						po.sendMessage("§6" + Main.PlayerNummer.get(p.getUniqueId().toString())
								+ "§e versucht sie anzurufen!");
						TextComponent tc = new TextComponent();
						tc.setText("§7[§aAnnehmen§7]");
						tc.setBold(true);
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptanruf"));
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§a§lAnnehmen").create()));
						TextComponent tcl = new TextComponent();
						tcl.setText("   ");
						tcl.setHoverEvent(
								new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
						tcl.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
						tc.addExtra(tcl);
						TextComponent tc2 = new TextComponent();
						tc2.setText("§7[§4Ablehnen§7]");
						tc2.setBold(true);
						tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denyanruf"));
						tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§l§7[§4Ablehnen§7]").create()));
						tc.addExtra(tc2);
						po.spigot().sendMessage(tc);
						Main.AnrufMap.put(po, p);
						p.sendMessage("§eDu rufst grade §6" + t + "§e an");
					} else {
						p.sendMessage("§cDer Anschluss ist zur Zeit leider nicht erreichbar!");
					}

					ChatInfo.removeChat(p);

				} else {
					p.sendMessage("§cDie Nummer gibt es nicht");
					ChatInfo.removeChat(p);
				}

			} catch (Exception e2) {
				p.sendMessage("§cDas ist keine Nummer!");
				p.sendMessage("§4Vorgang wurde abgebrochen! ");
				ChatInfo.removeChat(p);
				e2.printStackTrace();
			}
			e.setCancelled(true);

		}

	}

	@EventHandler
	public void KontaktBuch(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if (e.getView().getTitle().equals("§6Kontaktbuch")) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aKontakt hinzufügen")) {
					ChatInfo.addChat(p, ChatType.HANDYADDKONTAKT, 600);

					p.sendMessage("§eG");
					p.closeInventory();
					e.setCancelled(true);

				} else {
					String pName = e.getCurrentItem().getItemMeta().getDisplayName();
					String ps = pName.replace("§6", "");
					OfflinePlayer po = Bukkit.getPlayer(ps);
					kontaktInt.put(p, po);

					Inventory KontaktInv = Bukkit.createInventory(null, 9 * 2, "§6Kontakt");

					ItemStack löschen = new ItemBuilder(Material.RED_CONCRETE).setName("§4Löschen").build();

					ItemStack spielerkopf = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta spKopfM = (SkullMeta) spielerkopf.getItemMeta();
					spKopfM.setOwner(ps);
					spKopfM.setDisplayName("§6" + ps);
					spielerkopf.setItemMeta(spKopfM);

					ItemStack sms = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/b4bd9dd128c94c10c945eadaa342fc6d9765f37b3df2e38f7b056dc7c927ed");
					SkullMeta smsM = (SkullMeta) sms.getItemMeta();
					smsM.setDisplayName("§6SMS");
					sms.setItemMeta(smsM);

					ItemStack call = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/82442bbf7171b5cafca217c9ba44ce27647225df76cda9689d61a9f1c0a5f176");
					SkullMeta callM = (SkullMeta) call.getItemMeta();
					callM.setDisplayName("§6Anrufen");
					call.setItemMeta(callM);

					ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

					for (int i = 0; i < 18; i++) {
						KontaktInv.setItem(i, Glas);

					}

					KontaktInv.setItem(4, spielerkopf);
					KontaktInv.setItem(11, call);
					KontaktInv.setItem(13, löschen);
					KontaktInv.setItem(15, sms);

					p.closeInventory();
					p.openInventory(KontaktInv);

				}
				e.setCancelled(true);
			}

		} catch (Exception e2) {
		}

	}

	@EventHandler
	public void KSMS(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		OfflinePlayer t = kontaktInt.get(p);
		try {
			if (e.getView().getTitle().equals("§6Kontakt")) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Löschen")) {
					pi.removeKontakt(t.getUniqueId());
					p.closeInventory();
					p.sendMessage("§aDu hast diesen Kontakt gelöscht!");

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6SMS")) {
					ChatInfo.addChat(p, ChatType.HANDYSMSKONTAKT, 600);

					p.sendMessage("§eGebe die Nachricht an!");
					p.closeInventory();
					e.setCancelled(true);

				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Anrufen")) {
					if (t.isOnline() || Main.handyOnOff.get(t.getUniqueId()).equals(true)) {
						if(!Main.AnrufMap.containsKey(t.getPlayer())) {
							Player po = (Player) t;
							po.sendMessage("§6" + Main.PlayerNummer.get(p.getPlayer().getUniqueId().toString())
									+ "§e versucht sie anzurufen!");
							TextComponent tc = new TextComponent();
							tc.setText("§7[§aAnnehmen§7]");
							tc.setBold(true);
							tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptanruf"));
							tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("§a§lAnnehmen").create()));
							TextComponent tcl = new TextComponent();
							tcl.setText("   ");
							tcl.setHoverEvent(
									new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
							tcl.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
							tc.addExtra(tcl);
							TextComponent tc2 = new TextComponent();
							tc2.setText("§7[§4Ablehnen§7]");
							tc2.setBold(true);
							tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denyanruf"));
							tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("§l§7[§4Ablehnen§7]").create()));
							tc.addExtra(tc2);
							po.spigot().sendMessage(tc);
							Main.AnrufMap.put(po, p);
							p.sendMessage("§eDu rufst grade §6" + t.getName() + "§e an");
						}
					} else {
						p.sendMessage("§cDas Handy mit der Nummer §6" + t + "§c ist vermutlich aus!");
					}
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Standort")) {
					if(t.isOnline()) {
					if(!Main.handyOnOff.containsKey(t.getUniqueId()) == false) {
							Player tO = (Player) t;
							Location pLoc = p.getLocation();
							p.sendMessage("§6Du hast der Person einen Standort geschickt");
							tO.sendMessage("§6=================================");
							tO.sendMessage("§6" + p.getName() + " hat ihnen einen Standort gesendet:");
							tO.sendMessage("§6 X: " + pLoc.getX() + " Y: " + pLoc.getY() + " Z:" + pLoc.getZ() + " ");
							Main.PlayerSendStandort.put(tO, p.getLocation());
							TextComponent tc = new TextComponent();
							tc.setText("§7[§4Route anzeigen§7]");
							tc.setBold(true);
							tc.setColor(tc.getColor());
							tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/startrouteStandort"));
							tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("§a§lRoute anzeigen").create()));
							tO.spigot().sendMessage(tc);
							tO.sendMessage("§6=================================");
							p.closeInventory();
						}else 
							p.sendMessage("§6Sie konnten ihr Standort nicht senden, da die Person ihr Handy §4aus §6hat");
					} else 
						p.sendMessage("§6Sie konnten ihr Standort nicht senden, da die Person ihr Handy §4aus §6hat");
				} 
				e.setCancelled(true);

			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@EventHandler
	public void SMSKontaktBuch(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatInfo.getChat(p).equals(ChatType.HANDYSMSKONTAKT)) {
			e.setCancelled(true);
			try {
				smsBetreffKontakt.put(p, e.getMessage());
				OfflinePlayer t = kontaktInt.get(p);
				LocalDateTime myDateObj = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				String formattedDate = myDateObj.format(myFormatObj);
				PlayerInfo pi = new PlayerInfo(t);
				String geht = " " + smsBetreffKontakt.get(p);
				pi.addMessage(p.getUniqueId(), geht);

				if (t.isOnline()) {
					Player po = (Player) t;
					if (po.getOpenInventory().getTitle().equalsIgnoreCase("§eErhaltene SMS: " + pi.getSmsAutor().size())) {
						po.openInventory(pi.getSMSInv());
					}
					po.sendMessage("");
					po.sendMessage("§eDu hast eine Neue SMS erhalten:");
					po.sendMessage(
							"§e" + formattedDate + " Von §6" + Main.PlayerNummer.get((p.getUniqueId().toString())) + "§e:");
					po.sendMessage("§e" + smsBetreffKontakt.get(p));
					po.sendMessage("");
				}

				p.sendMessage("");
				p.sendMessage("§eDu hast die SMS gesendet:");
				p.sendMessage("§e" + formattedDate + " SMS:");
				p.sendMessage("§e" + smsBetreffKontakt.get(p));
				p.sendMessage("");

				e.setCancelled(true);
				
				
				ChatInfo.removeChat(p);
			} catch (Exception e2) {
				ChatInfo.removeChat(p);
			}
		}
	}

	@EventHandler
	public void ChatKontaktBuch(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatInfo.getChat(p).equals(ChatType.HANDYADDKONTAKT)) {
			e.setCancelled(true);
			try {
				boolean isPlayer = false;
				String kontaktP = e.getMessage();
				OfflinePlayer t = Bukkit.getOfflinePlayer(kontaktP);
				kontaktT.put(p, t);
				for (OfflinePlayer current : Bukkit.getOfflinePlayers()) {
					if (current.equals(t)) {
						isPlayer = true;
						break;
					}
				}
				if (isPlayer) {
					p.sendMessage("§eGebe die Nummer an");
					ChatInfo.addChat(p, ChatType.HANDYADDKONTAKTNUMMER, 600);
				} else {
					p.sendMessage("§cDer Spieler existiert nicht!");
					p.sendMessage("§4Vorgang abgebrochen!");
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				ChatInfo.removeChat(p);
			}

		} else if (ChatInfo.getChat(p).equals(ChatType.HANDYADDKONTAKTNUMMER)) {
			ChatInfo.removeChat(p);
			e.setCancelled(true);
			try {
				int kNummer = Integer.parseInt(e.getMessage());
				kontaktNummer.put(p, kNummer);
				if (Main.PlayerNummer.containsKey(kontaktT.get(p).getUniqueId().toString())) {
					if (Main.PlayerNummer.get(kontaktT.get(p).getUniqueId().toString()).equals(kNummer)) {
						PlayerInfo pi = new PlayerInfo(p);
						pi.addKontakt(kontaktT.get(p).getUniqueId());
						p.sendMessage("§eKontakt wurde hinzugefügt");
					} else
						p.sendMessage("§cDie angegebene Nummer passt nicht zur angegebenen Person!");
				} else
					p.sendMessage("§cDer angegebene Spieler besitzt keine Sim Karte!");
			} catch (Exception e2) {
				p.sendMessage("§cDas ist keine Nummer!");
				p.sendMessage("§4Vorgang wurde abgebrochen! ");
			}

		}

	}
}