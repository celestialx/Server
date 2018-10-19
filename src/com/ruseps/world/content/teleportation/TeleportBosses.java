package com.ruseps.world.content.teleportation;

public class TeleportBosses extends Teleporting  {

	public static enum Bosses {
		TELEPORT_1(new String[] {"King Black Dragon", "276"}, new int[] {2275, 4688, 0}, false),
		TELEPORT_2(new String[] {"Godwars Dungeon", "13-580"}, new int[] {2871, 5319, 2}, false),
		TELEPORT_3(new String[] {"Kalphite Queen", "28-333"}, new int[] {3488, 9516, 0}, false),
		TELEPORT_4(new String[] {"Slash Bash", "111"}, new int[] {3659, 3525, 0}, false),
		TELEPORT_5(new String[] {"Dagannoth Kings", "76-303"}, new int[] {1912, 4367, 0}, false),
		TELEPORT_6(new String[] {"Tormented Demons", "450"}, new int[] {2540, 5774, 0}, false),
		TELEPORT_7(new String[] {"Chaos Elemental", "305"}, new int[] {3276, 3915, 0}, true),
		TELEPORT_8(new String[] {"Corporeal Beast", "785"}, new int[] {2886, 4376, 0}, false),
		TELEPORT_9(new String[] {"Bork Cave", "267"}, new int[] {3104, 5536, 0}, false),
		TELEPORT_10(new String[] {"Barrelchest", "170"}, new int[] {2973, 9517, 3}, false),
		TELEPORT_11(new String[] {"Lizardman Shaman", "150"}, new int[] {2718, 9811, 1}, false),
		TELEPORT_12(new String[] {"Phoenix", "235"}, new int[] {2839, 9557, 0}, false),
		TELEPORT_13(new String[] {"Bandos Avatar", "299"}, new int[] {2891, 4767, 0}, false),
		TELEPORT_14(new String[] {"Glacors Cave", "188"}, new int[] {3050, 9573, 0}, false),
		TELEPORT_15(new String[] {"Nex", "285-1001"}, new int[] {2903, 5203, 0}, false),
		TELEPORT_16(new String[] {"Scorpia", "464"}, new int[] {3236, 3941, 0}, true),
		TELEPORT_17(new String[] {"Skotizo", "321"}, new int[] {3378, 9816, 0}, false),
		TELEPORT_18(new String[] {"Abby Sire", "150"}, new int[] {3370, 3888, 0}, true),
		TELEPORT_19(new String[] {"Giant Mole", "230"}, new int[] {1761, 5181, 0}, false),
		TELEPORT_20(new String[] {"Kraken", "0-100"}, new int[] {3696, 5807, 15}, false),
		TELEPORT_21(new String[] {"Venenatis", "464"}, new int[] {3350, 3734, 0}, true),
		TELEPORT_22(new String[] {"Cerberus", "318"}, new int[] {1240, 1243, 0}, false),
		TELEPORT_23(new String[] {"Callisto", "318"}, new int[] {3300, 3835, 0}, true),
		TELEPORT_24(new String[] {"Mutant", "318"}, new int[] {2980, 3763, 0}, true),
		TELEPORT_25(new String[] {"Tornado", "318"}, new int[] {3307, 3916, 0}, true),



		;
		/*
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
		private Bosses(final String[] teleportName, final int[] teleportCoordinates, final boolean isWildy) {
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
