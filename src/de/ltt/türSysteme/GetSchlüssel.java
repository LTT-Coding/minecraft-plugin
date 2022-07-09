package de.ltt.türSysteme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class GetSchlüssel implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command cmd,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				Inventory KeyInv = Bukkit.createInventory(null, 9,"§eKey Inventory");
				
				String key = "";
				for(int i = 0; i < 14; i++) {
					Random generator = new Random();
					int num = generator.nextInt(9);
					key += num;
				}
				ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA, 1);
				ItemMeta itemM = item.getItemMeta();
				itemM.setCustomModelData(10000002);
				itemM.setDisplayName("§6Schlüsselkarte");
				List<String> nummer = new ArrayList<String>();
				nummer.add(key);
				itemM.setLore(nummer);
				item.setItemMeta(itemM);
				
				ItemStack PolizeiKey = new ItemStack(Material.WHITE_STAINED_GLASS, 1);
				ItemMeta PolizeiKeyM = PolizeiKey.getItemMeta();
				PolizeiKeyM.setCustomModelData(10000005);
				PolizeiKeyM.setDisplayName("§6Schlüsselkarte");
				PolizeiKeyM.setLore(nummer);
				PolizeiKey.setItemMeta(PolizeiKeyM);
				
				String uuid = UUID.randomUUID().toString();
				ItemStack bank = new ItemBuilder(Material.PAPER).setName("§6Bankkarte").setLore(key).setTag("ID", uuid).setTag("Own", p.getUniqueId().toString()).build();
		        new PlayerInfo(p).setBankCard(uuid);
				
				KeyInv.addItem(PolizeiKey);
				KeyInv.addItem(item);
				KeyInv.addItem(bank);
				p.openInventory(KeyInv);

			}
		}
		return false;
	}
	

}
