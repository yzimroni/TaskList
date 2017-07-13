package net.yzimroni.tasklist.task;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.yzimroni.tasklist.utils.Utils;

public class Task {

	private int id;
	private String name;
	private int xp;
	private UUID creator;
	private long created;
	private long completed;

	private boolean changed;

	public Task(int id, String name, int xp, UUID creator, long created, long completed) {
		super();
		this.id = id;
		this.name = name;
		this.xp = xp;
		this.creator = creator;
		this.created = created;
		this.completed = completed;
	}

	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(isCompleted() ? Material.MINECART : Material.STORAGE_MINECART);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName((isCompleted() ? ChatColor.GOLD : ChatColor.BLUE) + getName());
		meta.setLore(Arrays.asList("Task: " + getName(), "xp: " + xp,
				"Creator: " + Bukkit.getOfflinePlayer(creator).getName(), "Created: " + Utils.formatDate(created),
				"Completed: " + (isCompleted() ? "Yes " + Utils.formatDate(completed) : "No")));

		i.setItemMeta(meta);

		return i;
	}

	public boolean isCompleted() {
		return completed > 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		changed = true;
		this.name = name;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		changed = true;
		this.xp = xp;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		changed = true;
		this.created = created;
	}

	public long getCompleted() {
		return completed;
	}

	public void setCompleted(long completed) {
		changed = true;
		this.completed = completed;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public UUID getCreator() {
		return creator;
	}

	public void setCreator(UUID creator) {
		this.creator = creator;
	}

}
