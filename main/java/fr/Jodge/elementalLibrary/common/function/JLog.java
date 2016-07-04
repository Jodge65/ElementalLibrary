package fr.Jodge.elementalLibrary.common.function;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ReportedException;
import fr.Jodge.elementalLibrary.common.Main;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.data.DataHelper;

public class JLog
{
	final static int JLOG_LEVEL = 4;
	
	/** First level of Log. Use it when game will crash. */
	public static void error(String text)
	{
		if(JLOG_LEVEL >= 1)
			write ("[ERROR] " + text);
	}
	
	/** Second level of log. Use it when game can survive without, but whit lot of bug. */
	public static void warning(String text)
	{
		if(JLOG_LEVEL >= 2)
			write ("[WARNING] " + text);
	}
	
	/** Thirty level of log. Use it when something is missing, but the game can survive without bug. */ 
	public static void alert(String text)
	{
		if(JLOG_LEVEL >= 3)
			write ("[ALERTE] " + text);
	}
	
	/** last level of log. Use it when something is generated and you want to keep a trace. */
	public static void info(String text)
	{
		if(JLOG_LEVEL >= 4)
			write ("[INFO] " + text);
	}
	
	/** pass through each verification and write. If DEBUG is set to true, then value will be write on second windows*/
	public static void write(String text)
	{
		if(Main.DEBUG)
		{
			Main.DEBUG_WINDOWS.addLine(text);
		}
		else
		{
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
    	try
		{
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "ElementalLibrary issue !\n" + text);
            CrashReportCategory crashreportcategory = crashreport.makeCategory("ElementalLibrary issues");
            throw new ReportedException(crashreport);
		}
    	catch (Throwable throwable2)
        {
    		text = 		"ElementalLibrary issue !\n"
    					+	"A problem occur when a crash report was made...\n"
    					+	"you are very unluky ^^' "
    		;
    				
    		CrashReport crashreport = CrashReport.makeCrashReport(throwable, text);
            CrashReportCategory crashreportcategory = crashreport.makeCategory("ElementalLibrary crash during rendering of crash report T-T ");
            throw new ReportedException(crashreport);
        }

	}
	
	public static String getDetails(EntityLivingBase entity)
	{
		
		String msg = "" 
				+	entity.getName() + " (" + entity.getClass() + ")\n"
				+	"List of Matrix : "
		;
		
		for(ElementalMatrix matrix : DataHelper.getElementalMatrix(entity))
		{
			msg += matrix.toString();
		}
		
		return msg;
	}
}
