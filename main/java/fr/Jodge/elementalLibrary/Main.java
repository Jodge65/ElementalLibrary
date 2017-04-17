package fr.Jodge.elementalLibrary;

import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;

import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.debug.ElementalLibraryDebug;
import fr.Jodge.elementalLibrary.event.DamageEvent;
import fr.Jodge.elementalLibrary.event.DataEvent;
import fr.Jodge.elementalLibrary.integration.ElementalIntegrationHelper;
import fr.Jodge.elementalLibrary.integration.base.BaubleHelper;
import fr.Jodge.elementalLibrary.integration.base.TinkersDamageHelper;
import fr.Jodge.elementalLibrary.integration.base.VanillaHelper;
import fr.Jodge.elementalLibrary.item.StaffOfDebuging;
import fr.Jodge.elementalLibrary.log.JLog;
import fr.Jodge.elementalLibrary.server.data.register.CommonElementalConstante;

/**
 * @author Jodge
 * 
 */
@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVER, dependencies = "after:Baubles",acceptedMinecraftVersions = "[1.10]")
public class Main
{
	public static final String MODID = "elLibrary";
	public static final String MODNAME = "Elemental Library";
	public static final String MODVER = "A0.1.0";
	
	public static ConfigurationHelper configFile;
	public static ElementalLibraryDebug DEBUG_WINDOWS;
	
	public static boolean isTinkerConstructLoaded;
	public static boolean isBaubleLoaded;

	// this is needed on server side to force class to be loaded.
	public static DataSerializer ELEMENTAL_SERIALIZER = ElementalDataSerializers.ELEMENTAL_SERIALIZER;
	
	@Mod.Instance(Main.MODID)
	public static Main instance;

	@SidedProxy(clientSide = "fr.Jodge.elementalLibrary.client.data.register.ClientElementalConstante", serverSide = "fr.Jodge.elementalLibrary.server.data.register.CommonElementalConstante")
	public static CommonElementalConstante constante;
	
	public static Item STAFF_OF_DEBUG;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ElementalConfiguration.devEnv = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		
		ModMetadata data = event.getModMetadata();
		data.autogenerated = false;
		data.name = Main.MODNAME;
		data.description = "Elemental Library.\nEnhance combat system whit Element !";
		data.credits = "Jodge";
		data.modId = Main.MODID;
		data.url = "";
		data.version = Main.MODVER;
		for(String author : getAuthor())
			data.authorList.add(author);
		data.logoFile = "logo.png";
		
		ElementalIntegrationHelper.registerClass(VanillaHelper.class);
		
		isBaubleLoaded = Loader.isModLoaded("Baubles");
		if(isBaubleLoaded)
		{
			JLog.info("Baubles API is found.");
			ElementalIntegrationHelper.registerClass(BaubleHelper.class);
		}
		
		isTinkerConstructLoaded = Loader.isModLoaded("tconstruct");
		if(isTinkerConstructLoaded)
		{
			JLog.info("Tinker's Construct is found.");
			ElementalIntegrationHelper.registerClass(TinkersDamageHelper.class);
		}
		

		
		configFile = ConfigurationHelper.getInstance(event);
		configFile.onPreInit();
		
		// WINDOW_DEBUG is automatically set to false on server side to prevent bug.
		if(ElementalConfiguration.WINDOW_DEBUG)
		{
			DEBUG_WINDOWS = new ElementalLibraryDebug();
		}
		STAFF_OF_DEBUG = new StaffOfDebuging();

		// Pretty useless but... So beautiful :D... Okay i take the door :(
		JLog.write("[ ### --- INFO --- ### ]");
		JLog.write("Name : " + MODNAME);
		JLog.write("Modid : " + MODID);
		JLog.write("ModVer : " + MODVER);
		JLog.write("Author : " + getAuthor());
		JLog.write("e-mail : " + getContact());
		JLog.write("[ ### ---        --- ### ]");
		JLog.write("");
		
		constante.onPreLoad();
		
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		constante.onLoad();
			
		MinecraftForge.EVENT_BUS.register(new DamageEvent());
		JLog.info("New event have been register for DamageEvent");

		MinecraftForge.EVENT_BUS.register(new DataEvent());
		JLog.info("New event have been register for DataEvent");

		configFile.onInit();
		
		/*
		if (event.getSide().isClient()) // Client
		{
		}
		else // Serveur
		{
		}
		*/

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		constante.onAfterLoad();

		
		configFile.onPostInit();

		JLog.info(" --- ENDINIT --- ");
	}

	@Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) 
	{
        if(event.getSide() == Side.SERVER) // Server only 
        {
            // save everything
        	for (PlayerHelper helper : PlayerHelper.allPlayer.values()) 
        	{
        	    if(!helper.save())
        	    {
        	    	JLog.alert("Current Helper for " + helper.player.getName() + " has trouble and can't be save...");
        	    }
        	}
        	// not needed on server only cause game was close after, but needed on integrated server.
        	PlayerHelper.allPlayer.clear();
        }
    }
    
	@Mod.EventHandler
	public void onServerStart(FMLServerAboutToStartEvent event)
	{
		Main.constante.onServerStart(event.getServer());
	}

	protected static String[] getAuthorMinecraftName()
	{
		String author[] = {"Jodge65"};
		return author;	
	}
	protected static String[] getAuthor()
	{
		String author[] = {"Jodge"};
		return author;
	}
	protected static String[] getContact()
	{
		String contact[] = {"joey.sarie@gmail.com"};
		return contact;
	}
	
	public static void main(String[ ] args)
	{
		// Do Some Test
		ElementalConfiguration.WINDOW_DEBUG = true;
		ElementalConfiguration.DEBUG = true;
		DEBUG_WINDOWS = new ElementalLibraryDebug();
		JLog.write("[ ### --- INFO --- ### ]");
		JLog.write("Name : " + MODNAME);
		JLog.write("Modid : " + MODID);
		JLog.write("ModVer : " + MODVER);
		JLog.write("Author : " + getAuthor());
		JLog.write("e-mail : " + getContact());
		JLog.write("[ ### ---        --- ### ]");
		JLog.write("");		
		JLog.write("START OF TEST.");
		
		// Add some debug information ?
		
		JLog.write("END OF TEST.");
	}
}