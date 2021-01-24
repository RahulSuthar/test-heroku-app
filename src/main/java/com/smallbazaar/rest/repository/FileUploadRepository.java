package com.smallbazaar.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smallbazaar.rest.model.FileUpload;

public interface FileUploadRepository extends JpaRepository<FileUpload, String> {

}
