package fr.Jodge.elementalLibrary.debug;

import javax.swing.JTextArea;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Variable;

public class ElementDebugUptade extends Thread
{
	protected static int actualSize = -1;
	
	@Override
	public void run()
	{
		while(true)
		{
			int size = Element.getAllActiveElement().size();
			if(actualSize != size)
			{
				actualSize = size;
				updateElement();
			}
			
			ElementalLibraryDebug.entityData.setText(display(ElementalLibraryDebug.entity));
			try
			{
				EntityLivingBase e = Minecraft.getMinecraft().thePlayer;
				if(e != null)
				{
					ElementalLibraryDebug.player = e;
					ElementalLibraryDebug.playerData.setText(display(ElementalLibraryDebug.player));
				}
				else
				{
					ElementalLibraryDebug.playerData.setText("No Player. You may not be in game.");
				}
				
			}
			catch(Exception e)
			{
				ElementalLibraryDebug.playerData.setText("Can't Access to Minecraft instance. Maybe you are Server Side ?");
			}
			
			try{sleep(ElementalLibraryDebug.debugSpeed);}
			catch (InterruptedException e){}
		}
	}
	
	public static void updateElement()
	{
		//caseElementID, caseElementName, caseElementActive;
		String listOfElementId = "  ID  \n";
		String listOfElementName = "  Name  \n";
		String listOfElementActive = "  isActive  \n";
		for(Element element : Element.getAllActiveElement())
		{
			listOfElementId += " " + element.getId() + "  \n";
			listOfElementName += " " + element.getName() + "  \n";
			listOfElementActive += " " + element.isActive() + "  \n";
		}
		ElementalLibraryDebug.caseElementID.setText(listOfElementId);
		ElementalLibraryDebug.caseElementName.setText(listOfElementName);
		ElementalLibraryDebug.caseElementActive.setText(listOfElementActive);

	}
	
	public String display(EntityLivingBase entity)
	{
		String text;
		if(entity == null)
		{
			text = "No Entity Selected. Use Staff Of Debuging to Select Something to display.";
		}
		else
		{
			text = "\"Class\" : " + entity.getClass() + "\n"
					 + "\"Name\" : " + entity.getName() + "\n"	
					 + "\"Life\" : " + entity.getHealth() + "/" + entity.getMaxHealth() + "\n"
				;
			for(Class clazz : Main.constante.MONSTER_STATS)
			{
				DataParameter key = Getter.getDataKeyForEntity(entity, clazz);
				ElementalMatrix matrix = entity.getDataManager().get(key);
				String sMatrix = matrix.toJsonObject().toString();
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jsonParser = new JsonParser();
				
				JsonElement writableElement = jsonParser.parse(sMatrix);
				sMatrix = gson.toJson(writableElement);
				
				text += "\"" + clazz + "\" :\n " + sMatrix + "\n";					
			} 
		}
		return text;

	}
}