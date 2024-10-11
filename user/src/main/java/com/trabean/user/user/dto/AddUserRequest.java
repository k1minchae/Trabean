package com.trabean.user.user.dto;


public record AddUserRequest(
	String name,
	String email,
	String password
//	String user_key
) {

}
