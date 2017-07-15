package net.yzimroni.tasklist.menu.builder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MenuRow {

	private List<ItemStack> items = new ArrayList<>();
	private int[] alignment;

	public MenuRow() {
		super();
	}

	public void addItem(ItemStack i) {
		items.add(i);
		alignment = null;
	}

	public void calculateAlign(MenuRowAlign align, int entriesPerRow) {
		/*
		 * We use entriesPerRow and not SLOTS_PER_ROW because we already calculate The
		 * different between SLOTS_PER_ROW and entriesPerRow in MenuBuilder#checkValid,
		 * So we just have to check the different between the amount of items and
		 * entriesPerRow.
		 */
		alignment = align.calculateAlign(items.size(), entriesPerRow);
	}

	public void addMiddleSpace() {
		if (items.size() % 2 == 0) {
			int middle = items.size() / 2;
			items.add(middle, new ItemStack(Material.AIR));
		}
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}

	public int[] getAlignment() {
		return alignment;
	}

	public void setAlignment(int[] alignment) {
		this.alignment = alignment;
	}

}
