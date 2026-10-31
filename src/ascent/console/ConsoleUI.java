package ascent.console;

import java.util.Scanner;
import ascent.model.WorkoutLog;
import ascent.model.Workout;
import ascent.model.Goal;
import ascent.model.Exercise;
import ascent.model.StrengthExercise;
import ascent.model.CardioExercise;
import ascent.model.FlexibilityExercise;
import ascent.model.DateUtil;

/**
 * Menu-driven console interface for the Ascent fitness workout
 * planner. Reads commands from the terminal and uses the ascent.model
 * classes to do the actual work, so a Phase 2 GUI can call into the
 * same model classes without needing any of this console code.
 */
public class ConsoleUI
{
   /** Reads what the user types in the console. */
   private Scanner scanner;
   /** All the workouts finished so far. */
   private WorkoutLog log;
   /** The workout being built right now, or null if none is open. */
   private Workout currentWorkout;
   /** The calorie goal the user set, or null if there isn't one yet. */
   private Goal currentGoal;
   /**
      Constructs the console UI with an empty workout log and no
      workout in progress.
   */
   public ConsoleUI()
   {
      scanner = new Scanner(System.in);
      log = new WorkoutLog();
      currentWorkout = null;
      currentGoal = null;
   }

   /**
      Runs the menu loop until the user chooses to quit.
   */
   public void run()
   {
      System.out.println("===== Ascent: Fitness Workout Planner =====");
      boolean running = true;
      while (running)
      {
         printMenu();
         int choice = readMenuChoice();
         switch (choice)
         {
            case 1:
               handleStartWorkout();
               break;
            case 2:
               handleAddExercise();
               break;
            case 3:
               handleFinishWorkout();
               break;
            case 4:
               handleViewHistory();
               break;
            case 5:
               handleSetGoal();
               break;
            case 6:
               handleCheckProgress();
               break;
            case 7:
               running = false;
               System.out.println("Goodbye!");
               break;
         }
         System.out.println();
      }
   }

   /** Prints the list of menu options. */
   private void printMenu()
   {
      System.out.println("1. Start a new workout");
      System.out.println("2. Add an exercise to the current workout");
      System.out.println("3. Finish the current workout");
      System.out.println("4. View workout history");
      System.out.println("5. Set a calorie goal");
      System.out.println("6. Check goal progress");
      System.out.println("7. Quit");
      System.out.print("Choose an option: ");
   }

