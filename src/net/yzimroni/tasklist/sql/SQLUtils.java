package net.yzimroni.tasklist.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.yzimroni.tasklist.task.Task;

public class SQLUtils {

	private SQL sql;
	private String prefix;

	private static SQLUtils instance;

	public SQLUtils(SQL sql, String prefix) {
		this.sql = sql;
		this.prefix = prefix;

		instance = this;

		createTables();
	}

	public static SQLUtils get() {
		return instance;
	}

	public void createTables() {
		PreparedStatement p = sql.getPrepare("CREATE TABLE IF NOT EXISTS " + prefix("tasks")
				+ " ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL, `xp` INTEGER NOT NULL, `creator` TEXT, `created` INTEGER, `completed` INTEGER DEFAULT 0 );");
		try {
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String prefix(String table) {
		return prefix + table;
	}

	public void close() {
		sql.closeConnection();
		sql = null;
	}

	public List<Task> loadTasks() {
		PreparedStatement p = sql.getPrepare("SELECT * FROM " + prefix("tasks"));
		try {
			List<Task> tasks = new ArrayList<Task>();
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				String creator = rs.getString("creator");
				Task t = new Task(rs.getInt("ID"), rs.getString("name"), rs.getInt("xp"),
						creator == null ? null : UUID.fromString(creator), rs.getLong("created"),
						rs.getLong("completed"));
				tasks.add(t);
			}
			return tasks;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveTask(Task t) {
		synchronized (t) {
			if (t.getId() == -1) {
				PreparedStatement p = sql.getPrepareAutoKeys(
						"INSERT INTO " + prefix("tasks") + " (name,xp,creator,created,completed) VALUES(?,?,?,?,?)");
				try {
					p.setString(1, t.getName());
					p.setInt(2, t.getXp());
					if (t.getCreator() == null) {
						p.setNull(3, Types.VARCHAR);
					} else {
						p.setString(3, t.getCreator().toString());
					}
					p.setLong(4, t.getCreated());
					p.setLong(5, t.getCompleted());
					p.executeUpdate();
					int id = sql.getIdFromPrepared(p);
					t.setId(id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (t.isChanged()) {
				PreparedStatement p = sql.getPrepare(
						"UPDATE " + prefix("tasks") + " SET name=?,xp=?,creator=?,created=?,completed=? WHERE ID=?");
				try {
					p.setString(1, t.getName());
					p.setInt(2, t.getXp());
					if (t.getCreator() == null) {
						p.setNull(3, Types.VARCHAR);
					} else {
						p.setString(3, t.getCreator().toString());
					}
					p.setLong(4, t.getCreated());
					p.setLong(5, t.getCompleted());

					p.setInt(6, t.getId());
					p.executeUpdate();
					t.setChanged(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteTask(Task t) {
		if (t.getId() != -1) {
			PreparedStatement p = sql.getPrepare("DELETE FROM " + prefix("tasks") + " WHERE ID=?");
			try {
				p.setInt(1, t.getId());
				p.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
