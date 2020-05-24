package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserJpaRepository;
import team.ohjj.momo.mail.MailHandler;
import team.ohjj.momo.mail.TempKey;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {
    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    JavaMailSender sender;

    @PostMapping("/login")
    public Integer login(@ModelAttribute User user) {
        Optional<User> loginUser = userJpaRepository.findByEmailAndPasswordAndType(user.getEmail(), user.getPassword(), user.getType());

        return loginUser.isPresent() ? loginUser.get().getNo() : 0;
    }

    @GetMapping("/{id}")
    public User getUserInfo(@PathVariable Integer id) {
        Optional<User> user = userJpaRepository.findById(id);

        return user.isPresent() ? user.get() : null;
    }

    @PutMapping("/insert")
    public Integer createUser(@ModelAttribute User user) {
        User insertedUser = userJpaRepository.save(user);

        return insertedUser == null ? 0 : insertedUser.getNo();
    }

    @GetMapping("/check/email")
    public Boolean checkEmail(@RequestParam String email) {
        return !userJpaRepository.findByEmail(email).isPresent();
    }

    @GetMapping("/check/nickname")
    public Boolean checkNickname(@RequestParam String nickname) {
        return !userJpaRepository.findByNickname(nickname).isPresent();
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

    @PutMapping("/update")
    public Integer updateUser(@ModelAttribute User user) {
        User updatedUser = userJpaRepository.save(user);

        return updatedUser == null ? 0 : updatedUser.getNo();
    }

    @DeleteMapping("/delete/{no}")
    public Integer deleteUser(@PathVariable Integer no) {
        Integer result = 0;

        try {
            userJpaRepository.deleteById(no);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }
}
