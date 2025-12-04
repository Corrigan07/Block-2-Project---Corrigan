package com.zork;

public class WifeRequirements {

  private boolean hasFood = false;
  private boolean hasKey = false;
  private boolean knockedOnDoor = false;
  private int pintsDrank = 0;
  private int currentMoney;
  private int timeInMinutes;
  private int health;

  private static final int REQUIREMENTS_NEEDED = 3;
  private static final int MAX_PINTS = 4;
  private static final int MIN_HEALTH = 70;
  private static final int MIN_MONEY = 50;
  private static final int LATE_HOUR = 23; // 11 PM - last acceptable hour

  public WifeRequirements(
    boolean hasFood,
    boolean hasKey,
    boolean knockedOnDoor,
    int pintsDrank,
    int currentMoney,
    int timeInMinutes,
    int health
  ) {
    this.hasFood = hasFood;
    this.hasKey = hasKey;
    this.knockedOnDoor = knockedOnDoor;
    this.pintsDrank = pintsDrank;
    this.currentMoney = currentMoney;
    this.timeInMinutes = timeInMinutes;
    this.health = health;
  }

  public int calculateScore() {
    int score = 0;
    if (hasFood) score++;
    if (hasKey && !knockedOnDoor) score++;
    if (pintsDrank <= MAX_PINTS) score++;
    if (health >= MIN_HEALTH) score++;
    // Check if arrived before midnight (< 1440 minutes = 24 hours from start of day)
    int hourOfDay = (timeInMinutes / 60) % 24;
    if (hourOfDay >= 18 && hourOfDay <= LATE_HOUR) score++; // Between 6 PM and 12 AM same day
    if (currentMoney >= MIN_MONEY) score++;
    return score;
  }

  public boolean isWifeHappy() {
    return calculateScore() >= REQUIREMENTS_NEEDED;
  }

  public String getEndingMessage() {
    int score = calculateScore();

    switch (score) {
      case 6:
        return "Incredible! Your wife couldn't be happier. You've aced it! \n \"Wow, this isn't like you at all!\"";
      case 5:
        return "Great job! Your wife is very pleased with your efforts. \n \"Mustn't of been a good night if you're home in this state haha.\"";
      case 4:
        return "Good effort! Your wife appreciates what you've done. \n \"You know, I was starting to worry there for a bit.\"";
      case 3:
        return "Not bad! Your wife is somewhat satisfied. \n \"I guess it could have been worse, but you can do better.\"";
      case 2:
        return "Oh dear! Your wife is quite disappointed. \n \"I can't believe you came home like this... We need to talk.\"";
      case 1:
        return "Terrible! Your wife is very upset with you. \n \"This is unacceptable! I don't know if I can trust you anymore.\"";
      case 0:
        return "Oh no! Your wife is furious. \n \"Get out of my sight! I never want to see you again!\"";
      default:
        return "An unexpected error occurred while calculating your wife's happiness.";
    }
  }

  public String getScoreBreakdown() {
    StringBuilder breakdown = new StringBuilder();
    breakdown.append("Wife's Happiness Score Breakdown:\n");

    if (hasFood) {
      breakdown.append("- Brought Home Food: Yes (+1)\n");
    } else {
      breakdown.append("- Brought Home Food: No (+0)\n");
    }

    if (hasKey && !knockedOnDoor) {
      breakdown.append("- Has House Key and Didn't Knock: Yes (+1)\n");
    } else {
      breakdown.append("- Has House Key and Didn't Knock: No (+0)\n");
    }

    if (pintsDrank <= MAX_PINTS) {
      breakdown.append("- Pints Drank Within Limit: Yes (+1)\n");
    } else {
      breakdown.append("- Pints Drank Within Limit: No (+0)\n");
    }

    if (health >= MIN_HEALTH) {
      breakdown.append("- Health Above Minimum: Yes (+1)\n");
    } else {
      breakdown.append("- Health Above Minimum: No (+0)\n");
    }

    int hourOfDay = (timeInMinutes / 60) % 24; // Wrap to 24-hour format
    // Check if arrived between 6 PM and 12 AM same day
    if (hourOfDay >= 18 && hourOfDay <= LATE_HOUR) {
      breakdown.append(
        "- Home on time: " + formatTime(timeInMinutes) + " (+1)\n"
      );
    } else {
      breakdown.append("- Home late: " + formatTime(timeInMinutes) + " (+0)\n");
    }

    if (currentMoney >= MIN_MONEY) {
      breakdown.append("- Has Minimum Money: Yes (+1)\n");
    } else {
      breakdown.append("- Has Minimum Money: No (+0)\n");
    }

    int totalScore = calculateScore();
    breakdown.append("Total Score: " + totalScore + " out of 6\n");
    breakdown.append(
      "Requirements needed to please Wife: " + REQUIREMENTS_NEEDED + " /6\n"
    );

    return breakdown.toString();
  }

  private String formatTime(int totalMinutes) {
    int hours = (totalMinutes / 60) % 24; // Wrap hours to 24-hour format
    int minutes = totalMinutes % 60;

    String period = "";
    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        hours -= 12;
      }
    } else {
      period = "AM";
      if (hours == 0) {
        hours = 12;
      }
    }

    return String.format("%02d:%02d %s", hours, minutes, period);
  }
}
