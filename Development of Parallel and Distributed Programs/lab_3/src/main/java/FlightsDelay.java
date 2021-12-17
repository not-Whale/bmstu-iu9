import java.io.Serializable;

public class FlightsDelay implements Serializable {
    private float delayedFlightsNumber;
    private float cancelledFlightNumber;
    private float maxDelay;
    private final int flightsNumber;

    public FlightsDelay() {
    }
}
