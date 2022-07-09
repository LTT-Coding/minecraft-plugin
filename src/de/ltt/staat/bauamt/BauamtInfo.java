package de.ltt.staat.bauamt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class BauamtInfo {

	private int money;
	private List<String> memberList = new ArrayList<>();
	private List<Integer> memberRank = new ArrayList<>();
	private List<Integer> pay = new ArrayList<>();
	private HashMap<String, Integer> RankMap = new HashMap<>();
	
	public BauamtInfo() {
		FileConfiguration config = Main.getPlugin().getConfig();
		money = config.getInt("Staat.Bauamt.money");
		memberList = config.getStringList("Staat.Bauamt.member");
		memberRank = config.getIntegerList("Staat.Bauamt.rank");
		pay = config.getIntegerList("Staat.Bauamt.pay");
		
		for(int i = 0; i < memberList.size(); i++) {
			RankMap.put(memberList.get(i), memberRank.get(i));
		}
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
		Main.getPlugin().getConfig().set("Staat.Bauamt.money", money);
		Main.getPlugin().saveConfig();
	}

	public List<String> getMember() {
		return memberList;
	}

	public void setMember(List<String> memberList) {
		this.memberList = memberList;
	}

	public List<Integer> getPay() {
		return pay;
	}

	public void setPay(List<Integer> pay) {
		this.pay = pay;
		Main.getPlugin().getConfig().set("Staat.Bauamt.pay", pay);
	}

	public int getRank(OfflinePlayer t) {
		if (RankMap.containsKey(t.getUniqueId().toString())) {
			return RankMap.get(t.getUniqueId().toString());
		} else
			return 0;
	}
	
	public void addMember(Player t) {
		PlayerInfo ti = new PlayerInfo(t);
		memberList.add(t.getUniqueId().toString());
		memberRank.add(1);
		RankMap.put(t.getUniqueId().toString(), 1);
		ti.setBauamt(true);
		Main.getPlugin().getConfig().set("Staat.Bauamt.member", memberList);
		Main.getPlugin().getConfig().set("Staat.Bauamt.rank", memberRank);
		Main.getPlugin().saveConfig();
	}
	
	public void removeMember(OfflinePlayer t) {
		PlayerInfo ti = new PlayerInfo(t);
		memberRank.remove(memberList.indexOf(t.getUniqueId().toString()));
		memberList.remove(t.getUniqueId().toString());
		RankMap.remove(t.getUniqueId().toString());
		ti.setBauamt(false);
		Main.getPlugin().getConfig().set("Staat.Bauamt.member", memberList);
		Main.getPlugin().getConfig().set("Staat.Bauamt.rank", memberRank);
		Main.getPlugin().saveConfig();
	}
	
	public void rankUp(OfflinePlayer t) {
		memberRank.set(memberList.indexOf(t.getUniqueId().toString()), RankMap.get(t.getUniqueId().toString()) + 1);
		   RankMap.put(t.getUniqueId().toString(),RankMap.get(t.getUniqueId().toString()) + 1);
		   Main.getPlugin().getConfig().set("Staat.Bauamt.rank", memberRank);
		   Main.getPlugin().saveConfig();
	}
	public void rankDown(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), RankMap.get(t.getUniqueId().toString()) - 1);
		   RankMap.put(t.getUniqueId().toString(),RankMap.get(t.getUniqueId().toString()) - 1);
		   Main.getPlugin().getConfig().set("Staat.Bauamt.rank", memberRank);
		   Main.getPlugin().saveConfig();
	}
	
	public void resetPay() {
		pay.clear();
		pay.add(150);
		pay.add(250);
		pay.add(350);
		Main.getPlugin().getConfig().set("Staat.Bauamt.pay", pay);
	}
	
	
	
}
