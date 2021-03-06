package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class VirtualPaymentRequestDTO {
	@NotNull
	private final String fromPublicKey;
	@NotNull
	private final String toPublicKey;
	@NotNull
	private final Long amount;
	private final long nonce;
}
