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

import org.json.JSONException;
import org.json.JSONObject;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class ProyectoApiRest {
	private RestClient restClient = new RestClient();

	public void crearProyecto(Proyecto proyecto){
		JSONObject nuevoProyecto = new JSONObject();
		try {
			nuevoProyecto.put("id", proyecto.getId());
			nuevoProyecto.put("nombre", proyecto.getNombre());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		restClient.crear(nuevoProyecto, "proyectos");
	}

	public void crearUsuario(Usuario usuario){
		JSONObject nuevoUsuario= new JSONObject();
		try {
			nuevoUsuario.put("id",usuario.getId());
			nuevoUsuario.put("nombre",usuario.getNombre());
			nuevoUsuario.put("correoElectronico", usuario.getCorreoElectronico());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		restClient.crear(nuevoUsuario, "usuarios");
	}

	public void borrarProyecto(Integer id){
		restClient.borrar(id, "proyectos");
	}
	public void actualizarProyecto(Proyecto p){

	}

	public Proyecto buscarProyecto(Integer id){
		JSONObject t = restClient.getById(1,"proyectos");
		// transformar el objeto JSON a proyecto y retornarlo
		return null;
	}
}
