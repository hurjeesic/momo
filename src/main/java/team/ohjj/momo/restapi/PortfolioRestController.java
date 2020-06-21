package team.ohjj.momo.restapi;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.ohjj.momo.domain.Portfolio;
import team.ohjj.momo.domain.Project;
import team.ohjj.momo.domain.User;
import team.ohjj.momo.entity.PortfolioJpaRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		File projectFile;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;

		try {
			if (file != null) {
				if (FilenameUtils.getExtension(file.getOriginalFilename()).equals("zip")) {
					Project project = projectJpaRepository.findById(portfolio.getProject().getNo()).get();
					project.setFileName(file.getOriginalFilename());
					String fileName = portfolio.getProject().getNo() + "_" + project.getFileName();
					projectFile = new File(request.getServletContext().getRealPath("/") + "/WEB-INF/static/project/" + fileName);
					fileOutputStream = new FileOutputStream(projectFile);
					bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
					bufferedOutputStream.write(file.getBytes());
					projectJpaRepository.save(project);
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

	@GetMapping("/download/{no}")
	public ResponseEntity<Resource> downloadProjectFile(HttpServletRequest request, HttpSession session, @PathVariable Integer no) throws IOException {
		User user = (User)session.getAttribute("user");
		Project project = projectJpaRepository.findById(no).get();
		String fileName = project.getNo() + "_" + project.getFileName();
		Path path = Paths.get(request.getServletContext().getRealPath("/") + "/WEB-INF/static/project/" + fileName);
		String contentType = Files.probeContentType(path); // application/x-zip-compressed

		HttpHeaders headers = new HttpHeaders();
		String encodedFileName = URLEncoder.encode(project.getFileName(), "UTF-8").replace("+", "%20");
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\";");
		headers.add(HttpHeaders.CONTENT_ENCODING, "binary");

		Resource resource = new InputStreamResource(Files.newInputStream(path));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
}
