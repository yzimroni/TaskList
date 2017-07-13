package net.yzimroni.tasklist.menu.builder;

import java.util.ArrayList;
import java.util.List;

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
		alignment = align.calculateAlign(items.size(), entriesPerRow);
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
