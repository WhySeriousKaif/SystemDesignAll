package scaler.concept.observer;

import java.util.*;

/**
 * Observer Design Pattern Demonstration
 * Concept: IPL Match Score Update System
 */

// 1. Observer Interface (The Subscriber Contract)
interface Observer {
    // This method is called by the Subject to notify the Observer of a change
    void update(String matchStatus);
}

// 2. Subject Interface (The Publisher Contract)
interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// 3. Concrete Subject (The real source of data)
class IPLMatch implements Subject {
    // List to maintain all registered observers
    private List<Observer> viewers = new ArrayList<>();
    private String matchStatus;

    @Override
    public void addObserver(Observer observer) {
        viewers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        viewers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // Broadcast the update to every registered observer
        for (Observer observer : viewers) {
            observer.update(matchStatus);
        }
    }

    /**
     * Important method to update score.
     * When this state changes, notifyObservers() is triggered.
     */
    public void updateMatchScore(String matchStatus) {
        this.matchStatus = matchStatus;
        notifyObservers(); // 🔥 Trigger the broadcast
    }
}

// 4. Concrete Observer - TV Display
class TVDisplay implements Observer {
    private String viewerName;

    public TVDisplay(String viewerName) {
        this.viewerName = viewerName;
    }

    @Override
    public void update(String matchStatus) {
        System.out.println(viewerName + " on TV: Match Update - " + matchStatus);
    }
}

// 5. Concrete Observer - Mobile App
class MobileApp implements Observer {
    private String appName;

    public MobileApp(String appName) {
        this.appName = appName;
    }

    @Override
    public void update(String matchStatus) {
        System.out.println(appName + " on Mobile App: Match Update - " + matchStatus);
    }
}

// 6. Concrete Observer - Google Search
class GoogleSearch implements Observer {
    @Override
    public void update(String matchStatus) {
        System.out.println("Google Search: Match Update - " + matchStatus);
    }
}

// Main Execution Class
public class ObserverPatternDemo {
    public static void main(String[] args) {

        // 1. Create the Subject (Source of Truth)
        IPLMatch match = new IPLMatch();

        // 2. Create the Observers (Subscribers)
        Observer tvViewer = new TVDisplay("Star Sports");
        Observer mobileApp = new MobileApp("JioCinema");
        Observer google = new GoogleSearch();

        // 3. Register (Subscribe) the observers
        match.addObserver(tvViewer);
        match.addObserver(mobileApp);
        match.addObserver(google);

        // 4. First update - everyone gets it
        System.out.println("--- First Match Update ---");
        match.updateMatchScore("CSK: 150/3 in 18 overs");

        // 5. Second update
        System.out.println("\n--- Second Match Update ---");
        match.updateMatchScore("CSK: 180/4 in 20 overs");

        // 6. Remove one observer (Unsubscribe)
        match.removeObserver(google);

        // 7. Third update (Google will NOT receive this)
        System.out.println("\n--- Third Match Update (Google removed) ---");
        match.updateMatchScore("CSK: 200/5 in 20 overs");
    }
}
