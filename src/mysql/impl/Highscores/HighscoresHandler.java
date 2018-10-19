package mysql.impl.Highscores;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.mysql.jdbc.Statement;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.world.entity.impl.player.Player;

public class HighscoresHandler implements Runnable{




	private Player player;

	public HighscoresHandler(Player player) {
		this.player = player;
	}

	/**
	 * Function that handles everything, it inserts or updates
	 * user data in database
	 */
	@Override
	public void run() {
		
		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER)
			return;
		/**
		 * Players username
		 */
		final String username = player.getUsername();
		/**
		 * Represents game mode
		 * If you want to set game modes do this:
		 */
		final int gameMode = 0;
		/**
		 * Represents overall xp
		 */
		final long overallXp = player.getSkillManager().getTotalExp();
		/**
		 * Represents attack xp
		 */
		final long attackXp = player.getSkillManager().getExperience(Skill.ATTACK);
		/**
		 * Represents defence xp
		 */
		final long defenceXp = player.getSkillManager().getExperience(Skill.DEFENCE);
		/**
		 * Represents strength xp
		 */
		final long strengthXp = player.getSkillManager().getExperience(Skill.STRENGTH);
		/**
		 * Represents constitution xp
		 */
		final long constitutionXp = player.getSkillManager().getExperience(Skill.CONSTITUTION);
		/**
		 * Represents ranged xp
		 */
		final long rangedXp = player.getSkillManager().getExperience(Skill.RANGED);
		/**
		 * Represents prayer xp
		 */
		final long prayerXp = player.getSkillManager().getExperience(Skill.PRAYER);
		/**
		 * Represents magic xp
		 */
		final long magicXp = player.getSkillManager().getExperience(Skill.MAGIC);
		/**
		 * Represents cooking xp
		 */
		final long cookingXp = player.getSkillManager().getExperience(Skill.COOKING);
		/**
		 * Represents woodcutting xp
		 */
		final long woodcuttingXp = player.getSkillManager().getExperience(Skill.WOODCUTTING);
		/**
		 * Represents fletching xp
		 */
		final long fletchingXp = player.getSkillManager().getExperience(Skill.FLETCHING);
		/**
		 * Represents fishing xp
		 */
		final long fishingXp = player.getSkillManager().getExperience(Skill.FISHING);
		/**
		 * Represents firemaking xp
		 */
		final long firemakingXp = player.getSkillManager().getExperience(Skill.FIREMAKING);
		/**
		 * Represents crafting xp
		 */
		final long craftingXp = player.getSkillManager().getExperience(Skill.CRAFTING);
		/**
		 * Represents smithing xp
		 */
		final long smithingXp = player.getSkillManager().getExperience(Skill.SMITHING);
		/**
		 * Represents mining xp
		 */
		final long miningXp = player.getSkillManager().getExperience(Skill.MINING);
		/**
		 * Represents herblore xp
		 */
		final long herbloreXp = player.getSkillManager().getExperience(Skill.HERBLORE);
		/**
		 * Represents agility xp
		 */
		final long agilityXp = player.getSkillManager().getExperience(Skill.AGILITY);
		/**
		 * Represents thieving xp
		 */
		final long thievingXp = player.getSkillManager().getExperience(Skill.THIEVING);
		/**
		 * Represents slayer xp
		 */
		final long slayerXp = player.getSkillManager().getExperience(Skill.SLAYER);
		/**
		 * Represents farming xp
		 */
		final long farmingXp = player.getSkillManager().getExperience(Skill.FARMING);
		/**
		 * Represents runecrafting xp
		 */
		final long runecraftingXp = player.getSkillManager().getExperience(Skill.RUNECRAFTING);
		/**
		 * Represents hunter xp
		 */
		final long hunterXp = player.getSkillManager().getExperience(Skill.HUNTER);
		/**
		 * Represents construction xp
		 */
		final long constructionXp = player.getSkillManager().getExperience(Skill.CONSTRUCTION);
		final long summoningXp = player.getSkillManager().getExperience(Skill.SUMMONING);
		final long dungXp = player.getSkillManager().getExperience(Skill.DUNGEONEERING);
		/**
		 * Creates new instance of jdbc driver
		 * if that driver exists
		 */
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		/**
		 * Sets Connection variable to null
		 */
		Connection connection = null;
		/**
		 * Sets Statement variable to null
		 */
		Statement stmt = null;

		/**
		 * Attempts connecting to database
		 */
		try {
			connection = DriverManager.getConnection("jdbc:mysql://ns536923.ip-144-217-68.net:3306/simplici_hiscores", "simplici_main", "H#tDHaFx4jWjo]");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		/**
		 * Checks if connection isnt null
		 */
		if (connection != null) {
		    try {
		    	stmt = (Statement) connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM `hs_users` WHERE username='" +username+ "'");
				if(rs.next()) {
					if(rs.getInt("count") > 0)  {
						stmt.executeUpdate("UPDATE `hs_users` SET overall_xp = '"+overallXp+"', attack_xp = '"+attackXp+"', defence_xp = '"+defenceXp+"', strength_xp = '"+strengthXp+"', constitution_xp = '"+constitutionXp+"', ranged_xp = '"+rangedXp+"', prayer_xp = '"+prayerXp+"', magic_xp = '"+magicXp+"', cooking_xp = '"+cookingXp+"', woodcutting_xp = '"+woodcuttingXp+"', fletching_xp = '"+fletchingXp+"', fishing_xp = '"+fishingXp+"', firemaking_xp = '"+firemakingXp+"', crafting_xp = '"+craftingXp+"', smithing_xp = '"+smithingXp+"', mining_xp = '"+miningXp+"', herblore_xp = '"+herbloreXp+"', agility_xp = '"+agilityXp+"', thieving_xp = '"+thievingXp+"', slayer_xp = '"+slayerXp+"', farming_xp = '"+farmingXp+"', runecrafting_xp = '"+runecraftingXp+"', hunter_xp = '"+hunterXp+"', construction_xp = '"+constructionXp+"', summoning_xp = '"+summoningXp+"', dungeoneering_xp = '"+dungXp+"' WHERE username = '"+username+"'");
					} else {
						stmt.executeUpdate("INSERT INTO `hs_users` (username, rights, overall_xp, attack_xp, defence_xp, strength_xp, constitution_xp, ranged_xp, prayer_xp, magic_xp, cooking_xp, woodcutting_xp, fletching_xp, fishing_xp, firemaking_xp, crafting_xp, smithing_xp, mining_xp, herblore_xp, agility_xp, thieving_xp, slayer_xp, farming_xp, runecrafting_xp, hunter_xp, construction_xp, summoning_xp, dungeoneering_xp) VALUES ('"+username+"', '"+gameMode+"', '"+overallXp+"', '"+attackXp+"', '"+defenceXp+"', '"+strengthXp+"', '"+constitutionXp+"', '"+rangedXp+"', '"+prayerXp+"', '"+magicXp+"', '"+cookingXp+"', '"+woodcuttingXp+"', '"+fletchingXp+"', '"+fishingXp+"', '"+firemakingXp+"', '"+craftingXp+"', '"+smithingXp+"', '"+miningXp+"', '"+herbloreXp+"', '"+agilityXp+"', '"+thievingXp+"', '"+slayerXp+"', '"+farmingXp+"', '"+runecraftingXp+"', '"+hunterXp+"', '"+constructionXp+"','"+summoningXp+"', '"+dungXp+"')");
					}
				}
				stmt.close();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}

		return;
	}
}
