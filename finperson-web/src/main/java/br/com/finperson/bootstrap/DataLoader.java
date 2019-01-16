package br.com.finperson.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.finperson.core.service.CategoryService;
import br.com.finperson.domain.CategoryEntity;

@Component
public class DataLoader implements CommandLineRunner {

	private final CategoryService categoryService;

	public DataLoader(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
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

		System.out.println("Loaded Categories....");

	}

}
