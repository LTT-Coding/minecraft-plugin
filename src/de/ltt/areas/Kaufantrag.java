package de.ltt.areas;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.OwnerType;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.area.AreaInfo;

public class Kaufantrag implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(AreaInfo.hasRequested(p)) {
					AreaInfo ai = AreaInfo.getRequest(p);
					if(args[0].equalsIgnoreCase("annehmen")) {
						int fläche = (ai.getlX()-ai.getsX() + 1)*(ai.getlZ() - ai.getsZ() + 1);
						double price = Main.areaPriceMap.get(ai.getAreaid());
						double kosten = fläche*price;
						PlayerInfo pi = new PlayerInfo(p);
						if(pi.getMoney() >= kosten) {
							pi.subtractMoney(kosten);
							new AreaInfo().addArea(ai.getsX(), ai.getlX(), ai.getsZ(), ai.getlZ(), OwnerType.PRIVATE, p.getUniqueId().toString());
							ai.removeRequest();
							p.sendMessage("§aDu hast das Grundstück für §6" + kosten + "€ §agekauft!");
							SQLData.removeAreaPrice(ai.getAreaid());
							pi.addTransaction("§4-" + kosten + "€ §6 || Hauskauf |");
						}else {
							p.sendMessage("§cDazu hast du nicht genug Geld!");
							p.sendMessage("§cWenn du genug Geld hast, kannst du den Befehl erneut ausführen!");
						}
					}else if(args[0].equalsIgnoreCase("ablehnen")) {
						SQLData.removeAreaPrice(ai.getAreaid());
						ai.removeRequest();
						
					}else 
						p.sendMessage("§cBenutze §6/kaufantrag annehmen/ablehnen §cum das Angebot vom Bauamt zu akzeptieren oder abzulehnen!");
				}else
					p.sendMessage("§cDazu musst du erst einen Kaufantrag stellen!");
			}else 
				p.sendMessage("§cBenutze §6/kaufantrag annehmen/ablehnen §cum das Angebot vom Bauamt zu akzeptieren oder abzulehnen!");
		}else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
