package ascent.gui;

/**
 * Turns the text typed into a form field into a value the controller
 * can use. Each method throws an IllegalArgumentException with a plain
 * message, like "Sets must be a whole number", when the text does not
 * fit. The screens catch that and show the message to the user.
 *
 * This class only checks that the text can be read as the right kind
 * of value. Rules about the value itself (sets must be positive,
 * intensity must be 1 to 5) stay in the model classes.
 */
public final class FormInput
{
   /** Not used. This class only has static methods. */
   private FormInput()
   {
   }

   /**
      Reads a required line of text.
      @param label the field name, used in the error message
      @param text the text typed into the field
      @return the text with spaces trimmed from both ends
      @throws IllegalArgumentException if the text is blank
   */
   public static String requiredText(String label, String text)
   {
      String trimmed = text.trim();
      if (trimmed.isEmpty())
      {
         throw new IllegalArgumentException(label + " cannot be blank");
      }
      return trimmed;
   }

   /**
      Reads a line of text that may be left blank.
      @param text the text typed into the field
      @return the text with spaces trimmed from both ends, possibly empty
   */
   public static String optionalText(String text)
   {
      return text.trim();
   }

   /**
      Reads a whole number.
      @param label the field name, used in the error message
      @param text the text typed into the field
      @return the number
      @throws IllegalArgumentException if the text is blank or is not
      a whole number
   */
   public static int wholeNumber(String label, String text)
   {
      String trimmed = requiredText(label, text);
      try
      {
         return Integer.parseInt(trimmed);
      }
      catch (NumberFormatException e)
      {
         throw new IllegalArgumentException(label + " must be a whole number");
      }
   }

   /**
      Reads a decimal number. Text like NaN and Infinity, which Java
      would otherwise accept, is rejected.
      @param label the field name, used in the error message
      @param text the text typed into the field
      @return the number
      @throws IllegalArgumentException if the text is blank or is not
      an ordinary number
   */
   public static double number(String label, String text)
   {
      String trimmed = requiredText(label, text);
      double value;
      try
      {
         value = Double.parseDouble(trimmed);
      }
      catch (NumberFormatException e)
      {
         throw new IllegalArgumentException(label + " must be a number");
      }
      if (Double.isNaN(value) || Double.isInfinite(value))
      {
         throw new IllegalArgumentException(label + " must be a number");
      }
      return value;
   }
}