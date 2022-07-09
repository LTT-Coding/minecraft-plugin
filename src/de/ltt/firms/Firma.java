package de.ltt.firms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Firma implements CommandExecutor{

	public static HashMap<Player, Player> selledPlayer = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> selledPrice = new HashMap<Player, Integer>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInfo pi = new PlayerInfo(p); 
			if(pi.getJob() > 0) {
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				if(args.length != 0) {
						if(args[0].equalsIgnoreCase("einstellen")) {
							if(args.length == 2) {
								if(fi.getRights().contains(Rights.MAIN_INVITE.toString())) {
									if(fi.hasrank(p, Rights.MAIN_INVITE)) {
										Player t = Bukkit.getPlayer(args[1]);
										if(t != null) {
											if(t != p) {
												if(!fi.getMember().contains(t.getUniqueId().toString())) {
													if(!Main.inviteMap.containsKey(t)) {
														PlayerInfo ti = new PlayerInfo(t);
														if(ti.getJob() == 0) {
															Main.inviteMap.put(t, p);
															t.sendMessage("§6" + p.getName() + " §alädt dich in seine Firma §6" + fi.getFirmname() + " §aein, wähle eine Aktion!");
															TextComponent tc =  new TextComponent();
															tc.setText("§7[§aAnnehmen§7]");
															tc.setBold(true);
															tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/acceptfirminvite"));
															tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAnnehmen").create()));
															TextComponent tcl = new TextComponent();
															tcl.setText("   ");
															tcl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
															tcl.setClickEvent(new ClickEvent(Action.RUN_COMMAND, ""));
															tc.addExtra(tcl);
															TextComponent tc2 = new TextComponent();
															tc2.setText("§7[§4Ablehnen§7]");
															tc2.setBold(true);
															tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/denyfirminvite"));
															tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§l§7[§4Ablehnen§7]").create()));
															tc.addExtra(tc2);
															t.spigot().sendMessage(tc);
															p.sendMessage("§aEinladung abgesendet");
														}
													}else
														p.sendMessage("§cDer Spieler ist bereits in einer Firma!");
												}else
													p.sendMessage("§cDer Spieler ist bereits in dieser Firma!");
											}else
												p.sendMessage("§cDu kannst dich nicht selbst in eine Firma einladen!");
										}else
											p.sendMessage("§cDieser Spieler ist nicht online oder existiert nicht!");
									}else
										p.sendMessage("§cDazu ist dein Rang zu niedrig");
								}else
									p.sendMessage("§cDeine Firma ist dazu nicht berechtigt!");
							}else
								p.sendMessage("§cBenutze §6/invite [Spieler] §c um leute in deine Firma einzuladen");
						}else if(args[0].equalsIgnoreCase("kündigen") || args[0].equalsIgnoreCase("rauswerfen")) {
							if(args.length == 2) {
								OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
								if(fi.getMember().contains(t.getUniqueId().toString())) {
									if(fi.hasrank(p, Rights.MAIN_UNINVITE)) {
										
										if(t != p) {
											if(!t.getUniqueId().toString().equals(fi.getOwner())) {
												fi.removeMember(t);
												p.sendMessage("§6" + t.getName() + "§4 wurde erfolgreich entfernt!");
												if(t.isOnline()) {
													t.getPlayer().sendMessage("§4Du wurdest aus der Firma §6" + fi.getFirmname() + "§4 geworfen!");
												}else {
													new PlayerInfo(t).addToMailBox("§4Du wurdest aus der Firma §6" + fi.getFirmname() + "§4 geworfen!");
												}
											}else
												p.sendMessage("§cDu kannst den Leiter der Firma nicht rauswerfen");
										}else {
											if(!fi.getOwner().equals(p.getUniqueId().toString())) {
												fi.removeMember(t);
												pi.removeJob();
											}else {
												p.sendMessage("§cDu bist Leiter der Firma und kannst dich nicht rauswerfen!");
												p.sendMessage("§cBenutze §6/sellfirma [Spieler] [Preis] §cum deine Firma an jemanden zu verkaufen!");
											}
										}
									}else
										p.sendMessage(Main.KEINE_RECHTE);
								}else
									p.sendMessage("§cDieser Spieler ist nicht in dieser Firma!");
							}else 
								p.sendMessage("§cBenutze §6/firma kündigen [Spieler] §cum jemanden aus der Firma zu werfen!");
						}else if(args[0].equalsIgnoreCase("rechte")) {
							if(fi.hasrank(p, Rights.MAIN_SETRIGHTS)) {
								if(!SetRights.pRightsetter.containsKey(pi.getJob())) {
									int i;
									for(i = 0; i*9< fi.getMember().size(); i++) {
										
									}
									Inventory Inv = Bukkit.createInventory(null, i*9, "§eWähle einen Spieler aus!");
									for(int l = 0; l < fi.getMember().size(); l++) {
										OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(fi.getMember().get(l)));
										ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
										SkullMeta meta = (SkullMeta) item.getItemMeta();
										meta.setDisplayName("§6" + t.getName());
										meta.setOwner(t.getName());
										item.setItemMeta(meta);
										item.setAmount(1);
										Inv.setItem(l, item);
									}
									p.openInventory(Inv);
								}else
									p.sendMessage("§cJemand aus deiner Firma ist bereits in diesem Menü!");
							}else
								p.sendMessage(Main.KEINE_RECHTE);
						}else if(args[0].equalsIgnoreCase("bezahlung")) {
							if(fi.getRights().contains(Rights.MAIN_SETPAY.toString())) {
								if(fi.hasrank(p, Rights.MAIN_SETPAY)) {
									if(!SetPayment.ranksetter.containsKey(pi.getJob())) {
										Inventory payInv = Bukkit.createInventory(null, 9, "§eKlicke auf einen Rang");
										
										ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc");
										ItemMeta skull1Meta = skull1.getItemMeta();
										skull1Meta.setDisplayName("§71");
										skull1Meta.setLore(Arrays.asList( fi.getPay().get(1).toString() + "$"));
										skull1.setItemMeta(skull1Meta);
										skull1.setAmount(1);

										ItemStack skull2 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a");
										ItemMeta skull2Meta = skull2.getItemMeta();
										skull2Meta.setDisplayName("§72");
										skull2Meta.setLore(Arrays.asList( fi.getPay().get(2).toString() + "$"));
										skull2.setItemMeta(skull2Meta);
										skull2.setAmount(1);

										ItemStack skull3 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2");
										ItemMeta skull3Meta = skull3.getItemMeta();
										skull3Meta.setDisplayName("§73");
										skull3Meta.setLore(Arrays.asList( fi.getPay().get(3).toString() + "$"));
										skull3.setItemMeta(skull3Meta);
										skull3.setAmount(1);

										ItemStack skull4 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058");
										ItemMeta skull4Meta = skull4.getItemMeta();
										skull4Meta.setDisplayName("§74");
										skull4Meta.setLore(Arrays.asList( fi.getPay().get(4).toString() + "$"));
										skull4.setItemMeta(skull4Meta);
										skull4.setAmount(1);

										ItemStack skull5 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763");
										ItemMeta skull5Meta = skull5.getItemMeta();
										skull5Meta.setDisplayName("§75");
										skull5Meta.setLore(Arrays.asList( fi.getPay().get(5).toString() + "$"));
										skull5.setItemMeta(skull5Meta);
										skull5.setAmount(1);

										ItemStack skull6 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1");
										ItemMeta skull6Meta = skull6.getItemMeta();
										skull6Meta.setDisplayName("§76");
										skull6Meta.setLore(Arrays.asList( fi.getPay().get(6).toString() + "$"));
										skull6.setItemMeta(skull6Meta);
										skull6.setAmount(1);
										
										ItemStack skull0 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5");
										ItemMeta skull0Meta = skull0.getItemMeta();
										skull0Meta.setLore(Arrays.asList( fi.getPay().get(0).toString() + "$"));
										skull0Meta.setDisplayName("§70");
										skull0.setItemMeta(skull0Meta);
										
										
										
										ItemStack skullreset = ItemSkulls.getSkull("http://textures.minecraft.net/texture/c4e490e1658bfde4d4ef1ea7cd646c5353377905a1369b86ee966746ae25ca7");
										ItemMeta resetmeta = skullreset.getItemMeta();
										resetmeta.setDisplayName("§4Zurücksetzen");
										skullreset.setItemMeta(resetmeta);
										
										payInv.addItem(skull0);
										payInv.addItem(skull1);
										payInv.addItem(skull2);
										payInv.addItem(skull3);
										payInv.addItem(skull4);
										payInv.addItem(skull5);
										payInv.addItem(skull6);
										payInv.addItem(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());
										payInv.addItem(skullreset);
										p.openInventory(payInv);
										
									}else
										p.sendMessage("§cEs ist bereits jemand aus deiner Firma in diesem Menü");
								}else
									p.sendMessage(Main.KEINE_RECHTE);
							}else
								p.sendMessage("§cDazu ist deine Firma nicht befugt");
						}else if(args[0].equalsIgnoreCase("degradieren")) {
							if(fi.getRights().contains(Rights.MAIN_RANKDOWN.toString())) {
								if(fi.hasrank(p, Rights.MAIN_RANKDOWN)) {
									if(args.length == 2) {
										OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);  
										if(t != null) {
											PlayerInfo ti = new PlayerInfo(t);
											if(ti.getJob() != 0) {
												if(ti.getJob() == pi.getJob()) {
													if(fi.getRank(p) > fi.getRank(t) | fi.getRank(p) == 6) {
														if(t.getUniqueId().toString() != fi.getOwner()) {
															if(fi.getRank(t) > 0) {
																fi.rankDown(t);
																if(t.isOnline()) {
																	Player to = (Player) t;
																	p.sendMessage("§aDu hast §6" + t.getName() + " Rang " + fi.getRank(t) + "§a gegeben");
																	to.sendMessage("§6" + p.getName() + " §a hat dir §6Rang " + fi.getRank(t) + " §agegeben");
																}else {
																	p.sendMessage("§aDu hast §6" + t.getName() + " Rang " + fi.getRank(t) + "§a gegeben");
																}
															}else {
																p.sendMessage("§cDer Spieler kann nicht weiter degradiert werden. Möchtest du ihn rauswerfen?");
																TextComponent tc2 = new TextComponent();
																tc2.setText("[Rauswerfen]");
																tc2.setColor(ChatColor.DARK_RED);
																tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7[§4Rauswerfen§7]").create()));
																tc2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/firma kündigen " + t.getName()));
																p.spigot().sendMessage(tc2);
															}
														}else
															p.sendMessage("§cDu kannst den Leiter der Firma nicht degradieren!");
													}else
														p.sendMessage("Du kannst keine Spieler befördern, die den gleichen Rang haben wie du!");
												}else
													p.sendMessage("§cDer Spieler arbeitet nicht in deiner Firma");
											}else
												p.sendMessage("§cDer Spieler arbeitet in keiner Firma!");
										}else
											p.sendMessage("§cDieser Spieler existiert nicht!");
									}else
										p.sendMessage("§cBenutze §6/degradieren [Spieler] §cum jemanden zu befördern!");
								}else
									p.sendMessage(Main.KEINE_RECHTE);
							}else
								p.sendMessage("§cDazu ist deine Firma nicht befugt!");
						}else if(args[0].equalsIgnoreCase("befördern")) {
							if(fi.getRights().contains(Rights.MAIN_RANKUP.toString())) {
								if(fi.hasrank(p, Rights.MAIN_RANKUP)) {
									if(args.length == 2) {
										OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
										if(t != null) {
											PlayerInfo ti = new PlayerInfo(t);
											if(ti.getJob() != 0) {
												if(ti.getJob() == pi.getJob()) {
													if(fi.getRank(p) > fi.getRank(t)) {
														if(t.isOnline()) {
															fi.rankUp(t);
															Player to = (Player) t;
															p.sendMessage("§aDu hast §6" + t.getName() + " Rang " + fi.getRank(t) + "§a gegeben!");
															to.sendMessage("§6" + p.getName() + " §a hat dir §6Rang " + fi.getRank(t) + " §agegeben!");
														}else 
															p.sendMessage("§cDer Spieler ist nicht Online und kann nicht befördert werden!");
													}else
														p.sendMessage("§cDu kannst keine Spieler befördern, die den gleichen Rang haben wie du!");
												}else
													p.sendMessage("§cDer Spieler arbeitet nicht in deiner Firma");
											}else
												p.sendMessage("§cDer Spieler arbeitet in keiner Firma!");
										}else
											p.sendMessage("§cDieser Spieler existiert nicht!");
									}else
										p.sendMessage("§cBenutze §6/befoerdern [Spieler] §cum jemanden zu befördern!");
								}else
									p.sendMessage(Main.KEINE_RECHTE);
							}else
								p.sendMessage("§cDazu ist deine Firma nicht befugt!");
						}else if(args[0].equalsIgnoreCase("umbenennen")) {
							if(fi.getRights().contains(Rights.MAIN_CHANGENAME.toString())) {
								if(fi.hasrank(p, Rights.MAIN_CHANGENAME)) {
									if(args.length == 1) {
										ChatInfo.addChat(p, ChatType.CHANGEFIRMNAME, 30*20);
										p.sendMessage("§eGebe den neuen Firmennamen ein:");
										p.sendMessage("§eSchreibe §6'cancel'§e um den Vorgang abzubrechen!");
									}else
										p.sendMessage("§cBenutze §6/firma umbenennen§c um die Firma umzubenennen!");
								}else
									p.sendMessage(Main.KEINE_RECHTE);
							}else
								p.sendMessage("§cDazu ist deine Firma nicht befugt!");
						}else if(args[0].equalsIgnoreCase("verkaufen")) {
							if(fi.getOwner().equals(p.getUniqueId().toString())) {
								if(args.length == 3) {
									Player t = Bukkit.getPlayer(args[1]);
									if(t != null) {
										PlayerInfo ti = new PlayerInfo(t);
										if(ti.getJob() == 0 || fi.getMember().contains(p.getUniqueId().toString())) {
											try {
												if(Float.parseFloat(args[2]) < Integer.MAX_VALUE) {
													int price = Integer.parseInt(args[2]);
									 				selledPlayer.put(t, p);
													selledPrice.put(t, price);
													t.sendMessage("§6" + p.getName() + "§e möchte dir die Firma §6" + fi.getFirmname() + "§e für §6" + price + "€§e verkaufen!");
													t.sendMessage("§eWähle eine Aktion:");
													TextComponent tc = new TextComponent();
													tc.setText("§7[§aAnnehmen§7]");
													tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptfirmbuy"));
													tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
													tc.setBold(true);
													TextComponent leer = new TextComponent();
													leer.setText("  ");
													leer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
													leer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
													leer.setBold(true);
													TextComponent tc2 = new TextComponent();
													tc2.setText("§7[§4Ablehnen§7]");
													tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/denyfirmbuy"));
													tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Ablehnen").create()));
													tc2.setBold(true);
													tc.addExtra(leer);
													tc.addExtra(tc2);
													t.spigot().sendMessage(tc);
													p.sendMessage("§aAnfrage abgesendet!");
												}else
													p.sendMessage("§cDiese Zahl ist zu groß!");
											} catch (Exception e) {
												p.sendMessage("§cDeine Eingabe ist keine Zahl!");
											}
										}
									}else
										p.sendMessage("§cDieser Spieler ist nicht online oder existiert nicht!");
								}else 
									p.sendMessage("§cBenutze §6/firma verkaufen [Spieler] [Preis] §cum die Firma ein jemanden zu verkaufen!");
							}else
								p.sendMessage("§cDazu musst du der Besitzer der Firma sein!");
						}
				}else {
					
				}
			}else
				p.sendMessage("§cDu arbeitest in keiner Firma!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
