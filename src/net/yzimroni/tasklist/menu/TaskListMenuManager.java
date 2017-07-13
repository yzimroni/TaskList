package net.yzimroni.tasklist.menu;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TaskListMenuManager implements Listener {

	private HashMap<UUID, TaskListMenu> openMenus = new HashMap<UUID, TaskListMenu>();

	public boolean hasMenu(UUID u) {
		return openMenus.containsKey(u);
	}

	public TaskListMenu getMenu(UUID u) {
		return openMenus.get(u);
	}

	@EventHandler
	public void onInvenotryClose(InventoryCloseEvent e) {
		if (hasMenu(e.getPlayer().getUniqueId())) {
			TaskListMenu menu = getMenu(e.getPlayer().getUniqueId());
			menu.onInventoryClose(e);
			openMenus.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (hasMenu(p.getUniqueId())) {
			TaskListMenu menu = getMenu(p.getUniqueId());
			menu.onInventoryClick(e);
		}
	}

}
