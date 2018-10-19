package com.ruseps.model.definitions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.ruseps.model.container.impl.Equipment;

/**
 * This file manages every item definition, which includes
 * their name, description, value, skill requirements, etc.
 * 
 * @author relex lawl
 */

public class ItemDefinition {
	
	/**
	 * The directory in which item definitions are found.
	 */
	private static final String FILE_DIRECTORY = "./data/def/txt/items.txt";

	/**
	 * The max amount of items that will be loaded.
	 */
	private static final int MAX_AMOUNT_OF_ITEMS = 22694;
	
	private static int[] customItem = {11592, 11593, 11594, 11595, 11596, 11597, 11598, 11599, 11600, 11601, 11602, 11603, 11604, 11605, 11606, 11607, 11608, 11526, 11527, 695, 625, 624, 623, 622, 621, 620, 619, 618, 665, 666, 669, 670, 671, 672, 673, 3812, 3080, 3305, 3308, 3647, 3648, 3649, 3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658, 3659, 3660, 3661, 8675, 8677, 3619, 3641, 3642, 667, 13506, 1231, 1249, 1263, 13479, 13478, 13462, 4180, 3072, 3073, 14484, 910, 911, 912, 3091, 914, 3089, 896, 924, 2380, 909, 898, 899, 900, 901, 902, 903, 2548, 2547, 904, 3088, 906, 907, 908, 926, 3878, 82, 894, 895, 11609, 20692, 6201, 20690, 14581, 14582, 14583, 14584, 14585, 14586, 3090, 3811, 3287, 3288, 3289, 3290, 3292, 3293, 3294, 3295, 3296, 3297, 3298, 3299, 6756, 11292, 3869, 84, 4204, 758, 788, 983, 5, 1544, 21077, 11586, 11587, 11588, 11589, 11590, 11591  };	/**
	 * ItemDefinition array containing all items' definition values.
	 */
	private static ItemDefinition[] definitions = new ItemDefinition[MAX_AMOUNT_OF_ITEMS];
	
