package edu.nyu.cims.compfin15.orderbook;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class Book {
    private HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> askBook;
    private HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> bidBook;
    private HashMap<String, BookOrder> lookupTable;

    public Book() {
        askBook = new HashMap<String, TreeMap<Double, LinkedList<BookOrder>>>();
        bidBook = new HashMap<String, TreeMap<Double, LinkedList<BookOrder>>>();
        lookupTable = new HashMap<String, BookOrder>();

    }

    public HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> getAskBook() {
        return askBook;
    }

    public HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> getBidBook() {
        return bidBook;
    }

    public HashMap<String, BookOrder> getLookupTable() {
        return lookupTable;
    }

    private void insertIntoBook(BookOrder bookOrder, HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> book) {

        TreeMap<Double, LinkedList<BookOrder>> priceMap;
        if(!book.containsKey(bookOrder.getSymbol())) {
            // There is no entry for this Symbol in the book. First order
            priceMap = new TreeMap<Double, LinkedList<BookOrder>>();
            LinkedList<BookOrder> list = new LinkedList<BookOrder>();
            list.add(bookOrder);
            priceMap.put(bookOrder.getLimitPrice(), list);
            book.put(bookOrder.getSymbol(), priceMap);
        } else {
            // There is already an entry in the ask book. So, get the list and append this order at the end.
            priceMap = book.get(bookOrder.getSymbol());
            if(priceMap.containsKey(bookOrder.getLimitPrice())) {
                // if there is already orders for this limit price, then append at the end of the list
                priceMap.get(bookOrder.getLimitPrice()).add(bookOrder);
            } else {
                // There is no previous orders for this limit price. So, create the list and add it.
                LinkedList<BookOrder> list = new LinkedList<BookOrder>();
                list.add(bookOrder);
                priceMap.put(bookOrder.getLimitPrice(), list);
            }
        }

        lookupTable.put(bookOrder.getOrderId(), bookOrder);
    }

    /**
     * inserts the value into the ask book
     * @param bookOrder
     */
    public void insertIntoAskBook(BookOrder bookOrder) {
        //System.out.println("Insert into ask book");
        insertIntoBook(bookOrder, askBook);
    }

    /**
     * inserts the value in the bid book
     * @param bookOrder
     */
    public void insertIntoBidBook(BookOrder bookOrder) {
        //System.out.println("Insert into bid book");
        insertIntoBook(bookOrder, bidBook);
    }

    /**
     * Method to print the best (top) of ask and bid books.
     */
    public void printTopOfBooks() {
        System.out.println("Best of the ask book:");
        for (Map.Entry<String, TreeMap<Double, LinkedList<BookOrder>>> entry: askBook.entrySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(entry.getKey());
            sb.append(" ");
            // Skip dead order and only prints live order.
            for (BookOrder order: entry.getValue().firstEntry().getValue()) {
                if (order.getSize() > 0) {
                    sb.append(String.format("%.2f", order.getLimitPrice()));
                    sb.append(",ask,");
                    sb.append(order.getSize());
                    sb.append(" ");
                }
            }
            System.out.println(sb);
        }
        System.out.println("Best of the bid book:");
        for (Map.Entry<String, TreeMap<Double, LinkedList<BookOrder>>> entry: bidBook.entrySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(entry.getKey());
            sb.append(": ");
            for (BookOrder order: entry.getValue().lowerEntry(Double.NaN).getValue()) {
                // Skip dead order and only prints live order.
                if (order.getSize() > 0) {
                    sb.append(String.format("%.2f", order.getLimitPrice()));
                    sb.append(",bid,");
                    sb.append(order.getSize());
                    sb.append(" ");
                }
            }
            System.out.println(sb);
        }
    }
}
