package net.yzimroni.tasklist.menu.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.yzimroni.tasklist.menu.Menu;
import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.builder.MenuBuilder;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.utils.ItemBuilder;
import net.yzimroni.tasklist.utils.Utils;

public class ChangeXpMenu extends Menu {

	private final static int PER_PAGE = 4 * 9;
	private final static int MAX = 64;

	private Task task;
	private int page;

	public ChangeXpMenu(Task task, int page) {
		super();
		this.task = task;
		this.page = page;
		addItemTrackers();
	}

	private void addItemTrackers() {
		getItemTracker().addItemHandler(Utils.ITEM_PREVIOUS, (i, p) -> {
			MenuManager.get().open(p, new TaskMenu(task));
		});

		getItemTracker().addItemHandler(Utils.ITEM_BACK, (i, p) -> {
			MenuManager.get().open(p, new ChangeXpMenu(task, page - 1));
		});

		getItemTracker().addItemHandler(Utils.ITEM_NEXT, (i, p) -> {
			MenuManager.get().open(p, new ChangeXpMenu(task, page + 1));
		});

		getItemTracker().addItemMatcherHandler(i -> {
			return i.getType() == Material.EXP_BOTTLE && i.hasItemMeta() && i.getItemMeta().hasDisplayName();
		}, (i, p) -> {
			if (!task.isCompleted()) {
				int xp = i.getAmount();
				task.setXp(xp);
				p.sendMessage(ChatColor.GREEN + "You've changed task '" + task.getName() + "' xp to " + xp);
				MenuManager.get().open(p, new TaskMenu(task));
			} else {
				p.closeInventory();
			}
		});
	}

	@Override
	public Inventory createInventory(Inventory o_inv) {
		Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Change xp: " + task.getName());
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(Utils.ITEM_PREVIOUS);
		items.add(task.getItemStack());
		items.add(MenuBuilder.BR);

		int start = (page - 1) * PER_PAGE;
		int end = Math.min(start + PER_PAGE, MAX);
		for (int i = start; i < end; i++) {
			int xp = i + 1;
			ItemStack item = new ItemBuilder(task.getXp() == xp ? Material.POTION : Material.EXP_BOTTLE)
					.displayName(ChatColor.GREEN + "" + xp + " XP").amount(xp)
					.addLore("Change task '" + task.getName() + "'", "XP reward to " + xp).build();
			items.add(item);
		}
		items.add(MenuBuilder.BR);
		if (start > 0) {
			items.add(Utils.ITEM_BACK);
		}
		if (end < MAX) {
			items.add(Utils.ITEM_NEXT);
		}

		MenuBuilder builder = new MenuBuilder(inventory);
		builder.create(items);

		return inventory;
	}

}
