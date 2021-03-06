package net.yzimroni.tasklist.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class Menu {

	private MenuItemTracker itemTracker = new MenuItemTracker();

	public abstract Inventory createInventory(Inventory inventory);

	public void onInventoryClose(InventoryCloseEvent e) {

	}

	public void onInventoryClick(InventoryClickEvent e) {

	}

	public MenuItemTracker getItemTracker() {
		return itemTracker;
	}

}
