package de.ltt.staat.bauamt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BauamtInviteAcc implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Bauamt.inviteMap.containsKey(p)) {
				Player own = Bauamt.inviteMap.get(p);
				BauamtInfo bi = new BauamtInfo();
				bi.addMember(p);
				p.sendMessage("§aDu bist dem §6Bauamt§a beigetreten!");
				own.sendMessage("§aDer Spieler §6" + p.getName() + "§a ist dem §6Bauamt§a beigetreten!");
				Bauamt.inviteMap.remove(p);
			}else
				p.sendMessage("§cDu hast keine Einladung in das Bauamt!");
		}
		return false;
	}

}
