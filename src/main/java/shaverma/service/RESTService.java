package shaverma.service;

import com.sun.net.httpserver.*;
import org.json.JSONArray;
import org.json.JSONObject;
import shaverma.logic.AccessorRegistry;
import shaverma.logic.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

public class RESTService {
    private AccessorRegistry registry;
    private HttpServer server;

    public RESTService(AccessorRegistry registry) {
        this.registry = registry;
    }

    private AccessorRegistry getRegistry() {
        return this.registry;
    }

    public void run(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(new GetHandler());
        server.start();
        System.out.println("Server started");
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }

    private class GetHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            AccessorRegistry registry = getRegistry();
            Storage storage = registry.getStorage();
            JSONArray jsonArray = new JSONArray(
                    storage.getItems()
                            .stream()
                            .map(item -> (new JSONObject())
                                    .put("name", item.getComponent().getName())
                                    .put("amount", item.getAmount())
                                    .put("price", item.getPrice())
                            )
                            .collect(Collectors.toList())
            );
            String response = jsonArray.toString();
            Headers headers = httpExchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
