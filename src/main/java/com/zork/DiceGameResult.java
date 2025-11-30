package com.zork;

public class DiceGameResult {

  private int playerRoll;
  private int bobRoll;
  private int moneyChange;

  public DiceGameResult(int playerRoll, int bobRoll, int moneyChange) {
    this.playerRoll = playerRoll;
    this.bobRoll = bobRoll;
    this.moneyChange = moneyChange;
  }

  public int getPlayerRoll() {
    return playerRoll;
  }

  public int getBobRoll() {
    return bobRoll;
  }

  public int getMoneyChange() {
    return moneyChange;
  }

  public boolean isWin() {
    return moneyChange > 0;
  }

  public boolean isTie() {
    return moneyChange == 0;
  }

  public boolean isLoss() {
    return moneyChange < 0;
  }
}
