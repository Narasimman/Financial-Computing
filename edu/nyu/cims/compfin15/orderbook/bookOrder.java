package edu.nyu.cims.compfin15.orderbook;

import java.util.SortedSet;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class BookOrder {
    private String symbol;
    private int size;
    private String orderId;
    private double limitPrice;
    private boolean dead;
    private orderTypes type;
    private Book book;

    private enum orderTypes{SELL_LIMIT, BUY_LIMIT, SELL_MARKET, BUY_MARKET};

    /**
     * Class constructor that specifies the symbol, size, id and limit price
     * of an order.
     * @param order - Order item
     */
    public BookOrder(NewOrder order, Book book) {
        this.symbol = order.getSymbol();
        this.size = order.getSize();
        this.orderId = order.getOrderId();
        this.limitPrice = order.getLimitPrice();
        this.dead = false;
        this.book = book;
        // Decide the type of the order
        if(order.getSize() < 0) {
            this.type  = (!Double.isNaN(order.getLimitPrice()))? orderTypes.SELL_LIMIT : orderTypes.SELL_MARKET;
        } else {
            this.type  = (!Double.isNaN(order.getLimitPrice()))? orderTypes.BUY_LIMIT : orderTypes.BUY_MARKET;
        }
    }

    public String getSymbol() {
        return symbol;
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

    public double getLimitPrice() {
        return limitPrice;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * This executes the order with the given price
     * @param limitPrice
     * @return
     */
    private int executeLimitOrder(int size, Double limitPrice) {

        return size;
    }
    /**
     * Execute the limit of with the limit price specified in the order
     * @return
     */
    private int executeLimitOrder() {
        int size = this.getSize();

        if(this.type == orderTypes.SELL_LIMIT) {
            // This is sell limit order

            //Execute market orders at this price first
            if(book.getBidBook().containsKey(Double.NaN)) {
                size = executeLimitOrder(this.getSize(), Double.NaN);
            }

            // After executing the market orders, if the sell limit is still not completed,
            // proceed with the limit orders
            if(size > 0) {
                // Execute prices from low to high
                SortedSet<Double> candidatePrices =
                        book.getBidBook().get(symbol).navigableKeySet().tailSet(this.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = executeLimitOrder(size, canPrice.doubleValue());
                        if (size == 0) {
                            break;
                        }
                    }
                }
            }

        } else {
            // This is buy limit order

            // Execute market orders first
            if(book.getAskBook().containsKey(Double.NaN)) {
                size = executeLimitOrder(this.getSize(), Double.NaN);
            }


            if(size > 0) {
                // Execute limit orders if the buy limit is still not completed
                SortedSet<Double> candidatePrices =
                        book.getAskBook().get(symbol).descendingKeySet().tailSet(this.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = executeLimitOrder(size, canPrice.doubleValue());
                        if (size == 0) {
                            break;
                        }
                    }
                }
            }
        }
        return size;
    }

    /**
     * Executes the order at the market price
     * @return
     */
    private int executeMarketOrder() {
        int size = 0;
        return size;
    }

    /**
     * Executes the order of the callee order object
     * @return
     */
    public boolean executeOrder() {
        boolean ret = false;
        String symbol = this.getSymbol();
        String orderId = this.getOrderId();
        double price = this.getLimitPrice();
        switch (this.type) {
            case SELL_LIMIT:
                if(book.getBidBook().containsKey(symbol)) {
                    int size;
                    size = this.executeLimitOrder();
                    ret  = (size == 0)? true : false;
                }
                break;
            case SELL_MARKET:
                if(book.getBidBook().containsKey(symbol)) {
                    int size;
                    size = this.executeMarketOrder();
                    ret  = (size == 0)? true : false;
                }
                break;
            case BUY_LIMIT:
                if(book.getAskBook().containsKey(symbol)) {
                    int size;
                    size = this.executeLimitOrder();
                    ret  = (size == 0)? true : false;
                }
                break;
            case BUY_MARKET:
                if(book.getAskBook().containsKey(symbol)) {
                    int size;
                    size = this.executeMarketOrder();
                    ret  = (size == 0)? true : false;
                }
                break;
        }
        return ret;
    }
}
