package de.ltt.auto;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.ltt.auto.cars.RTW;

public class SpawnCar implements CommandExecutor, Listener{

	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
        	RTW rtw = new RTW(((Player) sender).getUniqueId().toString(), "Hello", ((Player) sender).getLocation());
			rtw.spawnCar();
			Car.cars.add(rtw);
        }
        return false;
    }
	

}