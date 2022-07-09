package de.ltt.staat.bauamt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class BauamtInviteDel implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Bauamt.inviteMap.containsKey(p)) {
				Player own = Bauamt.inviteMap.get(p);
				Bauamt.inviteMap.remove(p);
				own.sendMessage("§6" + p.getName() + "§4 hat das Angebot abgelehnt!");
				p.sendMessage("§4Du hast das Angebot von §6" + own.getName() + "§4 abgelehnt!");
			}else
				p.sendMessage("§cDu hast keine Einladung in das Bauamt!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
