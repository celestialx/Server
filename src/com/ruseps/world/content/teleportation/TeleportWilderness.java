package com.ruseps.world.content.teleportation;

/**
 * Teleport class for Skilling.
 * @author Tyler
 *
 */

public class TeleportWilderness extends Teleporting {
	
	public static enum Wilderness {
		
		TELEPORT_1(new String[] {"Green Dragons", "79"}, new int[] {3351, 3680, 0}, true),
		TELEPORT_2(new String[] {"Zombie Graveyard", "None"}, new int[] {3166, 3682, 0}, true),
		TELEPORT_3(new String[] {"Greater Demons", "None"}, new int[] {3288, 3886, 0}, true),
		TELEPORT_4(new String[] {"Wilderness Castle", "None"}, new int[] {3005, 3631, 0}, true),
		TELEPORT_5(new String[] {"West Dragons", "None"}, new int[] {2980, 3599, 0}, true),
		TELEPORT_6(new String[] {"East Dragons", "None"}, new int[] {3339, 3667, 0}, true),
		TELEPORT_7(new String[] {"Chaos Altar", "None"}, new int[] {3239, 3619, 0}, true),
		TELEPORT_8(new String[] {"Rune Rocks", "None"}, new int[] {3061, 3886, 0}, true),
		TELEPORT_9(new String[] {"Revenant Town", "None"}, new int[] {3651, 3486, 0}, true),
		TELEPORT_10(new String[] {"Rogues' Castle", "None"}, new int[] {3286, 3922, 0}, true),
		TELEPORT_11(new String[] {"Ice Plateau", "None"}, new int[] {2953, 3901, 0},  true),
		TELEPORT_12(new String[] {"Safe Pvp Portal", "None"}, new int[] {2815, 5511, 0}, false);
		
		/**
		 * Initializing the teleport names.
		 */
		private String[] teleportName;
		/**
		 * Initializing the teleport coordinates.
		 */
		private int[] teleportCoordinates;
		
		private boolean isWildy;

		/**
		 * Constructing the enumerator.
		 * @param teleportName
		 * 			The name of the teleport.
		 * @param teleportName2
		 * 			The secondary name of the teleport.
		 * @param teleportCoordinates
		 * 			The coordinates of the teleport.
		 */
		private Wilderness(final String[] teleportName, final int[] teleportCoordinates, final boolean isWildy) {
			this.teleportName = teleportName;
			this.teleportCoordinates = teleportCoordinates;
			this.isWildy = isWildy;
		}
		/**
		 * Setting the teleport name.
		 * @return
		 */
		public String[] getTeleportName() {
			return teleportName;
		}
		/**
		 * Setting the teleport coordinates.
		 * @return
		 */
		public int[] getCoordinates() {
			return teleportCoordinates;
		}
		
		public boolean getisWildy() {
			return isWildy;
		}
	}

}
