package domain.model;

import java.util.ArrayList;

public class Root {
    private float resultCount;
    ArrayList<Result> results = new ArrayList<Result>();


    // Getter Methods

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public float getResultCount() {
        return resultCount;
    }

    // Setter Methods

    public void setResultCount( float resultCount ) {
        this.resultCount = resultCount;
    }
}