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
    private void printTrade(String traderId, String tradeId, int size,  double limitPrice) {
        System.out.println("  - - - - - - - -- - - - - - - -- ");
        System.out.println("||   Order "  + Math.abs(size) + " " + traderId + " traded with " + tradeId + " @" + limitPrice + "  ||");
        System.out.println("  - - - - - - - -- - - - - - - -- ");
    }

    /**
     * This executes the order with the given price
     * @param limitPrice
     * @return
     */
    private int executeLimitOrder(int size, Double limitPrice) {
        LinkedList<BookOrder> list;

        // Pull the list from the corresponding book.
        if(this.type == orderTypes.SELL_LIMIT) {
            list = book.getBidBook().get(this.getSymbol()).get(limitPrice);
        } else {
            list = book.getAskBook().get(this.getSymbol()).get(limitPrice);
        }
        int res = size;
        for (int i = 0; i < list.size(); ++i) {
            if (res > list.get(i).getSize()) {
                res -= list.get(i).getSize();
                if (list.get(i).getSize() != 0) {
                    String tradedOrderId = list.get(i).getOrderId();
                    this.printTrade(this.orderId, tradedOrderId, list.get(i).getSize(), limitPrice);
                }
                // The bid order gets fully executed, removing it from the queue.
                list.remove(i);
                --i;
            } else if (res < list.get(i).getSize()) {
                String tradedOrderId = list.get(i).getOrderId();
                this.printTrade(this.orderId, tradedOrderId, list.get(i).getSize(), limitPrice);
                // The sell order gets fully executed.
                list.get(i).setSize(list.get(i).getSize() - res);
                res = 0;
                break;
            } else {
                if (list.get(i).getSize() != 0) {
                    String tradedOrderId = list.get(i).getOrderId();
                    this.printTrade(this.orderId, tradedOrderId ,list.get(i).getSize(), limitPrice);
                }
                // Both the sell and bid orders get fully executed.
                res = 0;
                list.remove(i);
                break;
            }
        }
        return res;
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
                size = this.executeLimitOrder(this.getSize(), Double.NaN);
            }

            // After executing the market orders, if the sell limit is still not completed,
            // proceed with the limit orders
            if(size > 0) {
                // Execute prices from low to high
                SortedSet<Double> candidatePrices =
                        book.getBidBook().get(this.getSymbol()).navigableKeySet().tailSet(this.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = this.executeLimitOrder(size, canPrice.doubleValue());
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
                size = this.executeLimitOrder(this.getSize(), Double.NaN);
            }


            if(size > 0) {
                // Execute limit orders if the buy limit is still not completed
                SortedSet<Double> candidatePrices =
                        book.getAskBook().get(this.getSymbol()).descendingKeySet().tailSet(this.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = this.executeLimitOrder(size, canPrice.doubleValue());
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
        Set<Map.Entry<Double, LinkedList<BookOrder>>> entrySet;
        // Depending on the type of the order pull the list either in natural order for buy and descending order for sell.
        if (this.type == orderTypes.SELL_MARKET) {
            TreeMap<Double, LinkedList<BookOrder>> priceMap = book.getBidBook().get(this.getSymbol());
            entrySet = priceMap.descendingMap().entrySet();
        } else {
            TreeMap<Double, LinkedList<BookOrder>> priceMap = book.getAskBook().get(this.getSymbol());
            entrySet = priceMap.entrySet();
        }
        int res = size;
        for(Map.Entry<Double, LinkedList<BookOrder>> entry : entrySet) {
            if (!entry.getKey().isNaN()) {
                // handle orders at the current market price
                LinkedList<BookOrder> list = entry.getValue();
                for (int i = 0; i < list.size(); ++i) {
                    if (res > list.get(i).getSize()) {
                        if (list.get(i).getSize() != 0) {
                            String tradedOrderId = list.get(i).getOrderId();
                            this.printTrade(this.orderId, tradedOrderId, list.get(i).getSize(), entry.getKey());
                        }
                        // The order on book gets fully executed.
                        res -= list.get(i).getSize();
                        list.remove(i);
                        --i;
                    } else if (res < list.get(i).getSize()) {
                        String tradedOrderId = list.get(i).getOrderId();
                        this.printTrade(this.orderId, tradedOrderId, list.get(i).getSize(), entry.getKey());
                        // The new order gets fully executed.
                        list.get(i).setSize(list.get(i).getSize() - res);
                        res = 0;
                        break;
                    } else {
                        if (list.get(i).getSize() != 0) {
                            String tradedOrderId = list.get(i).getOrderId();
                            this.printTrade(this.orderId, tradedOrderId, list.get(i).getSize(), entry.getKey());
                        }
                        // Both the new order and the order on book get fully executed.
                        res = 0;
                        list.remove(i);
                        break;
                    }
                }

                if (res == 0) {
                    break;
                }
            }
        }
        return res;
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
                        size = this.executeLimitOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case SELL_MARKET:
                    if (book.getBidBook().containsKey(symbol)) {
                        int size;
                        size = this.executeMarketOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case BUY_LIMIT:
                    if (book.getAskBook().containsKey(symbol)) {
                        int size;
                        size = this.executeLimitOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
                case BUY_MARKET:
                    if (book.getAskBook().containsKey(symbol)) {
                        int size;
                        size = this.executeMarketOrder();
                        ret = (size == 0) ? true : false;
                    }
                    break;
            }
        }
        return ret;
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
}
