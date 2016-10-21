/*
 * Copyright (c) 2016 Daniel Campodonico; Emiliano Gioria; Lucas Moretti.
 * This file is part of Lab05.
 *
 * Lab05 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lab05 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lab05.  If not, see <http://www.gnu.org/licenses/>.
 */

package dam.isi.frsf.utn.edu.ar.lab05.dao;

import org.json.JSONObject;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class ProyectoApiRest {

    public void crearProyecto(Proyecto p){

    }
    public void borrarProyecto(Integer id){

    }
    public void actualizarProyecto(Proyecto p){

    }
    public List<Proyecto> listarProyectos(){
        return null;
    }

    public Proyecto buscarProyecto(Integer id){
        RestClient cliRest = new RestClient();
        JSONObject t = cliRest.getById(1,"proyectos");
        // transformar el objeto JSON a proyecto y retornarlo
        return null;
    }

}
