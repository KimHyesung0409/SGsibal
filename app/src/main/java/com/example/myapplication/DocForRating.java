package com.example.myapplication;

import com.google.firebase.firestore.DocumentSnapshot;

public class DocForRating implements Comparable<DocForRating> {

    /*
     * GeoFireUtil.getGeoHashQueryBounds에서 지역쿼리 후 평점순으로
     * 정렬하기 위해 정의한 클래스로 Comparable 인터페이스를 상속받고 compareTo를 재정의한다.
     * 이때 float rating 를 사용하여 정렬하게 된다.
     */

    private DocumentSnapshot documentSnapshot;
    private float rating;

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    // setter getter
    public void setRating(float rating) {
        this.rating = rating;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public float getRating() {
        return rating;
    }

    // compareTo 재정의.
    // rating를 비교 대상으로 설정.
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
