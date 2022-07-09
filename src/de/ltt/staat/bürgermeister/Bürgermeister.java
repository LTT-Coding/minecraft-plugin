package de.ltt.staat.b�rgermeister;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class B�rgermeister implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.b�rgermeister != "" && Main.b�rgermeister != null) {
				OfflinePlayer b�rgermeister = Bukkit.getOfflinePlayer(UUID.fromString(Main.b�rgermeister));
				p.sendMessage("�eB�rgermeister: �6" + b�rgermeister.getName());
				if(Main.vizeB�rgermeister != "" && Main.vizeB�rgermeister != null) {
					OfflinePlayer vizeB�rgermeister = Bukkit.getOfflinePlayer(UUID.fromString(Main.vizeB�rgermeister));
					p.sendMessage("�eVizeb�rgermeister: �6" + vizeB�rgermeister.getName());
				}else
					p.sendMessage("�cEs gibt aktuell keinen Vizeb�rgermeister!");
			}else
				p.sendMessage("�cEs gibt aktuell keinen B�rgermeister!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
