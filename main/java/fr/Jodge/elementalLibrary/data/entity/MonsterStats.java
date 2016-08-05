package fr.Jodge.elementalLibrary.data.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jline.internal.Log;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonObject;

import scala.reflect.io.Directory;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.function.JLog;

public class MonsterStats extends AbstractStats
{	
	public static final String folder = "monsterdata\\";
	
	public int id;

	public EntityLivingBase entity;

	public MonsterStats(int id)
	{
		super(Main.constante.MONSTER_STATS);
		this.id = id;
	}
	
	public MonsterStats(EntityLivingBase target) 
	{
		this(target.getEntityId());
		this.entity = target;
		
		MinecraftServer server = target.getServer();
		String subFolder = target.getClass().getSimpleName() + "\\";
		String url = folder + subFolder;
		data = null;

		// check if entity has custom name tag
		if(target.hasCustomName())
		{
			// if yes, then we try whi custom name
			url += target.getCustomNameTag() + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			
			if(!data.exists())
			{
				data = null;
			}
		}
		
		
		if(data == null)
		{
			url += "default" + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
		}

		
		makeByFile();
	}
	
	/**
	 * Try to use all default constructor available in listOfAvailableStats
	 * @param data <i>File</i>
	 */
	@Override
	protected boolean CreateNew() 
	{
		if(super.CreateNew(false))
		{
			for(IElementalWritable objet : value.values())
			{
				objet.autoUptdate(entity);
			}

			save();
			return true;
		}

		return false;
	}
}
