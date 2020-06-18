package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.*;
import team.ohjj.momo.entity.AwardJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/award")
public class AwardRestController {
	@Autowired
	AwardJpaRepository awardJpaRepository;

	@GetMapping("/list")
	public List<Award> getMyAwardList(HttpSession session) {
		return awardJpaRepository.findAllByUser((User)session.getAttribute("user"));
	}

	@PostMapping("/insert")
	public Award createMyAward(HttpSession session, Award award) {
		award.setUser((User)session.getAttribute("user"));

		return awardJpaRepository.save(award);
	}

	@DeleteMapping("/delete")
	public Boolean deleteMyAward(HttpSession session, @ModelAttribute AwardList awardList) {
		User user = (User)session.getAttribute("user");
		for (Award award : awardList.getAwardList()) {
			awardJpaRepository.deleteById(award.getNo());
		}

		return true;
	}
}