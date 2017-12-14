package it.dstech.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StoricoOfferte {
	@Id
	@GeneratedValue
	int id;
	LocalDate dataOfferta;
	
	public StoricoOfferte() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getDataOfferta() {
		return dataOfferta;
	}
	public void setDataOfferta(LocalDate dataOfferta) {
		this.dataOfferta = dataOfferta;
	}
	@Override
	public String toString() {
		return "StoricoOfferte [id=" + id + ", dataOfferta=" + dataOfferta + "]";
	}

}
