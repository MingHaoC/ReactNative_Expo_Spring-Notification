package expo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.util.Calendar.getInstance;


@Component
@EnableAsync
public class scheduler {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserEventRepository userEventRepository;

    @Scheduled(fixedRate = 600000)
    public void reportCurrentTime() {
        Date now = new Date();
        Calendar calendar = getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 2);
        Date toDate = calendar.getTime();

        // System.out.println(now + " " + toDate);
        eventRepository.findAllByTimeRange(now, toDate).forEach(event -> {
            // System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            // System.out.println("User who register for the following event:\ntitle: " + event.getEventTitle() + "\nDescription: " + event.getEventDescription() + "\nLocation: " + event.getLocation() + "\n");
            userEventRepository.getUserRegisterInEventWithExpoToken(event.getEventId()).forEach(user -> {
                sendNotification(user.getExpoToken(), event.getEventTitle(), event.getEventDescription());
                System.out.println("Name: " + user.getFirstName() + " " + user.getLastName() + "\nExpoToken: " + user.getExpoToken() + "\n");
            });
            // System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        });
    }


    private void sendNotification(String expoToken, String title, String description) {
        var values = new HashMap<String, String>() {{
            put("to", expoToken);
            put ("title", title);
            put("body", description);
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(values);

            System.out.println(requestBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://exp.host/--/api/v2/push/send"))
                    .headers("Content-Type", "text/plain;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
