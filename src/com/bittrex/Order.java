package com.bittrex;

public class Order {
    private String market;
    private double buyPrice;

    public Order(String market, double buyPrice){
        this.market = market;
        this.buyPrice = buyPrice;
    }

    @Override
    public String toString() {
        return "\n  market: " + market + "\n  Buy price: " + buyPrice;
    }
}
