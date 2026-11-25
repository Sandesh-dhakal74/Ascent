package ascent.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ascent.controller.AscentController;

/**
 * Entry point for the Ascent fitness workout planner. Opens the main
 * window on the Swing event thread.
 */
public class AscentApp
{
   /** Not used. This class only has the main method. */
   private AscentApp()
   {
   }

   /**
      Starts the program. Uses the operating system's look and feel
      when it is available.
      @param args command line arguments, not used
   */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(() ->
      {
         try
         {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         }
         catch (Exception e)
         {
            // The default look and feel works fine if this one is missing.
         }
         MainWindow window = new MainWindow(new AscentController());
         window.setVisible(true);
      });
   }
}
