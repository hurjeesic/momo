package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;
import team.ohjj.momo.mail.MailHandler;
import team.ohjj.momo.mail.TempKey;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender sender;

    private final int minute = 60;

    @PostMapping("/login")
    public Integer login(HttpSession session, @ModelAttribute User user) {
        user = userRepository.findByEmailAndPasswordAndType(user.getEmail(), user.getPassword(), user.getType()).get();

        session.setMaxInactiveInterval(10 * minute);
        session.setAttribute("user", user);

        return user.getNo();
    }

    @GetMapping("/")
    public User getUserInfo(HttpSession session) {
        return (User)session.getAttribute("user");
    }

    @PostMapping("/insert")
    public Integer createUser(HttpSession session, @ModelAttribute User user) {
        if ((Boolean)session.getAttribute("authorization")) {
            session.removeAttribute("authorization code");
            session.removeAttribute("authorization");

            return userRepository.save(user).getNo();
        }

        return null;
    }

    @GetMapping("/check/email")
    public Boolean checkEmail(@RequestParam String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    @GetMapping("/check/nickname")
    public Boolean checkNickname(@RequestParam String nickname) {
        return !userRepository.findByNickname(nickname).isPresent();
    }

    @GetMapping("/confirm/email")
    public void getConfirmCode(HttpSession session, @RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        MailHandler mailHandler = new MailHandler(sender);

        mailHandler.setFrom("jshur2015108211@gmail.com", "모모게시판");
        mailHandler.setTo(email);

        session.setMaxInactiveInterval(5 * minute);
        session.setAttribute("authorization code", TempKey.getInstance().getKey(5, false));
        session.setAttribute("authorization", false);
        mailHandler.setSubject("이메일 인증 코드");
        mailHandler.setText(new StringBuffer()
                .append("모모게시판 회원가입을 위한 이메일 인증 코드 메일입니다.<br>")
                .append("인증 코드 : <b>" + session.getAttribute("authorization code") + "</b>")
                .toString()
        );

        mailHandler.send();
    }

    @GetMapping("/confirm/email/check")
    public Boolean checkCode(HttpSession session, @RequestParam String code) {
        if (session.getAttribute("authorization code").equals(code)) {
            session.setAttribute("authorization", true);
            return true;
        }

        return false;
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
