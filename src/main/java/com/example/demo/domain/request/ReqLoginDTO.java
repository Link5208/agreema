package com.example.demo.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
	@NotBlank(message = "Username must not blank")
	private String username;

	@NotBlank(message = "Password must not blank")
	private String password;
}
