package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.*;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.entity.MemberJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectRestController {
	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@Autowired
	ChatRoomRepository chatRoomRepository;

	private final Integer unitCount = 10;

	@GetMapping("/count")
	public Integer getProjectCount() {
		return (int)projectJpaRepository.count();
	}

	@GetMapping("/list/{pageNo}")
	public List<Map<String, Object>> getProjectList(@PathVariable Integer pageNo) {
		List<Project> allProject = projectJpaRepository.findAll();
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
		return projectJpaRepository.findById(no).get();
	}

	@PostMapping("/insert")
	public Integer createProject(HttpSession session, @ModelAttribute Project project, @ModelAttribute ApplyFieldList applyFields, @RequestParam String field) {
		User user = (User)session.getAttribute("user");

		project.setOrganizer(user);
		project = projectJpaRepository.save(project);

		Member member = new Member();
		member.setUser(user);
		member.setProject(project);
		for (ApplyField applyField : applyFields.getApplyFieldList()) {
			applyField.setProject(project);
			applyFieldJpaRepository.save(applyField);
			if (applyField.getField().equals(field)) {
				member.setField(applyField);
				memberJpaRepository.save(member);
			}
		}

		chatRoomRepository.createChatRoom("일반", project.getNo());

		return project.getNo();
	}

	@PutMapping("/update")
	public Integer updateProject(@ModelAttribute Project project) {
		Project updatedProject = projectJpaRepository.save(project);

		return updatedProject == null ? 0 : updatedProject.getNo();
	}

	@DeleteMapping("/delete/{no}")
	public Integer deleteProject(HttpSession session, @PathVariable Integer no) {
		Integer result = -1;
		User user = (User)session.getAttribute("user");

		if (user == null) {
			result = 0;
		}
		else if (projectJpaRepository.findById(no).get().getOrganizer().equals(user)) {
			projectJpaRepository.deleteById(no);
			result = no;
		}

		return result;
	}
}
