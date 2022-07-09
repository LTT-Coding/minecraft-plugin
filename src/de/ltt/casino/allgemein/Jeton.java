package de.ltt.casino.allgemein;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemSkulls;



public class Jeton implements Listener, CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			new VJeton(p.getLocation());
			p.sendMessage("§aDer Jeton_Händler wurde erfolgreich erstellt.");

			
		} else
			p.sendMessage("§cDazu must du ein §4Admin§c sein!");
			
		return false;
	}
	
	@EventHandler
	public void KlickOnHändler(PlayerInteractEntityEvent e) {
		if(!(e.getRightClicked() instanceof Villager)) return;
		Villager shop = (Villager) e.getRightClicked();
		
		if(shop.getCustomName().equals(VJeton.VILLAGER_Jeton)) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			int jeton = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".jeton");
			int moneyinhand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".moneyInHand");
			
			
			Inventory Inv = Bukkit.createInventory(null, 5*9, "§6Jeton Händler");
			
			  ItemStack Kaufen1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/acd70ce4818581ca47adf6b81679fd1646fd687c7127fdaae94fed640155e");
		      ItemMeta Kaufen1Meta = Kaufen1.getItemMeta();
		      Kaufen1Meta.setDisplayName("§a1 Jeton");
		      Kaufen1.setItemMeta(Kaufen1Meta); 
		      Kaufen1.setAmount(1);
		      
		      ItemStack Kaufen100 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/acd70ce4818581ca47adf6b81679fd1646fd687c7127fdaae94fed640155e");
		      ItemMeta Kaufen100Meta = Kaufen100.getItemMeta();
		      Kaufen100Meta.setDisplayName("§a100 Jetons");
		      Kaufen100.setItemMeta(Kaufen100Meta); 
		      Kaufen100.setAmount(1);  
		      
		      
		      ItemStack KaufenU = ItemSkulls.getSkull("http://textures.minecraft.net/texture/fcc7f6441bd71fc974e9977bcb22efbc4b61277c49efb2423ab9154895be");
		      ItemMeta KaufenUMeta = KaufenU.getItemMeta();
		      KaufenUMeta.setDisplayName("§a" + moneyinhand + " Jetons");
		      KaufenU.setItemMeta(KaufenUMeta); 
		      KaufenU.setAmount(1);
		      
		      ItemStack Verkaufen1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/bf75d1b785d18d47b3ea8f0a7e0fd4a1fae9e7d323cf3b138c8c78cfe24ee59");
		      ItemMeta Verkaufen1Meta = Verkaufen1.getItemMeta();
		      Verkaufen1Meta.setDisplayName("§a+1€");
		      Verkaufen1.setItemMeta(Verkaufen1Meta); 
		      Verkaufen1.setAmount(1);
		      
		      ItemStack Verkaufen100 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/bf75d1b785d18d47b3ea8f0a7e0fd4a1fae9e7d323cf3b138c8c78cfe24ee59");
		      ItemMeta Verkaufen100Meta = Verkaufen100.getItemMeta();
		      Verkaufen100Meta.setDisplayName("§a+100€");
		      Verkaufen100.setItemMeta(Verkaufen100Meta); 
		      Verkaufen100.setAmount(1);
		      
		      ItemStack VerkaufenU = ItemSkulls.getSkull("http://textures.minecraft.net/texture/c8ea7933581ee9fb400f39044d3015ca0d43bb6e72fc9267c7fd1361f68ff12b");
		      ItemMeta VerkaufenUMeta = VerkaufenU.getItemMeta();
		      VerkaufenUMeta.setDisplayName("§a+" + jeton + "€");
		      VerkaufenU.setItemMeta(VerkaufenUMeta); 
		      VerkaufenU.setAmount(1);
		      
		      ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE , 1);
		      ItemMeta GlasMeta = Glas.getItemMeta();
		      GlasMeta.setDisplayName(" ");
		      Glas.setItemMeta(GlasMeta); 
		      Glas.setAmount(1);
		      
			for (int i = 0; i < 5*9; i++) {
				if (i != 11 | i != 15) {
					Inv.setItem(i, Glas);
				}
			}
			Inv.setItem(10, Kaufen1);
			Inv.setItem(13, Kaufen100);
			Inv.setItem(16, KaufenU);
			Inv.setItem(28, Verkaufen1);
			Inv.setItem(31, Verkaufen100);
			Inv.setItem(34, VerkaufenU);
			


			p.openInventory(Inv);
			
		}
	}
	
	@EventHandler
	public void KlickonItem(InventoryClickEvent e) {
		try {
		if (e.getView().getTitle().equals("§6Jeton Händler")) {
			Player p = (Player) e.getWhoClicked();
			
			int jeton = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".jeton");
			int moneyinhand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId() + ".moneyInHand");

			
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a1 Jeton")) {
					
					if(moneyinhand >= 1) {
					Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand-1 );
					Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton+1 );
					Main.getPlugin().saveConfig();
					p.sendMessage("§a+1 Jeton");
					p.closeInventory();

					} else 
						p.sendMessage("§cDu hast leider nicht genug Geld auf der Hand!");
					
					e.setCancelled(true);
				

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a100 Jetons")) {
					
					if(moneyinhand >= 100) {
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand-100 );
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton+100 );
						Main.getPlugin().saveConfig();
						p.sendMessage("§a+100 Jeton");
						p.closeInventory();
						} else 
							p.sendMessage("§cDu hast leider nicht genug Geld auf der Hand!");

					e.setCancelled(true);
			

				}else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a" + moneyinhand + " Jetons")) {
					
					if(moneyinhand >= moneyinhand) {
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton+moneyinhand );
						p.sendMessage("§a+" + moneyinhand +" Jeton");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand-moneyinhand );
						Main.getPlugin().saveConfig();
						p.closeInventory();
						} else 
							p.sendMessage("§cDu hast leider nicht genug Geld auf der Hand!");
					

					
					e.setCancelled(true);
	

				}else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+1€")) {
					
					if(jeton >= 1) {
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand+1 );
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton-1 );
						Main.getPlugin().saveConfig();
						p.sendMessage("§a+1€");
						p.closeInventory();
						} else 
							p.sendMessage("§cDu hast leider nicht genug Jetons auf der Hand!");
					

					
					e.setCancelled(true);
				

				}else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+100€")) {
					
					if(jeton >= 100) {
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand+100 );
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton-100 );
						Main.getPlugin().saveConfig();
						p.sendMessage("§a+100€");
						p.closeInventory();
						} else 
							p.sendMessage("§cDu hast leider nicht genug Jetons auf der Hand!");

					
					e.setCancelled(true);
					

				}else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+" + jeton + "€")) {
					
					if(jeton >= jeton) {
						p.sendMessage("§a+" + jeton +" Geld");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".moneyInHand", moneyinhand+jeton );
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId() + ".jeton", jeton-jeton );
						Main.getPlugin().saveConfig();
						p.closeInventory();
						} else 
							p.sendMessage("§cDu hast leider nicht genug Geld auf der Hand!");

				
					e.setCancelled(true);
					

				} else e.setCancelled(true);
					
				
			
		}
	} catch (Exception exception) {
	}

	}

	

}
