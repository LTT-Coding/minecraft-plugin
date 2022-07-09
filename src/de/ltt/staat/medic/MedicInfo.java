package de.ltt.staat.medic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;

public class MedicInfo {
	
	private List<String> memberList = new ArrayList<String>();
	private List<Integer> memberRank = new ArrayList<Integer>();
	private int money;
	private String owner;
	private List<Integer> payList = new ArrayList<Integer>();
	private List<String> rights = new ArrayList<String>();
	private HashMap<String, Integer> rankMap = new HashMap<String, Integer>();
	
	
	public MedicInfo() {
		if(Main.getPlugin().getMedicConfig().contains("Staat.Medic")) {
			memberList = Main.getPlugin().getMedicConfig().getStringList("Staat.Medic.member");
			memberRank = Main.getPlugin().getMedicConfig().getIntegerList("Staat.Medic.rank");
			money = Main.getPlugin().getMedicConfig().getInt("Staat.Medic.money");
			owner = Main.getPlugin().getMedicConfig().getString("Staat.Medic.owner");
			payList = Main.getPlugin().getMedicConfig().getIntegerList("Staat.Medic.pay");
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
			Main.getPlugin().getMedicConfig().set("Staat.Medic.pay", payList);
			money = 0;
			Main.getPlugin().getMedicConfig().set("Staat.Medic.money", money);
			Main.getPlugin().saveMedicConfig();
		}
	}
	
	public void loadRights() {
		rights.add(MedicRights.INVITE.toString());
		rights.add(MedicRights.SETPLAYERPAY.toString());
		rights.add(MedicRights.SETRANKPAY.toString());
		rights.add(MedicRights.SETRIGHTS.toString());
		rights.add(MedicRights.UNINVITE.toString());
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
		Main.getPlugin().getMedicConfig().set("Staat.Medic.money", money);
		Main.getPlugin().saveMedicConfig();
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
		Main.getPlugin().getMedicConfig().set("Staat.Medic.owner", owner);
	}
	
	public List<String> getMember(){
		return memberList;
	}
	
	public void setMember(List<String> memberList) {
		this.memberList = memberList;
		Main.getPlugin().getMedicConfig().set("Staat.Medic.member", memberList);
		Main.getPlugin().saveMedicConfig();
	}
	
	public List<Integer> getPayRanks(){
		return payList;
	}
	
	public void setPayRanks(List<Integer> payList) {
		this.payList = payList;
		Main.getPlugin().getMedicConfig().set("Staat.Medic.pay", payList);
	}
	
	public int getPayPlayer(OfflinePlayer t) {
		return Main.getPlugin().getMedicConfig().getInt("Staat.Medic.playerpay." + t.getUniqueId());
	}
	
	public void setPayPlayer(OfflinePlayer t, int pay) {
		Main.getPlugin().getMedicConfig().set("Staat.Medic.playerpay." + t.getUniqueId() , pay);
		Main.getPlugin().saveMedicConfig();
	}
	
	public List<String> getpRights(OfflinePlayer t){
		return Main.getPlugin().getMedicConfig().getStringList("Staat.Medic.memberrights." + t.getUniqueId());
	}
	
	public void setpRights(OfflinePlayer t, List<String> pRights) {
		Main.getPlugin().getMedicConfig().set("Staat.Medic.memberrights." + t.getUniqueId(), pRights);
		Main.getPlugin().saveMedicConfig();
	}
	
	public void addpRights(OfflinePlayer t, String right) {
		List<String> rights = getpRights(t);
		rights.add(right);
		Main.getPlugin().getMedicConfig().set("Staat.Medic.memberrights." + t.getUniqueId(), rights);
		Main.getPlugin().saveMedicConfig();
	}
	
	public void removepRights(OfflinePlayer t, String right) {
		List<String> rights = getpRights(t);
		rights.remove(right);
		Main.getPlugin().getMedicConfig().set("Staat.Medic.memberrights." + t.getUniqueId(), rights);
		Main.getPlugin().saveMedicConfig();
	}
	
	public List<String> getRights() {
		return rights;
	}

    private void setRanks(List<Integer> memberRank){
        this.memberRank = memberRank;
        Main.getPlugin().getMedicConfig().set("Staat.Medic.rank", memberRank);
    }
    
    public boolean hasRankString(OfflinePlayer p, String right) {
		List<String> pRights = Main.getPlugin().getMedicConfig().getStringList("Staat.Medic.rank" + p.getUniqueId()); 
		if(pRights.contains(right)) {
			return true;
		}else return false;
	}
    
    public boolean hasRight(OfflinePlayer t, MedicRights right) {
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
        new PlayerInfo(t).setJob(-1);
        
	}

    public void removeMember(OfflinePlayer t) {
		if(memberList.contains(t.getUniqueId().toString())){
            memberRank.remove(memberList.indexOf(t.getUniqueId().toString()));
            setRanks(memberRank);
            memberList.remove(t.getUniqueId().toString());
		    setMember(memberList);
            Main.getPlugin().getMedicConfig().set("Staat.Medic.memberrights." + t.getUniqueId().toString(), null);
            new PlayerInfo(t).setJob(0);
        }
        
	}
	
    public void rankUp(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), rankMap.get(t.getUniqueId().toString()) + 1);
		   rankMap.put(t.getUniqueId().toString(), rankMap.get(t.getUniqueId().toString()) + 1);
		   Main.getPlugin().getMedicConfig().set("Staat.Medic.rank", memberRank);
		   Main.getPlugin().saveMedicConfig();
	}
	  
	public void rankDown(OfflinePlayer t) {
		   memberRank.set(memberList.indexOf(t.getUniqueId().toString()), rankMap.get(t.getUniqueId().toString()) - 1);
		   rankMap.put(t.getUniqueId().toString(),rankMap.get(t.getUniqueId().toString()) - 1);
		   Main.getPlugin().getMedicConfig().set("Staat.Medic.rank", memberRank);
		   Main.getPlugin().saveMedicConfig();
	}
	
	public int getRank(OfflinePlayer t) {
		return rankMap.get(t.getUniqueId().toString());
	}
	

}
