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

import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

import static dam.isi.frsf.utn.edu.ar.lab05.dao.RestClient.IP_SERVER;
import static dam.isi.frsf.utn.edu.ar.lab05.dao.RestClient.PORT_SERVER;

public class ListarProyectosTask extends AsyncTask<String, Void, List<Proyecto>> {
		private BusquedaProyectosListener<Proyecto> listener;

		public ListarProyectosTask(BusquedaProyectosListener<Proyecto> dListener){
			this.listener = dListener;
		}

		@Override
		protected  void onPreExecute(){
			listener.busquedaIniciada() ;
		}

		@Override
		protected void onPostExecute(List<Proyecto> proyectos) {
			listener.busquedaFinalizada(proyectos);
		}

		@Override
		protected List<Proyecto> doInBackground(String... urls) {
			final StringBuilder sb = new StringBuilder();
			final ArrayList<Proyecto> proyectos = new ArrayList<>();
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL("http://" + IP_SERVER + ":" + PORT_SERVER + "/proyectos/");
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
					Log.d("JSON", "doInBackground: "+ sb.toString());
					JSONArray jsonArray = new JSONArray(sb.toString());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject p = jsonArray.getJSONObject(i);
						Integer id = p.getInt("id");
						String nombre = p.getString("nombre");
						Proyecto proyecto = new Proyecto(id, nombre);
						proyectos.add(proyecto);
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
		return proyectos;
		}
	}