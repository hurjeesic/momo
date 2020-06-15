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

	@GetMapping("/list/{no}")
	public List<Member> getMemberListByProject(HttpSession session, @PathVariable Integer no) {
		User user = (User)session.getAttribute("user");
		Project project = projectJpaRepository.findById(no).get();
		List<Member> members = memberJpaRepository.findAllByProject(project);
		for (Member member : members) {
			if (member.getUser().equals(user)) {
				return memberJpaRepository.findAllByProject(project);
			}
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
