package edu.poly.shop.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	void delete(String storeFilename) throws IOException;

	Resource loadAsResource(String filename);

	Path load(String filename);

	void store(MultipartFile file, String storedFilename);

	String getStoredFilename(MultipartFile file, String id);

}
