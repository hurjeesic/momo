package team.ohjj.momo.restapi;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ohjj.momo.domain.Portfolio;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.PortfolioJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	public Boolean createPortfolio(HttpServletRequest request, HttpSession session, @Nullable @RequestParam MultipartFile file, @ModelAttribute Portfolio portfolio) {
		User user = (User)session.getAttribute("user");
		String path;
		File projectFile;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;

		try {
			if (file != null) {
				if (FilenameUtils.getExtension(file.getOriginalFilename()).equals("zip")) {
					portfolio.setProjectFile(portfolio.getProject().getNo() + "_" + file.getOriginalFilename());
					path = request.getServletContext().getRealPath("/") + "/WEB-INF/static/project/" + portfolio.getProjectFile();
					projectFile = new File(path);
					fileOutputStream = new FileOutputStream(projectFile);
					bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
					bufferedOutputStream.write(file.getBytes());
				}
				else {
					throw new RuntimeException("Extension name Exception");
				}
			}

			portfolio.setUser(user);

			return portfolioJpaRepository.save(portfolio) != null;
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
		catch (RuntimeException re) {
			re.printStackTrace();
		}
		finally {
			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
}
