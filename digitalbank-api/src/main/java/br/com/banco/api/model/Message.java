package br.com.banco.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Message {
	
	private String message;

	public Message(String message) {
		super();
		this.message = message;
	}

}
