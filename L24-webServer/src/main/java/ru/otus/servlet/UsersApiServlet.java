package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.SystemException;
import java.io.BufferedReader;
import java.io.IOException;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

@SuppressWarnings({"java:S1989"})
public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient UserDao userDao;
    private final transient Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userDao.findById(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
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
        User user = gson.fromJson(sb.toString(), User.class);

        try {
            userDao.saveUser(user);
        } catch (SystemException e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
        }

        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
