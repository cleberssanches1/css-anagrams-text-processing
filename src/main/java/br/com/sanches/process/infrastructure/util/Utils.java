package br.com.sanches.process.infrastructure.util;

import java.util.List;
import java.util.Objects;

import br.com.sanches.process.infrastructure.exception.InvalidCharacterException;

public class Utils {

	private static final String THE_NUMBER_MUST_NOT_BE_NEGATIVE = "O número não deve ser negativo.";
	private static final String INVALID_REPORTED_VALUES = "Valores informados inválidos";
	private static final String ALLOWED_LETTERS_REGEX = "^[a-zA-Z]+$";

	private Utils() {

	}

	public static void validateOnlyLetters(List<String> values) {
		for (String value : values) {
			if (!value.matches(ALLOWED_LETTERS_REGEX)) {
				throw new InvalidCharacterException();
			}
		}
	}

	public static void validateNullValue(List<String> values) {
		for (String value : values) {
			if (Objects.isNull(value) || value.isBlank()) {
				throw new InvalidCharacterException(INVALID_REPORTED_VALUES);
			}
		}
	}

	public static int calcularFatorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(THE_NUMBER_MUST_NOT_BE_NEGATIVE);
		}
		return (n == 0 || n == 1) ? 1 : n * calcularFatorial(n - 1);
	}

	public static String arrayToString(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String s : array) {
			sb.append(s);
		}
		return sb.toString();
	}
}