package de.ltt.server.reflaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;

public class FirmInfo {

	private static int firmidn = 1;
	private int firmid;
	private double firmmoney;
	private String firmname;
	private String firmtype;
	private String owner;
	private List<String> memberList = new ArrayList<String>();
	private List<Integer> memberRank = new ArrayList<Integer>();
	private List<Double> pay = new ArrayList<Double>();
	private List<String> rights = new ArrayList<String>();
	private HashMap<String, Integer> RankMap = new HashMap<String, Integer>();
	private List<String> transactions = new ArrayList<String>();
	
	public FirmInfo() {
		if(Main.getPlugin().getConfig().get("Server.Firmids") != null) {
			firmidn = Main.getPlugin().getConfig().getInt("Server.Firmids");
		}
	}

	public FirmInfo loadfirm(int firmid) {
		this.firmid = firmid;
		if (Main.getPlugin().getConfig().contains("Server.Firms." + firmid)) {
			firmmoney = Main.getPlugin().getConfig().getDouble("Server.Firms." + firmid + ".firmmoney");
			firmname = Main.getPlugin().getConfig().getString("Server.Firms." + firmid + ".firmname");
			firmtype = Main.getPlugin().getConfig().getString("Server.Firms." + firmid + ".firmtype");
			owner = Main.getPlugin().getConfig().getString("Server.Firms." + firmid + ".owner");
			memberList = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".member");
			pay = Main.getPlugin().getConfig().getDoubleList("Server.Firms." + firmid + ".pay");
			rights = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".rights");
			memberRank = Main.getPlugin().getConfig().getIntegerList("Server.Firms." + firmid + ".rank");
			transactions = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".transactions");
			for (int i = 0; i < memberRank.size(); i++) {
				RankMap.put(memberList.get(i), memberRank.get(i));
			}
		}
		return this;
	}

	public FirmInfo addfirm(String firmname, FirmTypes firmtype, String owner) {
		this.firmname = firmname;
		this.firmtype = firmtype.getTranslation();
		this.owner = owner;
		memberList.add(owner);
		memberRank.add(6);
		RankMap.put(owner, 6);
		pay.add(10D);
		pay.add(100D);
		pay.add(150D);
		pay.add(200D);
		pay.add(250D);
		pay.add(250D);
		pay.add(300D);
		if (firmtype.getTranslation() == FirmTypes.IMMOBILIENBÜRO.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.IMMO_BUYPHOUSE.toString());
			rights.add(Rights.IMMO_SELLPHOUSE.toString());
			rights.add(Rights.IMMO_SETPRICE.toString());
		} else if (firmtype.getTranslation() == FirmTypes.BERGBAU.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.MINE_BUYBUILDPLACE.toString());
			rights.add(Rights.MINE_CHANGEPRICE.toString());
			rights.add(Rights.MINE_SELLPRODUCTS.toString());
			rights.add(Rights.MINE_SETPRODUCTS.toString());
		} else if (firmtype.getTranslation() == FirmTypes.EINZELHANDEL.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.SELL_CHANGEPRICE.toString());
			rights.add(Rights.SELL_SELLPROTUCTS.toString());
			rights.add(Rights.SELL_SETPRODUCTS.toString());
		} else if (firmtype.getTranslation() == FirmTypes.LANDWIRTSCHAFT.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.LAWS_BUYBUILDPLACE.toString());
			rights.add(Rights.LAWS_CHANGEPRICE.toString());
			rights.add(Rights.LAWS_SELLPRODUCTS.toString());
			rights.add(Rights.LAWS_SETPRODUCTS.toString());
		} else if (firmtype.getTranslation() == FirmTypes.MINIMARKT.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.SELL_CHANGEPRICE.toString());
			rights.add(Rights.SELL_SELLPROTUCTS.toString());
			rights.add(Rights.SELL_SETPRODUCTS.toString());
			rights.add(Rights.PROC_SETPRODUCTS.toString());
		} else if (firmtype.getTranslation() == FirmTypes.VERTRAGSFIRMA.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
		} else if (firmtype.getTranslation() == FirmTypes.WEITERVERARBEITUNG.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.PROC_CHANGEPRICE.toString());
			rights.add(Rights.PROC_SELLPRODUCTS.toString());
			rights.add(Rights.PROC_SETPRODUCTS.toString());
		}else if(firmtype.getTranslation() == FirmTypes.WASSERVERSORGUNG.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.WATE_SELLTARIF.toString());
			rights.add(Rights.WATE_SELLWATER.toString());
			rights.add(Rights.WATE_SETTARIFS.toString());
			rights.add(Rights.WATE_SETWATERPRICE.toString());
		}else if(firmtype.getTranslation() == FirmTypes.MÜLLENTSORGUNG.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.GARB_GETWASTE.toString());
			rights.add(Rights.GARB_SELLPRODUCTS.toString());
			rights.add(Rights.GARB_SETATOMICWASTEPLACE.toString());
			rights.add(Rights.GARB_SETPRODUCTS.toString());
		}else if(firmtype.getTranslation() == FirmTypes.ENERGIEPRODUZENT.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
			rights.add(Rights.ENER_BUYBURNMATERIAL.toString());
			rights.add(Rights.ENER_SELLTARIF.toString());
			rights.add(Rights.ENER_SETBURNMATERIAL.toString());
			rights.add(Rights.ENER_SETTARIF.toString());
		}else if(firmtype.getTranslation() == FirmTypes.BAUARBEITER.getTranslation()) {
			rights.add(Rights.MAIN_BUYFIRMHOUSE.toString());
			rights.add(Rights.MAIN_CHANGENAME.toString());
			rights.add(Rights.MAIN_DAUERVERTRAG.toString());
			rights.add(Rights.MAIN_FIRMINFO.toString());
			rights.add(Rights.MAIN_FIRMINFORIGHTS.toString());
			rights.add(Rights.MAIN_INVITE.toString());
			rights.add(Rights.MAIN_SELLFIRMHOUSE.toString());
			rights.add(Rights.MAIN_SELLFIRM.toString());
			rights.add(Rights.MAIN_SETPAY.toString());
			rights.add(Rights.MAIN_SETRIGHTS.toString());
			rights.add(Rights.MAIN_UNINVITE.toString());
			rights.add(Rights.MAIN_RANKDOWN.toString());
			rights.add(Rights.MAIN_RANKUP.toString());
			rights.add(Rights.MAIN_CHARGEMONEY.toString());
		}

		firmidn++;
		firmid = firmidn;
		firmmoney = 0;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmname", firmname);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmtype", firmtype.getTranslation());
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".owner", owner);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".member", memberList);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".pay", pay);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rights", rights);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", memberRank);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmmoney", firmmoney);
		Main.getPlugin().getConfig().set("Server.Firmids", firmidn);
		Player p = Bukkit.getPlayer(UUID.fromString(owner));
		new PlayerInfo(p).setJob(firmid);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".memberrights." + owner, rights);
		p.sendMessage("§6===========§eFirma erstellt§6===========");
		p.sendMessage("§6Firmenname: §e" + firmname);
		p.sendMessage("§6Inhaber: §e" + p.getName());
		p.sendMessage("§6Firmenguthaben: §e0€");
		p.sendMessage("§6Mitarbeiterzahl: §e0");
		p.sendMessage("§6Firmentyp: §e" + firmtype.getTranslation());
		p.sendMessage("§6=================================");
		Main.getPlugin().saveConfig();
		return this;
	}

	public double getFirmmoney() {
		return firmmoney;
	}

	public void setFirmmoney(double d) {
		this.firmmoney = d;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmmoney", d);
		Main.getPlugin().saveConfig();
	}

	public String getFirmname() {
		return firmname;
	}

	public void setFirmname(String firmname) {
		this.firmname = firmname;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmname", firmname);
		Main.getPlugin().saveConfig();
	}

	public String getFirmtype() {
		return firmtype;
	}

	public void setFirmtype(FirmTypes firmtype) {
		this.firmtype = firmtype.getTranslation();
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmtype", firmtype.toString());
		Main.getPlugin().saveConfig();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".owner", owner);
		Main.getPlugin().saveConfig();
	}

	public List<String> getMember() {
		return memberList;
	}

	public void setMember(List<String> memberList) {
		this.memberList = memberList;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".member", memberList);
		Main.getPlugin().saveConfig();
	}

	public List<Double> getPay() {
		return pay;
	}

	public void setPay(List<Double> pay) {
		this.pay = pay;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".pay", pay);
		Main.getPlugin().saveConfig();
	}

	public List<String> getRights() {
		return rights;
	}
	
	public List<String> getpRights(OfflinePlayer p){
		List<String> pRights = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".memberrights." + p.getUniqueId());
		return pRights;
	}
	
	public void setpRights(OfflinePlayer p, List<String> pRights) {
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".memberrights." + p.getUniqueId(), pRights);
		Main.getPlugin().saveConfig();
	}

	@SuppressWarnings("null")
	public int getRank(OfflinePlayer t) {
		if (RankMap.containsKey(t.getUniqueId().toString())) {
			return RankMap.get(t.getUniqueId().toString());
		} else
			return (Integer) null;
	}

	public boolean hasrank(OfflinePlayer p, Rights right) {
		List<String> pRights = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".memberrights." + p.getUniqueId());
		String sRight = right.toString();
		if(pRights.contains(sRight)) {
			return true;
		}else return false;
	}
	
	public boolean hasRankString(OfflinePlayer p, String right) {
		List<String> pRights = Main.getPlugin().getConfig().getStringList("Server.Firms." + firmid + ".memberrights." + p.getUniqueId());
		if(pRights.contains(right)) {
			return true;
		}else return false;
	}

	public void addMember(Player t) {
		memberList.add(t.getUniqueId().toString());
		memberRank.add(0);
		RankMap.put(t.getUniqueId().toString(), 0);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".member", memberList);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", memberRank);
		new PlayerInfo(t).setJob(firmid);
		List<String> pRights = new ArrayList<String>();
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".memberrights." + t.getUniqueId(), pRights);
		Main.getPlugin().saveConfig();
	}
	public void removeMember(OfflinePlayer t) {
		memberRank.remove(memberList.indexOf(t.getUniqueId().toString()));
		memberList.remove(t.getUniqueId().toString());
		RankMap.remove(t.getUniqueId().toString());
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".member", memberList);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", memberRank);
		new PlayerInfo(t).setJob(firmid);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".memberrights." + t.getUniqueId(), null);
		Main.getPlugin().saveConfig();
	}
	
	public void removeFirm() {
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmname", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmtype", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".owner", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".member", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".pay", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rights", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".firmmoney", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".ranks", null);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid, null);
		Main.getPlugin().saveConfig();
	}
	
	public void rankUp(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), RankMap.get(t.getUniqueId().toString()) + 1);
		   RankMap.put(t.getUniqueId().toString(),RankMap.get(t.getUniqueId().toString()) + 1);
		   Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", memberRank);
		   Main.getPlugin().saveConfig();
	}
	  
	public void rankDown(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), RankMap.get(t.getUniqueId().toString()) - 1);
		   RankMap.put(t.getUniqueId().toString(),RankMap.get(t.getUniqueId().toString()) - 1);
		   Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".rank", memberRank);
		   Main.getPlugin().saveConfig();
	}
	
	public void resetPay() {
		pay.clear();
		pay.add(10D);
		pay.add(100D);
		pay.add(150D);
		pay.add(200D);
		pay.add(250D);
		pay.add(250D);
		pay.add(300D);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".pay", pay);
	}
	
	public List<String> getTransactions(){
		return transactions;
	}
	
	public void setTransctions(List<String> transactions) {
		this.transactions = transactions;
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".transactions", transactions);
		Main.getPlugin().saveConfig();
	}
	
	public void addTransaction(String transaction) {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	    String formattedDate = myDateObj.format(myFormatObj);
		transaction += " " + formattedDate + "\n§7==================";
		transactions.add(transaction);
		Main.getPlugin().getConfig().set("Server.Firms." + firmid + ".transactions", transactions);
		Main.getPlugin().saveConfig();
	}
	
	public boolean nameIsFree(String name) {
		for(int i = 1;  i < firmidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Firms." + i)) {
				FirmInfo fi = new FirmInfo().loadfirm(i);
				if(fi.getFirmname().equalsIgnoreCase(name)) {
					if(i != firmid) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
