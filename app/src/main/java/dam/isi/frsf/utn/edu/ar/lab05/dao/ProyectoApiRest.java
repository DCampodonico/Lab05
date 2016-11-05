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

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

import static dam.isi.frsf.utn.edu.ar.lab05.dao.RestClient.IP_SERVER;
import static dam.isi.frsf.utn.edu.ar.lab05.dao.RestClient.PORT_SERVER;

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

	public void actualizarProyecto(Proyecto proyecto){
		JSONObject nuevoProyecto = new JSONObject();
		try {
			nuevoProyecto.put("id", proyecto.getId());
			nuevoProyecto.put("nombre", proyecto.getNombre());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		restClient.actualizar(nuevoProyecto, "proyectos", proyecto.getId());
	}

	public void verTareas(final Integer proyectoId){
		final Runnable r = new Runnable() {
			public void run() {
				final StringBuilder sb = new StringBuilder();
				HttpURLConnection urlConnection = null;
				try {
					URL url = new URL("http://" + IP_SERVER + ":" + PORT_SERVER + "/tareas/");
					urlConnection = (HttpURLConnection) url.openConnection();
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					InputStreamReader isw = new InputStreamReader(in);
					int data = isw.read();
					while (data != -1) {
						char current = (char) data;
						sb.append(current);
						data = isw.read();
					}
					try {
						JSONArray jsonArray = new JSONArray(sb.toString());
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject p = jsonArray.getJSONObject(i);
							Integer id = p.getInt("proyectoId");
							if(proyectoId.equals(id)){
								Log.d("TAREAS", p.toString(4));						}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					if (urlConnection != null) urlConnection.disconnect();
				}
			}
		};
		new Thread(r).start();
	}

	public Proyecto buscarProyecto(Integer id){
		JSONObject t = restClient.getById(1,"proyectos");
		// transformar el objeto JSON a proyecto y retornarlo
		return null;
	}
}
