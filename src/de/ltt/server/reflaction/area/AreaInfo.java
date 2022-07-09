package de.ltt.server.reflaction.area;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.ltt.areas.AreaRequest;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.OwnerType;

public class AreaInfo {
	private static int areaidn = 0;
	private static int requestidn = 0;
	
	private boolean isaccepted;
	private int areaid;
	private int sX;
	private int lX ;
	private int sZ;
	private int lZ;
	private String owner;
	private int ownFirm;
	private OwnerType ownerType;
	private int energyFirm;
	private int waterFirm;
	
	public AreaInfo() {
		if(Main.getPlugin().getConfig().get("Server.Areaids") != null ) {
			areaidn = Main.getPlugin().getConfig().getInt("Server.Areaids");
		}
		if(Main.getPlugin().getConfig().get("Server.Requestids") != null ) {
			requestidn = Main.getPlugin().getConfig().getInt("Server.Requestids");
		}
	}
	
	public AreaInfo loadArea(int areaid) {
		this.areaid = areaid;
		if(Main.getPlugin().getConfig().contains("Server.Areas." + areaid)) {
			sX = Main.getPlugin().getConfig().getInt("Server.Areas." + areaid + ".sX");
			lX = Main.getPlugin().getConfig().getInt("Server.Areas." + areaid + ".lX");
			sZ = Main.getPlugin().getConfig().getInt("Server.Areas." + areaid + ".sZ");
			lZ = Main.getPlugin().getConfig().getInt("Server.Areas." + areaid + ".lZ");
			ownerType = OwnerType.fromString(Main.getPlugin().getConfig().getString("Server.Areas." + areaid + ".ownertype"));
			if(ownerType == OwnerType.FIRM || ownerType == OwnerType.IMMOBILIE) {
				ownFirm = Main.getPlugin().getConfig().getInt("Server.Areas." + areaid + ".own");
			}else if(ownerType == OwnerType.PRIVATE){
				owner = Main.getPlugin().getConfig().getString("Server.Areas." + areaid + ".own");
			}
			
		}
		return this;
	}
	
	public AreaInfo loadRequest(int areaid) {
		this.areaid = areaid;
		if(Main.getPlugin().getConfig().contains("Server.Requests." + areaid)) {
			sX = Main.getPlugin().getConfig().getInt("Server.Requests." + areaid + ".sX");
			lX = Main.getPlugin().getConfig().getInt("Server.Requests." + areaid + ".lX");
			sZ = Main.getPlugin().getConfig().getInt("Server.Requests." + areaid + ".sZ");
			lZ = Main.getPlugin().getConfig().getInt("Server.Requests." + areaid + ".lZ");
			ownerType = OwnerType.fromString(Main.getPlugin().getConfig().getString("Server.Requests." + areaid + ".ownertype"));
			if(ownerType == OwnerType.FIRM || ownerType == OwnerType.IMMOBILIE) {
				ownFirm = Main.getPlugin().getConfig().getInt("Server.Requests." + areaid + ".own");
			}else if(ownerType == OwnerType.PRIVATE){
				owner = Main.getPlugin().getConfig().getString("Server.Requests." + areaid + ".own");
			}
			isaccepted = Main.getPlugin().getConfig().getBoolean("Server.Requests." + areaid + ".isaccepted");
		}
		return this;
	}
	
