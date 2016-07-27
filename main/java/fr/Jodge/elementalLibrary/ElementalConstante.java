package fr.Jodge.elementalLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
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

public class ElementalConstante 
{
	/** List of stats available for player, and default value. */
	public static List<Pair<Class, IElementalWritable>> PLAYER_STATS;
	/** List of stats available for monster. Value here is just use to have access to some value */
	public static List<Pair<Class, IElementalWritable>> MONSTER_STATS;
	/** Map of each kind of existing stats */
	public static Map<Class, Map<Class, DataParameter>> STATS;
	/** Save Default monster Stats to use it again instead of ask server/read file  */
	public static Map<Class, AbstractStats> DEFAULT_STATS;

	public static Map<String, DamageMatrix> WEAPONS_DAMAGE;
	
	public static SimpleNetworkWrapper STATS_SOCKET;
	public static String STATS_ID = Main.MODID;
	
	public static class init
	{
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
			PLAYER_STATS = new ArrayList<Pair<Class, IElementalWritable>>();
			PLAYER_STATS.add(new ImmutablePair<Class, IElementalWritable>(AttackMatrix.class, new AttackMatrix()));
			PLAYER_STATS.add(new ImmutablePair<Class, IElementalWritable>(DefenceMatrix.class, new DefenceMatrix()));
			JLog.info("New list have been create for PLAYER_STATS");

			/** list of value that exist for monster, value is only use to know which class use. */ 
			MONSTER_STATS = new ArrayList<Pair<Class, IElementalWritable>>();
			MONSTER_STATS.add(new ImmutablePair<Class, IElementalWritable>(AttackMatrix.class, new AttackMatrix()));
			MONSTER_STATS.add(new ImmutablePair<Class, IElementalWritable>(DefenceMatrix.class, new DefenceMatrix()));
			JLog.info("New list have been create for MONSTER_STATS");

			/** list of all available stats. Use for algorithm only. */
			STATS = new HashMap<Class, Map<Class, DataParameter>>();
			register.addNewStats(AttackMatrix.class);
			register.addNewStats(DefenceMatrix.class);
	
			WEAPONS_DAMAGE = new HashMap<String, DamageMatrix>();
			JLog.info("New map have been create for WEAPONS_DAMAGE");
			
			DEFAULT_STATS = new HashMap<Class, AbstractStats>();
			JLog.info("New map have been create for DEFAULT_STATS");

			
		}

		public static void onLoad()
		{
			JLog.info(" --- INIT --- ");

		}
		
		public static void onAfterLoad()
		{
			JLog.info(" --- POSTINIT --- ");

		}	
		
		public static void onClientExit() 
		{
			JLog.info(" --- START PURGE --- ");
			DEFAULT_STATS.clear();
			
			JLog.info(" --- END PURGE --- ");

		}
	}

	public static class register
	{
		/**
		 * Add a new kind of Stat that include interface IElementalWritable
		 * @param c <i>Class<? extends IElementalWritable></i>
		 */
		public static void addNewStats(Class<? extends IElementalWritable> c)
		{
			if(!STATS.containsKey(c))
				STATS.put(c, new HashMap<Class, DataParameter>());
			else
				JLog.alert("Class " + c + " is already references under stats...");
		}
		
		/**
		 * Add new Default Matrix to an item. UnlocalizedName will be used as key.
		 * @param item <i>Item</i> 
		 * @param matrix <i>DamageMatrix</i>
		 */
		public static void addNewWeaponMatrix(Item item, DamageMatrix matrix)
		{
			addNewWeaponMatrix(item.getUnlocalizedName(), matrix);
		}
		
		/**
		 * Add new Default Matrix to a key. If you don't use UnlocalizedName, then key will be never call.
		 * @param name <i>String</i> 
		 * @param matrix <i>DamageMatrix</i>
		 */
		public static void addNewWeaponMatrix(String name, DamageMatrix matrix)
		{
			if(!WEAPONS_DAMAGE.containsKey(name))
				WEAPONS_DAMAGE.put(name, matrix);
			else
				JLog.alert("Item " + name + " is already references under. ");
		}
		
		
	}


	public static DamageMatrix getMatrixForItem(Item item)
	{
		return getMatrixForItem(item.getUnlocalizedName());
	}
	
	public static DamageMatrix getMatrixForItem(String name)
	{
		return WEAPONS_DAMAGE.getOrDefault(name, null);
	}
	
	public static DataParameter getDataKeyForEntity(Entity target, Class dataType)
	{
		Class key = target.getClass();
		Map<Class, DataParameter> currentMap;
		
		if(STATS.containsKey(dataType))
		{
			currentMap = STATS.get(dataType);
		}
		else
		{
			JLog.warning("The data type " + dataType + " is undefined...");
			return null;
		}
		
		if(currentMap.containsKey(key))
		{
			return currentMap.get(key);
		}
		else
		{
			DataParameter value = EntityDataManager.createKey(target.getClass(), ElementalDataSerializers.ELEMENTAL_SERIALIZER);
			currentMap.put(key, value);
			
			JLog.info("New key have ben create for entity " + target.getClass() + ". Id use is : " + value.getId() + ".");
			
			return value;
		}	
	}



	
}
