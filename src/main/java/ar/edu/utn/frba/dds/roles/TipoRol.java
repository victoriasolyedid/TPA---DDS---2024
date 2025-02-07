package ar.edu.utn.frba.dds.roles;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    ADMIN, JURIDICA, HUMANA, INVITADO, TECNICO;
}