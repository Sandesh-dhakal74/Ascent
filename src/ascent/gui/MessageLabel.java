package ascent.gui;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * A one-line label for telling the user what just happened. Errors
 * show in red and confirmations in green. It holds a blank space when
 * empty so the layout does not jump when a message appears.
 */
public class MessageLabel extends JLabel
{
   /** Color used for error messages. */
   private static final Color ERROR_COLOR = new Color(176, 0, 32);
   /** Color used for confirmations. */
   private static final Color INFO_COLOR = new Color(0, 110, 51);

   /**
      Constructs an empty message label.
   */
   public MessageLabel()
   {
      super(" ");
   }

   /**
      Shows an error message in red.
      @param message the text to show
   */
   public void showError(String message)
   {
      setForeground(ERROR_COLOR);
      setText(message);
   }

   /**
      Shows a confirmation message in green.
      @param message the text to show
   */
   public void showInfo(String message)
   {
      setForeground(INFO_COLOR);
      setText(message);
   }

   /**
      Clears the message.
   */
   public void clear()
   {
      setText(" ");
   }
}