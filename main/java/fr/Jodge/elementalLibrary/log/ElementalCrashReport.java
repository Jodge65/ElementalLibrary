package fr.Jodge.elementalLibrary.log;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ReportedException;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.register.Getter;

public class ElementalCrashReport extends CrashReport
{
	//public CrashReportCategory elementalCategorie;
	
	public ElementalCrashReport(String descriptionIn, Throwable causeThrowable) 
	{
		super("ElementalLibrary issue !\n" + descriptionIn, causeThrowable);
		//elementalCategorie = makeCategory("ElementalLibrary Data");
	}

	/**
	 * crash Minecraft and make a crash report adapted to the situation
	 * @param throwable <i>Throwable</i> (to use : try{something();}catch(Throwable t){JLog.crashReport(t, "What wrong");}
	 * @param text <i>String</i> Describe of the error. (can be on multiline whit \n)
	 */
	public static void crashReport(Throwable throwable, String text)
	{
	     CrashReport crashreport = CrashReport.makeCrashReport(throwable, "ElementalLibrary issue !\n" + text);
         //CrashReportCategory crashreportcategory = crashreport.makeCategory("ElementalLibrary issues");
         
         throw new ReportedException(crashreport);
	}
	
	public static String getDetails(EntityLivingBase entity)
	{
		if(entity == null)
			return "Empty Entity.";
		
		String msg = "" 
				+	entity.getName() + " (" + entity.getClass() + ")\n"
				+	"List of Matrix : \n"
		;
		
		for(ElementalMatrix matrix : DataHelper.getElementalMatrix(entity))
		{
			msg += "Matrix : " + matrix.getClass() + ", whit key id : " + Getter.getDataKeyForEntity(entity, matrix.getClass()).getId() + " :\n";
			msg += matrix.toString() + "\n";
		}
		
		return msg;
	}
	
	
}
