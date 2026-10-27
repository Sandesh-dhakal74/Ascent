package ascent.model;

/**
 * A cardio exercise measured by distance covered over a duration,
 * such as running, cycling, or swimming.
 */
public class CardioExercise extends Exercise
{
   private double distanceMiles;
   private double durationMinutes;

   /**
      Constructs a cardio exercise.
      @param name the activity name (e.g. "Morning Run")
      @param date the date performed, in YYYY-MM-DD form
      @param notes optional notes
      @param distanceMiles distance covered in miles, must be positive
      @param durationMinutes time spent in minutes, must be positive
   */
   public CardioExercise(String name, String date, String notes,
         double distanceMiles, double durationMinutes)
   {
      super(name, date, notes);
      if (distanceMiles <= 0)
      {
         throw new IllegalArgumentException("Distance must be positive");
      }
      if (durationMinutes <= 0)
      {
         throw new IllegalArgumentException("Duration must be positive");
      }
      this.distanceMiles = distanceMiles;
      this.durationMinutes = durationMinutes;
   }

   /**
      Gets the distance covered.
      @return the distance in miles
   */
   public double getDistanceMiles()
   {
      return distanceMiles;
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
      Pace in minutes per mile: duration divided by distance.
      @return pace in minutes per mile
   */
   public double getPaceMinPerMile()
   {
      return durationMinutes / distanceMiles;
   }

   /**
      Cardio calorie estimate using a simplified formula: a 10 min/mile
      pace burns roughly 10 calories per minute, and faster paces scale
      that rate up proportionally.
      @return estimated calories burned
   */
   @Override
   public double estimateCalories()
   {
      double paceFactor = 10.0 / getPaceMinPerMile();
      return durationMinutes * 10.0 * paceFactor;
   }

   /**
      Builds a one-line summary showing distance, duration, pace, and
      the estimated calorie cost.
      @return the formatted summary line
   */
   @Override
   public String getSummary()
   {
      return String.format("%s: %.2f mi in %.0f min (pace %.1f min/mi, ~%.0f cal)",
            getName(), distanceMiles, durationMinutes, getPaceMinPerMile(), estimateCalories());
   }
}