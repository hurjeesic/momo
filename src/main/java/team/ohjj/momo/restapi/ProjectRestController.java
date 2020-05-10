package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.entity.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectRestController {
    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/count")
    public Integer getProjectCount() {
        return (int)projectRepository.count();
    }

    @GetMapping("/list/{pageNo}")
    public List<Project> getProjectList(@PathVariable Integer pageNo) {
        Integer unitCount = 10;
        List<Project> allProject = projectRepository.findAll();
        List<Project> partProject = new ArrayList<>();

        try {
            for (int i = unitCount * (pageNo - 1); i < unitCount * pageNo; i++) {
                partProject.add(allProject.get(i));
                Project presentProject = partProject.get(i - unitCount * (pageNo - 1));
                if (presentProject.getContent().length() > 20) {
                    presentProject.setContent(presentProject.getContent().substring(0, 20) + "...");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return partProject;
    }

    @GetMapping("/{no}")
    public Project getProject(@PathVariable Integer no) {
        Optional<Project> project = projectRepository.findById(no);

        return project.isPresent() ? project.get() : null;
    }

    @PutMapping("/insert")
    public Integer createProject(@ModelAttribute Project project) {
        Project insertedProject = projectRepository.save(project);

        return insertedProject == null ? 0 : insertedProject.getNo();
    }

    @PutMapping("/update")
    public Integer updateProject(@ModelAttribute Project project) {
        Project updatedProject = projectRepository.save(project);

        return updatedProject == null ? 0 : updatedProject.getNo();
    }

    @DeleteMapping("/delete/{no}")
    public Integer deleteProject(@PathVariable Integer no, @RequestParam String email) {
        Integer result = 0;

        try {
            if (!email.equals(projectRepository.findById(no).get().getOrganizer().getEmail())) {
                projectRepository.deleteById(no);
                result = no;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }
}
