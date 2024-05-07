import java.util.Iterator;
import java.util.Scanner;

public class CircularLinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Constructor
    public CircularLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add a node to the end of the list
    public void add(E item) {
        add(size, item);
    }

    // Add a node at a specified index
    public void add(int index, E item) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index out of bounds");

        Node<E> newNode = new Node<>(item);
        if (size == 0) {
            head = newNode;
            tail = newNode;
            newNode.next = newNode;
        } else if (index == 0) {
            newNode.next = head;
            tail.next = newNode;
            head = newNode;
        } else if (index == size) {
            tail.next = newNode;
            newNode.next = head;
            tail = newNode;
        } else {
            Node<E> prevNode = getNode(index - 1);
            newNode.next = prevNode.next;
            prevNode.next = newNode;
        }
        size++;
    }

    // Remove a node at a specified index
    public E remove(int index) {
        if (size == 0)
            throw new IllegalStateException("List is empty");
        E removedItem;
        if (size == 1) {
            removedItem = head.item;
            head = null;
            tail = null;
        } else {
            Node<E> prevNode = tail;
            for (int i = 0; i < index; i++) {
                prevNode = prevNode.next;
            }
            removedItem = prevNode.next.item;
            prevNode.next = prevNode.next.next;
            //adjust head if it was removed
            if(removedItem == head.item)
                head = prevNode.next;
            //adjust tail if it was removed
            if(removedItem == tail.item)
                tail = prevNode;
        }
        size--;
        return removedItem;
    }


    // Get the node at a specified index
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index out of bounds");

        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    // Stringify the list
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (size == 0) {
            result.append("Empty list");
        } else {
            Node<E> current = head;
            for (int i = 0; i < size; i++) {
                result.append(current.item).append(" ==> ");
                current = current.next;
            }
        }
        return result.toString();
    }

    // Iterator
    public Iterator<E> iterator() {
        return new ListIterator<E>();
    }

    // Inner class for iterator
    private class ListIterator<E> implements Iterator<E> {
        private Node<E> nextItem;
        private int index;

        public ListIterator() {
            nextItem = (Node<E>) head;
            index = 0;
        }

        public boolean hasNext() {
            return size > 0;
        }

        public E next() {
            if (!hasNext())
                throw new IllegalStateException("No next element");

            E currentItem = nextItem.item;
            nextItem = nextItem.next;
            index++;
            index = index % size; // Adjust index to handle circular removal
            return currentItem;
        }

        public void remove() {
            if (size == 0)
                throw new IllegalStateException("List is empty");
            index = index == 0 ? size - 1 : index - 1;
            CircularLinkedList.this.remove(index);
        }
    }

    // Node class
    private static class Node<E> {
        private E item;
        private Node<E> next;

        public Node(E item) {
            this.item = item;
            this.next = null;
        }
    }

    // Main method
    public static void main(String[] args) {
        CircularLinkedList<Integer> soldiers = new CircularLinkedList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of soldiers:");
        int n = scanner.nextInt();
        System.out.println("Enter the count of soldiers to remove:");
        int k = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            soldiers.add(i);
        }
        System.out.println("Initial circle: " + soldiers);

        Iterator<Integer> iterator = soldiers.iterator();
        while (soldiers.size > 2) {
            for (int i = 0; i < k - 1; i++) {
                if (!iterator.hasNext())
                    iterator = soldiers.iterator(); // Restart from the beginning if end is reached
                iterator.next(); // Move to the next soldier
            }
            int removedSoldier = iterator.next(); // Get the soldier to be removed
            iterator.remove(); // Remove the soldier
            System.out.println("Soldier " + removedSoldier + " was killed. Resulting circle: " + soldiers);

            // Move the iterator to the next soldier
            if (!iterator.hasNext())
                iterator = soldiers.iterator(); // Restart from the beginning if end is reached
        }

        System.out.println("Josephus and his companion survived: " + soldiers);
    }
}