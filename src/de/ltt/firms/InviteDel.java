package de.ltt.firms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class InviteDel implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.inviteMap.containsKey(p)) {
				Player own = Main.inviteMap.get(p);
				Main.inviteMap.remove(p);
				own.sendMessage("§6" + p.getName() + "§4 hat das Angebot abgelehnt!");
				p.sendMessage("§4Du hast das Angebot von §6" + own.getName() + "§4 abgelehnt");
			}else
				p.sendMessage("§CDu wurdes von keiner Firma eingeladen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		
		return false;
	}

}
