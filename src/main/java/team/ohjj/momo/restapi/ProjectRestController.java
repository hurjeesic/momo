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
	public Project getProject(HttpSession session, @PathVariable Integer no) {
		User user = (User)session.getAttribute("user");

		Project project = projectJpaRepository.findById(no).get();
		if (project.getOrganizer().equals(user)) {
			return projectJpaRepository.findById(no).get();
		}

		return null;
	}

	@PostMapping("/insert")
	public Boolean createProject(HttpSession session, @ModelAttribute Project project, @ModelAttribute ApplyFieldList applyFields, @RequestParam String field) {
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

		return true;
	}

	@PutMapping("/update")
	public Boolean updateProject(HttpSession session, @ModelAttribute Project project) {
		if (session.getAttribute("user") != null) {
			projectJpaRepository.save(project);

			return true;
		}

		return false;
	}

	@DeleteMapping("/delete")
	public Boolean deleteProject(HttpSession session, @ModelAttribute Project project) {
		if (session.getAttribute("user") != null) {
			projectJpaRepository.deleteById(project.getNo());

			return true;
		}

		return false;
	}
}
