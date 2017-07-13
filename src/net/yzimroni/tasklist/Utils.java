package net.yzimroni.tasklist;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Utils {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

	public static final ItemStack ITEM_BACK = getSkull(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2NDM5YzNmNjVlOWZlNDFmMmQ4YmU0N2JmNDkzZjE0M2FjYWRmZDVkYmVhODZmZWE2ZGJlMTllNDM1ZWEifX19");
	public static final ItemStack ITEM_PREVIOUS = getSkull(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU2NDM5YzNmNjVlOWZlNDFmMmQ4YmU0N2JmNDkzZjE0M2FjYWRmZDVkYmVhODZmZWE2ZGJlMTllNDM1ZWEifX19");
	public static final ItemStack ITEM_NEXT = getSkull(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWEyMGQ2ZWYxMjY4MTMwNjZhMjU3N2NjYzVkZjcxMDI2ODNmMWFmN2MzZTk0ZTQ1YWI5YWQ2N2VkYmI1MiJ9fX0=");

	static {
		{
			ItemMeta meta = ITEM_BACK.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Back");
			ITEM_BACK.setItemMeta(meta);
		}
		{
			ItemMeta meta = ITEM_PREVIOUS.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Previous");
			ITEM_PREVIOUS.setItemMeta(meta);
		}
		{
			ItemMeta meta = ITEM_NEXT.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Next");
			ITEM_NEXT.setItemMeta(meta);
		}
	}

	private Utils() {

	}

	public static ItemStack getSkull(String data) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta meta = skull.getItemMeta();

		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", data));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		skull.setItemMeta(meta);
		return skull;
	}

	public static String formatDate(long time) {
		return DATE_FORMATTER.format(new Date(time));
	}

}
