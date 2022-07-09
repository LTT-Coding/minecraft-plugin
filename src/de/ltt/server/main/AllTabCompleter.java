package de.ltt.server.main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.staat.medic.MedicInfo;
import de.ltt.staat.police.grundSystem.PoliceInfo;

public class AllTabCompleter implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender,  Command command, String label,  String[] args) {
		final String name = command.getName().toLowerCase();
		if(name.equals("Admin") || name.equals("Moderator") || name.equals("Supporter") || name.equals("invsee") 
				|| name.equals("einstellen") || name.equals("einladen") || name.equals("invite") || name.equals("kuendigen")
				|| name.equals("rauswerfen") || name.equals("befoerdern") || name.equals("degradieren") || name.equals("addjeton")
				|| name.equals("removejeton") || name.equals("seejeton") || name.equals("seejetonadmin")) {
			if(args.length >= 2) { 
				List<String> PlayerTab = new ArrayList<String>();
				for (Player p : Bukkit.getOnlinePlayers()) {
					if(p.getName().toLowerCase().startsWith(args[0])) {
						PlayerTab.add(p.getName());
					}
				}
				return PlayerTab;
			}
		} else if(name.equals("holo")) {
			List<String> holoTab = new ArrayList<String>();
			holoTab.add("add");
			holoTab.add("remove");
			holoTab.add("all");
			holoTab.add("list");
			holoTab.add("tp");
			
			if(args[0].equals("remove") || args[0].equals("tp")) {
				List<String> holoTabNummer = new ArrayList<String>();
				for (int i = 0; i < Main.holo.size(); i++) {
					String nummer = i + "";
					holoTabNummer.add(nummer);
				}
				return holoTabNummer;
			}
			return holoTab;
		} else if(name.equals("gm")) {
			if(args.length > 1 && args.length <= 2) {
				
			} else if(args.length == 1){
				List<String> gmTab = new ArrayList<String>();
				gmTab.add("0");
				gmTab.add("1");
				gmTab.add("2");
				gmTab.add("3");
				return gmTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		} else if(name.equals("wahl")) {
			if(args.length == 1){
				List<String> wahlTab = new ArrayList<String>();
				wahlTab.add("start");
				wahlTab.add("stop");
				return wahlTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		} else if(name.equals("hashtags") || name.equals("bugsTodo")) {
			if(args.length == 1){
				List<String> wahlTab = new ArrayList<String>();
				wahlTab.add("add");
				wahlTab.add("remove");
				return wahlTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		}else if(name.equals("bauamt")) {
			if(args.length == 1) {
				List<String> wahlTab = new ArrayList<>();
				wahlTab.add("einstellen");
				return wahlTab;
			}
		}else if(name.equals("medicdoor") || name.equals("polizeidoor") || name.equals("blocksperre")) {
			//if(args.length <= 2){
				List<String> doorTab = new ArrayList<String>();
				doorTab.add("add");
				doorTab.add("remove");
				doorTab.add("finish");
				return doorTab;
		//	} else {
			//	List<String> NotTab = new ArrayList<String>();
			//	return NotTab;
		//	}
			
		}else if(name.equals("lift")) {
			if(args.length == 1) {
				List<String> liftTab = new ArrayList<>();
				liftTab.add("register");
				liftTab.add("remove");
				liftTab.add("cancel");
				liftTab.add("finish");
				return liftTab;
			}else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		}else if(name.equals("haus")) {
			if(args.length == 1) {
				List<String> hausTab = new ArrayList<>();
				hausTab.add("kaufen");
				return hausTab;
			}else if(args.length == 2){
				List<String> hausTab = new ArrayList<String>();
				hausTab.add("cancel");
				return hausTab;
			}else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
				
		} else if(name.equals("wartung")) {
			if(args.length == 1) {
				List<String> hausTab = new ArrayList<>();
				hausTab.add("on");
				hausTab.add("off");
				hausTab.add("add");
				hausTab.add("remove");
				hausTab.add("list");
				hausTab.add("listremove");
				hausTab.add("listremoveplayer");
				return hausTab;
			}else if(args[0].equals("add")){
				List<String> PlayerTab = new ArrayList<String>();
				return PlayerTab;
			}else if(args[0].equals("remove")){
				List<String> WartungSpieler = new ArrayList<String>();
				for (int i = 0; i < Main.AddWartungPlayer.size(); i++) {
					WartungSpieler.add(Main.AddWartungPlayer.get(i));
				}
				return WartungSpieler;
			}else if(args[0].equals("listremoveplayer")){
				List<String> WartungSpieler = new ArrayList<String>();
				for (String string : Main.JoinPlayerZahl.keySet()) {
					OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(string)); 
					WartungSpieler.add(t.getName());
				}
				return WartungSpieler;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
			
		} else if(name.equals("cmdsperre")) {
			if(args.length == 1) {
				List<String> cmdTab = new ArrayList<>();
				cmdTab.add("add");
				cmdTab.add("remove");
				cmdTab.add("list");
				return cmdTab;
			}else if(args[0].equals("add")){
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}else if(args[0].equals("remove")){
				List<String> GesperrteCommand = new ArrayList<String>();
				for (int i = 0; i < Main.GesperrteCMD.size(); i++) {
					GesperrteCommand.add(Main.GesperrteCMD.get(i));
				}
				return GesperrteCommand;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		} else if(name.equals("kopf")) {
			if(args.length == 1) {
				List<String> kopfTab = new ArrayList<>();
				kopfTab.add("open");
				kopfTab.add("add");
				kopfTab.add("remove");
				kopfTab.add("spieler");
				kopfTab.add("get");
				kopfTab.add("search");
				kopfTab.add("searchadd");
				kopfTab.add("searchremove");
				return kopfTab;
			}else if(args[0].equals("spieler")){
                List<String> PlayerTab = new ArrayList<String>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                	PlayerTab.add(player.getName());
				}
				return PlayerTab;
			}else if(args[0].equals("search") || args[0].equals("searchremove")){
                List<String> searchTab = new ArrayList<String>();
                for (int i = 0; i < Main.KopfSearch.size(); i++) {
                	searchTab.add(Main.KopfSearch.get(i));
				}
				return searchTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		}else if(name.equals("model")) {
			if(args.length == 1) {
				List<String> kopfTab = new ArrayList<>();
				kopfTab.add("open");
				kopfTab.add("add");
				kopfTab.add("remove");
				kopfTab.add("search");
				kopfTab.add("searchadd");
				kopfTab.add("searchremove");
				return kopfTab;
			}else if(args[0].equals("search") || args[0].equals("searchremove")){
                List<String> searchTab = new ArrayList<String>();
                for (int i = 0; i < Main.ModelSearch.size(); i++) {
                	searchTab.add(Main.ModelSearch.get(i));
				}
				return searchTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		} else if(name.equals("sinnlosefakten")) {
			if(args.length == 1) {
				List<String> SinnTab = new ArrayList<>();
				SinnTab.add("add");
				SinnTab.add("remove");
				SinnTab.add("list");
				return SinnTab;
			}else if(args[0].equals("remove")){
                List<String> SinnRTab = new ArrayList<String>();
                for (int i = 0; i < Main.SinnloseFakten.size(); i++) {
					SinnRTab.add(Main.SinnloseFakten.get(i));
				}
				return SinnRTab;
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		}else if(name.equals("registernavipoint")) {
			if(args.length == 1){
				List<String> realTab = new ArrayList<String>();
				List<String> tab = new ArrayList<String>();
				tab.add("add");
				tab.add("remove");
				tab.add("cancel");
				for(String current : tab) {
					if(current.startsWith(args[0])) {
						realTab.add(current);
					}
				}
				return realTab;
			}else if(args.length >= 2) {
				if(args[0].equalsIgnoreCase("remove")) {
					
					List<String> tab = new ArrayList<String>();
					String names = "";
					for(int i = 1; i < args.length; i++) {
						names += args[i];
						if(i < args.length - 1) {
							names += " ";
						}
					}
					for(String current : Main.naviPoints.keySet()) {
						if(current.toLowerCase().startsWith(names.toLowerCase())) {
							tab.add(current);
						}
					}
					return tab;
				}else {
					List<String> NotTab = new ArrayList<String>();
					return NotTab;
				}
			}
		} else if(name.equalsIgnoreCase("geheimtuer")) {
			if(args.length == 1) {
				List<String> kopfTab = new ArrayList<>();
				kopfTab.add("finish");
				kopfTab.add("add");
				kopfTab.add("remove");
				kopfTab.add("abbrechen");
				return kopfTab;
			
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		} else if(name.equalsIgnoreCase("rettungsdienst")) {
			if(args.length == 1) {
				List<String> medicTab = new ArrayList<>();
				List<String> realTab = new ArrayList<>();
				medicTab.add("einstellen");
				medicTab.add("kündigen");
				medicTab.add("rechte");
				medicTab.add("rauswerfen");
				medicTab.add("bezahlung");
				medicTab.add("besitzer");
				for(String current : medicTab) {
					if(current.startsWith(args[0])) {
						realTab.add(current);
					}
				}
				return realTab;
			
			} else if(args.length == 2) {
				if(args[1].equalsIgnoreCase("einstellen")) {
					List<String> medicInviteTab = new ArrayList<>();
					MedicInfo mi = new MedicInfo();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if(!(mi.getMember() == p)) {
							medicInviteTab.add(p.getName());
						}
					}
					return medicInviteTab;
				} else if(args[1].equalsIgnoreCase("kündigen") || args[1].equalsIgnoreCase("rauswerfen")) {
					List<String> medicUninviteTab = new ArrayList<>();
					MedicInfo mi = new MedicInfo();
					for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
						if(mi.getMember() == p) {
							medicUninviteTab.add(p.getName());
						}
					}
					return medicUninviteTab;
				}
			}
			} else if(name.equalsIgnoreCase("npc")) {
				if(args.length == 1) {
					List<String> PoliceTab = new ArrayList<>();
					List<String> realTab = new ArrayList<>();
					PoliceTab.add("create");
					PoliceTab.add("remove");
					for(String current : PoliceTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				
				} else if(args.length == 2) {
					List<String> PoliceTab = new ArrayList<>();
					PoliceTab.add("<name>");
					return PoliceTab;

				} else if(args.length == 3) {
					List<String> PoliceTab = new ArrayList<>();
					PoliceTab.add("<skin>");
					return PoliceTab;
					
				}
			}
		else if(name.equalsIgnoreCase("polizei")) {
				if(args.length == 1) {
					List<String> PoliceTab = new ArrayList<>();
					List<String> realTab = new ArrayList<>();
					PoliceTab.add("einstellen");
					PoliceTab.add("kündigen");
					PoliceTab.add("rechte");
					PoliceTab.add("rauswerfen");
					PoliceTab.add("bezahlung");
					PoliceTab.add("besitzer");
					PoliceTab.add("equip");
					for(String current : PoliceTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				
				} else if(args.length == 2) {
					if(args[1].equalsIgnoreCase("einstellen")) {
						List<String> PoliceInviteTab = new ArrayList<>();
						PoliceInfo mi = new PoliceInfo();
						for (Player p : Bukkit.getOnlinePlayers()) {
							if(!(mi.getMember() == p)) {
								PoliceInviteTab.add(p.getName());
							}
						}
						return PoliceInviteTab;
					} else if(args[1].equalsIgnoreCase("kündigen") || args[1].equalsIgnoreCase("rauswerfen")) {
						List<String> PoliceUninviteTab = new ArrayList<>();
						PoliceInfo mi = new PoliceInfo();
						for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
							if(mi.getMember() == p) {
								PoliceUninviteTab.add(p.getName());
							}
						}
						return PoliceUninviteTab;
					}
				}
				} else if(name.equals("tp")) {
					
						List<String> TPTab = new ArrayList<>();
						List<String> realTab = new ArrayList<>();
						for(Player current : Bukkit.getOnlinePlayers()) {
							TPTab.add(current.getName());
						}
						TPTab.add("@a");
						TPTab.add("@e");
						TPTab.add("@p");
						TPTab.add("@r");
						TPTab.add("@s");
						TPTab.add("~");
						TPTab.add("~ ~");
						TPTab.add("~ ~ ~");
						
						for(String current : TPTab) {
							if(current.startsWith(args[0])) {
								realTab.add(current);
							}
						}
						return realTab;
					
					
				} else if(name.equals("polizeicomputer")) {
					
					List<String> TPTab = new ArrayList<>();
					List<String> realTab = new ArrayList<>();

					TPTab.add("add");
					TPTab.add("remove");
					
					for(String current : TPTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				
				
			} else if(name.equals("money")) {
				if(args.length == 1) {
					List<String> mTab  = new ArrayList<String>();
					List<String> realTab = new ArrayList<>();
					mTab.add("Konto");
					mTab.add("Hand");
					for(String current : mTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				} else if(args.length == 2) {
					List<String> mTab1  = new ArrayList<String>();
					List<String> realTab1 = new ArrayList<>();
					mTab1.add("add");
					mTab1.add("remove");
					mTab1.add("set");
					for(String current : mTab1) {
						if(current.startsWith(args[1])) {
							realTab1.add(current);
						}
					}
					return realTab1;
				} else if(args.length == 3) {
					List<String> mTab2  = Main.getChars();
					List<String> realTab2 = new ArrayList<>();
					for(String current : mTab2) {
						if(current.startsWith(args[2])) {
							realTab2.add(current);
						}
					}
					return realTab2;
				} else {
					List<String> NotTab = new ArrayList<String>();
					return NotTab;
				}
					
				
			} else if(name.equals("seemoney")) {
				if(args.length == 1) {
					List<String> mTab  = Main.getChars();
					List<String> realTab = new ArrayList<>();
					for(String current : mTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				} else {
					List<String> NotTab = new ArrayList<String>();
					return NotTab;
				}
			} else if(name.equals("bannedword")) {
				if(args.length == 1) {
					List<String> mTab  = new ArrayList<>();
					mTab.add("add");
					mTab.add("remove");
					List<String> realTab = new ArrayList<>();
					for(String current : mTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				} else if(args.length == 2) {
					if(args[1].equals("remove")) {
						List<String> mTab  = new ArrayList<>();
						for (String string : Main.BannedWords) {
							mTab.add(string);
						}
						List<String> realTab = new ArrayList<>();
						for(String current : mTab) {
							if(current.startsWith(args[0])) {
								realTab.add(current);
							}
						}
						return realTab;
					} else {
						List<String> NotTab = new ArrayList<String>();
						return NotTab;
					}
				} else {
					List<String> NotTab = new ArrayList<String>();
					return NotTab;
				}
			} else if(name.equals("lightning")) {
				if(args.length == 1) {
					List<String> mTab  = new ArrayList<>();
					mTab.add("add");
					mTab.add("remove");
					mTab.add("start");
					List<String> realTab = new ArrayList<>();
					for(String current : mTab) {
						if(current.startsWith(args[0])) {
							realTab.add(current);
						}
					}
					return realTab;
				} else if(args.length == 2) {
					if(args[1].equals("remove")) {
						List<String> mTab  = new ArrayList<>();
						mTab.add("2");
						mTab.add("5");
						mTab.add("10");
						
						List<String> realTab = new ArrayList<>();
						for(String current : mTab) {
							if(current.startsWith(args[0])) {
								realTab.add(current);
							}
						}
						return realTab;
					} else {
						List<String> NotTab = new ArrayList<String>();
						return NotTab;
					}
				} else {
					List<String> NotTab = new ArrayList<String>();
					return NotTab;
				}
			} else {
				List<String> NotTab = new ArrayList<String>();
				return NotTab;
			}
		
		
		return null;
	}

}
