package mysql.impl.voting;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import com.ruseps.world.content.PlayerLogs;
import com.mysql.jdbc.Statement;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.content.Achievements;
import com.ruseps.world.content.Achievements.AchievementData;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.GameLoader;
import com.ruseps.model.Item;

public class Voting implements Runnable {

  private static int VOTES;
  
  Player player = null;
  String auth = null;
  
  public Voting(String auth, Player player) {
	  this.player = player;
	  this.auth = auth;
  }
	
  public void run() {
	
	Connection connection = null;
	Statement stmt = null;

	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
		} catch (Exception e) {
			e.printStackTrace();
			connection = null;
			stmt = null;
		}
	
	try {
		connection = DriverManager.getConnection("jdbc:mysql://ns536923.ip-144-217-68.net/simplici_vote","simplici_main", "H#tDHaFx4jWjo]");

	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
	
	

	if (connection != null) {
		 try {
			stmt = (Statement) connection.createStatement();
			String sql;
		    sql = "SELECT COUNT(*) FROM auth WHERE auth='" +auth.replaceAll("'", "\\\\'")+ "'";
		    ResultSet rs;
		    rs = stmt.executeQuery(sql);
		    rs.next();
		    int count;
		    count = rs.getInt(1);
		    if(count > 0) {
		    	
		    	  Item item = new Item(19670, GameLoader.getDay() == GameLoader.MONDAY ? 2 : 1);
		    	  player.getInventory().add(item, true); // replace	995, 1000000 with 19670, 1 to give a vote scroll instead of cash.
	              player.getPacketSender().sendMessage("Auth redeemed, thanks for voting!");
	              Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
	              int bonus = RandomUtility.RANDOM.nextInt(2000000);
	             
	              player.getInventory().add(995, Misc.getRandom(bonus));
	              player.getPacketSender().sendMessage("You a bonus "+bonus+" coins");
	              if(Misc.getRandom(15) == 7) {
	            	  player.getInventory().add(6199, 1);
	                  player.getPacketSender().sendMessage("You recieve a bonus mystery box!");

	              }
	             
	              PlayerLogs.log(player.getUsername(), "Player received vote reward!");
	              if(VOTES >= 20) {
	      			World.sendMessage("@red@[VOTING]@blu@ Another 20 votes have been claimed! Vote now using ::vote!");
	      			VOTES = 0;
	      		}
	      		VOTES++;
		     }
		     sql = "DELETE FROM auth WHERE auth='" +auth.replaceAll("'", "\\\\'")+ "'";
		     stmt.execute(sql);
		     rs.close();
		     stmt.close();
		     connection.close();
		     return;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	} else {
		System.out.println("Failed to make connection!");
		return;
	}
	return;
  }
}