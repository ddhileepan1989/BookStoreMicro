package com.micro.inventory;

import com.micro.inventory.model.Inventory;
import com.micro.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import scala.collection.mutable.LinkedHashMap;

import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.micro.*")
public class InventoryServiceApplication {

	private final InventoryRepository inventoryRepository;
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadSkuCodes()
	{
		return args -> {


			LinkedHashMap.LinkedEntry<String,String> obj;

			Inventory inventory=new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory2=new Inventory();
			inventory2.setSkuCode("iphone_13_red");
			inventory2.setQuantity(0);
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory2);
		};
	}
}
