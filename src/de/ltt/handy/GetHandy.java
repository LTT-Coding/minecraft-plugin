package de.ltt.handy;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GetHandy implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			ItemStack Handy = new ItemStack(Material.HEART_OF_THE_SEA, 1);
			ItemMeta HandyM = Handy.getItemMeta();
			HandyM.setCustomModelData(10000001);
			HandyM.setDisplayName("§6Handy");
			Handy.setItemMeta(HandyM);
			Handy.setAmount(1);
			
			p.getInventory().addItem(Handy);
			
			
		}
		return false;
	}

}
