/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Lenny
 */
public class LeerJSON {

    public String getJSON(URL url) {
        // Creamos un StringBuilder para almacenar la respuesta del web service
        StringBuilder sb = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //indicamos por que verbo HTML ejecutaremos la solicitud
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                //si la respuesta del servidor es distinta al codigo 200 lanzaremos una Exception
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            conn.disconnect();
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }
}
