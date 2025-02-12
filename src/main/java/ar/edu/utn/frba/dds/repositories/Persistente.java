package ar.edu.utn.frba.dds.models.repositories;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public class Persistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
