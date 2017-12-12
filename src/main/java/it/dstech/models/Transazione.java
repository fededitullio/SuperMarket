package it.dstech.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Transazione {   
	@Id
	@GeneratedValue
	private int idTransazione;
	private int idUser;
	private int codOrdine;
	@Transient
	private List<Product> product=new ArrayList();
   
   
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
public List<Product> getProduct() {
	return product;
}
public void setProduct(List<Product> product) {
	this.product = product;
}

   

}
