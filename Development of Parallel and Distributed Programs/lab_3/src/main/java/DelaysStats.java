import java.io.Serializable;

public class DelaysStats implements Serializable {
    private int delayedFlightsCount;
    private int cancelledFlightCount;
    private float maxDelay;
    private final int flightsNumber;

    public DelaysStats(float maxDelay, int flightsNumber, int delayedFlightsCount, int cancelledFlightCount) {
        this.delayedFlightsCount = delayedFlightsCount;
        this.cancelledFlightCount = cancelledFlightCount;
        this.maxDelay = maxDelay;
        this.flightsNumber = flightsNumber;
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

    public DelaysStats(FlightsDelay flightsDelay) {
        this.delayedFlightsCount = 0;
        this.cancelledFlightCount = 0;
        this.maxDelay = 0.0f;
        this.flightsNumber = 1;
        update(flightsDelay);
    }

    public static DelaysStats addDelay(DelaysStats delaysStats, FlightsDelay flightsDelay) {
        delaysStats.update(flightsDelay);
        return new DelaysStats(
                delaysStats.getMaxDelay(),
                delaysStats.getFlightsNumber() + 1,
                delaysStats.getDelayedFlightsCount(),
                delaysStats.getCancelledFlightsCount()
        );
    }

    public static DelaysStats add(DelaysStats a, DelaysStats b) {
        return new DelaysStats(
                Math.max(a.getMaxDelay(), b.getMaxDelay()),
                a.getFlightsNumber() + b.getFlightsNumber(),
                a.getDelayedFlightsCount() + b.getDelayedFlightsCount(),
                a.getCancelledFlightsCount() + b.getCancelledFlightsCount()
        );
    }

    public float getMaxDelay() {
        return this.maxDelay;
    }

    public int getFlightsNumber() {
        return this.flightsNumber;
    }

    public int getDelayedFlightsCount() {
        return this.delayedFlightsCount;
    }

    public int getCancelledFlightsCount() {
        return this.cancelledFlightCount;
    }
}
