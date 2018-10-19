package com.ruseps.world.content.teleportation;

public class TeleportMinigames extends Teleporting {
	public static enum Minigames {
		TELEPORT_2(new String[] {"Barrows", "None"}, new int[] {3565, 3313, 0}, false),
		TELEPORT_3(new String[] {"Fight Caves", "None"}, new int[] {2445, 5177, 0}, false),
		TELEPORT_4(new String[] {"Fight Pits", "None"}, new int[] {2399, 5177, 0}, false),
		TELEPORT_5(new String[] {"Pest Control", "None"}, new int[] {2663, 2654, 0}, false),
		TELEPORT_6(new String[] {"Duel Arena", "None"}, new int[] {3364, 3267, 0}, false),
		TELEPORT_7(new String[] {"Warrior's Guild", "None"}, new int[] {2855, 3543, 0}, false),
		TELEPORT_8(new String[] {"Recipe For Disaster", "None"}, new int[] {3080, 3498, 0}, false),
		TELEPORT_9(new String[] {"Last Man Standing", "None"}, new int[] {2464, 4782, 0}, false),//teleports me to treassure island
		TELEPORT_10(new String[] {"Treasure Island", "None"}, new int[] {3039, 2910, 0}, false),
		TELEPORT_11(new String[] {"Inferno", "None"}, new int[] {2445, 5177, 0}, false);//doesn't work

		
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
		private Minigames(final String[] teleportName, final int[] teleportCoordinates, final boolean isWildy) {
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
