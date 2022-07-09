package de.ltt.server.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.ltt.FakePlayer.NPC;
import de.ltt.other.KopfInv.KopfInv;
import de.ltt.other.ModelInv.ModelInv;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.türSysteme.DirectionType;

public class SQLData {
	
	public static List<String> getAdmin(){
		List<String> admin = new ArrayList<String>();
		Main.mysql.update("create table if not exists Admins(UUID varchar(64));");
		try {
			ResultSet rs = Main.mysql.query("select UUID from Admins;");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	               admin.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§aAdmins wurden geladen!");
		return admin;
	}
	
	public static void addAdmin(String uuid) {
		if(!Main.Admin.contains(uuid)) {
			Main.mysql.update("insert into Admins (UUID) values ('" + uuid + "');");
			Main.Admin.add(uuid);
			addMod(uuid);
			addSupp(uuid);
		}
	}
	
	public static void removeAdmin(String uuid) {
		if(Main.Admin.contains(uuid)) {
			Main.mysql.update("delete from Admins where UUID = '" + uuid + "';");
			Main.Admin.remove(uuid);
			removeMod(uuid);
			removeSupp(uuid);
		}
	}
	
	public static List<String> getMods(){
		List<String> mods = new ArrayList<String>();
		Main.mysql.update("create table if not exists Moderatoren(UUID varchar(64));");
		try {
			ResultSet rs = Main.mysql.query("select UUID from Moderatoren;");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	               mods.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§aModeratoren wurden geladen!");
		return mods;
	}
	
	public static void addMod(String uuid) {
		if(!Main.Moderatoren.contains(uuid)) {
			Main.mysql.update("insert into Moderatoren (UUID) values ('" + uuid + "');");
			Main.Moderatoren.add(uuid);
			addSupp(uuid);
		}
	}
	
	public static void removeMod(String uuid) {
		if(Main.Moderatoren.contains(uuid)) {
			Main.mysql.update("delete from Moderatoren where UUID = '" + uuid + "';");
			Main.Moderatoren.remove(uuid);
			removeAdmin(uuid);
			removeSupp(uuid);
		}
	}
	
	public static List<String> getSupps(){
		List<String> supps = new ArrayList<String>();
		Main.mysql.update("create table if not exists Supporter(UUID varchar(64));");
		
		try {
			ResultSet rs = Main.mysql.query("select UUID from Supporter;");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	               supps.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Supporter wurden geladen!");
		return supps;
	}
	
	public static void addSupp(String uuid) {
		if(!Main.Supporter.contains(uuid)) {
			Main.mysql.update("insert into Supporter (UUID) values ('" + uuid + "');");
			Main.Supporter.add(uuid);
		}
	}
	
	public static void removeSupp(String uuid) {
		if(Main.Supporter.contains(uuid)) {
			Main.mysql.update("delete from Supporter where UUID = '" + uuid + "';");
			Main.Supporter.remove(uuid);
			removeAdmin(uuid);
			removeMod(uuid);
		}
	}
	
	public static List<String> getVanish(){
		List<String> Vanish = new ArrayList<String>();
		Main.mysql.update("create table if not exists Vanish(UUID varchar(64));");
		
		try {
			ResultSet rs = Main.mysql.query("select UUID from Vanish;");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	Vanish.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Vanish wurden geladen!");
		return Vanish;
	}
	
	public static void addVanish(String uuid) {
		if(!Main.vanish.contains(uuid)) {
			Main.mysql.update("insert into Vanish (UUID) values ('" + uuid + "');");
			Main.vanish.add(uuid);
		}
	}
	
	public static void removeVanish(String uuid) {
		if(Main.vanish.contains(uuid)) {
			Main.mysql.update("delete from Vanish where UUID = '" + uuid + "';");
			Main.vanish.remove(uuid);
		}
	}
	
	public static List<String> getTester(){
		List<String> tester = new ArrayList<String>();
		Main.mysql.update("create table if not exists Betatester(UUID varchar(64));");
		try {
			ResultSet rs = Main.mysql.query("select UUID from Betatester");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	               tester.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Betatester wurden geladen!");
		return tester;
	}
	
	public static void addTester(String uuid) {
		if(!Main.BetaT.contains(uuid)) {
			Main.mysql.update("insert into Betatester (UUID) values ('" + uuid + "');");
			Main.BetaT.add(uuid);
		}
	}
	
	public static void removeTester(String uuid) {
		if(Main.BetaT.contains(uuid)) {
			Main.mysql.update("delete from Betatester where UUID = '" + uuid + "';");
			Main.BetaT.remove(uuid);
		}
	}
	
	public static List<String> getHashTags(){
		List<String> hashtags = new ArrayList<String>();
		Main.mysql.update("create table if not exists HashTags(HashTag varchar(200));");
		try {
			ResultSet rs = Main.mysql.query("select HashTag from HashTags");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	               hashtags.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "HashTags wurden geladen!");
		return hashtags;
	}
	
	public static void addHashTag(String hashtag) {
		if(!Main.hashTag.contains(hashtag)) {
			Main.mysql.update("insert into HashTags (HashTag) values ('" + hashtag + "');");
			Main.hashTag.add(hashtag);
		}
	}
	
	public static void removeHashTag(String hashtag) {
		if(Main.hashTag.contains(hashtag)) {
			Main.mysql.update("delete from HashTags where HashTag = '" + hashtag + "';");
			Main.hashTag.remove(hashtag);
		}
	}
	
	public static List<String> getBugs(){
		List<String> bugs = new ArrayList<String>();
		Main.mysql.update("create table if not exists Bugs(Bug varchar(1000));");
		try {
			ResultSet rs = Main.mysql.query("select Bug from Bugs");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	bugs.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Bugs wurden geladen!");
		return bugs;
	}
	
	public static void addBug(String bug) {
		if(!Main.BugsTodo.contains(bug)) {
			Main.mysql.update("insert into Bugs (Bug) values ('" + bug + "');");
			Main.BugsTodo.add(bug);
		}
	}
	
	public static void removeBug(String bug) {
		if(Main.BugsTodo.contains(bug)) {
			Main.mysql.update("delete from Bugs where Bug = '" + bug + "';");
			Main.BugsTodo.remove(bug);
		}
	}
	
	public static List<Location> getMedicDoors() {
		List<Location> medicdoors = new ArrayList<Location>();
		Main.mysql.update("create table if not exists MedicDoors(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from MedicDoors;");
			ResultSet y = Main.mysql.query("select y from MedicDoors;");
			ResultSet z = Main.mysql.query("select z from MedicDoors;");
			ResultSet world = Main.mysql.query("select world from MedicDoors;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	               medicdoors.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Medictueren wurden geladen!");
		return medicdoors;
	}
	
	public static void addMedicDoor(Location loc) {
		if(!Main.MedicTürLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into MedicDoors(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.MedicTürLoc.add(loc);
		}
	}
	
	public static void removeMedicDoor(Location loc) {
		if(Main.MedicTürLoc.contains(loc)) {
			Main.mysql.update("delete from MedicDoors where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld().getName() + "';");
			Main.MedicTürLoc.remove(loc);
		}
	}
	
	public static List<Location> getGeheimDoors() {
		List<Location> Geheimdoors = new ArrayList<Location>();
		Main.mysql.update("create table if not exists GeheimDoor(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from GeheimDoor;");
			ResultSet y = Main.mysql.query("select y from GeheimDoor;");
			ResultSet z = Main.mysql.query("select z from GeheimDoor;");
			ResultSet world = Main.mysql.query("select world from GeheimDoor;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	Geheimdoors.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "GeheimDoor wurden geladen!");
		return Geheimdoors;
	}
	
	public static void addGeheimDoor(Location loc) {
		if(!Main.GeheimTürLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into GeheimDoor(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.GeheimTürLoc.add(loc);
		}
	}
	
	public static void removeGeheimDoor(Location loc) {
		if(Main.GeheimTürLoc.contains(loc)) {
			Main.mysql.update("delete from GeheimDoor where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld().getName() + "';");
			Main.GeheimTürLoc.remove(loc);
		}
	}
	
	public static List<Location> getPolizeiDoors() {
		List<Location> Polizeidoors = new ArrayList<Location>();
		Main.mysql.update("create table if not exists PolizeiDoors(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from PolizeiDoors;");
			ResultSet y = Main.mysql.query("select y from PolizeiDoors;");
			ResultSet z = Main.mysql.query("select z from PolizeiDoors;");
			ResultSet world = Main.mysql.query("select world from PolizeiDoors;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	Polizeidoors.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Polizeitueren wurden geladen!");
		return Polizeidoors;
	}
	
	public static void addPolizeiDoor(Location loc) {
		if(!Main.PolizeiTürLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into PolizeiDoors(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.PolizeiTürLoc.add(loc);
		}
	}
	
	public static void removePolizeiDoor(Location loc) {
		if(Main.PolizeiTürLoc.contains(loc)) {
			Main.mysql.update("delete from PolizeiDoors where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld().getName() + "';");
			Main.PolizeiTürLoc.remove(loc);
		}
	}
	
	public static HashMap<Integer, Integer> getAreaPrices(){
		HashMap<Integer, Integer> areaPrices = new HashMap<Integer, Integer>();
		Main.mysql.update("create table if not exists AreaPrices(AreaId int, Price int);");
		try {
			ResultSet ids = Main.mysql.query("select AreaId from AreaPrices;");
			ResultSet prices = Main.mysql.query("select Price from AreaPrices;");
			while(ids.next() && prices.next()) {
				int columns = ids.getMetaData().getColumnCount();
				for(int i = 1; i <= columns; i++) {
					areaPrices.put(ids.getInt(i), prices.getInt(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return areaPrices;
	}
	
	public static void addAreaPrice(int id, int price) {
		if(!Main.areaPriceMap.containsKey(id)) {
			Main.areaPriceMap.put(id, price);
			Main.mysql.update("insert into AreaPrices(AreaId, Price) values (" + id + ", " + price + ");");
		}
	}
	
	public static void removeAreaPrice(int id) {
		if(Main.areaPriceMap.containsKey(id)) {
			Main.areaPriceMap.remove(id);
			Main.mysql.update("delete from AreaPrices where AreaId = " + id + ";");
		}
	}
	
	
	public static List<Location> getBlockSperre() {
		List<Location> blocksperre = new ArrayList<Location>();
		Main.mysql.update("create table if not exists BlockSperre(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from BlockSperre;");
			ResultSet y = Main.mysql.query("select y from BlockSperre;");
			ResultSet z = Main.mysql.query("select z from BlockSperre;");
			ResultSet world = Main.mysql.query("select world from BlockSperre;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	blocksperre.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Blocksperren wurden geladen!");
		return blocksperre;
	}
	
	public static void addBlockSperre(Location loc) {
		if(!Main.BlockSperreLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into BlockSperre(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.BlockSperreLoc.add(loc);
		}
	}
	
	public static void removeBlockSperre(Location loc) {
		if(Main.BlockSperreLoc.contains(loc)) {
			Main.mysql.update("delete from BlockSperre where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld() + "';");
			Main.BlockSperreLoc.remove(loc);
		}
	}
	
	public static void getNummer() {
		Main.mysql.update("create table if not exists Nummern(Player varchar(64), Nummer int);");
		
		try {
			ResultSet player = Main.mysql.query("select Player from Nummern;");
			ResultSet nummer = Main.mysql.query("select Nummer from Nummern;");
			int columns = player.getMetaData().getColumnCount();
			while (player.next() && nummer.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	Main.NummerPlayer.put(nummer.getInt(i), player.getString(i));
	            	Main.PlayerNummer.put(player.getString(i), nummer.getInt(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Nummern wurden geladen!");
	}
	
	public static void addNummer(String uuid, int nummer) {
		if(!Main.PlayerNummer.containsKey(uuid)) {
			Main.mysql.update("insert into Nummern(Player, Nummer) values ('" + uuid + "', " + nummer + ");");
			Main.NummerPlayer.put(nummer, uuid);
			Main.PlayerNummer.put(uuid, nummer);
		}
	}
	
	public static void removeNummer(String uuid) {
		if(Main.PlayerNummer.containsKey(uuid)) {
			Main.NummerPlayer.remove(Main.PlayerNummer.get(uuid));
			Main.PlayerNummer.remove(uuid);
			Main.mysql.update("delete from Nummern where Player = '" + uuid + "';");
		}
	}
	
	public static void removeNummer(int nummer) {
		if(Main.NummerPlayer.containsKey(nummer)) {
			Main.PlayerNummer.remove(Main.NummerPlayer.get(nummer));
			Main.NummerPlayer.remove(nummer);
			Main.mysql.update("delete from Nummern where Nummer = " + nummer + ";");
		}
	}
	
	public static List<String> getBlockedCMD() {
		List<String> BlockedCMD = new ArrayList<String>();
		Main.mysql.update("create table if not exists CommandSperre(Command varchar(64));");
		try {
			ResultSet commands = Main.mysql.query("select Command from CommandSperre;");
			int columns = commands.getMetaData().getColumnCount();
			while (commands.next()) {
				for (int i = 1; i <= columns; i++) {
					BlockedCMD.add(commands.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "gesperrte Befehle wurden geladen!");
		return BlockedCMD;
	}
	
	public static void addBlockedCMD(String CMD) {
		if(!Main.GesperrteCMD.contains(CMD)) {
			Main.mysql.update("insert into CommandSperre(Command) values ('" + CMD + "');");
			Main.GesperrteCMD.add(CMD);
		}
	}
	
	public static void removeBlockedCMD(String CMD) {
		if(Main.GesperrteCMD.contains(CMD)) {
			Main.mysql.update("delete from CommandSperre where Command = '" + CMD + "';");
			Main.GesperrteCMD.remove(CMD);
		}
	}
	
	public static List<String> getSearch() {
		List<String> SearchList = new ArrayList<String>();
		Main.mysql.update("create table if not exists Search(Begriff varchar(64));");
		try {
			ResultSet Begriff = Main.mysql.query("select Begriff from Search;");
			int columns = Begriff.getMetaData().getColumnCount();
			while (Begriff.next()) {
				for (int i = 1; i <= columns; i++) {
					SearchList.add(Begriff.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Suchbegriffe wurden geladen!");
		return SearchList;
	}
	
	public static void addSearch(String Search) {
		if(!Main.KopfSearch.contains(Search)) {
			Main.mysql.update("insert into Search(Begriff) values ('" + Search + "');");
			Main.KopfSearch.add(Search);
		}
	}
	
	public static void removeSearch(String Search) {
		if(Main.KopfSearch.contains(Search)) {
			Main.mysql.update("delete from Search where Begriff = '" + Search + "';");
			Main.KopfSearch.remove(Search);
		}
	}
	
	public static List<String> getModelSearch() {
		List<String> SearchList = new ArrayList<String>();
		Main.mysql.update("create table if not exists ModelSearch(Begriff varchar(64));");
		try {
			ResultSet Begriff = Main.mysql.query("select Begriff from ModelSearch;");
			int columns = Begriff.getMetaData().getColumnCount();
			while (Begriff.next()) {
				for (int i = 1; i <= columns; i++) {
					SearchList.add(Begriff.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Model Suchbegriffe wurden geladen!");
		return SearchList;
	}
	
	public static void addModelSearch(String Search) {
		if(!Main.ModelSearch.contains(Search)) {
			Main.mysql.update("insert into ModelSearch(Begriff) values ('" + Search + "');");
			Main.ModelSearch.add(Search);
		}
	}
	
	public static void removeModelSearch(String Search) {
		if(Main.ModelSearch.contains(Search)) {
			Main.mysql.update("delete from ModelSearch where Begriff = '" + Search + "';");
			Main.ModelSearch.remove(Search);
		}
	}
	
	public static List<String> getSinnlosefakten() {
		List<String> SinnlosefaktenList = new ArrayList<String>();
		Main.mysql.update("create table if not exists Sinnlosefakten(Fakt varchar(500));");
		try {
			ResultSet Fakt = Main.mysql.query("select Fakt from Sinnlosefakten;");
			int columns = Fakt.getMetaData().getColumnCount();
			while (Fakt.next()) {
				for (int i = 1; i <= columns; i++) {
					SinnlosefaktenList.add(Fakt.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Sinnlosefakten wurden geladen!");
		return SinnlosefaktenList;
	}
	
	public static void addSinnlosefakten(String Sinnlosefakten) {
		if(!Main.SinnloseFakten.contains(Sinnlosefakten)) {
			Main.mysql.update("insert into Sinnlosefakten(Fakt) values ('" + Sinnlosefakten + "');");
			Main.SinnloseFakten.add(Sinnlosefakten);
		}
	}
	
	public static void removeSinnlosefakten(String Sinnlosefakten) {
		if(Main.SinnloseFakten.contains(Sinnlosefakten)) {
			Main.mysql.update("delete from Sinnlosefakten where Fakt = '" + Sinnlosefakten + "';");
			Main.SinnloseFakten.remove(Sinnlosefakten);
		}
	}
	
	
	public static HashMap<String, String> getKöpfeMap(){
		HashMap<String, String> köpfe = new HashMap<String, String>();
		Main.mysql.update("create table if not exists KopfInv(Name varchar(100), Kopf varchar(200));");
		try {
			ResultSet name = Main.mysql.query("select Name from KopfInv;");
			ResultSet kopf = Main.mysql.query("select Kopf from KopfInv;");
			int columns = name.getMetaData().getColumnCount();
			while(name.next() && kopf.next()) {
				for(int i = 1; i <= columns; i++) {
					köpfe.put(kopf.getString(i), name.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return köpfe;
	}
	
	public static List<String> getKöpfeList(){
		List<String> köpfe = new ArrayList<String>();
		Main.mysql.update("create table if not exists KopfInv(Name varchar(100), Kopf varchar(200));");
		try {
			ResultSet kopf = Main.mysql.query("select Kopf from KopfInv;");
			int columns = kopf.getMetaData().getColumnCount();
			while(kopf.next()) {
				for(int i = 1; i <= columns; i++) {
					köpfe.add(kopf.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return köpfe;
		
	}
	
	
	public static void addKopf(String name, String url) {
		if(!Main.KopfInv.contains(url)) {
			Main.KopfInv.add(url);
			KopfInv.kopfName.put(url, name);
			Main.mysql.update("insert into KopfInv (Name, Kopf) values ('" + name + "', '" + url + "');");
			
		}
	}
	
	public static void removeKopf(String url) {
		if(Main.KopfInv.contains(url)) {
			Main.KopfInv.remove(url);
			KopfInv.kopfName.remove(url);
			Main.mysql.update("delete from KopfInv where Kopf = '" + url + "';");
		}
	}
	
	public static HashMap<String, String> getModelMap(){
		HashMap<String, String> Models = new HashMap<String, String>();
		Main.mysql.update("create table if not exists ModelInv(Name varchar(100), Model varchar(200));");
		try {
			ResultSet name = Main.mysql.query("select Name from ModelInv;");
			ResultSet Model = Main.mysql.query("select Model from ModelInv;");
			int columns = name.getMetaData().getColumnCount();
			while(name.next() && Model.next()) {
				for(int i = 1; i <= columns; i++) {
					Models.put(Model.getString(i), name.getString(i));
				}
			}
		} catch (Exception e) {
		}
		return Models;
	}
	
	public static List<String> getModelList(){
		List<String> Models = new ArrayList<String>();
		Main.mysql.update("create table if not exists ModelInv(Name varchar(100), Model varchar(200));");
		try {
			ResultSet Model = Main.mysql.query("select Model from ModelInv;");
			int columns = Model.getMetaData().getColumnCount();
			while(Model.next()) {
				for(int i = 1; i <= columns; i++) {
					Models.add(Model.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return Models;
		
	}
	
	
	public static void addModel(String name, String Model) {
		if(!Main.ModelInv.contains(Model)) {
			Main.ModelInv.add(Model);
			ModelInv.ModelName.put(Model, name);
			Main.mysql.update("insert into ModelInv (Name, Model) values ('" + name + "', '" + Model + "');");
			
		}
	}
	
	public static void removeModel(String ModelID) {
		if(Main.ModelInv.contains(ModelID)) {
			Main.ModelInv.remove(ModelID);
			ModelInv.ModelName.remove(ModelID);
			Main.mysql.update("delete from ModelInv where Model = '" + ModelID + "';");
		}
	}
	
	public static List<String> getBlockedURL(){
		List<String> blocked = new ArrayList<String>();
		Main.mysql.update("create table if not exists BlockedURL(URL varchar(64));");
		try {
			ResultSet rs = Main.mysql.query("select URL from BlockedURL;");
			int columns = rs.getMetaData().getColumnCount();
			while(rs.next()) {
				for(int i = 1; i <= columns; i++) {
					blocked.add(rs.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return blocked;
	}
	
	public static HashMap<String, String> getPMotd(){
		HashMap<String, String> IP = new HashMap<String, String>();
		Main.mysql.update("create table if not exists PlayerMotd(IP varchar(64), UUID varchar(100));");
		try {
			ResultSet ip = Main.mysql.query("select IP from PlayerMotd;");
			ResultSet id = Main.mysql.query("select UUID from PlayerMotd;");
			int columns = ip.getMetaData().getColumnCount();
			while(ip.next() && id.next()) {
				for(int i = 1; i <= columns; i++) {
					IP.put(ip.getString(i), id.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "PlayerMotd wurden geladen!");
		return IP;
	}
	
	public static void setPMotd(String IP, String ID) {
		if(!Main.pMotd.containsKey(IP)) {
			Main.pMotd.put(IP, ID);
			Main.mysql.update("insert into PlayerMotd (IP, UUID) values ('" + IP + "', '" + ID + "');");
		}
	}
	
	public static HashMap<UUID, Boolean> getHandyOnOff(){
		HashMap<UUID, Boolean> HandyOnOff = new HashMap<UUID, Boolean>();
		Main.mysql.update("create table if not exists HandyOnOff(UUID varchar(100), Boolean varchar(6));");
		try {
			ResultSet UUID2 = Main.mysql.query("select UUID from HandyOnOff;");
			ResultSet Boolean = Main.mysql.query("select Boolean from HandyOnOff;");
			int columns = UUID2.getMetaData().getColumnCount();
			while(UUID2.next() && Boolean.next()) {
				for(int i = 1; i <= columns; i++) {
					HandyOnOff.put(UUID.fromString(UUID2.getString(i)), Boolean.getBoolean(i));
				}
			}
		} catch (SQLException e) {
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "HandyOnOff wurde geladen!");
		return HandyOnOff;
	}
	
	public static void setHandyOnOff(UUID UUID, Boolean status) {
		Main.handyOnOff.put(UUID, status);		
		Main.mysql.update("delete from HandyOnOff where UUID = '" + UUID + "';");
		Main.mysql.update("insert into HandyOnOff (UUID, Boolean) values ('" + UUID + "', '" + status + "');");
	}
	
	public static List<String> getWartungsPlayer(){
		List<String> WartungsPlayer = new ArrayList<String>();
		Main.mysql.update("create table if not exists Wartung(Name varchar(64));");
		try {
			ResultSet name = Main.mysql.query("select Name from Wartung;");
			int columns = name.getMetaData().getColumnCount();
			while(name.next()) {
				for(int i = 1; i <= columns; i++) {
					WartungsPlayer.add(name.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return WartungsPlayer;
		
	}
	
	
	public static void addWartungsPlayer(String name) {
		if(!Main.AddWartungPlayer.contains(name)) {
			Main.AddWartungPlayer.add(name);
			Main.mysql.update("insert into Wartung (Name) values ('" + name + "');");
			
		}
	}
	
	public static void removeWartungsPlayer(String name) {
		if(Main.AddWartungPlayer.contains(name)) {
			Main.AddWartungPlayer.remove(name);
			Main.mysql.update("delete from Wartung where Name = '" + name + "';");
		}
	}
	
	public static List<String> getHolo(){
		List<String> Holo = new ArrayList<String>();
		Main.mysql.update("create table if not exists Hologram(ID varchar(100));");
		try {
			ResultSet id = Main.mysql.query("select ID from Hologram;");
			int columns = id.getMetaData().getColumnCount();
			while(id.next()) {
				for(int i = 1; i <= columns; i++) {
					Holo.add(id.getString(i));
				}
			}
		} catch (SQLException e) {
		}
		return Holo;
		
	}
	
	
	public static void addHolo(String ID) {
		if(!Main.holo.contains(ID)) {
			Main.holo.add(ID);
			Main.mysql.update("insert into Hologram (ID) values ('" + ID + "');");
			
		}
	}
	
	public static void removeHolo(String ID) {
		if(Main.holo.contains(ID)) {
			Main.holo.remove(ID);
			Main.mysql.update("delete from Hologram where ID = '" + ID + "';");
		}
	}
	
	public static void removeAllHolo() {
		    Main.holo.clear();
			Main.mysql.update("delete from Hologram;");
	}
	
	
	public static void getNaviPoint() {
		Main.mysql.update("create table if not exists NaviPoints(Name varchar(100), World varchar(64), X double, Y double, Z double, Material varchar(64), Klicks long);");
		try {
			ResultSet name = Main.mysql.query("select Name from NaviPoints;");
			ResultSet world = Main.mysql.query("select World from NaviPoints;");
			ResultSet x = Main.mysql.query("select X from NaviPoints;");
			ResultSet y = Main.mysql.query("select Y from NaviPoints;");
			ResultSet z = Main.mysql.query("select Z from NaviPoints;");
			ResultSet material = Main.mysql.query("select Material from NaviPoints;");
			ResultSet klick = Main.mysql.query("select Klicks from NaviPoints;");
			int columns = name.getMetaData().getColumnCount();
			while(name.next() && world.next() && x.next() && y.next() && z.next() && material.next() && klick.next()) {
				for(int i = 1; i <= columns; i++) {
					Main.naviPoints.put(name.getString(i), new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
					Main.pointMaterial.put(name.getString(i), Material.getMaterial(material.getString(i)));
					Main.navigationsPunkte.add(name.getString(i));
					Main.pointKlicks.put(name.getString(i), klick.getLong(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addNaviPoint(Location loc, String name, Material material) {
		if(!Main.naviPoints.containsKey(name)) {
			Main.naviPoints.put(name, loc);
			Main.pointMaterial.put(name, material);
			Main.navigationsPunkte.add(name);
			Main.pointKlicks.put(name, (long) 0);
			Main.mysql.update("insert into NaviPoints(Name, World, X, Y, Z, Material, Klicks) values ('" + name + "', '" + loc.getWorld().getName() + "', " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", '" + material.toString() + "', 0);");
		}
	}
	
	public static void removeNaviPoint(String name) {
		if(Main.naviPoints.containsKey(name)) {
			Main.naviPoints.remove(name);
			Main.pointMaterial.remove(name);
			Main.navigationsPunkte.remove(name);
			Main.pointKlicks.remove(name);
			Main.mysql.update("delete from NaviPoints where Name = '" + name + "';");
		}
	}
	
	public static void addNaviKlick(String name) {
		if(Main.pointKlicks.containsKey(name)) {
			long klicks = Main.pointKlicks.get(name) + 1;
			Main.pointKlicks.put(name, klicks);
			Main.mysql.update("update NaviPoints set Klicks = " + klicks + " where Name = '" + name + "';");
		}else {
			Main.pointKlicks.put(name, (long) 1);
			Main.mysql.update("update NaviPoints set Klicks = 1 where Name = '" + name + "';");
		}
	}
	
	public static HashMap<String, Integer> getJoinPlayer(){
		HashMap<String, Integer> JoinPlayer = new HashMap<String, Integer>();
		Main.mysql.update("create table if not exists JoinPlayer(UUID varchar(100), Joined int);");
		try {
			ResultSet uuid = Main.mysql.query("select UUID from JoinPlayer;");
			ResultSet join = Main.mysql.query("select Joined from JoinPlayer;");
			int columns = uuid.getMetaData().getColumnCount();
			while(uuid.next() && join.next()) {
				for(int i = 1; i <= columns; i++) {
					JoinPlayer.put(uuid.getString(i), join.getInt(i));
				}
			}
		} catch (SQLException e) {
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "JoinPlayer wurde geladen!");
		return JoinPlayer;
	}
	
	public static void removeJoinPlayer(String UUID) {
		if(Main.JoinPlayerZahl.containsKey(UUID)) {
			Main.JoinPlayerZahl.remove(UUID);
			Main.mysql.update("delete from JoinPlayer where UUID = '" + UUID + "';");
		}
	}
	
	public static void removeAllJoinPlayer() {
	    Main.JoinPlayerZahl.clear();
		Main.mysql.update("delete from JoinPlayer;");
	}
	
	public static void addJoinPlayer(String UUID) {
		if(Main.JoinPlayerZahl.containsKey(UUID)) {
			int JoinVersuche = Main.JoinPlayerZahl.get(UUID) + 1;
			Main.JoinPlayerZahl.remove(UUID);
			Main.JoinPlayerZahl.put(UUID, JoinVersuche);
			Main.mysql.update("update JoinPlayer set Joined = " + JoinVersuche + " where UUID = '" + UUID + "';");
		}else {
			Main.JoinPlayerZahl.put(UUID, (int) 1);
			Main.mysql.update("insert into JoinPlayer(UUID, Joined) values ('" + UUID +"', 1);");
		}
	}
	
	
	public static HashMap<String, Double> getCookieClicker(){
		HashMap<String, Double> JoinPlayer = new HashMap<String, Double>();
		Main.mysql.update("create table if not exists CookieClicker(UUID varchar(100), Klicker double);");
		try {
			ResultSet uuid = Main.mysql.query("select UUID from CookieClicker;");
			ResultSet Klicker = Main.mysql.query("select Klicker from CookieClicker;");
			int columns = uuid.getMetaData().getColumnCount();
			while(uuid.next() && Klicker.next()) {
				for(int i = 1; i <= columns; i++) {
					JoinPlayer.put(uuid.getString(i), Klicker.getDouble(i));
				}
			}
		} catch (SQLException e) {
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "CookieClicker wurde geladen!");
		return JoinPlayer;
	}
	
	public static void removeCookieClicker(String UUID) {
		if(Main.CookieClick.containsKey(UUID)) {
			Main.CookieClick.remove(UUID);
			Main.mysql.update("delete from CookieClicker where UUID = '" + UUID + "';");
		}
	}
	
	public static void addCookieClicker(String UUID) {
		if(Main.CookieClick.containsKey(UUID)) {
			double Klick = Main.CookieClick.get(UUID) + 1;
			Main.CookieClick.put(UUID, Klick);
			Main.mysql.update("update CookieClicker set Klicker = " + Klick + " where UUID = '" + UUID + "';");
		}else {
			Main.CookieClick.put(UUID, (double) 1);
			Main.mysql.update("insert into CookieClicker(UUID, Klicker) values ('" + UUID +"', 1);");
		}
	}
	
	public static void getPrisonDoors() {
		Main.mysql.update("create table if not exists PrisonDoor(world varchar(64), tX double, tY double, tZ double, lXZ double, sXZ double, lY double, sY double, XZ double, Type varchar(1));");
		try {
			ResultSet world = Main.mysql.query("select world from PrisonDoor;");
			ResultSet tX = Main.mysql.query("select tX from PrisonDoor;");
			ResultSet tY = Main.mysql.query("select tY from PrisonDoor;");
			ResultSet tZ = Main.mysql.query("select tZ from PrisonDoor;");
			ResultSet lXZ = Main.mysql.query("select lXZ from PrisonDoor;");
			ResultSet sXZ = Main.mysql.query("select sXZ from PrisonDoor;");
			ResultSet lY = Main.mysql.query("select lY from PrisonDoor;");
			ResultSet sY = Main.mysql.query("select sY from PrisonDoor;");
			ResultSet XZ = Main.mysql.query("select XZ from PrisonDoor;");
			ResultSet type = Main.mysql.query("select Type from PrisonDoor;");
			int coulumns = world.getMetaData().getColumnCount();
			while(world.next() && tX.next() && tY.next() && tZ.next() && lXZ.next() && sXZ.next() && lY.next() && sY.next() && XZ.next() && type.next()) {
				for(int i = 1; i<= coulumns; i++) {
					List<Double> doubles = new ArrayList<Double>();
					doubles.add(lXZ.getDouble(i));
					doubles.add(sXZ.getDouble(i));
					doubles.add(lY.getDouble(i));
					doubles.add(sY.getDouble(i));
					doubles.add(XZ.getDouble(i));
					Location loc = new Location(Bukkit.getWorld(world.getString(i)), tX.getDouble(i), tY.getDouble(i), tZ.getDouble(i));
					Main.triggerMap.put(loc, doubles);
					Main.directionMap.put(loc, type.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPrisonDoor(Location trigger, DirectionType type, double lXZ, double sXZ, double lY, double sY, double XZ) {
		List<Double> doubles = new ArrayList<Double>();
		doubles.add(lXZ);
		doubles.add(sXZ);
		doubles.add(lY);
		doubles.add(sY);
		doubles.add(XZ);
		Main.triggerMap.put(trigger, doubles);
		Main.directionMap.put(trigger, type.toString());
		Main.mysql.update("insert into PrisonDoor(world, tX, tY, tZ, lXZ, sXZ, lY, sY, XZ, Type) values ('" + trigger.getWorld().getName() + "', " + trigger.getX() + ", " + trigger.getY() + "," + trigger.getZ() + ", " 
		+ lXZ + ", " + sXZ + ", " + lY + ", " + sY + ", " + XZ + ",'" + type.toString() + "');");
	}
	
	public static void removePrisonDoor(Location trigger) {
		if(Main.triggerMap.containsKey(trigger)) {
			Main.triggerMap.remove(trigger);
			Main.directionMap.remove(trigger);
			Main.mysql.update("delete from PrisonDoor where tX = " + trigger.getX() + " and tY = " + trigger.getY() + " and tZ = " + trigger.getZ() + ";");
		}
	}
	
	public static void loadNPC() {
		Main.mysql.update("create table if not exists NPC(Name varchar(16), UUID varchar(200), x double, y double, z double, yaw float, pitch float, world varchar(64), texture varchar(750), signature varchar(750));");
	try {
		ResultSet name = Main.mysql.query("select Name from NPC;");
		ResultSet uuid = Main.mysql.query("select UUID from NPC;");
		ResultSet x = Main.mysql.query("select x from NPC;");
		ResultSet y = Main.mysql.query("select y from NPC;");
		ResultSet z = Main.mysql.query("select z from NPC;");
		ResultSet yaw = Main.mysql.query("select yaw from NPC;");
		ResultSet pitch = Main.mysql.query("select pitch from NPC;");
		ResultSet world = Main.mysql.query("select world from NPC;");
		ResultSet texture = Main.mysql.query("select texture from NPC;");
		ResultSet signature = Main.mysql.query("select signature from NPC;");
		int coulumns = name.getMetaData().getColumnCount();
		while (name.next() && uuid.next() && x.next() && y.next() && z.next() && yaw.next() && pitch.next() && world.next() && texture.next() && signature.next()) {
			for(int i = 1; i<= coulumns; i++) {
				NPC.loadNPC(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i), yaw.getFloat(i), pitch.getFloat(i)), name.getString(i), uuid.getString(i), texture.getString(i), signature.getString(i), world.getString(i));
			}
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	}
	
	public static List<Location> getPoliceComputer() {
		List<Location> PC = new ArrayList<Location>();
		Main.mysql.update("create table if not exists PoliceComputer(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from PoliceComputer;");
			ResultSet y = Main.mysql.query("select y from PoliceComputer;");
			ResultSet z = Main.mysql.query("select z from PoliceComputer;");
			ResultSet world = Main.mysql.query("select world from PoliceComputer;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	PC.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "Polizei Computer wurden geladen!");
		return PC;
	}
	
	public static void addPoliceComputer(Location loc) {
		if(!Main.PoliceComputerLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into PoliceComputer(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.PoliceComputerLoc.add(loc);
		}
	}
	
	public static void removePoliceComputer(Location loc) {
		if(Main.PoliceComputerLoc.contains(loc)) {
			Main.mysql.update("delete from PoliceComputer where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld() + "';");
			Main.PoliceComputerLoc.remove(loc);
		}
	}
	
	public static List<Location> getWardrobe() {
		List<Location> WD = new ArrayList<Location>();
		Main.mysql.update("create table if not exists Wardrobe(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from Wardrobe;");
			ResultSet y = Main.mysql.query("select y from Wardrobe;");
			ResultSet z = Main.mysql.query("select z from Wardrobe;");
			ResultSet world = Main.mysql.query("select world from Wardrobe;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	WD.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§aWardrobe wurden geladen!");
		return WD;
	}
	
	public static void addWardrobe(Location loc) {
		if(!Main.WardrobeLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into Wardrobe(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.WardrobeLoc.add(loc);
		}
	}
	
	public static void removeWardrobe(Location loc) {
		if(Main.WardrobeLoc.contains(loc)) {
			Main.mysql.update("delete from Wardrobe where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld() + "';");
			Main.WardrobeLoc.remove(loc);
		}
	}
	
	public static HashMap<String, Integer> getPoliceEquipPrices(){
		HashMap<String, Integer> PoliceEquipPrices = new HashMap<String, Integer>();
		Main.mysql.update("create table if not exists PoliceEquipPrices(Name varchar(64), Price int);");
		try {
			ResultSet name = Main.mysql.query("select Name from PoliceEquipPrices;");
			ResultSet prices = Main.mysql.query("select Price from PoliceEquipPrices;");
			while(name.next() && prices.next()) {
				int columns = name.getMetaData().getColumnCount();
				for(int i = 1; i <= columns; i++) {
					PoliceEquipPrices.put(name.getString(i), prices.getInt(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return PoliceEquipPrices;
	}
	
	public static void addPoliceEquipPrices(String name, int price) {
		if(!Main.PoliceEquipCost.containsKey(name)) {
			Main.PoliceEquipCost.put(name, price);
			Main.mysql.update("insert into PoliceEquipPrices(Name, Price) values ('" + name + "', " + price + ");");
		}
	}
	
	public static void removePoliceEquipPrices(String name) {
		if(Main.PoliceEquipCost.containsKey(name)) {
			Main.PoliceEquipCost.remove(name);
			Main.mysql.update("delete from PoliceEquipPrices where Name = '" + name + "';");
		}
	}	
	
	public static List<String> getBannedWords(){
		List<String> word = new ArrayList<String>();
		Main.mysql.update("create table if not exists BannedWords(Words varchar(64));");
		try {
			ResultSet rs = Main.mysql.query("select Words from BannedWords;");
			int columns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	word.add(rs.getString(i));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§aBannedWords wurden geladen!");
		return word;
	}
	
	public static void addBannedWords(String word) {
		if(!Main.BannedWords.contains(word)) {
			Main.mysql.update("insert into BannedWords (Words) values ('" + word + "');");
			Main.BannedWords.add(word);
		}
	}
	
	public static void removeBannedWords(String word) {
		if(Main.BannedWords.contains(word)) {
			Main.mysql.update("delete from BannedWords where Words = '" + word + "';");
			Main.BannedWords.remove(word);
		}
	}
	
	public static List<Location> getLightning() {
		List<Location> Lightning = new ArrayList<Location>();
		Main.mysql.update("create table if not exists Lightning(x double, y double, z double, world varchar(64));");
		
		try {
			ResultSet x = Main.mysql.query("select x from Lightning;");
			ResultSet y = Main.mysql.query("select y from Lightning;");
			ResultSet z = Main.mysql.query("select z from Lightning;");
			ResultSet world = Main.mysql.query("select world from Lightning;");
			int columns = x.getMetaData().getColumnCount();
			while (x.next() && y.next() && z.next() && world.next()) {
	            for (int i = 1; i <= columns; i++) {
	            	Lightning.add(new Location(Bukkit.getWorld(world.getString(i)), x.getDouble(i), y.getDouble(i), z.getDouble(i)));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getConsoleSender().sendMessage(Main.PREFIX + "§aLightning wurden geladen!");
		return Lightning;
	}
	
	public static void addLightning(Location loc) {
		if(!Main.LightningLoc.contains(loc)) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().getName();
			Main.mysql.update("insert into Lightning(x, y, z, world) values (" + x + ", " + y + ", " + z + ",  '" + world + "');");
			Main.LightningLoc.add(loc);
		}
	}
	
	public static void removeLightning(Location loc) {
		if(Main.LightningLoc.contains(loc)) {
			Main.mysql.update("delete from Lightning where x = " + loc.getX() + "and y = " + loc.getY() + "and z = " + loc.getZ() + "and world = '" + loc.getWorld() + "';");
			Main.LightningLoc.remove(loc);
		}
	}
	
}
