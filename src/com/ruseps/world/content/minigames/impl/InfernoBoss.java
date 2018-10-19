package com.ruseps.world.content.minigames.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Locations;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class InfernoBoss {

	public static void hit(Player c, NPC TzKalZuk, NPC Glyph) {
		if (c.getLocation() != Location.INFERNO) {
			return;
		}
		TzKalZuk.setChargingAttack(true);
		TzKalZuk.performAnimation(mage);
		TaskManager.submit(new Task(1, TzKalZuk, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 0:
					new Projectile(TzKalZuk, Glyph, mageGfx.getId(), 50, 5, 90, 38, 0).sendProjectile();
					TzKalZuk.setChargingAttack(false);
					break;
				case 2:
					Glyph.performAnimation(new Animation(7568));
					stop();
					break;
				}
				tick++;
			}
		});
	}

	public static void hitplayer(Player c, NPC TzKalZuk, NPC Glyph) {
		if (c.getLocation() != Location.INFERNO) {
			return;
		}
		TzKalZuk.setChargingAttack(true);
		TzKalZuk.getCombatBuilder().setContainer(new CombatContainer(TzKalZuk, c, 1, 4, CombatType.MAGIC, true));
		TzKalZuk.performAnimation(mage);
		TaskManager.submit(new Task(1, TzKalZuk, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 0:
					new Projectile(TzKalZuk, c, mageGfx.getId(), 50, 5, 75, 38, 0).sendProjectile();
					break;
				case 1:
					c.dealDamage(new Hit(500 + RandomUtility.RANDOM.nextInt(700), Hitmask.RED, CombatIcon.RANGED));
					TzKalZuk.setChargingAttack(false);
					stop();
					break;

				}
				tick++;
			}
		});
	}


	public static void spawnBossWave(Player c) {
		c.setjadWave(false);
		c.sethealerWave(false);
		int H = c.getIndex() * 4;

		NPC TzKalZuk = new NPC(7706, new Position(2268, 5362, H)).setSpawnedFor(c);
		NPC Glyph = new NPC(7893, new Position(2270, 5363, H)).setSpawnedFor(c);

		NPC mager = new NPC(7703, new Position(2264, 5351, H)).setSpawnedFor(c);
		NPC ranger = new NPC(7702, new Position(2271, 5350, H)).setSpawnedFor(c);
		// NPC mager1 = new NPC(7703, new Position(2267, 5352, H)).setSpawnedFor(c);
		// NPC ranger1 = new NPC(7702, new Position(2275, 5350, H)).setSpawnedFor(c);

		NPC Jad = new NPC(7700, new Position(2269, 5345, H)).setSpawnedFor(c);

		NPC Healer = new NPC(7750, new Position(2276, 5363, H)).setSpawnedFor(c);
		NPC Healer1 = new NPC(7750, new Position(2280, 5363, H)).setSpawnedFor(c);
		NPC Healer2 = new NPC(7750, new Position(2266, 5363, H)).setSpawnedFor(c);
		NPC Healer3 = new NPC(7750, new Position(2262, 5363, H)).setSpawnedFor(c);

		c.getPA().sendMessage("@red@The Inferno has began."); 
		World.sendMessage("@red@" + c.getUsername() + " has just Started the Inferno!!");

		
		TaskManager.submit(new Task(1, c, false) {
			int tick = 0;

			@Override
			public void execute() {

				switch (tick) {
				case 1:
					World.register(TzKalZuk);
					c.getRegionInstance().getNpcsList().add(TzKalZuk);

					TzKalZuk.performAnimation(new Animation(7563));
					TzKalZuk.setPositionToFace(new Position(2271, 1));
					CustomObjects.cornerFix(new GameObject(30342, new Position(2267, 5366, c.getIndex() * 4), 10, 1),
							c);
					CustomObjects.cornerFix(new GameObject(30341, new Position(2275, 5366, c.getIndex() * 4), 10, 3),
							c);
					CustomObjects.cornerFix(new GameObject(-1, new Position(2270, 5363, c.getIndex() * 4), 10, 1), c);
					CustomObjects.cornerFix(new GameObject(30340, new Position(2267, 5364, c.getIndex() * 4), 10, 1),
							c);
					CustomObjects.cornerFix(new GameObject(30339, new Position(2275, 5364, c.getIndex() * 4), 10, 3),
							c);
					break;
				case 0:
					CustomObjects.leftWall(new GameObject(30344, new Position(2268, 5364, c.getIndex() * 4), 10, 3), c,
							2);
					CustomObjects.rightWall(new GameObject(30343, new Position(2273, 5364, c.getIndex() * 4), 10, 3), c,
							2);

					World.register(Glyph);
					c.getRegionInstance().getNpcsList().add(Glyph);
					Glyph.setPositionToFace(new Position(2271, 1));

					break;
				case 2:
					Glyph.getMovementQueue().walkStep(0, -3);
					break;
				case 7:
					CustomObjects.bricks(new GameObject(30346, new Position(2268, 5364, c.getIndex() * 4), 10, 3), c);
					CustomObjects.bricks(new GameObject(30345, new Position(2273, 5364, c.getIndex() * 4), 10, 3), c);
					break;
				case 8:
					Glyph.getMovementQueue().walkStep(-13, 0);
					break;

				case 28:
					Glyph.getMovementQueue().walkStep(26, 0);
					break;

				case 100:
					World.register(mager);
					World.register(ranger);
					c.getRegionInstance().getNpcsList().add(mager);
					c.getRegionInstance().getNpcsList().add(ranger);
					mager.setEntityInteraction(Glyph);
					ranger.setEntityInteraction(Glyph);
					mager.getCombatBuilder().attack(Glyph);
					ranger.getCombatBuilder().attack(Glyph);
					break;

				/*
				 * case 530:
				 * 
				 * World.deregister(mager); World.deregister(ranger); if (c.lastKilled >= 2) {
				 * c.lastKilled = 0; c.getPacketSender().sendMessage("Amount: " + c.lastKilled);
				 * 
				 * World.register(mager1); World.register(ranger1);
				 * c.getRegionInstance().getNpcsList().add(mager1);
				 * c.getRegionInstance().getNpcsList().add(ranger1);
				 * mager1.setEntityInteraction(Glyph); ranger1.setEntityInteraction(Glyph);
				 * mager1.getCombatBuilder().attack(Glyph);
				 * ranger1.getCombatBuilder().attack(Glyph);
				 * 
				 * } break;
				 */

				}

				if (c.getConstitution() <= 0) {
					stop();
				}

				if (Glyph.getConstitution() <= 0 && c.getLocation() == Location.INFERNO) {
					stop();

					TaskManager.submit(new Task(1, TzKalZuk, false) {
						int tick = 0;

						@Override
						public void execute() {

							for (int i = 7; i < 250000; i += 8) {
								if (tick ==  i) {
									TzKalZuk.performAnimation(mage);
									TzKalZuk.setChargingAttack(true);
									new Projectile(TzKalZuk, c, mageGfx.getId(), 50, 5, 75, 38, 0).sendProjectile();
								}
								if (tick == 2 + i) {
									c.dealDamage(new Hit(500 + RandomUtility.RANDOM.nextInt(700), Hitmask.RED, CombatIcon.RANGED));
									TzKalZuk.setChargingAttack(false);
								}
								

							}
							if (TzKalZuk.getConstitution() <= 0) {
								c.delayedMoveTo(new Position(2438, 5168, 0), 4);
								c.sethealerWave(true);
								World.sendMessage("@red@" + c.getUsername() + " has just defeated the Inferno minigame!");
								c.getInventory().add(19112, 1);
								stop();
							}
							tick++;
						}
					});

				} else {
					for (int i = 36; i < 250000; i += 64) {

						if (Glyph.getPosition().getX() >= 2260 && tick == i) {
							if ((c.getPosition().getX() >= 2265 && c.getPosition().getX() <= 2268)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}

					}

					for (int i = 46; i < 250000; i += 64) {

						if (Glyph.getPosition().getX() >= 2274 && tick == i) {
							if ((c.getPosition().getX() >= 2274 && c.getPosition().getX() <= 2277)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}
					}

					for (int i = 69; i < 250000; i += 64) {

						if (Glyph.getPosition().getX() >= 2274 && tick == i) {
							if ((c.getPosition().getX() >= 2274 && c.getPosition().getX() <= 2277)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}
					}

					for (int i = 77; i < 250000; i += 64) {

						if (Glyph.getPosition().getX() >= 2265 && tick == i) {
							if ((c.getPosition().getX() >= 2265 && c.getPosition().getX() <= 2268)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}

					}
					for (int i = 23; i < 250000; i += 64) {
						if (Glyph.getPosition().getX() == 2257 && tick == i) {

							if ((c.getPosition().getX() <= 2260)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}

						if (Glyph.getPosition().getX() == 2283 && tick == i + 33) {
							if ((c.getPosition().getX() >= 2282)) {
								hit(c, TzKalZuk, Glyph);
							} else {
								hitplayer(c, TzKalZuk, Glyph);
							}
						}

					}

				}
				for (int i = 61; i < 250000; i += 64) {
					if (tick == i) {
						Glyph.getMovementQueue().walkStep(-26, 0);
					}
					if (tick == 32 + i) {
						Glyph.getMovementQueue().walkStep(26, 0);
					}
				}

				tick++;

				if (TzKalZuk.getConstitution() <= 0) {
					c.delayedMoveTo(new Position(2438, 5168, 0), 4);
					c.sethealerWave(true);
					World.sendMessage("@red@" + c.getUsername() + " has just defeated the Inferno minigame!");
					c.getInventory().add(19112, 1);

					stop();
				}

				if (TzKalZuk.getConstitution() <= 4800 && !c.jadWave()) {
					World.register(Jad);
					c.getRegionInstance().getNpcsList().add(Jad);
					Jad.getCombatBuilder().attack(c);
					c.setjadWave(true);
				} 

				if (TzKalZuk.getConstitution() <= 2400 && !c.healerWave()) {
					c.getPacketSender().sendMessage("Healers spawned");
					c.sethealerWave(true);
					World.register(Healer);
					World.register(Healer1);
					World.register(Healer2);
					World.register(Healer3);
					c.getRegionInstance().getNpcsList().add(Healer);
					c.getRegionInstance().getNpcsList().add(Healer1);
					c.getRegionInstance().getNpcsList().add(Healer2);
					c.getRegionInstance().getNpcsList().add(Healer3);
					c.getRegionInstance().getNpcsList().add(Healer);
					Healer.getCombatBuilder().attack(TzKalZuk);
					c.getRegionInstance().getNpcsList().add(Healer1);
					Healer1.getCombatBuilder().attack(TzKalZuk);
					c.getRegionInstance().getNpcsList().add(Healer2);
					Healer2.getCombatBuilder().attack(TzKalZuk);
					c.getRegionInstance().getNpcsList().add(Healer3);
					Healer3.getCombatBuilder().attack(TzKalZuk);
					Healer.performAnimation(new Animation(2864));
					Healer1.performAnimation(new Animation(2864));
					Healer2.performAnimation(new Animation(2864));
					Healer3.performAnimation(new Animation(2864));
					
				}

			}
		});

	}

	private static final Animation mage = new Animation(7566);
	private static final Graphic mageGfx = new Graphic(1375);

}
