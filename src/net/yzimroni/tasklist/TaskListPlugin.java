package net.yzimroni.tasklist;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.SubCommand;
import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.command.args.arguments.IntegerArgument;
import net.yzimroni.commandmanager.command.args.arguments.StringArgument;
import net.yzimroni.commandmanager.manager.CommandManager;
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

		initCommands();
	}

	private void initSql() throws Exception {
		SQL sql = new SQL("plugins/" + getName() + "/tasklist.db");
		this.sql = new SQLUtils(sql, getConfig().getString("database.prefix"));

	}

	private void initCommands() {
		Command tasklist = new Command("tasklist", "TaskList command", (sender, command, args) -> {
			MenuManager.get().open((Player) sender, new TaskListMenu(1));
		});
		tasklist.setAliases("tl", "task", "t");
		tasklist.setOnlyPlayer(true);
		tasklist.setAutoHelpCommand(true);

		SubCommand add = new SubCommand("add", "Add a new task", (sender, command, args) -> {
			String taskName = args.get("task", String.class);
			int xp = args.get("xp", Integer.class);
			if (manager.getTaskByName(taskName) != null) {
				sender.sendMessage("Task already exist!");
				return;
			}
			UUID uuid = null;
			if (sender instanceof Player) {
				uuid = ((Player) sender).getUniqueId();
			}
			manager.addTask(new Task(-1, taskName, xp, uuid, System.currentTimeMillis(), 0));
			sender.sendMessage(ChatColor.GREEN + "Task '" + taskName + "' added!");
		});
		add.setPermission("tasklist.op");
		add.addArgument(new IntegerArgument("xp", true, 1, 100, true));
		add.addArgument(new StringArgument("task", true, true));

		tasklist.addSubCommand(add);

		CommandArgument<Task> taskArgument = new CommandArgument<Task>("task") {

			@Override
			public Task getInput(ArgumentParseData data) {
				return manager.getTaskByName(data.getInput());
			}

			@Override
			public String getInputType() {
				return "Task name";
			}

			@Override
			public List<String> getTabCompleteOptions(ArgumentParseData data) {
				return Arrays.asList();
			}

			@Override
			public String getValidInputs() {
				return "";
			}

			@Override
			public ArgumentValidCheck isValidInput(ArgumentParseData data) {
				return ArgumentValidCheck.create(manager.getTaskByName(data.getInput()) != null, "Task not exists");
			}

			@Override
			public boolean isVarArgs() {
				return true;
			}
		};

		SubCommand changeXp = new SubCommand("changexp", "Change the xp reward of a task", (sender, command, args) -> {
			Task task = args.get("task", Task.class);
			int xp = args.get("xp", Integer.class);
			task.setXp(xp);
			sender.sendMessage(ChatColor.GREEN + "You've changed task '" + task.getName() + "' xp to " + xp);
		});

		changeXp.setPermission("tasklist.op");
		changeXp.addArgument(new IntegerArgument("xp", true, 1, 100, true));
		changeXp.addArgument(taskArgument);

		tasklist.addSubCommand(changeXp);

		CommandManager.get().registerCommand(this, tasklist);

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
