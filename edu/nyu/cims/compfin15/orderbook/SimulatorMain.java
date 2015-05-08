package edu.nyu.cims.compfin15.orderbook;

import edu.nyu.cims.compfin15.orderbook.OrdersIterator.*;
import edu.nyu.cims.compfin15.orderbook.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Narasimman on 5/6/2015.
 */
public class SimulatorMain {
    private Book book;
    private HashMap<String, BookOrder> lookupTable;

    public SimulatorMain() {
        book = new Book();
        lookupTable = new HashMap<String, BookOrder>();
    }

    private void processOrders(Iterator<Message> iterator, boolean best) {

        Message msg;

        while(iterator.hasNext()) {
            msg = iterator.next();
            if (msg instanceof NewOrder) {
                NewOrder order = (NewOrderImpl) msg;
                BookOrder bookOrder = new BookOrder(order);

                // Sell order
                if(order.getSize() < 0) {
                    boolean completed = bookOrder.executeOrder(true);
                    if(!completed) {
                        book.insertIntoAskBook(bookOrder);
                    }
                } else { // buy order
                    boolean completed = bookOrder.executeOrder(false);
                    if(!completed) {
                        book.insertIntoBidBook(bookOrder);
                    }
                }


            } else if (msg instanceof OrderCxR) {
                OrderCxR orderCxR = (OrderCxRImpl) msg;
            }
        }
    }

    public static void main(String args[]) {
        Iterator<Message> iterator = new OrdersIterator().getIterator();
        SimulatorMain simulator = new SimulatorMain();
        simulator.processOrders(iterator, true);
    }
}
