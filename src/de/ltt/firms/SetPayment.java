package de.ltt.firms;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;

public class SetPayment implements Listener{

	public static HashMap<Integer, Player> ranksetter = new HashMap<Integer, Player>();
	public static HashMap<Player, Integer> ranksettings = new HashMap<Player, Integer>();
	public static HashMap<Player, Double> rankPay = new HashMap<Player, Double>();
	
	@EventHandler
	public void onInvKlick(InventoryClickEvent e) {
		try {
			if(e.getView().getTitle().equals("§eKlicke auf einen Rang")) {
				 if(!ranksetter.containsKey(new PlayerInfo((OfflinePlayer) e.getWhoClicked()).getJob())) {
					 ranksetter.put(new PlayerInfo((OfflinePlayer) e.getWhoClicked()).getJob(), (Player)e.getWhoClicked());
					 Player p = (Player) e.getWhoClicked();
					 if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
						 
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§71")) {
						 ranksettings.put((Player) e.getWhoClicked(), 1);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§72")) {
						 ranksettings.put((Player) e.getWhoClicked(), 2);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§73")) {
						 ranksettings.put((Player) e.getWhoClicked(), 3);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§74")) {
						 ranksettings.put((Player) e.getWhoClicked(), 4);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§75")) {
						 ranksettings.put((Player) e.getWhoClicked(), 5);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§76")) {
						 ranksettings.put((Player) e.getWhoClicked(), 6);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§70")) {
						 ranksettings.put((Player) e.getWhoClicked(), 0);
					 }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Zurücksetzen")) {
						 p.closeInventory();
						 FirmInfo fi = new FirmInfo().loadfirm(new PlayerInfo(p).getJob());
						 fi.resetPay();
						 p.sendMessage("§aBezahlung zurückgesetzt!");
					 }
					 
					 if(ranksettings.containsKey(p)) {
						 Inventory Inv = Bukkit.createInventory(null, 5*9, "§eWähle die Bezahlung für den Rang");
						 
						    ItemStack skull1 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc");
							ItemMeta skull1Meta = skull1.getItemMeta();
							skull1Meta.setDisplayName("§71");
							skull1.setItemMeta(skull1Meta);
							skull1.setAmount(1);

							ItemStack skull2 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a");
							ItemMeta skull2Meta = skull2.getItemMeta();
							skull2Meta.setDisplayName("§72");
							skull2.setItemMeta(skull2Meta);
							skull2.setAmount(1);

							ItemStack skull3 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2");
							ItemMeta skull3Meta = skull3.getItemMeta();
							skull3Meta.setDisplayName("§73");
							skull3.setItemMeta(skull3Meta);
							skull3.setAmount(1);

							ItemStack skull4 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058");
							ItemMeta skull4Meta = skull4.getItemMeta();
							skull4Meta.setDisplayName("§74");
							skull4.setItemMeta(skull4Meta);
							skull4.setAmount(1);

							ItemStack skull5 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763");
							ItemMeta skull5Meta = skull5.getItemMeta();
							skull5Meta.setDisplayName("§75");
							skull5.setItemMeta(skull5Meta);
							skull5.setAmount(1);

							ItemStack skull6 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1");
							ItemMeta skull6Meta = skull6.getItemMeta();
							skull6Meta.setDisplayName("§76");
							skull6.setItemMeta(skull6Meta);
							skull6.setAmount(1);

							ItemStack skull7 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb");
							ItemMeta skull7Meta = skull7.getItemMeta();
							skull7Meta.setDisplayName("§77");
							skull7.setItemMeta(skull7Meta);
							skull7.setAmount(1);  

							ItemStack skull8 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d");
							ItemMeta skull8Meta = skull8.getItemMeta();
							skull8Meta.setDisplayName("§78");
							skull8.setItemMeta(skull8Meta);
							skull8.setAmount(1);

							ItemStack skull9 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba");
							ItemMeta skull9Meta = skull9.getItemMeta();
							skull9Meta.setDisplayName("§79");
							skull9.setItemMeta(skull9Meta);
							skull9.setAmount(1);

							ItemStack skull0 = ItemSkulls.getSkull("http://textures.minecraft.net/texture/3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5");
							ItemMeta skull0Meta = skull0.getItemMeta();
							skull0Meta.setDisplayName("§70");
							skull0.setItemMeta(skull0Meta);
							skull0.setAmount(1);

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

							ItemStack Glas = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
							ItemMeta GlasMeta = Glas.getItemMeta();
							GlasMeta.setDisplayName(" ");
							Glas.setItemMeta(GlasMeta);
							Glas.setAmount(1);

							ItemStack Löschen = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
							ItemMeta LöschenMeta = Löschen.getItemMeta();
							LöschenMeta.setDisplayName("§4Löschen");
							Löschen.setItemMeta(LöschenMeta);
							Löschen.setAmount(1);

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
							
							e.getWhoClicked().openInventory(Inv);
							rankPay.put(p, 0D);
						 
					 }
					 
				 }else
					 e.getWhoClicked().sendMessage("§cEs ist bereits jemand aus deiner Firma in diesem Menü");
				 e.setCancelled(true);
			 }else if(e.getView().getTitle().equals("§eWähle die Bezahlung für den Rang")) {
				 String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
				 double payment = rankPay.get(e.getWhoClicked());
				 if(String.valueOf(payment).length() < 9) {
					 if(itemName.equals("§71")) {
						 payment = payment*10+1;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§72")) {
						 payment = payment*10+2;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§73")) {
						 payment = payment*10+3;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§74")) {
						 payment = payment*10+4;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§75")) {
						 payment = payment*10+5;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§76")) {
						 payment = payment*10+6;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§77")) {
						 payment = payment*10+7;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§78")) {
						 payment = payment*10+8;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§79")) {
						 payment = payment*10+9;
						 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
					 }else if(itemName.equals("§70")) {
						 if(payment != 0 ) {
							 payment = payment*10;
							 e.getInventory().setItem(String.valueOf(payment).length() - 1, e.getCurrentItem());
						 }
					 }
					 
				 }
				 if(itemName.equals("§4Löschen")) {
					 for(int i = 0; i < 9; i++) {
						 e.getInventory().setItem(i, null);
					 }
					 payment = 0;
				 }else if(itemName.equals("§6Abbrechen")) {
					 e.getWhoClicked().closeInventory();
				 }else if(itemName.equals("§2Akzeptieren")) {
					  FirmInfo fi = new FirmInfo().loadfirm(new PlayerInfo((OfflinePlayer) e.getWhoClicked()).getJob());
					  List<Double> pay = fi.getPay();
					  pay.set(ranksettings.get(e.getWhoClicked()), rankPay.get(e.getWhoClicked()));
					  fi.setPay(pay);
					  e.getWhoClicked().closeInventory();
					  e.getWhoClicked().sendMessage("§eDie Bezahlung von Rang §6" + ranksettings.get(e.getWhoClicked()) + " §eauf§6 " + payment + "€§e gesetzt");
				 }
				 rankPay.put((Player) e.getWhoClicked(), payment);
				 e.setCancelled(true);
			 }
		} catch (Exception e2) {
			
		}
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		if(ranksetter.containsKey(new PlayerInfo((OfflinePlayer) e.getPlayer()).getJob())) {
			if(ranksetter.get(new PlayerInfo((OfflinePlayer) e.getPlayer()).getJob()).equals(e.getPlayer())) {
				if(e.getView().getTitle().equals("§eWähle die Bezahlung für den Rang")) {
					ranksetter.remove(new PlayerInfo((OfflinePlayer) e.getPlayer()).getJob());
					
					e.getPlayer().sendMessage("§7Du hast das Menü verlassen!");
				}
			}
		}

	}

}
