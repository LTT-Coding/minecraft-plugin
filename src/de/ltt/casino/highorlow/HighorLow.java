package de.ltt.casino.highorlow;

import java.util.HashMap;
import java.util.Random;

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
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.ItemSkulls;


public class HighorLow implements Listener{
	
	
	HashMap<Player, Integer> SpielGeld = new HashMap<Player, Integer>();
	int HoLZahl;
	boolean High;
	boolean Low;
	int MLow = 2; //Multiplikator von Low
	int MHigh = 4; //Multiplikator von High
	
	@EventHandler
	public void HighorLow_Schild(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (Main.Admin.contains(p.getUniqueId().toString())) {
			if (e.getLine(0) != null && e.getLine(0) != "") {
				if (e.getLine(0).equalsIgnoreCase("/highorlow")) {
					e.setLine(0, "§a[High or Low]");
					e.setLine(1, "§7[High]");
					e.setLine(2, "§7[or]");
					e.setLine(3, "§7[Low]");

				}

			}
		}
	}
	
	@EventHandler
	public void HighorLow_Inter(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				Player p = e.getPlayer();
				if (sign.getLine(0) != null && sign.getLine(0) != "") {
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[High or Low]")) {
					
					Inventory Inv = Bukkit.createInventory(null, 27, "§6High or Low");
					
					  ItemStack High = ItemSkulls.getSkull("http://textures.minecraft.net/texture/14a5667ef7285c9225fc267d45117eab5478c786bd5af0a199c29a2c14c1f");
				      ItemMeta HighMeta = High.getItemMeta();
				      HighMeta.setDisplayName("§bHigh");
				      High.setItemMeta(HighMeta); 
				      High.setAmount(1);
				      
				      ItemStack Low = ItemSkulls.getSkull("http://textures.minecraft.net/texture/d1b62db5c0a3fa1ef441bf7044f511be58bedf9b6731853e50ce90cd44fb69");
				      ItemMeta LowMeta = Low.getItemMeta();
				      LowMeta.setDisplayName("§bLow");
				      Low.setItemMeta(LowMeta); 
				      Low.setAmount(1);
				      
				      ItemStack H = ItemSkulls.getSkull("http://textures.minecraft.net/texture/6c2899f956d9c87da118bb813aa8226f5f339591eee5fbf9bafcee59cd9e1df");
				      ItemMeta HMeta = H.getItemMeta();
				      HMeta.setDisplayName("§bH");
				      H.setItemMeta(HMeta); 
				      H.setAmount(1);
				      
				      ItemStack I = ItemSkulls.getSkull("http://textures.minecraft.net/texture/feb79a7fc95428ca8c88d52028399cd1f3ad973769c3b91a836275519f4fb29");
				      ItemMeta IMeta = I.getItemMeta();
				      IMeta.setDisplayName("§bH");
				      I.setItemMeta(IMeta); 
				      I.setAmount(1);
				      
				      ItemStack G = ItemSkulls.getSkull("http://textures.minecraft.net/texture/1b575b5577ccb32e42d54304a1ef5f23ad6bad5a3456340a4912a62b3797bb5");
				      ItemMeta GMeta = G.getItemMeta();
				      GMeta.setDisplayName("§bG");
				      G.setItemMeta(GMeta); 
				      G.setAmount(1);
				      
				      ItemStack O = ItemSkulls.getSkull("http://textures.minecraft.net/texture/52faf892cb57fa92c53baf57b8b6c2984c4f4c70aefeb6959ff2fadc6623f72");
				      ItemMeta OMeta = O.getItemMeta();
				      OMeta.setDisplayName("§bO");
				      O.setItemMeta(OMeta); 
				      O.setAmount(1);
				      
				      ItemStack O2 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/6ede6082442383e5e1c28257b6db2613dc37f9749f48ea0b4f94af98ae0ce");
				      ItemMeta O2Meta = O2.getItemMeta();
				      O2Meta.setDisplayName("§bO");
				      O2.setItemMeta(O2Meta); 
				      O2.setAmount(1);
				      
				      ItemStack R = ItemSkulls.getSkull("http://textures.minecraft.net/texture/c93ed807dbf147c5ef9b8ec46d3fa6e2d7b2dbd3431a23417c1354bb863c4");
				      ItemMeta RMeta = R.getItemMeta();
				      RMeta.setDisplayName("§bR");
				      R.setItemMeta(RMeta); 
				      R.setAmount(1);
				      
				      ItemStack L = ItemSkulls.getSkull("http://textures.minecraft.net/texture/597463d7181e83a143a6ced1a1f77f66d1f28e6f272fe8cd95e7fb89ea0dc69");
				      ItemMeta LMeta = L.getItemMeta();
				      LMeta.setDisplayName("§bL");
				      L.setItemMeta(LMeta); 
				      L.setAmount(1);
				      
				      ItemStack W = ItemSkulls.getSkull("http://textures.minecraft.net/texture/60dbb4ade8c99b1cd18c5c6bc24fbd36b65a8b66d283e71fd4ff9194d6ae7a4");
				      ItemMeta WMeta = W.getItemMeta();
				      WMeta.setDisplayName("§bW");
				      W.setItemMeta(WMeta); 
				      W.setAmount(1);
				      
				      
				      
				      
				      
				      ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE , 1);
				      ItemMeta GlasMeta = Glas.getItemMeta();
				      GlasMeta.setDisplayName(" ");
				      Glas.setItemMeta(GlasMeta); 
				      Glas.setAmount(1);
				      
			      
				      for(int i = 0; i<27;i++) {
					    	 if(i !=11|i!=15) {
					    		 Inv.setItem(i, Glas);
					    	 }
					     }
				      Inv.setItem(11, High);
				      Inv.setItem(15, Low);
				      Inv.setItem(0, H);
				      Inv.setItem(1, I);
				      Inv.setItem(2, G);
				      Inv.setItem(3, H);
				      Inv.setItem(4, O2);
				      Inv.setItem(5, R);
				      Inv.setItem(6, L);
				      Inv.setItem(7, O);
				      Inv.setItem(8, W);
				      	      
						p.openInventory(Inv);
					}

				}

			}
		}

	}
	
	@EventHandler
	public void KlickonItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		try {
		if (e.getView().getTitle().equals("§6High or Low")) {

			
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bHigh")) {
					p.sendMessage("§eDu hast High ausgewählt.");
					High = true;
					ChatInfo.addChat(p, ChatType.HIGHORLOW, 600);
					p.sendMessage("§eGib ein betrag an als Spielgeld.");
					p.closeInventory();
	
					e.setCancelled(true);

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bLow")) {
					p.sendMessage("§eDu hast Low ausgewählt.");
					Low = true;
					ChatInfo.addChat(p, ChatType.HIGHORLOW, 600);
					p.sendMessage("§eGib ein betrag an als Spielgeld.");
					p.closeInventory();

					e.setCancelled(true);

				} else e.setCancelled(true);
					
				
			
			e.setCancelled(true);
		}
		} catch (Exception exception) {
		}
		
	}

	
	@EventHandler
	public void ChatInt(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if(ChatInfo.getChat(p).equals(ChatType.HIGHORLOW)) {
			ChatInfo.removeChat(p);
			try {
				int moneyInHand2 = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".moneyInHand");
				int spielgeld = Integer.parseInt(e.getMessage());
				if(spielgeld <= moneyInHand2) {
					
					Random r = new Random();
					int Zufall = r.nextInt(9);
					switch(Zufall) {
					case 0:
						HoLZahl = 1;
						
						break;
					case 1:
						HoLZahl = 2;
						
						break;
					case 2:
						HoLZahl = 3;
						
						break;
					case 3:
						HoLZahl = 4;
						
						break;
					case 4:
						HoLZahl = 5;
						
						break;	
					case 5:
						HoLZahl = 7;
						
						break;
					case 6:
						HoLZahl = 8;
						
						break;
					case 7:
						HoLZahl = 9;
						
						break;
					case 8:
						HoLZahl = 10;
						
						break;

					}
				    if(High == true) {
					if(Zufall >= 7) {
						int GesamtHigh = spielgeld*MHigh;
						int moneyInHand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".moneyInHand");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId().toString() + ".moneyInHand", moneyInHand + GesamtHigh);
						p.sendMessage("§aGlückwusch du hast die Zahl §6"+ Zufall + "§a und §e" + GesamtHigh + "€ §agewonnen!");
						Main.getPlugin().saveConfig();
						High = false;
						
					} else {
						int moneyInHand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".moneyInHand");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId().toString() + ".moneyInHand", moneyInHand - spielgeld);
						p.sendMessage("§aSchade du hast die Zahl §6"+ Zufall + "§a und §e" + spielgeld + "€ §averloren!");
						Main.getPlugin().saveConfig();
						High = false;
					}

				} else if(Low == true) {
					if(Zufall <= 6) {
						int GesamtLow = spielgeld*MLow;
						int moneyInHand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".moneyInHand");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId().toString() + ".moneyInHand", moneyInHand + GesamtLow);
						p.sendMessage("§aGlückwusch du hast die Zahl §6"+ Zufall + "§a und §e" + GesamtLow + "€ §agewonnen!");
						Main.getPlugin().saveConfig();
						Low = false;
					}else {
						int moneyInHand = Main.getPlugin().getConfig().getInt("Spieler." + p.getUniqueId().toString() + ".moneyInHand");
						Main.getPlugin().getConfig().set("Spieler." + p.getUniqueId().toString() + ".moneyInHand", moneyInHand - spielgeld);
						p.sendMessage("§aSchade du hast die Zahl §6"+ Zufall + "§a und §e" + spielgeld + "€ §averloren!");
						Main.getPlugin().saveConfig();
						Low = false;
					}
					
				} 
					
					
				}else {
					p.sendMessage("§cSo viel Geld hast du nicht auf der Hand!");
					p.sendMessage("§cVorgang abgebrochen!");
				}
				
				  
            }catch (Exception e2) {
                p.sendMessage("§cDeine Eingabe ist keine Zahl!");
				p.sendMessage("§cVorgang abgebrochen!");
            }
			e.setCancelled(true);
		}
		
	}
	
	

}
