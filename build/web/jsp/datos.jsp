<%-- 
    Document   : datos
    Created on : 10/12/2020, 04:47:20 PM
    Author     : Lenny
--%>

<%@page import="dto.Tipo"%>
<%@page import="dto.Sabor"%>
<%@page import="negocio.SistemaPizza"%>
<%@page import="dto.IngredienteAdicional"%>
<%@page import="java.util.List"%>
<%@page import="dao.Conexion"%>
<%@page import="dao.IngredienteAdicionalJpaController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet">
    <!-- Inter font -->
    <link rel="stylesheet" href="css/styles.css">
    <title>Pizzería La QQTEÑA</title>
</head>
<body class="antialised">
    <!-- Navbar -->
    <div class="flex inter pt-12 px-8 justify-between items-start">
        <a href="index.html" class="flex items-center">
            <img src="images/pizza.png" alt="pizza icon" class="w-12 h-12 rounded-full object-cover object-left-bottom">
            <p class="ml-2 font-semibold text-2xl tracking-tighter">Pizzería la QQTEÑA</p>
        </a>

        <div class="flex flex-col inter">
            <p class="tracking-tight font-medium">Desarrollado por:</p>
            <div class="flex items-center mt-2">
                <img class="w-10 h-10 rounded-full mr-4" src="images/pizza.png" alt="Avatar of Javier Contreras">
                <div class="text-sm">
                    <p class="leading-none font-medium">Javier Eduardo Contreras Castro</p>
                    <p class="text-gray-700 text-sm">Código: 1151828</p>
                </div>
            </div>
        </div>
    </div>
    <!-- ./ navbar -->
    
    <% SistemaPizza sistema = new SistemaPizza(); %>
    <!-- This example requires Tailwind CSS v2.0+ -->
<div class="flex flex-col p-12">
    <p class="font-semibold text-xl tracking-tighter text-center uppercase">Datos insertados en la base de datos</p>
    <p class="font-medium tracking-tighter uppercase mt-6">Ingredientes Adicionales</p>
    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8 mt-6">
      <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  id ingrediente
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  descripcion
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  valor del ingrediente adicional
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <%
                    List<IngredienteAdicional> adicionales = sistema.getAdicionales();
                    for(IngredienteAdicional dato : adicionales){ %>
              <tr>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getIdIngrediente() %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getDescripcion() %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getValor() %>
                </td>
              </tr> 
              <% } %>
  
              <!-- More rows... -->
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <p class="font-medium tracking-tighter uppercase mt-12">Sabores de Pizza</p>
    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8 mt-6">
      <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  id sabor
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  descripcion
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <% 
                    List<Sabor> listSabores = sistema.getSabores();
                    for(Sabor dato : listSabores) { %>
              <tr>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getIdSabor() %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getDescripcion() %>
                </td>
              </tr>
              <% } %>
              <!-- More rows... -->
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <p class="font-medium tracking-tighter uppercase mt-12">Tipos de Pizza</p>
    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8 mt-6">
      <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  id tipo
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  descripcion
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <% List<Tipo> listTipos = sistema.getTipos();
                for(Tipo dato : listTipos) { %>
              <tr>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getIdTipo() %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <%= dato.getDescripcion() %>
                </td>
              </tr>
              <% } %>
              <!-- More rows... -->
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div class="flex w-24 mx-auto pb-12">
      <a href="index.html" class="px-4 py-2 shadow rounded border bg-gray-50 inter">Regresar</a>
  </div>
  
</body>
</html>
