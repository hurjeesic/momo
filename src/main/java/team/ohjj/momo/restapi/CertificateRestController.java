package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Certificate;
import team.ohjj.momo.domain.CertificateList;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.CertificateJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateRestController {
	@Autowired
	CertificateJpaRepository certificateJpaRepository;

	@GetMapping("/list")
	public List<Certificate> getMyCertificateList(HttpSession session) {
		return certificateJpaRepository.findAllByUser((User)session.getAttribute("user"));
	}

	@PostMapping("/insert")
	public Certificate createMyCertificate(HttpSession session, Certificate certificate) {
		certificate.setUser((User)session.getAttribute("user"));

		return certificateJpaRepository.save(certificate);
	}

	@DeleteMapping("/delete")
	public Boolean deleteMyCertificateList(HttpSession session, @ModelAttribute CertificateList certificateList) {
		User user = (User)session.getAttribute("user");
		for (Certificate certificate : certificateList.getCertificateList()) {
			certificateJpaRepository.deleteById(certificate.getNo());
		}

		return true;
	}
}
