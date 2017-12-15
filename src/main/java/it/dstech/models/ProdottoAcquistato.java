package it.dstech.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class ProdottoAcquistato {
	@Id
	@GeneratedValue
	private int id;
	private String nome;
	private String marca;
	private String dataScadenza;
	private Category categoria;
	private double quantitaDisponibile;
	private double quantitaDaAcquistare;
	private Unita unita;
	private double prezzoUnitario;
	private double prezzoSenzaIva;
	private double prezzoIvato;
	private String img;
	private int offerta;
	private double prezzoScontato;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANS_ID", referencedColumnName = "idTransazione")
	private Transazione transazione;
	
	
	
	public ProdottoAcquistato() {
		super();
	}
	@Override
	public String toString() {
		return "ProdottoAcquistato [nome=" + nome + ", marca=" + marca + ", quantitaDisponibile=" + quantitaDisponibile
				+ ", quantitaDaAcquistare=" + quantitaDaAcquistare + ", prezzoUnitario=" + prezzoUnitario
				+ ", prezzoSenzaIva=" + prezzoSenzaIva + ", prezzoIvato=" + prezzoIvato + ", offerta=" + offerta
				+ ", prezzoScontato=" + prezzoScontato + "]";
	}



	public ProdottoAcquistato(String nome, String marca, String dataScadenza, Category categoria,
			double quantitaDisponibile, double quantitaDaAcquistare, Unita unita, double prezzoUnitario,
			double prezzoSenzaIva, double prezzoIvato, String img, int offerta, double prezzoScontato) {
		super();
		this.nome = nome;
		this.marca = marca;
		this.dataScadenza = dataScadenza;
		this.categoria = categoria;
		this.quantitaDisponibile = quantitaDisponibile;
		this.quantitaDaAcquistare = quantitaDaAcquistare;
		this.unita = unita;
		this.prezzoUnitario = prezzoUnitario;
		this.prezzoSenzaIva = prezzoSenzaIva;
		this.prezzoIvato = prezzoIvato;
		this.img = img;
		this.offerta = offerta;
		this.prezzoScontato = prezzoScontato;
		
	}



	public Transazione getTransazione() {
		return transazione;
	}



	public void setTransazione(Transazione transazione) {
		this.transazione = transazione;
	}
	
	
	
	
}
