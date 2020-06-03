package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.ApplyField;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/applyField")
public class ApplyFieldRestController {
	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

	@GetMapping("/list/{projectNo}")
	public List<ApplyField> getApplyFieldListByProject(HttpSession session, @PathVariable Integer projectNo) {
		User user = (User)session.getAttribute("user");
		Project project = projectJpaRepository.findById(projectNo).get();
		if (project.getOrganizer().equals(user)) {
			return applyFieldJpaRepository.findAllByProject(project);
		}

		return null;
	}
}
