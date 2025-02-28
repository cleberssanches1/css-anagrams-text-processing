package br.com.sanches.process.infrastructure.exception.dto;
 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import net.minidev.json.JSONObject;

@Data
public class MessageExceptionHandler {

    private Integer status;	
	private String message;
	private LocalDateTime timestamp;
	private List<JSONObject> erros = new ArrayList<>();
	
	public MessageExceptionHandler(LocalDateTime dataHora, Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = dataHora;
	}

	public MessageExceptionHandler(LocalDateTime dataHora, String message) {
		super();
		this.message = message;
		this.timestamp = dataHora;
	}
	
	public MessageExceptionHandler() {
		super();
	}
	 
}