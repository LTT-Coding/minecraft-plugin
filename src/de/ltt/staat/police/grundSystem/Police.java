package de.ltt.staat.police.grundSystem;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.bank.listener.Bank_System;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.MySQL;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.staat.police.equip.PoliceEquip;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Police implements CommandExecutor, Listener {

	public static HashMap<Player, Player> InviAcc = new HashMap<Player, Player>();
	public static HashMap<Player, String> EquipOption = new HashMap<Player, String>();
	HashMap<Player, Integer> GeldInt = new HashMap<Player, Integer>();


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);
			PoliceInfo mi = new PoliceInfo();
			if (pi.getJob() == -2 || Main.Admin.contains(p.getUniqueId().toString())
					|| Main.bürgermeister.equals(p.getUniqueId().toString())) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("einstellen")) {
						if(args.length == 2) {
							OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
							if (mi.hasRight(p, PoliceRights.INVITE)) {
								if (!InviAcc.containsKey(t)) {
									PlayerInfo ti = new PlayerInfo(t);
									if (ti.getJob() == 0) {
										if (t.isOnline()) {
											InviAcc.put(t.getPlayer(), p);
											t.getPlayer().sendMessage("§6" + p.getName() + " §alädt dich in den §6Polizei §aein, wähle eine Aktion!");
											TextComponent tc = new TextComponent();
											tc.setText("§7[§aAnnehmen§7]");
											tc.setBold(true);
											tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/acceptpolizeiinvite"));
											tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAnnehmen").create()));
											TextComponent tcl = new TextComponent();
											tcl.setText("   ");
											tcl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
											tcl.setClickEvent(new ClickEvent(Action.RUN_COMMAND, ""));
											tc.addExtra(tcl);
											TextComponent tc2 = new TextComponent();
											tc2.setText("§7[§4Ablehnen§7]");
											tc2.setBold(true);
											tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/denypolizeiinvite"));
											tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§l§7[§4Ablehnen§7]").create()));
											tc.addExtra(tc2);
											t.getPlayer().spigot().sendMessage(tc);
											p.sendMessage("§aEinladung abgesendet");
										} else
											p.sendMessage("§cDieser Spieler ist nicht online!");
									} else
										p.sendMessage("§cDer Spieler ist bereits in einer Firma!");
								} else
									p.sendMessage("§cDiese Person wurde bereits zur §6Polizei eingeladen!");
							} else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBitte benutze /polizei einstellen/rauswerfen <Name> oder /polizei rechte/bezahlung");
					} else if (args[0].equalsIgnoreCase("kündigen") || args[0].equalsIgnoreCase("rauswerfen")) {
						if(args.length == 2) {
							OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
							if (mi.hasRight(p, PoliceRights.UNINVITE)) {
								if(mi.getMember().contains(t.getUniqueId().toString())) {
									mi.removeMember(t);
									if (t.getUniqueId().toString() != mi.getOwner()) {
										if (t.isOnline()) {
											t.getPlayer().sendMessage("§6Du wurdest aus der §6Polizei§c geworfen");
										}
									} else
										p.sendMessage("§cDu kannst den Besitzer der Polizei nicht rauswerfen!");
									p.sendMessage("§aDu hast " + t.getName() + " aus der §6Polizei§c geworfen");
								}else
									p.sendMessage("§cDieser Spieler arbeitet nicht bei der§6 Polizei!");
							} else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBitte benutze /polizei einstellen/rauswerfen <Name> oder /polizei rechte/bezahlung");
					} else if (args[0].equalsIgnoreCase("rechte")) {
						if (mi.hasRight(p, PoliceRights.SETRIGHTS)) {							
							if (!SetPoliceRights.pRightsetter.containsKey(pi.getJob())) {
								if(mi.getMember().size() != 0) {
									int i;
									for (i = 0; i * 9 < mi.getMember().size(); i++) {

									}
									Inventory Inv = Bukkit.createInventory(null, i * 9,"§1Wähle einen Spieler aus");
									for (int l = 0; l < mi.getMember().size(); l++) {
										OfflinePlayer t2 = Bukkit.getOfflinePlayer(UUID.fromString(mi.getMember().get(l)));
										ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
										SkullMeta meta = (SkullMeta) item.getItemMeta();
										meta.setDisplayName("§6" + t2.getName());
										meta.setOwner(t2.getName());
										item.setItemMeta(meta);
										item.setAmount(1);
										Inv.setItem(l, item);
									}
									p.openInventory(Inv);
								} else
									p.sendMessage("§cEs gibt in der Polizei keine Mitarbeiter!");
							} else
								p.sendMessage("§cJemand aus der §6Polizei§c ist bereits in diesem Menü!");
						} else
							p.sendMessage(Main.KEINE_RECHTE);
					} else if (args[0].equalsIgnoreCase("bezahlung")) {
						if (mi.hasRight(p, PoliceRights.SETRANKPAY) || mi.hasRight(p, PoliceRights.SETPLAYERPAY)) {
							if (!SetPolicePay.pRightsetter) {
								Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§1Wähle einen Modus");
								ItemStack item = ItemSkulls.getSkull("http://textures.minecraft.net/texture/78a81efdae47bcb480a25ed91ff6de9772b07ae87c3c4e277705abbbd3419");
								ItemMeta meta = item.getItemMeta();
								meta.setDisplayName("§6Bezahlungsstufen einstellen");
								item.setItemMeta(meta);
								ItemStack glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build();
								for (int i = 0; i < 5; i++) {
									inv.setItem(i, glass);
								}
								inv.setItem(1, item);
								inv.setItem(3, new ItemBuilder(Material.PLAYER_HEAD).setName("§6Bezahlung einzeln einstellen").build());
								p.openInventory(inv);
								SetPolicePay.GeldInt.put(p, 0);
							} else
								p.sendMessage("§cJemand aus der §6Polizei§c ist bereits in diesem Menü!");
						} else
							p.sendMessage(Main.KEINE_RECHTE);
					} else if (args[0].equalsIgnoreCase("besitzer")) {
						if (args.length == 2) {
							if (Main.Admin.contains(p.getUniqueId().toString())
									|| Main.bürgermeister.equals(p.getUniqueId().toString())) {
								Player t = Bukkit.getPlayer(args[1]);
								if(t != null) {
									PlayerInfo ti = new PlayerInfo(t);
									if(ti.getJob() == 0 || ti.getJob() == -1) {
										if(mi.getOwner() != "" && mi.getOwner() != null) {
											OfflinePlayer own = Bukkit.getOfflinePlayer(UUID.fromString(mi.getOwner()));
											if(own.isOnline()) {
												own.getPlayer().sendMessage("§4Du wurdest als Besitzer entfernt und aus der §6Polizei §4geworfen!");
											}else
												new PlayerInfo(own).addToMailBox("§4Du wurdest als Besitzer entfernt und aus der§6 Polizei §4geworfen!");
											mi.removeMember(own);
										}
										if(pi.getJob() == -1) {
											mi.setOwner(t.getUniqueId().toString());
											mi.setpRights(t, mi.getRights());
										}else {
											mi.addmember(t);
											mi.setOwner(t.getUniqueId().toString());
											mi.setpRights(t, mi.getRights());
										}
										t.sendMessage("§aDu wurdest als Besitzer der §6Polizei§a eingesetzt!");
										p.sendMessage("§6" + t.getName() + "§a wurde als Besitzer für die §6Polizei§a eingesetzt!");
									}else
										p.sendMessage("§cDer Spieler muss arbeitslos sein, oder bei der §6Polizei §carbeiten!");
								}else
									p.sendMessage("§cDiese Person ist nicht online ode existiert nicht!");
							} else
								p.sendMessage("§cDazu musst du §6Bürgermeister §csein!");
						} else {
							if(mi.getOwner() != "" && mi.getOwner() != null) {
								p.sendMessage("§eDer Besitzer der §6Polizei§e ist §6" + Bukkit.getOfflinePlayer(UUID.fromString(mi.getOwner())).getName());
							}else
								p.sendMessage("§eDie §6Polizei§e hat aktuell §4keinen§e Besitzer!");
						}
					}else if(args[0].equalsIgnoreCase("befördern")) {
						if(args.length == 2) {
							if(mi.hasRight(p, PoliceRights.RANKUP)){
								Player t = Bukkit.getPlayer(args[1]);
								if(t != null) {
									if(mi.getMember().contains(t.getUniqueId().toString())) {
										if(t.getUniqueId().toString() != mi.getOwner() || p.getUniqueId().toString().equals(mi.getOwner())) {
											if(mi.getRank(t) < mi.getRank(p) || mi.getOwner().equals(p.getUniqueId().toString()) || Main.Admin.contains(p.getUniqueId().toString())) {
												if(mi.getRank(t) < 8) {
													mi.rankUp(t);
													p.sendMessage("§aDu hast §6" + t.getName() + "§a auf §6Stufe " + mi.getRank(t) + "§a befördert!");
													t.sendMessage("§aDu wurdest auf §6Stufe " + mi.getRank(t) + "§a befördert!");
												}else 
													p.sendMessage("§cDiese Person hat bereits die höchste Stufe!");
											}else
												p.sendMessage("§cDu musst Besitzer der §6Polizei§c sein um jemanden auf eine Stufe über dich zu setzen!");
										}
									}else
										p.sendMessage("§cDiese Person arbeitet nicht bei der §6Polizei§c!");
								}else
									p.sendMessage("§cDiese Person ist nicht online oder existiert nicht!");
							}else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBenutze §6/polizei befördern <Spieler> §cum einen Mitarbeiter zu befördern!");
					}else if(args[0].equalsIgnoreCase("degradieren")) {
						if(args.length == 2) {
							if(mi.hasRight(p, PoliceRights.RANKDOWN)){
								Player t = Bukkit.getPlayer(args[1]);
								if(t != null) {
									if(mi.getMember().contains(t.getUniqueId().toString())) {
										if(t.getUniqueId().toString() != mi.getOwner() || mi.getOwner().equals(p.getUniqueId().toString())) {
											if(mi.getRank(t) < mi.getRank(p) || p.getUniqueId().toString().equals(mi.getOwner()) || Main.Admin.contains(p.getUniqueId().toString())) {
												if(mi.getRank(t) > 0) {
													mi.rankDown(t);
													p.sendMessage("§aDu hast §6" + t.getName() + "§a auf §6Stufe " + mi.getRank(t) + "§a degradiert!");
													t.sendMessage("§4Du wurdest auf §6Stufe " + mi.getRank(t) + "§4 degradiert!");
												}else {
													p.sendMessage("§cDiese Person hat bereits die niedrigste Stufe!");
												}
											}else
												p.sendMessage("§cDu musst Besitzer der §6Polizei§c sein um jemanden auf eine Stufe über dich zu setzen!");
										}else
											p.sendMessage("§cDu kannst den Besitzer nicht degradieren!");
									}else
										p.sendMessage("§cDiese Person arbeitet nicht bei der §6Polizei§c!");
								}else
									p.sendMessage("§cDiese Person ist nicht online oder existiert nicht!");
							}else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBenutze §6/polizei befördern <Spieler> §cum einen Mitarbeiter zu degradieren!");
					}else if(args[0].equalsIgnoreCase("equip")) {
						if(mi.hasRight(p, PoliceRights.EQUIPEINSTELLEN)) {
							p.sendMessage("§eDrück auf das Item welches du einstellen möchtest.");
							Inventory inv = Bukkit.createInventory(null, 2*9, "§1Polizei Equip einstellen");
							for (int i = 0; i < inv.getSize(); i++) {
								inv.setItem(i, PoliceEquip.items.get(i));
							}
							p.openInventory(inv);
						}
					}else
						p.sendMessage("§cBitte benutze /polizei einstellen/rauswerfen <Name> oder /polizei rechte/bezahlung");
				} else
					p.sendMessage("§cBitte benutze /polizei invite/uninvite <Name>");
			} else
				p.sendMessage("§cDafür musst du in §6Polizei§c sein");
		}
		return false;
	}
	
	@EventHandler
	public void onEquip(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		try {
			if(e.getView().getTitle().equals("§1Polizei Equip einstellen") && e.getInventory() == e.getView().getTopInventory()) {
				 String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
				 if(e.getCurrentItem().getItemMeta().getDisplayName().equals(itemName) && !(e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE)) {
					 EquipOption.put(p, itemName);
					 p.sendMessage("§eDu stellst grade den Preis für §6" + itemName + "§e ein");
					 p.closeInventory();
					 Inventory inv = Bukkit.createInventory(null, 9*5, "§1Polizei Equip ändern");
					 Bank_System.numpadinv(inv);
					 p.openInventory(inv);
				 }
				 e.setCancelled(true);
			} else if(e.getView().getTitle().equals("§1Polizei Equip ändern") && e.getInventory() == e.getView().getTopInventory()) {
				e.setCancelled(true);
				int geld = 0;
				if(GeldInt.containsKey(p))geld = GeldInt.get(p);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
					
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§2Akzeptieren")) {
					SQLData.addPoliceEquipPrices(EquipOption.get(p), geld);
					p.closeInventory();
					p.sendMessage("§eDu hast von §6" + EquipOption.get(p) + "§e den Preis zu §6" + geld + "€§e geändert");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Löschen")) {
					geld = 0;
					GeldInt.put(p, 0);
					for(int i = 0; i < 9; i++) {
						e.getInventory().setItem(i, null);
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Abbrechen")) {
					p.closeInventory();
					GeldInt.remove(p);
					p.sendMessage("§eDer Preis von §6" + EquipOption.get(p) + "§e hat sich nicht geändert");
				}else {
					int length = 0;
					if(geld != 0)length = String.valueOf(geld).length();
					if(length >= 9)return;
					int klicked = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7 ", ""));
					e.getInventory().setItem(length, e.getCurrentItem());
					geld = geld*10 + klicked;
					GeldInt.put(p, geld);
				}
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}

}
