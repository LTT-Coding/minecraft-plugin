package de.ltt.casino.allgemein;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class RemoveJeton implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player)sender;
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			if(args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                 try {
                    int removeJeton = Integer.parseInt(args[1]);
                    int jeton = Main.getPlugin().getConfig().getInt("Spieler." + target.getUniqueId().toString() + ".jeton");
                    int JJ = jeton - removeJeton;
                    if(target != null) {
                        Main.getPlugin().getConfig().set("Spieler." + target.getUniqueId().toString() + ".jeton",  jeton - removeJeton );
                        p.sendMessage("§aDu hast erfolgreich §6" + target.getName() + " " + removeJeton + " §awegenommen!");
                        target.sendMessage("§6================================");
                        target.sendMessage("§eJetons: " + jeton);
                        target.sendMessage("§eNeue Jetons:§c -" + removeJeton);
                        target.sendMessage("§eInsgesamte Jetons: " + JJ);
                        target.sendMessage("§6================================");
                        
                        Main.getPlugin().saveConfig();
                    }
                }catch (Exception e) {
                    p.sendMessage("§cDeine Eingabe ist keine Zahl!");
                }
                
            }else
                p.sendMessage("§cBenutze §6/removejeton [Spieler] [Zahl] §cum Jetons vom Spieler wegzunehmen!");
			
		} else
			p.sendMessage("§cDazu hast du keine Rechte!");

		return false;
	}

}
