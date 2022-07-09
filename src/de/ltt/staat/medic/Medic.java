package de.ltt.staat.medic;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Medic implements CommandExecutor {

	public static HashMap<Player, Player> InviAcc = new HashMap<Player, Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p);
			MedicInfo mi = new MedicInfo();
			if (pi.getJob() == -1 || Main.Admin.contains(p.getUniqueId().toString())
					|| Main.bürgermeister.equals(p.getUniqueId().toString())) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("einstellen")) {
						if(args.length == 2) {
							OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
							if (mi.hasRight(p, MedicRights.INVITE)) {
								if (!InviAcc.containsKey(t)) {
									PlayerInfo ti = new PlayerInfo(t);
									if (ti.getJob() == 0) {
										if (t.isOnline()) {
											InviAcc.put(t.getPlayer(), p);
											t.getPlayer().sendMessage("§6" + p.getName() + " §alädt dich in den §&Rettungsdienst §aein, wähle eine Aktion!");
											TextComponent tc = new TextComponent();
											tc.setText("§7[§aAnnehmen§7]");
											tc.setBold(true);
											tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/acceptmedicinvite"));
											tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAnnehmen").create()));
											TextComponent tcl = new TextComponent();
											tcl.setText("   ");
											tcl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
											tcl.setClickEvent(new ClickEvent(Action.RUN_COMMAND, ""));
											tc.addExtra(tcl);
											TextComponent tc2 = new TextComponent();
											tc2.setText("§7[§4Ablehnen§7]");
											tc2.setBold(true);
											tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/denymedicinvite"));
											tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§l§7[§4Ablehnen§7]").create()));
											tc.addExtra(tc2);
											t.getPlayer().spigot().sendMessage(tc);
											p.sendMessage("§aEinladung abgesendet");
										} else
											p.sendMessage("§cDieser Spieler ist nicht online!");
									} else
										p.sendMessage("§cDer Spieler ist bereits in einer Firma!");
								} else
									p.sendMessage("§cDiese Person wurde bereits zum §6Rettungsdienst eingeladen!");
							} else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBitte benutze /rettungsdienst einstellen/rauswerfen <Name> oder /rettungsdienst rechte/bezahlung");
					} else if (args[0].equalsIgnoreCase("kündigen") || args[0].equalsIgnoreCase("rauswerfen")) {
						if(args.length == 2) {
							OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
							if (mi.hasRight(p, MedicRights.UNINVITE)) {
								if(mi.getMember().contains(t.getUniqueId().toString())) {
									mi.removeMember(t);
									if (t.getUniqueId().toString() != mi.getOwner()) {
										if (t.isOnline()) {
											t.getPlayer().sendMessage("§6Du wurdest aus dem §6Rettungsdienst§c geworfen");
										}
									} else
										p.sendMessage("§cDu kannst den Besitzer des Rettungsdienstes nicht rauswerfen!");
									p.sendMessage("§aDu hast " + t.getName() + " aus dem §6Rettungsdienst§c geworfen");
								}else
									p.sendMessage("§cDieser Spieler arbeitet nicht beim§6 Rettungsdienst!");
							} else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBitte benutze /rettungsdienst einstellen/rauswerfen <Name> oder /rettungsdienst rechte/bezahlung");
					} else if (args[0].equalsIgnoreCase("rechte")) {
						if (mi.hasRight(p, MedicRights.SETRIGHTS)) {							
							if (!SetMedicRights.pRightsetter.containsKey(pi.getJob())) {
								if(mi.getMember().size() != 0) {
									int i;
									for (i = 0; i * 9 < mi.getMember().size(); i++) {

									}
									Inventory Inv = Bukkit.createInventory(null, i * 9,"§4Wähle einen Spieler aus");
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
								}
							} else
								p.sendMessage("§cJemand aus dem §6Rettungsdienst§c ist bereits in diesem Menü!");
						} else
							p.sendMessage(Main.KEINE_RECHTE);
					} else if (args[0].equalsIgnoreCase("bezahlung")) {
						if (mi.hasRight(p, MedicRights.SETRANKPAY) || mi.hasRight(p, MedicRights.SETPLAYERPAY)) {
							if (!SetMedicPay.pRightsetter) {
								Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§4Wähle einen Modus");
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
								SetMedicPay.GeldInt.put(p, 0);
							} else
								p.sendMessage("§cJemand aus dem §6Rettungsdienst§c ist bereits in diesem Menü!");
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
												own.getPlayer().sendMessage("§4Du wurdest als Besitzer entfernt und aus dem §6Rettungsdienst §4geworfen!");
											}else
												new PlayerInfo(own).addToMailBox("§4Du wurdest als Besitzer entfernt und aus dem§6 Rettungsdienst §4geworfen!");
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
										t.sendMessage("§aDu wurdest als Besitzer des §6Rettungsdienstes§a eingesetzt!");
										p.sendMessage("§6" + t.getName() + "§a wurde als Besitzer für den §6Rettungsdienst§a eingesetzt!");
									}else
										p.sendMessage("§cDer Spieler muss arbeitslos sein, oder beim §6Rettungsdienst §carbeiten!");
								}else
									p.sendMessage("§cDiese Person ist nicht online ode existiert nicht!");
							} else
								p.sendMessage("§cDazu musst du §6Bürgermeister §csein!");
						} else {
							if(mi.getOwner() != "" && mi.getOwner() != null) {
								p.sendMessage("§eDer Besitzer des §6Rettungsdienstes§e ist §6" + Bukkit.getOfflinePlayer(UUID.fromString(mi.getOwner())).getName());
							}else
								p.sendMessage("§eDer §6Rettungsdienst§e hat aktuell §4keinen§e Besitzer!");
						}
					}else if(args[0].equalsIgnoreCase("befördern")) {
						if(args.length == 2) {
							if(mi.hasRight(p, MedicRights.RANKUP)){
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
												p.sendMessage("§cDu musst Besitzer des §6Rettungsdienstes§c sein um jemanden auf eine Stufe über dich zu setzen!");
										}
									}else
										p.sendMessage("§cDiese Person arbeitet nicht beim §6Rettungsdienst§c!");
								}else
									p.sendMessage("§cDiese Person ist nicht online oder existiert nicht!");
							}else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBenutze §6/rettungsdienst befördern <Spieler> §cum einen Mitarbeiter zu befördern!");
					}else if(args[0].equalsIgnoreCase("degradieren")) {
						if(args.length == 2) {
							if(mi.hasRight(p, MedicRights.RANKDOWN)){
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
												p.sendMessage("§cDu musst Besitzer des §6Rettungsdienstes§c sein um jemanden auf eine Stufe über dich zu setzen!");
										}else
											p.sendMessage("§cDu kannst den Besitzer nicht degradieren!");
									}else
										p.sendMessage("§cDiese Person arbeitet nicht beim §6Rettungsdienst§c!");
								}else
									p.sendMessage("§cDiese Person ist nicht online oder existiert nicht!");
							}else
								p.sendMessage(Main.KEINE_RECHTE);
						}else
							p.sendMessage("§cBenutze §6/rettungsdienst befördern <Spieler> §cum einen Mitarbeiter zu degradieren!");
					}else
						p.sendMessage("§cBitte benutze /rettungsdienst einstellen/rauswerfen <Name> oder /rettungsdienst rechte/bezahlung");
				} else
					p.sendMessage("§cBitte benutze /rettungsdienst invite/uninvite <Name>");
			} else
				p.sendMessage("§cDafür musst du im §6Rettungsdienst§c sein");
		}
		return false;
	}

}
