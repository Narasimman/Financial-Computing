package edu.nyu.cims.compfin15.orderbook;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class MarketOrder implements Order {
        private Book book;

    public MarketOrder(Book bk) {
        this.book = bk;
    }

    /**
     * Executes the order at the market price
     * @return
     */
    @Override
    public int executeOrder(BookOrder bookOrder) {
        Set<Map.Entry<Double, LinkedList<BookOrder>>> entrySet;
        // Depending on the type of the order pull the list either in natural order for buy and descending order for sell.
        if (bookOrder.getType() == BookOrder.orderTypes.SELL_MARKET) {
            TreeMap<Double, LinkedList<BookOrder>> priceMap = book.getBidBook().get(bookOrder.getSymbol());
            entrySet = priceMap.descendingMap().entrySet();
        } else {
            TreeMap<Double, LinkedList<BookOrder>> priceMap = book.getAskBook().get(bookOrder.getSymbol());
            entrySet = priceMap.entrySet();
        }
        int res = bookOrder.getSize();
        for(Map.Entry<Double, LinkedList<BookOrder>> entry : entrySet) {
            if (!entry.getKey().isNaN()) {
                // handle orders at the current market price
                LinkedList<BookOrder> list = entry.getValue();
                for (int i = 0; i < list.size(); ++i) {
                    if (res > list.get(i).getSize()) {
                        if (list.get(i).getSize() != 0) {
                            String tradedOrderId = list.get(i).getOrderId();
                            bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId, list.get(i).getSize(), entry.getKey());
                        }
                        // The order on book gets fully executed.
                        res -= list.get(i).getSize();
                        list.remove(i);
                        --i;
                    } else if (res < list.get(i).getSize()) {
                        String tradedOrderId = list.get(i).getOrderId();
                        bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId, list.get(i).getSize(), entry.getKey());
                        // The new order gets fully executed.
                        list.get(i).setSize(list.get(i).getSize() - res);
                        res = 0;
                        break;
                    } else {
                        if (list.get(i).getSize() != 0) {
                            String tradedOrderId = list.get(i).getOrderId();
                            bookOrder.printTrade(bookOrder.getOrderId(), tradedOrderId, list.get(i).getSize(), entry.getKey());
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
}
