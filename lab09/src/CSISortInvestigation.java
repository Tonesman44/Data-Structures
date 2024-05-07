import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;


public class CSISortInvestigation {

    // Class to store sorting statistics
    static class SortStats {
        int comparisons;
        int exchanges;

        // Constructor to initialize statistics
        public SortStats() {
            comparisons = 0;
            exchanges = 0;
        }
    }

    // Sort1
    public static Map<String, Integer> sort1(int[] arr, SortStats stats) {
        // Mystery sort algorithm 1
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                stats.comparisons++; // Increment comparisons
                arr[j + 1] = arr[j];
                stats.exchanges++; // Increment exchanges
                j--;
            }
            arr[j + 1] = key;
        }

        return createStatsMap(stats); // Convert statistics to map and return
    }

    // Sort2
    public static Map<String, Integer> sort2(int[] arr, SortStats stats) {
        mysterySort(arr, 0, arr.length - 1, stats); // Call mystery sort

        return createStatsMap(stats); // Convert statistics to map and return
    }

    // Mystery sort algorithm
    private static void mysterySort(int[] arr, int low, int high, SortStats stats) {
        if (low < high) {
            int[] partitionStats = mysteryPartition(arr, low, high, stats); // Partition the array
            int pi = partitionStats[0];
            mysterySort(arr, low, pi - 1, stats); // Sort left partition
            mysterySort(arr, pi + 1, high, stats); // Sort right partition
        }
    }

    // Mystery partition method
    private static int[] mysteryPartition(int[] arr, int low, int high, SortStats stats) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            stats.comparisons++; // Increment comparisons
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                stats.exchanges++; // Increment exchanges
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        stats.exchanges++; // Increment exchanges

        return new int[]{i + 1}; // Return pivot index
    }

    // Sort3
    public static Map<String, Integer> sort3(int[] arr, SortStats stats) {
        mysterySort2(arr, 0, arr.length - 1, stats); // Call mystery sort 2

        return createStatsMap(stats); // Convert statistics to map and return
    }

    // Mystery sort 2 algorithm
    private static void mysterySort2(int[] arr, int l, int r, SortStats stats) {
        if (l < r) {
            int m = (l + r) / 2;
            mysterySort2(arr, l, m, stats); // Sort left half
            mysterySort2(arr, m + 1, r, stats); // Sort right half
            mysteryMerge(arr, l, m, r, stats); // Merge the sorted halves
        }
    }

    // Mystery merge method
    private static void mysteryMerge(int[] arr, int l, int m, int r, SortStats stats) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            stats.comparisons++; // Increment comparisons
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            stats.exchanges++; // Increment exchanges
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
            stats.exchanges++; // Increment exchanges
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
            stats.exchanges++; // Increment exchanges
        }
    }

    // Utility method to create Map from SortStats
    private static Map<String, Integer> createStatsMap(SortStats stats) {
        Map<String, Integer> statsMap = new HashMap<>();
        statsMap.put("comparisons", stats.comparisons);
        statsMap.put("exchanges", stats.exchanges);
        return statsMap;
    }

    // Testing method
    public static void runTests() {
        Random random = new Random();

        try {
            FileWriter writer = new FileWriter("sorting_results.csv");
            // Write headers
            writer.append("Sorting Algorithm,Input Size,Comparisons,Exchanges\n");

            for (int size = 100; size <= 1000; size += 100) {
                int[] testArray = new int[size];

                for (int i = 0; i < testArray.length; i++) {
                    testArray[i] = random.nextInt(1000);
                }

                // Test each sorting algorithm
                for (int i = 1; i <= 3; i++) {
                    String sortName = "Sort" + i;
                    int[] data = testArray.clone();
                    SortStats stats = new SortStats();
                    long startTime = System.nanoTime();
                    Map<String, Integer> result = null;
                    switch (i) {
                        case 1:
                            result = sort1(data, stats);
                            break;
                        case 2:
                            result = sort2(data, stats);
                            break;
                        case 3:
                            result = sort3(data, stats);
                            break;
                    }
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000; // milliseconds

                    // Write results to CSV
                    writer.append(sortName + "," + size + "," + result.get("comparisons") + "," + result.get("exchanges") + "\n");
                }
            }

            writer.flush();
            writer.close();
            System.out.println("Results written to sorting_results.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create scatterplot image
        createScatterplotImage("sorting_results.csv", "Input Size", "Comparisons", "scatterplot.png");

        System.out.println("Scatterplot image created: scatterplot.png");
    }

    // Method to create scatterplot image
    public static void createScatterplotImage(String inputFile, String xLabel, String yLabel, String fileName) {
        try {
            List<Integer> xData = new ArrayList<>();
            List<Integer> yData = new ArrayList<>();
            List<Color> colors = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            reader.readLine(); // skip headers
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int xValue = Integer.parseInt(parts[1]); // Assuming input size is in the second column
                int yValue = Integer.parseInt(parts[2]); // Assuming comparisons is in the third column
                xData.add(xValue);
                yData.add(yValue);
                // Assigning colors based on sorting algorithm
                String sortName = parts[0];
                switch (sortName) {
                    case "Sort1":
                        colors.add(Color.BLUE);
                        break;
                    case "Sort2":
                        colors.add(Color.RED);
                        break;
                    case "Sort3":
                        colors.add(Color.GREEN);
                        break;
                    default:
                        colors.add(Color.BLACK);
                        break;
                }
            }
            reader.close();

            // Find min and max values for scaling
            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (int i = 0; i < xData.size(); i++) {
                int x = xData.get(i);
                int y = yData.get(i);
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }

            // Create BufferedImage
            int width = 800;
            int height = 600;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            // Plot data points
            for (int i = 0; i < xData.size(); i++) {
                int x = xData.get(i);
                int y = yData.get(i);
                int scaledX = scaleValue(x, minX, maxX, 50, width - 50);
                int scaledY = scaleValue(y, minY, maxY, height - 50, 50);
                g2d.setColor(colors.get(i));
                g2d.fillOval(scaledX - 2, scaledY - 2, 4, 4);
            }

            // Draw axes and labels
            g2d.setColor(Color.BLACK);
            g2d.drawLine(50, height - 50, width - 50, height - 50); // X-axis
            g2d.drawLine(50, height - 50, 50, 50); // Y-axis
            g2d.drawString(xLabel, width / 2 - 20, height - 20);
            g2d.drawString(yLabel, 10, height / 2 + 20);

            // Save image
            File output = new File(fileName);
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to scale values to fit within a range
    private static int scaleValue(int value, int minInput, int maxInput, int minOutput, int maxOutput) {
        return ((value - minInput) * (maxOutput - minOutput) / (maxInput - minInput)) + minOutput;
    }

    // Main method
    public static void main(String[] args) {
        runTests();
    }
}
