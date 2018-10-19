package com.ruseps.world.content.achievement;

public enum Rewards {

	ABYSSAL_WHIP(4152,50,"easy"),
	RUNE_SCIMITAR(1333,5,"easy"),
	COWHIDE(1739,1,"medium"),
	ABYSSAL_WHIP_HARD(4151,50,"hard"),
	COWHIDE_ELITE(1739,1,"elite");
	
	private int itemId;
	private int amount;
	private String tier;
	
	Rewards(int itemId, int amount, String tier){
		this.setItemId(itemId);
		this.setAmount(amount);
		this.setTier(tier);
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
