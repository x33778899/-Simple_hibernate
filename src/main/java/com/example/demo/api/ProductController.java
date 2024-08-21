package com.example.demo.api;

import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.dto.SetProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private final ProductDAO productDAO;
    @Autowired
    private final UserDAO userDAO;

    public ProductController() {
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
    }

    public ProductController(ProductDAO productDAO, UserDAO userDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        ApiResponse response = new ApiResponse(200, "products retrieved successfully", products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getProducts(@PathVariable Long userId) {
        List<Product> product = productDAO.findByUserId(userId);
        ApiResponse response = new ApiResponse(200, "products retrieved successfully", product);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProducts(@RequestBody SetProductDTO setProductDTO) {
        Long userId = setProductDTO.getUserId();

        if (userId == null) {
            // 返回错误响应，指示缺少 userId
            ApiResponse response = new ApiResponse(400, "User ID is required", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userDAO.getUser(userId);

        if (user == null) {

            ApiResponse response = new ApiResponse(404, "User not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }


        Product product = new Product();
        product.setName(setProductDTO.getName());
        product.setPrice(setProductDTO.getPrice());
        product.setUser(user);

        productDAO.saveProduct(product);

        // 返回成功响应
        ApiResponse response = new ApiResponse(200, "Product created successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
