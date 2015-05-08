package edu.nyu.cims.compfin15.orderbook;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Narasimman on 5/8/2015.
 */
public class Book {
    private HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> askBook;
    private HashMap<String, TreeMap<Double, LinkedList<BookOrder>>> bidBook;

    public Book() {
        askBook = new HashMap<String, TreeMap<Double, LinkedList<BookOrder>>>();
        bidBook = new HashMap<String, TreeMap<Double, LinkedList<BookOrder>>>();
    }

    public void insertIntoAskBook(BookOrder bookOrder) {
        if(!askBook.containsKey(bookOrder.getSymbol())) {
            // There is no entry for this Symbol in the ask book. First order
            TreeMap<Double, LinkedList<BookOrder>> priceMap = new TreeMap<Double, LinkedList<BookOrder>>();
            LinkedList<BookOrder> list = new LinkedList<BookOrder>();
            list.add(bookOrder);
            priceMap.put(bookOrder.getLimitPrice(), list);
            askBook.put(bookOrder.getSymbol(), priceMap);
        } else {
            // There is already an entry in the ask book. So, get the list and append this order at the end.
            

        }
    }

    public void insertIntoBidBook(BookOrder bookOrder) {

    }
}
