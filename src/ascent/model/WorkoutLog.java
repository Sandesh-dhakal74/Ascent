package ascent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The full training history: every workout a user has logged, in the
 * order they were added.
 */
public class WorkoutLog
{
   private List<Workout> workouts;

   /**
      Constructs an empty workout log.
   */
   public WorkoutLog()
   {
      this.workouts = new ArrayList<Workout>();
   }

   /**
      Adds a completed workout to the log.
      @param workout the workout to add, must not be null
   */
   public void addWorkout(Workout workout)
   {
      if (workout == null)
      {
         throw new IllegalArgumentException("Workout cannot be null");
      }
      workouts.add(workout);
   }

   /**
      Gets every workout in the log.
      @return the list of workouts, in the order they were added
   */
   public List<Workout> getWorkouts()
   {
      return workouts;
   }

   /**
      Gets how many workouts have been logged.
      @return the workout count
   */
   public int getWorkoutCount()
   {
      return workouts.size();
   }

   /**
      Total estimated calories across every workout ever logged. Calls
      each Workout's own getTotalCalories(), which in turn calls each
      Exercise's own estimateCalories(), so this one number is really
      built up from several layers of polymorphic calls underneath.
      @return lifetime total estimated calories
   */
   public double getTotalCaloriesAllTime()
   {
      double total = 0;
      for (Workout workout : workouts)
      {
         total += workout.getTotalCalories();
      }
      return total;
   }

   /**
      Prints every logged workout's summary, followed by a lifetime
      total, or a message if nothing has been logged yet.
   */
   public void printHistory()
   {
      if (workouts.isEmpty())
      {
         System.out.println("No workouts logged yet.");
         return;
      }
      for (Workout workout : workouts)
      {
         workout.printSummary();
         System.out.println();
      }
      System.out.printf("Lifetime total: ~%.0f calories across %d workout(s)%n",
            getTotalCaloriesAllTime(), workouts.size());
   }
}