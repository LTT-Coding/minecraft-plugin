package de.ltt.staat.police.grundSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class PoliceInfo {
	
	private List<String> memberList = new ArrayList<String>();
	private List<Integer> memberRank = new ArrayList<Integer>();
	private int money;
	private String owner;
	private List<Integer> payList = new ArrayList<Integer>();
	private List<String> rights = new ArrayList<String>();
	private HashMap<String, Integer> rankMap = new HashMap<String, Integer>();
	
	
	public PoliceInfo() {
		if(Main.getPlugin().getPoliceConfig().contains("Staat.Polizei")) {
			memberList = Main.getPlugin().getPoliceConfig().getStringList("Staat.Polizei.member");
			memberRank = Main.getPlugin().getPoliceConfig().getIntegerList("Staat.Polizei.rank");
			money = Main.getPlugin().getPoliceConfig().getInt("Staat.Polizei.money");
			owner = Main.getPlugin().getPoliceConfig().getString("Staat.Polizei.owner");
			payList = Main.getPlugin().getPoliceConfig().getIntegerList("Staat.Polizei.pay");
			loadRights();
			for(int i = 0; i < memberList.size(); i++) {
				rankMap.put(memberList.get(i), memberRank.get(i));
			}
		}else {
			payList.add(10);
			payList.add(100);
			payList.add(150);
			payList.add(200);
			payList.add(250);
			payList.add(250);
			payList.add(300);
			payList.add(350);
			payList.add(400);
			Main.getPlugin().getPoliceConfig().set("Staat.Polizei.pay", payList);
			money = 0;
			Main.getPlugin().getPoliceConfig().set("Staat.Polizei.money", money);
			Main.getPlugin().savePoliceConfig();
		}
	}
	
	public void loadRights() {
		rights.add(PoliceRights.INVITE.toString());
		rights.add(PoliceRights.SETPLAYERPAY.toString());
		rights.add(PoliceRights.SETRANKPAY.toString());
		rights.add(PoliceRights.UNINVITE.toString());
		rights.add(PoliceRights.SETRIGHTS.toString());
		rights.add(PoliceRights.EQUIP.toString());
		rights.add(PoliceRights.EQUIPEINSTELLEN.toString());
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.money", money);
		Main.getPlugin().savePoliceConfig();
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.owner", owner);
	}
	
	public List<String> getMember(){
		return memberList;
	}
	
	public void setMember(List<String> memberList) {
		this.memberList = memberList;
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.member", memberList);
		Main.getPlugin().savePoliceConfig();
	}
	
	public List<Integer> getPayRanks(){
		return payList;
	}
	
	public void setPayRanks(List<Integer> payList) {
		this.payList = payList;
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.pay", payList);
	}
	
	public int getPayPlayer(OfflinePlayer t) {
		return Main.getPlugin().getPoliceConfig().getInt("Staat.Polizei.playerpay." + t.getUniqueId());
	}
	
	public void setPayPlayer(OfflinePlayer t, int pay) {
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.playerpay." + t.getUniqueId() , pay);
		Main.getPlugin().savePoliceConfig();
	}
	
	public List<String> getpRights(OfflinePlayer t){
		return Main.getPlugin().getPoliceConfig().getStringList("Staat.Polizei.memberrights." + t.getUniqueId());
	}
	
	public void setpRights(OfflinePlayer t, List<String> pRights) {
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.memberrights." + t.getUniqueId(), pRights);
		Main.getPlugin().savePoliceConfig();
	}
	
	public void addpRights(OfflinePlayer t, String right) {
		List<String> rights = getpRights(t);
		rights.add(right);
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.memberrights." + t.getUniqueId(), rights);
		Main.getPlugin().savePoliceConfig();
	}
	
	public void removepRights(OfflinePlayer t, String right) {
		List<String> rights = getpRights(t);
		rights.remove(right);
		Main.getPlugin().getPoliceConfig().set("Staat.Polizei.memberrights." + t.getUniqueId(), rights);
		Main.getPlugin().savePoliceConfig();
	}
	
	public List<String> getRights() {
		return rights;
	}

    private void setRanks(List<Integer> memberRank){
        this.memberRank = memberRank;
        Main.getPlugin().getPoliceConfig().set("Staat.Polizei.rank", memberRank);
    }
    
    public boolean hasRankString(OfflinePlayer p, String right) {
		List<String> pRights = Main.getPlugin().getPoliceConfig().getStringList("Staat.Polizei.rank" + p.getUniqueId()); 
		if(pRights.contains(right)) {
			return true;
		}else return false;
	}
    
    public boolean hasRight(OfflinePlayer t, PoliceRights right) {
    	if(Main.Admin.contains(t.getUniqueId().toString())) return true;
    	if(!memberList.contains(t.getUniqueId().toString()))return false;
    	if(getpRights(t).contains(right.toString()))return true;
    	return false;
    }
	
	public void addmember(Player t) {
		memberList.add(t.getUniqueId().toString());
		setMember(memberList);
		memberRank.add(0);
        setRanks(memberRank);
        List<String> rights = new ArrayList<String>();
        setpRights(t, rights);
        new PlayerInfo(t).setJob(-2);
        
	}

    public void removeMember(OfflinePlayer t) {
		if(memberList.contains(t.getUniqueId().toString())){
            memberRank.remove(memberList.indexOf(t.getUniqueId().toString()));
            setRanks(memberRank);
            memberList.remove(t.getUniqueId().toString());
		    setMember(memberList);
            Main.getPlugin().getPoliceConfig().set("Staat.Polizei.memberrights." + t.getUniqueId().toString(), null);
            new PlayerInfo(t).setJob(0);
        }
        
	}
	
    public void rankUp(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), rankMap.get(t.getUniqueId().toString()) + 1);
		   rankMap.put(t.getUniqueId().toString(), rankMap.get(t.getUniqueId().toString()) + 1);
		   Main.getPlugin().getPoliceConfig().set("Staat.Polizei.rank", memberRank);
		   Main.getPlugin().savePoliceConfig();
	}
	  
	public void rankDown(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), rankMap.get(t.getUniqueId().toString()) - 1);
		   rankMap.put(t.getUniqueId().toString(),rankMap.get(t.getUniqueId().toString()) - 1);
		   Main.getPlugin().getPoliceConfig().set("Staat.Polizei.rank", memberRank);
		   Main.getPlugin().savePoliceConfig();
	}
	
	public int getRank(OfflinePlayer t) {
		return rankMap.get(t.getUniqueId().toString());
	}
	
	public int getEquipCost(String ItemName) {
		int cost = 0;
		if(Main.PoliceEquipCost.containsKey(ItemName)) {
			cost = Main.PoliceEquipCost.get(ItemName);
		}
		return cost; 
	}
	
	public String getEquipCostString(String ItemName) {
		String cost = "0";
		if(Main.PoliceEquipCost.containsKey(ItemName)) {
			cost = Main.PoliceEquipCost.get(ItemName).toString();
		}
		return cost; 
	}
	

}
