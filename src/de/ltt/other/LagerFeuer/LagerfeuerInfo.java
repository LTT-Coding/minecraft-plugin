package de.ltt.other.LagerFeuer;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Lightable;
import org.bukkit.scheduler.BukkitRunnable;

import de.ltt.server.main.Main;

public class LagerfeuerInfo {

    public static HashMap<Integer, BukkitRunnable> fires = new HashMap<Integer, BukkitRunnable>();
	
	private static int idn = 0;
	final double duration = 7;
	private Location campfireLoc;
	private int wood;
	private int asche;
	private int id;
	
	public LagerfeuerInfo() {
		idn = Main.getPlugin().getLagerfeuerConfig().getInt("Lagerfeuer.ids");
	}
	
	public LagerfeuerInfo loadFeuer(int id) {
		this.id = id;
		if(Main.getPlugin().getLagerfeuerConfig().contains("Lagerfeuer." + id)) {
			double X = Main.getPlugin().getLagerfeuerConfig().getDouble("Lagerfeuer." + id + ".x");
			double Y = Main.getPlugin().getLagerfeuerConfig().getDouble("Lagerfeuer." + id + ".y");
			double Z = Main.getPlugin().getLagerfeuerConfig().getDouble("Lagerfeuer." + id + ".z");
			String World = Main.getPlugin().getLagerfeuerConfig().getString("Lagerfeuer." + id + ".world");
			campfireLoc = new Location(Bukkit.getWorld(World), X, Y, Z);
		    wood = Main.getPlugin().getLagerfeuerConfig().getInt("Lagerfeuer." + id + ".wood");
		    asche = Main.getPlugin().getLagerfeuerConfig().getInt("Lagerfeuer." + id + ".asche");
			return this;
		}
        return null;
		
	}
	
	public LagerfeuerInfo addFeuer(Location campfireLoc) {
		this.id = idn;
		idn++;
		wood = 0;
		asche = 0;
		this.campfireLoc = campfireLoc;
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".x", campfireLoc.getX());
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".y", campfireLoc.getY());
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".z", campfireLoc.getZ());
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".world", campfireLoc.getWorld().getName());
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".wood", wood);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".asche", asche);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer.ids", idn);
		Main.getPlugin().saveLagerfeuerConfig();
		return this;
	}
	
	public void addHolz() {
		wood++;
        Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".wood", wood);
        Main.getPlugin().saveLagerfeuerConfig();
	}
    public void removeHolz(){
       wood--;
        Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".wood", wood);
        Main.getPlugin().saveLagerfeuerConfig();
    }

    public int getHolz(){
        return wood;
    }
    
    public void addAsche() {
    	Random r = new Random();
		int Zufall = r.nextInt(2);
		Zufall++;
        Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".asche", Zufall + asche);
        Main.getPlugin().saveLagerfeuerConfig();
	}
    
    public void setAsche(int asche){
        this.asche = asche;
        Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".asche", asche);
        Main.getPlugin().saveLagerfeuerConfig();
    }

    public int getAsche(){
        return asche;
    }

    public Location getLocation(){
        return campfireLoc;
    }

    public void runFeuer(){
       // Lightable block = (Lightable) campfireLoc.getBlock().getBlockData();
       // block.setLit(true);
    	LagerfeuerInfo.fires.put(id, new BukkitRunnable() {
            
            @Override
            public void run() {
                removeHolz();
                addAsche();
                if(getHolz() == 0) {
                	stopFeuer();
                }
             
            }
        });
        LagerfeuerInfo.fires.get(id).runTaskTimer(Main.getPlugin(),(long) duration*20*60, (long) duration*20*60);
    }

    public void stopFeuer(){
    	try {
    		Lightable block = (Lightable) campfireLoc.getBlock().getBlockData();
            block.setLit(false);
            campfireLoc.getBlock().setBlockData(block);
            campfireLoc.getBlock().getState().update();
           if(LagerfeuerInfo.fires.containsKey(id)){
        	   fires.get(id).cancel();
        	   LagerfeuerInfo.fires.remove(id);
           }
		} catch (Exception e) {
			System.out.print("[Lagerfeuer]Fehler: Location X: " + campfireLoc.getX() + " Y: " + campfireLoc.getY() + " Z: " + campfireLoc.getZ() + " Welt: " + campfireLoc.getWorld());
		}
    }

    public static void removeFires(){
        for(int i = 0; i < idn; i++){
            if(Main.getPlugin().getLagerfeuerConfig().contains("Lagerfeuer." + i)){
               new LagerfeuerInfo().loadFeuer(i).stopFeuer();
            }
        }
    }

    public static LagerfeuerInfo getFireByLoc(Location campfireLoc){
        for(int i = 0; i < idn; i++){
            if(Main.getPlugin().getLagerfeuerConfig().contains("Lagerfeuer." + i)){
               LagerfeuerInfo li = new LagerfeuerInfo().loadFeuer(i);
               if(li.getLocation().equals(campfireLoc)){
                   return li;
               }
            }
        }
        return null;
    }
    
    public void removeFire() {
    	Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".x", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".y", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".z", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".world", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".wood", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id + ".asche", null);
		Main.getPlugin().getLagerfeuerConfig().set("Lagerfeuer." + id, null);
		Main.getPlugin().saveLagerfeuerConfig();
    }

	public int getId() {
		return id;
	}
    
    
}
