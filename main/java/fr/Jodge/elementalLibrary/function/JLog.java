package fr.Jodge.elementalLibrary.function;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ReportedException;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;

public class JLog
{
	
	/** First level of Log. Use it when game will crash. */
	public static void error(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 1)
			write ("[ERROR] " + text);
	}
	
	/** Second level of log. Use it when game can survive without, but whit lot of bug. */
	public static void warning(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 2)
			write ("[WARNING] " + text);
	}
	
	/** Thirty level of log. Use it when something is missing, but the game can survive without bug. */ 
	public static void alert(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 3)
			write ("[ALERTE] " + text);
	}
	
	/** last level of log. Use it when something is generated and you want to keep a trace. */
	public static void info(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 4)
			write ("[INFO] " + text);
	}
	
	/** pass through each verification and write. If DEBUG is set to true, then value will be write on second windows*/
	public static void write(String text)
	{
		if(ElementalConfiguration.DEBUG)
		{
			if(ElementalConfiguration.WINDOW_DEBUG)
			{
				Main.DEBUG_WINDOWS.addLine(text);
			}
			System.out.println(text);
		}
		
	}

	
	
	/**
	 * crash Minecraft and make a crash report adapted to the situation
	 * @param throwable <i>Throwable</i> (to use : try{something();}catch(Throwable t){JLog.crashReport(t, "What wrong");}
	 * @param text <i>String</i> Describe of the error. (can be on multiline whit \n)
	 */
	public static void crashReport(Throwable throwable, String text)
	{
	     CrashReport crashreport = CrashReport.makeCrashReport(throwable, "ElementalLibrary issue !\n" + text);
         CrashReportCategory crashreportcategory = crashreport.makeCategory("ElementalLibrary issues");
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
			msg += matrix.toString() + "\n";
		}
		
		return msg;
	}
}
