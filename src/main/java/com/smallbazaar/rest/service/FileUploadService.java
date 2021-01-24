package com.smallbazaar.rest.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.smallbazaar.rest.model.FileUpload;
import com.smallbazaar.rest.repository.FileUploadRepository;

@Service
public class FileUploadService {

	@Autowired
	FileUploadRepository fileStorageRepository;

	public FileUpload store(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileUpload fileDB = new FileUpload(fileName, file.getContentType(), file.getBytes());

		return fileStorageRepository.save(fileDB);
	}

	public FileUpload getFile(String id) {
		return fileStorageRepository.findById(id).get();
	}

	public Stream<FileUpload> getAllFiles() {
		return fileStorageRepository.findAll().stream();
	}
}
