package de.ltt.server.reflaction;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.ltt.FakePlayer.NPC;
import de.ltt.other.InvSpeichern.Serialization;
import de.ltt.server.main.Main;
import de.ltt.server.mySQL.SQLData;
import de.ltt.staat.medic.MedicInfo;
import de.ltt.staat.medic.violations.Violation;
import de.ltt.staat.police.grundSystem.PoliceInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_15_R1.Vector3f;
import net.minecraft.server.v1_15_R1.WorldServer;

public class PlayerInfo {

	private static HashMap<OfflinePlayer, Boolean> haspackedMap = new HashMap<OfflinePlayer, Boolean>();
	private static HashMap<OfflinePlayer, Boolean> ispackedMap = new HashMap<OfflinePlayer, Boolean>();
	private static HashMap<OfflinePlayer, Boolean> isstunnedMap = new HashMap<OfflinePlayer, Boolean>();
	private static HashMap<OfflinePlayer, Boolean> canJumpMap = new HashMap<OfflinePlayer, Boolean>();
	private static HashMap<OfflinePlayer, Boolean> isSittingMap = new HashMap<OfflinePlayer, Boolean>();
	private static HashMap<OfflinePlayer, EntityArmorStand> routeMap = new HashMap<OfflinePlayer, EntityArmorStand>();
	private static HashMap<OfflinePlayer, Integer> activeRoutes = new HashMap<OfflinePlayer, Integer>();
	private static HashMap<UUID, List<Violation>> violationMap = new HashMap<UUID, List<Violation>>();

	private OfflinePlayer p;
	private double money;
	private double moneyInHand;
	private int job;
	private int guthaben;
	private int PlayTime;
	private String nummer;
	private String staatsangehörigkeit;
	private String firstName;
	private String lastName;
	private boolean haspacked;
	private boolean ispacked;
	private boolean isstunned;
	private boolean canJump;
	private boolean isvorbestraft;
	private boolean isSitting;
	private boolean isBauamt;
	private boolean mailBoxJoin;
	private String bankCard;
	private String keyCode;
	private UUID uuid;
	private Gender gender;
	private String birthDate;

	private List<String> smsAutor = new ArrayList<String>();
	private List<String> smsNachricht = new ArrayList<String>();
	private List<String> smsTitle = new ArrayList<String>();
	private List<String> contacts = new ArrayList<String>();
	private List<Violation> violations = new ArrayList<Violation>();

	private List<String> AktenAutor = new ArrayList<String>();
	private List<String> AktenNachricht = new ArrayList<String>();
	private List<String> AktenTitleUUID = new ArrayList<String>();

	private List<String> mailBox = new ArrayList<String>();
	private List<String> transactions = new ArrayList<String>();
	private List<String> skinname = new ArrayList<String>();
	private List<ItemStack> CharInv = new ArrayList<ItemStack>();
	private HashMap<Player, Location> lastSkinLoc = new HashMap<Player, Location>();

	public PlayerInfo(OfflinePlayer p) {
		this.p = p;
		uuid = PlayerInfo.getUUID(p);
	}

	public PlayerInfo(OfflinePlayer p, UUID uuid) {
		this.p = p;
		this.uuid = uuid;
	}

