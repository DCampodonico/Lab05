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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

public class AltaProyectoActivity extends AppCompatActivity implements View.OnClickListener {

	EditText editTextAltaProyectoNombre;
	Button buttonAltaProyectoGuardar, buttonAltaProyectoCancelar;
	ProyectoDAO proyectoDAO;
	ProyectoApiRest proyectoApiRest;
	Integer idProyecto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alta_proyecto);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		editTextAltaProyectoNombre = (EditText) findViewById(R.id.editTextAltaProyectoNombre);
		buttonAltaProyectoGuardar = (Button) findViewById(R.id.buttonAltaProyectoGuardar);
		buttonAltaProyectoCancelar = (Button) findViewById(R.id.buttonAltaProyectoCancelar);

		proyectoDAO = new ProyectoDAO(this);
		proyectoApiRest = new ProyectoApiRest();
		buttonAltaProyectoGuardar.setOnClickListener(this);
		buttonAltaProyectoCancelar.setOnClickListener(this);

		Intent intent = getIntent();
		idProyecto = intent.getIntExtra("ID_PROYECTO", 0);
		if(idProyecto != 0){
			setTitle(R.string.title_activity_editar_proyecto);
			editTextAltaProyectoNombre.setText(getIntent().getStringExtra("NOMBRE_PROYECTO"));
		}
		else{
			setTitle(R.string.title_activity_alta_proyecto);
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == buttonAltaProyectoCancelar.getId()){
			finish();
		}
		else if(id == buttonAltaProyectoGuardar.getId()){
			String errores = validar();
			if(validar().isEmpty()){
				Proyecto proyecto = new Proyecto();
				proyecto.setNombre(editTextAltaProyectoNombre.getText().toString().trim());
				if(idProyecto != 0){
					proyecto.setId(idProyecto);
				}
				proyecto.setId(proyectoDAO.crearOActualizarProyecto(proyecto));
				if(idProyecto == 0){
					proyectoApiRest.crearProyecto(proyecto);
					Toast.makeText(this, R.string.exito_nuevo_proyecto, Toast.LENGTH_LONG).show();
				}
				else{
					proyectoApiRest.actualizarProyecto(proyecto);
					Toast.makeText(this, R.string.exito_modificar_proyecto, Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(this, errores, Toast.LENGTH_LONG).show();
			}
		}
	}

	private String validar() {
		return editTextAltaProyectoNombre.getText().toString().trim().isEmpty()? getString(R.string.error_nombre_proyecto) : "";
	}
}
