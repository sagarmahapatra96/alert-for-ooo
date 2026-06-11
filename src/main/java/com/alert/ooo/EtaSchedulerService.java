package com.alert.ooo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Service
public class EtaSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(EtaSchedulerService.class);
    private final RestTemplate restTemplate;

    public EtaSchedulerService() {
        this.restTemplate = new RestTemplate();
    }

    // Run every minute (60000 ms)
    @Scheduled(fixedRate = 60000)
    public void checkEta() {
        // San Francisco to Los Angeles coordinates
        String startLon = "-122.4194";
        String startLat = "37.7749";
        String endLon = "-118.2437";
        String endLat = "34.0522";

        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=false",
                startLon, startLat, endLon, endLat);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "Ok".equals(response.get("code"))) {
                List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
                if (routes != null && !routes.isEmpty()) {
                    Map<String, Object> route = routes.get(0);
                    Double durationInSeconds = (Double) route.get("duration");
                    Double distanceInMeters = (Double) route.get("distance");

                    double durationInMinutes = durationInSeconds / 60.0;
                    double distanceInKm = distanceInMeters / 1000.0;

                    logger.info(String.format("ETA from SF to LA: %.2f minutes. Distance: %.2f km", durationInMinutes, distanceInKm));
                }
            } else {
                logger.warn("Failed to get a valid response from OSRM API. Response: {}", response);
            }
        } catch (Exception e) {
            logger.error("Error fetching ETA from OSRM API", e);
        }
    }
}
