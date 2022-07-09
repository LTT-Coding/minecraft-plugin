package de.ltt.staat.police.policeComputer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.ltt.server.reflaction.ItemBuilder;
import de.ltt.server.reflaction.ItemSkulls;

public class AktenSystem {
	
	/*public static Inventory getPersonAktenInv(int page) {
		int i;
		if (Main..size() <= 45) {

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

	}*/
	
	public static Inventory getCategoryInv(Material CategoryItem) {
		Inventory inv = Bukkit.createInventory(null, 2 * 9, "Kategorie");
		inv.setItem(0, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(1, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(2, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(3, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(4, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(5, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(6, new ItemBuilder(CategoryItem).setName("§6").build());
		inv.setItem(7, new ItemBuilder(CategoryItem).setName("§6").build());
		return inv;
	}

}
