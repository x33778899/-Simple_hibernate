package com.example.demo.api;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        ApiResponse response = new ApiResponse(200, "Users retrieved successfully", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        User user = userDAO.getUser(id);
        if (user != null) {
            ApiResponse response = new ApiResponse(200, "User retrieved successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse(404, "User not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        try {
            // 檢查使用者名稱是否已存在
            User existingUser = userDAO.findByUsername(user.getUsername());
            if (existingUser != null) {
                // 如果使用者名稱已存在，則傳回 400 錯誤
                ApiResponse response = new ApiResponse(400, "Username already exists", null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 如果用戶名不存在，儲存用戶
            userDAO.saveUser(user);
            ApiResponse response = new ApiResponse(200, "User created successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // 捕獲並處理異常
            ApiResponse response = new ApiResponse(500, "An error occurred: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userDAO.getUser(id);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            userDAO.updateUser(user);
            ApiResponse response = new ApiResponse(200, "User updated successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse(404, "User not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        if (userDAO.getUser(id) != null) {
            userDAO.deleteUser(id);
            ApiResponse response = new ApiResponse(200, "User deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse(404, "User not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
