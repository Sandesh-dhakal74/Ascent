package ascent.console;

/**
 * Entry point for the Ascent fitness workout planner. Starts the
 * console menu and keeps the application running until the user
 * quits.
 */
public class AscentApp
{
   public static void main(String[] args)
   {
      ConsoleUI ui = new ConsoleUI();
      ui.run();
   }
}