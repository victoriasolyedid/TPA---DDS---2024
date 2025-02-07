package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.AuthorizationState;
import it.tdlight.jni.TdApi.CreatePrivateChat;
import it.tdlight.jni.TdApi.FormattedText;
import it.tdlight.jni.TdApi.InputMessageText;
import it.tdlight.jni.TdApi.Message;
import it.tdlight.jni.TdApi.MessageContent;
import it.tdlight.jni.TdApi.MessageSenderUser;
import it.tdlight.jni.TdApi.SendMessage;
import it.tdlight.jni.TdApi.TextEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ITelegramSender implements AdapterTelegram {

    private final long adminId = 5032464197L;
    private final String apiToken = "2455b66daf1372f4f5353274181f286e";
    private final int apiId = 25246082;
    private final String botToken = "6841954922:AAHglYPiPXesfPzYp1NxlvioQ9rY2Y9l0RI";
    private final Path sessionPath = Paths.get("tdlight-session-id5032464197");

    public ITelegramSender() {
        try {
            // Inicializa la librería TDLight
            Init.init();

            // Seteamos el manejador de logs
            Log.setLogMessageHandler(1, new Slf4JLogMessageHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enviarMensajeA(Object a, String mensaje, String valor) {
        String userIDString =  valor;
        long userId = Long.parseLong(userIDString);
        try {
            enviarMensaje(userId, mensaje);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions internally
        }
    }

    private void enviarMensaje(long userId, String mensaje) throws Exception {
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
            // Obtenemos el token de la API
            var apiToken = new APIToken(apiId, this.apiToken);

            // Configuramos el cliente
            TDLibSettings settings = TDLibSettings.create(apiToken);
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            // Preparamos un nuevo cliente
            SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);

            // Configuramos la autenticación
            SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.bot(botToken);

            // Creamos una nueva instancia de la aplicación
            try (var app = new ExampleApp(clientBuilder, authenticationData, adminId)) {
                TdApi.User me = app.getClient().getMeAsync().get(1, TimeUnit.MINUTES);

                // Obtener información del usuario antes de crear el chat privado
                var user = app.getClient().send(new TdApi.GetUser(userId)).get(1, TimeUnit.MINUTES);
                if (user != null) {
                    // Crear el chat privado con tu user ID
                    var myChat = app.getClient().send(new CreatePrivateChat(userId, true)).get(1, TimeUnit.MINUTES);

                    // Enviar el mensaje
                    var req = new SendMessage();
                    req.chatId = myChat.id;
                    var txt = new InputMessageText();
                    txt.text = new FormattedText(mensaje, new TextEntity[0]);
                    req.inputMessageContent = txt;
                    Message result = app.getClient().sendMessage(req, true).get(1, TimeUnit.MINUTES);
                    System.out.println("Sent message: " + ((TdApi.MessageText) result.content).text.text);                }
            }
        }
    }
    // La clase ExampleApp es una clase interna que maneja los eventos de la aplicación.
    // Tiene manejadores de eventos para cuando el estado de autorización cambia, cuando se recibe un nuevo mensaje
    // y cuando se recibe el comando "stop". También tiene un método isAdmin que verifica si el remitente de un
    // mensaje es el administrador.
    private static class ExampleApp implements AutoCloseable {
        private final SimpleTelegramClient client;

        private final long adminId;

        public ExampleApp(SimpleTelegramClientBuilder clientBuilder,
                          SimpleAuthenticationSupplier<?> authenticationData,
                          long adminId) {
            this.adminId = adminId;
        // Manejadores de eventos
            clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);
            clientBuilder.addCommandHandler("stop", this::onStopCommand);
            clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

            this.client = clientBuilder.build(authenticationData);
        }

        @Override
        public void close() throws Exception {
            client.close();
        }

        public SimpleTelegramClient getClient() {
            return client;
        }
        //
        private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
            AuthorizationState authorizationState = update.authorizationState;
            if (authorizationState instanceof TdApi.AuthorizationStateReady) {
                System.out.println("Logged in");
            } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
                System.out.println("Closing...");
            } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
                System.out.println("Closed");
            } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
                System.out.println("Logging out...");
            }
        }
        // Manejador de eventos para nuevos mensajes
        private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
            MessageContent messageContent = update.message.content;
            String text;
            if (messageContent instanceof TdApi.MessageText messageText) {
                text = messageText.text.text;
            } else {
                text = String.format("(%s)", messageContent.getClass().getSimpleName());
            }

            long chatId = update.message.chatId;
            client.send(new TdApi.GetChat(chatId))
                    .whenCompleteAsync((chatIdResult, error) -> {
                        if (error != null) {
                            System.err.printf("Can't get chat title of chat %s%n", chatId);
                            error.printStackTrace(System.err);
                        } else {
                            String title = chatIdResult.title;
                            System.out.printf("Received new message from chat %s (%s): %s%n", title, chatId, text);
                        }
                    });
        }

        private void onStopCommand(TdApi.Chat chat, TdApi.MessageSender commandSender, String arguments) {
            if (isAdmin(commandSender)) {
                System.out.println("Received stop command. closing...");
                client.sendClose();
            }
        }

        private boolean isAdmin(TdApi.MessageSender sender) {
            if (sender instanceof MessageSenderUser messageSenderUser) {
                return messageSenderUser.userId == adminId;
            } else {
                return false;
            }
        }
    }
}
