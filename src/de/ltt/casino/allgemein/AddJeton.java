package de.ltt.casino.allgemein;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class AddJeton implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player)sender;
		if(Main.Admin.contains(p.getUniqueId().toString())) {
			if(args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                 try {
                    int addJeton = Integer.parseInt(args[1]);
                    int jeton = Main.getPlugin().getConfig().getInt("Spieler." + target.getUniqueId().toString() + ".jeton");
                    int JJ = addJeton + jeton;
                    if(target != null) {
                        Main.getPlugin().getConfig().set("Spieler." + target.getUniqueId().toString() + ".jeton", jeton + addJeton);
                        p.sendMessage("§aJetonguthaben erfolgreich geändert!");
                        target.sendMessage("§6================================");
                        target.sendMessage("§eJetons: " + jeton);
                        target.sendMessage("§eNeue Jetons:§a +" + addJeton);
                        target.sendMessage("§eInsgesamte Jetons: " + JJ);
                        target.sendMessage("§6================================");
                        
                        Main.getPlugin().saveConfig();
                    }
                }catch (Exception e) {
                    p.sendMessage("§cDeine Eingabe ist keine Zahl!");
                }
                
            }else
                p.sendMessage("§cBenutze §6/addjeton [Spieler] [Zahl] §cum einem Spieler Jeton zu adden!");
			
		} else
			p.sendMessage(Main.KEINE_RECHTE);

		return false;
	}

}
