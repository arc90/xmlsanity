package com.arc90.xmlsanity.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	private final List<ValidationError> errors = new ArrayList<ValidationError>();

	public List<ValidationError> getErrors() {
		return errors;
	}

	public String getErrorsAsHtmlList() {
		return getErrorsAsHtmlList(null);
	}

	/**
	 * 
	 * @param classValue The value of the class attribute of the ol tag.
	 *     If null, the class attribute will not be specified.
	 * @return String containing an HTML ordered list.
	 */
	public String getErrorsAsHtmlList(String classValue) {
		StringBuilder sb = new StringBuilder();

		if (classValue != null) {
			sb.append("<ol class=");
			sb.append('"');
			sb.append(classValue);
			sb.append('"');
			sb.append(">\n");
		} else {
			sb.append("<ol>\n");
		}

		for (ValidationError error : errors) {
			sb.append("\t<li>");
			sb.append(error);
			sb.append("</li>\n");
		}

		sb.append("</ol>");

		return sb.toString();
	}

	public boolean isValid() {
		return errors.size() == 0;
	}

	@Override
	public String toString() {
		if (isValid()) {
			return "valid";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("invalid, because:\n");

		for (int i = 0; i < errors.size(); i++) {
			ValidationError error = errors.get(i);

			sb.append(i + 1);
			sb.append(". ");
			sb.append(error);
			sb.append("\n");
		}

		return sb.toString();
	}

	protected void addError(String error) {
		errors.add(new ValidationError(error));
	}

	protected void addError(ValidationError validationError) {
		errors.add(validationError);
	}

}
