package de.ltt.bank.listener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.FakePlayer.RightClickNPC;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;


public class Bank_System implements Listener{
	
	ArrayList<String> Nummer = new ArrayList<String>();
	
	int geld;
	int afterPoint;
	int block;
	double money;
	double moneyInHand;
	HashMap<Player, Player> Spieler = new HashMap<Player, Player>();
	HashMap<Player, Integer> GeldInt = new HashMap<Player, Integer>();
	HashMap<Player, Integer> backPoint = new HashMap<Player, Integer>();
	HashMap<Player, String> Betreff = new HashMap<Player, String>();
	HashMap<Player, Integer> PlayerBlock = new HashMap<Player, Integer>();
	HashMap<Player, UUID> playerCard = new HashMap<Player, UUID>();
	HashMap<Player, String> playerKey = new HashMap<Player, String>();
	HashMap<Player, UUID> playerChar = new HashMap<Player, UUID>();
	ArrayList<Player> isPoint = new ArrayList<Player>();
	
	
	@EventHandler
	public void ATM_Schild(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getLine(0) != null && e.getLine(0) != "") {
				if (e.getLine(0).equalsIgnoreCase("/ATM")) {
					e.setLine(0, "§a[ATM]");
					e.setLine(1, "§7[7][8][9]");
					e.setLine(2, "§7[4][5][6]");
					e.setLine(3, "§7[1][2][3]");

				}

			}
		}
	}
	
	@EventHandler
	public void ATM_Inter(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
		if(!(e.getClickedBlock().getState() instanceof Sign))return;
		Sign sign = (Sign) e.getClickedBlock().getState();
		Player p = e.getPlayer();
		if(sign.getLine(0) == null || sign.getLine(0) == "")return;
		if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[ATM]"))return;
		if(!p.getItemInHand().getType().equals(Material.PAPER)  || !p.getItemInHand().getItemMeta().getDisplayName().equals("§6Bankkarte")) {
			p.sendMessage("§cBenutze eine Bankkarte um den Bankautomaten zu benutzen!");
			return;
		}
		OfflinePlayer pc = Bukkit.getOfflinePlayer(UUID.fromString(ItemBuilder.getTagValue(p.getItemInHand(), "Own")));
		UUID charz = UUID.fromString(ItemBuilder.getTagValue(p.getItemInHand(), "Char"));
		PlayerInfo pci = new PlayerInfo(pc);
		if(!pci.getBankCard().equals(ItemBuilder.getTagValue(p.getItemInHand(), "ID"))) {
			p.sendMessage("§cDiese Karte wurde vom Kontoinhaber gesperrt!");
			return;
		}
		if(isPoint.contains(p))isPoint.remove(p);
		GeldInt.put(p, 0);
		backPoint.put(p, 0);
		PlayerBlock.put(p, 0);
		playerCard.put(p, pc.getUniqueId());
		playerKey.put(p, "");
		playerChar.put(p, charz);
		Inventory inv = Bukkit.createInventory(null, 9*5, "§6Bitte PIN-Code eingeben");
		numpadinv(inv);
		inv.setItem(4, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(5, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(6, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(7, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(8, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void KlickonItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
		if (e.getView().getTitle().equals("§6ATM")) {

				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbuchen")) {
					p.closeInventory();
					Inventory InvAbbuchen = Bukkit.createInventory(null, 5 * 9, "§6ATM Abbuchen");
					numpadinv(InvAbbuchen);
					InvAbbuchen.setItem(41, new ItemBuilder("http://textures.minecraft.net/texture/c3885a63453782c915ed6d894cf1d94318740f6a156d6ea9d39524ba5d05f65").setName(" ").build());
					p.openInventory(InvAbbuchen);
					geld = 0;
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Einzahlen")) {
					p.closeInventory();
					Inventory InvEinzahlen = Bukkit.createInventory(null, 5 * 9, "§6ATM Einzahlen");
					numpadinv(InvEinzahlen);
					InvEinzahlen.setItem(41, new ItemBuilder("http://textures.minecraft.net/texture/c3885a63453782c915ed6d894cf1d94318740f6a156d6ea9d39524ba5d05f65").setName(" ").build());
					p.openInventory(InvEinzahlen);
					geld = 0;
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Überweisen")) {
					p.closeInventory();
					Inventory InvÜberweisen = Bukkit.createInventory(null, 5 * 9, "§6ATM Überweisen");
					numpadinv(InvÜberweisen);
					InvÜberweisen.setItem(41, new ItemBuilder("http://textures.minecraft.net/texture/c3885a63453782c915ed6d894cf1d94318740f6a156d6ea9d39524ba5d05f65").setName(" ").build());
					p.openInventory(InvÜberweisen);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Auf Firmenkonto überweisen")) {
					p.closeInventory();
					Inventory InvFirmÜberweisen = Bukkit.createInventory(null, 5 * 9, "§6Auf Firmenkonto überweisen");
					numpadinv(InvFirmÜberweisen);
					InvFirmÜberweisen.setItem(41, new ItemBuilder("http://textures.minecraft.net/texture/c3885a63453782c915ed6d894cf1d94318740f6a156d6ea9d39524ba5d05f65").setName(" ").build());
					p.openInventory(InvFirmÜberweisen);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Von Firmenkonto abheben")) {
					p.closeInventory();
					Inventory InvFirmAbbuchen = Bukkit.createInventory(null, 5 * 9, "§6Von Firmenkonto abheben");
					numpadinv(InvFirmAbbuchen);
					InvFirmAbbuchen.setItem(41, new ItemBuilder("http://textures.minecraft.net/texture/c3885a63453782c915ed6d894cf1d94318740f6a156d6ea9d39524ba5d05f65").setName(" ").build());
					p.openInventory(InvFirmAbbuchen);
				}
			
			e.setCancelled(true);
		} 
		} catch (Exception exception) {
		}
	}
	
	public void KlickHören(Location loc, Sound s) {
		
		for (Player current : Bukkit.getOnlinePlayers()) {
			if(loc.distance(current.getLocation()) <= 3) {
				current.playSound(loc, s, 3, 2);
				
			}
			
		}
	}
	
	@EventHandler
	public void ChatMessage(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if(ChatInfo.getChat(p).equals(ChatType.UEBERWEISEN)) {
			Player t = Main.getChar(e.getMessage());
			if(t != null) {
				Spieler.put(p, t);	
				p.sendMessage("§eGebe einen Betreff an");
			    e.setCancelled(true);
			    ChatInfo.removeChat(p);
			    ChatInfo.addChat(p, ChatType.BETREFF, 600);
			} else
				p.sendMessage("§cDer angegebene Spieler ist nicht online oder existiert nicht!");
			
		} else if(ChatInfo.getChat(p).equals(ChatType.BETREFF)) {
			geld = 0;
			afterPoint = 0;
			if(GeldInt.containsKey(p))geld = GeldInt.get(p);
			if(backPoint.containsKey(p))afterPoint = backPoint.get(p);
			double finish = Double.parseDouble(geld + "." + afterPoint);
			ChatInfo.removeChat(p);
			Betreff.put(p, e.getMessage());
			Player t = Spieler.get(p);
			
			Inventory InvÜberweisenAcc = Bukkit.createInventory(null, 3*3, "§6ATM Überweisen fertigstellen");
			
			ItemStack KopfSpieler = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
			SkullMeta KopfMeta = (SkullMeta) KopfSpieler.getItemMeta();
			KopfMeta.setOwner(t.getName());
			KopfMeta.setDisplayName("§6" + new PlayerInfo(p).getFullName() + " §e" + finish + "€ §6überweisen");
			KopfSpieler.setItemMeta(KopfMeta);
			KopfSpieler.setAmount(1);
			
			ItemStack Del = new ItemBuilder(Material.RED_CONCRETE).setName("§6Abbrechen").build();
			ItemStack ACC = new ItemBuilder(Material.LIME_CONCRETE).setName("§2Akzeptieren").build();

			InvÜberweisenAcc.setItem(4, KopfSpieler);
			InvÜberweisenAcc.setItem(1, Del);
			InvÜberweisenAcc.setItem(7, ACC);
			InvÜberweisenAcc.setItem(0, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
			InvÜberweisenAcc.setItem(2, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
			InvÜberweisenAcc.setItem(3, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
			InvÜberweisenAcc.setItem(5, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
			InvÜberweisenAcc.setItem(6, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
			InvÜberweisenAcc.setItem(8, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());

			p.openInventory(InvÜberweisenAcc);
			e.setCancelled(true);
		}
			
	}
	
	@EventHandler
	public void AccMenue(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
		if(e.getView().getTitle().equals("§6ATM Überweisen fertigstellen")) {
			
			 if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Abbrechen")) {
				 
				 p.sendMessage("§4Vorgang abgebrochen!");
				 
				 p.closeInventory();
				 e.setCancelled(true);
			 } else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
				 PlayerInfo pi = new PlayerInfo(Bukkit.getOfflinePlayer(playerCard.get(p)), playerChar.get(p)); 
				 geld = GeldInt.get(p);
				 afterPoint = backPoint.get(p);
				 double finish = Double.parseDouble(geld + "." + afterPoint);
				 money = Main.getPlugin().getConfig().getDouble("Spieler." + playerCard.get(p) + ".money");
				 Main.getPlugin().getConfig().set("Spieler." + playerCard.get(p) + ".money", money-finish);
				 Player t = Spieler.get(p);
				 money = Main.getPlugin().getConfig().getDouble("Spieler." + t.getUniqueId() + ".money");
				 Main.getPlugin().getConfig().set("Spieler." + t.getUniqueId() + ".money", money+finish);
				 Main.getPlugin().saveConfig();
				 p.sendMessage("§aDu hast §6" + new PlayerInfo(t).getFullName() + " §e" + geld + "€ §aüberwiesen");
				 t.sendMessage("§6" + pi.getFullName() + " §ahat dir §e" + geld + "€ §aüberwiesen");
				 t.sendMessage("§6Betreff: §e" + Betreff.get(p));
				 p.closeInventory();
				 e.setCancelled(true);
				 pi.addTransaction("§4-" + finish + "€ §6|| Überweisung an " + new PlayerInfo(t).getFullName() +  " | Betreff: " + Betreff.get(p) +  " |");
				 new PlayerInfo(t).addTransaction("§a+" + finish + "€ §6|| Überweisung von " + pi.getFullName() + " | Betreff: " + Betreff.get(p) +  " |");
				 Main.BroadcastLoc(p.getLocation(), 5, "§b*" + new PlayerInfo(p).getFullName() + " hat hat etwas am Bankautomaten gemacht");
			 } else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§2Akzeptieren")) {
				 e.setCancelled(true);
			 } else
				 e.setCancelled(true);
			
			e.setCancelled(true);
		} 
		} catch (Exception e2) {
		}

	}
	
	@EventHandler
	public void onNPCClick(RightClickNPC e) {
		if(!e.getNPC().getName().toLowerCase().contains("heike"))return; 
		if(!e.getPlayer().getItemInHand().getType().equals(Material.AIR))return;
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§eBankmenü");
		inv.setItem(0, new ItemBuilder("http://textures.minecraft.net/texture/c3e4b533e4ba2dff7c0fa90f67e8bef36428b6cb06c45262631b0b25db85b").setName("§6Karte Sperren").build());
		inv.setItem(1, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(2, new ItemBuilder("http://textures.minecraft.net/texture/461c8febcac21b9f63d87f9fd933589fe6468e93aa81cfcf5e52a4322e16e6").setName("§6PIN-Code ändern").build());
		inv.setItem(3, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
		inv.setItem(4, new ItemBuilder("http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7").setName("§6Neue Karte").build());
		e.getPlayer().sendMessage("Heike: Guten Tag. Ich bin Ihre Bankberaterin, Heike. Wie kann man Ihnen behilflich sein?");
		e.getPlayer().openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§eBankmenü")) {
				Player p = (Player) e.getWhoClicked();
				e.setCancelled(true);
				PlayerInfo pi = new PlayerInfo(p);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Karte Sperren")) {
					p.sendMessage(pi.getFullName() + ": Ich würde gerne meine Bankkarte sperren.");
					if(pi.getBankCard() != "") {
						pi.setBankCard("");
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								p.sendMessage("Heike: Ich habe die Karte jetzt gesperrt, kann ich Ihnen sonst noch helfen?");
								p.sendMessage("§aKarte wurde gesperrt!");	
							}
						}, 30);
					}else {
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								p.sendMessage("Heike: Tut mir leid, aber Sie haben keine Bankkarte.");
								p.sendMessage("§cBeantrage zuerst eine Karte!");
								
							}
						}, 30);
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6PIN-Code ändern")) {
					p.sendMessage(pi.getFullName() + ": Ich würde gerne meinen PIN-Code ändern.");
					if(pi.getBankCard() != "") {
						Inventory inv = Bukkit.createInventory(null, 9*5, "§6PIN-Code ändern");
						numpadinv(inv);
						inv.setItem(4, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
						inv.setItem(5, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
						inv.setItem(6, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
						inv.setItem(7, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
						inv.setItem(8, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
						p.openInventory(inv);
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								p.sendMessage("Heike: Bitte geben Sie den neuen PIN-Code ein.");
								Main.BroadcastLoc(p.getLocation(), 3, "§b*Heike reicht " + pi.getFullName() + " ein Nummernfeld");
							}
						}, 30);
						
					}else {
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								p.sendMessage("Heike: Tut mir leid, aber Sie haben keine Bankkarte.");
								p.sendMessage("§cBeantrage zuerst eine Karte!");
								
							}
						}, 30);
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Neue Karte")) {
					p.sendMessage(pi.getFullName() + ": Ich würde gerne eine neue Bankkarte haben.");
					if(pi.getBankCard() != "") {
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
							@Override
							public void run() {
								p.sendMessage("Heike: Tut mir leid, dazu müssen Sie erst die alte Karte sperren");
								p.sendMessage("§cSperre zuerst die alte Karte!");
							}
						}, 30);
						return;
					}
					String key = "";
					for(int i = 0; i < 14; i++) {
						Random generator = new Random();
						int num = generator.nextInt(9);
						key += num;
					}
					String uuid = UUID.randomUUID().toString();
					ItemStack bank = new ItemBuilder(Material.PAPER).setName("§6Bankkarte").setLore(key).setTag("ID", uuid).setTag("Own", p.getUniqueId().toString()).setTag("Char", PlayerInfo.getUUID(p).toString()).build();
			        pi.setBankCard(uuid);
			        p.getInventory().addItem(bank);
			        key = "";
					for(int i = 0; i < 4; i++) {
						Random generator = new Random();
						int num = generator.nextInt(9);
						key += num;
					}
					pi.setKeyCode(key);
					final String keyCode = key;
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							p.sendMessage("Heike: Hier ist die neue Karte. Passen Sie auf die Karte nicht zu verlieren.");
							Main.BroadcastLoc(p.getLocation(), 5, "§b*Heike gibt " + pi.getFullName() + " eine Bankkarte");
							p.sendMessage("§aDu hast eine neue Bankkarte erhalten!");
							p.sendMessage("§eDer PIN-Code ist §6" + keyCode);
						}
					}, 30);
				}
			}else if(e.getView().getTitle().equals("§6ATM Abbuchen") 
					|| e.getView().getTitle().equals("§6ATM Einzahlen") 
					|| e.getView().getTitle().equals("§6ATM Überweisen")
					|| e.getView().getTitle().equals("§6Von Firmenkonto abheben")
					|| e.getView().getTitle().equals("§6Auf Firmenkonto überweisen")) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				geld = 0;
				afterPoint = 0;
				if(GeldInt.containsKey(p))geld = GeldInt.get(p);
				if(backPoint.containsKey(p))afterPoint = backPoint.get(p);
				double finish = Double.parseDouble(geld + "." + afterPoint);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
					if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
						if(!isPoint.contains(p)) {
							isPoint.add(p);
							int length = 0;
							if(geld != 0)length = String.valueOf(geld).length();
							if(length >= 9)return;
							e.getClickedInventory().setItem(length, e.getCurrentItem());
						}
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§2Akzeptieren")) {
					KlickHören(p.getLocation(), Sound.ENTITY_CHICKEN_EGG);
					PlayerInfo pi = new PlayerInfo(Bukkit.getOfflinePlayer(playerCard.get(p)), playerChar.get(p));
					money = pi.getMoney();
					moneyInHand = new PlayerInfo(p).getMoneyInHand();
					FirmInfo fi = new FirmInfo();
					if(pi.getJob() > 0) {
						fi = new FirmInfo().loadfirm(pi.getJob());
					}
					if(e.getView().getTitle().equals("§6ATM Abbuchen")) {
						if(money >= finish) {
							double newMoney = money - finish;
							double newMoneyInHand = moneyInHand + finish;
							pi.setMoney(newMoney);
							new PlayerInfo(p).setMoneyInHand(newMoneyInHand);
							
							p.sendMessage("§6============§eKontostand§6============");
							p.sendMessage("§6Altes Guthaben: §e" + money + "€");
							p.sendMessage("§6Transaktion: §4-" + finish + "€");
							p.sendMessage("§6Neues Guthaben: §e" + newMoney + "€");
							p.sendMessage("§6=================================");
							Main.getPlugin().saveConfig();
							p.closeInventory();
							e.setCancelled(true);
							KlickHören(p.getLocation(), Sound.ENTITY_CHICKEN_EGG);
							pi.addTransaction("§4-" + geld + "€ §6|| Geldautomat |");
							Main.BroadcastLoc(p.getLocation(), 5, "§b*" + pi.getFullName() + " hat Geld vom Konto abgebucht");
						}else
							p.sendMessage("§cDazu hast du nicht genug Geld!");
						GeldInt.remove(p);
					}else if(e.getView().getTitle().equals("§6ATM Einzahlen")) {
						if(moneyInHand >= finish) {
							double newMoney = money + finish;
							double newMoneyInHand = moneyInHand - finish;
							pi.setMoney(newMoney);
							new PlayerInfo(p).setMoneyInHand(newMoneyInHand);
							
							p.sendMessage("§6============§eKontostand§6============");
							p.sendMessage("§6Altes Guthaben: §e" + money + "€");
							p.sendMessage("§6Transaktion: §a+" + finish + "€");
							p.sendMessage("§6Neues Guthaben: §e" + newMoney + "€");
							p.sendMessage("§6=================================");
							Main.getPlugin().saveConfig();
							p.closeInventory();
							KlickHören(p.getLocation(), Sound.ENTITY_CHICKEN_EGG);
							pi.addTransaction("§a+" + finish + "€ §6|| Geldautomat |");
							GeldInt.remove(p);
							Main.BroadcastLoc(p.getLocation(), 5, "§b*" + pi.getFullName() + " hat Geld auf das Konto eingezahlt");
						}else
							p.sendMessage("§cDazu hast du nicht genug Geld dabei!");
						e.setCancelled(true);
					}else if(e.getView().getTitle().equals("§6ATM Überweisen")) {
						if(money >= finish) {
							ChatInfo.addChat(p, ChatType.UEBERWEISEN, 600);
							
							p.sendMessage("§eGebe den Namen des Spielers an, dem du das Geld überweisen möchtest");
							p.closeInventory();
						}else
							p.sendMessage("§cDazu hast du nicht genug Geld!");
						e.setCancelled(true);
					}else if(e.getView().getTitle().equals("§6Auf Firmenkonto überweisen")) {
						if(money >= finish) {
							p.sendMessage("§6" + finish + "€ wurden auf das Firmenkonto überwiesen!");
							fi.setFirmmoney(fi.getFirmmoney() + finish);
							pi.subtractMoney(finish);
							
							p.closeInventory();
							pi.addTransaction("§4-" + finish + "€ §6|| Firmenüberweisung |");
							fi.addTransaction("§a+" + finish + "€ §6|| Überweisung von " + new PlayerInfo(p).getFullName() + " |");
						}else
							p.sendMessage("§cDazu hast du nicht genug Geld!");
						e.setCancelled(true);
						GeldInt.remove(p);
						Main.BroadcastLoc(p.getLocation(), 5, "§b*" + p.getName() + " hat hat etwas am Bankautomaten gemacht");
					}else if(e.getView().getTitle().equals("§6Von Firmenkonto abheben")) {
						if(fi.getFirmmoney() >= finish) {
							p.sendMessage("§6" + finish + "€ wurden von dem Firmenkonto abgebucht!");
							fi.setFirmmoney(fi.getFirmmoney() - finish);
							pi.setMoney(pi.getMoney() + finish);
							pi.addTransaction("§a+" + finish + "€ §6|| Firmenüberweisung |");
							fi.addTransaction("§4-" + finish + "€ §6|| Überweisung an " + p.getName() + " |");
							p.closeInventory();
						}else
							p.sendMessage("§cAuf dem Firmenkonto ist nicht genug Geld!");
						GeldInt.remove(p);
						Main.BroadcastLoc(p.getLocation(), 5, "§b*" + new PlayerInfo(p).getFullName() + " hat hat etwas am Bankautomaten gemacht");
						e.setCancelled(true);
					}
					p.closeInventory();
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Löschen")) {
					geld = 0;
					afterPoint = 0;
					GeldInt.put(p, 0);
					backPoint.put(p, 0);
					if(isPoint.contains(p))isPoint.remove(p);
					for(int i = 0; i < 9; i++) {
						e.getInventory().setItem(i, null);
					}
					KlickHören(p.getLocation(), Sound.BLOCK_CHEST_LOCKED);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Abbrechen")) {
					KlickHören(p.getLocation(), Sound.ENTITY_CHICKEN_EGG);
					p.closeInventory();
					GeldInt.remove(p);
				}else {
					int length = 0;
					if(isPoint.contains(p)) {
						length = String.valueOf(geld).length() + String.valueOf(afterPoint).length();
					}else
						if(geld != 0)length = String.valueOf(geld).length();
					if(length >= 9)return;
					if(String.valueOf(afterPoint).length() >=3)return;
					int klicked = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7 ", ""));
					e.getInventory().setItem(length, e.getCurrentItem());
					if(isPoint.contains(p)) {
						afterPoint = afterPoint*10 + klicked;
						backPoint.put(p, afterPoint);
					}else {
						geld = geld*10 + klicked;
						GeldInt.put(p, geld);
					}
					KlickHören(p.getLocation(), Sound.BLOCK_LEVER_CLICK);
				}
			}else if(e.getView().getTitle().equals("§6Bitte PIN-Code eingeben")
					|| e.getView().getTitle().equals("§6PIN-Code ändern")) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				String key = "";
				if(playerKey.containsKey(p))key = playerKey.get(p);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§2Akzeptieren")) {
					block = 0;
					PlayerBlock.put(p, block);
					p.closeInventory();
					if(e.getView().getTitle().equals("§6Bitte PIN-Code eingeben")) {
						OfflinePlayer pc = Bukkit.getOfflinePlayer(playerCard.get(p));
						if(new PlayerInfo(pc).getKeyCode().equals(key)) {
							PlayerInfo pi = new PlayerInfo((OfflinePlayer) e.getWhoClicked());
							Inventory Inv = Bukkit.createInventory(null, 27, "§6ATM");

							ItemStack Abbuchen = new ItemBuilder(Material.GOLD_BLOCK).setName("§6Abbuchen").build();
							ItemStack Einzahlen = new ItemBuilder(Material.IRON_BLOCK).setName("§6Einzahlen").build();
							ItemStack Überweisen = new ItemBuilder(Material.REDSTONE_BLOCK).setName("§6Überweisen").build();
							ItemStack Glas = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").build();
							ItemStack GesamtGeld = new ItemBuilder(Material.GOLD_INGOT).setName("§6" + new PlayerInfo(pc).getMoney() + "€").build();

							for (int i = 0; i < 27; i++) {
								if (i != 13 && i != 16 && i != 10) {
									Inv.setItem(i, Glas);
								}
							}
							
							
							if(p.getUniqueId().toString().equals(pc.getUniqueId().toString())) {
								if (pi.getJob() > 0) {
									FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
									ItemStack FirmEinzahlen = new ItemBuilder(Material.DIAMOND_BLOCK).setName("§6Auf Firmenkonto überweisen")
											.setLore(fi.getFirmname()).build();
									Inv.setItem(20, FirmEinzahlen);
									if (fi.hasrank(p, Rights.MAIN_CHARGEMONEY)) {
										ItemStack FirmAbbuchen = new ItemBuilder(Material.EMERALD_BLOCK).setName("§6Von Firmenkonto abheben")
												.setLore(String.valueOf(fi.getFirmmoney()) + "€").build();
										Inv.setItem(24, FirmAbbuchen);
									}
								}
							}

							Inv.setItem(10, Abbuchen);
							Inv.setItem(13, Einzahlen);
							Inv.setItem(16, Überweisen);
							Inv.setItem(22, GesamtGeld);

							p.openInventory(Inv);
						}else
							p.sendMessage("§4PIN falsch!");
					}else {
						new PlayerInfo(p).setKeyCode(key);
						p.sendMessage("§aPIN-Code wurde geändert!");
					}
					
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Löschen")) {
					key = "";
					playerKey.put(p, key);
					for(int i = 0; i < 4; i++) {
						e.getInventory().setItem(i, null);
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Abbrechen")) {
					p.closeInventory();
					playerKey.remove(p);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
					
				}else{
					if(key.length() >= 4)return;
					int klicked = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7 ", ""));
					e.getInventory().setItem(key.length(), e.getCurrentItem());
					key += klicked;
					playerKey.put(p, key);
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	
	public static void numpadinv(Inventory Inv) {

		for (int i = 9; i < 45; i++) {
			Inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
		}
		
		Inv.setItem(12, new ItemBuilder("http://textures.minecraft.net/texture/297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb").setName("§7 7").build());
		Inv.setItem(13, new ItemBuilder("http://textures.minecraft.net/texture/abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d").setName("§7 8").build());
		Inv.setItem(14, new ItemBuilder("http://textures.minecraft.net/texture/d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba").setName("§7 9").build());
		Inv.setItem(21, new ItemBuilder("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058").setName("§7 4").build());
		Inv.setItem(22, new ItemBuilder("http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763").setName("§7 5").build());
		Inv.setItem(23, new ItemBuilder("http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1").setName("§7 6").build());
		Inv.setItem(30, new ItemBuilder("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc").setName("§7 1").build());
		Inv.setItem(31, new ItemBuilder("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a").setName("§7 2").build());
		Inv.setItem(32, new ItemBuilder("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2").setName("§7 3").build());
		Inv.setItem(40, new ItemBuilder("http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5").setName("§7 0").build());
		Inv.setItem(37, new ItemBuilder(Material.RED_CONCRETE).setName("§6Abbrechen").build());
		Inv.setItem(43, new ItemBuilder(Material.LIME_CONCRETE).setName("§2Akzeptieren").build());
		Inv.setItem(25, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4Löschen").build());
	}

}