package main.utils;

import main.model.EChallan;
import main.model.Vehicle;

public class ChallanLinkedList {
    private class Node {
        EChallan challan;
        Node next;
        Node prev;

        Node (EChallan challan) {
            this.challan = challan;
            this.next = null;
            this.prev = null;
        }
    }

    public class ChallanLinkedListIterator {
        Node current = null;

        public boolean hasNext() {
            return current.next!=null;
        }

        public boolean hasPrevious() {
            return current.prev!=null;
        }

        public EChallan next() {
            if (current!=null) {
                current = current.next;
            }
            else {
                current = first;
            }
            return current.challan;
        }

        public EChallan previous() {
            current = current.prev;
            return current.challan;
        }
    }

    public int size;
    Node first = null;
    Node last = null;

    public void insertLast(EChallan challan) {
        Node node = new Node(challan);
        if (first==null) {
            first = node;
            last = node;
            size++;
            return;
        }
        size++;
        last.next = node;
        node.prev = last;
        last = node;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return first==null;
    }

    public ChallanLinkedListIterator getIterator() {
        return new ChallanLinkedListIterator();
    }
}