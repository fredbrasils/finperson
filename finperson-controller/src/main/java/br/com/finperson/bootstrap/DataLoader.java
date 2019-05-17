package br.com.finperson.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.model.CategoryEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
//@Profile("!postgresql")
public class DataLoader implements CommandLineRunner {

	private final CategoryService categoryService;

	public DataLoader(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public void run(String... args) throws Exception {

		CategoryEntity transport = new CategoryEntity();
		transport.setName("Transport");
		transport.setColor("241-112-19-100");
		transport.setIcon("fas fa-bus");
		
		CategoryEntity food = new CategoryEntity();
		food.setName("Food");
		food.setColor("126-211-33-100");
		food.setIcon("fas fa-utensils");
		
		CategoryEntity health = new CategoryEntity();
		health.setName("Health");
		health.setColor("208-2-27-100");
		health.setIcon("fas fa-briefcase-medical");
		
		CategoryEntity sport = new CategoryEntity();
		sport.setName("Sport");
		sport.setColor("144-19-254-100");
		sport.setIcon("fas fa-futbol");
		
		categoryService.save(transport);
		categoryService.save(food);
		categoryService.save(health);
		categoryService.save(sport);

		log.debug("Loaded Categories....");
		
	}

}
