package team.ohjj.momo;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.UserRepository;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class UserTests {
    @Autowired
    UserRepository userRepository;
    private Integer id = 1;
    private String email = "test1@gmail.com";
    private String password = "test1";
    private String nickname = "test1";
    private String phone = "010-1234-5678";
    private Byte type = 2;

    @Test
    public void getUser() {
        User user = userRepository.findById(id).get();

        assertThat(user.getEmail(), is(email));
        assertThat(user.getPassword(), is(password));
        assertThat(user.getNickname(), is(nickname));
        assertThat(user.getPhone(), is(phone));
        assertThat(user.getType(), is(type));
    }

    @Test
    public void insertUser() {
        User user = new User();

        user.setEmail("test2@gmail.com");
        user.setPassword("test2");
        user.setNickname("test2");
        user.setPhone("010-5678-1234");
        user.setType((byte) 2);

        User insertedUser = userRepository.save(user);

        assertThat(insertedUser.getEmail(), is(user.getEmail()));
        assertThat(insertedUser.getPassword(), is(user.getPassword()));
        assertThat(insertedUser.getNickname(), is(user.getNickname()));
        assertThat(insertedUser.getPhone(), is(user.getPhone()));
        assertThat(insertedUser.getType(), is(user.getType()));
    }

    @Test
    public void updateUser() {
        User user = new User();

        user.setEmail("test3@gmail.com");
        user.setPassword("test3");
        user.setNickname("test3");
        user.setPhone("010-1111-2222");
        user.setType((byte) 2);

        user = userRepository.save(user);

        Integer id = user.getNo();
        user = userRepository.findById(id).get();

        user.setPassword("test5");

        User updatedUser = userRepository.save(user);

        assertThat(updatedUser.getEmail(), is(user.getEmail()));
        assertThat(updatedUser.getPassword(), is(user.getPassword()));
        assertThat(updatedUser.getNickname(), is(user.getNickname()));
        assertThat(updatedUser.getPhone(), is(user.getPhone()));
        assertThat(updatedUser.getType(), is(user.getType()));
    }

    @Test
    public void deleteUser() {
        userRepository.deleteById(id);

        User deletedUser = null;

        try {
            deletedUser = userRepository.findById(id).get();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        assertThat(deletedUser, IsNull.nullValue());
    }
}
