package ar.edu.utn.frba.dds.models.entities.usuario;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.roles.TipoRol;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "username", columnDefinition = "VARCHAR(125)")
    private String nombreUsuario;

    @Column(name = "password", columnDefinition = "VARCHAR(125)")
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoRol", nullable = false, columnDefinition = "VARCHAR(15)")
    private TipoRol rol;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "colaborador_id", referencedColumnName = "id", nullable = true)
    private Colaborador colaborador;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", referencedColumnName = "id", nullable = true)
    private Tecnico tecnico;

    // TODO: tendria que estar persistido en la base de datos :(
    @Transient
    private ArrayList<String> contraseniaAnterior = new ArrayList<>();

    public Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    public Usuario(String nombreUsuario, String contrasenia, TipoRol rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    // Para generar la base
    public Usuario(String nombreUsuario, String contrasenia, TipoRol rol, Colaborador colaborador, Tecnico tecnico) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.colaborador = colaborador;
        this.tecnico = tecnico;
    }

    public void agregarAlHistorial(String contrasenia) {
        this.contraseniaAnterior.add(contrasenia);
    }

    public String toString() {
        if (this.colaborador != null) {
            return this.colaborador.toStringNom();
        } else {
            return "Colaborador no asignado"; // O cualquier otro valor que prefieras
        }
    }
}