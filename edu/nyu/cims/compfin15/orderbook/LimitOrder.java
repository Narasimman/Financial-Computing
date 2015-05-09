package edu.nyu.cims.compfin15.orderbook;

import java.util.LinkedList;
import java.util.SortedSet;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class LimitOrder implements Order {
    private Book book;

    /**
     * Constructor
     * @param bk
     */
    public LimitOrder(Book bk) {
        this.book = bk;
    }

    /**
     * This executes the order with the given price
     * @param limitPrice
     * @return
     */
    private int executeLimitOrder(BookOrder bookOrder, int size, Double limitPrice) {
        LinkedList<BookOrder> list;

        // Pull the list from the corresponding book.
        if(bookOrder.getType() == BookOrder.orderTypes.SELL_LIMIT) {
            list = book.getBidBook().get(bookOrder.getSymbol()).get(limitPrice);
        } else {
            list = book.getAskBook().get(bookOrder.getSymbol()).get(limitPrice);
        }
        int res = size;
        for (int i = 0; i < list.size(); ++i) {
            if (res > list.get(i).getSize()) {
                res -= list.get(i).getSize();
                if (list.get(i).getSize() != 0) {
                    String tradedOrderId = list.get(i).getOrderId();
                    bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId, list.get(i).getSize(), limitPrice);
                }
                // The bid order gets fully executed, removing it from the queue.
                list.remove(i);
                --i;
            } else if (res < list.get(i).getSize()) {
                String tradedOrderId = list.get(i).getOrderId();
                bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId, list.get(i).getSize(), limitPrice);
                // The sell order gets fully executed.
                list.get(i).setSize(list.get(i).getSize() - res);
                res = 0;
                break;
            } else {
                if (list.get(i).getSize() != 0) {
                    String tradedOrderId = list.get(i).getOrderId();
                    bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId ,list.get(i).getSize(), limitPrice);
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
     * @Override
     * Execute the limit of with the limit price specified in the order
     * @return
     */
    @Override
    public int executeOrder(BookOrder bookOrder) {
        int size = bookOrder.getSize();
        if(bookOrder.getType() == BookOrder.orderTypes.SELL_LIMIT) {
            // bookOrder is sell limit order

            //Execute market orders at this price first
            if(book.getBidBook().containsKey(Double.NaN)) {
                size = executeLimitOrder(bookOrder, bookOrder.getSize(), Double.NaN);
            }

            // After executing the market orders, if the sell limit is still not completed,
            // proceed with the limit orders
            if(size > 0) {
                // Execute prices from low to high
                SortedSet<Double> candidatePrices =
                        book.getBidBook().get(bookOrder.getSymbol()).navigableKeySet().tailSet(bookOrder.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = executeLimitOrder(bookOrder, size, canPrice.doubleValue());
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
                size = executeLimitOrder(bookOrder, bookOrder.getSize(), Double.NaN);
            }


            if(size > 0) {
                // Execute limit orders if the buy limit is still not completed
                SortedSet<Double> candidatePrices =
                        book.getAskBook().get(bookOrder.getSymbol()).descendingKeySet().tailSet(bookOrder.getLimitPrice());
                for (Double canPrice: candidatePrices) {
                    if (!canPrice.isNaN()) {
                        size = executeLimitOrder(bookOrder, size, canPrice.doubleValue());
                        if (size == 0) {
                            break;
                        }
                    }
                }
            }
        }
        return size;
    }
}
