package com.mycompany.myapp.web.rest.errors;

import java.net.URI;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Generic web application exception. Immutable.
 * */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed"})
public class Error extends RuntimeException {

	private static final long serialVersionUID = 697793250324190441L;
	
	private URI type;
	private String title;
	private HttpStatus status;
	private String detail;
	private String code;
	private Map<String, String> other;

	private Error(Builder builder) {
		this.type = builder.type;
		this.title = builder.title;
		this.status = builder.status;
		this.detail = builder.detail;
		this.code = builder.code;
		this.other = builder.other;
	}
	
	public URI getType() {
		return type;
	}
	public String getTitle() {
		return title;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public String getDetail() {
		return detail;
	}
	public String getCode() {
		return code;
	}
	public Map<String, String> getOther() {
		return other;
	}
	
	/**
	 * Creates builder to build {@link Error}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

    public static Error validationError(String path, Errors errors) {
        return new Builder().withStatus(HttpStatus.BAD_REQUEST)
            .withCode(String.join(", ", errors
                .getAllErrors()
                .stream().map(ObjectError::getCode)
                .collect(Collectors.toList())))
            .withDetail(String.join(", ", errors
                .getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList())))
            .withTitle("Validation error")
            .withType(URI.create(path))
            .build();
    }
	
	/**
	 * Builder to build {@link Error}.
	 */
	public static final class Builder {
		private URI type;
		private String title;
		private HttpStatus status;
		private String detail;
		private String code;
		private Map<String, String> other;

		private Builder() {
		}

		public Builder withType(URI type) {
			this.type = type;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withStatus(HttpStatus status) {
			this.status = status;
			return this;
		}

		public Builder withDetail(String detail) {
			this.detail = detail;
			return this;
		}

		public Builder withCode(String code) {
			this.code = code;
			return this;
		}

		public Builder with(String key, String value) {
			
			if (this.other == null) {
				this.other = new HashMap<>();
			}
			
			this.other.put(key, value);
			return this;
		}

		public Error build() {
			return new Error(this);
		}


	}
	
	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Error [type=");
		builder2.append(type);
		builder2.append(", title=");
		builder2.append(title);
		builder2.append(", status=");
		builder2.append(status);
		builder2.append(", detail=");
		builder2.append(detail);
		builder2.append(", code=");
		builder2.append(code);
		builder2.append(", other=");
		builder2.append(other);
		builder2.append("]");
		return builder2.toString();
	}
	
	
	
}
