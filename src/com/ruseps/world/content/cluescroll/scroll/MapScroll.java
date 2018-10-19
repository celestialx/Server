//package com.ruseps.world.content.cluescroll.scroll;
//
//import com.ruseps.model.Locations.Location;
//import com.ruseps.model.Position;
//import com.ruseps.world.content.cluescroll.Clue;
//import com.ruseps.world.content.cluescroll.Clue.ClueType;
//import com.ruseps.world.content.cluescroll.ClueDifficulty;
//import com.ruseps.world.content.cluescroll.ClueScroll;
//import com.ruseps.world.entity.impl.player.Player;
//
//public class MapScroll implements ClueScroll {
//	private final int scrollId;
//	private final ClueDifficulty difficulty;
//	private final Position endLocation;
//	private final Object[] data;
//	
//	public MapScroll(int scrollId, ClueDifficulty difficulty, Position endLocation, Object... data) {
//		this.scrollId = scrollId;
//		this.difficulty = difficulty;
//		this.endLocation = endLocation;
//		this.data = data;
//	}
//
//	@Override
//	public boolean inEndArea(Position location) {
//		return location.equals(endLocation);
//	}
//
//	@Override
//	public Clue getClue() {
//		return new Clue(ClueType.MAP, data);
//	}
//
//	@Override
//	public ClueDifficulty getDifficulty() {
//		return difficulty;
//	}
//
//	@Override
//	public boolean meetsRequirements(Player player) {
//		return inEndArea(player.getPosition());
//	}
//
//	@Override
//	public boolean execute(Player player) {
//		if (!player.getInventory().contains(scrollId)) {
//			return false;
//				
//		
//		}
//
//		reward(player, "You've found");
//		return true;
//	}
//
//	@Override
//	public int getScrollId() {
//		return scrollId;
//	}
//}