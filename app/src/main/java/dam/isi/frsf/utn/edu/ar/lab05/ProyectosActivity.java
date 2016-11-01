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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.BusquedaFinalizadaListener;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ListarProyectosTask;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

public class ProyectosActivity extends AppCompatActivity implements BusquedaFinalizadaListener<Proyecto>{

	private ListView listViewProyectos;
	private ProyectoApiRest proyectoApiRest;
	private ProyectoAdapter proyectoAdapter;
	private List<Proyecto> proyectos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proyectos);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		listViewProyectos = (ListView) findViewById(R.id.listViewProyectos);
		proyectos = new ArrayList<>();
		proyectoApiRest = new ProyectoApiRest();
		proyectoAdapter = new ProyectoAdapter(this, proyectos);
		listViewProyectos.setAdapter(proyectoAdapter);
		new ListarProyectosTask(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_proyectos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		Intent intent;

		switch (id){
			case R.id.action_alta_proyecto:
				intent = new Intent(this, AltaProyectoActivity.class);
				intent.putExtra("ID_PROYECTO", 0);
				startActivity(intent);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void busquedaFinalizada(List<Proyecto> proyectos) {
		this.proyectos.clear();
		this.proyectos.addAll(proyectos);
		proyectoAdapter.notifyDataSetChanged();
	}
}
