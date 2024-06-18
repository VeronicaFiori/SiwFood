/*package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cuoco {

	  @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

		private String nome;
		private String cognome;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate dataNascita;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getCognome() {
			return cognome;
		}

		public void setCognome(String cognome) {
			this.cognome = cognome;
		}

		public LocalDate getDataNascita() {
			return dataNascita;
		}

		public void setDataNascita(LocalDate dataNascita) {
			this.dataNascita = dataNascita;
		}

		@Override
		public int hashCode() {
			return Objects.hash(cognome, dataNascita, nome);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cuoco other = (Cuoco) obj;
			return Objects.equals(cognome, other.cognome) && Objects.equals(dataNascita, other.dataNascita)
					&& Objects.equals(nome, other.nome);
		}
		
		
}*/
