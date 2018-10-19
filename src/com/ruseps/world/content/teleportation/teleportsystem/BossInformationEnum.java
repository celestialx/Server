package com.ruseps.world.content.teleportation.teleportsystem;

import com.ruseps.model.Position;

/*
*@Author harryl
*
*/
public enum BossInformationEnum {
	ABBY(-28705, "Abyssal Sire", 5886, new int[][] { /*{11613,1},*/ {13047,1}, {13045,1}},
			new String[] {"The engineer of the abyss", "@red@Level 350@whi@ boss with @red@450 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @red@Level 45","","","","","",""}, new Position(3333, 3864, 0)),
	BANDOSAVATAR(-28704, "Bandos Avatar", 4540, new int[][] { /*{11613,1},*/ {11726,1}, {11724,1},{11728,1}, {11704,1}, {15259,1}},
			new String[] {"The highest priest", "@red@Level 299@whi@ boss with @red@1050 hp", "@whi@He attacks with melee", " and magic", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2891, 4767, 0)),
	BARRELCHEST(-28703, "Barrelchest", 5666, new int[][] { /*{11613,1},*/ {19780,1}, {10887,1}},
			new String[] {"The barrel dropper", "@red@Level 170@whi@ boss with @red@796 hp", "@whi@He attacks with melee", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2973, 9517, 1)),
	BORK(-28702, "Bork", 7134, new int[][] { /*{11613,1},*/ {17291,1}},
			new String[] {"The biggest ork", "@red@Level 268@whi@ boss with @red@867 hp", "@whi@He attacks with melee", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(3104, 5536, 0)),
	CALLISTO(-28701, "Callisto", 2009, new int[][] { /*{11613,1},*/ },
			new String[] {"The big bear", "@red@Level 470@whi@ boss with @red@500 hp", "@whi@He attacks with melee", "", "","Max hit: ","@red@Wilderness: Level 40","","","","","",""}, new Position(3300, 3835, 0)),
	CERBERUS(-28700, "Cerberus", 1999, new int[][] { /*{11613,1},*/{13235,1},{12708,1},{13239,1},{6585,1} },
			new String[] {"The gigantic hound", "@red@Level 1999@whi@ boss with @red@900 hp", "@whi@He attacks with all styles", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(1240, 1243, 0)),
	CHAOSELEMENTAL(-28699, "Chaos Elemental", 3200, new int[][] { {20057,1}, {20058,1}, {20059,1}, {11995,1} },
			new String[] {"The cloud with tentacles", "@red@Level 305@whi@ boss with @red@1800 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @red@Level 50 (Multi)","","","","","",""}, new Position(3276, 3915, 0)),
	CORP(-28698, "Corporeal Beast", 8133, new int[][] { {13752,1}, {13746,1}, {13748,1}, {13750,1},{12001,1} },
			new String[] {"The boss of bosses", "@red@Level 785@whi@ boss with @red@2850 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2886, 4376, 0)),
	DAGANNOTH(-28697, "Dagannoth Kings", -1, new int[][] { {6731, 1}, {6733, 1}, {6735, 1}, {6737, 1}, {6739, 1},{11990,1},{12005,1},{12006,1} },
			new String[] {"@yel@Defeat the various kings:", "@or1@Dagannoth Prime", "@or1@Dagannoth Supreme", "@or1@Dagannoth Rex", "","@whi@These kings use all attack","@whi@styles.","","","","","","","",""}, new Position(1912, 4367, 0)),
	GIANTMOLE(-28696, "Giant Mole", 3340, new int[][] { },
			new String[] {"The giant mistake", "@red@Level 230@whi@ boss with @red@500 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(1761, 5181, 0)),
	GIANTROC(-28695, "Giant Roc", 4972, new int[][] { {3653,1},{8421,1},{3656,1},{20690,1},{6201,2},{20058,1},{3648,1},{3655,1},{3658,1},{36570,1},{3654,1},{3650,1},{3649,1},{3647,1}},
			new String[] {"The formidable demons", "@red@Level 172@whi@ boss with @red@1000 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @red@Level 50 (Multi)","","","","","",""}, new Position(3307, 3916, 0)),
	GLACOR(-28694, "Glacor", 1382, new int[][] {{20002,1},{20001,1},{20000,1}},
			new String[] {"The rare ice elemental", "@red@Level 186@whi@ boss with @red@698 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(3050, 9573, 0)),
	GODWARS(-28693, "Godwars", -1, new int[][] { {11724, 1}, {11726, 1}, {11718, 1}, {11720, 1}, {11722, 1}, {11716, 1}, {11730,1}, {11997,1},{12002,1},{12004,1},{12003,1}},
			new String[] {"@yel@Defeat the various gods:", "God of Armadyl", "@gre@God of Bandos", "@cya@God of Saradomin", "@red@God of Zamorak","","","","","","","",""}, new Position(2871, 5319, 2)),
	KALPHITEQUEEN(-28692, "Kalphite Queen", 1158, new int[][] { {2513, 1}, {11993, 1} },
			new String[] {"The nightmare of every", "sailor. ", "@red@Level 291 @whi@boss with @red@255 @whi@hp", "She attacks with magic","","Max Hit:","Wilderness: @gre@No","", "","","","","",""}, new Position(3488, 9516, 0)),//
	KBD(-28691, "King Black Dragon", 50, new int[][] { /*{11613,1},*/ {11286,1}, {11732,1}, {6585,1}, {11996,1} },
			new String[] {"The fire breathing shadow", "@red@Level 276@whi@ boss with @red@450 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2275, 4688, 0)),
	KRAKEN(-28690, "Kraken", 3847, new int[][] { /*{11613,1},*/ {11554,1}, {13058,1}, {2581,1}, {2577,1} },
			new String[] {"The tentacle master", "@red@Level 291@whi@ boss with @red@555 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(3696, 5807, 0)),
	LIZARDMAN(-28689, "Lizardman Shaman", 6766, new int[][] { /*{11613,1},*/ {20555,1}},
			new String[] {"The big lizard", "@red@Level 150@whi@ boss with @red@472 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2718, 9811, 0)),
	MUTANT(-28688, "Mutant", 5421, new int[][] { /*{11613,1},*/ {3653,1},{8421,1},{3656,1},{20690,1},{6201,2},{20058,1},{3648,1},{3655,1},{3658,1},{36570,1},{3654,1},{3650,1},{3649,1},{3647,1}},
			new String[] {"The disfigured beast", "@red@Level 69@whi@ boss with @red@900 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @red@Level 31","","","","","",""}, new Position(2980, 3763, 0)),
	NEX(-28687, "Nex", 13447, new int[][] { /*{11613,1},*/ {14008,1},{14009,1},{14010,1},{14011,1},{14012,1},{14013,1},{14014,1},{14015,1},{14016,1},{11987,1}},
			new String[] {"The insane mage", "@red@Level 202@whi@ boss with @red@255 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2903, 5203, 0)),
	PHOENIX(-28686, "Phoenix", 8549, new int[][] { /*{11613,1},*/ {14062,1},{20014,1},{20013,1},{20019,1},{20020,1},{20011,1},{20012,1},{20021,1},{20022,1},{20018,1},{20017,1},{20016,1}},
			new String[] {"The insane mage", "@red@Level 202@whi@ boss with @red@255 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2839, 9557, 0)),
	SCORPIA(-28685, "Scorpia", 2001, new int[][] { /*{11613,1},*/ {11924,1},{11732,1},{6889,1}},
			new String[] {"The most powerful scorpian", "@red@Level 464@whi@ boss with @red@400 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @red@Level 53","","","","","",""}, new Position(3236, 3941, 0)),
	
	SKOTIZO(-28684, "Skotizo", 7286, new int[][] { /*{11613,1},*/ {16955,1},{16403,1},{16425,1}},
			new String[] {"The demonic demi-god", "@red@Level 464@whi@ boss with @red@400 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(3378, 9816, 0)),
	SLASHBASH(-28683, "Slash Bash", 2060, new int[][] { {11994,1}, {15501,1}, {15259,1}, {2366,1},{2368,1} },
			new String[] {"The famous flesh eater", "@red@Level 111@whi@ boss with @red@400 hp", "@whi@He attacks with melee.", "", "","Max hit:@red@ 50","Wilderness: @gre@No","","","","","",""}, new Position(2880, 3592, 0)),
	TORMENTED(-28682, "Tormented Demons", 8349, new int[][] { {14484,1}, {14472,1}, {14474,1}, {14476,1},{11992,1} },
			new String[] {"The formidable demons", "@red@Level 450@whi@ boss with @red@400 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(2540, 5774, 0)),

	VENENATIS(-28681, "Venenatis", 2000, new int[][] {},
			new String[] {"The most poisonous boss", "@red@Level 464@whi@ boss with @red@315 hp", "@whi@He attacks with all styles.", "", "","Max hit:@red@ ","Wilderness: @gre@No","","","","","",""}, new Position(3350, 3734, 0)),
	
	
	
	/*
	BARRELCHEST(-28699, "Barrelchest", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"The boss who is known from", "the quest Great Brain Robbery.", "This level 190 boss has 134 hp", "The boss uses melee", "and has a max hit of 60"}, new Position(3263, 3676, 0)),
	*/
	/*CALLISTO(-28698, "Callisto", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"The big bear who wanders", "around the south of the Demonic", "Ruins. This level 470 boss attacks", "you with melee and has 255 hp.", "The boss uses melee and", "has a max hit of 60"}, new Position(3315, 3829, 0)),
	
	CORP(-28696, "Corporeal Beast", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"The most challenging boss", "This level 785 boss has 2000", "hp and attacks you with melee", "and magic. The melee max hit is", "51 and magic max hit is 65"}, new Position(2976, 4384, 0)),
	NEX(-28695, "Nex", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"This insane mage resides", "west of the lava maze", "This level 202 boss has 225 hp", "and attack you with magic.", "He also has a special attack!"}, new Position(2990, 3850, 0)),
	VETION(-28694, "Vetion", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
	        new String[] {"The armored skeleton", "this level 454 boss has", "255 hp and attacks with", "and magic. The max hit is 45"}, new Position(2871, 5319, 2)),

	LIZARDSHAMAN(-28693, "Lizardman Shaman", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"That's a big lizard", "This level 150 boss has", "150 hp and attacks with melee", "and magic. the max his is 50",}, new Position(1495, 3700, 0)),
	VENENATIS(-28692, "Venenatis", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
			new String[] {"The biggest, most poisonous", "This level 464 boss has", "225 hp and attacks with melee", "and magic. the max his is 50",}, new Position(3352, 3730, 0)),
MUTANT(-28691, "Mutant", new int[][] { {995, 50000000}, {13179, 1}, {13180, 1}, {995, 5000000}, {13307, 400}, {13652, 1} },
		new String[] {"Mutant Monster", "This level 500 boss has", "800 hp and attacks with melee", "and magic. the max his is 50",}, new Position(2980, 3763, 0))*/;
	
	public void setItemdisplay(int[][] itemdisplay) {
				this.itemdisplay = itemdisplay;
			}

	public int buttonId;
	public String bossName;
	public int bossId;
	public int[][] itemdisplay;


	public String[] information;


	public Position pos;

	BossInformationEnum(int buttonId, String bossName, int bossId, int[][] itemdisplay, String[] information, Position pos) {
		this.buttonId = buttonId;
		this.bossName = bossName;
		this.bossId = bossId;
		this.itemdisplay = itemdisplay;
		this.information = information;
		this.pos = pos;
	}

	public void setItems(int[][] items) {
		this.itemdisplay = items;
	}
	public int getButtonId() {
		return buttonId;
	}

	public String getBossName() {
		return bossName;
	}
	
	public int getBossId() {
		return bossId;
	}
	public int[][] getItemdisplay() {
		return itemdisplay;
	}
	
	public String[] getInformation() {
		return information;
	}
	
	public Position getPos() {
		return pos;
	}
	
}
