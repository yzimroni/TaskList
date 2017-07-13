package net.yzimroni.tasklist.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemHandler {

	public void onClick(ItemStack item, Player player);

}
