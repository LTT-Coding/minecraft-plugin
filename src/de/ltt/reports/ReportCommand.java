package de.ltt.reports;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemSkulls;

public class ReportCommand implements CommandExecutor, Listener{

	public static ArrayList<Player> fragen = new ArrayList<Player>();
	public static ArrayList<Player> melden = new ArrayList<Player>();
	public static ArrayList<Player> fehler = new ArrayList<Player>();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!ReportCommand.fragen.contains(player) && !ReportCommand.melden.contains(player) && !ReportCommand.fehler.contains(player) && !Main.frChat.containsKey(player) && !Main.frChat2.containsKey(player)) {
				Inventory reportinv =  Bukkit.createInventory(null, 9, "§aReport-Menue");
				
				ItemStack frage = ItemSkulls.getSkull("http://textures.minecraft.net/texture/bc8ea1f51f253ff5142ca11ae45193a4ad8c3ab5e9c6eec8ba7a4fcb7bac40");
				SkullMeta fragemeta = (SkullMeta) frage.getItemMeta();
				fragemeta.setDisplayName("§6Eine Frage stellen");
				frage.setItemMeta(fragemeta);
				
				ItemStack report = new ItemStack(Material.PLAYER_HEAD, 1);
				SkullMeta reportmeta = (SkullMeta) report.getItemMeta();
				reportmeta.setDisplayName("§6Einen Spieler reporten");
				report.setItemMeta(reportmeta);
				
				ItemStack fehler = new ItemStack(Material.BARRIER);
				ItemMeta fehlermeta = fehler.getItemMeta();
				fehlermeta.setDisplayName("§6Einen Fehler melden");
				fehlermeta.setLocalizedName("§aJedes melden eines nicht bekannten fehlers wird belohnt");
				fehler.setItemMeta(fehlermeta);
				
				reportinv.setItem(1, frage);
				reportinv.setItem(4, report);
				reportinv.setItem(7, fehler);
				((Player) sender).openInventory(reportinv);
				sender.sendMessage("§aDas Reportinventar wurde geöffnet!");
			}else 
				player.sendMessage("§cDu hast bereits einen Report abgesendet!");
		}else
			sender.sendMessage("Dazu musst du ein Spieler sein!");
		return false;
	}
	
	@EventHandler
	public void onInvKlick(InventoryClickEvent e) {
		if(e.getView().getTitle().equals("§aReport-Menue")) {
			Player player = (Player) e.getWhoClicked();
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Eine Frage stellen")) {
				if(!Main.Supporter.contains(player.getUniqueId().toString())) {
					for(String current : Main.Supporter) {
						Player target = Bukkit.getPlayer(UUID.fromString(current));
						if(target != null) {
							target.sendMessage("§6" + player.getName() + "§4 hat eine Frage!");
							target.sendMessage("§eNehme den Report mit §6/fraccept §ean!");
						}
					}
					fragen.add(player);
					player.sendMessage("§aReport wurde abgesendet");
				}else
					player.sendMessage("§cDu bist Supporter und kannst keine Fragen stellen!");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Einen Spieler reporten")) {
				if(!Main.Moderatoren.contains(player.getUniqueId().toString())) {
					for(String current : Main.Moderatoren) {
						Player target =  Bukkit.getPlayer(UUID.fromString(current));
						if(target != null) {
							target.sendMessage("§6" + player.getName() + "§4 möchte einen Spieler melden!");
							target.sendMessage("§eNehme den Report mit §6/mlaccept §ean!");
						}
					}
					melden.add(player);
					player.sendMessage("§aReport wurde abgesendet");
				}else
					player.sendMessage("§cDu bist Moderator, kannst daher keine Fehler oder Spieler melden");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Einen Fehler melden")) {
				if(!Main.Moderatoren.contains(player.getUniqueId().toString())) {
					for(String current : Main.Moderatoren) {
						Player target = Bukkit.getPlayer(UUID.fromString(current));
						if(target != null) {
							target.sendMessage("§6" + player.getName() + "§4 möchte einen Fehler melden!");
							target.sendMessage("§eNehme den Report mit §6/flaccept §ean!");
						}
					}
					fehler.add(player);
					player.sendMessage("§aReport wurde abgesendet");
				}else
					player.sendMessage("§cDu bist Moderator, kannst daher keine Fehler oder Spieler melden");
			}
			e.setCancelled(true);
			player.closeInventory();
		}
	}
	

}