   /**
      Reads the menu choice, reprompting until the user enters a
      whole number from 1 to 7.
      @return the validated menu choice
   */
   private int readMenuChoice()
   {
      while (true)
      {
         String input = scanner.nextLine().trim();
         try
         {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= 7)
            {
               return choice;
            }
            System.out.print("Please enter a number from 1 to 7: ");
         }
         catch (NumberFormatException e)
         {
            System.out.print("That's not a number. Please enter a number from 1 to 7: ");
         }
      }
   }

   /**
      Reads a line of text, reprompting if it comes back empty.
      @param prompt the prompt to display
      @return the trimmed, nonblank line entered
   */
   private String readRequiredLine(String prompt)
   {
      while (true)
      {
         System.out.print(prompt);
         String input = scanner.nextLine().trim();
         if (!input.isEmpty())
         {
            return input;
         }
         System.out.println("This can't be blank.");
      }
   }

   /**
      Reads a line of text, blank allowed.
      @param prompt the prompt to display
      @return the trimmed line entered, possibly empty
   */
   private String readOptionalLine(String prompt)
   {
      System.out.print(prompt);
      return scanner.nextLine().trim();
   }

   /**
      Reads a date, reprompting until it is a real date written as
      YYYY-MM-DD.
      @param prompt the prompt to display
      @return the valid date text
   */
   private String readDate(String prompt)
   {
      while (true)
      {
         String input = readRequiredLine(prompt);
         if (DateUtil.isValidDate(input))
         {
            return input;
         }
         System.out.println("Use a real date in YYYY-MM-DD form, like 2026-10-30.");
      }
   }

   /**
      Reads a whole number, reprompting until the input actually
      parses as one.
      @param prompt the prompt to display
      @return the parsed integer
   */
   private int readInt(String prompt)
   {
      while (true)
      {
         System.out.print(prompt);
         String input = scanner.nextLine().trim();
         try
         {
            return Integer.parseInt(input);
         }
         catch (NumberFormatException e)
         {
            System.out.println("Please enter a whole number.");
         }
      }
   }

   /**
      Reads a whole number within a range, reprompting until the
      input parses and falls inside [min, max].
      @param prompt the prompt to display
      @param min the smallest acceptable value
      @param max the largest acceptable value
      @return the parsed, in-range integer
   */
   private int readIntInRange(String prompt, int min, int max)
   {
      while (true)
      {
         int value = readInt(prompt);
         if (value >= min && value <= max)
         {
            return value;
         }
         System.out.println("Please enter a number from " + min + " to " + max + ".");
      }
   }

   /**
      Reads a decimal number, reprompting until the input actually
      parses as one.
      @param prompt the prompt to display
      @return the parsed double
   */
   private double readDouble(String prompt)
   {
      while (true)
      {
         System.out.print(prompt);
         String input = scanner.nextLine().trim();
         try
         {
            return Double.parseDouble(input);
         }
         catch (NumberFormatException e)
         {
            System.out.println("Please enter a number.");
         }
      }
   }

   private void handleStartWorkout()
   {
      if (currentWorkout != null)
      {
         System.out.println("A workout is already in progress (" + currentWorkout.getWorkoutName()
               + "). Finish it first before starting a new one.");
         return;
      }
      String name = readRequiredLine("Workout name: ");
      String date = readDate("Date (YYYY-MM-DD): ");
      try
      {
         currentWorkout = new Workout(name, date);
         System.out.println("Started workout: " + name);
      }
      catch (IllegalArgumentException e)
      {
         System.out.println("Could not start workout: " + e.getMessage());
      }
   }

   private void handleAddExercise()
   {
      if (currentWorkout == null)
      {
         System.out.println("Start a workout first (option 1).");
         return;
      }

      System.out.println("Exercise type:");
      System.out.println("  1. Strength");
      System.out.println("  2. Cardio");
      System.out.println("  3. Flexibility");
      int type = readIntInRange("Choose a type: ", 1, 3);

      String name = readRequiredLine("Exercise name: ");
      String notes = readOptionalLine("Notes (optional): ");

      try
      {
         Exercise exercise;
         if (type == 1)
         {
            int sets = readInt("Sets: ");
            int reps = readInt("Reps: ");
            double weight = readDouble("Weight (lbs): ");
            exercise = new StrengthExercise(name, currentWorkout.getDate(), notes, sets, reps, weight);
         }
         else if (type == 2)
         {
            double distance = readDouble("Distance (miles): ");
            double duration = readDouble("Duration (minutes): ");
            exercise = new CardioExercise(name, currentWorkout.getDate(), notes, distance, duration);
         }
         else
         {
            double duration = readDouble("Duration (minutes): ");
            int intensity = readInt("Intensity (1-5): ");
            exercise = new FlexibilityExercise(name, currentWorkout.getDate(), notes, duration, intensity);
         }
         currentWorkout.addExercise(exercise);
         System.out.println("Added: " + exercise.getSummary());
      }
      catch (IllegalArgumentException e)
      {
         System.out.println("Could not add exercise: " + e.getMessage());
      }
   }

   private void handleFinishWorkout()
   {
      if (currentWorkout == null)
      {
         System.out.println("No workout in progress.");
         return;
      }
      if (currentWorkout.getExerciseCount() == 0)
      {
         System.out.println("This workout has no exercises yet. Add one first (option 2).");
         return;
      }
      log.addWorkout(currentWorkout);
      System.out.println("Workout saved.");
      currentWorkout.printSummary();
      currentWorkout = null;
   }

   private void handleViewHistory()
   {
      log.printHistory();
   }

   private void handleSetGoal()
   {
      String description = readRequiredLine("Goal description: ");
      double target = readDouble("Target calories: ");
      try
      {
         currentGoal = new Goal(description, target);
         System.out.println("Goal set.");
      }
      catch (IllegalArgumentException e)
      {
         System.out.println("Could not set goal: " + e.getMessage());
      }
   }

   private void handleCheckProgress()
   {
      if (currentGoal == null)
      {
         System.out.println("No goal set yet (option 5).");
         return;
      }
      currentGoal.printProgress(log);
   }
}