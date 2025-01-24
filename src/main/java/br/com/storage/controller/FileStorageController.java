package br.com.storage.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.storage.configuration.FileStorageProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/files")
public class FileStorageController {

	private final Path fileStorageLocation;
	
	public FileStorageController(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).
				toAbsolutePath().normalize();
	}
	
	@PostMapping("/upload")
	@Operation(summary = "Endpoint responsible for uploading files.") 
    @ApiResponse(responseCode = "200",description = " success",content = {
   	@Content(mediaType = "application.json",schema = @Schema(implementation = ResponseEntity.class))
    })       
	public ResponseEntity<String>uploadFile(@RequestParam(name = "file") MultipartFile file){
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			Path targetLocation = fileStorageLocation.resolve(fileName);
		    file.transferTo(targetLocation);
		    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		    		.path("/api/files/download/")
		    		.path(fileName)
		    		.toUriString();
		    
		    return ResponseEntity.ok("Upload completed! download link: " + fileDownloadUri);
			
		} catch (IOException eX) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/download/{fileName:.+}")
	@Operation(summary = "Endpoint responsible for downloading files.") 
    @ApiResponse(responseCode = "200",description = " success",content = {
   	@Content(mediaType = "application.json",schema = @Schema(implementation = ResponseEntity.class))
    })       
	public ResponseEntity<Resource>downloadFile(@PathVariable String fileName,HttpServletRequest request) throws IOException {
		Path filePath = fileStorageLocation.resolve(fileName).normalize();
		
		try {
			Resource  resource = new UrlResource(filePath.toUri());
			String contenType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			if(contenType == null) {
				contenType = "application/octet-stream";
			}
			
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contenType))
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
					
		} catch (MalformedURLException ex) {
			
			return ResponseEntity.badRequest().build();
		}
		
		
		
	}
	
	@GetMapping("/list")
	@Operation(summary = "Endpoint responsible for list files.") 
    @ApiResponse(responseCode = "200",description = " success",content = {
   	@Content(mediaType = "application.json",schema = @Schema(implementation = ResponseEntity.class))
    })       
	public ResponseEntity<List<String>>listFiles() throws IOException{
		List<String>fileNames = Files.list(fileStorageLocation)
				.map(Path::getFileName)
				.map(Path::toString)
				.collect(Collectors.toList());
		return ResponseEntity.ok(fileNames);
	}
	
}
