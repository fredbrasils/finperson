package br.com.finperson.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.core.service.RoleService;
import br.com.finperson.domain.CategoryEntity;
import br.com.finperson.domain.RoleEntity;
import br.com.finperson.domain.enumm.RoleEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

	private final CategoryService categoryService;
	private final RoleService roleService;

	public DataLoader(CategoryService categoryService, RoleService roleService) {
		this.categoryService = categoryService;
		this.roleService = roleService;
	}

	public void run(String... args) throws Exception {

		CategoryEntity transport = new CategoryEntity();
		transport.setName("Transport");
		
		CategoryEntity food = new CategoryEntity();
		food.setName("Food");
		
		CategoryEntity health = new CategoryEntity();
		health.setName("Health");
		
		CategoryEntity sport = new CategoryEntity();
		sport.setName("Sport");
		
		categoryService.save(transport);
		categoryService.save(food);
		categoryService.save(health);
		categoryService.save(sport);

		log.debug("Loaded Categories....");
		
		RoleEntity admin = RoleEntity.builder().role(RoleEnum.ROLE_ADIM).build();
		RoleEntity user = RoleEntity.builder().role(RoleEnum.ROLE_USER).build();
		RoleEntity guest = RoleEntity.builder().role(RoleEnum.ROLE_GUEST).build();

		roleService.save(admin);
		roleService.save(user);
		roleService.save(guest);
		
	}

}
