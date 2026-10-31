package ascent.console;

/**
 * Entry point for the Ascent fitness workout planner. Starts the
 * console menu and keeps the application running until the user
 * quits.
 */
public class AscentApp
{
   /**
      Starts the program by creating the console UI and running it.
      @param args command line arguments, not used
   */
   public static void main(String[] args)
   {
      ConsoleUI ui = new ConsoleUI();
      ui.run();
   }
}