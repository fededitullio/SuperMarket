package it.dstech.models;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
	@Entity
public class Product {
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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "PRODUCT_USER", joinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
	private List<User> user=new ArrayList<>();
	
	
	public Product() {
		super();
	}
	public Product(String nome, String marca, String dataScadenza, Category categoria, double quantitaDisponibile,
			double quantitaDaAcquistare, Unita unita, double prezzoUnitario,String img, int offerta) {
		super();
		this.nome = nome;
		this.marca = marca;
		this.dataScadenza = dataScadenza;
		this.categoria = categoria;
		this.quantitaDisponibile = quantitaDisponibile;
		this.quantitaDaAcquistare = quantitaDaAcquistare;
		this.unita = unita;
		this.prezzoUnitario = prezzoUnitario;
		this.prezzoIvato = prezzoUnitario*quantitaDaAcquistare;
		this.prezzoSenzaIva = prezzoIvato- prezzoIvato*22/100;
		this.img = img;
		this.offerta = offerta;
		this.prezzoScontato=prezzoIvato-prezzoIvato*offerta/100;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Category getCategoria() {
		return categoria;
	}
	public void setCategoria(Category categoria) {
		this.categoria = categoria;
	}
	public double getQuantitaDisponibile() {
		return quantitaDisponibile;
	}
	public void setQuantitaDisponibile(double quantitaDisponibile) {
		this.quantitaDisponibile = quantitaDisponibile;
	}
	public double getQuantitaDaAcquistare() {
		return quantitaDaAcquistare;
	}
	
	
	public void setQuantitaDaAcquistare(double quantitaDaAcquistare) {
		this.quantitaDaAcquistare = quantitaDaAcquistare;
	}
	public Unita getUnita() {
		return unita;
	}
	public void setUnità(Unita unita) {
		this.unita = unita;
	}
	public double getPrezzoUnitario() {
		return prezzoUnitario;
	}
	public void setPrezzoUnitario(double prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}
	public double getPrezzoSenzaIva() {
		return prezzoSenzaIva;
	}
	public void setPrezzoSenzaIva(double prezzoSenzaIva) {
		this.prezzoSenzaIva = prezzoSenzaIva;
	}
	public double getPrezzoIvato() {
		return prezzoIvato;
	}
	public void setPrezzoIvato(double prezzoIvato) {
		this.prezzoIvato = prezzoIvato;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getOfferta() {
		return offerta;
	}
	public void setOfferta(int offerta) {
		this.offerta = offerta;
	}
	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public double getPrezzoScontato() {
		return prezzoScontato;
	}
	public void setPrezzoScontato(double prezzoScontato) {
		this.prezzoScontato = prezzoScontato;
	}

	public void sconto() {
		this.prezzoScontato=prezzoIvato-prezzoIvato*offerta/100;
	}
	@Override
	public String toString() {
		return "Product [nome=" + nome + ", marca=" + marca + ", dataScadenza=" + dataScadenza + ", categoria="
				+ categoria + ", quantitaDisponibile=" + quantitaDisponibile + ", unità=" + unita + ", prezzoUnitario="
				+ prezzoUnitario + ", prezzoIvato=" + prezzoIvato + ", offerta=" + offerta + ", prezzoScontato="
				+ prezzoScontato + "]";
	}
	}
	
	