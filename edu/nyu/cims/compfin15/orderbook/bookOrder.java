package edu.nyu.cims.compfin15.orderbook;

import java.util.*;
import edu.nyu.cims.compfin15.orderbook.OrdersIterator.NewOrderImpl;

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
    private Order thisOrder;


    public static enum orderTypes{SELL_LIMIT, BUY_LIMIT, SELL_MARKET, BUY_MARKET};

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

        if(this.type == orderTypes.BUY_LIMIT || this.type == orderTypes.SELL_LIMIT) {
            thisOrder = new LimitOrder(book);
        } else {
            thisOrder = new MarketOrder(book);
        }
    }

    /**
     * This is the construtor to create an istance for the cancel ro replace order.
     * This is different from the new order since it doesnt have all the information of an order.
     * @param order
     * @param book
     */
    public BookOrder(OrderCxR order, Book book) {
        this.size = order.getSize();
        this.orderId = order.getOrderId();
        this.limitPrice = order.getLimitPrice();
        this.book = book;
    }

    public orderTypes getType() {
        return type;
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
     * Method to print a trade.
     */
    public void printTrade(String traderId, String tradeId, int size,  double limitPrice) {
        System.out.println("  - - - - - - - -- - - - - - - -- ");
        System.out.println("||   Order "  + Math.abs(size) + " " + traderId + " traded with " + tradeId + " @" + limitPrice + "  ||");
        System.out.println("  - - - - - - - -- - - - - - - -- ");
    }

    /**
     * Executes cancel/Replace orders
     */
    public void executeCxROrder() {
        // Set the replaced/cancelled order's size to 0
        book.getLookupTable().get(this.getOrderId()).setSize(0);

        //now, if the size not zero create a new order and put it at the end of the queue in the book
        if (this.getSize() != 0) {
            String symbol = book.getLookupTable().get(this.getOrderId()).getSymbol();
            BookOrder orderOnBook = new BookOrder(new NewOrderImpl(symbol,this.getOrderId(),this.getSize(),this.getLimitPrice()), book);

            // Process sell order.
            if (this.getSize() < 0) {
                boolean fullyExecuted = orderOnBook.executeOrder();
                if (!fullyExecuted) {
                    book.insertIntoAskBook(orderOnBook);
                }
            }

            // Process buy order.
            if (this.getSize() > 0) {
                boolean fullyExecuted = orderOnBook.executeOrder();
                if (!fullyExecuted) {
                    book.insertIntoBidBook(orderOnBook);
                }
            }
        }
    }

    /**
     * Executes the order of the callee order object
     * @return
     */
    public boolean executeOrder() {
        boolean ret = false;
        if(this.getSize() != 0) {
            String symbol = this.getSymbol();
            // Based on the order type call the corresponding handler
            switch (this.type) {
                case SELL_LIMIT:
                    if (book.getBidBook().containsKey(symbol)) {
                        int size;
                        size = thisOrder.executeOrder(this);
                        //size = this.executeLimitOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case SELL_MARKET:
                    if (book.getBidBook().containsKey(symbol)) {
                        int size;
                        size = thisOrder.executeOrder(this);
                        //size = this.executeMarketOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case BUY_LIMIT:
                    if (book.getAskBook().containsKey(symbol)) {
                        int size;
                        size = thisOrder.executeOrder(this);
                        //size = this.executeMarketOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case BUY_MARKET:
                    if (book.getAskBook().containsKey(symbol)) {
                        int size;
                        size = thisOrder.executeOrder(this);
                        //size = this.executeMarketOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
            }
        }
        return ret;
    }
}
