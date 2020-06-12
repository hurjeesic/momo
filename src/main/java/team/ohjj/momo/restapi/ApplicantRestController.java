package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Applicant;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ApplicantJpaRepository;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;
import team.ohjj.momo.entity.UserJpaRepository;

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
	ApplicantJpaRepository applicantJpaRepository;

	@GetMapping("/{no}")
	public List<Applicant> getApplicantList(@PathVariable Integer no) {
		return applicantJpaRepository.findAllByProject(projectJpaRepository.findById(no).get());
	}

	@PostMapping("/insert")
	public Boolean createApplicant(HttpSession session, @ModelAttribute Applicant applicant) {
		applicant.setUser(((User)session.getAttribute("user")));

		applicantJpaRepository.save(applicant);

		return true;
	}

	public Boolean deleteApplicant(HttpSession session, int applicantNo) {
		applicantJpaRepository.deleteById(applicantNo);

		return true;
	}
}
