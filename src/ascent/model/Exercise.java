package ascent.model;

/**
 * Base class for a single logged exercise. Holds what every exercise
 * shares (name, date, notes) and leaves the calorie math to each
 * subclass, since a strength set and a run have nothing in common
 * mathematically.
 */
public abstract class Exercise
{
   /** The exercise name, like "Bench Press". */
   private String name;
   /** The date performed, stored as YYYY-MM-DD text. */
   private String date;
   /** Optional notes, empty if there are none. */
   private String notes;
   /**
      Constructs an exercise.
      @param name the exercise name (e.g. "Bench Press", "Morning Run")
      @param date the date performed, in YYYY-MM-DD form
      @param notes optional free-text notes, may be empty
   */
   public Exercise(String name, String date, String notes)
   {
      if (name == null || name.isBlank())
      {
         throw new IllegalArgumentException("Exercise name cannot be empty");
      }
      if (!DateUtil.isValidDate(date))
      {
         throw new IllegalArgumentException("Exercise date must be a real date in YYYY-MM-DD form");
      }
      this.name = name;
      this.date = date;
      this.notes = (notes == null) ? "" : notes;
   }

   /**
      Gets the exercise name.
      @return the exercise name
   */
   public String getName()
   {
      return name;
   }

   /**
      Gets the date this exercise was performed.
      @return the date in YYYY-MM-DD form
   */
   public String getDate()
   {
      return date;
   }

   /**
      Gets any notes attached to this exercise.
      @return the notes, or an empty string if none were given
   */
   public String getNotes()
   {
      return notes;
   }

   /**
      Updates the notes attached to this exercise.
      @param notes the new notes; null is treated as an empty string
   */
   public void setNotes(String notes)
   {
      this.notes = (notes == null) ? "" : notes;
   }

   /**
      Estimates calories burned performing this exercise. Every
      subclass computes this its own way, since the inputs that drive
      calorie burn are different for each kind of exercise.
      @return estimated calories burned, always nonnegative
   */
   public abstract double estimateCalories();

   /**
      Builds a one-line summary of this exercise, including whatever
      numbers are specific to its type.
      @return a formatted summary line
   */
   public abstract String getSummary();

   /**
      Returns the date and summary together.
      @return the date followed by the summary
   */
   @Override
   public String toString()
   {
      return date + " - " + getSummary();
   }
}