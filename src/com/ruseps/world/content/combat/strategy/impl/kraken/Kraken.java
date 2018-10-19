package com.ruseps.world.content.combat.strategy.impl.kraken;

import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class Kraken implements CombatStrategy {

	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		NPC kraken = (NPC)entity;
		kraken.prepareSpell(CombatSpells.KRAKEN_STRIKE.getSpell(), victim);
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public int attackDelay(Character entity) {
		return 5;
	}

	@Override
	public int attackDistance(Character entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

}