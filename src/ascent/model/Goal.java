package ascent.model;

/**
 * A simple calorie target, checked against a WorkoutLog's logged
 * history to see how close the user is to reaching it.
 */
public class Goal
{
   private String description;
   private double targetCalories;

   /**
      Constructs a calorie goal.
      @param description what the goal is for (e.g. "Weekly calorie target")
      @param targetCalories the calorie target, must be positive
   */
   public Goal(String description, double targetCalories)
   {
      if (description == null || description.isBlank())
      {
         throw new IllegalArgumentException("Goal description cannot be empty");
      }
      if (targetCalories <= 0)
      {
         throw new IllegalArgumentException("Target calories must be positive");
      }
      this.description = description;
      this.targetCalories = targetCalories;
   }

   /**
      Gets the goal's description.
      @return the description
   */
   public String getDescription()
   {
      return description;
   }

   /**
      Gets the calorie target.
      @return the target calories
   */
   public double getTargetCalories()
   {
      return targetCalories;
   }

   /**
      Checks how much of the target a workout log's history has
      reached, as a percentage. Can exceed 100 if the target was
      passed.
      @param log the workout log to check against, must not be null
      @return progress toward the target, as a percentage
   */
   public double getProgressPercent(WorkoutLog log)
   {
      if (log == null)
      {
         throw new IllegalArgumentException("Workout log cannot be null");
      }
      return (log.getTotalCaloriesAllTime() / targetCalories) * 100;
   }

   /**
      Checks whether a workout log's logged calories meet or exceed
      this goal's target.
      @param log the workout log to check against, must not be null
      @return true if the target has been reached
   */
   public boolean isAchieved(WorkoutLog log)
   {
      if (log == null)
      {
         throw new IllegalArgumentException("Workout log cannot be null");
      }
      return log.getTotalCaloriesAllTime() >= targetCalories;
   }

   /**
      Prints the goal, the current progress, and either a success
      message or how many calories remain.
      @param log the workout log to check against, must not be null
   */
   public void printProgress(WorkoutLog log)
   {
      double achieved = log.getTotalCaloriesAllTime();
      System.out.println("Goal: " + description);
      System.out.printf("Progress: ~%.0f / %.0f calories (%.0f%%)%n",
            achieved, targetCalories, getProgressPercent(log));
      if (isAchieved(log))
      {
         System.out.println("Goal achieved!");
      }
      else
      {
         System.out.printf("Remaining: ~%.0f calories%n", targetCalories - achieved);
      }
   }
}