package fr.Jodge.elementalLibrary.data.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.entity.AbstractStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.EnvironmentalMatrix;
import fr.Jodge.elementalLibrary.data.network.AskMonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.network.AskPlayerStatsPacket;
import fr.Jodge.elementalLibrary.data.network.MonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.network.PlayerStatsPacket;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ElementalConstante extends Variable
{
	/**
	 * What you need to do : 
	 * - Refers each kind of existing stats
	 */
	public static void onPreLoad()
	{
		JLog.info(" --- PREINIT --- ");
		
		/** packet connect */
		STATS_SOCKET = NetworkRegistry.INSTANCE.newSimpleChannel(STATS_ID);
		STATS_SOCKET.registerMessage(AskPlayerStatsPacket.Handler.class, AskPlayerStatsPacket.class, 0, Side.SERVER);
		STATS_SOCKET.registerMessage(PlayerStatsPacket.Handler.class, PlayerStatsPacket.class, 1, Side.CLIENT);
		STATS_SOCKET.registerMessage(AskMonsterStatsPacket.Handler.class, AskMonsterStatsPacket.class, 2, Side.SERVER);
		STATS_SOCKET.registerMessage(MonsterStatsPacket.Handler.class, MonsterStatsPacket.class, 3, Side.CLIENT);
		JLog.info("New socket have been create for STATS_SOCKET");

		/** list of value that exist for player, and default value*/
		PLAYER_STATS = new ArrayList<Class<? extends IElementalWritable>>();
		PLAYER_STATS.add(AttackMatrix.class);
		PLAYER_STATS.add(DefenceMatrix.class);
		JLog.info("New list have been create for PLAYER_STATS");

		/** list of value that exist for monster, value is only use to know which class use. */ 
		MONSTER_STATS = new ArrayList<Class<? extends IElementalWritable>>();
		MONSTER_STATS.add(AttackMatrix.class);
		MONSTER_STATS.add(DefenceMatrix.class);
		JLog.info("New list have been create for MONSTER_STATS");

		/** list of all available stats. Use for algorithm only. */
		STATS = new HashMap<Class, Map<Class, DataParameter>>();
		Register.addNewStats(AttackMatrix.class);
		Register.addNewStats(DefenceMatrix.class);
		
		DEFAULT_STATS = new HashMap<Class, AbstractStats>();
		JLog.info("New map have been create for DEFAULT_STATS");
		
		WEAPONS_DAMAGE_BRUTE = new HashMap<String, DamageMatrix>();
		JLog.info("New map have been create for WEAPONS_DAMAGE_BRUTE");

		ENTITY_DAMAGE_PERCENT = new HashMap<Class, AttackMatrix>();
		JLog.info("New map have been create for ENTITY_DAMAGE_PERCENT");

		ENTITY_RESISTANCE_PERCENT = new HashMap<Class, DefenceMatrix>();
		JLog.info("New map have been create for ENTITY_RESISTANCE_PERCENT");

		DEFAULT_ELEMENT_DAMAGE_SOURCES = new HashMap<String, Element>();
		JLog.info("New map have been create for DEFAULT_ELEMENT_DAMAGE_SOURCES");

	}
	
	/**
	 * What you need to do : 
	 * - add/remove element
	 */
	public static void onLoad()
	{
		JLog.info(" --- INIT --- ");
		
		VanillaInitialization.initElement();
		JLog.info("Vanilla generation Initialized.");
		
	}
	
	/**
	 * 
	 */
	public static void onAfterLoad()
	{
		JLog.info(" --- POSTINIT --- ");

	}	
	
	/**
	 * - Purge Client value that depend on server
	 */
	public static void onClientExit() 
	{
		JLog.info(" --- START PURGE --- ");
		DEFAULT_STATS.clear();
		
		JLog.info(" --- END PURGE --- ");

	}
}
