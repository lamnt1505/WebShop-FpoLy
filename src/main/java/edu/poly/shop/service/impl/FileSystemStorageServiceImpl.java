package edu.poly.shop.service.impl;



import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.poly.shop.config.StorageProperties;
import edu.poly.shop.exception.StorageException;
import edu.poly.shop.exception.StorageFileNotFoundException;
import edu.poly.shop.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService{
	private final Path rootLocation;// đường dẫn gốc dùng để lưu file hình ảnh
	
	//file lưu trữ dựa vào Id truyền vào
	@Override
	public String getStoredFilename(MultipartFile file,String id) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + id + "." + ext;
	}
	
	//cấu hình cho lưu trữ
	public FileSystemStorageServiceImpl(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}
	
	//lưu nội dung của multipartFile
	@Override
	public void store(MultipartFile file, String storedFilename) {
		try {
			if(file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}
			
			Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename)).normalize().toAbsolutePath();
			if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside current directory!");
			}
			
			try(InputStream inputStream = file.getInputStream()){
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
			
		} catch (Exception e) {
			throw new StorageException("Failed to store file",e);
		}
	}
	
	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	
	//nạp nội dung của file dưới dạng resource
	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			org.springframework.core.io.Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}
			throw new StorageFileNotFoundException("Could not read file: " + filename);
		} catch (Exception e) {
			throw new StorageFileNotFoundException("Could not read file: " + e);
		}
	}
	
	//xóa file khi không cần thiết
	@Override
	public void delete(String storeFilename) throws IOException {
		Path destinationFile = this.rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();
		
		Files.delete(destinationFile);
	}
	
	//khởi tạo các thư mục
	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
