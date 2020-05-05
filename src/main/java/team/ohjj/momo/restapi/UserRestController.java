package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;
import team.ohjj.momo.mail.MailHandler;
import team.ohjj.momo.mail.TempKey;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.stream.DoubleStream;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender sender;

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

    @GetMapping("/confirm/email")
    public String getConfirmCode(@RequestParam String email) {
        String authKey = null;

        try {
            authKey = TempKey.getInstance().getKey(5, false);
            MailHandler mailHandler = new MailHandler(sender);

            mailHandler.setFrom("jshur2015108211@gmail.com", "모모게시판");
            mailHandler.setTo(email);

            mailHandler.setSubject("이메일 인증 코드");
            mailHandler.setText(new StringBuffer()
                .append("모모게시판 회원가입을 위한 이메일 인증 코드 메일입니다.<br>")
                .append("인증 코드 : <b>"+ authKey + "</b>")
                .toString()
            );

            mailHandler.send();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return authKey;
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
