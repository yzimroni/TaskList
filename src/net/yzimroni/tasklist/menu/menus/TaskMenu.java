package net.yzimroni.tasklist.menu.menus;

import org.bukkit.inventory.Inventory;

import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.task.Task;

public class TaskMenu extends Menu {

	private Task task;

	public TaskMenu(Task task) {
		super();
		this.task = task;
	}

	@Override
	public Inventory createInventory() {
		
		return null;
	}

}
