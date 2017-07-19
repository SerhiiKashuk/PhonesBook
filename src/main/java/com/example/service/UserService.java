package com.example.service;


import com.example.model.User;

public interface UserService {
	public User findUserByLogin(String email);
	public void saveUser(User user);
	User findById(int id);
}
