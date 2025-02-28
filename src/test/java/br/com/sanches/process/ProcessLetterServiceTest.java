package br.com.sanches.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sanches.process.domain.lettersort.model.RequestDTO;
import br.com.sanches.process.domain.lettersort.model.ResponseDTO;
import br.com.sanches.process.domain.lettersort.service.ProcessLetterService;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class ProcessLetterServiceTest {

	private ProcessLetterService processLetterService;

	@BeforeEach
	void setUp() {
		processLetterService = new ProcessLetterService();
	}

	@Test
	void testRetriveSortedLettersValidInputTest() {
		RequestDTO request = new RequestDTO();
		request.setLetters(Arrays.asList("a", "b", "c"));

		ResponseDTO response = processLetterService.retriveSortedLetters(request);

		assertNotNull(response);
		assertEquals(6, response.getSortedLetters().size());
	}

	@Test
	void testRetriveSortedLettersInvalidInputTest() {
		RequestDTO request = new RequestDTO();
		request.setLetters(Arrays.asList("a", "1", "c"));

		assertThrows(Exception.class, () -> processLetterService.retriveSortedLetters(request));
	}
}
