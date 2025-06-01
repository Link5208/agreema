package com.example.demo.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.demo.domain.response.RestResponse;
import com.example.demo.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@Nullable
	public Object beforeBodyWrite(
			@Nullable Object body,
			MethodParameter returnType,
			MediaType selectedContentType,
			Class selectedConverterType,
			ServerHttpRequest request,
			ServerHttpResponse response) {

		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = servletResponse.getStatus();
		RestResponse<Object> res = new RestResponse<>();
		res.setStatusCode(status);

		if (body instanceof String || body instanceof Resource) {
			return body;
		}

		String path = request.getURI().getPath();
		if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")
				|| path.startsWith("/api/v1/contracts/export-to-excel")) {
			return body;
		}

		if (status >= 400) {
			// case error
			return body;
		} else {
			// case success
			res.setData(body);
			ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
			res.setMessage(message != null ? message.value() : "CALL API SUCCESS");
		}
		return res;
	}

}
