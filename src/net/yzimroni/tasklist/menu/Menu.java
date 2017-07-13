package net.yzimroni.tasklist.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class Menu {
	
	public abstract Inventory createInventory();
	
	public void onInventoryClose(InventoryCloseEvent e) {
		
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		
	}

}
