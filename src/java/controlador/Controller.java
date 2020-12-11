/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.Conexion;
import dao.IngredienteAdicionalJpaController;
import dao.SaborJpaController;
import dao.TipoJpaController;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import negocio.LeerJSON;
import negocio.SistemaPizza;

/**
 *
 * @author Lenny
 */
public class Controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("accion");
        if(action.equalsIgnoreCase("registrarURL")){
            String url = request.getParameter("urlInput");
            try{
                LeerJSON leerJSON = new LeerJSON();
                //en la cadena output almacenamos toda la respuesta del servidor
                String output = leerJSON.getJSON(new URL(url));
                
                JsonObject obj = JsonParser.parseString(output).getAsJsonObject();
                JsonArray pizzasArray = obj.getAsJsonArray("pizzas"); // Obtener el array completo de pizzas
                JsonArray adicionalesArray = obj.getAsJsonArray("adicional"); // Obtener el array completo de adicionales
                
                // Saber cuantos datos hay registrados en la tabla actual
                int totalRows = getAdicionalesRows();

                // Test de inserción - Ingredientes adicionales --> * Working *
                SistemaPizza sistema = new SistemaPizza();
                for(JsonElement element : adicionalesArray){
                    JsonObject object = element.getAsJsonObject();
                    String descripcion = object.get("nombre_ingrediente").getAsString();
                    double valor = object.get("valor").getAsDouble();
                    sistema.agregarIngredienteAdicional(totalRows++, descripcion, valor);
                }

                // Test de inserción - Sabores
                int saboresRows = getSaboresRows();
                for(JsonElement element : pizzasArray){
                    JsonObject object = element.getAsJsonObject();
                    String descripcion = object.get("sabor").getAsString();
                    sistema.agregarSabor(saboresRows++, descripcion);
                }

                // Test de inserción - Tipos
                int tiposRows = getTiposRows();
                JsonElement e = pizzasArray.get(0);
                JsonObject o = e.getAsJsonObject();
                JsonArray arr = o.getAsJsonArray("precio");
                for(JsonElement element : arr){
                    JsonObject ob = element.getAsJsonObject();
                    String tamano = ob.get("tamano").getAsString();
                    sistema.agregarTipo(tiposRows++, tamano);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            
            request.getRequestDispatcher("jsp/datos.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    static int getAdicionalesRows(){ // Contar la cantidad de registros en la tabla de Ingredientes Adicionales
        Conexion conn = Conexion.getConexion();
        IngredienteAdicionalJpaController ing = new IngredienteAdicionalJpaController(conn.getBd());
        return ing.getIngredienteAdicionalCount() + 1;
    }
    
    static int getSaboresRows(){ // Contar la cantidad de registros en la tabla de Sabores
        Conexion conn = Conexion.getConexion();
        SaborJpaController saborDAO = new SaborJpaController(conn.getBd());
        return saborDAO.getSaborCount() + 1;
    }
    
    static int getTiposRows(){ // Contar la cantidad de registros en la tabla de Tipos
        Conexion conn = Conexion.getConexion();
        TipoJpaController tipoDAO = new TipoJpaController(conn.getBd());
        return tipoDAO.getTipoCount() + 1;
    }

    /*public static void main(String[] args) throws Exception {
        try{
            LeerJSON leerJSON = new LeerJSON();
            //en la cadena output almacenamos toda la respuesta del servidor
            String output = leerJSON.getJSON(new URL("https://raw.githubusercontent.com/madarme/persistencia/main/pizza.json"));
            //convertimos la cadena a JSON a traves de la libreria GSON
            JsonObject json = new Gson().fromJson(output, JsonObject.class);
            //imprimimos como Json
            System.out.println("salida como JSON" +  json);
            //imprimimos como String
            System.out.println("salida como String : " +output);

            JsonObject obj = JsonParser.parseString(output).getAsJsonObject();
            JsonArray pizzasArray = obj.getAsJsonArray("pizzas"); // Obtener el array completo de pizzas
            JsonArray adicionalesArray = obj.getAsJsonArray("adicional"); // Obtener el array completo de adicionales

            // Saber cuantos datos hay registrados en la tabla actual
            int totalRows = getAdicionalesRows();

            // Test de inserción - Ingredientes adicionales --> * Working *
            SistemaPizza sistema = new SistemaPizza();
            for(JsonElement element : adicionalesArray){
                JsonObject object = element.getAsJsonObject();
                String descripcion = object.get("nombre_ingrediente").getAsString();
                double valor = object.get("valor").getAsDouble();
                sistema.agregarIngredienteAdicional(totalRows++, descripcion, valor);
            }

            // Test de inserción - Sabores
            int saboresRows = getSaboresRows();
            for(JsonElement element : pizzasArray){
                JsonObject object = element.getAsJsonObject();
                String descripcion = object.get("sabor").getAsString();
                sistema.agregarSabor(saboresRows++, descripcion);
            }

            // Test de inserción - Tipos
            int tiposRows = getTiposRows();
            JsonElement e = pizzasArray.get(0);
            JsonObject o = e.getAsJsonObject();
            JsonArray arr = o.getAsJsonArray("precio");
            for(JsonElement element : arr){
                JsonObject ob = element.getAsJsonObject();
                String tamano = ob.get("tamano").getAsString();
                sistema.agregarTipo(tiposRows++, tamano);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }*/
}
