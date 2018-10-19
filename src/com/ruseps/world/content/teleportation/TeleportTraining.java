package com.ruseps.world.content.teleportation;



public class TeleportTraining extends Teleporting {
	
	public static enum Training {
		TELEPORT_1(new String[] {"Rock Crabs", "13"}, new int[] {2679, 3714, 0}, false),
		TELEPORT_2(new String[] {"Experiments", "25"}, new int[] {3561, 9948, 0}, false),
		TELEPORT_3(new String[] {"Bandit Camp", "0-100"}, new int[] {3169, 2982, 0}, false),
		TELEPORT_4(new String[] {"Yak Field", "0-100"}, new int[] {3206, 3263, 0}, false),
		TELEPORT_5(new String[] {"Ghoul Field", "79"}, new int[] {3420, 3510, 0}, false),
		TELEPORT_6(new String[] {"Armoured Zombies", "0-100"}, new int[] {3086, 9672, 0}, false),
		TELEPORT_7(new String[] {"Dust Devils", "0-100"}, new int[] {3277, 2964, 0}, false),
		//TELEPORT_8(new String[] {"Monkey Skeletons", "0-100"}, new int[] {2805, 9143, 0}, false),
	//	TELEPORT_9(new String[] {"Monkey Guards", "0-100"}, new int[] {2793, 2773, 0}, false),
		TELEPORT_10(new String[] {"Chaos Druids", "13"}, new int[] {2933, 9848, 0}, false),
		TELEPORT_11(new String[] {"Chicken Pen", "2"}, new int[] {3235, 3295, 0}, false),
		TELEPORT_12(new String[] {"Tzhaar Minions", "0-100"}, new int[] {2480, 5174, 0}, false);
		
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
		private Training(final String[] teleportName, final int[] teleportCoordinates, final boolean isWildy) {
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
