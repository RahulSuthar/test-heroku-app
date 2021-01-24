package com.smallbazaar.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.smallbazaar.rest.dto.ExcelProductData;
import com.smallbazaar.rest.model.FileUpload;
import com.smallbazaar.rest.service.FileUploadService;
import com.smallbazaar.rest.utils.ExcelHelper;

@RestController
@RequestMapping("/api/auth/upload")
public class FileUploadController {

	private List<ExcelProductData> productList = new ArrayList<>();
	
	@Autowired
	private FileUploadService fileUploadService;

	@GetMapping
	public ResponseEntity<Object> health() {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/images")
	public ResponseEntity<Object> uploadFile(@RequestParam("file")MultipartFile file) {
		String message="";
		
		try {
			fileUploadService.store(file);
			message="Uploaded the file successfully: "+file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}catch(Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}
	
	@PostMapping("/excel")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				productList=ExcelHelper.excelToTutorials(file.getInputStream());
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(message);
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
			}
		}
		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@GetMapping("/products")
	public ResponseEntity<List<ExcelProductData>> getAllUploadedProducts() {
		try {
			List<ExcelProductData> tutorials = productList;
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/files")
	public ResponseEntity<List<HashMap<String, String>>> getListFiles(){
		
		List<HashMap<String, String>> files=fileUploadService.getAllFiles().map(dbFile->{
			String fileDownloadUri=ServletUriComponentsBuilder
					.fromCurrentRequestUri()
					.path("/"+dbFile.getId())
					.toUriString();
			HashMap<String,String> fileData = new HashMap<>();
			fileData.put("name", dbFile.getName());
			fileData.put("downloadUrl", fileDownloadUri);
			fileData.put("type", dbFile.getType());
			fileData.put("length", String.valueOf(dbFile.getData().length));
			return fileData;
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
	
	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id){
		FileUpload fileDB=fileUploadService.getFile(id);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}
}