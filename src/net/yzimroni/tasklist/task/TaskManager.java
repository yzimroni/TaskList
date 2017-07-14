package net.yzimroni.tasklist.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskManager {

	private List<Task> tasks = new ArrayList<Task>();

	public TaskManager() {
		Random r = new Random();
		for (int i = 0; i < 100 /* + (r.nextInt(100) - 50) */; i++) {

			tasks.add(new Task(i + 1, "Task" + i, r.nextInt(100) + 1,
					UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"),
					System.currentTimeMillis() - (i * (i + 10)), r.nextBoolean() ? 0 : System.currentTimeMillis() - i));
		}
		// tasks.add(new Task(1, "Test", 10,
		// UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"), 1499962787, 0));
		// tasks.add(new Task(2, "Test1", 10,
		// UUID.fromString("341899b6-b28f-47a3-b85e-3aa3b491d0d3"), 1499962787,
		// 1499963787));
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public List<Task> getTasks(boolean completed) {
		return tasks.stream().filter(t -> t.isCompleted() == completed).collect(Collectors.toList());
	}

	public Task getTaskByName(String name) {
		for (Task t : tasks) {
			if (t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}

	public void addTask(Task t) {
		tasks.add(t);
	}

	public void removeTask(Task t) {
		tasks.remove(t);
	}

}
