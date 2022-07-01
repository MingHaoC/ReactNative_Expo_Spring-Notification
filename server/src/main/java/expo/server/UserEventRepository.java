package expo.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserEventRepository extends JpaRepository<UserEvent, UserEventKey> {

    @Query(value = "SELECT u.expoToken as expoToken, u.firstName as firstName, u.lastName as lastName FROM UserEvent ue LEFT JOIN User u ON ue.user.id = u.id WHERE ue.event.eventId = :eventID AND u.expoToken IS NOT null")
    List<IUser> getUserRegisterInEventWithExpoToken(@Param("eventID") Integer eventId);


}
