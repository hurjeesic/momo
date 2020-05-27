package team.ohjj.momo.restapi;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.ApplyField;
import team.ohjj.momo.domain.ApplyFieldList;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.ProjectRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectRestController {
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

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

	@PostMapping("/insert")
	public Integer createProject(HttpSession session, @ModelAttribute Project project, @ModelAttribute ApplyFieldList applyFields) {
		User user = (User)session.getAttribute("user");

		project.setOrganizer(user);
		project = projectRepository.save(project);

		for (ApplyField applyField : applyFields.getApplyFieldList()) {
			applyField.setProject(project);
			applyFieldJpaRepository.save(applyField);
		}

		return project.getNo();
	}

	@PutMapping("/update")
	public Integer updateProject(@ModelAttribute Project project) {
		Project updatedProject = projectRepository.save(project);

		return updatedProject == null ? 0 : updatedProject.getNo();
	}

	@DeleteMapping("/delete/{no}")
	public Integer deleteProject(HttpSession session, @PathVariable Integer no) {
		Integer result = -1;
		User user = (User)session.getAttribute("user");

		if (user == null) {
			result = 0;
		}
		else if (projectRepository.findById(no).get().getOrganizer().equals(user)) {
			projectRepository.deleteById(no);
			result = no;
		}

		return result;
	}
}
