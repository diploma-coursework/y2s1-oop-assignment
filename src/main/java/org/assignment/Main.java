package org.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase_service_account_key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://oop-assignment-3a178.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

            // Create and display the main layout (assuming Layout is a class you've defined)
            Layout layout = new Layout();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}