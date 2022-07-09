package de.ltt.bank.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class SeeMoney implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(Main.checkCharName(args[0])) {
					PlayerInfo pi = new PlayerInfo(Main.getChar(args[0]));
					p.sendMessage("§6========§e" + pi.getFirstName() + " " + pi.getLastName() + "§6========");
					p.sendMessage("§6Bargeld: §e" + pi.getMoneyInHand() + "€");
					p.sendMessage("§6Kontostand: §e" + pi.getMoney() + "€");
					p.sendMessage("§6Insgesamt: §e" + (pi.getMoney() + pi.getMoneyInHand()) + "€");
					p.sendMessage("§6=====================================");
				} else 
					p.sendMessage("§cDiesen Charakter gibt es nicht");
			} else 
				p.sendMessage("§cBitte benutze /seemoney [Vorname_Nachname]");
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
