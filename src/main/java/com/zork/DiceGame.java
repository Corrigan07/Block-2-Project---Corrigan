package com.zork;

import java.util.Random;

public class DiceGame {

  private Random random;
  private static final int MIN_BET = 5;
  private static final int MAX_BET = 20;

  public DiceGame() {
    this.random = new Random();
  }

  public int playRound(int betAmount, int playerMoney) {
    if (betAmount < MIN_BET || betAmount > MAX_BET) {
      System.out.println("Bet must be between " + MIN_BET + " and " + MAX_BET);
      return -999; // Invalid bet sentinel value
    }

    if (betAmount > playerMoney) {
      System.out.println("You don't have enough money to place that bet.");
      return -999; // Invalid bet sentinel value
    }

    int playerRoll = rollDice();
    int bobRoll = rollDice();

    System.out.println("\n --- Rolling dice... ---");
    System.out.println("You rolled: " + playerRoll);
    System.out.println("Bob rolled: " + bobRoll);

    if (playerRoll > bobRoll) {
      return betAmount;
    } else if (playerRoll < bobRoll) {
      return -betAmount;
    } else {
      return 0;
    }
  }

  private int rollDice() {
    int die1 = random.nextInt(6) + 1;
    int die2 = random.nextInt(6) + 1;
    return die1 + die2;
  }

  public int getMinBet() {
    return MIN_BET;
  }

  public int getMaxBet() {
    return MAX_BET;
  }
}
