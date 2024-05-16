package com.example.dictionary_ver3;

import java.io.*;
import java.util.Scanner;

public class EnglishLearningGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Open the text file
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\DictionaryAppOOP2024_6\\src\\main\\java\\com\\example\\dictionary_ver3\\Quizzies.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into its components
                String[] parts = line.split(",");

                String question = parts[1];
                String[] options = {parts[2], parts[3], parts[4], parts[5]};
                char answer = parts[6].charAt(0);

                System.out.println("Fill in the blank to complete the sentence:");
                System.out.println(question);
                System.out.println("A) " + options[0]);
                System.out.println("B) " + options[1]);
                System.out.println("C) " + options[2]);
                System.out.println("D) " + options[3]);

                System.out.print("Enter your answer (A/B/C/D): ");
                char userAnswer = scanner.next().charAt(0);

                if (Character.toUpperCase(userAnswer) == answer) {
                    System.out.println("Correct!");
                } else {
                    System.out.println("Incorrect. The correct answer is " + answer);
                }
            }

            // Close the reader
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
