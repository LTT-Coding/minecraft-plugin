package de.ltt.firms.sell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.firms.Firma;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.PlayerInfo;

public class AcceptFirmBuy implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Firma.selledPlayer.containsKey(p)) {
				int price = Firma.selledPrice.get(p);
				PlayerInfo pi = new PlayerInfo(p);
				if(pi.getMoney() >= price) {
					FirmInfo fi = new FirmInfo().loadfirm(new PlayerInfo(Firma.selledPlayer.get(p)).getJob());
					if(pi.getJob() == 0 || fi.getMember().contains(p.getUniqueId().toString())) {
						Player t = Firma.selledPlayer.get(p);
						pi.subtractMoney(price);
						fi.setOwner(p.getUniqueId().toString());
						fi.removeMember(Firma.selledPlayer.get(p));
						if(!fi.getMember().contains(p.getUniqueId().toString())) {
							fi.addMember(p);
						}
						fi.setpRights(p, fi.getRights());
						while(fi.getRank(p) < 6) {
							fi.rankUp(p);
						}
						p.sendMessage("§aDu bist nun Besitzer der Firma §6" + fi.getFirmname() + "§a!");
						t.sendMessage("§aDu hast deine Firma an §6" + p.getName() + "§a verkauft!");
						Firma.selledPlayer.remove(p);
						Firma.selledPrice.remove(p);
					}else
						p.sendMessage("§cDu musst arbeitslos oder Mitarbeiter dieser Firma sein, um diese zu kaufen!");
				}else {
					p.sendMessage("§cDazu hast du nicht genügend Geld!");
					Firma.selledPlayer.remove(p);
					Firma.selledPrice.remove(p);
					p.sendMessage("§4Vorgang abgebrochen!");
				}
				
			}else
				p.sendMessage("§cDir hat niemand eine Firma zum Kauf angeboten!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}
	
	

}
