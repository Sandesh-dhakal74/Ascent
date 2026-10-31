# Ascent

Ascent is a console fitness workout planner. You log workouts, add strength, cardio, and flexibility exercises to them, and Ascent estimates the calories burned for each exercise and for the whole workout. You can also set a calorie goal and check how close your logged workouts are to it.

I built it for CS3354 (Object-Oriented Programming in Java) at Texas State University as the Phase 1 project.

## Requirements

- JDK 11 or newer. The code uses `String.isBlank()`, which was added in Java 11.
- No other libraries.

Run `javac -version` and `java -version` to check. Both should say 11 or higher.

## Entry point

`ascent.console.AscentApp`

## How to compile and run

From the repo root (the folder that contains `src`):

Windows (PowerShell):

```
javac -d out src\ascent\model\*.java src\ascent\console\*.java
java -cp out ascent.console.AscentApp
```

macOS or Linux:

```
javac -d out src/ascent/model/*.java src/ascent/console/*.java
java -cp out ascent.console.AscentApp
```

The compiled classes go in an `out` folder. You can also open `AscentApp.java` in VS Code and click Run.

## Features

- Start a workout with a name and a date. The date must be a real date written as YYYY-MM-DD.
- Add three kinds of exercises to the open workout:
  - Strength: sets, reps, and weight in pounds
  - Cardio: distance in miles and duration in minutes
  - Flexibility: duration in minutes and intensity from 1 to 5
- Finish a workout to save it to the history. A workout needs at least one exercise to be saved.
- View the history, with a total for each workout and a lifetime total.
- Set a calorie goal and check progress toward it, shown as a percent and the calories remaining.
- Input checking. Text where a number is expected is asked for again. Values that break a rule (zero sets, intensity 6, a date like 2026-02-30) are rejected with a message, and nothing already entered is lost.

## Menu

```
1. Start a new workout
2. Add an exercise to the current workout
3. Finish the current workout
4. View workout history
5. Set a calorie goal
6. Check goal progress
7. Quit
```

## How calories are estimated

These are rough estimates for the demo, not medical numbers.

- Strength: volume (sets x reps x weight) x 0.05
- Cardio: duration x 10 x (10 / pace), where pace is minutes per mile. The duration cancels out, so this works out to about 100 calories per mile at any pace.
- Flexibility: duration x (1.5 + intensity x 0.5)

## Project layout

```
src/ascent/model     Exercise (abstract), StrengthExercise, CardioExercise,
                     FlexibilityExercise, Workout, WorkoutLog, Goal, DateUtil
src/ascent/console   AscentApp (main), ConsoleUI
```

The model package does the calculating and never reads input. Three of its methods (`Workout.printSummary`, `WorkoutLog.printHistory`, `Goal.printProgress`) print to the console, so a GUI would use the getters instead of those.

## Sample input

Run the program and type these lines in order. A blank line means just press Enter.

```
1
Push Day
2026-10-30
2
1
Bench Press
(blank)
3
10
135
2
2
Treadmill Run
(blank)
2
20
2
3
Yoga Flow
(blank)
30
3
3
4
5
Weekly calories
1000
6
7
```

You should see these results:

- Bench Press: 3 x 10 @ 135.0 lbs (volume 4050 lbs, ~203 cal)
- Treadmill Run: 2.00 mi in 20 min (pace 10.0 min/mi, ~200 cal)
- Yoga Flow: 30 min at intensity 3/5 (~90 cal)
- Workout total: ~493 calories across 3 exercise(s)
- Goal progress: ~493 / 1000 calories (49%), with ~508 remaining

## Known limitations

- Data is kept in memory only. Everything is lost when you quit.
- Only one goal can be set at a time. Setting a new one replaces the old one.
- Workouts and exercises cannot be edited or deleted after they are added.
- Values are only checked to be positive, so unrealistic numbers such as 30 miles in 1 minute are accepted.
- Dates are stored and shown as text. History is listed in the order workouts were finished, not sorted by date.
- If the input stream is closed (Ctrl+Z on Windows, Ctrl+D elsewhere) the program stops with an exception instead of exiting cleanly. Use option 7 to quit.
- There are no automated tests. I tested by running the program by hand.