import scala.Tuple2;

import java.util.Map;

public class AirportsFlightsStats extends DelaysStats {
    private final String departureAirportDescription;
    private final String destinationAirportDescription;

    public AirportsFlightsStats(Tuple2<String, String> airportDescriptions,
                                DelaysStats delaysStats, Map<String, String> airportDescription) {
        super(
                delaysStats.getMaxDelay(),
                delaysStats.getFlightsNumber(),
                delaysStats.getDelayedFlightsCount(),
                delaysStats.getCancelledFlightsCount()
        );

        this.departureAirportDescription = airportDescription.get(airportDescriptions._1());
        this.destinationAirportDescription = airportDescription.get(airportDescriptions._2());
    }
}
