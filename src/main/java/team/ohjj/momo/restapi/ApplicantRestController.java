package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Applicant;
import team.ohjj.momo.domain.Member;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/applicant")
public class ApplicantRestController {
	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@Autowired
	ApplicantJpaRepository applicantJpaRepository;

	@GetMapping("/{no}")
	public List<Applicant> getApplicantList(@PathVariable Integer no) {
		return applicantJpaRepository.findAllByProject(projectJpaRepository.findById(no).get());
	}

	@PostMapping("/complete")
	public List<Member> completeApply(HttpSession session, @RequestParam(value = "applicantNums[]") List<Integer> applicantNums) {
		User user = (User)session.getAttribute("user");
		List<Member> confirmMembers = new ArrayList<>();
		for (Integer no : applicantNums) {
			Applicant applicant = applicantJpaRepository.findById(no).get();

			if (user.equals(applicant.getProject().getOrganizer())) {
				applicantJpaRepository.deleteById(no);
				if (!applicantJpaRepository.findById(no).isPresent()) {
					try {
						Member member = new Member();
						member.setProject(applicant.getProject());
						member.setUser(applicant.getUser());
						member.setField(applicant.getField());
						member.setComplete(false);
						confirmMembers.add(memberJpaRepository.save(member));
					}
					catch (Exception e) {
						applicantJpaRepository.save(applicant);
						e.printStackTrace();
					}
				}
			}
		}

		return confirmMembers;
	}

	@PostMapping("/insert")
	public Boolean createApplicant(HttpSession session, @ModelAttribute Applicant applicant) {
		applicant.setUser(((User)session.getAttribute("user")));

		applicantJpaRepository.save(applicant);

		return true;
	}
}
