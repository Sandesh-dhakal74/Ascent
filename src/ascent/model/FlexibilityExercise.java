package ascent.model;

/**
 * A flexibility or mobility exercise, such as stretching or yoga,
 * measured by duration and perceived intensity rather than weight
 * or distance.
 */
public class FlexibilityExercise extends Exercise
{
   /** Time spent, in minutes. */
   private double durationMinutes;
   /** How hard it felt, from 1 (gentle) to 5 (intense). */
   private int intensityLevel;
   /**
      Constructs a flexibility exercise.
      @param name the activity name (e.g. "Yoga Flow")
      @param date the date performed, in YYYY-MM-DD form
      @param notes optional notes
      @param durationMinutes time spent in minutes, must be positive
      @param intensityLevel perceived intensity from 1 to 5
   */
   public FlexibilityExercise(String name, String date, String notes,
         double durationMinutes, int intensityLevel)
   {
      super(name, date, notes);
      if (durationMinutes <= 0)
      {
         throw new IllegalArgumentException("Duration must be positive");
      }
      if (intensityLevel < 1 || intensityLevel > 5)
      {
         throw new IllegalArgumentException("Intensity level must be between 1 and 5");
      }
      this.durationMinutes = durationMinutes;
      this.intensityLevel = intensityLevel;
   }

   /**
      Gets the duration of the activity.
      @return the duration in minutes
   */
   public double getDurationMinutes()
   {
      return durationMinutes;
   }

   /**
      Gets the perceived intensity level.
      @return the intensity, from 1 (gentle) to 5 (deep/intense)
   */
   public int getIntensityLevel()
   {
      return intensityLevel;
   }

   /**
      Flexibility calorie estimate: duration times a small per-minute
      rate that scales up with intensity level.
      @return estimated calories burned
   */
   @Override
   public double estimateCalories()
   {
      return durationMinutes * (1.5 + intensityLevel * 0.5);
   }

   /**
      Builds a one-line summary showing duration, intensity, and the
      estimated calorie cost.
      @return the formatted summary line
   */
   @Override
   public String getSummary()
   {
      return String.format("%s: %.0f min at intensity %d/5 (~%.0f cal)",
            getName(), durationMinutes, intensityLevel, estimateCalories());
   }
}