package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import ru.otus.dao.ClientsDao;
import ru.otus.model.RequestPhoneModel;

@SuppressWarnings({"java:S1989"})
public class ClientsPhoneApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient ClientsDao clientsDao;
    private final transient Gson gson;

    public ClientsPhoneApiServlet(ClientsDao clientsDao, Gson gson) {
        this.clientsDao = clientsDao;
        this.gson = gson;
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
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
        var requestModel = gson.fromJson(sb.toString(), RequestPhoneModel.class);

        var clientOptional = clientsDao.deletePhone(requestModel.getClientId(), requestModel.getNumber());

        if (clientOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ServletOutputStream out = response.getOutputStream();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        var requestModel = gson.fromJson(sb.toString(), RequestPhoneModel.class);

        var clientOptional = clientsDao.addPhone(requestModel.getClientId(), requestModel.getNumber());

        if (clientOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ServletOutputStream out = response.getOutputStream();
        //        out.print(gson.toJson(clientOptional));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
