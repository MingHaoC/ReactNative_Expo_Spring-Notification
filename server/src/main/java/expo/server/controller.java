package expo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Calendar.getInstance;

@RestController
public class controller {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println(user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("User already exists with that email", HttpStatus.CONFLICT);
        }
        userRepository.save(user);
        return new ResponseEntity<>("User successfully register", HttpStatus.OK);
    }

    @GetMapping("/user")
    public HttpEntity<?> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        return new ResponseEntity<>("user not found with that email", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/expoToken/register")
    public ResponseEntity<String> registerExpoToken(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            return new ResponseEntity<>("User doesn't exists", HttpStatus.NOT_FOUND);
        }
        if (userRepository.updateExpoToken(user.getEmail(), user.getExpoToken()) == 1)
            return new ResponseEntity<>("Successfully added token", HttpStatus.OK);
        return new ResponseEntity<>("Error, could not add the expo token", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/test")
    public List<IEvent> test() {
        Date now = new Date();
        Calendar calendar = getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, 5);

        return eventRepository.findAllByTimeRange(now, calendar.getTime());
    }
}
