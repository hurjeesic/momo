package team.ohjj.momo;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ProjectRepository;
import team.ohjj.momo.entity.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
public class ProjectTests {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    private Integer id = 1;
    private String title = "test";
    private String content = "this is test.";
    private Integer organizer = 1;
    private Byte process = 1;
    private Byte apply = 1;

    @Test
    public void getProject() {
        Project project = projectRepository.findById(id).get();

        assertThat(project.getTitle(), is(title));
        assertThat(project.getContent(), is(content));
        assertThat(project.getOrganizer().getNo(), is(organizer));
        assertThat(project.getProcess(), is(process));
        assertThat(project.getApply(), is(apply));
    }

    @Test
    public void insertProject() {
        Project project = new Project();

        project.setTitle("모여모여 게시판");
        project.setContent("대학생 프로젝트 관리를 위한 사이트 모여모여 게시판입니다.");
        project.setOrganizer(userRepository.findById(id).get());
        project.setProcess((byte) 1);
        project.setApply((byte) 1);

        Project insertedProject = projectRepository.save(project);

        assertThat(insertedProject.getTitle(), is(project.getTitle()));
        assertThat(insertedProject.getContent(), is(project.getContent()));
        assertThat(insertedProject.getOrganizer(), is(project.getOrganizer()));
        assertThat(insertedProject.getProcess(), is(project.getProcess()));
        assertThat(insertedProject.getApply(), is(project.getApply()));
    }

    @Test
    public void updateUser() {
        Project project = new Project();

        project.setTitle("모여모여 게시판");
        project.setContent("대학생 프로젝트 관리를 위한 사이트 모여모여 게시판입니다.");
        project.setOrganizer(userRepository.findById(id).get());
        project.setProcess((byte) 1);
        project.setApply((byte) 1);

        project = projectRepository.save(project);

        Integer id = project.getNo();
        project = projectRepository.findById(id).get();

        project.setContent("# 모모 프로젝트\n" + project.getContent());
        project.setProcess((byte) 2);

        Project updatedProject = projectRepository.save(project);

        assertThat(updatedProject.getTitle(), is(project.getTitle()));
        assertThat(updatedProject.getContent(), is(project.getContent()));
        assertThat(updatedProject.getOrganizer().toString(), is(project.getOrganizer().toString()));
        assertThat(updatedProject.getProcess(), is(project.getProcess()));
        assertThat(updatedProject.getApply(), is(project.getApply()));

    }

    @Test
    public void deleteUser() {
        Project project = new Project();

        project.setTitle("모여모여 게시판");
        project.setContent("대학생 프로젝트 관리를 위한 사이트 모여모여 게시판입니다.");
        project.setOrganizer(userRepository.findById(id).get());
        project.setProcess((byte) 1);
        project.setApply((byte) 1);

        project = projectRepository.save(project);

        projectRepository.deleteById(project.getNo());

        Optional<Project> deletedProject = projectRepository.findById(project.getNo());

        assertThat(deletedProject.isPresent(), is(false));
    }
}
