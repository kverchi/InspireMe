package com.kverchi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.kverchi.domain.User;

@Service
public interface UserService {
	public boolean registerAccount(User user);
	public void resetPassword(User user);
	public boolean isValidUsername(String usrName);
	public boolean isValidEmail(String email);
	public User getUserByUsername(String username);
	public User getUserById(int userId);
	public List<User> getAllUsers();
	public boolean setEnabled(User user);
	public boolean sendResetLink(String email);
}
