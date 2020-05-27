package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.*;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.MemberJpaRepository;
import team.ohjj.momo.entity.ProjectRepository;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectRestController {
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@GetMapping("/count")
	public Integer getProjectCount() {
		return (int)projectRepository.count();
	}

	@GetMapping("/list/{pageNo}")
	public List<Map<String, Object>> getProjectList(@PathVariable Integer pageNo) {
		Integer unitCount = 10;
		List<Project> allProject = projectRepository.findAll();
		List<Project> partProject = new ArrayList<>();
		List<Map<String, Object>> result = new ArrayList<>();

		try {
			int endPage = unitCount * pageNo;
			for (int i = endPage - 10; i < (allProject.size() < endPage ? allProject.size() : endPage); i++) {
				Map<String, Object> projectObj = new HashMap<>();
				partProject.add(allProject.get(i));
				Project presentProject = partProject.get(i - unitCount * (pageNo - 1));

				List<ApplyField> applyFieldList = applyFieldJpaRepository.findAllByProject(presentProject);
				List<Member> memberList = memberJpaRepository.findAllByProject(presentProject);

				if (presentProject.getContent().length() > 20) {
					presentProject.setContent(presentProject.getContent().substring(0, 20) + "...");
				}

				projectObj.put("project", presentProject);
				projectObj.put("applyFields", applyFieldList);
				projectObj.put("members", memberList);
				result.add(projectObj);
			}
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return result;
	}

	@GetMapping("/{no}")
	public Project getProject(@PathVariable Integer no) {
		Optional<Project> project = projectRepository.findById(no);

		return project.isPresent() ? project.get() : null;
	}

	@PostMapping("/insert")
	public Integer createProject(HttpSession session, @ModelAttribute Project project, @ModelAttribute ApplyFieldList applyFields, @ModelAttribute Member member) {
		User user = (User)session.getAttribute("user");

		project.setOrganizer(user);
		project = projectRepository.save(project);

		member.setProject(project);
		for (ApplyField applyField : applyFields.getApplyFieldList()) {
			applyField.setProject(project);
			applyFieldJpaRepository.save(applyField);
			if (applyField.getField().equals(member.getField())) {
				member.setField(applyField);
				memberJpaRepository.save(member);
			}
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
