package de.ltt.staat.police.equip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.ltt.FakePlayer.RightClickNPC;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.staat.police.grundSystem.PoliceInfo;
import de.ltt.staat.police.grundSystem.PoliceRights;
import net.minecraft.server.v1_15_R1.EntityPlayer;


public class PoliceEquip implements Listener{
	
	public static List<ItemStack> items = new ArrayList<ItemStack>();
	public static HashMap<Player, Location> npcLoc = new HashMap<Player, Location>();
	
	@EventHandler
	public void onClickEquip(RightClickNPC e) {
		if(e.getNPC().getName().equals("Dieter")) {
			Player p = e.getPlayer();
			PlayerInfo pip = new PlayerInfo(p); 
			EntityPlayer npc = e.getNPC();
			PoliceInfo pi = new PoliceInfo();
			Location NPCloc = new Location(p.getWorld(), npc.locX(), npc.locY(), npc.locZ());
			if(pi.hasRight(p, PoliceRights.EQUIP)) {
				Random r = new Random();
				int Zahl = r.nextInt(Main.PoliceWas.size());
				Main.BroadcastLoc(NPCloc, 4, "Dieter: Guten Tag " + pip.getLastName() + ", " + Main.PoliceWas.get(Zahl));
				Inventory inv = Bukkit.createInventory(null, 2*9, "§1Polizei Equip");
				
				ItemStack paper1 = new ItemBuilder(Material.PAPER).setName("§eTaser").setLore(pi.getEquipCostString("§eTaser") + "€").build();
				ItemStack paper2 = new ItemBuilder(Material.PAPER).setName("§eHandschelle").setLore(pi.getEquipCostString("§eHandschelle") + "€").build();
				ItemStack paper3 = new ItemBuilder(Material.PAPER).setName("§ePfefferspray").setLore(pi.getEquipCostString("§ePfefferspray") + "€").build();
				ItemStack paper4 = new ItemBuilder(Material.PAPER).setName("§eSchlagsstock").setLore(pi.getEquipCostString("§eSchlagsstöcke") + "€").build();
				ItemStack paper5 = new ItemBuilder(Material.PAPER).setName("§eBlitzer").setLore(pi.getEquipCostString("§eBlitzer") + "€").build();
				ItemStack paper6 = new ItemBuilder(Material.PAPER).setName("§ePolizeikarte").setLore(pi.getEquipCostString("§ePolizeikarte") + "€").build();
				ItemStack paper7 = new ItemBuilder(Material.PAPER).setName("§eHelm").setLore(pi.getEquipCostString("§eHelme") + "€").build();
				ItemStack paper8 = new ItemBuilder(Material.PAPER).setName("§eSchild").setLore(pi.getEquipCostString("§eSchild") + "€").build();
				ItemStack glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
				
				inv.setItem(1, paper1);
				inv.setItem(3, paper2);
				inv.setItem(5, paper3);
				inv.setItem(7, paper4);
				inv.setItem(9, paper5);
				inv.setItem(11, paper6);
				inv.setItem(13, paper7);
				inv.setItem(15, paper8);
				for (int i = 0; i < 2*9; i++) {
					if (i != 1 && i != 3 && i != 5 && i != 7 && i != 9 && i != 11 && i != 13 && i != 15) {
						inv.setItem(i, glas);
					}
				}
				
				if(items.isEmpty()) {
					for (int i = 0; i < inv.getSize(); i++) {
							items.add(inv.getItem(i));
					}
				}
				
				p.openInventory(inv);
				npcLoc.put(p, NPCloc);
				
			} else {
				Random r = new Random();
				int Zahl = r.nextInt(Main.PoliceGeh.size());
				Main.BroadcastLoc(NPCloc, 4, "Dieter: " + Main.PoliceGeh.get(Zahl));
			}
				
		}
	}
	
	@EventHandler
	public void closePoliceEquip(InventoryCloseEvent e) {
	if(e.getView().getTitle().equals("§1Polizei Equip")) {
		Random r = new Random();
		int Zahl = r.nextInt(Main.PoliceDanke.size());
		Main.BroadcastLoc(npcLoc.get(e.getPlayer()), 4, "Dieter: " + Main.PoliceDanke.get(Zahl));
		npcLoc.remove(e.getPlayer());
	}
	}
	
	@EventHandler
	public void clickPoliceEquip(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerInfo pi = new PlayerInfo(p); 
		PoliceInfo pip = new PoliceInfo();
		try {
			 if(e.getView().getTitle().equals("§1Polizei Equip") && e.getInventory() == e.getView().getTopInventory()) {
				 String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
				 if(e.getCurrentItem().getItemMeta().getDisplayName().equals(itemName) && !(e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE)) {
					 ItemStack buyItem = new ItemBuilder(e.getCurrentItem().getType()).setName(itemName).build();
					 p.getInventory().addItem(buyItem);
					 pi.setMoneyInHand(pi.getMoneyInHand() - pip.getEquipCost(itemName));
					 pip.setMoney(pip.getMoney() + pip.getEquipCost(itemName));
					 p.sendMessage("§eDu hast dir ein " + itemName + " Equipt");
				 }
				 e.setCancelled(true);
			 }
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}

}