	public AreaInfo addArea(int sX, int lX, int sZ, int lZ, OwnerType ownertype, String owner) {
		areaid = areaidn;
		areaidn++;
		this.ownerType = ownertype;
		this.owner = owner;
		this.sX = sX;
		this.lX = lX;
		this.sZ = sZ;
		this.lZ = lZ; 
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sX", sX);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lX", lX);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sZ", sZ);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lZ", lZ);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".own", owner);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".ownertype", ownertype.toString());
		Main.getPlugin().getConfig().set("Server.Areaids", areaidn);
		Main.getPlugin().saveConfig();
		return this;
	}
	
	public AreaInfo addArea(int sX,int lX,int sZ,int lZ, OwnerType ownertype, int ownFirm) {
		areaid = areaidn;
		areaidn++;
		this.ownerType = ownertype;
		this.ownFirm = ownFirm;
		this.sX = sX;
		this.lX = lX;
		this.sZ = sZ;
		this.lZ = lZ;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sX", sX);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lX", lX);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sZ", sZ);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lZ", lZ);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".own", ownFirm);
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".ownertype", ownertype.toString());
		Main.getPlugin().getConfig().set("Server.Areaids", areaidn);
		Main.getPlugin().saveConfig();
		return this;
	}
	
	public AreaInfo addRequest(int sX, int lX, int sZ, int lZ, OwnerType ownertype, String owner) {
		areaid = requestidn;
		requestidn++;
		this.ownerType = ownertype;
		this.owner = owner;
		this.sX = sX;
		this.lX = lX;
		this.sZ = sZ;
		this.lZ = lZ; 
		isaccepted = false;
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".sX", sX);
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".lX", lX);
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".sZ", sZ);
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".lZ", lZ);
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".own", owner);
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".ownertype", ownertype.toString());
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".isaccepted", isaccepted);
		Main.getPlugin().getConfig().set("Server.Requestids", requestidn);
		Main.getPlugin().saveConfig();
		return this;
	}
	
	public static boolean isOnArea(Location loc) {
		for(int i = 0; i < areaidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Areas." + i)) {
				AreaInfo ai = new AreaInfo().loadArea(i);
				if(loc.getX() <= ai.getlX()
					&& loc.getX() >= ai.getsX()
					&& loc.getZ() <= ai.getlZ()
					&& loc.getZ() >= ai.getsZ()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isonRequest(Location loc) {
		for(int i = 0; i < requestidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Requests." + i)) {
				AreaInfo ai = new AreaInfo().loadRequest(i);
				if(loc.getX() <= ai.getlX()
					&& loc.getX() >= ai.getsX()
					&& loc.getZ() <= ai.getlZ()
					&& loc.getZ() >= ai.getsZ()) {
					return true;
				}
			}
		}
		for(Player current : AreaRequest.selectedArea.keySet()) {
			List<Integer> selected = AreaRequest.selectedArea.get(current);
			if(loc.getX() <= selected.get(1)
				&& loc.getX() >= selected.get(0)
				&& loc.getZ() <= selected.get(3)
				&& loc.getY() >= selected.get(2)) {
				return true;
			}
		}
		return false;
	}
	
	public static AreaInfo getArea(Location loc) {
		for(int i = 0; i < areaidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Areas." + i)) {
				AreaInfo ai = new AreaInfo().loadArea(i);
				if(loc.getX() <= ai.getlX()
					&& loc.getX() >= ai.getsX()
					&& loc.getZ() <= ai.getlZ()
					&& loc.getZ() >= ai.getsZ()) {
					return ai;
				}
			}
		}
		return null;
	}
	
	public static AreaInfo getRequest(Location loc) {
		for(int i = 0; i < requestidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Requests." + i)) {
				AreaInfo ai = new AreaInfo().loadRequest(i);
				if(loc.getX() <= ai.getlX()
					&& loc.getX() >= ai.getsX()
					&& loc.getZ() <= ai.getlZ()
					&& loc.getZ() >= ai.getsZ()) {
					return ai;
				}
			}
		}
		return null;
	}
	
	public static boolean hasRequested(OfflinePlayer t) {
		for(int i = 0; i < requestidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Requests." + i)) {
				AreaInfo ai = new AreaInfo().loadRequest(i);
				if(ai.getOwner().equals(t.getUniqueId().toString())) return true;
			}
		}
		return false;
	}
	
	public static AreaInfo getRequest(OfflinePlayer t) {
		for(int i = 0; i < requestidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Requests." + i)) {
				AreaInfo ai = new AreaInfo().loadRequest(i);
				if(ai.getOwner().equals(t.getUniqueId().toString())) {
					return ai;
				}
			}
		}
		return null; 
	}
	
	public static List<AreaInfo> getRequests(){
		List<AreaInfo> requests = new ArrayList<AreaInfo>();
		for(int i = 0; i < requestidn; i++) {
			if(Main.getPlugin().getConfig().contains("Server.Requests." + i)) {
				AreaInfo ai = new AreaInfo().loadRequest(i);
				requests.add(ai); 
			}
		}
		return requests;
	}
	

	public int getAreaid() {
		return areaid;
	}

	public int getsX() {
		return sX;
	}

	public void setsX(int sX) {
		this.sX = sX;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sX", sX);
		Main.getPlugin().saveConfig();
	}

	public int getlX() {
		return lX;
	}

	public void setlX(int lX) {
		this.lX = lX;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lX", lX);
		Main.getPlugin().saveConfig();
	}

	public int getsZ() {
		return sZ;
	}

	public void setsZ(int sZ) {
		this.sZ = sZ;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".sZ", sZ);
		Main.getPlugin().saveConfig();
	}

	public int getlZ() {
		return lZ;
	}

	public void setlZ(int lZ) {
		this.lZ = lZ;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".lZ", lZ);
		Main.getPlugin().saveConfig();
	}

	public String getOwner() {
		return owner;
	}
	public int getOwnFirm() {
		return ownFirm;
	}

	public void setOwner(String owner) {
		this.owner = owner;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".own", owner);
		Main.getPlugin().saveConfig();
	}

	public void setOwner(int ownFirm) {
		this.ownFirm = ownFirm;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".own", ownFirm);
		Main.getPlugin().saveConfig();
	}

	public OwnerType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
	}

	public int getEnergyFirm() {
		return energyFirm;
	}

	public void setEnergyFirm(int energyFirm) {
		this.energyFirm = energyFirm;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".energy", energyFirm);
		Main.getPlugin().saveConfig();
	}

	public int getWaterFirm() {
		return waterFirm;
	}

	public void setWaterFirm(int waterFirm) {
		this.waterFirm = waterFirm;
		Main.getPlugin().getConfig().set("Server.Areas." + areaid + ".water", waterFirm);
		Main.getPlugin().saveConfig();
	}
	
	public boolean hasWaterFirm() {
		if(waterFirm != 0) return true;
		return false;
	}
	
	public boolean hasEnergyFirm() {
		if(energyFirm != 0) return true;
		return false;
	}
	
	public boolean isAccepted() {
		return isaccepted;
	}
	
	public void setAccepted(boolean isaccepted) {
		Main.getPlugin().getConfig().set("Server.Requests." + areaid + ".isaccepted", isaccepted);
		Main.getPlugin().saveConfig();
		
	}
	
	public void removeArea() {
		Main.getPlugin().getConfig().set("Server.Areas." + areaid, null);
		Main.getPlugin().saveConfig();
	}
	
	public void removeRequest() {
		Main.getPlugin().getConfig().set("Server.Requests." + areaid, null);
		Main.getPlugin().saveConfig();
	}
	
	
	
	
	
	
}
