package ar.edu.utn.frba.dds.models.entities.tarjetas;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.TarjetaRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GestorDePermisos {

    private static ColaboradorRepository colaboradorRepository;
    private static TarjetaRepository tarjetaRepository;
    private static PermisosRepository permisosRepository;

    public static void revocarPermisos(Colaborador colaborador, Permiso permiso) {
        Optional<Tarjeta> tarjetaColaborador = tarjetaRepository.buscarTarjetaPorColaboradorId(colaborador.getId());

        if(tarjetaColaborador.isPresent()) {
            tarjetaColaborador.get().getPermisos().remove(permiso);
            tarjetaRepository.actualizar(tarjetaColaborador.get());
            colaborador.setTarjeta(tarjetaColaborador.get());
            colaboradorRepository.actualizar(colaborador);
        }
        System.out.println("Se revocaron los permisos para la tarjeta " + tarjetaColaborador + " del colaborador " + colaborador);

    }

    public static boolean compararHoras(LocalDateTime horaEspecifica, LocalDateTime horaActual, double horaLimite) {
        long horas = Duration.between(horaEspecifica, horaActual).toHours();
        return horas < horaLimite;
    }

    public static Optional<Permiso> validarPermisos(Stream<Permiso> permisosSobreLaHeladera) {
        return permisosSobreLaHeladera
                .filter(permiso -> compararHoras(permiso.getFecha(), LocalDateTime.now(), 3))
                .findAny();
    }


    public static Stream<Permiso> buscarPermisos(Colaborador colaborador, TipoDeColaboracion colaboracion) {
        Optional<Tarjeta> tarjetaColaborador = tarjetaRepository.buscarTarjetaPorColaboradorId(colaborador.getId());

        if (tarjetaColaborador.isEmpty()) {
            return Stream.empty();
        }

        List<Permiso> permisosSobreTarjeta = permisosRepository.buscarPermisosPorTarjetaId(tarjetaColaborador.get().getCodigo());
//        Heladera heladera = heladeraRepository
        return permisosSobreTarjeta.stream()
                .filter(permiso -> permiso.getHeladeraAsociada().equals(colaboracion.getHeladera()));
    }


    public static void agregarUso(Colaborador colaborador, TipoDeColaboracion colaboracion) {
        Optional<Tarjeta> tarjetaColaborador = tarjetaRepository.buscarTarjetaPorColaboradorId(colaborador.getId());

        if(tarjetaColaborador.isPresent()) {
            UsoTarjeta usoNuevo = new UsoTarjeta(colaboracion.getFechaColaboracion(), colaboracion.getHeladera());
            tarjetaColaborador.get().agregarUso(usoNuevo);
            tarjetaRepository.actualizar(tarjetaColaborador.get());
            colaborador.setTarjeta(tarjetaColaborador.get());
            colaboradorRepository.actualizar(colaborador);
            System.out.println("Se agregó el uso: Fecha - " + usoNuevo.getFechaDeUso() + ", Heladera - " + usoNuevo.getHeladeraDondeSeUso().getNombre());

        }


    }
}