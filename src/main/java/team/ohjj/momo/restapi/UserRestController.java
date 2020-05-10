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
        Optional<User> loginUser = userRepository.findByEmailAndPasswordAndType(user.getEmail(), user.getPassword(), user.getType());

        return loginUser.isPresent() ? loginUser.get().getNo() : 0;
    }

    @GetMapping("/{id}")
    public User getUserInfo(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);

        return user.isPresent() ? user.get() : null;
    }

    @GetMapping("/check/email")
    public Boolean checkEmail(@RequestParam String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    @GetMapping("/check/nickname")
    public Boolean checkNickname(@RequestParam String nickname) {
        return !userRepository.findByNickname(nickname).isPresent();
    }

    @PutMapping("/insert")
    public Integer createUser(@ModelAttribute User user) {
        User insertedUser = userRepository.save(user);

        return insertedUser == null ? 0 : insertedUser.getNo();
    }

    @PutMapping("/update")
    public Integer updateUser(@ModelAttribute User user) {
        User updatedUser = userRepository.save(user);

        return updatedUser == null ? 0 : updatedUser.getNo();
    }

    @DeleteMapping("/delete/{no}")
    public Integer deleteUser(@PathVariable Integer no) {
        Integer result = 0;

        try {
            userRepository.deleteById(no);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }
}
