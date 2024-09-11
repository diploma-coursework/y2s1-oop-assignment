package org.assignment.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FirebaseConnection {
    private static Firestore db;
    private static final HashMap<CollectionReference, HashMap<DocumentReference, Object>> cachedDocumentsByCollection = new HashMap<>();

    private static synchronized void initializeDb() throws IOException {
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

    public static void cacheNewDocument(CollectionReference collectionReference, DocumentReference documentReference, Object object) {
        // check if collection is cached before
        if (cachedDocumentsByCollection.containsKey(collectionReference)) {
            // cache the document
            cachedDocumentsByCollection.get(collectionReference).put(documentReference, object);
        } else {
            // create a hashmap for the new collection and cache
            HashMap<DocumentReference, Object> newCollectionCache = new HashMap<>();
            newCollectionCache.put(documentReference, object);

            cachedDocumentsByCollection.put(collectionReference, newCollectionCache);
        }
    }

    public static Object getCachedDocument(CollectionReference collectionReference, DocumentReference documentReference) {
        if (!cachedDocumentsByCollection.containsKey(collectionReference)) return null;
        if (!cachedDocumentsByCollection.get(collectionReference).containsKey(documentReference)) return null;
        return cachedDocumentsByCollection.get(collectionReference).get(documentReference);
    }
}
