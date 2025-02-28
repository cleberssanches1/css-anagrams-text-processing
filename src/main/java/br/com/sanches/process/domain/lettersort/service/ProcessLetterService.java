package br.com.sanches.process.domain.lettersort.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.sanches.process.domain.lettersort.model.RequestDTO;
import br.com.sanches.process.domain.lettersort.model.ResponseDTO;
import br.com.sanches.process.infrastructure.util.Utils;

@Service
public class ProcessLetterService {

	public ResponseDTO retriveSortedLetters(RequestDTO letters) {
		ResponseDTO responseDTO = new ResponseDTO();

		Utils.validateOnlyLetters(letters.getLetters());

		List<String> sortedLetters = new ArrayList<>();

		String[] letterArray = letters.getLetters().toArray(new String[0]);

		generateSortedLetters(letterArray, 0, sortedLetters);

		responseDTO.setSortedLetters(sortedLetters);
		return responseDTO;
	}

	private void generateSortedLetters(String[] letters, int index, List<String> result) {
		if (index == letters.length - 1) {
			result.add(Utils.arrayToString(letters));
			return;
		}

		for (int i = index; i < letters.length; i++) {
			replacement(letters, i, index);
			generateSortedLetters(letters, index + 1, result);
			replacement(letters, i, index);
		}
	}

	private void replacement(String[] array, int i, int j) {
		String temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

}