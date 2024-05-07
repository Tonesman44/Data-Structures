import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DoctorPatientScheduler {
    // Hash Map implementation - Taken from textbook page 354
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
        System.out.println("Enter the name of the input CSV file: ");
        String fileName = scanner.nextLine();
        scanner.close();

        // Create hash table-like arrays to store doctors, patients, and their schedules
        Entry<String, Integer>[] doctors = new Entry[100];
        Entry<String, Integer>[] patients = new Entry[100];
        ArrayListCustom[] schedule = new ArrayListCustom[100];

        // Read data from the CSV file
        readDataFromFile(fileName, doctors, patients);

        // Attempt to schedule patients
        boolean isScheduled = schedulePatients(doctors, patients, schedule);
        if (isScheduled) {
            System.out.println("Patients scheduled successfully.");
            printSchedule(schedule, doctors);
            // Print the final schedule
        } else {
            System.out.println("Failed to schedule patients.");
        }
    }

    // Method to print the schedule
    public static void printSchedule(ArrayListCustom[] schedule, Entry<String, Integer>[] doctors) {
        for (int i = 0; i < schedule.length; i++) {
            ArrayListCustom patientList = schedule[i]; // Change to ArrayListCustom
            if (patientList != null && patientList.size() > 0) {
                String doctorName = doctors[i].getKey();
                // Prepare schedule string for each doctor
                StringBuilder scheduleStr = new StringBuilder(doctorName + " is scheduled to see ");
                for (int j = 0; j < patientList.size(); j++) {
                    scheduleStr.append(patientList.get(j));
                    if (j < patientList.size() - 1) {
                        scheduleStr.append(" and ");
                    }
                }
                System.out.println(scheduleStr.toString());
            }
        }
    }

    // Method to recursively schedule patients based on available doctors and their hours
    public static boolean schedulePatients(Entry<String, Integer>[] doctors, Entry<String, Integer>[] patients, ArrayListCustom[] schedule) {
        // If patients array is empty, all patients are scheduled
        boolean allPatientsScheduled = true;
        for (Entry<String, Integer> patientEntry : patients) {
            if (patientEntry != null) {
                allPatientsScheduled = false;
                break;
            }
        }
        if (allPatientsScheduled) {
            return true;
        }

        // Iterate through each patient
        for (Entry<String, Integer> patientEntry : patients) {
            if (patientEntry == null) continue;
            String patient = patientEntry.getKey();
            int patientHours = patientEntry.getValue();

            // Iterate through each doctor
            for (Entry<String, Integer> doctorEntry : doctors) {
                if (doctorEntry == null) continue;
                String doctor = doctorEntry.getKey();
                int doctorHours = doctorEntry.getValue();

                // If the patient's required hours can be accommodated by the doctor
                if (patientHours <= doctorHours) {
                    // Add patient to the doctor's schedule
                    ArrayListCustom patientList = schedule[getIndex(doctor)]; // Change to ArrayListCustom
                    if (patientList == null) {
                        patientList = new ArrayListCustom(); // Change to ArrayListCustom
                        schedule[getIndex(doctor)] = patientList;
                    }
                    patientList.add(patient);

                    // Update doctor's available hours and remove the patient
                    doctorEntry.setValue(doctorHours - patientHours);
                    patients[getIndex(patient)] = null;

                    // Recursively call schedulePatients for remaining patients
                    if (schedulePatients(doctors, patients, schedule)) {
                        return true; // If scheduling successful, return true
                    }

                    // Backtrack: Remove patient from schedule and restore doctor's hours
                    doctorEntry.setValue(doctorHours);
                    patientList.remove(patient);
                    patients[getIndex(patient)] = patientEntry;
                }
            }
        }
        return false; // If no scheduling option is successful, return false
    }

    // Method to read data from a CSV file and populate doctors and patients arrays
    public static void readDataFromFile(String fileName, Entry<String, Integer>[] doctors, Entry<String, Integer>[] patients) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0].trim();
                String name = parts[1].trim();
                int hours = Integer.parseInt(parts[3].trim());

                // Determine whether the entry is for a doctor or a patient
                if (type.equals("doctor")) {
                    doctors[getIndex(name)] = new Entry<>(name, hours);
                } else if (type.equals("patient")) {
                    patients[getIndex(name)] = new Entry<>(name, hours);
                }
            }
            reader.close(); // Close the file reader
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Helper method to get the index for the hash table-like arrays
    private static int getIndex(String key) {
        return Math.abs(key.hashCode() % 100); // Using modulo 100 for simplicity
    }
}

/*
Enter the name of the input text file:
C:\Users\anton\IdeaProjects\Data Structures\Final Project\src/DoctorPatient.txt
Patients scheduled successfully.
Doctor Sacks is scheduled to see Patient Lacks and Patient Washkansky
Doctor Thomas is scheduled to see Patient St. Martin and Patient Giese and Patient Gage
Doctor Ofri is scheduled to see Patient Molaison and Patient Writebol
Doctor Taussig is scheduled to see Patient Sandoval
 */