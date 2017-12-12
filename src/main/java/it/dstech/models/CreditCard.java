package it.dstech.models;

import java.time.LocalDate;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CreditCard {
	@Id
	@GeneratedValue
	private int id;
	private String numero;
	private String scadenza;
	private String ccv;
	private double credito;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	public CreditCard(String numero, String scadenza, String ccv, double credito, User user) {
		super();
		this.numero = numero;
		this.scadenza = scadenza;
		this.ccv = ccv;
		this.credito = credito;
		this.user = user;
	}
	
	public CreditCard() {
	}
	
	public void encryptNumero() {
		String encoded = new String(Base64.getEncoder().encode(this.numero.getBytes()));
		this.numero = encoded;
	}
	
	public String decryptNumero() {
		String decoded = new String(Base64.getDecoder().decode(this.numero));
		return decoded;
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
	public String getScadenza() {
		return scadenza;
	}
	public void setScadenza(String scadenza) {
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
