package edu.nyu.cims.compfin15.orderbook;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class BookOrder {
    private String symbol;
    private int size;
    private String orderId;
    private double limitPrice;
    boolean dead;

    /**
     * Class constructor that specifies the symbol, size, id and limit price
     * of an order.
     *
     * @param symbol, symbol of an order
     * @param size, size of an order
     * @param orderId, id of an order
     * @param limitPrice, limit price of an order. NaN indicates it is a
     *                    market order
     */
    public BookOrder(NewOrder order) {
        this.symbol = order.getSymbol();
        this.size = order.getSize();
        this.orderId = order.getOrderId();
        this.limitPrice = order.getLimitPrice();
        this.dead = false;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean executeOrder(boolean orderType) {

        return false;
    }
}
