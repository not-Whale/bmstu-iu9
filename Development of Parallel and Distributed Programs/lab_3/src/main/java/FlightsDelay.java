import java.io.Serializable;

public class FlightsDelay implements Serializable {
    private static final int DEPARTURE_AIRPORT = 11;
    private static final int DESTINATION_AIRPORT = 14;
    private static final int DALEY_DURATION = 18;
    private static final int CANCELLED_STATUS = 19;

    private boolean cancelled;
    private float delay;

    public FlightsDelay(String[] flightData) {
        
    }
}
