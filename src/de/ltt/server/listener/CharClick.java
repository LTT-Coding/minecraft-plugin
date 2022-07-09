package de.ltt.server.listener;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.ltt.FakePlayer.NPC;
import de.ltt.server.main.Main;
import de.ltt.server.reflaction.ChatInfo;
import de.ltt.server.reflaction.ChatType;
import de.ltt.server.reflaction.FirmInfo;
import de.ltt.server.reflaction.Gender;
import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.PlayerInfo;

public class CharClick implements Listener, CommandExecutor {

	public static HashMap<Player, String> firstNames = new HashMap<Player, String>();
	public static HashMap<Player, String> lastNames = new HashMap<Player, String>();
	public static HashMap<Player, LocalDate> birthDays = new HashMap<Player, LocalDate>();
	public static HashMap<Player, Gender> genders = new HashMap<Player, Gender>();
	public static ArrayList<Player> notKick = new ArrayList<Player>();
	public static HashMap<Player, String> removeChar = new HashMap<Player, String>();

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			if (e.getView().getTitle().equals("§6Erstelle einen neuen Charakter")
					&& e.getView().getTopInventory().equals(e.getClickedInventory())) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Vorname")) {
					ChatInfo.addChat(p, ChatType.CHARFISTNAME);
					p.sendMessage("§eBitte gib den Vornamen an: ");
					p.sendMessage("§eWir weisen expilzit darauf hin, dass NICHT der echte Name verwendet werden soll!");
					p.sendMessage("§eDieser Name wird auf dem Server angezeigt also überleg ihn dir gut");
					notKick.add(p);
					p.closeInventory();
					if (notKick.contains(p))
						notKick.remove(p);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Nachname")) {
					ChatInfo.addChat(p, ChatType.CHARLASTNAME);
					p.sendMessage("§eBitte gib den Nachname an: ");
					p.sendMessage("§eWir weisen expilzit darauf hin, dass NICHT der echte Name verwendet werden soll!");
					p.sendMessage("§eDieser Name wird auf dem Server angezeigt also überleg ihn dir gut");
					notKick.add(p);
					p.closeInventory();
					if (notKick.contains(p))
						notKick.remove(p);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Geburtstag")) {
					Inventory inv = Bukkit.createInventory(null, 9 * 3, "§6Gib ein Datum ein: ");
					for (int i = 0; i < 27; i++) {
						inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
					}
					inv.setItem(1, new ItemBuilder(
							"http://textures.minecraft.net/texture/1ad6c81f899a785ecf26be1dc48eae2bcfe777a862390f5785e95bd83bd14d")
									.setName("§aTag + 1").build());
					inv.setItem(4, new ItemBuilder(
							"http://textures.minecraft.net/texture/1ad6c81f899a785ecf26be1dc48eae2bcfe777a862390f5785e95bd83bd14d")
									.setName("§aMonat + 1").build());
					inv.setItem(7, new ItemBuilder(
							"http://textures.minecraft.net/texture/1ad6c81f899a785ecf26be1dc48eae2bcfe777a862390f5785e95bd83bd14d")
									.setName("§aJahr + 1").build());
					inv.setItem(10, new ItemBuilder(
							"http://textures.minecraft.net/texture/fa661419de49ff4a2c97b27f868014fbdaeb8dd7f4392777830b2714caafd1f")
									.setName("§6Tag").setLore("1").build());
					inv.setItem(13, new ItemBuilder(
							"http://textures.minecraft.net/texture/c8dec4666b4c67d8759714c85714be6ea4e39ff9628849f98b514edf1c3e4680")
									.setName("§6Monat").setLore("1").build());
					inv.setItem(16, new ItemBuilder(
							"http://textures.minecraft.net/texture/3da8b6473052ada22e6ca30c49f6dce9b99916e423ac4fc6b301ad733697f")
									.setName("§6Jahr").setLore("2000").build());
					inv.setItem(19, new ItemBuilder(
							"http://textures.minecraft.net/texture/882faf9a584c4d676d730b23f8942bb997fa3dad46d4f65e288c39eb471ce7")
									.setName("§4Tag - 1").build());
					inv.setItem(22, new ItemBuilder(
							"http://textures.minecraft.net/texture/882faf9a584c4d676d730b23f8942bb997fa3dad46d4f65e288c39eb471ce7")
									.setName("§4Monat - 1").build());
					inv.setItem(25, new ItemBuilder(
							"http://textures.minecraft.net/texture/882faf9a584c4d676d730b23f8942bb997fa3dad46d4f65e288c39eb471ce7")
									.setName("§4Jahr - 1").build());
					inv.setItem(17, new ItemBuilder(
							"http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6")
									.setName("§aAkzeptieren").build());
					notKick.add(p);
					p.openInventory(inv);
					if (notKick.contains(p))
						notKick.remove(p);
					birthDays.put(p, LocalDate.of(2000, 1, 1));
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAkzeptieren")) {
					if (firstNames.containsKey(p) && lastNames.containsKey(p) && birthDays.containsKey(p)
							&& genders.containsKey(p)) {
						notKick.add(p);
						PlayerInfo.addChar(p, firstNames.get(p), lastNames.get(p), birthDays.get(p).toString(),
								genders.get(p));
						p.closeInventory();
						if (notKick.contains(p))
							notKick.remove(p);
						p.sendMessage("§aDu hast den Charakter erstellt!");
						if (PlayerInfo.getChars(p).size() == 1) {
							Main.loadTab(p);
						}
						PlayerInfo pi = new PlayerInfo(p);
						pi.saveAktuellSkin();
					} else
						p.sendMessage("§cFülle erst alles aus!");
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Geschlecht")) {
					Gender gender;
					ItemStack item = e.getCurrentItem();
					if (genders.containsKey(p)) {
						gender = genders.get(p);
						switch (gender) {
						case MALE:
							gender = Gender.FEMALE;
							break;
						case FEMALE:
							gender = Gender.DIVERSE;
							break;
						case DIVERSE:
							gender = Gender.MALE;
							break;
						}
					} else {
						gender = Gender.MALE;
					}
					genders.put(p, gender);
					e.getClickedInventory().setItem(e.getSlot(),
							new ItemBuilder(item).setLore(gender.getTranslation()).build());
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().contains("§6Gib ein Datum ein: ")
					&& e.getView().getTopInventory().equals(e.getClickedInventory())) {
				Player p = (Player) e.getWhoClicked();
				e.setCancelled(true);
				LocalDate date = birthDays.get(p);
				int tag = date.getDayOfMonth();
				int monat = date.getMonthValue();
				int jahr = date.getYear();

				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aTag + 1")) {
					tag++;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(10, new ItemBuilder(
								"http://textures.minecraft.net/texture/fa661419de49ff4a2c97b27f868014fbdaeb8dd7f4392777830b2714caafd1f")
										.setName("§6Tag").setLore(tag + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						tag--;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Tag - 1")) {
					tag--;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(10, new ItemBuilder(
								"http://textures.minecraft.net/texture/fa661419de49ff4a2c97b27f868014fbdaeb8dd7f4392777830b2714caafd1f")
										.setName("§6Tag").setLore(tag + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						tag++;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aMonat + 1")) {
					monat++;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(13, new ItemBuilder(
								"http://textures.minecraft.net/texture/c8dec4666b4c67d8759714c85714be6ea4e39ff9628849f98b514edf1c3e4680")
										.setName("§6Monat").setLore(monat + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						monat--;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Monat - 1")) {
					monat--;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(13, new ItemBuilder(
								"http://textures.minecraft.net/texture/c8dec4666b4c67d8759714c85714be6ea4e39ff9628849f98b514edf1c3e4680")
										.setName("§6Monat").setLore(monat + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						monat++;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aJahr + 1")) {
					jahr++;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(16, new ItemBuilder(
								"http://textures.minecraft.net/texture/3da8b6473052ada22e6ca30c49f6dce9b99916e423ac4fc6b301ad733697f")
										.setName("§6Jahr").setLore(jahr + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						jahr--;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());

				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Jahr - 1")) {
					jahr--;
					if (dateIsValid(jahr, tag, monat)) {
						e.getClickedInventory().setItem(16, new ItemBuilder(
								"http://textures.minecraft.net/texture/3da8b6473052ada22e6ca30c49f6dce9b99916e423ac4fc6b301ad733697f")
										.setName("§6Jahr").setLore(jahr + "").build());
					} else {
						p.sendMessage("§cDas Datum ist nicht gültig!");
						jahr++;
					}
					saveDate(p, jahr, monat, tag, e.getClickedInventory());
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAkzeptieren")) {
					if (Period.between(date, LocalDate.now()).getYears() < 16)
						p.sendMessage("§cDas angebene Alter muss mindestens 16 sein!");
					else
						openCharCreate(p);
				}
			} else if (e.getView().getTitle().equals("§6Wähle einen Charakter")
					&& e.getView().getTopInventory().equals(e.getClickedInventory())) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aNeuen Charakter erstellen")) {
					if (firstNames.containsKey(p))
						firstNames.remove(p);
					if (lastNames.containsKey(p))
						lastNames.remove(p);
					if (birthDays.containsKey(p))
						birthDays.remove(p);
					if (genders.containsKey(p))
						genders.remove(p);
					openCharCreate(p);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Einen Charakter löschen")) {
					Main.openCharRemove(p);
				} else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					try {
						PlayerInfo pis = new PlayerInfo(p);
						pis.saveInv();
						pis.changeSkin(p.getName());
						pis.saveLastLocation();
						String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
						for (String cha : PlayerInfo.getChars(p)) {
							PlayerInfo pi = new PlayerInfo(p, UUID.fromString(cha));
							if (pi.getFullName().equals(name)) {
								pi.changeChar(UUID.fromString(cha));
								pi.loadInv();
								pi.loadLastLocation();
								pi.loadAktuellSkin();
								p.sendMessage("§aDu hast zu dem Charakter §6" + name + "§a gewechselt!");

								break;
							}
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().equals("§4Einen Charakter löschen")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("§6", "");
					for (String cha : PlayerInfo.getChars(p)) {
						PlayerInfo pi = new PlayerInfo(p, UUID.fromString(cha));
						if (pi.getFullName().equals(name)) {
							removeChar.put(p, cha);
							Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER,
									"§4Charakter wirklich löschen?");
							inv.setItem(0, new ItemBuilder(Material.RED_CONCRETE).setName("§4Abbrechen").build());
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
							inv.setItem(2, new ItemBuilder(Material.PLAYER_HEAD).setName("§6" + pi.getFullName())
									.setLore("Alter: " + pi.getAge(),
											"Geburtsdatum: " + LocalDate.parse(pi.getBirthDate()).format(myFormatObj1),
											"Nummer: " + pi.getNummer(), "Geld: " + pi.getMoneyInHand(),
											"Beruf: " + job)
									.build());
							inv.setItem(4,
									new ItemBuilder(Material.LIME_CONCRETE).setName("§aAkzeptieren")
											.setLore("§4Alle Daten über den Charakter werden gelöscht!",
													"§4Die Daten können nicht wiederhergestellt werden!")
											.build());
							p.openInventory(inv);
							break;
						}
					}
					
					//NPC.addJoinPacket(p);
					//Main.ject(true);

				}
				e.setCancelled(true);
			} else if (e.getView().getTitle().equals("§4Charakter wirklich löschen?")) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4Abbrechen")) {
					p.closeInventory();
					p.sendMessage("§4Vorgang abgebrochen!");
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aAkzeptieren")) {
					new PlayerInfo(p).killChar(removeChar.get(p));
					p.closeInventory();
					if (firstNames.containsKey(p))
						firstNames.remove(p);
					if (lastNames.containsKey(p))
						lastNames.remove(p);
					if (birthDays.containsKey(p))
						birthDays.remove(p);
					if (genders.containsKey(p))
						genders.remove(p);
					if (PlayerInfo.getChars(p).size() == 0) {
						Main.unloadTab();
						Main.openCharCreate(p);
					}

					p.sendMessage("§aCharakter wurde gelöscht!");
				}
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public boolean dateIsValid(int year, int day, int month) {
		try {
			LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			return false;
		}
		return true;
	}

	@EventHandler
	public void onPlayerChar(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ChatInfo.getChat(e.getPlayer()).equals(ChatType.CHARFISTNAME)) {
			if (e.getMessage().length() <= 15 && e.getMessage().length() >= 4) {
				if (Main.checkABC(e.getMessage())) {
					if (!Main.checkBannedWords(e.getMessage())) {
						if (!Main.checkPlayerName(e.getMessage())) {
							if(!Main.checkAllChars(e.getMessage())) {
								firstNames.put(p, Main.toUpperLowerCase(e.getMessage()));
								ChatInfo.removeChat(p);
							} else {
								p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
							}
						} else {
							p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
						}
					} else {
						p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
					}
				} else {
					p.sendMessage("§cDer Name darf nur Buchstaben enthalten!");
				}
			} else {
				p.sendMessage("§cDer Name darf maximal 15 Zeichen haben und muss mindestens 4 Buchstaben haben!");
			}
			openCharCreate(p);
			ChatInfo.removeChat(p);
			e.setCancelled(true);
		} else if (ChatInfo.getChat(e.getPlayer()).equals(ChatType.CHARLASTNAME)) {
			if (e.getMessage().length() <= 15 && e.getMessage().length() >= 4) {
				if (Main.checkABC(e.getMessage())) {
					if (!Main.checkBannedWords(e.getMessage())) {
						if (!Main.checkPlayerName(e.getMessage())) {
							if(!Main.checkAllChars(e.getMessage())) {
								lastNames.put(p, Main.toUpperLowerCase(e.getMessage()));
								ChatInfo.removeChat(p);
							} else {
								p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
							}
						} else {
							p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
						}
					} else {
						p.sendMessage("§cDer Name enthält ein verbotenes Wort!");
					}
				} else {
					p.sendMessage("§cDer Name darf nur Buchstaben enthalten!");
				}
			} else {
				p.sendMessage("§cDer Name darf maximal 15 Zeichen haben und muss mindestens 4 Buchstaben haben!");
			}

			openCharCreate(p);
			ChatInfo.removeChat(p);
			e.setCancelled(true);
		}
	}

	public void openCharCreate(Player p) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Erstelle einen neuen Charakter");
		notKick.add(p);
		p.closeInventory();
		if (notKick.contains(p))
			notKick.remove(p);
		if (firstNames.containsKey(p))
			inv.setItem(0, new ItemBuilder(Material.BOOK).setName("§6Vorname").setLore(firstNames.get(p)).build());
		else
			inv.setItem(0,
					new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Vorname").setLore("max. 15 Zeichen").build());
		if (lastNames.containsKey(p))
			inv.setItem(1, new ItemBuilder(Material.BOOK).setName("§6Nachname").setLore(lastNames.get(p)).build());
		else
			inv.setItem(1,
					new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Nachname").setLore("max. 15 Zeichen").build());
		DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if (birthDays.containsKey(p))
			inv.setItem(2,
					new ItemBuilder(Material.CAKE).setName("§6Geburtstag")
							.setLore(birthDays.get(p).format(myFormatObj1),
									Period.between(birthDays.get(p), LocalDate.now()).getYears() + "")
							.build());
		else
			inv.setItem(2, new ItemBuilder(Material.CAKE).setName("§6Geburtstag").setLore("mind. 16 Jahre").build());
		if (genders.containsKey(p))
			inv.setItem(3, new ItemBuilder(Material.EGG).setName("§6Geschlecht")
					.setLore(genders.get(p).getTranslation()).build());
		else
			inv.setItem(3, new ItemBuilder(Material.EGG).setName("§6Geschlecht").build());
		inv.setItem(4, new ItemBuilder(
				"http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6")
						.setName("§aAkzeptieren").build());
		p.openInventory(inv);
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		if (e.getView().getTitle().equals("§6Erstelle einen neuen Charakter")
				|| e.getView().getTitle().contains("§6Gib ein Datum ein: "))
			if (notKick.contains(e.getPlayer()))
				notKick.remove(e.getPlayer());
			else if (PlayerInfo.getChars((OfflinePlayer) e.getPlayer()).size() == 0)
				((Player) e.getPlayer()).kickPlayer("§cDu musst einen Charakter erstellen um Spielen zu können!");

	}

	public void saveDate(Player p, int jahr, int monat, int tag, Inventory inv2) {
		LocalDate rd = LocalDate.of(jahr, monat, tag);
		birthDays.put(p, rd);
		DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		Inventory inv = Bukkit.createInventory(null, 9 * 3, "§6Gib ein Datum ein: " + rd.format(myFormatObj1));
		inv.setContents(inv2.getContents());
		notKick.add(p);
		p.openInventory(inv);
		if (notKick.contains(p))
			notKick.remove(p);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (Main.Admin.contains(p.getUniqueId().toString())) {
				Main.openCharInv(p);
			} else
				p.sendMessage(Main.KEINE_RECHTE_ADMIN);
		} else
			sender.sendMessage(Main.KEIN_SPIELER);
		return false;
	}

}
