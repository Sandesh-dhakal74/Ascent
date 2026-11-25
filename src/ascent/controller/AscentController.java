package ascent.controller;

import ascent.model.CardioExercise;
import ascent.model.Exercise;
import ascent.model.FlexibilityExercise;
import ascent.model.Goal;
import ascent.model.StrengthExercise;
import ascent.model.Workout;
import ascent.model.WorkoutLog;

/**
 * Holds the state of the running application (the workout log, the
 * workout being built, and the calorie goal) and carries out each
 * action the user can take. In Phase 1 this state lived inside
 * ConsoleUI. Moving it here means the Swing screens only collect
 * input and display results, and all the rules stay in the model
 * classes.
 *
 * Every method either does its job or throws an exception with a
 * message that is safe to show to the user.
 */
public class AscentController
{
   /** All the workouts finished so far. */
   private WorkoutLog log;
   /** The workout being built right now, or null if none is open. */
   private Workout currentWorkout;
   /** The calorie goal the user set, or null if there isn't one yet. */
   private Goal currentGoal;

   /**
      Constructs a controller with an empty log, no open workout,
      and no goal.
   */
   public AscentController()
   {
      log = new WorkoutLog();
      currentWorkout = null;
      currentGoal = null;
   }

   /**
      Gets the log of finished workouts.
      @return the workout log
   */
   public WorkoutLog getLog()
   {
      return log;
   }

   /**
      Gets the workout being built.
      @return the open workout, or null if none is open
   */
   public Workout getCurrentWorkout()
   {
      return currentWorkout;
   }

   /**
      Gets the calorie goal.
      @return the goal, or null if none has been set
   */
   public Goal getGoal()
   {
      return currentGoal;
   }

   /**
      Starts a new workout.
      @param name the workout name, must not be blank
      @param date the date in YYYY-MM-DD form, must be a real date
      @throws IllegalStateException if a workout is already open
      @throws IllegalArgumentException if the name or date is invalid
   */
   public void startWorkout(String name, String date)
   {
      if (currentWorkout != null)
      {
         throw new IllegalStateException("A workout is already in progress ("
               + currentWorkout.getWorkoutName() + "). Finish or discard it first");
      }
      currentWorkout = new Workout(name, date);
   }

   /**
      Adds a strength exercise to the open workout, dated the same
      as the workout.
      @param name the lift name
      @param notes optional notes
      @param sets number of sets, must be positive
      @param reps reps per set, must be positive
      @param weightLbs weight per rep in pounds, must not be negative
      @return the exercise that was added
      @throws IllegalStateException if no workout is open
      @throws IllegalArgumentException if any value is invalid
   */
   public Exercise addStrength(String name, String notes, int sets, int reps, double weightLbs)
   {
      Workout workout = requireOpenWorkout();
      Exercise exercise = new StrengthExercise(name, workout.getDate(), notes, sets, reps, weightLbs);
      workout.addExercise(exercise);
      return exercise;
   }

   /**
      Adds a cardio exercise to the open workout, dated the same as
      the workout.
      @param name the activity name
      @param notes optional notes
      @param distanceMiles distance in miles, must be positive
      @param durationMinutes time in minutes, must be positive
      @return the exercise that was added
      @throws IllegalStateException if no workout is open
      @throws IllegalArgumentException if any value is invalid
   */
   public Exercise addCardio(String name, String notes, double distanceMiles, double durationMinutes)
   {
      Workout workout = requireOpenWorkout();
      Exercise exercise = new CardioExercise(name, workout.getDate(), notes, distanceMiles, durationMinutes);
      workout.addExercise(exercise);
      return exercise;
   }

   /**
      Adds a flexibility exercise to the open workout, dated the same
      as the workout.
      @param name the activity name
      @param notes optional notes
      @param durationMinutes time in minutes, must be positive
      @param intensityLevel intensity from 1 to 5
      @return the exercise that was added
      @throws IllegalStateException if no workout is open
      @throws IllegalArgumentException if any value is invalid
   */
   public Exercise addFlexibility(String name, String notes, double durationMinutes, int intensityLevel)
   {
      Workout workout = requireOpenWorkout();
      Exercise exercise = new FlexibilityExercise(name, workout.getDate(), notes, durationMinutes, intensityLevel);
      workout.addExercise(exercise);
      return exercise;
   }

   /**
      Saves the open workout to the log and closes it.
      @return the workout that was saved
      @throws IllegalStateException if no workout is open or it has
      no exercises
   */
   public Workout finishWorkout()
   {
      Workout workout = requireOpenWorkout();
      if (workout.getExerciseCount() == 0)
      {
         throw new IllegalStateException("This workout has no exercises yet. Add one first");
      }
      log.addWorkout(workout);
      currentWorkout = null;
      return workout;
   }

   /**
      Throws away the open workout without saving it.
      @throws IllegalStateException if no workout is open
   */
   public void discardWorkout()
   {
      requireOpenWorkout();
      currentWorkout = null;
   }

   /**
      Sets the calorie goal, replacing any earlier one.
      @param description what the goal is for, must not be blank
      @param targetCalories the calorie target, must be positive
      @throws IllegalArgumentException if either value is invalid
   */
   public void setGoal(String description, double targetCalories)
   {
      currentGoal = new Goal(description, targetCalories);
   }

   /**
      Gets the open workout or fails if there is none.
      @return the open workout
      @throws IllegalStateException if no workout is open
   */
   private Workout requireOpenWorkout()
   {
      if (currentWorkout == null)
      {
         throw new IllegalStateException("Start a workout first");
      }
      return currentWorkout;
   }
}