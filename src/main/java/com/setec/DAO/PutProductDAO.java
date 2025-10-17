package com.setec.DAO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutProductDAO {
	private int id;
	private String name;
	private int qty;
	private double price;
	private MultipartFile imageUrl;
}
