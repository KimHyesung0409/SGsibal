package com.example.myapplication;

import com.google.firebase.firestore.DocumentSnapshot;

public class DocForDistance implements Comparable<DocForDistance> {

    /*
     * GeoFireUtil.getGeoHashQueryBounds에서 지역쿼리 후 평점순으로
     * 정렬하기 위해 정의한 클래스로 Comparable 인터페이스를 상속받고 compareTo를 재정의한다.
     * 이때 double distance 를 사용하여 정렬하게 된다.
     */

    private DocumentSnapshot documentSnapshot;
    private double distance;

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    // setter getter
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public double getDistance() {
        return distance;
    }

    // compareTo 재정의.
    // distance를 비교 대상으로 설정.
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
