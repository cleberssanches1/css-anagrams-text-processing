package br.com.sanches.process.domain.lettersort.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sanches.process.domain.lettersort.model.RequestDTO;
import br.com.sanches.process.domain.lettersort.model.ResponseDTO;
import br.com.sanches.process.domain.lettersort.service.ProcessLetterService;
import br.com.sanches.process.infrastructure.exception.BusinessException;

@RestController
@RequestMapping("/letterSort")
public class LetterSortController {

	private ProcessLetterService processLetterService;

	@Autowired
	public LetterSortController(ProcessLetterService processLetterService) {
		this.processLetterService = processLetterService;
	}

	@PostMapping("/possibilities")
	public ResponseEntity<ResponseDTO> getPreviewBonusReport(@RequestBody  final RequestDTO request)
			throws BusinessException {

		return ResponseEntity.ok().body(processLetterService.retriveSortedLetters(request));
	}

}