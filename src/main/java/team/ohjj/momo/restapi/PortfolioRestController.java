package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.Portfolio;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.PortfolioJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/portfolio")
public class PortfolioRestController {
	@Autowired
	ProjectJpaRepository projectJpaRepository;

	@Autowired
	PortfolioJpaRepository portfolioJpaRepository;

	@GetMapping("/")
	public List<Portfolio> getPortfolioList(HttpSession session) {
		return portfolioJpaRepository.findAllByUser((User)session.getAttribute("user"));
	}

	@PostMapping("/insert")
	public Boolean createPortfolio(HttpSession session, @RequestParam Portfolio portfolio) {
		User user = (User)session.getAttribute("user");
		
		portfolio.setUser(user);
		portfolioJpaRepository.save(portfolio);

		return true;
	}
}
