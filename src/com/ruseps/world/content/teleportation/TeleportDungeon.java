package com.ruseps.world.content.teleportation;

public class TeleportDungeon extends Teleporting {
	public static enum Dungeons {
		TELEPORT_1(new String[] {"Edgeville Dungeon", "2-172"}, new int[] {3097, 9870, 0}, false),
		TELEPORT_2(new String[] {"Slayer Tower", "8-124"}, new int[] {3429, 3538, 0}, false),
		TELEPORT_3(new String[] {"Brimhaven Dungeon", "0-100"}, new int[] {2713, 9564, 0}, false),
		TELEPORT_4(new String[] {"Taverley Dungeon", "0-100"}, new int[] {2884, 9797, 0}, false),
		TELEPORT_5(new String[] {"Strykewyrm Cavern", "110-210"}, new int[] {2731, 5095, 0}, false),
		TELEPORT_6(new String[] {"Ancient Cavern", "115-306"}, new int[] {1745, 5325, 0}, false),
		TELEPORT_7(new String[] {"Metal Dragons", "0-100"}, new int[] {2710, 9467,4, 0}, false), // doesn't work
		TELEPORT_9(new String[] {"WaterFall Dungeon", "0-100"}, new int[] {2575, 9874, 4, 0}, false);// doesn't work

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
		private Dungeons(final String[] teleportName, final int[] teleportCoordinates, final boolean isWildy) {
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
