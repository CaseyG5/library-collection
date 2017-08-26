package library;

import java.util.ArrayList;
import java.util.Date;
import java.util.Comparator;

interface Borrowable {
    Date getCheckoutDate();
    Date getDueDate();
    void setCheckoutDate(Date d);
    void setDueDate(Date d);
    boolean isAvailable();                          // check if item is available
    boolean checkOut(LoanedCollection loans);       // add to checked out items
}

interface Iterator<E> {
    boolean isEmpty();
    boolean hasNext();
    E getNext();
}

class LibraryItem {
    final int ID;
    final String title;
    String desc;
    
    LibraryItem(int id, String t)
    { ID = id;  title = t;  desc = "blank"; }       // default description blank
    
    String getDesc() { return desc; }               // copy constr. needed?
    void setDesc(String d) { desc = d; }            // issue with following?  ???
    void showInfo() {  System.out.println(ID + " - " + title);  }
    
    @Override
    public boolean equals(Object x) {
        if(this == x) return true;                                  // same reference
        if(x == null) return false;                                 // no reference
        if(this.getClass() != x.getClass()) return false;           // different class
        return this.title.equalsIgnoreCase( ((LibraryItem) x).title ); // same title?
    }
}

class Book extends LibraryItem implements Borrowable {
    // inherits:  final int ID, final String title, String desc
    final String author;
    final int year;
    final int pages;
    
    Date checkoutDate;
    Date dueDate;
    boolean avail;
    
    Book(int id, String t, String a, int y, int p) {
        super(id, t);   author = a;   year = y;   pages = p;   avail = true;
    }
    Book(Book b) {  
        super(b.ID, b.title);  
        author = b.author; 
        year = b.year; 
        pages = b.pages; 
        avail = b.avail; 
    }
    
    public Date getCheckoutDate() { return checkoutDate; }
    public Date getDueDate() { return dueDate; }
    public void setCheckoutDate(Date d) { checkoutDate = d; }
    public void setDueDate(Date d) { dueDate = d; }
    public boolean isAvailable() { return avail; }
    public boolean checkOut(LoanedCollection loans) { 
        if(!avail) return false;
        int szb4 = loans.size();
        setCheckoutDate(new Date());  // checkoutDate only; dueDate set elsewhere
        avail = false;                 // set avail to false
        loans.add(this);                // add item to "loans"
        return szb4 < loans.size();
    }
    @Override
    void showInfo() {
        System.out.println(ID + " - " + title + " - " + author + " - " 
                + year + " - " + pages + " pgs");
    }
}

class DVD extends LibraryItem implements Borrowable {
    // inherits:  final int ID, final String title, String desc
    final String director;                          // last name of director
    final int year;
    final int rTime;                                // in minutes
    
    Date checkoutDate;
    Date dueDate;
    boolean avail;
    
    DVD(int id, String t, String d, int y, int rt) {
        super(id, t);   director = d;   year = y;   rTime = rt;   avail = true;
    }
    DVD(DVD d) {
        super(d.ID, d.title);
        director = d.director;
        year = d.year;
        rTime = d.rTime;
        avail = d.avail;
    }
    
    public Date getCheckoutDate() { return checkoutDate; }
    public Date getDueDate() { return dueDate; }
    public void setCheckoutDate(Date d) { checkoutDate = d; }
    public void setDueDate(Date d) { dueDate = d; }
    public boolean isAvailable() { return avail; }
    public boolean checkOut(LoanedCollection loans) { 
        if(!avail) return false;
        int szb4 = loans.size();
        setCheckoutDate(new Date());  // checkoutDate only; dueDate set elsewhere                               
        avail = false;                 // set avail to false
        loans.add(this);                // add item to "loans"
        return szb4 < loans.size();
    }
    @Override
    void showInfo() {
        System.out.println(ID + " - " + title + " - " + director + " - " 
                + year + " - " + rTime + " min");
    }
}

class Magazine extends LibraryItem {
    // inherits:  final int ID, final String title, String desc
    final int issue;      // issue #
    final Date pubDate;
    
