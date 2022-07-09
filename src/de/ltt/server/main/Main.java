package de.ltt.server.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.ltt.FakePlayer.CreateNPC;
import de.ltt.FakePlayer.PacketReader;
import de.ltt.areas.AcceptHouseRequest;
import de.ltt.areas.AreaRequest;
import de.ltt.areas.DenyHouseRequest;
import de.ltt.areas.HouseRequestBauamt;
import de.ltt.areas.Kaufantrag;
import de.ltt.aufzug.ClickOnAufzug;
import de.ltt.aufzug.Lift;
import de.ltt.aufzug.RegisterLift;
import de.ltt.aufzug.RemoveLift;
import de.ltt.auto.Bauteil;
import de.ltt.auto.Car;
import de.ltt.auto.CarHandler;
import de.ltt.auto.CarOwn;
import de.ltt.auto.CarType;
import de.ltt.auto.SpawnCar;
import de.ltt.bank.command.Geldbeutel;
import de.ltt.bank.command.Money;
import de.ltt.bank.command.SeeMoney;
import de.ltt.bank.listener.Bank_System;
import de.ltt.bank.listener.KontoAuszug;
import de.ltt.bauen.AntiBauenAbbauen;
import de.ltt.bauen.BlöckeBlocken;
import de.ltt.bugList.BugList;
import de.ltt.casino.allgemein.AddJeton;
import de.ltt.casino.allgemein.Jeton;
import de.ltt.casino.allgemein.RemoveJeton;
import de.ltt.casino.allgemein.SeeJeton;
import de.ltt.casino.allgemein.SeeJetonAdmin;
import de.ltt.casino.highorlow.HighorLow;
import de.ltt.firms.ChangeFirmName;
import de.ltt.firms.CreateFirma;
import de.ltt.firms.Firma;
import de.ltt.firms.InviteAcc;
import de.ltt.firms.InviteDel;
import de.ltt.firms.SetPayment;
import de.ltt.firms.SetRights;
import de.ltt.firms.sell.AcceptFirmBuy;
import de.ltt.firms.sell.DenyFirmBuy;
import de.ltt.handy.AnrufACC;
import de.ltt.handy.AnrufBE;
import de.ltt.handy.AnrufDel;
import de.ltt.handy.BuyGuthaben;
import de.ltt.handy.GetHandy;
import de.ltt.handy.GetNummer;
import de.ltt.handy.Handy;
import de.ltt.handy.SendStandort;
import de.ltt.hologram.Hologram;
import de.ltt.modelInv.VerkehrSchilder;
import de.ltt.navi.NavigationInv;
import de.ltt.navi.RegisterNaviPoint;
import de.ltt.other.AFKCommand;
import de.ltt.other.BannedWord;
import de.ltt.other.ChangeSkin;
import de.ltt.other.ClearChat;
import de.ltt.other.CmdSperre;
import de.ltt.other.Discord;
import de.ltt.other.Einstellungen;
import de.ltt.other.GC;
import de.ltt.other.GM;
import de.ltt.other.Glow;
import de.ltt.other.HashTags;
import de.ltt.other.MeClick;
import de.ltt.other.MeCommand;
import de.ltt.other.PlayerInteractInv;
import de.ltt.other.SetAFKPoint;
import de.ltt.other.ShowOnlinePlayer;
import de.ltt.other.SpawnLightning;
import de.ltt.other.Test;
import de.ltt.other.Vanish;
import de.ltt.other.Vote;
import de.ltt.other.Wardrobe;
import de.ltt.other.invsee;
import de.ltt.other.InvSpeichern.Serialization;
import de.ltt.other.KopfInv.KopfInv;
import de.ltt.other.LagerFeuer.LagerFeuer;
import de.ltt.other.LagerFeuer.LagerfeuerInfo;
import de.ltt.other.Sinnlosefakten.Sinnlosefakten;
import de.ltt.other.Teleport.TP;
import de.ltt.reports.DoneReport;
import de.ltt.reports.FlAccept;
import de.ltt.reports.FrAccept;
import de.ltt.reports.MlAccept;
import de.ltt.reports.ReportCommand;
import de.ltt.rights.AddAdmin;
import de.ltt.rights.Moderator;
import de.ltt.rights.Supporter;
import de.ltt.server.listener.AFKListener;
import de.ltt.server.listener.CharClick;
import de.ltt.server.listener.Chat;
import de.ltt.server.listener.CommandListener;
import de.ltt.server.listener.Join;
import de.ltt.server.listener.Move;
import de.ltt.server.listener.PlayerDismount;
import de.ltt.server.listener.PlayerMotd;
import de.ltt.server.listener.Quit;
import de.ltt.server.listener.ResourcePack;
import de.ltt.server.listener.Wartemodus;
import de.ltt.server.mySQL.MySQL;
import de.ltt.server.mySQL.SQLData;
import de.ltt.server.reflaction.Broadcaster;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;
import de.ltt.server.reflaction.PlayerInfo;
import de.ltt.server.reflaction.Rights;
import de.ltt.server.reflaction.area.AreaInfo;
import de.ltt.staat.bauamt.Bauamt;
import de.ltt.staat.bauamt.BauamtInviteAcc;
import de.ltt.staat.bauamt.BauamtInviteDel;
import de.ltt.staat.bauamt.houses.AcceptHouseBuy;
import de.ltt.staat.bauamt.houses.DenyHouseBuy;
import de.ltt.staat.bürgermeister.Bürgermeister;
import de.ltt.staat.bürgermeister.Nomination;
import de.ltt.staat.bürgermeister.SetBürgermeister;
import de.ltt.staat.bürgermeister.SetNominationLoc;
import de.ltt.staat.bürgermeister.SetWahlLoc;
import de.ltt.staat.bürgermeister.Waehlen;
import de.ltt.staat.bürgermeister.Wahl;
import de.ltt.staat.medic.AcceptMedicInvite;
import de.ltt.staat.medic.DenyMedicInvite;
import de.ltt.staat.medic.Medic;
import de.ltt.staat.medic.MedicRights;
import de.ltt.staat.medic.SetMedicPay;
import de.ltt.staat.medic.SetMedicRights;
import de.ltt.staat.medic.violations.VioListener;
import de.ltt.staat.police.equip.PoliceEquip;
import de.ltt.staat.police.grundSystem.AcceptPoliceInvite;
import de.ltt.staat.police.grundSystem.DenyPoliceInvite;
import de.ltt.staat.police.grundSystem.Police;
import de.ltt.staat.police.grundSystem.PoliceRights;
import de.ltt.staat.police.grundSystem.SetPolicePay;
import de.ltt.staat.police.grundSystem.SetPoliceRights;
import de.ltt.staat.police.policeComputer.ClickOnComputer;
import de.ltt.staat.police.policeComputer.RegisterComputer;
import de.ltt.türSysteme.GefängnisTür;
import de.ltt.türSysteme.GeheimTür;
import de.ltt.türSysteme.GetSchlüssel;
import de.ltt.türSysteme.IronTrapDoor;
import de.ltt.türSysteme.MedicTür;
import de.ltt.türSysteme.PolizeiTür;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static MySQL mysql;
	public static Field nameField = PlayerInfo.getField(GameProfile.class, "name");

	public static File lagerfeuerFile;
	public static FileConfiguration lagerfeuerConfig;
	public static File medicFile;
	public static FileConfiguration medicConfig;
	public static File policeFile;
	public static FileConfiguration policeConfig;
	public static File playerFile;
	public static FileConfiguration playerConfig;
	public static File charFile;
	public static FileConfiguration charConfig;
	public static File liftFile;
	public static FileConfiguration liftConfig;
	public static File invFile;
	public static FileConfiguration invConfig;
	public static File skinFile;
	public static FileConfiguration skinConfig;
	public static File carFile;
	public static FileConfiguration carConfig;
	public static File policeAktenFile;
	public static FileConfiguration policeAktenConfig;
	public static File bannedFile;
	public static FileConfiguration bannedConfig;

	public static final String PREFIX = "[MilePlay] ";
	public static final String URL_PREFIX = "§7[§cURL-Warnung§7] ";
	public static final String KEINE_RECHTE = "§cDazu hast du keine Rechte";
	public static final String KEINE_RECHTE_ADMIN = "§cDazu musst du §6Admin §csein!";
	public static final String KEINE_RECHTE_MOD = "§cDazu musst du §6Moderator §csein!";
	public static final String KEINE_RECHTE_SUPPORT = "§cDazu musst du §6Supporter §csein!";
	public static final String KEIN_SPIELER = "Dazu musst du ein Spieler sein!";
	public static final String KEIN_GELD = "§cDu hast nicht gengend Geld!";
	public static final String COMMAND_SPERRE = "§cDieser Command ist gesperrt!";
	public static final String KEINE_RECHTE_BAUAMT = "§cDazu musst du §6Chef im Bauamt§c sein!";
	public static final String MEDIC = "§7[§4Rettungsdienst§7]§r";
	public static final String POLIZEI = "§7[§5Polizei§7]§r";
	public static String TEXTUREPACK = "";

	public static HashMap<Player, Player> frChat = new HashMap<Player, Player>();
	public static HashMap<Player, Player> frChat2 = new HashMap<Player, Player>();
	public static HashMap<Player, Player> inviteMap = new HashMap<Player, Player>();
	public static HashMap<String, String> RightStrings = new HashMap<String, String>();
	public static HashMap<String, String> MedicRightStrings = new HashMap<String, String>();
	public static HashMap<String, String> PolizeiRightStrings = new HashMap<String, String>();
	public static HashMap<Integer, String> NummerPlayer = new HashMap<Integer, String>();
	public static HashMap<String, Integer> PlayerNummer = new HashMap<String, Integer>();
	public static HashMap<Player, Player> AnrufChat = new HashMap<Player, Player>();
	public static HashMap<Player, Player> AnrufeChat2 = new HashMap<Player, Player>();
	public static HashMap<Player, Player> AnrufMap = new HashMap<Player, Player>();
	public static HashMap<UUID, Boolean> handyOnOff = new HashMap<UUID, Boolean>();
	public static HashMap<Player, Integer> wahlPage = new HashMap<Player, Integer>();
	public static HashMap<String, Integer> wahlstats = new HashMap<String, Integer>();
	public static HashMap<String, Integer> PoliceEquipCost = new HashMap<String, Integer>();
	public static HashMap<String, String> pMotd = new HashMap<String, String>();
	public static HashMap<Integer, Integer> areaPriceMap = new HashMap<Integer, Integer>();
	public static HashMap<UUID, List<String>> CharsInv = new HashMap<UUID, List<String>>();

	public static Location AFKLoc;
	public static HashMap<Player, Location> savedLoc = new HashMap<Player, Location>();
	public static List<Player> shortAFK = new ArrayList<Player>();
	public static ArrayList<Player> longerAFK = new ArrayList<Player>();

	public static List<Location> BlockSperreLoc = new ArrayList<Location>();
	public static List<Location> PoliceComputerLoc = new ArrayList<Location>();
	public static List<Location> WardrobeLoc = new ArrayList<Location>();
	public static List<String> AddWartungPlayer = new ArrayList<String>();
	public static List<String> PoliceGeh = new ArrayList<String>();
	public static List<String> PoliceWas = new ArrayList<String>();
	public static List<String> PoliceDanke = new ArrayList<String>();
	public static List<String> SinnloseFakten = new ArrayList<String>();
	public static List<String> GesperrteCMD = new ArrayList<String>();
	public static List<String> KopfInv = new ArrayList<String>();
	public static List<String> KopfSearch = new ArrayList<String>();
	public static List<String> ModelInv = new ArrayList<String>();
	public static List<String> PersonenAkten = new ArrayList<String>();
	public static List<String> ModelSearch = new ArrayList<String>();
	public static List<String> blockedURL = new ArrayList<String>();
	public static List<String> BannedWords = new ArrayList<String>();
	public static List<Location> MedicTürLoc = new ArrayList<Location>();
	public static List<Location> GeheimTürLoc = new ArrayList<Location>();
	public static List<Location> PolizeiTürLoc = new ArrayList<Location>();
	public static List<Location> LightningLoc = new ArrayList<Location>();
	public static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();
	public static List<Player> blocked = new ArrayList<Player>();
	public static HashMap<Location, List<Double>> triggerMap = new HashMap<Location, List<Double>>();
	public static HashMap<Location, String> directionMap = new HashMap<Location, String>();
	public static HashMap<String, Integer> JoinPlayerZahl = new HashMap<String, Integer>();
	public static HashMap<String, Double> CookieClick = new HashMap<String, Double>();
	public static HashMap<Player, Location> PlayerSendStandort = new HashMap<Player, Location>();
	public static List<String> vanish = new ArrayList<String>();
	public static List<String> navigationsPunkte = new ArrayList<String>();
	public static HashMap<String, Location> naviPoints = new HashMap<String, Location>();
	public static HashMap<String, Material> pointMaterial = new HashMap<String, Material>();
	public static HashMap<String, Long> pointKlicks = new HashMap<String, Long>();
	public static HashMap<String, List<EntityPlayer>> tabNPC = new HashMap<String, List<EntityPlayer>>();
	public static HashMap<OfflinePlayer, HashMap<String, Integer>> test = new HashMap<OfflinePlayer, HashMap<String, Integer>>();

	public static List<UUID> gc = new ArrayList<UUID>();

	public static List<String> holo = new ArrayList<String>();

	public static List<String> streets = new ArrayList<String>();
	public static HashMap<String, List<String>> streetNumbers = new HashMap<String, List<String>>();
	public static HashMap<String, Location> streetNumberLoc = new HashMap<String, Location>();

	public static List<String> Admin = new ArrayList<String>();
	public static List<String> Supporter = new ArrayList<String>();
	public static List<String> Moderatoren = new ArrayList<String>();
	public static List<String> hashTag = new ArrayList<String>();
	public static List<String> BetaT = new ArrayList<String>();
	public static List<String> BugsTodo = new ArrayList<String>();

	public static String bürgermeister;
	public static String vizeBürgermeister;
	public static boolean iswahl;
	public static boolean isLightningOn;
	public static String wahlStart;
	public static Location nominationLoc;
	public static Location wahlLoc;
	public static List<String> nominated = new ArrayList<String>();
	public static List<String> wähler = new ArrayList<String>();

	public void onEnable() {
		plugin = this;
		loadConfigs();
		loadData();
		loadCommands();
		loadEvents();
		loadArguments();
		loadRightsStrings();
		closeInvs();
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (PlayerInfo.getChars(p).size() != 0) {
					loadTab(p);
					PlayerInfo pi = new PlayerInfo(p);
					pi.loadInv();
				}
			}
		}
		loadVanish();
		Broadcaster broadcaster = new Broadcaster();
		broadcaster.startVote();
		broadcaster.startAFK();
		new LagerfeuerInfo();
		new AreaInfo();
		clock();
		ject(true);
		loadCars();
	}

	private void loadConfigs() {
		lagerfeuerFile = createCustomFile("Lagerfeuer", "customConfig/");
		lagerfeuerConfig = createCustomConfig(lagerfeuerFile);
		medicFile = createCustomFile("Medic", "customConfig/");
		medicConfig = createCustomConfig(medicFile);
		policeFile = createCustomFile("Police", "customConfig/");
		policeConfig = createCustomConfig(policeFile);
		playerFile = createCustomFile("Player", "customConfig/");
		playerConfig = createCustomConfig(playerFile);
		charFile = createCustomFile("Chars", "customConfig/");
		charConfig = createCustomConfig(charFile);
		liftFile = createCustomFile("Lift", "customConfig/");
		liftConfig = createCustomConfig(liftFile);
		invFile = createCustomFile("Inventories", "customConfig/");
		invConfig = createCustomConfig(invFile);
		skinFile = createCustomFile("Skins", "customConfig/");
		skinConfig = createCustomConfig(skinFile);
		carFile = createCustomFile("Car", "customConfig/");
		carConfig = createCustomConfig(carFile);
		policeAktenFile = createCustomFile("PoliceAkten", "customConfig/");
		policeAktenConfig = createCustomConfig(policeAktenFile);
		bannedFile = createCustomFile("Banned", "customConfig/");
		bannedConfig = createCustomConfig(bannedFile);
		System.out.println(Main.PREFIX + "Configs wurden geladen!");

	}

	public File createCustomFile(String name, String path) {
		File configFile = new File(getDataFolder(), path + name + ".yml");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			configFile.getParentFile().mkdirs();
		}
		return configFile;
	}

	private FileConfiguration createCustomConfig(File configFile) {
		FileConfiguration config;

		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return config;

	}

	public static void connectMySQL() {
		mysql = new MySQL("127.0.0.1", "server", "server", "server");
	}

	public void onDisable() {
		saveCars();
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (PlayerInfo.getChars(p).size() != 0) {
					unloadTab();
					PlayerInfo pi = new PlayerInfo(p);
					pi.saveInv();
					pi.saveLastLocation();
					pi.saveAktuellSkin();
					ject(false);
				}
			}
		}
		LagerfeuerInfo.removeFires();
		UnloadVanish();	
	}

	public void saveCars() {
		List<String> ids = new ArrayList<String>();
		Bukkit.getPlayer("nicht_menschlich").sendMessage("hier wird geladen");
		for (Car car : Car.cars) {
			Bukkit.getPlayer("nicht_menschlich").sendMessage("hier wird geladen für jedes einzelne4e auto");
			UUID id = car.getId();
			CarType carType = car.getCarType();
			double maxSpeed = car.getMaxSpeed();
			double acceleration = car.getAcceleration();
			double deceleration = car.getDeceleration();
			double rotation = car.getRotation();
			List<Bauteil> bauteile = car.getBauteile();
			double fuel = car.getFuel();
			double maxFuel = car.getMaxFuel();
			Inventory trunk = car.getTrunk();
			boolean hasTrunk = car.isHasTrunk();
			int trunkSpace = car.getTrunkSpace();
			List<ArmorStand> stands = car.getStands();
			CarOwn type = car.getType();
			String owner = car.getOwner();
			int ownFirm = car.getOwnFirm();
			boolean gear = car.isGear();
			String registerNumber = car.getRegisterNumber();
			carConfig.set(id.toString() + ".carType", carType.toString());
			carConfig.set(id.toString() + ".maxSpeed", maxSpeed);
			carConfig.set(id.toString() + ".acceleration", acceleration);
			carConfig.set(id.toString() + ".deceleration", deceleration);
			carConfig.set(id.toString() + ".rotation", rotation);
			carConfig.set(id.toString() + ".fuel", fuel);
			carConfig.set(id.toString() + ".maxFuel", maxFuel);
			carConfig.set(id.toString() + ".hasTrunk", hasTrunk);
			carConfig.set(id.toString() + ".trunkSpace", trunkSpace);
			carConfig.set(id.toString() + ".ownType", type.toString());
			carConfig.set(id.toString() + ".gear", gear);
			String values = Serialization.invToBase64(trunk);
			Main.getPlugin().getInvConfig().set(id + ".trunk.items", values);
			Main.getPlugin().saveInvConfig();
			if (type == CarOwn.FIRM) {
				carConfig.set(id.toString() + ".owner", ownFirm);
			} else {
				carConfig.set(id.toString() + ".owner", owner);
			}
			List<String> standList = new ArrayList<String>();
			for (ArmorStand stand : stands) {
				standList.add(stand.getUniqueId().toString());
			}
			carConfig.set(id.toString() + ".stands", standList);
			carConfig.set(id.toString() + ".registerNumber", registerNumber);
			ids.add(id.toString());
			System.out.println(id + "unload");
		}
		carConfig.set("ids", ids);
		Main.getPlugin().saveCarConfig();

	}

	public void loadCars() {
		List<String> ids = carConfig.getStringList("ids");
		System.out.println("load");
		for (String id : ids) {
			System.out.println(id + "load");
			Car car = Car.loadCar(UUID.fromString(id));
			Car.cars.add(car);
		}
	}

	public static void BroadcastLoc(Location loc, int distance, String nachricht) {

		for (Player current : Bukkit.getOnlinePlayers()) {
			if (loc.distance(current.getLocation()) <= distance) {
				current.sendMessage(nachricht);

			}

		}

	}

	public static void ChatMessage(Location loc, int type, String nachricht) {
		for (Player current : Bukkit.getOnlinePlayers()) {
			switch (type) {
			case 0:
				int blocks = 0;
				/*
				 * if (loc.distance(current.getLocation()) >= 0 &&
				 * loc.distance(current.getLocation()) <= 10) { current.sendMessage(nachricht);
				 * } else if (loc.distance(current.getLocation()) > 10 &&
				 * loc.distance(current.getLocation()) <= 15) { current.sendMessage("§7" +
				 * nachricht); } else if (loc.distance(current.getLocation()) > 15 &&
				 * loc.distance(current.getLocation()) <= 20) { current.sendMessage("§8" +
				 * nachricht); }
				 */
				double rayDistance = current.getLocation().distance(loc);
				Location from = loc; // Whatever location you want to start from
				Location to = current.getLocation(); // Whatever location you want the ray to go to
				Vector direction = to.clone().subtract(from).toVector().normalize(); // Direction of travel
				for (int i = 0; i < rayDistance; i++) {
					from.add(direction);
					if (from.getBlock().getType() != Material.AIR) {
						blocks++;
					}
				}
				if (blocks < 2)
					current.sendMessage(nachricht);
				else
					current.sendMessage("kam nix an... IDIOT");
				current.sendMessage(blocks + "");
				break;
			case 1:
				if (loc.distance(current.getLocation()) >= 0 && loc.distance(current.getLocation()) <= 20) {
					current.sendMessage(nachricht);
				} else if (loc.distance(current.getLocation()) > 20 && loc.distance(current.getLocation()) <= 30) {
					current.sendMessage("§7" + nachricht);
				} else if (loc.distance(current.getLocation()) > 30 && loc.distance(current.getLocation()) <= 40) {
					current.sendMessage("§8" + nachricht);
				}
				break;
			case 2:
				if (loc.distance(current.getLocation()) >= 0 && loc.distance(current.getLocation()) <= 3) {
					current.sendMessage("§8" + nachricht);
				}
				break;
			}
		}
	};

	public static void loadData() {
		connectMySQL();

		Main.Admin = SQLData.getAdmin();
		Main.Moderatoren = SQLData.getMods();
		Main.Supporter = SQLData.getSupps();
		Main.BetaT = SQLData.getTester();
		Main.BugsTodo = SQLData.getBugs();
		Main.GesperrteCMD = SQLData.getBlockedCMD();
		Main.KopfSearch = SQLData.getSearch();
		Main.ModelSearch = SQLData.getModelSearch();
		Main.SinnloseFakten = SQLData.getSinnlosefakten();
		Main.pMotd = SQLData.getPMotd();
		Main.handyOnOff = SQLData.getHandyOnOff();
		Main.JoinPlayerZahl = SQLData.getJoinPlayer();
		Main.GeheimTürLoc = SQLData.getGeheimDoors();
		Main.CookieClick = SQLData.getCookieClicker();
		Main.PoliceComputerLoc = SQLData.getPoliceComputer();
		Main.PoliceEquipCost = SQLData.getPoliceEquipPrices();

		Main.TEXTUREPACK = Main.getPlugin().getConfig().getString("TexturePack");

		Main.holo = SQLData.getHolo();
		Main.KopfInv = SQLData.getKöpfeList();
		Main.ModelInv = SQLData.getModelList();
		Main.vanish = SQLData.getVanish();

		Main.iswahl = Main.getPlugin().getConfig().getBoolean("Staat.isWahl");
		Main.wahlStart = Main.getPlugin().getConfig().getString("Staat.wahlStart");
		Main.nominated = Main.getPlugin().getConfig().getStringList("Staat.nominated");
		Main.wähler = Main.getPlugin().getConfig().getStringList("Staat.wähler");
		Main.isLightningOn = Main.getPlugin().getConfig().getBoolean("Lightning");
		Main.AddWartungPlayer = SQLData.getWartungsPlayer();

		List<String> wahlStatsKey = Main.getPlugin().getConfig().getStringList("Staat.wahlStats.key");
		List<Integer> wahlStatsValue = Main.getPlugin().getConfig().getIntegerList("Staat.wahlStats.value");
		for (int i = 0; i < wahlStatsKey.size(); i++) {
			Main.wahlstats.put(wahlStatsKey.get(i), wahlStatsValue.get(i));
		}

		if (plugin.getConfig().contains("Staat.nominationLoc.world")) {
			Main.nominationLoc = new Location(
					Bukkit.getWorld(plugin.getConfig().getString("Staat.nominationLoc.world")),
					plugin.getConfig().getDouble("Staat.nominationLoc.x"),
					plugin.getConfig().getDouble("Staat.nominationLoc.y"),
					plugin.getConfig().getDouble("Staat.nominationLoc.z"));
		} else
			System.out.println("Es wurde keine Welt geladen!");

		if (plugin.getConfig().contains("Staat.wahlLoc.world")) {
			Main.wahlLoc = new Location(Bukkit.getWorld(plugin.getConfig().getString("Staat.wahlLoc.world")),
					plugin.getConfig().getDouble("Staat.wahlLoc.x"), plugin.getConfig().getDouble("Staat.wahlLoc.y"),
					plugin.getConfig().getDouble("Staat.wahlLoc.z"));
		} else
			System.out.println("Es wurde keine Welt geladen!");

		if (plugin.getConfig().contains("Server.AFKLoc.world")) {
			Main.AFKLoc = new Location(Bukkit.getWorld(plugin.getConfig().getString("Server.AFKLoc.world")),
					plugin.getConfig().getDouble("Server.AFKLoc.x"), plugin.getConfig().getDouble("Server.AFKLoc.y"),
					plugin.getConfig().getDouble("Server.AFKLoc.z"));
		} else
			System.out.println("Es wurde keine Welt geladen!");

		if (!Main.getPlugin().getConfig().contains("Lightning")) {
			Main.getPlugin().getConfig().set("Lightning", true);
		}

		MedicTürLoc = SQLData.getMedicDoors();
		PolizeiTürLoc = SQLData.getPolizeiDoors();
		BlockSperreLoc = SQLData.getBlockSperre();
		hashTag = SQLData.getHashTags();
		areaPriceMap = SQLData.getAreaPrices();
		SQLData.getNummer();
		SQLData.getNaviPoint();
		SQLData.getPrisonDoors();
		blockedURL = SQLData.getBlockedURL();
		SQLData.loadNPC();
		Main.BannedWords = SQLData.getBannedWords();
		Main.WardrobeLoc = SQLData.getWardrobe();
		Main.LightningLoc = SQLData.getLightning();
		Main.PersonenAkten = Main.getAllChars();

		Main.bürgermeister = Main.getPlugin().getConfig().getString("Staat.bürgermeister");
		Main.vizeBürgermeister = Main.getPlugin().getConfig().getString("Staat.vizeBürgermeister");
	}

	public void loadCommands() {

		// Bank Geld
		getCommand("geldbeutel").setTabCompleter(new AllTabCompleter());
		getCommand("seemoney").setTabCompleter(new AllTabCompleter());
		getCommand("money").setTabCompleter(new AllTabCompleter());

		getCommand("money").setExecutor(new Money());
		getCommand("geldbeutel").setExecutor(new Geldbeutel());
		getCommand("seemoney").setExecutor(new SeeMoney());

		// Server Rnge
		getCommand("Admin").setTabCompleter(new AllTabCompleter());
		getCommand("Moderator").setTabCompleter(new AllTabCompleter());
		getCommand("Supporter").setTabCompleter(new AllTabCompleter());

		getCommand("Admin").setExecutor(new AddAdmin());
		getCommand("Moderator").setExecutor(new Moderator());
		getCommand("Supporter").setExecutor(new Supporter());

		// Admin Feature
		getCommand("gc").setTabCompleter(new AllTabCompleter());
		getCommand("invsee").setTabCompleter(new AllTabCompleter());
		getCommand("gm").setTabCompleter(new AllTabCompleter());
		getCommand("rltextures").setTabCompleter(new AllTabCompleter());
		getCommand("reloadtextures").setTabCompleter(new AllTabCompleter());
		getCommand("clearchat").setTabCompleter(new AllTabCompleter());
		getCommand("sop").setTabCompleter(new AllTabCompleter());
		getCommand("showonlineplayer").setTabCompleter(new AllTabCompleter());
		getCommand("skin").setTabCompleter(new AllTabCompleter());

		getCommand("skin").setExecutor(new ChangeSkin());
		getCommand("gc").setExecutor(new GC());
		getCommand("sop").setExecutor(new ShowOnlinePlayer());
		getCommand("showonlineplayer").setExecutor(new ShowOnlinePlayer());
		getCommand("gm").setExecutor(new GM());
		getCommand("invsee").setExecutor(new invsee());
		getCommand("rltextures").setExecutor(new ResourcePack());
		getCommand("reloadtextures").setExecutor(new ResourcePack());
		getCommand("clearchat").setExecutor(new ClearChat());
		getCommand("glow").setExecutor(new Glow());

		// Report
		getCommand("report").setTabCompleter(new AllTabCompleter());
		getCommand("frAccept").setTabCompleter(new AllTabCompleter());
		getCommand("mlaccept").setTabCompleter(new AllTabCompleter());
		getCommand("flaccept").setTabCompleter(new AllTabCompleter());
		getCommand("donereport").setTabCompleter(new AllTabCompleter());

		getCommand("report").setExecutor(new ReportCommand());
		getCommand("frAccept").setExecutor(new FrAccept());
		getCommand("mlaccept").setExecutor(new MlAccept());
		getCommand("flaccept").setExecutor(new FlAccept());
		getCommand("donereport").setExecutor(new DoneReport());

		// Auto
		getCommand("spawncar").setExecutor(new SpawnCar());

		// Spieler Feature
		getCommand("wardrobe").setTabCompleter(new AllTabCompleter());

		getCommand("me").setExecutor(new MeCommand());
		getCommand("wardrobe").setExecutor(new Wardrobe());
		// Firma
		getCommand("createfirma").setTabCompleter(new AllTabCompleter());
		getCommand("acceptfirminvite").setTabCompleter(new AllTabCompleter());
		getCommand("denyfirminvite").setTabCompleter(new AllTabCompleter());
		getCommand("firma").setTabCompleter(new AllTabCompleter());

		getCommand("createfirma").setExecutor(new CreateFirma());
		getCommand("acceptfirminvite").setExecutor(new InviteAcc());
		getCommand("denyfirminvite").setExecutor(new InviteDel());
		getCommand("firma").setExecutor(new Firma());
		getCommand("acceptfirmbuy").setExecutor(new AcceptFirmBuy());
		getCommand("denyfirmbuy").setExecutor(new DenyFirmBuy());

		// Casino
		getCommand("addjeton").setTabCompleter(new AllTabCompleter());
		getCommand("removejeton").setTabCompleter(new AllTabCompleter());
		getCommand("seejeton").setTabCompleter(new AllTabCompleter());
		getCommand("spawnjeton").setTabCompleter(new AllTabCompleter());
		getCommand("seejetonadmin").setTabCompleter(new AllTabCompleter());

		getCommand("addjeton").setExecutor(new AddJeton());
		getCommand("removejeton").setExecutor(new RemoveJeton());
		getCommand("seejeton").setExecutor(new SeeJeton());
		getCommand("spawnjeton").setExecutor(new Jeton());
		getCommand("seejetonadmin").setExecutor(new SeeJetonAdmin());

		// Handy
		getCommand("getguthaben").setTabCompleter(new AllTabCompleter());
		getCommand("getnummer").setTabCompleter(new AllTabCompleter());
		getCommand("gethandy").setTabCompleter(new AllTabCompleter());
		getCommand("acceptanruf").setTabCompleter(new AllTabCompleter());
		getCommand("denyanruf").setTabCompleter(new AllTabCompleter());
		getCommand("auflegen").setTabCompleter(new AllTabCompleter());

		getCommand("getguthaben").setExecutor(new BuyGuthaben());
		getCommand("getnummer").setExecutor(new GetNummer());
		getCommand("gethandy").setExecutor(new GetHandy());
		getCommand("acceptanruf").setExecutor(new AnrufACC());
		getCommand("denyanruf").setExecutor(new AnrufDel());
		getCommand("auflegen").setExecutor(new AnrufBE());
		getCommand("startroutestandort").setExecutor(new SendStandort());

		// Staat
		getCommand("wahl").setTabCompleter(new AllTabCompleter());
		getCommand("nominieren").setTabCompleter(new AllTabCompleter());
		getCommand("setnominationLoc").setTabCompleter(new AllTabCompleter());
		getCommand("setwahlLoc").setTabCompleter(new AllTabCompleter());
		getCommand("waehlen").setTabCompleter(new AllTabCompleter());
		getCommand("setBuergermeister").setTabCompleter(new AllTabCompleter());
		getCommand("buergermeister").setTabCompleter(new AllTabCompleter());
		getCommand("bauamt").setTabCompleter(new AllTabCompleter());
		getCommand("acceptbauamtinvite").setTabCompleter(new AllTabCompleter());
		getCommand("denybauamtinvite").setTabCompleter(new AllTabCompleter());

		getCommand("wahl").setExecutor(new Wahl());
		getCommand("nominieren").setExecutor(new Nomination());
		getCommand("setnominationLoc").setExecutor(new SetNominationLoc());
		getCommand("setwahlLoc").setExecutor(new SetWahlLoc());
		getCommand("waehlen").setExecutor(new Waehlen());
		getCommand("buergermeister").setExecutor(new Bürgermeister());

		getCommand("setBuergermeister").setExecutor(new SetBürgermeister());

		getCommand("bauamt").setExecutor(new Bauamt());
		getCommand("acceptbauamtinvite").setExecutor(new BauamtInviteAcc());
		getCommand("denybauamtinvite").setExecutor(new BauamtInviteDel());

		// Extra
		getCommand("hashtags").setTabCompleter(new AllTabCompleter());
		getCommand("holo").setTabCompleter(new AllTabCompleter());
		getCommand("lift").setTabCompleter(new AllTabCompleter());
		getCommand("bugstodo").setTabCompleter(new AllTabCompleter());
		getCommand("wartung").setTabCompleter(new AllTabCompleter());
		getCommand("cmdsperre").setTabCompleter(new AllTabCompleter());
		getCommand("kopf").setTabCompleter(new AllTabCompleter());
		getCommand("model").setTabCompleter(new AllTabCompleter());
		getCommand("sinnlosefakten").setTabCompleter(new AllTabCompleter());
		getCommand("einstellungen").setTabCompleter(new AllTabCompleter());
		getCommand("geheimtuer").setTabCompleter(new AllTabCompleter());
		getCommand("npc").setTabCompleter(new AllTabCompleter());
		getCommand("setAFKLoc").setTabCompleter(new AllTabCompleter());
		getCommand("afk").setTabCompleter(new AllTabCompleter());
		getCommand("charakter").setTabCompleter(new AllTabCompleter());
		getCommand("bannedword").setTabCompleter(new AllTabCompleter());
		getCommand("lightning").setTabCompleter(new AllTabCompleter());

		getCommand("test").setExecutor(new Test());
		getCommand("bannedword").setExecutor(new BannedWord());
		getCommand("charakter").setExecutor(new CharClick());
		getCommand("hashtags").setExecutor(new HashTags());
		getCommand("holo").setExecutor(new Hologram());
		getCommand("lift").setExecutor(new Lift());
		getCommand("vs").setExecutor(new VerkehrSchilder());
		getCommand("bugstodo").setExecutor(new BugList());
		getCommand("ironopen").setExecutor(new IronTrapDoor());
		getCommand("cmdsperre").setExecutor(new CmdSperre());
		getCommand("wartung").setExecutor(new Wartemodus());
		getCommand("kopf").setExecutor(new KopfInv());
		getCommand("model").setExecutor(new de.ltt.other.ModelInv.ModelInv());
		getCommand("sinnlosefakten").setExecutor(new Sinnlosefakten());
		getCommand("einstellungen").setExecutor(new Einstellungen());
		getCommand("vanish").setExecutor(new Vanish());
		getCommand("npc").setExecutor(new CreateNPC());
		getCommand("vote").setExecutor(new Vote());
		getCommand("geheimtuer").setExecutor(new GeheimTür());
		getCommand("setAFKLoc").setExecutor(new SetAFKPoint());
		getCommand("afk").setExecutor(new AFKCommand());
		getCommand("discord").setExecutor(new Discord());
		getCommand("lightning").setExecutor(new SpawnLightning());

		// Tür
		getCommand("medicdoor").setTabCompleter(new AllTabCompleter());
		getCommand("polizeidoor").setTabCompleter(new AllTabCompleter());
		getCommand("getkey").setTabCompleter(new AllTabCompleter());
		getCommand("prisondoor").setTabCompleter(new AllTabCompleter());

		getCommand("medicdoor").setExecutor(new MedicTür());
		getCommand("polizeidoor").setExecutor(new PolizeiTür());
		getCommand("getkey").setExecutor(new GetSchlüssel());
		getCommand("prisondoor").setExecutor(new GefängnisTür());

		// Bauen
		getCommand("blocksperre").setTabCompleter(new AllTabCompleter());
		getCommand("blocksperre").setExecutor(new BlöckeBlocken());

		// Haus
		getCommand("haus").setTabCompleter(new AllTabCompleter());
		getCommand("accepthouserequest").setTabCompleter(new AllTabCompleter());
		getCommand("denyhouserequest").setTabCompleter(new AllTabCompleter());
		getCommand("accepthousebuy").setTabCompleter(new AllTabCompleter());
		getCommand("denyhousebuy").setTabCompleter(new AllTabCompleter());
		getCommand("kaufantrag").setTabCompleter(new AllTabCompleter());

		getCommand("haus").setExecutor(new AreaRequest());
		getCommand("accepthouserequest").setExecutor(new AcceptHouseRequest());
		getCommand("denyhouserequest").setExecutor(new DenyHouseRequest());
		getCommand("accepthousebuy").setExecutor(new AcceptHouseBuy());
		getCommand("denyhousebuy").setExecutor(new DenyHouseBuy());
		getCommand("kaufantrag").setExecutor(new Kaufantrag());

		// Test
		// getCommand("test").setExecutor(new Test());

		// Navi
		getCommand("navi").setTabCompleter(new AllTabCompleter());
		getCommand("registernavipoint").setTabCompleter(new AllTabCompleter());

		getCommand("navi").setExecutor(new NavigationInv());
		getCommand("registernavipoint").setExecutor(new RegisterNaviPoint());

		// Medic
		getCommand("rettungsdienst").setTabCompleter(new AllTabCompleter());

		getCommand("rettungsdienst").setExecutor(new Medic());
		getCommand("acceptmedicinvite").setExecutor(new AcceptMedicInvite());
		getCommand("denymedicinvite").setExecutor(new DenyMedicInvite());

		// Polizei
		// getCommand("tp").setTabCompleter(new AllTabCompleter());

		getCommand("polizei").setTabCompleter(new AllTabCompleter());
		getCommand("polizeicomputer").setTabCompleter(new AllTabCompleter());

		getCommand("polizei").setExecutor(new Police());
		getCommand("polizeicomputer").setExecutor(new RegisterComputer());
		getCommand("acceptpolizeiinvite").setExecutor(new AcceptPoliceInvite());
		getCommand("denypolizeiinvite").setExecutor(new DenyPoliceInvite());
	}

	public void loadEvents() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new Join(), this);
		pluginManager.registerEvents(new Bank_System(), this);
		pluginManager.registerEvents(new ReportCommand(), this);
		pluginManager.registerEvents(new Chat(), this);
		pluginManager.registerEvents(new HighorLow(), this);
		pluginManager.registerEvents(new CreateFirma(), this);
		pluginManager.registerEvents(new Jeton(), this);
		pluginManager.registerEvents(new SetPayment(), this);
		pluginManager.registerEvents(new SetRights(), this);
		pluginManager.registerEvents(new PlayerInteractInv(), this);
		pluginManager.registerEvents(new PlayerDismount(), this);
		pluginManager.registerEvents(new Move(), this);
		pluginManager.registerEvents(new Handy(), this);
		pluginManager.registerEvents(new Quit(), this);
		pluginManager.registerEvents(new CommandListener(), this);
		pluginManager.registerEvents(new MedicTür(), this);
		pluginManager.registerEvents(new PolizeiTür(), this);
		pluginManager.registerEvents(new Waehlen(), this);
		pluginManager.registerEvents(new ResourcePack(), this);
		pluginManager.registerEvents(new RegisterLift(), this);
		pluginManager.registerEvents(new ClickOnAufzug(), this);
		pluginManager.registerEvents(new RemoveLift(), this);
		pluginManager.registerEvents(new SpawnCar(), this);
		pluginManager.registerEvents(new MeClick(), this);
		pluginManager.registerEvents(new LagerFeuer(), this);
		pluginManager.registerEvents(new AntiBauenAbbauen(), this);
		pluginManager.registerEvents(new BlöckeBlocken(), this);
		pluginManager.registerEvents(new AreaRequest(), this);
		pluginManager.registerEvents(new HouseRequestBauamt(), this);
		pluginManager.registerEvents(new AcceptHouseBuy(), this);
		pluginManager.registerEvents(new KopfInv(), this);
		pluginManager.registerEvents(new de.ltt.other.ModelInv.ModelInv(), this);
		pluginManager.registerEvents(new PlayerMotd(), this);
		pluginManager.registerEvents(new Einstellungen(), this);
		pluginManager.registerEvents(new IronTrapDoor(), this);
		pluginManager.registerEvents(new NavigationInv(), this);
		pluginManager.registerEvents(new RegisterNaviPoint(), this);
		pluginManager.registerEvents(new ChangeFirmName(), this);
		pluginManager.registerEvents(new KontoAuszug(), this);
		pluginManager.registerEvents(new GeheimTür(), this);
		pluginManager.registerEvents(new GefängnisTür(), this);
		pluginManager.registerEvents(new SetMedicRights(), this);
		pluginManager.registerEvents(new SetMedicPay(), this);
		pluginManager.registerEvents(new SetPoliceRights(), this);
		pluginManager.registerEvents(new SetPolicePay(), this);
		pluginManager.registerEvents(new Test(), this);
		pluginManager.registerEvents(new CreateNPC(), this);
		pluginManager.registerEvents(new VioListener(), this);
		pluginManager.registerEvents(new AFKListener(), this);
		pluginManager.registerEvents(new PoliceEquip(), this);
		pluginManager.registerEvents(new Police(), this);
		pluginManager.registerEvents(new RegisterComputer(), this);
		pluginManager.registerEvents(new ClickOnComputer(), this);
		pluginManager.registerEvents(new CharClick(), this);
		pluginManager.registerEvents(new Wardrobe(), this);
		pluginManager.registerEvents(new CarHandler(), this);

	}

	public void loadArguments() {
		PoliceGeh.add("Du bekommst bei mir nichts!");
		PoliceGeh.add("Du bekommst von mir nichts verstanden?");
		PoliceGeh.add("Du hast hier nichts zu suchen!");
		PoliceGeh.add("Was willst du hier, geh weg!");
		PoliceWas.add("was brauchst du?");
		PoliceWas.add("wie kann ich dir helfen?");
		PoliceWas.add("brauchst du neue ausrüstung?");
		PoliceDanke.add("Wenn du nochmal was brauchst sag bescheid!");
		PoliceDanke.add("Schönen Tag dir noch!");
		PoliceDanke.add("Viel Spaß mit dem zeug!");
	}

	public void loadRightsStrings() {
		RightStrings.put(Rights.ENER_BUYBURNMATERIAL.toString(), "Verbrennungsmaterial kaufen");
		RightStrings.put(Rights.ENER_SELLTARIF.toString(), "Tarife an Spieler anbieten");
		RightStrings.put(Rights.ENER_SETBURNMATERIAL.toString(), "Verbrennungsmaterial für Fabriken einstellen");
		RightStrings.put(Rights.ENER_SETTARIF.toString(), "Tarife einstellen");
		RightStrings.put(Rights.GARB_GETWASTE.toString(), "Entscheiden bei wem der Müll abgeholt werden soll");
		RightStrings.put(Rights.GARB_SELLPRODUCTS.toString(), "Aus Müllsortierung entstandene Produkte verkaufen");
		RightStrings.put(Rights.GARB_SETATOMICWASTEPLACE.toString(), "Müllplatz für Atommüll setzen");
		RightStrings.put(Rights.GARB_SETPRODUCTS.toString(), "Einstellen, welche Produkte zum Verkauf stehen");
		RightStrings.put(Rights.IMMO_BUYPHOUSE.toString(), "Handelshäuser für die Firma kaufen");
		RightStrings.put(Rights.IMMO_SELLPHOUSE.toString(), "Handeslhäuser für die Firma verkaufen");
		RightStrings.put(Rights.IMMO_SETPRICE.toString(), "Preise für bestimme Handelshäuser festlegen");
		RightStrings.put(Rights.LAWS_BUYBUILDPLACE.toString(), "Anbaugebiet für Landwirtschaft kaufen");
		RightStrings.put(Rights.LAWS_CHANGEPRICE.toString(), "Preise für Produkte festlegen");
		RightStrings.put(Rights.LAWS_SELLPRODUCTS.toString(), "Produkte verkaufen");
		RightStrings.put(Rights.LAWS_SETPRODUCTS.toString(), "Einstellen welche Produkte zum Verkauf stehen");
		RightStrings.put(Rights.MAIN_BUYEXTERNPRODUCTS.toString(), "Produkte anderer Firmen kaufen");
		RightStrings.put(Rights.MAIN_BUYFIRMHOUSE.toString(), "Firmengelände kaufen");
		RightStrings.put(Rights.MAIN_CHARGEMONEY.toString(), "Geld von dem Firmenkonto abbuchen");
		RightStrings.put(Rights.MAIN_CONTROLLMACHINES.toString(), "Maschinen (Ent-)Sperren");
		RightStrings.put(Rights.MAIN_DAUERVERTRAG.toString(), "Anderen Firmen Dauervertrge anbieten");
		RightStrings.put(Rights.MAIN_FIRMINFO.toString(), "Informationen über die Firma einsehen");
		RightStrings.put(Rights.MAIN_FIRMINFORIGHTS.toString(), "Einige Informationen über die Firma ändern");
		RightStrings.put(Rights.MAIN_INVITE.toString(), "Spieler in die Firma einladen");
		RightStrings.put(Rights.MAIN_RANKDOWN.toString(), "Spieler in der Firma um eine Stufe heruntersetzen");
		RightStrings.put(Rights.MAIN_RANKUP.toString(), "Spieler in der Firma um eine Stufe hochsetzen");
		RightStrings.put(Rights.MAIN_SELLFIRM.toString(), "Firma an einen Spieler verkaufen");
		RightStrings.put(Rights.MAIN_SELLFIRMHOUSE.toString(), "Firmengelände verkaufen");
		RightStrings.put(Rights.MAIN_SETPAY.toString(), "Gehalt für die verschiedenen Stufen einstellen");
		RightStrings.put(Rights.MAIN_SETRIGHTS.toString(), "Einstellen, welcher Mitarbeiter welche Rechte hat");
		RightStrings.put(Rights.MAIN_UNINVITE.toString(), "Einen Spieler aus der Firma rauswerfen");
		RightStrings.put(Rights.MAIN_CHANGENAME.toString(), "Den Firmennamen ändern");
		RightStrings.put(Rights.MINE_BUYBUILDPLACE.toString(), "Abbaugelände für den Bergbau kaufen");
		RightStrings.put(Rights.MINE_CHANGEPRICE.toString(), "Preise für bestimmte Produkte einstellen");
		RightStrings.put(Rights.MINE_SELLPRODUCTS.toString(), "Produkte verkaufen");
		RightStrings.put(Rights.MINE_SETPRODUCTS.toString(), "Einstellen, welche Produkte zum Verkauf stehen");
		RightStrings.put(Rights.PROC_CHANGEPRICE.toString(), "Preise für bestimmte Produkte einstellen");
		RightStrings.put(Rights.PROC_SELLPRODUCTS.toString(), "Produkte verkaufen");
		RightStrings.put(Rights.PROC_SETPRODUCTS.toString(), "Einstellen, welche Produkte produziert werden");
		RightStrings.put(Rights.SELL_CHANGEPRICE.toString(), "Preise für bestimmte Produkte einstellen");
		RightStrings.put(Rights.SELL_SELLPROTUCTS.toString(), "Produkte verkaufen");
		RightStrings.put(Rights.SELL_SETPRODUCTS.toString(), "Einstellen, welche Produkte zum Verkauf stehen");
		RightStrings.put(Rights.WATE_SELLTARIF.toString(), "Tarife an Spieler anbieten");
		RightStrings.put(Rights.WATE_SELLWATER.toString(), "Wasserflaschen verkaufen");
		RightStrings.put(Rights.WATE_SETTARIFS.toString(), "Tarife einstellen");
		RightStrings.put(Rights.WATE_SETWATERPRICE.toString(), "Preis für Wasserflaschen einstellen");
		MedicRightStrings.put(MedicRights.INVITE.toString(), "Spieler in den Rettungsdienst einladen");
		MedicRightStrings.put(MedicRights.SETRIGHTS.toString(), "Einstellen, welcher Mitarbeiter welche Rechte hat");
		MedicRightStrings.put(MedicRights.UNINVITE.toString(), "Einen Spieler aus der Rettungsdienst rauswerfen");
		MedicRightStrings.put(MedicRights.SETPLAYERPAY.toString(), "Bezahlungen für einzelne Spieler einstellen");
		MedicRightStrings.put(MedicRights.SETRANKPAY.toString(), "Bezahlungen für Stufen einstellen");
		PolizeiRightStrings.put(PoliceRights.INVITE.toString(), "Spieler in die Polizei einladen");
		PolizeiRightStrings.put(PoliceRights.SETRIGHTS.toString(), "Einstellen, welcher Mitarbeiter welche Rechte hat");
		PolizeiRightStrings.put(PoliceRights.UNINVITE.toString(), "Einen Spieler aus der Polizei rauswerfen");
		PolizeiRightStrings.put(PoliceRights.SETPLAYERPAY.toString(), "Bezahlungen für einzelne Spieler einstellen");
		PolizeiRightStrings.put(PoliceRights.SETRANKPAY.toString(), "Bezahlungen für Stufen einstellen");
		PolizeiRightStrings.put(PoliceRights.EQUIP.toString(), "Mit Angeboten ausrüsten");
		PolizeiRightStrings.put(PoliceRights.EQUIPEINSTELLEN.toString(), "Ausrüstungs Preise verändern");
		PolizeiRightStrings.put(PoliceRights.AKTELESEN.toString(), "Akten einsehen");
		PolizeiRightStrings.put(PoliceRights.AKTELÖSCHEN.toString(), "Akten vernichten");
		PolizeiRightStrings.put(PoliceRights.AKTEBEARBEITEN.toString(), "Akten bearbeiten");
	}

	public void closeInvs() {
		for (Player current : Bukkit.getOnlinePlayers()) {
			current.closeInventory();
		}
	}

	public static Main getPlugin() {
		return plugin;
	}

	public FileConfiguration getLagerfeuerConfig() {
		return lagerfeuerConfig;
	}

	public void saveLagerfeuerConfig() {
		try {
			lagerfeuerConfig.save(lagerfeuerFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getMedicConfig() {
		return medicConfig;
	}

	public void saveMedicConfig() {
		try {
			medicConfig.save(medicFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getPoliceConfig() {
		return policeConfig;
	}

	public void savePoliceConfig() {
		try {
			policeConfig.save(policeFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getPlayerConfig() {
		return playerConfig;
	}

	public void savePlayerConfig() {
		try {
			playerConfig.save(playerFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getCharConfig() {
		return charConfig;
	}

	public void saveCharConfig() {
		try {
			charConfig.save(charFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getLiftConfig() {
		return liftConfig;
	}

	public void saveLiftConfig() {
		try {
			liftConfig.save(liftFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getInvConfig() {
		return invConfig;
	}

	public void saveInvConfig() {
		try {
			invConfig.save(invFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getSkinConfig() {
		return skinConfig;
	}

	public void saveSkinConfig() {
		try {
			skinConfig.save(skinFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getCarConfig() {
		return carConfig;
	}

	public void saveCarConfig() {
		try {
			carConfig.save(carFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getPoliceAktenConfig() {
		return policeAktenConfig;
	}

	public void savePoliceAktenConfig() {
		try {
			policeAktenConfig.save(policeAktenFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getBannedConfig() {
		return bannedConfig;
	}

	public void saveBannedConfig() {
		try {
			bannedConfig.save(bannedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadVanish() {
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			if (!Main.Admin.contains(p2.getUniqueId().toString())) {
				for (String p3 : Main.vanish) {
					if (Bukkit.getPlayer(UUID.fromString(p3)) != null) {
						p2.hidePlayer(Bukkit.getPlayer(UUID.fromString(p3)));
					}
				}
			}
		}
	}

	public static void UnloadVanish() {
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			if (!Main.Admin.contains(p2.getUniqueId().toString())) {
				for (String p3 : Main.vanish) {
					if (Bukkit.getPlayer(UUID.fromString(p3)) != null) {
						p2.showPlayer(Bukkit.getPlayer(UUID.fromString(p3)));
					}
				}
			}
		}
	}

	public static Inventory getWahlInv(int page) {
		int i;
		if (nominated.size() <= 45) {

			for (i = 0; i * 9 < nominated.size(); i++) {
			}

			Inventory wahlInv = Bukkit.createInventory(null, i * 9, "§eWer soll Bürgermeister werden?");

			for (String current : nominated) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(current));
				ItemStack Kopf = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta KopfM = (SkullMeta) Kopf.getItemMeta();
				KopfM.setOwner(t.getName());
				KopfM.setDisplayName("§6" + t.getName());
				Kopf.setItemMeta(KopfM);
				wahlInv.addItem(Kopf);
			}
			return wahlInv;
		} else {
			int startslot = (page - 1) * 9 * 5;
			double size = nominated.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory wahlInv = Bukkit.createInventory(null, i * 9, "§eWer soll Bürgermeister werden?");
			i = 0;
			boolean finish = false;
			for (int l = 0; l < wahlInv.getSize() - 9 && l + startslot < nominated.size(); l++) {
				OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(nominated.get(l + startslot)));
				ItemStack Kopf = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta KopfM = (SkullMeta) Kopf.getItemMeta();
				KopfM.setOwner(t.getName());
				KopfM.setDisplayName("§6" + t.getName());
				Kopf.setItemMeta(KopfM);
				wahlInv.setItem(l, Kopf);
			}
			if (page * 36 >= nominated.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					wahlInv.setItem(wahlInv.getSize() - 5, skull1);
				}
			} else {

				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				wahlInv.setItem(wahlInv.getSize() - 6, skull2);
				if (!finish) {

					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					wahlInv.setItem(wahlInv.getSize() - 4, skull1);
				}
			}
			return wahlInv;
		}

	}

	public static void clock() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (Main.tabNPC.containsKey(p.getName())) {
						List<EntityPlayer> npcs = Main.tabNPC.get(p.getName());
						EntityPlayer npc = Main.tabNPC.get(p.getName()).get(22);
						EntityPlayer npc3 = Main.tabNPC.get(p.getName()).get(59);
						PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
						PlayerInfo pi = new PlayerInfo(p);
						if (!shortAFK.contains(p)) {
							pi.addPlayTime(1);
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc3));
							if (pi.getPlayTime() >= 60) {
								int time = Math.round(pi.getPlayTime() / 60);
								String PlayTime = Integer.toString(time) + " Stunden";
								npc3.listName = new ChatMessage("§2" + PlayTime, "");
								co.sendPacket(new PacketPlayOutPlayerInfo(
										PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc3));
								npcs.set(59, npc3);
							} else {
								String PlayTime = "§20" + " Stunden";
								npc3.listName = new ChatMessage(PlayTime, "");
								co.sendPacket(new PacketPlayOutPlayerInfo(
										PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc3));
								npcs.set(59, npc3);
							}
						}
						if (Main.tabNPC.containsKey(p.getName())) {
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
							String clock;
							LocalDateTime myDateObj = LocalDateTime.now();
							DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
							String formattedDate = myDateObj.format(myFormatObj);
							clock = "         §6" + formattedDate;
							npc.listName = new ChatMessage(clock, "");
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
							npcs.set(22, npc);
						}
						Main.tabNPC.put(p.getName(), npcs);
						try {
							String name = "";
							double distance = Double.MAX_VALUE;
							for (String current : navigationsPunkte) {
								if (p.getLocation().distance(Main.naviPoints.get(current)) < distance) {
									name = current;
									distance = p.getLocation().distance(Main.naviPoints.get(current));
								}
							}
							EntityPlayer npc2 = Main.tabNPC.get(p.getName()).get(57);
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc2));
							String naviPoint = "§2" + name;
							npc2.listName = new ChatMessage(naviPoint, "");
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc2));
							npcs.set(57, npc2);
							Main.tabNPC.put(p.getName(), npcs);
						} catch (Exception e) {
							EntityPlayer npc2 = Main.tabNPC.get(p.getName()).get(57);
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc2));
							String naviPoint = "                 ";
							npc2.listName = new ChatMessage(naviPoint, "");
							co.sendPacket(new PacketPlayOutPlayerInfo(
									PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc2));
							npcs.set(57, npc2);
							Main.tabNPC.put(p.getName(), npcs);
						}
					}
				}
				
				playLightning();

			}
		}, 1200, 1200);
	}

	public static void playLightning() {
		if (!LightningLoc.isEmpty() && isLightningOn) {
			for (int il = 0; il < LightningLoc.size(); il++) {
				int delay = (int) ((Math.random() * (5 - 1)) + 1);
				int Lightning = (int) ((Math.random() * (LightningLoc.size() - 0)) + 0);
				try {
					for (int i = 0; i < 6; i++) {
						LightningLoc.get(Lightning).getWorld().strikeLightning(LightningLoc.get(Lightning));
					}
					TimeUnit.SECONDS.sleep(delay);
				} catch (InterruptedException e) {
					System.out.println("Lightning fail: " + e.toString());
				}
			}
		}
	}
	
	public static void unloadTab() {
		List<EntityPlayer> npcs = new ArrayList<EntityPlayer>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
			if (Main.tabNPC.containsKey(p.getName())) {
				npcs = Main.tabNPC.get(p.getName());
				for (EntityPlayer current : npcs) {
					co.sendPacket(new PacketPlayOutPlayerInfo(
							PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, current));
				}
			}
		}
		npcs.clear();
	}

	public static void KlickHören(Location loc, Sound s, byte distance) {

		for (Player current : Bukkit.getOnlinePlayers()) {
			if (loc.distance(current.getLocation()) <= distance) {
				current.playSound(loc, s, distance, 2);

			}

		}
	}

	public static void ject(boolean Boolean) {
		if (Boolean == true) {
			if (!Bukkit.getOnlinePlayers().isEmpty())
				for (Player p : Bukkit.getOnlinePlayers()) {
					PacketReader r = new PacketReader();
					r.inject(p);
				}
		} else if (Boolean == false) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				PacketReader r = new PacketReader();
				r.uninject(p);
				if (de.ltt.FakePlayer.NPC.getNPCs() != null && de.ltt.FakePlayer.NPC.getNPCs().size() != 0) {
					for (EntityPlayer npc : de.ltt.FakePlayer.NPC.getNPCs()) {
						de.ltt.FakePlayer.NPC.removeNPC(p, npc);
					}
				}
			}
		}
	}

	public static void loadTab(Player player) {
		Player p = (Player) player;
		PlayerInfo pi = new PlayerInfo(p);
		p.setPlayerListName(" ");
		PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				List<EntityPlayer> npcs = new ArrayList<EntityPlayer>();
				for (int i = 0; i < 80; i++) {
					MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
					WorldServer world = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
					GameProfile gP;
					gP = new GameProfile(UUID.randomUUID(), " " + i);
					EntityPlayer npc = new EntityPlayer(server, world, gP, new PlayerInteractManager(world));
					String name;
					switch (i) {
					case 0:
						name = "§6Aktueller Server:";
						break;
					case 48:
						name = "§2" + pi.getFullName();
						break;
					case 5:
						if (pi.getAge() == 0) {
							name = "                 ";
							break;
						}
						name = "§2" + pi.getAge();
						break;
					case 27:
						name = "§6MilePlay.Spigot.eu";
						break;
					case 45:
						name = "§6Spieler online:";
						break;
					case 63:
						name = "§6 " + Bukkit.getOnlinePlayers().size() + " / 50";
						break;
					case 1:
					case 28:
					case 46:
					case 64:
					case 24:
					case 42:
					case 60:
					case 79:
					case 12:
					case 15:
					case 18:
					case 20:
					case 67:
					case 7:
					case 72:
					case 75:
						name = "§e" + "================";
						break;
					case 22:
						LocalDateTime myDateObj = LocalDateTime.now();
						DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
						String formattedDate = myDateObj.format(myFormatObj);
						name = "         §6" + formattedDate;
						break;
					case 77:
						LocalDateTime myDateObj1 = LocalDateTime.now();
						DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
						String formattedDate1 = myDateObj1.format(myFormatObj1);
						name = "      §6" + formattedDate1;
						break;
					case 3:
						name = "§2Name: ";
						break;
					case 31:
						name = "§2Alter: ";
						break;
					case 33:
						name = "§2Beruf: ";
						break;
					case 35:
						name = "§2Nummer:";
						break;
					case 37:
						name = "§2Geld:";
						break;
					case 55:
						name = "§2" + pi.getMoneyInHand() + "€";
						break;
					case 53:
						String nummer = pi.getNummer();
						name = "§2" + nummer;
						break;
					case 51:
						String job;
						if (pi.getJob() == -1) {
							job = "Rettungsdienst";
						} else if (pi.getJob() == -2) {
							job = "Polizei";
						} else if (pi.getJob() != 0) {
							FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
							job = fi.getFirmname();
						} else {
							job = "Arbeitslos";
						}
						name = "§2" + job;
						break;
					case 39:
						name = "§2In der Nähe:";
						break;
					case 40:
						name = "§2Spielzeit:";
						break;
					case 59:
						if (pi.getPlayTime() >= 60) {
							int time = Math.round(pi.getPlayTime() / 60);
							name = "§2" + Integer.toString(time) + " Stunden";
						} else {
							name = "§20 Stunden";
						}
						break;
					case 10:
					case 13:
					case 16:
					case 19:
					case 25:
					case 43:
					case 61:
					case 8:
					case 65:
					case 68:
					case 70:
					case 73:
						name = "§3    Kauf dir hier";
						break;
					case 11:
					case 14:
					case 17:
					case 2:
					case 26:
					case 44:
					case 62:
					case 9:
					case 66:
					case 69:
					case 71:
					case 74:
						name = "§3   deine Werbung!";
						break;
					case 57:
						name = "                 ";
						try {
							double distance = Double.MAX_VALUE;
							for (String current : navigationsPunkte) {
								if (p.getLocation().distance(Main.naviPoints.get(current)) < distance) {
									name = "§2" + current;
									distance = p.getLocation().distance(Main.naviPoints.get(current));
								}
							}
						} catch (Exception e) {

						}
						break;
					default:
						name = "                 ";
						break;
					}
					npc.listName = new ChatMessage(name, "");
					gP.getProperties().put("textures", new Property("textures",
							"ewogICJ0aW1lc3RhbXAiIDogMTU5MTU1MTMxODY0MywKICAicHJvZmlsZUlkIiA6ICJhNDc5ZDZhMmViNDY0ZjY0OTE2YzQxZDliMDE1OWRmZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJIZXJyTGl0dCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83OWZjMWQyZDM1ZTUzZjJjNjg2ZjFmMDAxMTA0OThjY2E1NGNlNmUyMDI0YjgyM2JlYzNiOWEzMTI0NWJhYmQiCiAgICB9CiAgfQp9",
							"BFzcWUKWbKMNjRDeeEn4djgqW0IzZUIDEjN3WxO+fNrCb7HXah3kUXLAUJppTSrXbxwKGjOQfElbnLbNdotb2FHxYan9N9AvxmZyqvE2tCiW86yZghqPPx44bc6g1UYwxx8AqDgVQCy6OsO1fsCMXVjuXBrD502HPN3WK2LBRA7dpEeMOOFP2ri7bgBPCB04y2QfEMkKJq/AJRa3vtO0/38o0Gs5HKyYcEtkWZrFgh84yJmZYvRbKijMzCLg8JAlaiNt0z8TR5nK5jADkYSznnp60g1TBgB/NpUPOBHJBbRPL9ZE6pIN9sv0R8t3WdfzxpiUK46lNuBlvDUlEYgCzDa7jY4VEka4o89egllrXt89nJDu0N23J7+9gTbjhuj1oYooDnWyu8uDYvH/v8vdtRS5RID2zI4MV/hVknP1u8OvBXFHlNaaAp7P0jL9SoqbVtunMwUPV3dJvR7rN//jYQLu8hE5hsItoQCtyS0rdoltkNfpGWNH4dVMpkVCG3j2KXPLTZ6fE6w/dgks676vCuaxCdk0TwUAZoOirgCgoI3eiCWtpZfcaEMBWQXcV42MR403VswBEccGqY7ofMWduFDIhgXjB1DGRB89cHh2KQxd7C52zwqFzDj02i6MUJMd3JhNofrZ6NjeOv1XSXGt+K1NZYitdz8jYIcbDnwJHyw="));
					co.sendPacket(
							new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
					npcs.add(npc);
				}
				Main.tabNPC.put(p.getName(), npcs);
			}
		}, 40);
	}

	public static void updateTab(Player p, int slot, String name) {
		if (Main.tabNPC.containsKey(p.getName())) {
			List<EntityPlayer> npcs = Main.tabNPC.get(p.getName());
			EntityPlayer npc = Main.tabNPC.get(p.getName()).get(slot);
			PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
			co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
			npc.listName = new ChatMessage(name, "");
			co.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			npcs.set(slot, npc);
			Main.tabNPC.put(p.getName(), npcs);
		}
	}

	public static boolean checkCharName(String CheckName) {
		List<String> oC = getChars();
		for (int i = 0; i < oC.size(); i++) {
			if (CheckName.equalsIgnoreCase(oC.get(i))) {
				return true;
			}
		}
		return false;
	}

	public static Player getChar(String name) {
		for (Player current : Bukkit.getOnlinePlayers()) {
			PlayerInfo pi = new PlayerInfo(current);
			String fullName = pi.getFirstName() + "_" + pi.getLastName();
			if (fullName.equals(name))
				return current;
		}
		return null;
	}

	public static OfflinePlayer getOfflineChar(String name) {
		for (OfflinePlayer current : Bukkit.getOfflinePlayers()) {
			for (String id : PlayerInfo.getChars(current)) {
				PlayerInfo pi = new PlayerInfo(current, UUID.fromString(id));
				String fullName = pi.getFirstName() + "_" + pi.getLastName();
				if (fullName.equals(name))
					return current;
			}
		}
		return null;
	}

	public static List<String> getOnlineChars() {
		List<String> oC = new ArrayList<String>();
		for (Player current : Bukkit.getOnlinePlayers()) {
			PlayerInfo pi = new PlayerInfo(current);
			oC.add(pi.getFirstName() + "_" + pi.getLastName());
		}
		return oC;
	}

	public static List<String> getChars() {
		List<String> oC = new ArrayList<String>();
		for (Player current : Bukkit.getOnlinePlayers()) {
			List<String> chars = PlayerInfo.getChars(current);
			for (String cha : chars) {
				PlayerInfo pi = new PlayerInfo(current, UUID.fromString(cha));
				oC.add(pi.getFirstName() + "_" + pi.getLastName());
			}
		}
		return oC;
	}

	public static List<String> getAllChars() {
		List<String> oC = new ArrayList<String>();
		for (OfflinePlayer current : Bukkit.getOfflinePlayers()) {
			List<String> chars = PlayerInfo.getChars(current);
			for (String cha : chars) {
				PlayerInfo pi = new PlayerInfo(current, UUID.fromString(cha));
				oC.add(pi.getFirstName() + "_" + pi.getLastName());
			}
		}
		return oC;
	}

	public static void openCharCreate(Player p) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Erstelle einen neuen Charakter");
		inv.setItem(0, new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Vorname").setLore("max. 15 Zeichen").build());
		inv.setItem(1,
				new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Nachname").setLore("max. 15 Zeichen").build());
		inv.setItem(2, new ItemBuilder(Material.CAKE).setName("§6Geburtstag").setLore("mind. 16 Jahre").build());
		inv.setItem(3, new ItemBuilder(Material.EGG).setName("§6Geschlecht").build());
		inv.setItem(4, new ItemBuilder(
				"http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6")
						.setName("§aAkzeptieren").build());
		p.openInventory(inv);
	}

	public static void openCharInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 3 * 9, "§6Wähle einen Charakter");
		for (int i = 0; i < 3 * 9; i++) {
			inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
		}
		for (int i = 0; i < PlayerInfo.getChars(p).size(); i++) {
			PlayerInfo pi = new PlayerInfo(p, UUID.fromString(PlayerInfo.getChars(p).get(i)));
			DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String job;
			if (pi.getJob() == -1) {
				job = "Rettungsdienst";
			} else if (pi.getJob() == -2) {
				job = "Polizei";
			} else if (pi.getJob() != 0) {
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				job = fi.getFirmname();
			} else {
				job = "Arbeitslos";
			}
			inv.setItem(i, new ItemBuilder(Material.PLAYER_HEAD).setName("§6" + pi.getFullName())
					.setLore("Alter: " + pi.getAge(),
							"Geburtsdatum: " + LocalDate.parse(pi.getBirthDate()).format(myFormatObj1),
							"Nummer: " + pi.getNummer(), "Geld: " + pi.getMoneyInHand(), "Beruf: " + job)
					.build());
		}
		inv.setItem(21, new ItemBuilder(
				"http://textures.minecraft.net/texture/c3e4b533e4ba2dff7c0fa90f67e8bef36428b6cb06c45262631b0b25db85b")
						.setName("§4Einen Charakter löschen").build());
		inv.setItem(23, new ItemBuilder(
				"http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7")
						.setName("§aNeuen Charakter erstellen").build());

		p.openInventory(inv);
	}

	public static void openCharRemove(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9 * 2, "§4Einen Charakter löschen");
		for (String current : PlayerInfo.getChars(p)) {
			PlayerInfo pi = new PlayerInfo(p, UUID.fromString(current));
			DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String job;
			if (pi.getJob() == -1) {
				job = "Rettungsdienst";
			} else if (pi.getJob() == -2) {
				job = "Polizei";
			} else if (pi.getJob() != 0) {
				FirmInfo fi = new FirmInfo().loadfirm(pi.getJob());
				job = fi.getFirmname();
			} else {
				job = "Arbeitslos";
			}
			inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setName("§6" + pi.getFullName())
					.setLore("Alter: " + pi.getAge(),
							"Geburtsdatum: " + LocalDate.parse(pi.getBirthDate()).format(myFormatObj1),
							"Nummer: " + pi.getNummer(), "Geld: " + pi.getMoneyInHand(), "Beruf: " + job)
					.build());
		}
		p.openInventory(inv);
	}

	public static void fillGlass(Inventory inv) {
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build());
		}
	}

	public static boolean isNumeric(String text) {
		try {
			Integer.parseInt(text);
			Double.parseDouble(text);
			Float.parseFloat(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkABC(String text) {
		text = text.toLowerCase();
		for (Character c : text.toCharArray()) {
			if (!(c >= 'a' && c <= 'z')) {
				return false;
			}
		}
		return true;
	}

	public static String toUpperLowerCase(String text) {
		String finish = "";
		finish += Character.toUpperCase(text.charAt(0));
		for (int i = 1; i < text.length(); i++) {
			finish += Character.toLowerCase(text.charAt(i));
		}
		return finish;
	}

	public static boolean checkBannedWords(String text) {
		for (String word : BannedWords) {
			if (text.toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkPlayerName(String name) {
		for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			if (name.toLowerCase().contains(op.getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkAllChars(String name) {
		for (OfflinePlayer current : Bukkit.getOfflinePlayers()) {
			List<String> chars = PlayerInfo.getChars(current);
			for (String cha : chars) {
				PlayerInfo pi = new PlayerInfo(current, UUID.fromString(cha));
				if (name.toLowerCase().contains(pi.getFullName().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

}

/*
 * 00001111111000001111111100000 00000000000110011000000000000
 * 00000000000110011000000000000 00000000000110011000000000000
 * 00001111111100011111111100000 00000000000110011000001100000
 * 00000000000110011000001100000 00000000000110011000001100000
 * 00001111111100011111111100000
 */
