package com.example.myapplication;

import com.google.firebase.firestore.DocumentSnapshot;

public class DocForDistance implements Comparable<DocForDistance> {

    private DocumentSnapshot documentSnapshot;
    private double distance;

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(DocForDistance docForDistance) {

        double compare_distance = docForDistance.getDistance();

        if (this.distance < compare_distance)
        {
            return -1;
        }
        else if (this.distance > compare_distance)
        {
            return 1;
        }

        return 0;
    }
}
