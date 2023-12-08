package domain.model;

public class Result {
    private int collectionId;
    private int trackId;
    private String artistName = "";
    private String trackName = "";
    private String previewUrl = "";
    private String artworkUrl100 = "";


    // Getter Methods

    public String getLocalPreviewFilename(){
        return getTrackId() + ".m4a.tmp";
    }

    public String getLocalArtworkFilename(){
        return getCollectionId() + ".jpg.tmp";
    }

    public int getCollectionId() {
        return collectionId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }


    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }
}