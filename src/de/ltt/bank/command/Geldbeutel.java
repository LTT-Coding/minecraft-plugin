package de.ltt.bank.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class Geldbeutel implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				PlayerInfo pi = new PlayerInfo(p);
				p.sendMessage("§eGeldbeutel: §6" + pi.getMoneyInHand() + "€");
				Main.BroadcastLoc(p.getLocation(), 5, "§6" + p.getName() + " öffnet seinen Geldbeutel");
			} else
				sender.sendMessage("§cBitte benutze /geldbeutel");
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
