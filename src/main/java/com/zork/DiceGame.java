package com.zork;

import java.util.Random;

public class DiceGame {

  private Random random;
  private static final int MIN_BET = 5;
  private static final int MAX_BET = 20;

  public DiceGame() {
    this.random = new Random();
  }

  public DiceGameResult playRound(int betAmount, int playerMoney) {
    // Validation - return null for invalid bets
    if (betAmount < MIN_BET || betAmount > MAX_BET) {
      return null; // Invalid bet
    }

    if (betAmount > playerMoney) {
      return null; // Not enough money
    }

    // Roll dice
    int playerRoll = rollDice();
    int bobRoll = rollDice();

    // Calculate result
    int moneyChange;
    if (playerRoll > bobRoll) {
      moneyChange = betAmount; // Win
    } else if (playerRoll < bobRoll) {
      moneyChange = -betAmount; // Lose
    } else {
      moneyChange = 0; // Tie
    }

    return new DiceGameResult(playerRoll, bobRoll, moneyChange);
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
