package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/16/2015.
 */
public class Option {
    /* Name of the stock*/
    private String stockName;
    /* Initial price of the stock*/
    private double initialPrice;
    /* Volatility per day */
    private double volatility;
    /* Rate of Interest */
    private double rateOfInterest;
    /* number of days to expire */
    private double numOfDays;
    /* Strike price of the stock at maturity */
    private double strikePrice;
    /* PayOut type */
    private String payoutType;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public double getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(double numOfDays) {
        this.numOfDays = numOfDays;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public void setPaymentType(String payoutType) {
        this.payoutType = payoutType;
    }
}
