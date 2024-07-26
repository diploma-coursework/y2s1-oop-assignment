package org.assignment.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;

class FirebaseConnection {
    private static Firestore db;

    private static void initializeDb() throws IOException {
        // read service account key json
        InputStream serviceAccount = FirebaseConnection.class.getClassLoader().getResourceAsStream("firebase_service_account_key.json");

        // service account key json file not found
        if (serviceAccount == null) {
            throw new IOException("Missing firebase_service_account_key.json in resources folder.");
        }

        // initialize firebase app
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);

        // get a firestore instance
        db = FirestoreClient.getFirestore();
    }

    public static Firestore getDb() {
        if (db == null) {
            try {
                initializeDb();
            } catch (IOException e) {
                System.err.println("Failed to initialize Firestore: " + e.getMessage());
            }
        }
        return db;
    }
}
