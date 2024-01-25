package com.cramcat.platform.CRUDapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.os.AsyncTask;
import okhttp3.*;

public class Api {

    public String loginAndGetUserId(String mail, String password) {
        String loginUrl = "http://10.1.105.65:8000/user/login";  // Cambiar la URL del endpoint de inicio de sesión

        try {
            URL url = new URL(loginUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                // Configurar la conexión para enviar datos y recibir datos JSON
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Crear los parámetros de la solicitud
                String urlParameters = "username=" + URLEncoder.encode(mail, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                // Enviar los datos de la solicitud
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(urlParameters.getBytes("UTF-8"));
                    os.flush();
                }

                // Obtener la respuesta del servidor
                int responseCode = connection.getResponseCode();
                System.out.println("Código de respuesta: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String userId = extractUserIdFromResponse(connection);
                    System.out.println("ID del usuario: " + userId);
                    return userId;
                } else {
                    // Manejar otros códigos de respuesta si es necesario
                    System.out.println("Inicio de sesión fallido");
                    return null;
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    private String extractUserIdFromResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Analizar la respuesta JSON y extraer el ID del usuario (ajustar según el formato de respuesta real)
            // Supongamos que la respuesta es un JSON como {"id": "123"}
            // Puedes usar una biblioteca como Gson para analizar el JSON de manera más robusta.
            return response.toString().trim().replace("{\"id\": \"", "").replace("\"}", "");
        }
    }

    private void handleException(IOException e) {
        // Manejar la excepción de manera más específica (lanzar, registrar, etc.)
        e.printStackTrace();
    }
    public void loginAsync(String mail, String password, ApiCallback callback) {
        try {
            String loginUrl = "http://10.1.105.65:8000/user/login";

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            String jsonBody = "{\"mail\":\"" + mail + "\", \"password\":\"" + password + "\"}";

            RequestBody body = RequestBody.create(jsonBody, mediaType);

            Request request = new Request.Builder()
                    .url(loginUrl)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callback != null) {
                        callback.onApiResult(null);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful() || responseBody == null) {
                            if (callback != null) {
                                callback.onApiResult(null);
                            }
                            return;
                        }

                        String result = responseBody.string();

                        if (callback != null) {
                            callback.onApiResult(result);
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (callback != null) {
                callback.onApiResult(null);
            }
        }
    }

    private static class LoginAsyncTask extends AsyncTask<String, Void, String> {
        private ApiCallback callback;

        public LoginAsyncTask(ApiCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String mail = params[0];
            String password = params[1];

            try {
                String loginUrl = "http://10.1.105.65:8000/user/login";
                String encodedUsername = URLEncoder.encode(mail, "UTF-8");
                String encodedPassword = URLEncoder.encode(password, "UTF-8");

                StringBuilder urlWithParams = new StringBuilder(loginUrl)
                        .append("?username=").append(encodedUsername)
                        .append("&password=").append(encodedPassword);

                URL obj = new URL(urlWithParams.toString());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                try {
                    con.setRequestMethod("GET");

                    int responseCode = con.getResponseCode();
                    System.out.println("Código de respuesta: " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();

                    System.out.println("Respuesta del servidor: " + response);

                    return response.toString();

                } finally {
                    con.disconnect();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                callback.onApiResult(result);
            }
        }
    }
    public interface ApiCallback {
        void onApiResult(String result);
    }
}