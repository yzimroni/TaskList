package net.yzimroni.tasklist;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.tasklist.menu.MenuManager;
import net.yzimroni.tasklist.menu.menus.TaskListMenu;
import net.yzimroni.tasklist.sql.SQL;
import net.yzimroni.tasklist.sql.SQLUtils;
import net.yzimroni.tasklist.task.Task;
import net.yzimroni.tasklist.task.TaskManager;

public class TaskListPlugin extends JavaPlugin {

	private SQLUtils sql;
	private TaskManager manager;
	private MenuManager menuManager;

	private static TaskListPlugin instance;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		try {
			initSql();
		} catch (Exception e) {
			System.out.println("Failed to start sql:");
			e.printStackTrace();
		}
		manager = new TaskManager();
		Bukkit.getPluginManager().registerEvents(manager, this);
		menuManager = new MenuManager();
		Bukkit.getPluginManager().registerEvents(menuManager, this);

		instance = this;

		manager.loadTasks();
	}

	private void initSql() throws Exception {
		SQL sql = new SQL("plugins/" + getName() + "/tasklist.db");
		this.sql = new SQLUtils(sql, getConfig().getString("database.prefix"));

	}

	@Override
	public void onDisable() {
		if (manager != null) {
			manager.saveTasks();
		}
		if (sql != null) {
			sql.close();
		}
	}

	public static TaskListPlugin get() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && args.length == 0) {
			MenuManager.get().open((Player) sender, new TaskListMenu(1));
			return false;
		}
		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
			if (sender.isOp()) {
				sender.sendMessage(ChatColor.GREEN + "/" + label.toLowerCase() + " add <xp> <text>");
			}
		}
		if (args.length >= 3) {
			if (args[0].equalsIgnoreCase("add")) {
				if (sender.isOp()) {
					int xp = 0;
					try {
						xp = Integer.valueOf(args[1]);
					} catch (Exception e) {
						sender.sendMessage("Invalid number: " + args[1]);
						return false;
					}
					String name = getText(args, 2);
					if (manager.getTaskByName(name) != null) {
						sender.sendMessage("Task already exist!");
						return false;
					}
					UUID uuid = null;
					if (sender instanceof Player) {
						uuid = ((Player) sender).getUniqueId();
					}
					manager.addTask(new Task(-1, name, xp, uuid, System.currentTimeMillis(), 0));
					sender.sendMessage(ChatColor.GREEN + "Task '" + name + "' added!");
				}
			} else if (args[0].equalsIgnoreCase("changexp")) {
				if (sender.isOp()) {
					int xp = 0;
					try {
						xp = Integer.valueOf(args[1]);
					} catch (Exception e) {
						sender.sendMessage("Invalid number: " + args[1]);
						return false;
					}
					String name = getText(args, 2);
					Task task = manager.getTaskByName(name);
					if (task == null) {
						sender.sendMessage("Task not exists!");
						return false;
					}
					task.setXp(xp);
					sender.sendMessage("You've changed task '" + task.getName() + "' xp to " + xp);
				}
			}
		}
		return false;
	}

	public static String getText(String[] args, int start) {
		if (args.length <= start) {
			return null;
		}
		String result = "";
		for (int i = start; i < args.length; i++) {
			if (!result.isEmpty()) {
				result += " ";
			}
			result += args[i];
		}
		return result;
	}

	public TaskManager getManager() {
		return manager;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public SQLUtils getSql() {
		return sql;
	}

}
