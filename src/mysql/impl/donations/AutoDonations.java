package mysql.impl.donations;

import java.sql.*;
import com.ruseps.util.Misc;
import com.ruseps.model.GameMode;
import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.World;
import com.ruseps.world.content.PlayerPanel;

import com.ruseps.world.content.PointsHandler;
import com.ruseps.model.PlayerRights;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Using this class: To call this class, it's best to make a new thread. You can
 * do it below like so: new Thread(new Donation(player)).start();
 */
public class AutoDonations implements Runnable {

	public static final String HOST = "82.221.105.125"; // website ip address
	public static final String USER = "rebornho_owner";
	public static final String PASS = "n-]m[UK,7bD8";
	public static final String DATABASE = "rebornho_store";

	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 * 
	 * @param player
	 */
	public AutoDonations(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery(
					"SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");
			while (rs.next()) {
				int item_number = rs.getInt("item_number");

				switch (item_number) {// add products according to their ID in
				// the ACP
				case 18: // 5$ donation
					player.getInventory().add(6799, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 19: // 10$ donation.
					player.getInventory().add(6800, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 20: // 20$ donation.
					player.getInventory().add(6800, 2);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 21: // 25$ donation.
					player.getInventory().add(6801, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 22: // 50$ donation.
					player.getInventory().add(6802, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 23: // 75$ donation.
					player.getInventory().add(6802, 1);
					player.getInventory().add(6801, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 24: // 100$ donation.
					player.getInventory().add(6803, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 25: // 150$ donation.
					player.getInventory().add(6804, 1);

					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 26: // 250$ donation.
					player.getInventory().add(6805, 1);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 27: // 500$ donation.
					player.getInventory().add(6805, 2);

					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 28: // 3 Mystery Box (10$)
					player.getInventory().add(6199, 3);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 29: // Mega Mystery Box (10$)
					player.getInventory().add(6201, 3);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				case 30: // 2 Legendary MBox ($10)
					player.getInventory().add(15501, 2);
					player.getPacketSender().sendMessage(
							"@or2@We Appreciate your contribution, this will allow us to expand our playerbase.");
					player.getPacketSender().sendMessage(
							"@or2@Aswell as Deliver New updates! We seriously appreciate it, thanks buddy.");
					break;
				
				}
				rs.updateInt("claimed", 1); // do not delete otherwise they can
				// reclaim!
				rs.updateRow();
			}
			destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host
	 *            the host ip address or url
	 * @param database
	 *            the name of the database
	 * @param user
	 *            the user attached to the database
	 * @param pass
	 *            the users password
	 * @return true if connected
	 */
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection and
	 * statement instances
	 */
	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes an update query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * Executres a query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void checkForRankUpdate(Player player) {
		if (player.getRights().isStaff()) {
			return;
		}
		if (player.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender().sendMessage("@red@You did not recieve donator rank because you are an iron man!");
			return;
		}
		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("You did not recieve donator rank because you are an iron man!");
			return;
		}

		PlayerRights rights = null;
		if (player.getAmountDonated() >= 20)
			rights = PlayerRights.DONATOR;
		if (player.getAmountDonated() >= 50)
			rights = PlayerRights.SUPER_DONATOR;
		if (player.getAmountDonated() >= 250)
			rights = PlayerRights.EXTREME_DONATOR;
		if (player.getAmountDonated() >= 1000)
			rights = PlayerRights.EPIC_DONATOR;
		if (player.getAmountDonated() >= 2500)
			rights = PlayerRights.LEGENDARY_DONATOR;
		// if(player.getAmountDonated() >= 1000)
		// rights = PlayerRights.RUBY_MEMBER;
		if (rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage(
					"You've become a " + Misc.formatText(rights.toString().toLowerCase()) + "! Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}

}