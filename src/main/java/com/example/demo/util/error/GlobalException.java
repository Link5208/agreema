package com.example.demo.util.error;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.demo.domain.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(value = {
			UsernameNotFoundException.class,
			BadCredentialsException.class,
			IdInvalidException.class
	})
	public ResponseEntity<RestResponse<Object>> handleIdException(Exception e) {
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(e.getMessage());
		res.setError("Exception occurs ...");
		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<RestResponse<Object>> handleUsernameException(
			MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();

		RestResponse<Object> res = new RestResponse<>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(ex.getBody().getDetail());

		List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
		res.setMessage(errors.size() > 1 ? errors : errors.get(0));
		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception e) {
		RestResponse<Object> response = new RestResponse<>();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setError("404 Not Found. URL may not exist...");
		response.setMessage(e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(value = IOException.class)
	public ResponseEntity<RestResponse<Object>> handleExportExcelException(Exception e) {
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(e.getMessage());
		res.setError("Export-excel exception occurs ...");
		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(value = StorageException.class)
	public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception e) {
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(e.getMessage());
		res.setError("Upload-file exception occurs ...");
		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestResponse<Object>> handleAllException(Exception e) {
		RestResponse<Object> response = new RestResponse<>();
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setMessage(e.getMessage());
		response.setError("Intermal Server Error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}