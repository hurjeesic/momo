package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public Integer login(@ModelAttribute User user) {
        User loginUser = checkUser(userRepository.findByEmailAndPasswordAndType(user.getEmail(), user.getPassword(), user.getType()));

        return loginUser == null ? 0 : loginUser.getNo();
    }

    @GetMapping("/{id}")
    public User getUserInfo(@PathVariable Integer id) {
        return checkUser(userRepository.findById(id));
    }

    @GetMapping("/check/email")
    public Boolean checkEmail(@RequestParam String email) {
        return checkUser(userRepository.findByEmail(email)) == null;
    }

    @GetMapping("/check/nickname")
    public Boolean checkNickname(@RequestParam String nickname) {
        return checkUser(userRepository.findByNickname(nickname)) == null;
    }

    @PutMapping("/insert")
    public Integer createUser(@ModelAttribute User user) {
        User insertedUser = userRepository.save(user);

        return insertedUser == null ? 0 : insertedUser.getNo();
    }

    private User checkUser(Optional<User> user) {
        User checkedUser = null;

        try {
            checkedUser = user.get();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return checkedUser;
    }
}
