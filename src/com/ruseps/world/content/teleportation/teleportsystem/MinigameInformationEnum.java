package com.ruseps.world.content.teleportation.teleportsystem;

import com.ruseps.model.Position;

public enum MinigameInformationEnum {

	BARROWS(155151, "Barrows", new Position(1, 1, 1), new String[] {"Defeat the 6 barrows brothers", "to receive your barrows loot!"}),
	DUELARENA(155152, "Duel Arena", new Position(1, 1, 1), new String[] {"Are you a real fighter?", "Proof it by fighting in a duel,", "against other players!"}),
	FIGHTCAVES(155153, "Fight Caves", new Position(1, 1, 1), new String[] {"First rulr of the fight caves!", "You must never speak of the fight caves!"}),
	FISHING_TOURNAMENT(155154, "Fishing Tournament", new Position(1, 1, 1), new String[] {"No information yet!"}),
	WARRIORS_GUILD(155155, "Warrior's guild", new Position(1, 1, 1), new String[] {"Get tokens, fight cyclopses", "and get your defenders!"}),
	PEST_CONTROL(155156, "Pest Control", new Position(1, 1, 1), new String[] {"Defend the void knight", "win the games and get your void armour!"});
	
	public int buttonId;
	public String mininame;
	public Position pos;
	public String[] information;
	
	MinigameInformationEnum(int buttonId, String mininame, Position pos, String[] information) {
		this.buttonId = buttonId;
		this.mininame = mininame;
		this.pos = pos;
		this.information = information;
	}
	
	public int getButtonId() {
		return buttonId;
	}
	public String getMiniName() {
		return mininame;
	}
	public Position getPos() {
		return pos;
	}
	public String[] getInformation() {
		return information;
	}
}
