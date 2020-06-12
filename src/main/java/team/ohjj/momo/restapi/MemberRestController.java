package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Member;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.MemberJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/member")
public class MemberRestController {
	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@GetMapping("/")
	public Member getMemberListByProject(HttpSession session, @RequestParam Integer projectNo) {
		User user = (User)session.getAttribute("user");
		Project project = projectJpaRepository.findById(projectNo).get();
		if (project.getOrganizer().equals(user)) {
			return memberJpaRepository.findByProjectAndUser(project, user).get();
		}

		return null;
	}

	@PostMapping("/insert")
	public Boolean createMember(HttpSession session, @ModelAttribute Member member) {
		User user = (User)session.getAttribute("user");

		memberJpaRepository.save(member);

		return true;
	}
}
