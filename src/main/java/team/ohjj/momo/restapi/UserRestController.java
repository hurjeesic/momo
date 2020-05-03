package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public Integer login(@RequestParam Map<String, Object> param) {
        Integer id = 0;

        try {
            String email = (String) param.get("email");
            String password = (String) param.get("password");
            Byte type = Byte.parseByte((String) param.get("type"));

            id = userRepository.findByEmailAndPasswordAndType(email, password, type).getNo();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return id;
    }

    @GetMapping("/{id}")
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
