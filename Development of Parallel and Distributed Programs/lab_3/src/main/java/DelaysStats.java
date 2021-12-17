import java.io.Serializable;

public class DelaysStats implements Serializable {
    private float delayedFlightsCount;
    private float cancelledFlightCount;
    private float maxDelay;
    private final int flightsNumber;

    public DelaysStats(float maxDelay, int flightsNumber, float delayedFlightsCount, float cancelledFlightCount) {
        this.delayedFlightsCount = delayedFlightsCount;
        this.cancelledFlightCount = cancelledFlightCount;
        this.maxDelay = maxDelay;
        this.flightsNumber = flightsNumber;
    }

    
}
