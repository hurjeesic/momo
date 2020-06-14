package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.*;
import team.ohjj.momo.entity.*;

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

	@Autowired
	ApplicantJpaRepository applicantJpaRepository;

	private final Integer unitCount = 10;

	@GetMapping("/count")
	public Integer getProjectCount() {
		return (int)projectJpaRepository.count();
	}

	@GetMapping("/list/{pageNo}")
	public List<Map<String, Object>> getProjectList(HttpSession session, @PathVariable Integer pageNo) {
		User user = (User)session.getAttribute("user");

		List<Project> allProject = projectJpaRepository.findAllByOrganizerNot(user);
		List<Project> partProject = new ArrayList<>();
		List<Map<String, Object>> result = new ArrayList<>();
		int endPage = unitCount * pageNo;

		for (int i = 0; i < allProject.size(); i++) {
			if (memberJpaRepository.findByProjectAndUser(allProject.get(i), user).isPresent() ||
				applicantJpaRepository.findByProjectAndUser(allProject.get(i), user).isPresent()) {
				allProject.remove(i--);
			}
		}

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

		return result;
	}

	@GetMapping("/{no}")
	public Map<String, Object> getProject(HttpSession session, @PathVariable Integer no) {
		User user = (User)session.getAttribute("user");

		Project project = projectJpaRepository.findById(no).get();
		Map<String, Object> projectObj = new HashMap<>();

		List<ApplyField> applyFieldList = applyFieldJpaRepository.findAllByProject(project);
		List<Member> memberList = memberJpaRepository.findAllByProject(project);

		projectObj.put("project", project);
		projectObj.put("applyFields", applyFieldList);
		projectObj.put("members", memberList);

		return projectObj;
	}

	@GetMapping("/list/present")
	public List<Project> getMyProjectList(HttpSession session) {
		User user = (User)session.getAttribute("user");
		List<Project> myAllProject = projectJpaRepository.findAll();

		for (int i = 0; i < myAllProject.size(); i++) {
			if (!memberJpaRepository.findByProjectAndUser(myAllProject.get(i), user).isPresent()) {
				myAllProject.remove(i--);
			}
		}

		return myAllProject;
	}

	@GetMapping("/list/apply")
	public List<Project> getMyApplyProjectList(HttpSession session) {
		User user = (User)session.getAttribute("user");
		List<Project> myAllProject = projectJpaRepository.findAllByOrganizerNot(user);

		for (int i = 0; i < myAllProject.size(); i++) {
			if (!applicantJpaRepository.findByProjectAndUser(myAllProject.get(i), user).isPresent()) {
				myAllProject.remove(i--);
			}
		}

		return myAllProject;
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
				member.setComplete(false);
				memberJpaRepository.save(member);
			}
		}

		chatRoomRepository.createChatRoom("일반", project.getNo());

		return true;
	}

	@PutMapping("/update")
	public Boolean updateProject(HttpSession session, @ModelAttribute Project project, @ModelAttribute ApplyFieldList applyFields, @RequestParam String field) {
		User user = (User)session.getAttribute("user");

		project.setOrganizer(user);
		project = projectJpaRepository.save(project);

		Member member = memberJpaRepository.findByProjectAndUser(project, user).get();
		for (ApplyField applyField : applyFields.getApplyFieldList()) {
			applyField.setProject(project);
			applyFieldJpaRepository.save(applyField);
			if (applyField.getField().equals(field)) {
				member.setField(applyField);
				member.setComplete(false);
				memberJpaRepository.save(member);
			}
		}

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

	@PostMapping("/complete/{no}")
	public Boolean completeProject(HttpSession session, @PathVariable Integer no) {
		User user = (User)session.getAttribute("user");
		Project project = projectJpaRepository.findById(no).get();

		if (user.equals(project.getOrganizer())) {
			List<Member> members = memberJpaRepository.findAllByProject(project);
			Member organizer = memberJpaRepository.findByProjectAndUser(project, user).get();
			members.remove(organizer);
			for (Member member : members) {
				if (!member.isComplete()) {
					return false;
				}
			}

			organizer.setComplete(true);
			memberJpaRepository.save(organizer);
			project.setComplete(true);
			projectJpaRepository.save(project);
		}
		else {
			return false;
		}


		return true;
	}
}
