package de.ltt.other;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class AFKCommand implements CommandExecutor{
	
	public static List<Player> temporaryAFK = new ArrayList<Player>();
	
	@Override
	public boolean onCommand(CommandSender sender,  Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!temporaryAFK.contains(p)) {
				temporaryAFK.add(p);
				p.sendMessage("§eDu bist nun AFK!");
				for(Player current : Bukkit.getOnlinePlayers()) 
					if(p.getLocation().distance(current.getLocation()) < 5 && p != current) 
						current.sendMessage("§6" + p.getName() + "§e ist in Gedanken versunken!");
			}
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

	
	
}
