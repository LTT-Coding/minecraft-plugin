package de.ltt.bank.listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;

public class KontoAuszug implements Listener{

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			if(e.getLine(0) != null && e.getLine(0) != "") {
				if(e.getLine(0).equalsIgnoreCase("/auszug")) {
					e.setLine(0, "ßa[Kontoauszug]");
					e.setLine(1, "ß7[__]");
					e.setLine(3, "ß7[========]");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getState() instanceof Sign) {
				Sign sign  = (Sign) e.getClickedBlock().getState();
				Player p = e.getPlayer();
				PlayerInfo pi = new PlayerInfo(p);
				if(sign.getLine(0) != null && sign.getLine(0) != "") {
					if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Kontoauszug]")) {
						Inventory inv  = Bukkit.createInventory(null, InventoryType.HOPPER, "ßeKontoauszug");
						for(int i = 0; i < 5; i++) {
							inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("").build());
						}
						if(pi.getJob() > 0) {
							if(new FirmInfo().loadfirm(pi.getJob()).hasrank(p, Rights.MAIN_CHARGEMONEY)) {
								inv.setItem(1, new ItemBuilder(Material.IRON_BLOCK).setName("ß6Privates Konto").build());
								inv.setItem(3, new ItemBuilder(Material.EMERALD_BLOCK).setName("ß6Firmenkonto").build());
							}else 
								inv.setItem(2, new ItemBuilder(Material.IRON_BLOCK).setName("ß6Privates Konto").build());
						}else
							inv.setItem(2, new ItemBuilder(Material.IRON_BLOCK).setName("ß6Privates Konto").build());
							
						p.openInventory(inv);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("ßeKontoauszug") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				PlayerInfo pi = new PlayerInfo(p);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("ß6Privates Konto")) {
					List<String> transactions = pi.getTransactions();
					if(transactions.size() != 0) {
						List<String> pages = new ArrayList<String>();
						LocalDateTime myDateObj = LocalDateTime.now();
						DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					    String formattedDate = myDateObj.format(myFormatObj);
						pages.add("ß6Kontoauszug von dem Konto von " + p.getName() + "\nbis einschlieﬂlich " + formattedDate);
						int page = 1;
						pages.add("");
						List<String> toRemove = new ArrayList<String>();
						for(String current : transactions) {
							int length = pages.get(page).length() + current.length();
							if(page < 50) {
								if(length > 256) {
									page++;
									pages.add("ß6" + current);
								}else {
									String text = pages.get(page);
									text += "\n " + current;
									pages.set(page, text);
								}
								toRemove.add(current);
							}
				
						}
						for(String current : toRemove) {
							transactions.remove(current);
						}
						pi.setTransctions(transactions);
						ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
						BookMeta meta = (BookMeta) item.getItemMeta();
						meta.setPages(pages);
						meta.setAuthor(p.getName());
						meta.setTitle("ß6Kontoauszug");
						item.setItemMeta(meta);
						p.getInventory().addItem(item);
					}else
						p.sendMessage("ßcEs gibt keine neuen Transaktionen!");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("ß6Firmenkonto")) {
					if(pi.getJob() > 0) {
						FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
						if(fi.hasrank(p, Rights.MAIN_CHARGEMONEY)) {
							List<String> transactions = fi.getTransactions();
							if(transactions.size() != 0) {
								List<String> pages = new ArrayList<String>();
								LocalDateTime myDateObj = LocalDateTime.now();
								DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy");
							    String formattedDate = myDateObj.format(myFormatObj);
								pages.add("ß6Kontoauszug vom Firmenkonto " + fi.getFirmname() + "\n bis einschlieﬂlich " + formattedDate);
								int page = 1;
								pages.add("");
								List<String> toRemove = new ArrayList<String>();
								for(String current : transactions) {
									int length = pages.get(page).length() + current.length();
									if(page < 50) {
										if(length > 256) {
											page++;
											pages.add("ß6" + current);
										}else {
											String text = pages.get(page);
											text += "\n " + current;
											pages.set(page, text);
										}
										toRemove.add(current);
									}
						
								}
								for(String current : toRemove) {
									transactions.remove(current);
								}
								fi.setTransctions(transactions);
								ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
								BookMeta meta = (BookMeta) item.getItemMeta();
								meta.setPages(pages);
								meta.setAuthor(p.getName());
								meta.setTitle("ß6Kontoauszug");
								item.setItemMeta(meta);
								p.getInventory().addItem(item);
							}else
								p.sendMessage("ßcEs gibt keine neuen Transaktionen!");
						}else
							p.sendMessage(Main.KEINE_RECHTE);
					}else
						p.sendMessage("ßcDu arbeitest in keiner Firma mehr!");
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
}
