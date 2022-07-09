package de.ltt.modelInv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ltt.server.main.Main;

public class VerkehrSchilder implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,Command cmd,  String lable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
					Inventory VInv = Bukkit.createInventory(null, 3*9, "§6VerkehrSchilder");
					
					for (int i = 0; i < 18; i++) {
						ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
						ItemMeta itemM = item.getItemMeta();
						itemM.setCustomModelData(10000006 + i);
						itemM.setDisplayName("§6VerkehrsSchild");
						item.setItemMeta(itemM);
						VInv.addItem(item);
					}
					p.openInventory(VInv);
					p.sendMessage("§6Inventar wurde geöffnet");
			}else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
			
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	

}
