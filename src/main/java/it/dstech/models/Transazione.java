package it.dstech.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transazione {
	@Id
	@GeneratedValue
	private int idTransazione;
	private int idUser;
	private int codOrdine;

	@OneToMany(mappedBy = "transazione", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ProdottoAcquistato> product = new ArrayList<>();

	public int getIdTransazione() {
		return idTransazione;
	}

	public void setIdTransazione(int idTransazione) {
		this.idTransazione = idTransazione;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getCodOrdine() {
		return codOrdine;
	}

	public void setCodOrdine(int codOrdine) {
		this.codOrdine = codOrdine;
	}

	public List<ProdottoAcquistato> getProduct() {
		return product;
	}

	public void setProduct(List<ProdottoAcquistato> product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Transazione [idTransazione=" + idTransazione + ", idUser=" + idUser + ", codOrdine=" + codOrdine
				+ ", product=" + product + "]";
	}
	

}
