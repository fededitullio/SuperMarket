package it.dstech.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transazione {
	@Id
	@GeneratedValue
	
   private int idTransazione;
   private int idUser;
   private double prezzoFinale;
   private int codOrdine;
   private List<Product> product=new ArrayList<>();
   
   
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
public double getPrezzoFinale() {
	return prezzoFinale;
}
public void setPrezzoFinale(double prezzoFinale) {
	this.prezzoFinale = prezzoFinale;
}
public int getCodOrdine() {
	return codOrdine;
}
public void setCodOrdine(int codOrdine) {
	this.codOrdine = codOrdine;
}
public List<Product> getProduct() {
	return product;
}
public void setProduct(List<Product> product) {
	this.product = product;
}
@Override
public String toString() {
	return "Transazione [idTransazione=" + idTransazione + ", idUser=" + idUser + ", prezzoFinale=" + prezzoFinale
			+ ", codOrdine=" + codOrdine + ", product=" + product + "]";
}
   

}
