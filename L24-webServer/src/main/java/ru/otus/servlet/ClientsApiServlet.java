package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import ru.otus.dao.ClientsDao;
import ru.otus.model.*;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient ClientsDao clientsDao;
    private final transient Gson gson;

    public ClientsApiServlet(ClientsDao clientsDao, Gson gson) {
        this.clientsDao = clientsDao;
        this.gson = gson;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        var requestModel = gson.fromJson(sb.toString(), RequestCreateClientModel.class);

        var client = new Client(
                null,
                requestModel.getName(),
                new Address(null, requestModel.getAddress()),
                List.of(new Phone(null, requestModel.getNumber())));

        var clientOptional = clientsDao.createClient(client);

        if (clientOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ServletOutputStream out = response.getOutputStream();
    }
}
