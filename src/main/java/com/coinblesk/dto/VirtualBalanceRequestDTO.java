package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VirtualBalanceRequestDTO {

	@NotNull
	private final String publicKey;

}
