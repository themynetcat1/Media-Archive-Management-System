import java.util.LinkedList;

public class MediaItem {
    private String ID;
    private String url;//ID is key so it is not here
    private String title;
    private String type;
    private String genre;
    private String realeaseYear;
    private String platform;
    private String availableCountries;
    private String rating;
    private String  voteCount;
    private LinkedList<String> platforms=new LinkedList();

    public MediaItem(){

    }

    public MediaItem(String url, String title, String type, String genre,
                     String realeaseYear, String ID, String  rating, String  voteCount,
                     String platform, String availableCountries) {
        this.platform=platform;
        this.availableCountries=availableCountries;
        this.ID = ID;
        this.url = url;
        this.title = title;
        this.type = type;
        this.genre = genre;
        this.realeaseYear = realeaseYear;
        this.rating = rating;
        this.voteCount = voteCount;
        platforms.add(platform+"|"+availableCountries);
    }
    public String getPlatform(){
        return platform;
    }
    public String getAvailableCountries(){
        return availableCountries;
    }
    public void addPlatform(String platform, String availableCountries){
        platforms.add(platform+"|"+availableCountries);
    }

    public String getID() {
        return ID;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getGenre() {
        return genre;
    }

    public String getRealeaseYear() {
        return realeaseYear;
    }

    public String getRating() {
        return rating;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public LinkedList<String> getPlatforms() {
        return platforms;
    }

    public String getImdbID() {
        return ID;
    }
}