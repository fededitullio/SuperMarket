package it.dstech.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String username;
	
	private String password;
	
	private String telefono;
	
	private String cap;
	
	private String via;
	
	private String citta;
	
	private String provincia;
	
	@Enumerated(EnumType.STRING)
	private UserProfileType profileType;

	@JsonIgnore
	@OneToMany(mappedBy="user")
	private List<CreditCard> listCard= new ArrayList();
	@JsonIgnore
	@ManyToMany(mappedBy="user")
	private List<Product> listProduct= new ArrayList();
	@Enumerated(EnumType.STRING)
	private TipoUtente tipoUtente;
	
	
	
	
	
	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public UserProfileType getProfileType() {
		return profileType;
	}

	public void setProfileType(UserProfileType profileType) {
		this.profileType = profileType;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public List<CreditCard> getListCard() {
		return listCard;
	}

	public void setListCard(List<CreditCard> listCard) {
		this.listCard = listCard;
	}

	public List<Product> getListProduct() {
		return listProduct;
	}

	public void setListProduct(List<Product> listProduct) {
		this.listProduct = listProduct;
	}

	public User() {
		this.listProduct = new ArrayList<Product>();
		this.listCard = new ArrayList<CreditCard>();
		
	}
	
	
}