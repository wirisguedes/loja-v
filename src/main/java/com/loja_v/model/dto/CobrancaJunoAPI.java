package com.loja_v.model.dto;

import java.io.Serializable;

public class CobrancaJunoAPI implements Serializable {

	private static final long serialVersionUID = 1L;

	private ChargeDTO chargeDTO = new ChargeDTO();

	private Billing billing = new Billing();

	public ChargeDTO getChargeDTO() {
		return chargeDTO;
	}

	public void setCharge(ChargeDTO charge) {
		this.chargeDTO = charge;
	}

	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

}