	public double getMoney() {
		money = Main.getPlugin().getCharConfig().getDouble("Spieler." + uuid + ".money");
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".money", money);
		Main.getPlugin().saveCharConfig();
	}

	public void addMoney(double plusMoney) {
		money += plusMoney;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".money", money);
		Main.getPlugin().saveCharConfig();
	}

	public void subtractMoney(double minusMoney) {
		money = -minusMoney;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".money", money);
		Main.getPlugin().saveCharConfig();
	}

	public double getMoneyInHand() {
		moneyInHand = Main.getPlugin().getCharConfig().getDouble("Spieler." + uuid + ".moneyInHand");
		return moneyInHand;
	}

	public void setMoneyInHand(double moneyInHand) {
		this.moneyInHand = moneyInHand;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".moneyInHand", moneyInHand);
		Main.getPlugin().saveCharConfig();
		Main.updateTab(p.getPlayer(), 55, "§2" + moneyInHand + "€");
	}

	public void addMoneyInHand(double plusMoney) {
		setMoneyInHand(getMoneyInHand() + plusMoney);
	}

	public void subtractMoneyInHand(double minusMoney) {
		setMoneyInHand(getMoneyInHand() - minusMoney);
	}

	public int getJob() {
		job = Main.getPlugin().getCharConfig().getInt("Spieler." + uuid + ".job");
		return job;
	}

	public void setJob(int job) {
		this.job = job;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".job", job);
		Main.getPlugin().saveCharConfig();
		String jobs;
		if (job == -1) {
			jobs = "§2Rettungsdienst";
		} else if (job == -2) {
			jobs = "§2Polizei";
		} else if (job != 0) {
			FirmInfo fi = new FirmInfo().loadfirm(job);
			jobs = "§2" + fi.getFirmname();
		} else {
			jobs = "§2Arbeitslos";
		}
		Main.updateTab(p.getPlayer(), 51, jobs);
	}

	public String getStaatsangehörogkeit() {
		staatsangehörigkeit = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".Staatsangehörigkeit");
		return staatsangehörigkeit;
	}

	public void setStaatsangehörogkeit(String staatsangehörigkeit) {
		this.staatsangehörigkeit = staatsangehörigkeit;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".Staatsangehörigkeit", staatsangehörigkeit);
		Main.getPlugin().saveConfig();
	}

	public String getFirstName() {
		firstName = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".firstName");
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".firstName", firstName);
		Main.getPlugin().saveCharConfig();
		Main.updateTab(p.getPlayer(), 48, getFullName());
	}

	public String getLastName() {
		lastName = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".lastName");
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".lastName", lastName);
		Main.getPlugin().saveCharConfig();
		Main.updateTab(p.getPlayer(), 48, "§2" + getFullName());
	}

	public String getFullName() {
		String FullName = getFirstName() + " " + getLastName();
		return FullName;
	}

	public int getAge() {
		if (getBirthDate() == null || getBirthDate() == "")
			return 0;
		LocalDate bdDate = LocalDate.parse(getBirthDate());
		LocalDate cD = LocalDate.now();
		int age = Period.between(bdDate, cD).getYears();
		return age;
	}

	public void removeJob() {
		setJob(0);
	}

	public boolean haspacked() {
		if (haspackedMap.containsKey(p)) {
			haspacked = haspackedMap.get(p);
		} else
			haspacked = false;
		haspackedMap.put(p, haspacked);
		return haspacked;
	}

	public boolean ispacked() {
		if (ispackedMap.containsKey(p)) {
			ispacked = ispackedMap.get(p);
		} else
			ispacked = false;
		ispackedMap.put(p, ispacked);
		return ispacked;
	}

	public boolean isstunned() {
		if (isstunnedMap.containsKey(p)) {
			isstunned = isstunnedMap.get(p);
		} else
			isstunned = false;
		isstunnedMap.put(p, isstunned);
		return isstunned;
	}

	public boolean canJump() {
		if (canJumpMap.containsKey(p)) {
			canJump = canJumpMap.get(p);
		} else
			canJump = true;
		canJumpMap.put(p, canJump);
		return canJump;
	}

	public void sethaspacked(boolean haspacked) {
		this.haspacked = haspacked;
		haspackedMap.put(p, haspacked);
	}

	public void setispacked(boolean ispacked) {
		this.ispacked = ispacked;
		ispackedMap.put(p, ispacked);
	}

	public void setisstunned(boolean isstunned) {
		this.isstunned = isstunned;
		isstunnedMap.put(p, isstunned);
	}

	public void setcanJump(boolean canJump) {
		this.canJump = canJump;
		canJumpMap.put(p, canJump);
	}

	public void addMessage(UUID sendID, String message) {
		smsAutor = getSmsAutor();
		smsNachricht = getSmsNachricht();
		smsTitle = getSmsTitle();
		Player s = Bukkit.getPlayer(sendID);
		if (smsAutor.size() == 54) {
			smsAutor.remove(1);
			smsNachricht.remove(1);
			smsTitle.remove(1);
		}

		smsAutor.add(s.getName());
		smsNachricht.add(message);
		String title = "";
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		String formattedDate = myDateObj.format(myFormatObj);
		int nummer = Main.PlayerNummer.get(sendID.toString());
		title = "" + formattedDate + " " + nummer;
		smsTitle.add(title);
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".sms.autors", smsAutor);
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".sms.messages", smsNachricht);
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".sms.titels", smsTitle);
		Main.getPlugin().saveConfig();
	}

	public List<String> getContacts() {
		contacts = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid + ".contacts");
		return contacts;
	}

	public Inventory getSMSInv() {
		smsAutor = getSmsAutor();
		smsNachricht = getSmsNachricht();
		smsTitle = getSmsTitle();
		int i;
		for (i = 0; i * 9 < smsAutor.size(); i++) {

		}
		Inventory SMSInv = Bukkit.createInventory(null, i * 9, "§eErhaltene SMS: " + smsAutor.size());

		for (int l = 0; l < smsAutor.size(); l++) {

			List<String> pages = new ArrayList<String>();
			pages.add("§6===================\n§6Datum: §6\n" + smsTitle.get(l) + "\n\n§6Absender: \n§6"
					+ smsAutor.get(l) + "\n===================");
			pages.add("§6Nachricht: " + smsNachricht.get(l));
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
			BookMeta meta = (BookMeta) book.getItemMeta();
			meta.setPages(pages);
			meta.setAuthor(smsAutor.get(l));
			meta.setTitle("§e" + smsTitle.get(l));
			book.setItemMeta(meta);

			SMSInv.addItem(book);
		}
		return SMSInv;
	}

	public List<String> getSmsAutor() {
		smsAutor = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid + ".sms.autors");
		return smsAutor;
	}

	public List<String> getSmsNachricht() {
		smsNachricht = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid + ".sms.messages");
		return smsNachricht;
	}

	public List<String> getSmsTitle() {
		smsTitle = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid + ".sms.titels");
		return smsTitle;
	}

	public Inventory getKontaktbuch(String name) {
		contacts = getContacts();
		int i;
		for (i = 0; i * 9 < contacts.size(); i++) {

		}
		int slot = i * 9 + 4;
		i++;

		Inventory InvKontaktBuch = Bukkit.createInventory(null, i * 9, name);

		for (String current : contacts) {
			OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(current));
			ItemStack Kopf = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta KopfM = (SkullMeta) Kopf.getItemMeta();
			KopfM.setOwner(t.getName());
			KopfM.setDisplayName("§6" + t.getName());
			KopfM.setLore(Arrays.asList(Main.PlayerNummer.get(t.getUniqueId().toString()).toString()));
			Kopf.setItemMeta(KopfM);
			InvKontaktBuch.addItem(Kopf);
		}

		ItemStack add = ItemSkulls.getSkull(
				"http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7");
		SkullMeta addM = (SkullMeta) add.getItemMeta();
		addM.setDisplayName("§aKontakt hinzufügen");
		add.setItemMeta(addM);
		add.setAmount(1);
		InvKontaktBuch.setItem(slot, add);
		// Glas!

		return InvKontaktBuch;
	}

	public void addKontakt(UUID id) {
		contacts = getContacts();
		contacts.add(id.toString());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".contacts", contacts);
		Main.getPlugin().saveCharConfig();
	}

	public void removeKontakt(UUID id) {
		contacts = getContacts();
		contacts.remove(id.toString());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".contacts", contacts);
		Main.getPlugin().saveCharConfig();
	}

	public int getGuthaben() {
		guthaben = Main.getPlugin().getCharConfig().getInt("Handy.Spieler." + uuid + ".guthaben");
		return guthaben;
	}

	public void setGuthaben(int guthaben) {
		this.guthaben = guthaben;
		Main.getPlugin().getCharConfig().set("Handy.Spieler." + uuid + ".guthaben", guthaben);
		Main.getPlugin().saveCharConfig();
	}

	public void subtractGuthaben(int minusguthaben) {
		guthaben = getGuthaben();
		guthaben = guthaben - minusguthaben;
		Main.getPlugin().getCharConfig().set("Handy.Spieler." + uuid + ".guthaben", guthaben);
		Main.getPlugin().saveConfig();
	}

	public void addierenGuthaben(int plusguthaben) {
		guthaben = getGuthaben();
		guthaben = guthaben + plusguthaben;
		Main.getPlugin().getCharConfig().set("Handy.Spieler." + uuid + ".guthaben", guthaben);
		Main.getPlugin().saveCharConfig();
	}

	public boolean isvorbestraft() {
		// TODO
		return isvorbestraft;
	}

	public boolean isSitting() {
		if (isSittingMap.containsKey(p)) {
			isSitting = isSittingMap.get(p);
		} else
			isSitting = false;
		isSittingMap.put(p, isSitting);
		return isSitting;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
		isSittingMap.put(p, isSitting);
	}

	public void addToMailBox(String message) {
		/*
		 * mailBox = getMailBox(); mailBox.add(message);
		 * Main.getPlugin().getConfig().set("Player." + uuid + ".mailBox", mailBox);
		 * Main.getPlugin().saveConfig();TODO:
		 */
	}

	public List<String> getMailBox() {
		mailBox = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid + ".mailBox");
		return mailBox;
		// TODO:
	}

	public void setMailBox(List<String> mailBox) {
		this.mailBox = mailBox;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".mailBox", mailBox);
		Main.getPlugin().saveCharConfig();
	}

	public void clearMailBox() {
		mailBox.clear();
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".mailBox", mailBox);
		Main.getPlugin().saveCharConfig();
	}

	public boolean isBauamt() {
		isBauamt = Main.getPlugin().getCharConfig().getBoolean("Spieler." + uuid.toString() + ".isBauamt");
		return isBauamt;
	}

	public void setBauamt(boolean isBauamt) {
		this.isBauamt = isBauamt;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid.toString() + ".isBauamt", isBauamt);
		Main.getPlugin().saveCharConfig();
	}

	public void runRoute(Location route) {
		if (p.isOnline()) {
			stopRoute();
			Location ploc = p.getPlayer().getLocation();
			ploc.setPitch(0);
			Location loc = ploc.toVector().add(ploc.getDirection().multiply(4).setY(0))
					.toLocation(p.getPlayer().getWorld());
			loc.subtract(0, 0.75, 0);
			loc.setPitch(0);
			Vector direction = route.toVector().subtract(loc.toVector());
			loc.setDirection(direction);
			EntityArmorStand stand = spawnEntity(loc);
			stand.setHeadPose(new Vector3f(loc.getPitch(), loc.getYaw(), 0));
			stand.setInvisible(true);
			ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(10000024);
			item.setItemMeta(meta);
			routeMap.put(p, stand);

			loc.setDirection(direction);
			stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
			stand.setHeadPose(new Vector3f(loc.getPitch(), loc.getYaw(), 0));
			stand.setInvisible(true);

			p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent("§aZiel in §6" + ((int) route.distance(ploc)) + "m§a erreicht"));

			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			PacketPlayOutEntityMetadata data = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(),
					true);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(data);
			PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(stand.getId(), EnumItemSlot.HEAD,
					CraftItemStack.asNMSCopy(item));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(equip);

			int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					Location ploc = p.getPlayer().getLocation();
					ploc.setPitch(0);
					Location loc;
					if (p.getPlayer().getVehicle() != null) {
						loc = ploc.toVector().add(ploc.getDirection().multiply(5).setY(0))
								.toLocation(p.getPlayer().getWorld());
					} else {
						loc = ploc.toVector().add(ploc.getDirection().multiply(3).setY(0))
								.toLocation(p.getPlayer().getWorld());
					}
					loc.subtract(0, 0.75, 0);
					if (loc.distance(new Location(loc.getWorld(), stand.locX(), stand.locY(), stand.locZ())) != 0) {
						if (route.distance(ploc) >= 2) {
							Vector direction = route.toVector().subtract(loc.toVector());
							loc.setDirection(direction);
							stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
							stand.setHeadPose(new Vector3f(loc.getPitch(), loc.getYaw(), 0));
							stand.setInvisible(true);

							p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
									new TextComponent("§aZiel in §6" + ((int) route.distance(ploc)) + "m§a erreicht"));

							PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(stand);
							((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
							PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(stand.getId(),
									stand.getDataWatcher(), true);
							((CraftPlayer) p).getHandle().playerConnection.sendPacket(meta);
						} else {
							stopRoute();
							p.getPlayer().sendMessage("§6Du hast dein Ziel erreicht!");
						}
					}
				}
			}, 0, 1);
			activeRoutes.put(p, taskID);
		}
	}

	public boolean isActiveRoute() {
		if (activeRoutes.containsKey(p))
			return true;
		return false;
	}

	public void stopRoute() {
		if (routeMap.containsKey(p)) {
			EntityArmorStand stand = routeMap.get(p);
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			routeMap.remove(p);
			Bukkit.getScheduler().cancelTask(activeRoutes.get(p));
			activeRoutes.remove(p);
		}
	}

	public boolean getMailBoxJoin() {
		return mailBoxJoin;
	}

	public void setMailBoxJoin(boolean mailBoxJoin) {
		this.mailBoxJoin = mailBoxJoin;
	}

	private EntityArmorStand spawnEntity(Location loc) {
		ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(10000024);
		item.setItemMeta(meta);
		WorldServer s = ((CraftWorld) loc.getWorld()).getHandle();
		EntityArmorStand stand = new EntityArmorStand(s, loc.getX(), loc.getY(), loc.getZ());
		stand.setCustomNameVisible(false);
		stand.setNoGravity(true);
		stand.setInvisible(true);
		stand.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item));

		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		PacketPlayOutEntityMetadata data = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(data);
		PacketPlayOutEntityEquipment equip = new PacketPlayOutEntityEquipment(stand.getId(), EnumItemSlot.HEAD,
				CraftItemStack.asNMSCopy(item));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(equip);
		return stand;
	}

	public List<String> getTransactions() {
		transactions = Main.getPlugin().getCharConfig().getStringList("Spieler." + uuid.toString() + ".transactions");
		return transactions;
	}

	public void setTransctions(List<String> transactions) {
		this.transactions = transactions;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid.toString() + ".transactions", transactions);
		Main.getPlugin().saveCharConfig();
	}

	public void addTransaction(String transaction) {
		transactions = getTransactions();
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		String formattedDate = myDateObj.format(myFormatObj);
		transaction += " " + formattedDate + "\n§7===================";
		transactions.add(transaction);
		Main.getPlugin().getCharConfig().set("Spieler." + uuid.toString() + ".transactions", transactions);
		Main.getPlugin().saveCharConfig();
	}

	public String getNummer() {
		nummer = "§cKeine Nummer";
		if (Main.PlayerNummer.containsKey(uuid.toString())) {
			Bukkit.getPlayer("HerrLitt").sendMessage(nummer);
			nummer = Main.PlayerNummer.get(uuid.toString()).toString();
			Bukkit.getPlayer("HerrLitt").sendMessage(nummer);
		}
		return nummer;
	}

	public void setNummer(int nummer) {
		SQLData.addNummer(uuid.toString(), nummer);
		String Nummer = "§2" + Main.PlayerNummer.get(uuid.toString()).toString();
		Main.updateTab(p.getPlayer(), 53, Nummer);
	}

	public void removeNummer(Player t) {
		SQLData.removeNummer(t.getUniqueId().toString());
		String Nummer = "§cKeine Nummer";
		Main.updateTab(p.getPlayer(), 53, Nummer);
	}

	public Player getNummerPlayer(String nummer) {
		Player p = null;
		if (Main.NummerPlayer.containsKey(nummer)) {
			p = Bukkit.getPlayer(Main.NummerPlayer.get(nummer));
		}
		return p;
	}

	public int getPlayTime() {
		PlayTime = Main.getPlugin().getPlayerConfig().getInt("Player." + p.getUniqueId().toString() + ".PlayTime");
		return PlayTime;
	}

	public void resetPlayerTime() {
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId().toString() + ".PlayTime", 0);
		Main.getPlugin().savePlayerConfig();
	}

	public void addPlayTime(int addTime) {
		PlayTime = getPlayTime();
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId().toString() + ".PlayTime",
				PlayTime + addTime);
		Main.getPlugin().savePlayerConfig();
	}

	public void addViolation(Violation violation) {
		violations = getViolations();
		violations.add(violation);
		violationMap.put(uuid, violations);
	}

	public void removeViolation(int index) {
		violations = getViolations();
		violations.remove(index);
		violationMap.put(uuid, violations);
	}

	public List<Violation> getViolations() {
		if (violationMap.containsKey(uuid)) {
			violations = violationMap.get(uuid);
		}
		violationMap.put(uuid, violations);
		return violations;
	}

	public String getBankCard() {
		bankCard = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".bankCard");
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".bankCard", bankCard);
		Main.getPlugin().saveCharConfig();
		this.bankCard = bankCard;
	}

	public String getKeyCode() {
		keyCode = Main.getPlugin().getCharConfig().getString("Spieler." + uuid.toString() + ".keyCode");
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		Main.getPlugin().getCharConfig().set("Spieler." + uuid.toString() + ".keyCode", keyCode);
		Main.getPlugin().saveCharConfig();
		this.keyCode = keyCode;
	}

	public void death() {
		// TODO: HANDLE DEATH
	}

	public void killChar(String uuid) {
		List<String> chars = getChars(p);
		chars.remove(uuid);
		PlayerInfo pi = new PlayerInfo(p, UUID.fromString(uuid));
		if (pi.getJob() != 0) {
			if (pi.getJob() == -1) {
				new MedicInfo().removeMember(p);
			} else if (pi.getJob() == -2) {
				new PoliceInfo().removeMember(p);
			} else {
				new FirmInfo().loadfirm(pi.getJob()).removeMember(p);
			}
		}
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId() + ".chars", chars);
		Main.getPlugin().getCharConfig().set("Spieler." + uuid, null);
		Main.getPlugin().savePlayerConfig();
		Main.getPlugin().saveCharConfig();
		if (getUUID(p).toString().equals(uuid)) {
			if (chars.size() != 0) {
				changeChar(UUID.fromString(chars.get(0)));
				p.getPlayer().sendMessage(
						"§eDa du deinen aktuellen Charakter gelöscht hast, hast du zu einem anderen gewechselt!");
				loadInv();
			}
		}
	}

	public void changeChar(UUID uuid) {
		this.uuid = uuid;
		setMoneyInHand(getMoneyInHand());
		if (getNummer().equals("§cKeine Nummer")) {
			removeNummer(p.getPlayer());
		} else {
			setNummer(Integer.parseInt(getNummer()));
		}
		setJob(getJob());
		setFirstName(getFirstName());
		setLastName(getLastName());
		setBirthDate(getBirthDate());
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId().toString() + ".uuid", uuid.toString());
		Main.getPlugin().savePlayerConfig();
	}

	public Gender getGender() {
		gender = Gender.valueOf(Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".gender"));
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".gender", gender.toString());
		Main.getPlugin().saveCharConfig();
	}

	public String getBirthDate() {
		birthDate = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".birthDate");
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".birthDate", birthDate);
		Main.getPlugin().saveCharConfig();
		Main.updateTab(p.getPlayer(), 5, "§2" + getAge());
	}

	public static UUID getUUID(OfflinePlayer p) {
		String uuid = Main.getPlugin().getPlayerConfig().getString("Player." + p.getUniqueId().toString() + ".uuid");
		if (uuid != "" && uuid != null)
			return UUID.fromString(uuid);
		return null;
	}

	public static List<String> getChars(OfflinePlayer p) {
		return Main.getPlugin().getPlayerConfig().getStringList("Player." + p.getUniqueId() + ".chars");
	}

	public static void addChar(OfflinePlayer p, String firstName, String lastName, String birthDate, Gender gender) {
		UUID uuid = UUID.randomUUID();
		List<String> uuids = PlayerInfo.getChars(p);
		uuids.add(uuid.toString());
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId() + ".chars", uuids);
		Main.getPlugin().getPlayerConfig().set("Player." + p.getUniqueId() + ".uuid", uuid.toString());
		Main.getPlugin().savePlayerConfig();
		PlayerInfo pi = new PlayerInfo(p);
		pi.setFirstName(firstName);
		pi.setLastName(lastName);
		pi.setBirthDate(birthDate);
		pi.setGender(gender);
		pi.setMoney(1000);
		pi.setMoneyInHand(0);
	}

	private void removeFromTab(CraftPlayer cp) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
				cp.getHandle());
		sendPacket(packet);
	}

	private void addToTab(CraftPlayer cp) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
		sendPacket(packet);
	}

	private void sendPacket(Packet<?> packet) {
		for (Player current : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) current).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}

	}

	public void loadInv() {
		if (Main.getPlugin().getInvConfig().getString("Spieler." + uuid + ".inv.items") != null) {
			p.getPlayer().closeInventory();
			String content = Main.getPlugin().getInvConfig().getString("Spieler." + uuid + ".inv.items");
			String armor = Main.getPlugin().getInvConfig().getString("Spieler." + uuid + ".inv.armor");
			String[] values = new String[] { content, armor };
			ItemStack[][] items = Serialization.base64ToInv(values);

			p.getPlayer().getInventory().clear();
			p.getPlayer().getInventory().setContents(items[0]);
			p.getPlayer().getInventory().setArmorContents(items[1]);
		}
	}

	public void saveInv() {
		p.getPlayer().closeInventory();
		String[] values = Serialization.invToBase64(p.getPlayer().getInventory());
		Main.getPlugin().getInvConfig().set("Spieler." + uuid + ".inv.items", values[0]);
		Main.getPlugin().getInvConfig().set("Spieler." + uuid + ".inv.armor", values[1]);
		Main.getPlugin().saveInvConfig();
	}

	public void loadAktuellSkin() {
		saveAktuellSkin();
		String texture = Main.getPlugin().getSkinConfig().getString("Spieler." + uuid + ".aktuell.texture");
		String signature = Main.getPlugin().getSkinConfig().getString("Spieler." + uuid + ".aktuell.signature");
		changeSkin(texture, signature);
		p.getPlayer().sendMessage("LoadAktuellSkin");
	}

	public void loadLastLocation() {
		if (Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".location.x") != null) {
			String world = Main.getPlugin().getCharConfig().getString("Spieler." + uuid + ".location.world");
			double x = Main.getPlugin().getCharConfig().getDouble("Spieler." + uuid + ".location.x");
			double y = Main.getPlugin().getCharConfig().getDouble("Spieler." + uuid + ".location.y");
			double z = Main.getPlugin().getCharConfig().getDouble("Spieler." + uuid + ".location.z");
			float pitch = Main.getPlugin().getCharConfig().getInt("Spieler." + uuid + ".location.pitch");
			float yaw = Main.getPlugin().getCharConfig().getInt("Spieler." + uuid + ".location.yaw");
			Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			new BukkitRunnable() {
				@Override
				public void run() {
					Player p2 = Bukkit.getPlayer(p.getName());
					p2.teleport(loc, TeleportCause.PLUGIN);
					p2.sendMessage("loadLastLocation" + " || " + uuid + " || " + loc + " || " + p.getName());
				}
			}.runTaskLater(Main.getPlugin(), 20);
		}

	}

	public void saveLastLocation() {
		// if (Main.getPlugin().getCharConfig().contains("Spieler." + uuid +
		// ".location.world")) {
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.world",
				p.getPlayer().getLocation().getWorld().getName());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.x", p.getPlayer().getLocation().getX());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.y", p.getPlayer().getLocation().getY());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.z", p.getPlayer().getLocation().getZ());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.pitch",
				p.getPlayer().getLocation().getPitch());
		Main.getPlugin().getCharConfig().set("Spieler." + uuid + ".location.yaw", p.getPlayer().getLocation().getYaw());
		Main.getPlugin().saveCharConfig();
		p.getPlayer().sendMessage("saveLastLocation");
		// }
	}

	public List<String> getSkinName() {
		skinname = Main.getPlugin().getSkinConfig().getStringList("Spieler." + uuid.toString() + ".name");
		return skinname;
	}

	public void saveAktuellSkin() {
		String[] skins = NPC.getSkin(p.getPlayer(), p.getPlayer().getName());
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.texture", skins[0]);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.signature", skins[1]);
		Main.getPlugin().saveSkinConfig();
	}

	public void saveSkin(String SkinName) {
		skinname = getSkinName();
		skinname.add(SkinName);
		String[] skins = NPC.getSkin(p.getPlayer(), p.getPlayer().getName());
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".skin." + SkinName + ".texture", skins[0]);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".skin." + SkinName + ".signature", skins[1]);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".name", skinname);
		Main.getPlugin().saveSkinConfig();
	}

	public void removeSkin(String SkinName) {
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".skin." + SkinName, null);
		skinname = getSkinName();
		skinname.remove(SkinName);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".name", skinname);
		Main.getPlugin().saveSkinConfig();
	}

	public void changeSkin(String skin) {
		GameProfile gp = ((CraftPlayer) p).getProfile();
		CraftPlayer cp = ((CraftPlayer) p);
		String[] skins = NPC.getSkin(p.getPlayer(), skin);
		gp.getProperties().removeAll("textures");
		gp.getProperties().put("textures", new Property("textures", skins[0], skins[1]));
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.texture", skins[0]);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.signature", skins[1]);
		Main.getPlugin().saveSkinConfig();
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
		PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacket(info);
		lastSkinLoc.put(p.getPlayer(), p.getPlayer().getLocation());
		p.getPlayer().setHealth(0);

		new BukkitRunnable() {

			@Override
			public void run() {
				cp.spigot().respawn();
				p.getPlayer().teleport(lastSkinLoc.get(p.getPlayer()));

				PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
						cp.getHandle());
				sendPacket(tab);

				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!all.getName().equals(cp.getName()))
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
					all.hidePlayer(p.getPlayer());
					all.showPlayer(p.getPlayer());
				}
				p.getPlayer().sendMessage("ChangeSkin");

			}
		}.runTaskLater(Main.getPlugin(), 4);
	}

	public void changeSkin(String texture, String signature) {
		GameProfile gp = ((CraftPlayer) p).getProfile();
		CraftPlayer cp = ((CraftPlayer) p);
		gp.getProperties().removeAll("textures");
		gp.getProperties().put("textures", new Property("textures", texture, signature));
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.texture", texture);
		Main.getPlugin().getSkinConfig().set("Spieler." + uuid + ".aktuell.signature", signature);
		Main.getPlugin().saveSkinConfig();
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
		PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacket(info);
		lastSkinLoc.put(p.getPlayer(), p.getPlayer().getLocation());
		p.getPlayer().setHealth(0);

		new BukkitRunnable() {

			@Override
			public void run() {
				cp.spigot().respawn();
				p.getPlayer().teleport(lastSkinLoc.get(p.getPlayer()));

				PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
						cp.getHandle());
				sendPacket(tab);

				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!all.getName().equals(cp.getName()))
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
					all.hidePlayer(p.getPlayer());
					all.showPlayer(p.getPlayer());
				}
			}
		}.runTaskLater(Main.getPlugin(), 4);
	}

	public void sendChangeSkinPacket(Packet packet) {
		for (Player op : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) op).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void addAkte(List<String> pages, String CharNameFromCreater, String AktenTitle) {
		AktenTitleUUID = getAktenUUID();
		UUID uuids = UUID.randomUUID();
		AktenTitleUUID.add(uuid.toString());
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String formattedDate = myDateObj.format(myFormatObj);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten." + uuids + ".autor",
				CharNameFromCreater);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten." + uuids + ".nachricht",
				pages);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten." + uuids + ".aktenTitel",
				AktenTitle);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten." + uuids + "aktenLore",
				formattedDate);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten.aktenUUIDs", AktenTitleUUID);
		Main.getPlugin().savePoliceAktenConfig();
	}

	public void removeAkte(String UUID) {
		AktenTitleUUID = getAktenUUID();
		AktenTitleUUID.remove(UUID);
		Main.getPlugin().getPoliceAktenConfig().set("Spieler." + uuid.toString() + ".akten." + UUID, null);
		Main.getPlugin().savePoliceAktenConfig();
	}

	public Inventory openAkteInv(int page) {
		AktenTitleUUID = getAktenUUID();
		int i;
		if (AktenTitleUUID.size() <= 45) {
			for (i = 0; i * 9 < AktenTitleUUID.size(); i++) {
			}

			Inventory wahlInv = Bukkit.createInventory(null, (i * 9) + 18, "§1Akten von " + getFullName());

			for (int j = 0; j <= 8; j++) {
				if (j == 7) {
					ItemStack Suche = new ItemBuilder(Material.LEVER).setName("§eSuche").build();
					wahlInv.setItem(7, Suche);
				} else if (j == 6) {
					ItemStack Filter = new ItemBuilder(Material.HOPPER).setName("§eFilter").build();
					wahlInv.setItem(6, Filter);
				} else if (j != 7 || j != 6) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				}
			}

			for (String current : AktenTitleUUID) {
				ItemStack Akte = new ItemStack(Material.BOOK);
				BookMeta AkteM = (BookMeta) Akte.getItemMeta();
				AkteM.setTitle(getAktenTitle(current));
				AkteM.setAuthor(getAktenAutorName(current));
				AkteM.setPages(getAktenNachricht(current));
				Akte.setItemMeta(AkteM);
				Akte = new ItemBuilder(Akte).setTag("UUID", current).setLore(getAktenLore(current)).build();
				wahlInv.addItem(Akte);
			}

			for (int j = (i * 9 + 18) - 9; j < (i * 9 + 18); j++) {
				if (j != (i * 9 + 18) - 1) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				} else {
					ItemStack Glas = new ItemBuilder(Material.GREEN_CONCRETE).setName("§aAkte hinzufügen").build();
					wahlInv.setItem(j, Glas);
				}
			}
			return wahlInv;
		} else {
			int startslot = ((page - 1) * 9 * 5) + 18;
			double size = AktenTitleUUID.size() - startslot;
			for (i = 0; i * 9 < size && i < 5; i++) {
			}
			i++;
			Inventory wahlInv = Bukkit.createInventory(null, i * 9, "§1Akten von " + getFullName());
			i = 0;
			boolean finish = false;
			for (int l = 0; l < wahlInv.getSize() - 9 && l + startslot < AktenTitleUUID.size(); l++) {
				ItemStack Akte = new ItemStack(Material.BOOK);
				BookMeta AkteM = (BookMeta) Akte.getItemMeta();
				AkteM.setTitle(getAktenTitle(AktenTitleUUID.get(l + startslot)));
				AkteM.setAuthor(getAktenAutorName(AktenTitleUUID.get(l + startslot)));
				AkteM.setPages(getAktenNachricht(AktenTitleUUID.get(l + startslot)));
				Akte.setItemMeta(AkteM);
				Akte = new ItemBuilder(Akte).setTag("UUID", AktenTitleUUID.get(l + startslot))
						.setLore(getAktenLore(AktenTitleUUID.get(l + startslot))).build();
				wahlInv.setItem(l, Akte);
			}
			for (int j = (i * 9 + 18) - 9; j < (i * 9 + 18); j++) {
				if (j != (i * 9 + 18) - 7) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				} else {
					ItemStack Glas = new ItemBuilder(Material.GREEN_CONCRETE).setName("§aAkte hinzufügen").build();
					wahlInv.setItem(j, Glas);
				}
			}
			if (page * 36 >= AktenTitleUUID.size())
				finish = true;
			if (page == 1) {
				if (!finish) {

					for (int j = 0; j <= 8; j++) {
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
						wahlInv.setItem(j, Glas);
					}
					ItemStack skull1 = ItemSkulls.getSkull(
							"http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
					ItemMeta skull1Meta = skull1.getItemMeta();
					skull1Meta.setDisplayName("§7Nächste Seite");
					skull1.setItemMeta(skull1Meta);
					skull1.setAmount(1);
					wahlInv.setItem(wahlInv.getSize() - 5, skull1);

					for (int j = 0; j < 8; j++) {
						ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
						wahlInv.setItem(j, Glas);
					}
				}
			} else {

				for (int j = 0; j <= 8; j++) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
					wahlInv.setItem(j, Glas);
				}
				ItemStack skull2 = ItemSkulls.getSkull(
						"http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
				ItemMeta skull2Meta = skull2.getItemMeta();
				skull2Meta.setDisplayName("§7Vorherige Seite");
				skull2.setItemMeta(skull2Meta);
				skull2.setAmount(1);
				wahlInv.setItem(wahlInv.getSize() - 6, skull2);
				for (int j = 0; j < 8; j++) {
					ItemStack Glas = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").build();
					wahlInv.setItem(j, Glas);
				}
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

	public String getAktenAutorName(String UUID) {
		String AutorName = Main.getPlugin().getPoliceAktenConfig()
				.getString("Spieler." + uuid.toString() + ".akten." + UUID + ".autor");
		return AutorName;
	}

	public String getAktenTitle(String UUID) {
		String AktenTitle = Main.getPlugin().getPoliceAktenConfig()
				.getString("Spieler." + uuid.toString() + ".akten." + UUID + ".aktenTitel");
		return AktenTitle;
	}

	public String getAktenLore(String UUID) {
		String AktenLore = Main.getPlugin().getPoliceAktenConfig()
				.getString("Spieler." + uuid.toString() + ".akten." + UUID + ".aktenLore");
		return AktenLore;
	}

	public List<String> getAktenUUID() {
		AktenTitleUUID = Main.getPlugin().getPoliceAktenConfig()
				.getStringList("Spieler." + uuid.toString() + ".aktenTitleUUID");
		return AktenTitleUUID;
	}

	public List<String> getAktenNachricht(String UUID) {
		AktenNachricht = Main.getPlugin().getPoliceAktenConfig()
				.getStringList("Spieler." + uuid.toString() + ".akten." + UUID + ".nachricht");
		return AktenNachricht;
	}
}
