package ascent.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Small helper for checking that a date string is a real calendar
 * date in YYYY-MM-DD form.
 */
public final class DateUtil
{
   /** Not used. This class only has static methods. */
   private DateUtil()
   {
   }

   /**
      Checks whether a string is a real date written as YYYY-MM-DD.
      Rejects missing dashes, single-digit months or days, and dates
      that do not exist such as 2026-02-30.
      @param date the text to check, may be null
      @return true if the text is a valid YYYY-MM-DD date
   */
   public static boolean isValidDate(String date)
   {
      if (date == null)
      {
         return false;
      }
      try
      {
         LocalDate.parse(date);
         return true;
      }
      catch (DateTimeParseException e)
      {
         return false;
      }
   }
}