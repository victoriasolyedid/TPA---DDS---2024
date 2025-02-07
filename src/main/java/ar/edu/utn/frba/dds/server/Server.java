package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.utils.EqHelper;
import ar.edu.utn.frba.dds.utils.JavalinRenderer;
import ar.edu.utn.frba.dds.utils.PrettyProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import java.io.IOException;
import java.util.function.Consumer;

public class Server {
    private static Javalin app = null;

    public static Javalin app() {
        if (app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    public static void init() {
        if (app == null) {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            int port = Integer.parseInt(PrettyProperties.getInstance().propertyFromName("server_port"));
            app = Javalin.create(config()).start(port);

            Router.init(app);

            if (Boolean.parseBoolean(PrettyProperties.getInstance().propertyFromName("dev_mode"))) {
                // El initializer es para ejectuar datos ni bien arranca el programa, ver que poner
                //Initializer.init();
            }
        }
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Hibernate5Module())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    private static Consumer<JavalinConfig> config() {
        return config -> {
            // Configura archivos estáticos, si tienes archivos como CSS o JS
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "src/main/resources/assets"; // Esta es la ruta para archivos estáticos
                staticFiles.location = io.javalin.http.staticfiles.Location.EXTERNAL;
            });

            config.fileRenderer(new JavalinRenderer().register("hbs", (path, model, context) -> {
                Handlebars handlebars = new Handlebars(new FileTemplateLoader("src/main/resources/templates/", ".hbs"));
                handlebars.registerHelper("eq", new EqHelper());
                handlebars.registerHelper("json", (context1, options) -> objectMapper.writeValueAsString(context1));
                handlebars.with(new FileTemplateLoader("src/main/resources/templates/", ".hbs"));
                Template template = null;
                try {
                    template = handlebars.compile("/" + path.replace(".hbs", ""));
                    return template.apply(model);
                } catch (IOException e) {
                    e.printStackTrace();
                    context.status(HttpStatus.NOT_FOUND);
                    return "No se encuentra la página indicada...";
                }
            }));
        };
    }
}