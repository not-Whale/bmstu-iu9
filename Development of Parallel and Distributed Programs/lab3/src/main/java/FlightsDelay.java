import scala.Tuple2;

import java.io.Serializable;

public class FlightsDelay implements Serializable {
    private static final int DEPARTURE_AIRPORT = 11;
    private static final int DESTINATION_AIRPORT = 14;
    private static final int DELAY_DURATION = 18;
    private static final int CANCELLED_STATUS = 19;

    private final boolean cancelled;
    private float delay;

    public FlightsDelay(String[] flightData) {
        String cancelledStatusString = flightData[CANCELLED_STATUS];
        float cancelledStatus = Float.parseFloat(cancelledStatusString);

        if (cancelledStatus < 1.0 && flightData[DELAY_DURATION].length() != 0) {
            this.cancelled = false;
            this.delay = Float.parseFloat(flightData[DELAY_DURATION]);
        } else {
            this.cancelled = true;
        }
    }

    public boolean getCancelledStatus() {
        return this.cancelled;
    }

    public float getDelay() {
        return this.delay;
    }

    public static Tuple2<String, String> getAirportsPair(String[] flightData) {
        return new Tuple2<>(
                flightData[DEPARTURE_AIRPORT],
                flightData[DESTINATION_AIRPORT]
        );
    }
}