    Magazine(int id, String t, int i, Date d) {
        super(id, t);   issue = i;   pubDate = d;
    }
    @Override
    void showInfo() {
        System.out.println(ID + " - " + title + " - " + "issue #" 
                + issue + " - " + pubDate);
    }
}

// The most substantial class
class LibraryCollection<E> {
    ArrayList<E> lc;
    
    LibraryCollection() 
    {   lc = new ArrayList<>();  }          // constructor
    
    boolean add(E item) {                                               //tested
        int szb4 = size();
        lc.add(item);
        return size() > szb4;
    }
    
    boolean addAll(LibraryCollection<? extends E> c) {                  //tested
        int szb4 = size();
        Iterator<? extends E> i = c.getIterator();
        while(i.hasNext()) 
            add(i.getNext());
        return size() > szb4; 
    }
    
    boolean remove(Object o) {                                          //tested
        int szb4 = size();
        lc.remove( (E) o );      // cast object to type E
        return size() < szb4;
    }
    
    void clear() {                                                      //tested
        if(isEmpty()) return;
        for(int i=size()-1; i>=0; i--)
            lc.remove(i);
    }
    
    boolean removeAll(LibraryCollection<?> c) {                         //tested
        int szb4 = size();
        for(int i=0; i<c.size(); i++) remove(c.lc.get(i));
        return size() < szb4;    
    }
      // needed to change LibraryCollection<?> to <? extends E>
    boolean retainAll(LibraryCollection<? extends E> c) {               //tested
        // ensure Library contains everything in subset c    
        if(!containsAll(c)) return false;
        clear();
        return addAll(c);
    }
    
    boolean contains(Object o) {                                        //tested
        for( E item : lc )                             
            if(item.equals((E) o)) return true;        // uses equals() override
        return false;                            
    }
    
      // needed to change LibraryCollection<?> to <? extends E>
    boolean containsAll(LibraryCollection<? extends E> c) {             //tested
        Iterator<? extends E> i = c.getIterator();
        while(i.hasNext()) 
            if(!contains(i.getNext())) return false;
        return true;
    }
    
      // Iterator not helpful here
    void printAll() {                                                   //tested
        if(isEmpty()) return;
        LibraryItem temp;
        for(int i=0; i<size(); i++) {
            temp = (LibraryItem) lc.get(i);
            temp.showInfo();
        }
    }
    
    boolean checkOut(Borrowable b, LoanedCollection loans) {            //tested
        if(contains(b))                             // if library has the item
            return b.checkOut(loans);               // try to get it
        return false;                               // see above checkOut()
    }
        
    Object[] toArray() {                                                //tested
        Object[] a = new Object[size()];
        for(int i=0; i<a.length; i++)
            a[i] = lc.get(i);
        return a;
    }
    
    <T> T[] toArray(T[] a) {                                            //tested
        for(int i=0; i<size(); i++)
            a[i] = (T) lc.get(i);
        return a;
    }
    
    int size() { return lc.size(); }                                    //tested
    boolean isEmpty() { return size() == 0; }
    
    public Iterator<E> getIterator() 
    {   return new LibItemIterator();   }
    
    private class LibItemIterator implements Iterator<E> {
        int i = 0;
        public boolean isEmpty() { return lc.size() == 0; }
        public boolean hasNext() { return i < lc.size(); }
        public E getNext() { return lc.get(i++); }
    }
}

// named "LoanedCollection" instead of "CheckoutCart"
class LoanedCollection<E extends Borrowable> extends LibraryCollection<E> {
    // inherits its own lc from super
    
    LoanedCollection() {  super();  }
    
    Iterator<Borrowable> getDueDateIterator(ArrayList<E> list) 
    {   
        Comparator<Borrowable> byDueDate = (b1,b2) -> { 
            if(b1.getDueDate().before(b2.getDueDate())) return -1;
            else if(b1.getDueDate().after(b2.getDueDate())) return 1;
            else return 0;
        };                                      // sort with Comparator - works
        list.sort(byDueDate);
        class DueDateIterator implements Iterator<Borrowable> {
            int i = 0;
            public boolean isEmpty() { return list.size() == 0; }
            public boolean hasNext() { return i < list.size(); }
            public Borrowable getNext() { return list.get(i++); }
        }
        return new DueDateIterator();
    }
    
