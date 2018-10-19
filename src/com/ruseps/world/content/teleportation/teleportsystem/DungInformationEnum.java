package com.ruseps.world.content.teleportation.teleportsystem;

import com.ruseps.model.Position;

public enum DungInformationEnum {

	EDGEVILLE(151175, "Edgeville Dungeon", new Position(1, 1, 1), new String[] {"Low level monsters,", "train your first few levels here!"}),
	BRIMHAVEN(151176, "Brimhaven Dungeon", new Position(1, 1, 1), new String[] {"Medium to high level monsters,", "home of the iron- and steel dragon!"}),
	RELLEKKA(151177, "Rellekka Dungeon", new Position(1, 1, 1), new String[] {"Low to medium level monsters,", "most slayer creatures are found here!"}),
	TAVERLEY(151178, "Taverley Dungeon", new Position(1, 1, 1), new String[] {"Low to high level monsters,", "home of the blue dragons!"});
	
	public int buttonId;
	public String dungname;
	public Position pos;
	public String[] information;
	
	DungInformationEnum(int buttonId, String dungname, Position pos, String[] information) {
		this.buttonId = buttonId;
		this.dungname = dungname;
		this.pos = pos;
		this.information = information;
	}
	
	public int getButtonId() {
		return buttonId;
	}
	public String getDungName() {
		return dungname;
	}
	public Position getPos() {
		return pos;
	}
	public String[] getInformation() {
		return information;
	}
}
