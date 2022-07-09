package de.ltt.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.accessibility.AccessibleAction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import de.ltt.server.listener.Chat;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class Wardrobe implements CommandExecutor, Listener{

	public static List<Player> addWD = new ArrayList<Player>();
	public static List<Player> removeWD = new ArrayList<Player>();
	public static List<Player> addWDKleidung = new ArrayList<Player>();
	
	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					if(args[0].equals("add")) {
						addWD.add(p);
						p.sendMessage("§eKlick auf den Block den du Registieren willst");
					} else if(args[0].equals("remove")) {
						removeWD.add(p);
						p.sendMessage("§eKlick auf den Block den du removen willst");
					}else
						p.sendMessage("§cBitte benutze /wardrobe add/remove");
				}else
					p.sendMessage("§cBitte benutze /wardrobe add/remove");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	@EventHandler
	public void onWardrobe(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			try {
				if(addWD.contains(e.getPlayer())) {
					if(!Main.WardrobeLoc.contains(e.getClickedBlock().getLocation())) {
						SQLData.addWardrobe(e.getClickedBlock().getLocation());
						e.getPlayer().sendMessage("§eDu hast die Wardrobe eingerichtet");
					} else {
						e.getPlayer().sendMessage("§cDer Block ist eine Wardrobe");
					}
					addWD.remove(e.getPlayer());
				} else if(removeWD.contains(e.getPlayer())) {
					if(Main.WardrobeLoc.contains(e.getClickedBlock().getLocation())) {
						SQLData.removeWardrobe(e.getClickedBlock().getLocation());
						e.getPlayer().sendMessage("§eDu hast die Wardrobe entfernt");
					} else {
						e.getPlayer().sendMessage("§cDer Block ist keine Wardrobe");
					}
					removeWD.remove(e.getPlayer());
				} else if(Main.WardrobeLoc.contains(e.getClickedBlock().getLocation())) {
					Player p = e.getPlayer();
					Inventory inv = Bukkit.createInventory(null, 3*9, "§eKleiderschrank");
					
					Main.fillGlass(inv);
					inv.setItem(26, new ItemBuilder(Material.GREEN_CONCRETE).setName("§aKleidungsstück hineinlegen").build());
					inv.setItem(18, new ItemBuilder(Material.RED_CONCRETE).setName("§aKleidungsstück entfernt").build());
					int slot = 0;
					PlayerInfo pi = new PlayerInfo(p);
					if(pi.getSkinName() != null) {
						for (String name : pi.getSkinName()) {
							inv.setItem(slot, new ItemBuilder(Material.PLAYER_HEAD).setName(name).build());
							slot++;
						}
					}
					p.openInventory(inv);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onWardrobeInv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p);
		try {
			if(e.getView().getTitle().equals("§eKleiderschrank") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§aKleidungsstück hineinlegen")) {
					addWDKleidung.add(p);
					ChatInfo.addChat(p, ChatType.ISCHAT);
					p.sendMessage("§eSchreib ein Namen für das Kleidungsstück");
					p.closeInventory();
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§aKleidungsstück entfernt")) {
					p.sendMessage("§eKlick auf das Kleidungsstück welches du entfernen möchtest");
					Inventory inv = Bukkit.createInventory(null, 2*9, "§eKleidungsstück entfernen");
					if(pi.getSkinName() != null) {
						for (String name : pi.getSkinName()) {
							inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setName(name).build());
						}
					}
					p.openInventory(inv);
				} else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					String texture = Main.getPlugin().getSkinConfig().getString("Spieler." + PlayerInfo.getUUID(p) + ".skin." + e.getCurrentItem().getItemMeta().getDisplayName() + ".texture");
					String signature = Main.getPlugin().getSkinConfig().getString("Spieler." + PlayerInfo.getUUID(p) + ".skin." + e.getCurrentItem().getItemMeta().getDisplayName() + ".signature");
					pi.saveInv();
					p.getInventory().clear();
					pi.changeSkin(texture, signature);
					p.sendMessage("§aAussehen Erfolgreich geändert");
					pi.loadInv();
				}
				e.setCancelled(true);
			} else if(e.getView().getTitle().equals("§eKleidungsstück entfernen") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
				if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					pi.removeSkin(e.getCurrentItem().getItemMeta().getDisplayName());
					p.sendMessage("§eDu hast ein Kleidungsstück entfernt");
				}
				e.setCancelled(true);
				p.closeInventory();
			}
		} catch (Exception e2) {
		}
		
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		if(addWDKleidung.contains(e.getPlayer())) {
			PlayerInfo pi = new PlayerInfo(e.getPlayer());
			if(!pi.getSkinName().contains(e.getMessage())) {
				pi.saveSkin(e.getMessage());
				e.getPlayer().sendMessage("§eDu hast ein Kleidungsstück hineingelegt");
			} else {
				e.getPlayer().sendMessage("§eEs gibt schon so ein Kleidungsstück mit dem Namen");
			}
			ChatInfo.removeChat(e.getPlayer());
			addWDKleidung.remove(e.getPlayer());
		}
	}

}
