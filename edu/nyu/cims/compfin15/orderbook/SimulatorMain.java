package edu.nyu.cims.compfin15.orderbook;

import edu.nyu.cims.compfin15.orderbook.OrdersIterator.NewOrderImpl;
import edu.nyu.cims.compfin15.orderbook.OrdersIterator.OrderCxRImpl;

import java.util.Iterator;

/**
 * Created by Narasimman on 5/6/2015.
 */
public class SimulatorMain {
    private Book book;

    /**
     * Constructor
     */
    public SimulatorMain() {
        book = new Book();
    }

    /**
     * This loops through the iterator and executes the order.
     * @param iterator
     * @param best
     */
    private void processOrders(Iterator<Message> iterator, boolean best) {

        Message msg;

        while(iterator.hasNext()) {
            msg = iterator.next();
            if (msg instanceof NewOrder) {
                NewOrder order = (NewOrderImpl) msg;
                BookOrder bookOrder = new BookOrder(order, book);

                // Sell order
                if(order.getSize() < 0) {
                    boolean completed = bookOrder.executeOrder();
                    if(!completed) {
                        book.insertIntoAskBook(bookOrder);
                    }
                } else { // buy order
                    boolean completed = bookOrder.executeOrder();
                    if(!completed) {
                        book.insertIntoBidBook(bookOrder);
                    }
                }


            } else if (msg instanceof OrderCxR) {
                OrderCxR orderCxR = (OrderCxRImpl) msg;
                BookOrder bookOrder = new BookOrder(orderCxR, book);
                bookOrder.executeCxROrder();

            }
            if(best) {
                book.printTopOfBooks();
            }
        }
    }

    public static void main(String args[]) {
        Iterator<Message> iterator = new OrdersIterator().getIterator();
        SimulatorMain simulator = new SimulatorMain();
        simulator.processOrders(iterator, true);
    }
}
