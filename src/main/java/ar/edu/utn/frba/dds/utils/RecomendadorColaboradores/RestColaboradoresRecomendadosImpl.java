package ar.edu.utn.frba.dds.models.entities.RecomendadorColaboradores;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.ColaboradorController;
import ar.edu.utn.frba.dds.dtos.outputs.ColaboradorDTO;
import ar.edu.utn.frba.dds.dtos.outputs.MedioDeContactoDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras.PuntosRecomendadosAPI;
import ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras.ServicioPuntosRecomendados;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//@Path("/colaboradores")
public class RestColaboradoresRecomendadosImpl  {
    private static RestColaboradoresRecomendadosImpl instancia;
    private ColaboradorRepository colaboradorRepository;
    private ColaboracionRepository colaboracionRepository;
    private Retrofit retrofit;

    public RestColaboradoresRecomendadosImpl() {
        this.colaboradorRepository = ServiceLocator.instanceOf(ColaboradorRepository.class);
        this.colaboracionRepository = ServiceLocator.instanceOf(ColaboracionRepository.class);

        this.retrofit = new Retrofit.Builder().
                baseUrl("http://localhost:8080").
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }

    public static RestColaboradoresRecomendadosImpl getInstance() throws IOException {
        if (instancia == null) {
            instancia = new RestColaboradoresRecomendadosImpl();
        }
        return instancia;
    }


    public List<Colaborador> createColaboradores() throws Exception {
        // Inicializa el repositorio de colaboradores

        List<Colaborador> colaboradores = colaboradorRepository.buscarTodos();

        // Convertir los colaboradores a DTOs
        List<ColaboradorDTO> colaboradoresDTO = colaboradores.stream().map(colaborador -> {
            List<MedioDeContactoDTO> medioDeContactoDTO = colaborador.getMedioDeContacto().stream().map(medioDeContacto -> {
                return MedioDeContactoDTO.builder().
                        tipoMedioDeContacto(medioDeContacto.getTipo().name()).
                        detalle(medioDeContacto.getValor()).build();
            }).toList();
            Long donaciones = colaboracionRepository.getTotalCantidadViandasUltimoMes(colaborador.getId());

            return ColaboradorDTO.builder()
                    .id(colaborador.getId())
                    .nroDocumento(colaborador.getDatosColaborador().getCamposCompletos().get("numero_documento"))
                    .tipoDocumento(colaborador.getDatosColaborador().getCamposCompletos().get("tipo_documento"))
                    .nombre(colaborador.getDatosColaborador().getCamposCompletos().get("nombre"))
                    .apellido(colaborador.getDatosColaborador().getCamposCompletos().get("apellido"))
                    .donaciones(donaciones.intValue())
                    .puntos(colaborador.getPuntosAcumulados())
                    .mediosDeContacto(medioDeContactoDTO).build();
        }).collect(Collectors.toList());

        // Crea el cliente para el servicio externo y envía los colaboradores
        RecomendadorColaboradorClient recomendadorColaboradorClient = this.retrofit.create(RecomendadorColaboradorClient.class);
        recomendadorColaboradorClient.createColaboradores(colaboradoresDTO).execute();

        return colaboradores;
    }

}
