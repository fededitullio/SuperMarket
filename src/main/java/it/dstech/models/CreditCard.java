package it.dstech.models;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class CreditCard {
	@Id
	@GeneratedValue
	int id;
	String numero;
	LocalDate scadenza;
	String ccv;
	double credito;
	
	@ManyToOne
	private User user;
	
	public CreditCard(String numero, LocalDate scadenza, String ccv, double credito, User user) {
		super();
		this.numero = numero;
		this.scadenza = scadenza;
		this.ccv = ccv;
		this.credito = credito;
		this.user = user;
	}
	
	public CreditCard() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public LocalDate getScadenza() {
		return scadenza;
	}
	public void setScadenza(LocalDate scadenza) {
		this.scadenza = scadenza;
	}
	public String getCcv() {
		return ccv;
	}
	public void setCcv(String ccv) {
		this.ccv = ccv;
	}
	public double getCredito() {
		return credito;
	}
	public void setCredito(double credito) {
		this.credito = credito;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CreditCard [id=" + id + ", numero=" + numero + ", scadenza=" + scadenza + ", ccv=" + ccv + ", credito="
				+ credito + ", user=" + user + "]";
	}
	
	
}
