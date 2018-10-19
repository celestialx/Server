package com.ruseps.world.content.combat.strategy.inferno;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class JalTokJad implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}
	
	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC JalTokJad = (NPC)entity;
		if(victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if(JalTokJad.getConstitution() <= 1000 && !JalTokJad.hasHealed()) {
			JalTokJad.performGraphic(gfx1);
			JalTokJad.setConstitution(JalTokJad.getConstitution() + Misc.getRandom(1500));
			JalTokJad.setHealed(true);
		}
		if(JalTokJad.isChargingAttack()) {
			return true;
		}
		int random = Misc.getRandom(10);
		if(random <= 8 && Locations.goodDistance(JalTokJad.getPosition().getX(), JalTokJad.getPosition().getY(), victim.getPosition().getX(), victim.getPosition().getY(), 2)) {
			JalTokJad.performAnimation(anim2);
			JalTokJad.getCombatBuilder().setContainer(new CombatContainer(JalTokJad, victim, 1, 2, CombatType.MELEE, true));
		} else if(random <= 4 && Locations.goodDistance(JalTokJad.getPosition().getX(), JalTokJad.getPosition().getY(), victim.getPosition().getX(), victim.getPosition().getY(), 14)) {
			JalTokJad.getCombatBuilder().setContainer(new CombatContainer(JalTokJad, victim, 1, 5, CombatType.MAGIC, true));
			JalTokJad.performAnimation(anim3);
			JalTokJad.setChargingAttack(true);
			TaskManager.submit(new Task(2, JalTokJad, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 0:
						new Projectile(JalTokJad, victim, gfx5.getId(), 44, 3, 85, 30, 0).sendProjectile();
						JalTokJad.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		} else {
			JalTokJad.getCombatBuilder().setContainer(new CombatContainer(JalTokJad, victim, 1, 5, CombatType.RANGED, true));
			JalTokJad.performAnimation(anim4);
			JalTokJad.performGraphic(gfx2);
			JalTokJad.setChargingAttack(true);
			TaskManager.submit(new Task(2, JalTokJad, false) {
				@Override
				public void execute() {
					victim.performGraphic(gfx4);
					JalTokJad.setChargingAttack(false);
					stop();
				}
			});
		}
		return true;
	}
	
	
	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 15;
	}

	//private static final Animation anim1 = new Animation(9254);
	private static final Animation anim2 = new Animation(7590);
	private static final Animation anim3 = new Animation(7592);
	private static final Animation anim4 = new Animation(7593);
	private static final Graphic gfx1 = new Graphic(444);
	private static final Graphic gfx2 = new Graphic(1625);
	private static final Graphic gfx3 = new Graphic(1626);
	private static final Graphic gfx4 = new Graphic(451);
	private static final Graphic gfx5 = new Graphic(1627);
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
