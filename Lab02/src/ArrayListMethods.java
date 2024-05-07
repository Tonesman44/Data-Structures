import java.util.*;

public class ArrayListMethods {

    // Method to check if all items in the List are unique
    // Creates a hashset which doesn't allow duplicate items, then adds items from list to the hashset
    // If size of the HashSet equals the size of original list, then items are unique
    public static <E> boolean unique(List<E> list) {
        Set<E> set = new HashSet<>(list);
        return set.size() == list.size();
    }

    // Method to return a new List of integers that are multiples of the given int
    // loops through list to see if each element is divisible by num, if so add to list
    public static List<Integer> allMultiples(List<Integer> list, int num) {
        List<Integer> result = new ArrayList<>();
        for (Integer item : list) {
            if (item % num == 0) {
                result.add(item);
            }
        }
        return result;
    }

    // Method to return a new List<String> with all Strings of length 'size'
    // loops through each string in list checking if its length matches the size
    public static List<String> allStringsOfSize(List<String> list, int size) {
        List<String> result = new ArrayList<>();
        for (String item : list) {
            if (item.length() == size) {
                result.add(item);
            }
        }
        return result;
    }

    // Method to check if two Lists are permutations of each other
    // Compares sizes of two lists, then creates hashmaps to store the elements in each list
    // after counting, checks weather the hashmaps are equal
    public static <E> boolean isPermutation(List<E> list1, List<E> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        Map<E, Integer> map1 = new HashMap<>();
        Map<E, Integer> map2 = new HashMap<>();
        for (E item : list1) {
            map1.put(item, map1.getOrDefault(item, 0) + 1);
        }
        for (E item : list2) {
            map2.put(item, map2.getOrDefault(item, 0) + 1);
        }
        return map1.equals(map2);
    }

    // Method to tokenize a string and return a List of words
    // splits input string into words based on whitespace
    // uses split method of the string class with a regular expression which matches one or more whitespace characters
    // Then converts the array of tokens into a list and returns it
    public static List<String> tokenize(String input) {
        String[] tokens = input.split("\\s+");
        return Arrays.asList(tokens);
    }

    // Method to remove all occurrences of an item from the List
    // uses removeAll method of the list interface, passing a singleton list containing only the specified item.
    // Removes all occurrences of that item from the original list
    public static <E> List<E> removeAll(List<E> list, E item) {
        list.removeAll(Collections.singleton(item));
        return list;
    }

    // Test cases
    public static void main(String[] args) {
        // Test unique()
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(unique(list1));  // true

        // Test unique()
        List<Integer> list2 = Arrays.asList(1, 2, 2, 3, 4, 5);
        System.out.println(unique(list2));  // false

        // Test allMultiples()
        List<Integer> list3 = Arrays.asList(1, 25, 2, 5, 30, 19, 57, 2, 25);
        System.out.println(allMultiples(list3, 5));  // [25, 5, 30, 25]

        // Test allStringsOfSize()
        List<String> list4 = Arrays.asList("I", "like", "to", "eat", "eat", "eat", "apples", "and", "bananas");
        System.out.println(allStringsOfSize(list4, 3));  // [eat, eat, eat, and]

        // Test isPermutation()
        List<Integer> list5 = Arrays.asList(1, 2, 4);
        List<Integer> list6 = Arrays.asList(2, 1, 4);
        System.out.println(isPermutation(list5, list6));  // true

        // Test tokenize()
        String input = "Hello, world!";
        System.out.println(tokenize(input));  // [Hello,, world!]

        // Test removeAll()
        List<Integer> list7 = new ArrayList<>(Arrays.asList(1, 4, 5, 6, 5, 5, 2));
        System.out.println(removeAll(list7, 5));  // [1, 4, 6, 2]
    }
}