	/**
	 * Loading all item definitions
	 */
	public static void init() {
		ItemDefinition definition = definitions[0];
		try {
			File file = new File(FILE_DIRECTORY);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("inish")) {
					definitions[definition.id] = definition;
					continue;
				}
				String[] args = line.split(": ");
				if (args.length <= 1)
					continue;
				String token = args[0], value = args[1];
				if (line.contains("Bonus[")) {
					String[] other = line.split("]");
					int index = Integer.valueOf(line.substring(6, other[0].length()));
					double bonus = Double.valueOf(value);
					definition.bonus[index] = bonus;
					continue;
				}
				if (line.contains("Requirement[")) {
					String[] other = line.split("]");
					int index = Integer.valueOf(line.substring(12, other[0].length()));
					int requirement = Integer.valueOf(value);
					definition.requirement[index] = requirement;
					continue;
				}
				switch (token.toLowerCase()) {
				case "item id":
					int id = Integer.valueOf(value);
					definition = new ItemDefinition();
					definition.id = id;
					break;
				case "name":
					if(value == null)
						continue;
					definition.name = value;
					break;
				case "examine":
					definition.description = value;
					break;
				case "value":
					int price = Integer.valueOf(value);
					definition.value = price;
					break;
				case "stackable":
					definition.stackable = Boolean.valueOf(value);
					break;
				case "noted":
					definition.noted = Boolean.valueOf(value);
					break;
				case "double-handed":
					definition.isTwoHanded = Boolean.valueOf(value);
					break;
				case "equipment type":
					definition.equipmentType = EquipmentType.valueOf(value);
					break;
				case "is weapon":
					definition.weapon = Boolean.valueOf(value);
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static ItemDefinition[] getDefinitions() {
		return definitions;
	}
	
	/**
	 * Gets the item definition correspondent to the id.
	 * 
	 * @param id	The id of the item to fetch definition for.
	 * @return		definitions[id].
	 */
	public static ItemDefinition forId(int id) {
		return (id < 0 || id > definitions.length || definitions[id] == null) ? new ItemDefinition() : definitions[id];
	}
	
	/**
	 * Gets the max amount of items that will be loaded
	 * in Niobe.
	 * @return	The maximum amount of item definitions loaded.
	 */
	public static int getMaxAmountOfItems() {
		return MAX_AMOUNT_OF_ITEMS;
	}
	
	/**
	 * The id of the item.
	 */
	private int id = 0;
	
	/**
	 * Gets the item's id.
	 * 
	 * @return id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * The name of the item.
	 */
	private String name = "None";
	
	/**
	 * Gets the item's name.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The item's description.
	 */
	private String description = "Null";
	
	/**
	 * Gets the item's description.
	 * 
	 * @return	description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Flag to check if item is stackable.
	 */
	private boolean stackable;
	
	/**
	 * Checks if the item is stackable.
	 * 
	 * @return	stackable.
	 */
	public boolean isStackable() {
		if(noted)
			return true;
		return stackable;
	}
	
	public boolean isCustom() {
		for (int i = 0; i<customItem.length; i++) {
			if (getId() == customItem[i]) {
				return true;
			}	
		}
		return false;
	}
	/**
	 * The item's shop value.
	 */
	private int value;
	
	/**
	 * Gets the item's shop value.
	 * 
	 * @return	value.
	 */
	public int getValue() {
		return isNoted() ? ItemDefinition.forId(getId() - 1).value : value;
	}
	
	/**
	 * Gets the item's equipment slot index.
	 * 
	 * @return	equipmentSlot.
	 */
	public int getEquipmentSlot() {
		return equipmentType.slot;
	}
	
	/**
	 * Flag that checks if item is noted.
	 */
	private boolean noted;
	
	/**
	 * Checks if item is noted.
	 * 
	 * @return noted.
	 */
	public boolean isNoted() {
		return noted;
	}
		
	private boolean isTwoHanded;
	
	/**
	 * Checks if item is two-handed
	 */
	public boolean isTwoHanded() {
		return isTwoHanded;
	}
	
	private boolean weapon;
	
	public boolean isWeapon() {
		return weapon;
	}
	
	private EquipmentType equipmentType = EquipmentType.WEAPON;
	
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	
	/**
	 * Checks if item is full body.
	 */
	public boolean isFullBody() {
		return equipmentType.equals(EquipmentType.PLATEBODY);
	}
	
	/**
	 * Checks if item is full helm.
	 */
	public boolean isFullHelm() {
		return equipmentType.equals(EquipmentType.FULL_HELMET);
	}
	
	private double[] bonus = new double[18];
	
	public double[] getBonus() {
		return bonus;
	}
	
	private int[] requirement = new int[25];

	public int[] getRequirement() {
		return requirement;
	}
	
	public static enum EquipmentType {
		HAT(Equipment.HEAD_SLOT),
		CAPE(Equipment.CAPE_SLOT),
		SHIELD(Equipment.SHIELD_SLOT),
		GLOVES(Equipment.HANDS_SLOT),
		BOOTS(Equipment.FEET_SLOT),
		AMULET(Equipment.AMULET_SLOT),
		RING(Equipment.RING_SLOT),
		ARROWS(Equipment.AMMUNITION_SLOT),
		FULL_MASK(Equipment.HEAD_SLOT),
		FULL_HELMET(Equipment.HEAD_SLOT),
		BODY(Equipment.BODY_SLOT),
		PLATEBODY(Equipment.BODY_SLOT),
		LEGS(Equipment.LEG_SLOT),
		WEAPON(Equipment.WEAPON_SLOT);
		
		private EquipmentType(int slot) {
			this.slot = slot;
		}
		
		private int slot;
	}
	
	@Override
	public String toString() {
		return "[ItemDefinition(" + id + ")] - Name: " + name + "; equipment slot: " + getEquipmentSlot() + "; value: "
				+ value + "; stackable ? " + Boolean.toString(stackable) + "; noted ? " + Boolean.toString(noted) + "; 2h ? " + isTwoHanded;
	}
	
	public static int getItemId(String itemName) {
		for (int i = 0; i < MAX_AMOUNT_OF_ITEMS; i++) {
			if (definitions[i] != null) {
				if (definitions[i].getName().equalsIgnoreCase(itemName)) {
					return definitions[i].getId();
				}
			}
		}
		return -1;
	}
}
