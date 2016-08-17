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
	public boolean isDefaultStats;
	
	public MonsterStats(int id)
	{
		super(Main.constante.MONSTER_STATS);
		this.id = id;
	}
	
	public MonsterStats(EntityLivingBase target) 
	{
		this(target.getEntityId());
		this.entity = target;

		MinecraftServer server = this.entity.getServer();
		String subFolder = 	this.entity.getClass().getSimpleName() + "\\";
		String url = folder + subFolder;
		data = null;

		// check if entity has custom name tag
		if(this.entity.hasCustomName())
		{
			// if yes, then we try whit custom name
			url += this.entity.getCustomNameTag() + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			
			if(!data.exists())
			{
				JLog.info("Custom File " + data.getAbsolutePath() + " not exist. Defautl file will be used");
				data = null;
			}
			else
			{
				JLog.info("Custom File " + data.getAbsolutePath() + " exist. It will be used.");
				this.isDefaultStats = false;
			}
		}
		
		
		if(data == null)
		{
			url = folder + subFolder + "default" + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			this.isDefaultStats = true;
		}

		JLog.info("Make monster Stats for " + entity.getClass() + " by file.");
		makeByFile();
	}
	
}
