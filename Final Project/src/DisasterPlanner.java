import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DisasterPlanner {

    // Entry class to store city name and distance (Hash Map Implementation Page 354)
    private static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the input text file: ");
        String fileName = scanner.nextLine();
        System.out.println("Enter the maximum number of cities to supply: ");
        int numCities = scanner.nextInt();
        scanner.close();

        // Custom ArrayLists for road network and supply locations
        ArrayListCustom<Entry<String, ArrayListCustom<Entry<String, Integer>>>> roadNetwork = new ArrayListCustom<>();
        ArrayListCustom<String> supplyLocations = new ArrayListCustom<>();

        // Read data from the text file
        readDataFromFile(fileName, roadNetwork);

        // Allocate resources and check if disaster planning is successful
        boolean isPossible = allocateResources(roadNetwork, numCities, supplyLocations);
        if (isPossible) {
            System.out.println("Disaster planning successful!");
            System.out.println("Supply locations: " + supplyLocations);
        } else {
            System.out.println("Disaster planning failed. No solution possible with the chosen maximum number of cities.");
        }
    }

    // Method to read data from the input text file and store city names and distances
    public static void readDataFromFile(String fileName, ArrayListCustom<Entry<String, ArrayListCustom<Entry<String, Integer>>>> roadNetwork) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String origin = parts[0].trim();
                String[] destinations = parts[1].split(",");
                ArrayListCustom<Entry<String, Integer>> neighbors = new ArrayListCustom<>();
                for (String dest : destinations) {
                    String[] cityAndDistance = dest.trim().split("\\("); // Split city name and distance
                    String city = cityAndDistance[0].trim();
                    Integer distance = Integer.parseInt(cityAndDistance[1].replaceAll("\\)", "").trim()); // Parse distance
                    Entry<String, Integer> cityEntry = new Entry<>(city, distance);
                    neighbors.add(cityEntry);
                }
                Entry<String, ArrayListCustom<Entry<String, Integer>>> entry = new Entry<>(origin, neighbors);
                roadNetwork.add(entry);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to allocate resources based on road network and maximum number of cities
    public static boolean allocateResources(ArrayListCustom<Entry<String, ArrayListCustom<Entry<String, Integer>>>> roadNetwork, int numCities, ArrayListCustom<String> supplyLocations) {
        ArrayListCustom<String> visited = new ArrayListCustom<>();

        // Start with the first city in the road network
        String startCity = roadNetwork.get(0).getKey();
        visited.add(startCity);

        for (int i = 0; i < visited.size(); i++) {
            String currentCity = visited.get(i);

            // Add current city to supply locations if not already present
            if (!supplyLocations.contains(currentCity)) {
                supplyLocations.add(currentCity);
            }

            // Check if maximum number of cities reached
            if (supplyLocations.size() >= numCities) {
                return true;
            }

            // Visit neighboring cities and add them to visited list
            ArrayListCustom<Entry<String, Integer>> neighbors = roadNetwork.get(i).getValue();
            for (int j = 0; j < neighbors.size(); j++) {
                String neighbor = neighbors.get(j).getKey();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                }
            }
        }
        return false;
    }

    // Custom ArrayList implementation
    public static class ArrayListCustom<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] array;
        private int size;

        // Constructor
        public ArrayListCustom() {
            array = new Object[DEFAULT_CAPACITY];
        }

        // Add element to the list
        public void add(E element) {
            if (size == array.length) {
                increaseCapacity();
            }
            array[size++] = element;
        }

        // Get element at index
        @SuppressWarnings("unchecked")
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return (E) array[index];
        }

        // Check if element is present in the list
        public boolean contains(Object element) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }

        // Get size of the list
        public int size() {
            return size;
        }

        // Increase capacity of the list
        private void increaseCapacity() {
            int newCapacity = array.length * 2;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }

        // Customized string representation of the list
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < size; i++) {
                Object element = array[i];
                if (element instanceof Entry<?, ?>) {
                    Entry<String, Integer> entry = (Entry<String, Integer>) element;
                    sb.append(entry.getKey());
                    if (entry.getValue() != null) {
                        sb.append(" (").append(entry.getValue()).append(")");
                    }
                } else if (element instanceof String) {
                    sb.append(element);
                }
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }
}


/*
Enter the name of the input text file:
C:\Users\anton\IdeaProjects\Data Structures\Final Project\src/DisasterExample.txt
Enter the maximum number of cities to supply:
2
Disaster planning successful!
Supply locations: [Seattle, Butte]
 */
