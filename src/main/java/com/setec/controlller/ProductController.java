package com.setec.controlller;

import java.io.File; 
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.setec.DAO.PostProductDAO;
import com.setec.DAO.PutProductDAO;
import com.setec.config.MyConfig;
import com.setec.entity.Product;
import com.setec.repos.ProductRepo;
 

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final MyConfig myConfig; 
	
	// http://localhost:9000/swagger-ui/index.html
	// http://localhost:9000/api/product
	
	private static final boolean True = false;
	@Autowired
	private ProductRepo productRepo;

    ProductController(MyConfig myConfig) {
        this.myConfig = myConfig;
    }

    // get all products
	@GetMapping
	public Object getAll() {
		var products = productRepo.findAll();
		
		if(products.size() == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						Map.of("Message","No Products.")
					);
		}else {
			return products;
		}
	}
	
	// filter product by id
	@GetMapping("id={id}")
	public Object getById(@PathVariable("id") Integer id) {
		var prod = productRepo.findById(id);
		
		if(prod.isEmpty()) {
			return ResponseEntity.status(404).body(Map.of(
						"message","product id = " + id + " not found."
					));
		}else {
			return prod.get();
		}
	}
	
	// filter product by name
	@GetMapping("product={name}")
	public Object getByName(@PathVariable("name") String name) {
		List<Product> prod = productRepo.findByName(name);
		
		if(prod.isEmpty()) {
			return ResponseEntity.status(404).body(Map.of(
						"message","product id = " + name + " not found."
					));
		}else {
			return prod;
		}
	}
	
	// insert product
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object postProduct(@ModelAttribute PostProductDAO product)throws Exception {
		
		var file = product.getImageFile();
		
		String uploadDir = new File("myApp/static").getAbsolutePath();
		File dir = new File(uploadDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		// ✅ Create a unique filename (preserve extension)
	    String originalName = Objects.requireNonNull(file.getOriginalFilename());
	    String extension = originalName.substring(originalName.lastIndexOf('.') + 1);
	    String fileName = UUID.randomUUID() + "." + extension;
	    
		String filePath = Paths.get(uploadDir,fileName).toString();
		
		// save image
		file.transferTo(new File(filePath));
		
		Product prod = new Product();
		prod.setName(product.getName());
		prod.setPrice(product.getPrice());
		prod.setQty(product.getQty());
		prod.setImageUrl("/static/" + fileName);
		
		productRepo.save(prod);
		return ResponseEntity.status(201).body(prod);
	}
	
	// update product by id
	@PutMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object updateByid(PutProductDAO product) throws IllegalStateException, IOException {
		
		// check product id has or not
		var prods = productRepo.findById(product.getId()); 
		if(!prods.isPresent()) {
			 return ResponseEntity.notFound().build();
		}

		Product prod = prods.get();
		prod.setName(product.getName());
		prod.setPrice(product.getPrice());
		prod.setQty(product.getQty());
		
		MultipartFile file = product.getImageUrl();
		
		if (file != null && !file.isEmpty()) {
		    String uploadDir = new File("myApp/static").getAbsolutePath();

		    String originalName = Objects.requireNonNull(file.getOriginalFilename());
		    String extension = originalName.substring(originalName.lastIndexOf('.') + 1);
		    String fileName = UUID.randomUUID() + "." + extension;
		    String filePath = Paths.get(uploadDir, fileName).toString();

		    // Delete old image (handle path correctly)
		    if (prod.getImageUrl() != null && !prod.getImageUrl().isEmpty()) {
		        String oldFileName = new File(prod.getImageUrl()).getName(); // extract just the filename
		        File fileToDelete = new File(uploadDir, oldFileName);
		        if (fileToDelete.exists()) {
		            fileToDelete.delete();
		        }
		    }

		    // Save new image
		    file.transferTo(new File(filePath));

		    // Save accessible URL (relative to static folder)
		    prod.setImageUrl("/static/" + fileName);
		}

//		prod.setImageUrl(prods.get().getImageUrl());
		productRepo.save(prod);
		
		return ResponseEntity.ok(prod);
}
	
	// delete product by id
	@DeleteMapping("delete/id={id}")
	public Object deleteByid(@PathVariable("id") Integer id) {
		
		var prod = productRepo.findById(id);
		
		
		if(!prod.isEmpty()) {
			
			var imageName = prod.get().getImageUrl().substring(8);
			String dir = new File("myApp/static").getAbsolutePath();
			
//			String filePath = dir + "\\" +  imageName;
			String filePath = dir + File.separator + imageName; // Use File.separator for cross-platform
			
			File file = new File(filePath);
			
			file.delete();
			
			productRepo.deleteById(id);
			
			return ResponseEntity.status(200).body(Map.of("message","The product was deleted successfully."));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","product id = " + id + " not found."));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
