package com.example.myapplication;

import com.google.firebase.firestore.DocumentSnapshot;

public class DocForRating implements Comparable<DocForRating> {

    private DocumentSnapshot documentSnapshot;
    private float rating;

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public int compareTo(DocForRating docForRating) {

        float compare_rating = docForRating.getRating();

        if (this.rating < compare_rating)
        {
            return 1;
        }
        else if (this.rating > compare_rating)
        {
            return -1;
        }

        return 0;
    }
}
