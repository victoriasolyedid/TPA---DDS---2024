//package ar.edu.utn.frba.dds.models.entities.repositories.mains;
//
//import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
//import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
//import ar.edu.utn.frba.dds.models.entities.repositories.UsuariosRepository;
//
//import javax.transaction.Transactional;
//
//public class MainUsuarios {
//
//    private UsuariosRepository usuariosRepositorio;
//
//    public static void main (String[] args){
//        MainUsuarios instance = new MainUsuarios();
//        //instance.usuariosRepositorio = UsuariosRepository.getInstancia();
//
//        instance.guardarUsuario();
//    }
//
//    @Transactional
//    public void guardarUsuario(){
//        Colaborador usuario1 = new Colaborador(TipoDocumento.DNI,27852369,"Juana","Perez","juanaperez@yahoo.com.ar");
//
//        this.usuariosRepositorio.guardar(usuario1);
//    }
//}
