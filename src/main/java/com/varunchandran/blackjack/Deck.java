package com.varunchandran.blackjack;

public enum Deck {
  Ace(-1), //
  King(-1), //
  Queen(-1), //
  Jack(-1), //
  Ten(-1), //
  Nine(0), //
  Eight(0), //
  Seven(0), //
  Six(1), //
  Five(1), //
  Four(1), //
  Three(1), //
  Two(1);

  private int value;

  private Deck(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
