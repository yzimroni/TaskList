package net.yzimroni.tasklist.menu;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MenuManager implements Listener {

	private HashMap<UUID, Menu> openMenus = new HashMap<UUID, Menu>();

	private static MenuManager instance;

	public MenuManager() {
		instance = this;
	}

	public static MenuManager get() {
		return instance;
	}

	public boolean hasMenu(UUID u) {
		return openMenus.containsKey(u);
	}

	public Menu getMenu(UUID u) {
		return openMenus.get(u);
	}

	public void open(Player p, Menu menu) {
		Inventory inventory = null;
		if (hasMenu(p.getUniqueId())) {
			inventory = p.getOpenInventory().getTopInventory();
			openMenus.remove(p.getUniqueId());
		}
		openMenus.put(p.getUniqueId(), menu);
		p.openInventory(menu.createInventory(inventory));
		if (!hasMenu(p.getUniqueId())) {
			openMenus.put(p.getUniqueId(), menu);
		}

	}

	@EventHandler
	public void onInvenotryClose(InventoryCloseEvent e) {
		if (hasMenu(e.getPlayer().getUniqueId())) {
			Menu menu = getMenu(e.getPlayer().getUniqueId());
			menu.onInventoryClose(e);
			openMenus.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (hasMenu(p.getUniqueId())) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			Menu menu = getMenu(p.getUniqueId());
			menu.onInventoryClick(e);
		}
	}

}