    E[] getOverDueItems(Date d) {                               
        E temp;
        ArrayList<Borrowable> od = new ArrayList<>();  
        Iterator<Borrowable> i = getDueDateIterator(this.lc);
        while(i.hasNext()) {
            temp = (E) i.getNext();   // cast okay because E extends Borrowable?
            if(temp.getDueDate().before(d)) od.add(temp);     // if overdue
        }                                                        // add to array
        return (E[]) od.toArray(new Borrowable[od.size()]);
    }
}

public class Library {
    public static void main(String[] args) {
        Date today = new Date(2017,2,31);
        
        LibraryCollection<LibraryItem> tinyLib = 
              new LibraryCollection<>();
        Book b1 = new Book(213, "Man's Search for Meaning", "Frankl, Viktor", 1946, 200);
        Book b2 = new Book(201, "Algorithms 4th ed.", "Sedgewick/Wayne", 2011, 931);
        DVD d1 = new DVD(404, "Dragon", "Cohen, Rob", 1993, 126);
        DVD d2 = new DVD(402, "Big Fish", "Burton, Tim", 2003, 125);
        Magazine m1 = new Magazine(713, "MAD", 537, new Date("12/16/2015"));
        Magazine m2 = new Magazine(705, "The Economist", 9031, new Date("03/11/2017"));
        
        b1.setDueDate(new Date(2017,2,21));  b2.setDueDate(new Date(2017,4,27));
        d1.setDueDate(new Date(2017,2,22));  d2.setDueDate(new Date(2016,11,23));
        
        // add books, DVDs, and magazines
        tinyLib.add(b1);  tinyLib.add(b2);
        tinyLib.add(d1);  tinyLib.add(d2);
        tinyLib.add(m1);  tinyLib.add(m2);        
        System.out.println("Library has:");         tinyLib.printAll();
        
        Book b3 = new Book(221, "Taming Your Gremlin", "Carson, Rick", 1984, 173);
        Book b4 = new Book(210, "Java Programming", "Schildt/Skrien", 2013, 1128);
        DVD d3 = new DVD(413, "My Life as a Dog", "Lasse Hallström", 1985, 101);
        DVD d4 = new DVD(419, "Romeo Must Die", "Andrzej Bartkowiak", 2000, 115);
        
        b3.setDueDate(new Date(2017,2,25));  b4.setDueDate(new Date(2017,4,25));
        d3.setDueDate(new Date(2017,3,12));  d4.setDueDate(new Date(2017,0,29));
        
        // test contains, remove, retain, etc.
        System.out.print("\nThat book is ");
        if(tinyLib.contains(b1)) System.out.println("in the collection.");
        else System.out.println("not in the collection.");
        System.out.print("That book is ");
        if(tinyLib.contains(b3)) System.out.println("in the collection.");
        else System.out.println("not in the collection.");
        
        LibraryCollection<LibraryItem> morestuff = new LibraryCollection<>();
        morestuff.add(b3);  morestuff.add(b4);
        morestuff.add(d3);  morestuff.add(d4);
        
        tinyLib.addAll(morestuff);
        System.out.println("\nAdditional stock added.\n");
        System.out.println("Library now has:");     tinyLib.printAll();
        
        LoanedCollection<Borrowable> atHome =
              new LoanedCollection<>();
        
        // checkout items from tinyLib for atHome
        tinyLib.checkOut(b1, atHome);   tinyLib.checkOut(b3, atHome);
        tinyLib.checkOut(d2, atHome);   tinyLib.checkOut(b4, atHome);
        tinyLib.checkOut(d3, atHome);   tinyLib.checkOut(d4, atHome);
        tinyLib.checkOut(b2, atHome);
        //tinyLib.checkOut(m1, atHome);  Will not compile - Magazine not Borrowable
        
        // List loaned items
        System.out.println("\nItems checked out:");
        atHome.printAll();
        
          // list overdue items
        System.out.println("\nOverdue:");
        Borrowable[] od = atHome.getOverDueItems(today);
        for(Borrowable b : od) {
            ((LibraryItem) b).showInfo();
            System.out.println(" Due " + b.getDueDate());
        }
        
        // clear items in the collections
        tinyLib.removeAll(morestuff);
        System.out.println("\nAdditional stock removed.\n");
        System.out.println("What's left:");
        tinyLib.printAll();
        
        System.out.println("\nClearing inventory and going out of business.");
        tinyLib.clear();    tinyLib.printAll();
    }
} 
/*
Library has:
213 - Man's Search for Meaning - Frankl, Viktor - 1946 - 200 pgs
201 - Algorithms 4th ed. - Sedgewick/Wayne - 2011 - 931 pgs
404 - Dragon - Cohen, Rob - 1993 - 126 min
402 - Big Fish - Burton, Tim - 2003 - 125 min
713 - MAD - issue #537 - Wed Dec 16 00:00:00 PST 2015
705 - The Economist - issue #9031 - Sat Mar 11 00:00:00 PST 2017

That book is in the collection.
That book is not in the collection.

Additional stock added.

Library now has:
213 - Man's Search for Meaning - Frankl, Viktor - 1946 - 200 pgs
201 - Algorithms 4th ed. - Sedgewick/Wayne - 2011 - 931 pgs
404 - Dragon - Cohen, Rob - 1993 - 126 min
402 - Big Fish - Burton, Tim - 2003 - 125 min
713 - MAD - issue #537 - Wed Dec 16 00:00:00 PST 2015
705 - The Economist - issue #9031 - Sat Mar 11 00:00:00 PST 2017
221 - Taming Your Gremlin - Carson, Rick - 1984 - 173 pgs
210 - Java Programming - Schildt/Skrien - 2013 - 1128 pgs
413 - My Life as a Dog - Lasse Hallström - 1985 - 101 min
419 - Romeo Must Die - Andrzej Bartkowiak - 2000 - 115 min

Items checked out:
213 - Man's Search for Meaning - Frankl, Viktor - 1946 - 200 pgs
221 - Taming Your Gremlin - Carson, Rick - 1984 - 173 pgs
402 - Big Fish - Burton, Tim - 2003 - 125 min
210 - Java Programming - Schildt/Skrien - 2013 - 1128 pgs
413 - My Life as a Dog - Lasse Hallström - 1985 - 101 min
419 - Romeo Must Die - Andrzej Bartkowiak - 2000 - 115 min
201 - Algorithms 4th ed. - Sedgewick/Wayne - 2011 - 931 pgs

Overdue:
402 - Big Fish - Burton, Tim - 2003 - 125 min
 Due Sat Dec 23 00:00:00 PST 3916
419 - Romeo Must Die - Andrzej Bartkowiak - 2000 - 115 min
 Due Mon Jan 29 00:00:00 PST 3917
213 - Man's Search for Meaning - Frankl, Viktor - 1946 - 200 pgs
 Due Wed Mar 21 00:00:00 PDT 3917
221 - Taming Your Gremlin - Carson, Rick - 1984 - 173 pgs
 Due Sun Mar 25 00:00:00 PDT 3917

Additional stock removed.

What's left:
213 - Man's Search for Meaning - Frankl, Viktor - 1946 - 200 pgs
201 - Algorithms 4th ed. - Sedgewick/Wayne - 2011 - 931 pgs
404 - Dragon - Cohen, Rob - 1993 - 126 min
402 - Big Fish - Burton, Tim - 2003 - 125 min
713 - MAD - issue #537 - Wed Dec 16 00:00:00 PST 2015
705 - The Economist - issue #9031 - Sat Mar 11 00:00:00 PST 2017

Clearing inventory and going out of business.
*/