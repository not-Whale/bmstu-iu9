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

    public DelaysStats(FlightsDelay flightsDelay) {
        this.delayedFlightsCount = 0;
        this.cancelledFlightCount = 0;
        this.maxDelay = 0.0f;
        this.flightsNumber = 1;
        update(flightsDelay);
    }

    public DelaysStats addDelay(DelaysStats delaysStats, FlightsDelay flightsDelay) {
        update(flightsDelay);
        
    }

    public static DelaysStats add(DelaysStats a, DelaysStats b) {
        return new DelaysStats(
                Math.max(a.getMaxDelay(), b.getMaxDelay()),
                a.getFlightsNumber() + b.getFlightsNumber(),
                a.getDelayedFlightsCount() + b.getDelayedFlightsCount(),
                a.getCancelledFlightsCount() + b.getCancelledFlightsCount()
        );
    }

    private void update(FlightsDelay flightsDelay) {
        if (flightsDelay.getCancelledStatus()) {
            this.cancelledFlightCount++;
        } else {
            float delay = flightsDelay.getDelay();
            if (delay > 0.0f) {
                this.delayedFlightsCount++;
                this.maxDelay = Math.max(delay, this.maxDelay);
            }
        }
    }

    public float getMaxDelay() { return this.maxDelay; }

    public int getFlightsNumber() { return this.flightsNumber; }

    public float getDelayedFlightsCount() { return this.delayedFlightsCount; }

    public float getCancelledFlightsCount() { return this.cancelledFlightCount; }
}
