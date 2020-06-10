package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ohjj.momo.domain.Applicant;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.ApplicantJpaRepository;
import team.ohjj.momo.entity.ApplyFieldJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/applicant")
public class ApplicantRestController {
	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	ApplyFieldJpaRepository applyFieldJpaRepository;

	@Autowired
	ApplicantJpaRepository applicantJpaRepository;

	@PostMapping("/insert")
	public Boolean createApplicant(HttpSession session, @ModelAttribute Applicant applicant) {
		applicant.setUser(((User)session.getAttribute("user")).getNo());

		applicantJpaRepository.save(applicant);

		return true;
	}
}
