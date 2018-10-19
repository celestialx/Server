package com.ruseps.world.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ruseps.model.PlayerRights;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

/**
 * @author Ajw
 */
public class StaffList {

	public static List<String> staff = new ArrayList<>();

	public static String getName(String name) {
		String[] split = name.split(" ");
		return split[1].substring(split[1].lastIndexOf('@') + 1);
	}
	
	public static void showStaff(Player player) {
		for (int i = 0; i < staff.size(); i++) {
			String name = staff.get(i);
			player.getPacketSender().sendMessage(""+name);
		}
	}


	public static void login(Player player) {
		staff.add(getPrefix(player) + " @bla@" + player.getUsername());
	}
	public static void logout(Player player) {
		if (staff.contains(getPrefix(player) + " @bla@" + player.getUsername())) {
			staff.remove(getPrefix(player) + " @bla@" + player.getUsername());
		}
	}

	public static String getPrefix(Player player) {
		String crown = "";
		switch (player.getRights()) {
		case MODERATOR:
			crown = "<img=1>";
			break;
		case SUPPORT:
			crown = "<img=10>";
			break;
		case ADMINISTRATOR:
			crown = "<img=2>";
			break;
		case OWNER:
			crown = "<img=3>";
			break;
		case MANAGER:
			crown = "<img=13>";
			break;
		case DEVELOPER:
			crown = "<img=4>";
			break;
		default:
			break;
		}
		return crown;
	}

}
