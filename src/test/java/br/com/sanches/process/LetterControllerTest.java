package br.com.sanches.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.sanches.process.domain.lettersort.controller.LetterSortController;
import br.com.sanches.process.domain.lettersort.model.RequestDTO;
import br.com.sanches.process.domain.lettersort.model.ResponseDTO;
import br.com.sanches.process.domain.lettersort.service.ProcessLetterService;
import br.com.sanches.process.infrastructure.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class LetterControllerTest {
	
	@Mock
	private ProcessLetterService processLetterService;

	@InjectMocks
	private LetterSortController letterSortController;

	private RequestDTO request;
	private ResponseDTO response;

	@BeforeEach
	void setUp() {
		request = new RequestDTO();
		request.setLetters(Arrays.asList("a", "b", "c"));

		response = new ResponseDTO();
		response.setSortedLetters(List.of("abc", "acb", "bac", "bca", "cab", "cba"));
	}

	@Test
    void testGetPreviewBonusReportSuccessTest() throws BusinessException {
        when(processLetterService.retriveSortedLetters(request)).thenReturn(response);

        ResponseEntity<ResponseDTO> responseEntity = letterSortController.getPreviewBonusReport(request);

        assertNotNull(responseEntity);       
        assertEquals(6, responseEntity.getBody().getSortedLetters().size());
    }
}
