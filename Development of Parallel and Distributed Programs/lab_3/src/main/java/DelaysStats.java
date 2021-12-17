import java.io.Serializable;

public class DelaysStats implements Serializable {
    private float delayedFlightsNumber;
    private float cancelledFlightNumber;
    private float maxDelay;
    private final int flightsNumber;

    public DelaysStats() {

    }
}
