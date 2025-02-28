package br.com.sanches.process.infrastructure.exception;

public class InvalidCharacterException extends BusinessException {

	private static final String THERE_ARE_CHARACTERS_THAT_ARE_NOT_NUMBERS_IN_THE_REQUEST = "HÁ CARACTERES QUE NÃO SÃO NÚMEROS NA REQUISIÇÃO";

	private static final long serialVersionUID = 1L;

	public InvalidCharacterException() {
		super(THERE_ARE_CHARACTERS_THAT_ARE_NOT_NUMBERS_IN_THE_REQUEST);
	}

	public InvalidCharacterException(String message) {
		super(message);
	}

}