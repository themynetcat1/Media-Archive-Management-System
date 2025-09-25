import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void findPlatform(DictionaryInterface<String,MediaItem> hashedDictionary){
        Iterator<String> keyIterator =hashedDictionary.getKeyIterator();
        MediaItem temp = new MediaItem();
        while (keyIterator.hasNext()){
            temp = hashedDictionary.get(keyIterator.next());
            LinkedList platforms = temp.getPlatforms();
            if(platforms.size() == 5){
                writeFoundEntry(temp);
            }
        }
    }
    public static void findcountry(String country,DictionaryInterface<String ,MediaItem> hashedDictionary){
        Iterator<String> keyIterator =hashedDictionary.getKeyIterator();
        MediaItem temp = new MediaItem();
        while (keyIterator.hasNext()){
            temp = hashedDictionary.get(keyIterator.next());
            if(temp.getAvailableCountries().toUpperCase().contains(country)){
                writeFoundEntry(temp);
            }
        }
    }
    public static void writeFoundEntry(MediaItem writeValue){
        if(writeValue != null){
            LinkedList platforms = writeValue.getPlatforms();

            System.out.println("Type : "+writeValue.getType());
            System.out.println("Genre : "+writeValue.getGenre());
            System.out.println("Release Year: "+writeValue.getRealeaseYear());
            System.out.println("IMDb ID: "+writeValue.getImdbID());
            System.out.println("Rating: "+writeValue.getRating());
            System.out.println("Number of Votes: "+writeValue.getVoteCount());
            System.out.println();
            System.out.println(platforms.size()+" platforms found for "+writeValue.getTitle());
            System.out.println();
            for(int i = 0; i < platforms.size(); i++){
                String damn = (String) platforms.get(i);
                String[] splited = damn.split("\\|");
                System.out.print(splited[0]+" - ");
                System.out.println(splited[1]);
            }
            System.out.println("---------------------------------------");
        }
    }

    public static void findMax(DictionaryInterface<String ,MediaItem> hashedDictionary) throws FileNotFoundException {
        Iterator<String> keyIterator =hashedDictionary.getKeyIterator();
        MediaItem[] m = new MediaItem[10];
        MediaItem temp = new MediaItem();
        double max = 0;
        int count = 0;
        while (keyIterator.hasNext()){
            temp = hashedDictionary.get(keyIterator.next());
            if((temp.getRating() != "")){
                Double rate = Double.parseDouble(temp.getRating());
                if(max < rate){
                    max = rate;
                    m[0] = temp;
                }
            }
        }
        double prev = max;
        MediaItem last = m[count];
        while (count<10){
            max = 0;
            Iterator<String> key =hashedDictionary.getKeyIterator();
            while (key.hasNext()){
                temp = hashedDictionary.get(key.next());

                if((temp.getRating() != "")) {
                    boolean isExist = false;
                    Double rate = Double.parseDouble(temp.getRating());
                    if (rate > max && rate <= prev) {
                        for(int i = 0;i < count + 1;i++){
                            if(temp == m[i]){
                                isExist = true;
                            }
                        }
                        if(!isExist){
                            max = rate;
                            m[count] = temp;
                        }
                    }
                }
            }
            prev = max;
            last = m[count];
            count++;

        }
        System.out.println("Here is the top 10 media : ");
        for(int i = 0;i <10;i++){
            System.out.println((i+1) + ". "+ m[i].getTitle());
        }

    }
    public static void search(DictionaryInterface hashedDictionary) throws FileNotFoundException {
        File filee = new File("search.txt");
        Scanner scann = new Scanner(filee);
        long startTime = System.nanoTime();
        while(scann.hasNext()){
            writeFoundEntry(hashedDictionary.get(scann.next()));
        }
        long endTime = System.nanoTime();
        long avgTime = (endTime - startTime) / 1000;
        System.out.println("Avarage Time for search: "+avgTime);
    }
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sa = new Scanner(System.in);
        MediaItem mediaItem = new MediaItem();
        int choice = 0;

        boolean menu = true;
        DictionaryInterface<String,MediaItem> hashedDictionary = new HashedDictionary<String,MediaItem>();
        boolean loaded = false;

        while (menu){
            System.out.println("-----------");
            System.out.println("1.Load dataset");
            System.out.println("2.Run 1000 search test");
            System.out.println("3.Search for a media item with the ImdbId");
            System.out.println("4.List the top 10 media according to user votes");
            System.out.println("5.List all the media streams in a given country");
            System.out.println("6.List the media items that are streaming on all 5 platforms");
            System.out.println("7.Exit");
            System.out.println("-----------");
            System.out.print("choice :");
            String s = "";
            while (true){
                choice = sa.nextInt();
                System.out.println();
                if(choice>7 || choice<1){
                    System.out.println("Enter number between 7 and 1");
                }else{
                    break;
                }
            }
            System.out.println("-----------");
            if(choice == 1){
                if(!loaded){
                    hashedDictionary = load();
                    loaded = true;
                }else{
                    System.out.println("dataset already loaded");
                }
            } else if (choice == 2&&loaded) {
                search(hashedDictionary);
            } else if (choice == 3&&loaded) {

                System.out.print("Enter your search no :");
                Scanner input = new Scanner(System.in);
                String jil = input.nextLine();
                writeFoundEntry(hashedDictionary.get(jil));

            } else if (choice == 4 && loaded) {
                findMax(hashedDictionary);
            } else if (choice == 5&& loaded) {
                System.out.print("Enter your search country :");
                Scanner input = new Scanner(System.in);
                String country = input.nextLine().toUpperCase();
                findcountry(country,hashedDictionary);
            } else if (choice == 6 && loaded) {
                System.out.println("There are medias available on 5 platforms: ");
                findPlatform(hashedDictionary);

            }else if (choice == 7){
                menu = false;
            }
            else{
                System.out.println("Please first load data's ");
            }


        }
    }
    public static DictionaryInterface load() throws FileNotFoundException {
        File file=new File("movies_dataset.csv");
        Scanner scanner=new Scanner(file);
        scanner.nextLine();
        DictionaryInterface hashedDictionary = new HashedDictionary();
        long startAdd = System.nanoTime();
        while (scanner.hasNextLine()) {
            String url = null;
            String title = null;
            String type = null;
            String genre = null;
            String releaseYear = null;
            String imdbID = null;
            String rating = null;
            String voteCount = null;
            String platform = null;
            String availableCountries = null;
            String[] sep = new String[10];
            String[] sep2 = new String[10];
            String[] sep3 = new String[10];
            String[] sep4 = new String[10];
            String line = scanner.nextLine();
            line = line.replace("\"\"", "");
            sep = line.split("\"");
            if (sep.length == 3) {
                if(sep[0].equals(",")){
                    url = sep[0];
                    title = sep[1];
                    sep3 = sep[2].split(",");
                    type = sep3[1];
                    genre = sep3[2];
                    releaseYear = sep3[3];
                    imdbID = sep3[4];
                    rating= sep3[5];
                    voteCount = sep3[6];
                    platform = sep3[7];
                    availableCountries = sep3[8];
                }else{
                    sep2 = sep[0].split(",");
                    url = sep2[0];

                    if(sep2.length == 1){
                        title = sep[1];
                        sep3 = sep[2].split(",");
                        type = sep3[1];
                        genre = sep3[2];
                        releaseYear = sep3[3];
                        imdbID = sep3[4];
                        rating= sep3[5];
                        voteCount = sep3[6];
                        platform = sep3[7];
                        availableCountries = sep3[8];
                    }else{
                        title = sep2[1];
                        type = sep2[2];
                        genre = sep[1];
                        sep3 = sep[2].split(",");
                        imdbID = sep3[2];
                        releaseYear = sep3[1];
                        rating = sep3[3];
                        voteCount = sep3[4];
                        platform = sep3[5];
                        availableCountries = sep3[6];
                    }
                }
            } else if (sep.length == 4) {
                if(sep[0].equals(",")){
                    url = sep[0];
                    title = sep[1];
                    sep3 = sep[2].split(",");
                    type = sep3[1];
                    genre = sep3[2];
                    releaseYear = sep3[3];
                    imdbID = sep3[4];
                    rating= sep3[5];
                    voteCount = sep3[6];
                    platform = sep3[7];
                }else {
                    sep2 = sep[0].split(",");
                    url = sep2[0];

                    if(sep2.length == 1){

                        title = sep[1];
                        sep3 = sep[2].split(",");
                        type = sep3[1];
                        genre = sep3[2];
                        releaseYear = sep3[3];
                        imdbID = sep3[4];
                        rating= sep3[5];
                        voteCount = sep3[6];
                        platform = sep3[7];
                    } else{
                        title = sep2[1];
                        type = sep2[2];
                        genre = sep[1];
                        sep3 = sep[2].split(",");
                        imdbID = sep3[2];
                        releaseYear = sep3[1];
                        rating = sep3[3];
                        voteCount = sep3[4];
                        platform = sep3[5];
                    }
                }
                availableCountries = sep[3];

            } else if (sep.length == 5) {
                url = sep[0];
                title = sep[1];
                type = sep[2];
                genre = sep[3];
                sep2 = sep[4].split(",");
                releaseYear = sep2[1];
                imdbID = sep2[2];
                rating = sep2[3];
                voteCount = sep2[4];
                platform = sep2[5];
                availableCountries = sep2[6];
            } else if( sep.length == 2){
                sep2 = sep[0].split(",");
                url = sep2[0];
                title = sep2[1];
                type = sep2[2];
                genre = sep2[3];
                releaseYear = sep2[4];
                imdbID = sep2[5];
                rating = sep2[6];
                voteCount = sep2[7];
                platform = sep2[8];
                availableCountries = sep[1];
            }else if(sep.length == 1){
                sep2 = sep[0].split(",");
                url = sep2[0];
                title = sep2[1];
                type = sep2[2];
                genre = sep2[3];
                releaseYear = sep2[4];
                imdbID = sep2[5];
                rating = sep2[6];
                voteCount = sep2[7];
                platform = sep2[8];
                availableCountries = sep2[9];
            } else if (sep.length == 6) {
                url = sep[0];
                title = sep[1];
                sep3 = sep[2].split(",");
                type = sep3[1];
                genre = sep[3];
                sep4 = sep[4].split(",");
                releaseYear = sep4[1];
                imdbID = sep4[2];
                rating = sep4[3];
                voteCount = sep4[4];
                platform = sep4[5];
                availableCountries = sep[5];
            }else{
                throw new IllegalStateException("There is a problem with splitting.");
            }
            MediaItem mediaItem = new MediaItem(url,title,type,genre,releaseYear,imdbID,rating,voteCount,platform,availableCountries);
            hashedDictionary.add(imdbID,mediaItem);
        }
        long endAdd = System.nanoTime();
        System.out.println("Time for Adding : " + (endAdd - startAdd));

        System.out.println("finish");
        return hashedDictionary;
    }

}