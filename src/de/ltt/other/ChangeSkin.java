package de.ltt.other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class ChangeSkin implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command cmd,  String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(Main.Admin.contains(p.getUniqueId().toString())) {
				if(args.length == 1) {
					PlayerInfo pi = new PlayerInfo(p);
					pi.saveInv();
					p.getInventory().clear();
					pi.changeSkin(args[0]);
					p.sendMessage("§eSkin wurde verändert");
					pi.loadInv();
				} else
					p.sendMessage("§cBitte benutze /skin <Name>");
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
