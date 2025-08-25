package main.utils;

import main.model.Vehicle;

public class VehicleLinkedList {
    private class Node {
        Vehicle vehicle;
        Node next;
        Node prev;

        Node (Vehicle vehicle) {
            this.vehicle = vehicle;
            this.next = null;
            this.prev = null;
        }
    }

    public class VehicleLinkedListIterator {
        Node current = null;

        public boolean hasNext() {
            return current.next!=null;
        }

        public boolean hasPrevious() {
            return current.prev!=null;
        }

        public Vehicle next() {
            if (current!=null) {
                current = current.next;
            }
            else {
                current = first;
            }
            return current.vehicle;
        }

        public Vehicle previous() {
            current = current.prev;
            return current.vehicle;
        }
    }

    public int size;
    Node first = null;
    Node last = null;

    public void insertLast(Vehicle vehicle) {
        Node node = new Node(vehicle);
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

    public VehicleLinkedListIterator getIterator() {
        return new VehicleLinkedListIterator();
    }
}