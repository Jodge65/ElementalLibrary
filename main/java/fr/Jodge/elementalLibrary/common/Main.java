package fr.Jodge.elementalLibrary.common;

import fr.Jodge.elementalLibrary.common.damage.DamageEvent;
import fr.Jodge.elementalLibrary.common.data.DataEvent;
import fr.Jodge.elementalLibrary.common.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.common.function.JLog;
import fr.Jodge.elementalLibrary.proxy.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.relauncher.Side;
/**
 * @author Jodge
 * 
 */
@Mod(modid = "elementalLibrary", name = "Jodge Library", version = "A0.1.0", acceptedMinecraftVersions = "[1.9]")
public class Main
{
	public static final String MODID = "elementalLibrary";
	public static final String MODNAME = "Elemental Library";
	public static final String MODVER = "A0.1.0";
	
	public static final boolean DEBUG = true;
	public static ElementalLibraryDebug DEBUG_WINDOWS;

	
	@Mod.Instance("elementalLibrary")
	public static Main instance;
	
	@SidedProxy(clientSide = "fr.Jodge.elementalLibrary.proxy.ClientProxy", serverSide = "fr.Jodge.elementalLibrary.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		if(DEBUG)
			DEBUG_WINDOWS = new ElementalLibraryDebug();

		// prety useless but... So beautiful :D
		JLog.write("[ ### --- INFO --- ### ]");
		JLog.write("Name : " + MODNAME);
		JLog.write("Modid : " + MODID);
		JLog.write("ModiVer : " + MODVER);
		JLog.write("Author : " + getAuthor());
		JLog.write("e-mail : " + getContact());
		JLog.write("[ ### ---        --- ### ]");
		JLog.write("");
		
		ElementalConstante.onPreLoad();
		
		
		
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		ElementalConstante.onLoad();
		
		proxy.registerRender();
		
		MinecraftForge.EVENT_BUS.register(new DamageEvent());
		JLog.info("New event have been register for DamageEvent");

		MinecraftForge.EVENT_BUS.register(new DataEvent());
		JLog.info("New event have been register for DataEvent");

		if (event.getSide().isClient()) // Client
		{
		}
		else // Serveur
		{
		}
		

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ElementalConstante.onAfterLoad();

		
		
		JLog.info(" --- ENDINIT --- ");
	}
	
	private static String getAuthor()
	{
		return "Jodge65";
	}
	private static String getContact()
	{
		return "joey.sarie@gmail.com";
	}
}
