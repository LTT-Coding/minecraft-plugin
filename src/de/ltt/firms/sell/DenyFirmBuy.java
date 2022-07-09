package de.ltt.firms.sell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.firms.Firma;
import de.ltt.server.main.Main;

public class DenyFirmBuy implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Firma.selledPlayer.containsKey(p)) {
				Firma.selledPlayer.get(p).sendMessage("§6" + p.getName() + "§4 hat das Angebot abgelehnt!");
				Firma.selledPlayer.remove(p);
				Firma.selledPrice.remove(p);
				p.sendMessage("§4Du hast das Angebot abgelehnt!");
			}else
				p.sendMessage("§cDir hat niemand eine Firma zum Kauf angeboten!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
