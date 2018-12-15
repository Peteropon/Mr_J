package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import parsers.MovieInfo;
import parsers.Parsable;
import parsers.ServiceParser;

import java.util.*;

public class Program {

    Scanner sc = new Scanner(System.in);

    //List<Parsable> services;
    private Map<String, ServiceParser> services;

    public static ObservableList<MovieInfo> getHits() {
        return hits;
    }


    private static ObservableList<MovieInfo> hits;

    public Program() {
        //this.services = new ArrayList<Parsable>();
        this.services = new HashMap<>();
        hits = FXCollections.observableArrayList();
    }

    public Map<String, ServiceParser> getServices() {
        return services;
    }

    public void startLogin(){
        System.out.println("Starting login");
        for (ServiceParser service : services.values()) {
            if(service.getAccount().getUserName() != null && service.getAccount().getPassword() != null) {
                System.out.println("Username and password found");
                if (service.getCookieHandler().isExpired()) {
                    System.out.println("Logging in");
                    Thread thread = new Thread(()-> service.login());
                    thread.start();
                }
            } else {
                System.out.println("No username found");
            }
        }
    }
    public void startSearch(String searchText){

        System.out.println("Starting search");
        int hitCount;

        Platform.runLater(()->hits.clear());
        hitCount = 0;
//            System.out.println("Enter movie title to search for:");
//            String movieTitle = sc.nextLine();
//            if (movieTitle.equalsIgnoreCase("quit"))
//                System.exit(0);
        for (ServiceParser service : services.values()) {
            System.out.println("Starting thread");
            Thread thread = new Thread(() -> service.parse(searchText));
            thread.start();  // thread instansiation maybe here
        }
        //main thread sleeps to make sure results populate list before printout
        //here we could use listener on 'List<MovieInfo>hits' to update javafx element without having to use sleep
//        try {
//            Thread.sleep(10000);
//        }catch(InterruptedException e){
//
//        }
//        for (MovieInfo movie: hits) {
//            if(movie != null){
//                System.out.println(movie.getTitle() +" : " + movie.getUrl() + " : " + movie.getSource());
//                hitCount ++;
//            }
//        }
//        if (hitCount == 0){
//            System.out.println("Sorry, " + searchText + " was not found on any streaming platform");
//        }

    }

    public void addService(String name, ServiceParser service){
        if(service != null && name != null){
            //services.add(service);
            services.put(name, service);
        }
    }

    public static void addHit(MovieInfo movieInfo){
        synchronized (hits){
            Platform.runLater(()->hits.add(movieInfo));
            //hits.add(movieInfo);
        }
    }


}
