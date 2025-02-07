package ar.edu.utn.frba.dds.models.entities.tarjetas;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.PersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tarjeta")
public class Tarjeta implements Serializable {

    @Id
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "fechaVencimiento", columnDefinition = "DATE")
    private LocalDate fechaVencimiento;

    @OneToOne(mappedBy = "tarjeta")
    private Colaborador colaboradorAsociado;

    @OneToOne(mappedBy = "tarjeta")
    private PersonaSitVulnerable personaEnSitVulnerableAsociada;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsoTarjeta> usos;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permiso> permisos;

    public Tarjeta(Colaborador colaboradorAsociado) {
        this.colaboradorAsociado = colaboradorAsociado;
        this.permisos = new ArrayList<>();
        this.usos = new ArrayList<>();
        this.codigo = 10_000_000_000_000_000L + new SecureRandom().nextLong(90_000_000_000_000_000L);
        this.fechaVencimiento = LocalDate.now().plusYears(3);
    }

    public void agregarUso(UsoTarjeta uso){
        usos.add(uso);
    }
}