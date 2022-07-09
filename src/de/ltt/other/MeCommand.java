package de.ltt.other;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class MeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			PlayerInfo pi = new PlayerInfo(player);
			if(args.length == 0) {
				Inventory Inv = Bukkit.createInventory(null, 9*3, "§eWähle eine Aktion");
				if(pi.haspacked()) {
					Player t = PlayerInteractInv.packMap.get(player);
					ItemStack KopfSpieler = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
					SkullMeta KopfMeta = (SkullMeta) KopfSpieler.getItemMeta();
					KopfMeta.setOwner(t.getName());
					KopfMeta.setDisplayName("§6" + t.getName());
					KopfSpieler.setItemMeta(KopfMeta);
					Inv.addItem(KopfSpieler);
				}
				ItemStack sit = new ItemBuilder(Material.OAK_STAIRS).setName("§6Hinsetzen").build();
				Inv.addItem(sit);
				player.openInventory(Inv);
			}else {
				String action = "";
				for(int i = 0; i<args.length; i++) {
					action += " " + args[i];
				}
				Main.BroadcastLoc(player.getLocation(), 5, "§b*" + player.getName() + action);
			}
		}
		return false;
	}

}
