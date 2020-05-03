package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{id}")
    public User getUserInfo(@PathVariable Integer id) {
        User user = null;

        try {
            user = userRepository.findById(id).get();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }
}
