package expo.server;

import java.util.Date;

public interface IEvent {
    Integer getEventId();
    String getEventTitle();
    String getEventDescription();
    Date getTimes();
    String getLocation();
}

