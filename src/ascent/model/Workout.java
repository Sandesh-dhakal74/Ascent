package ascent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A named training session made up of one or more exercises performed
 * on the same date, such as "Push Day" or "Leg Day".
 */
public class Workout
{
   /** The session name, like "Push Day". */
   private String workoutName;
   /** The date of the workout, in YYYY-MM-DD form. */
   private String date;
   /** The exercises logged in this workout. */
   private List<Exercise> exercises;
   /**
      Constructs an empty workout.
      @param workoutName the session name (e.g. "Push Day")
      @param date the date performed, in YYYY-MM-DD form
   */
   public Workout(String workoutName, String date)
   {
      if (workoutName == null || workoutName.isBlank())
      {
         throw new IllegalArgumentException("Workout name cannot be empty");
      }
      if (!DateUtil.isValidDate(date))
      {
         throw new IllegalArgumentException("Workout date must be a real date in YYYY-MM-DD form");
      }
      this.workoutName = workoutName;
      this.date = date;
      this.exercises = new ArrayList<Exercise>();
   }

   /**
      Gets the workout's name.
      @return the workout name
   */
   public String getWorkoutName()
   {
      return workoutName;
   }

   /**
      Gets the date this workout was performed.
      @return the date in YYYY-MM-DD form
   */
   public String getDate()
   {
      return date;
   }

   /**
      Adds an exercise to this workout. Accepting the abstract Exercise
      type means a StrengthExercise, CardioExercise, or
      FlexibilityExercise can all be added through this one method.
      @param exercise the exercise to add, must not be null
   */
   public void addExercise(Exercise exercise)
   {
      if (exercise == null)
      {
         throw new IllegalArgumentException("Exercise cannot be null");
      }
      exercises.add(exercise);
   }

   /**
      Gets every exercise logged in this workout.
      @return the list of exercises, in the order they were added
   */
   public List<Exercise> getExercises()
   {
      return exercises;
   }

   /**
      Gets how many exercises are logged in this workout.
      @return the exercise count
   */
   public int getExerciseCount()
   {
      return exercises.size();
   }

   /**
      Total estimated calories for the whole workout. Loops over every
      exercise and calls estimateCalories() through the Exercise
      reference, so each subclass's own formula runs automatically.
      The loop never checks what kind of exercise it is looking at,
      which is exactly what polymorphism is for.
      @return total estimated calories across all exercises
   */
   public double getTotalCalories()
   {
      double total = 0;
      for (Exercise exercise : exercises)
      {
         total += exercise.estimateCalories();
      }
      return total;
   }

   /**
      Prints every exercise's summary line, followed by the workout
      total, or a message if nothing has been logged yet.
   */
   public void printSummary()
   {
      System.out.println(workoutName + " - " + date);
      if (exercises.isEmpty())
      {
         System.out.println("No exercises logged for this workout.");
         return;
      }
      for (Exercise exercise : exercises)
      {
         System.out.println("  " + exercise.getSummary());
      }
      System.out.printf("Total: ~%.0f calories across %d exercise(s)%n",
            getTotalCalories(), exercises.size());
   }
}