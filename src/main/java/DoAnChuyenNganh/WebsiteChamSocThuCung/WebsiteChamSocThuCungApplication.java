package DoAnChuyenNganh.WebsiteChamSocThuCung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"DoAnChuyenNganh.WebsiteChamSocThuCung",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.config",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.controllers",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.services",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.repositories",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.models",
		"DoAnChuyenNganh.WebsiteChamSocThuCung.security"
})
@EnableJpaRepositories(basePackages = "DoAnChuyenNganh.WebsiteChamSocThuCung.repositories")
public class WebsiteChamSocThuCungApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebsiteChamSocThuCungApplication.class, args);
	}
}