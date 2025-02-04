package fr.umontpellier.iut.shared;

public class Winner {
    private final String winnerPrice;
    private final int secondPrice;

    public Winner(String winnerPrice, int secondPrice) {
        this.winnerPrice = winnerPrice;
        this.secondPrice = secondPrice;
    }

    public String getWinnerPrice() {
        return winnerPrice;
    }

    public int getSecondPrice() {
        return secondPrice;
    }

    public String getMessage() {
        return winnerPrice + secondPrice;
    }
}
