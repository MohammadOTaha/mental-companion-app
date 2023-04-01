package com.omar.mentalcompanion.data.data_source.cloud

import com.google.firebase.firestore.FirebaseFirestore

class Firestore(
    private val db: FirebaseFirestore
) {
    fun getCollection(collectionName: String) = db.collection(collectionName)

    fun getDocument(collectionName: String, documentName: String) = db.collection(collectionName).document(documentName)
}