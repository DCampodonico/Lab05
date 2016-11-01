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

package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

/**
 * Created by daniel on 31/10/16.
 */
public class ProyectoAdapter extends ArrayAdapter<Proyecto>{

	private LayoutInflater inflater;
	private Context context;
	private List<Proyecto> proyectos;
	public ProyectoAdapter(Context context, List<Proyecto> items) {
		super(context, R.layout.fila_proyecto, items);
		proyectos = items;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) convertView = inflater.inflate(R.layout.fila_proyecto, parent, false);
		TextView textViewProyectoNombre = (TextView) convertView.findViewById(R.id.textViewProyectoNombre);
		textViewProyectoNombre.setText(this.getItem(position).getNombre());

		final Button buttonProyectoVerTareas = (Button) convertView.findViewById(R.id.buttonProyectoVerTareas);
		final Button buttonProyectoEditar = (Button) convertView.findViewById(R.id.buttonProyectoEditar);
		final Button buttonProyectoEliminar = (Button) convertView.findViewById(R.id.buttonProyectoEliminar);

		buttonProyectoEditar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		buttonProyectoEliminar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		buttonProyectoVerTareas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

}