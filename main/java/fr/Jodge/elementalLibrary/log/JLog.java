package fr.Jodge.elementalLibrary.log;

import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;

public class JLog
{
	
	/** First level of Log. Use it when game will crash. */
	public static void error(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 1)
			write ("[ERROR] " + text, 3);
	}
	
	/** Second level of log. Use it when game can survive without, but whit lot of bug. */
	public static void warning(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 2)
			write ("[WARNING] " + text, 3);
	}
	
	/** Thirty level of log. Use it when something is missing, but the game can survive without bug. */ 
	public static void alert(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 3)
			write ("[ALERTE] " + text, 3);
	}
	
	/** last level of log. Use it when something is generated and you want to keep a trace. */
	public static void info(String text)
	{
		if(ElementalConfiguration.JLOG_LEVEL >= 4)
			write ("[INFO] " + text, 3);
	}

	public static void write(String text)
	{
		write(text, 3);
	}
	/** pass through each verification and write. If DEBUG is set to true, then value will be write on second windows*/
	public static void write(String text, int stackLine)
	{
		if(ElementalConfiguration.DEBUG)
		{
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String bonus = stackTrace[stackLine].getMethodName() + "(" + stackTrace[stackLine].getFileName() + ":" + stackTrace[stackLine].getLineNumber() + ")";
			

			if(ElementalConfiguration.WINDOW_DEBUG)
			{
				Main.DEBUG_WINDOWS.addLine(bonus + "-" + text);
			}
			System.out.println(stackTrace[stackLine] + "-" + text);
		}
		
	}
}