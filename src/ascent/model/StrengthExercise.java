package ascent.model;

/**
 * A weight-training exercise: a number of sets, each with a number of
 * reps lifted at a given weight.
 */
public class StrengthExercise extends Exercise
{
   /** Number of sets performed. */
   private int sets;
   /** Reps done in each set. */
   private int reps;
   /** Weight used per rep, in pounds. */
   private double weightLbs;
   /**
      Constructs a strength exercise.
      @param name the lift name (e.g. "Bench Press")
      @param date the date performed, in YYYY-MM-DD form
      @param notes optional notes
      @param sets number of sets, must be positive
      @param reps reps per set, must be positive
      @param weightLbs weight used per rep, in pounds, must not be negative
   */
   public StrengthExercise(String name, String date, String notes,
         int sets, int reps, double weightLbs)
   {
      super(name, date, notes);
      if (sets <= 0)
      {
         throw new IllegalArgumentException("Sets must be positive");
      }
      if (reps <= 0)
      {
         throw new IllegalArgumentException("Reps must be positive");
      }
      if (weightLbs < 0)
      {
         throw new IllegalArgumentException("Weight cannot be negative");
      }
      this.sets = sets;
      this.reps = reps;
      this.weightLbs = weightLbs;
   }

   /**
      Gets the number of sets performed.
      @return the set count
   */
   public int getSets()
   {
      return sets;
   }

   /**
      Gets the number of reps performed per set.
      @return the rep count
   */
   public int getReps()
   {
      return reps;
   }

   /**
      Gets the weight used per rep.
      @return the weight in pounds
   */
   public double getWeightLbs()
   {
      return weightLbs;
   }

   /**
      Total weight moved across every set: sets times reps times weight.
      @return total volume in pounds
   */
   public double getVolume()
   {
      return sets * reps * weightLbs;
   }

   /**
      Strength calorie estimate: roughly 0.05 calories per pound of
      volume moved. A simplified model, not a clinical one.
      @return estimated calories burned
   */
   @Override
   public double estimateCalories()
   {
      return getVolume() * 0.05;
   }

   /**
      Builds a one-line summary showing sets, reps, weight, volume,
      and the estimated calorie cost.
      @return the formatted summary line
   */
   @Override
   public String getSummary()
   {
      return String.format("%s: %d x %d @ %.1f lbs (volume %.0f lbs, ~%.0f cal)",
            getName(), sets, reps, weightLbs, getVolume(), estimateCalories());
   }
}