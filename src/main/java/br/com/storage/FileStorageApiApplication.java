package br.com.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;



@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
			title = "API - System For Donwload And Uploading Files.",
			version = "1.0",
			description = "Documenting an api for downloading and uploading files.",
			contact = @Contact(name = "Carlos Roberto", email = "crrsj1@gmail.com")
		)
	)
public class FileStorageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileStorageApiApplication.class, args);
	}

}
