
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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity implements View.OnClickListener {
	EditText editTextDescripcion, editTextHorasEstimadas;
	Spinner spinnerResponsable;
	SeekBar seekBarPrioridad;
	TextView textViewPrioridad;
	Button btnGuardar, btnCancelar;
	ProyectoDAO proyectoDAO;
	Integer idTarea;
	ArrayList<Usuario> contactos;
	ArrayList<Prioridad> prioridades;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alta_tarea);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
		editTextHorasEstimadas = (EditText) findViewById(R.id.editTextHorasEstimadas);

		spinnerResponsable = (Spinner) findViewById(R.id.spinnerReponsable);

		buscarContactos();
		ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, contactos);
		spinnerResponsable.setAdapter(adapter);

		proyectoDAO = new ProyectoDAO(this);

		seekBarPrioridad = (SeekBar) findViewById(R.id.seekBarPrioridad);
		prioridades = new ArrayList<>();
		prioridades.addAll(proyectoDAO.listarPrioridades());
		Collections.sort(prioridades, new Comparator<Prioridad>() {
			@Override
			public int compare(Prioridad prioridad1, Prioridad prioridad2)
			{
				return  prioridad1.getId().compareTo(prioridad2.getId());
			}
		});
		textViewPrioridad = (TextView) findViewById(R.id.textViewPrioridad);
		seekBarPrioridad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				textViewPrioridad.setText(prioridades.get(progress).getPrioridad());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		seekBarPrioridad.setProgress(3);
		seekBarPrioridad.setProgress(0);
		btnGuardar = (Button) findViewById(R.id.btnGuardar);
		btnCancelar = (Button) findViewById(R.id.btnCanelar);

		btnGuardar.setOnClickListener(this);
		btnCancelar.setOnClickListener(this);

		Intent intent = getIntent();
		idTarea = intent.getIntExtra("ID_TAREA", 0);
		if(idTarea != 0){
			setTitle(R.string.title_activity_editar_tarea);
			cargarDatos(idTarea);
		}
		else{
			setTitle(R.string.title_activity_alta_tarea);
		}
	}

	private void cargarDatos(Integer idTarea) {
		Tarea t = proyectoDAO.obtenerTarea(idTarea);
		editTextDescripcion.setText(t.getDescripcion());
		editTextHorasEstimadas.setText(t.getHorasEstimadas().toString());
		seekBarPrioridad.setProgress(t.getPrioridad().getId());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == btnCancelar.getId()){
			finish();
		}
		else if(id == btnGuardar.getId()){
			String errores = validar();
			if(errores.equals("")){
				Tarea t = new Tarea();
				t.setDescripcion(editTextDescripcion.getText().toString().trim());
				t.setFinalizada(false);
				t.setHorasEstimadas(Integer.parseInt(editTextHorasEstimadas.getText().toString().trim()));
				t.setMinutosTrabajados(0);
				t.setPrioridad(prioridades.get(seekBarPrioridad.getProgress()));
				//TODO t.setProyecto();
				t.setProyecto(new Proyecto(1, ""));
				t.setResponsable(((Usuario) spinnerResponsable.getSelectedItem()));
				proyectoDAO.crearOActualizarTarea(t);
				if(idTarea == 0){
					Toast.makeText(this, R.string.exito_nueva_tarea, Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(this, R.string.exito_modificar_tarea, Toast.LENGTH_LONG).show();
				}
				finish();
			}
			else{
				Toast.makeText(this, errores, Toast.LENGTH_LONG).show();
			}
		}
	}

	private String validar() {
		String res = "";
		if(editTextDescripcion.getText().toString().trim().equals("")){
			res += getString(R.string.error_descripcion);
		}

		try{
			Integer.parseInt(editTextHorasEstimadas.getText().toString().trim());
		}
		catch (Exception e){
			res += (res.isEmpty()? "" : "\n") + getString(R.string.error_horas_estimadas);
		}

		if(seekBarPrioridad.getProgress()<0 || seekBarPrioridad.getProgress()>3){
			res += (res.isEmpty()? "" : "\n") + getString(R.string.error_prioridad);
		}

		//TODO validar spinner??

		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_alta_tarea, menu);
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
			case R.id.action_alta_usuario:
				intent = new Intent(this, AltaUsuarioActivity.class);
				startActivity(intent);
				break;

		}

		return super.onOptionsItemSelected(item);
	}

	public void buscarContactos(){
		//TODO pedir permisos
		contactos = new ArrayList<>();
		Uri uri =  ContactsContract.Contacts.CONTENT_URI;
		String sortOrder =  ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		final String[] projection = new String[] {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
		};
		String selection = ContactsContract.Contacts.DISPLAY_NAME+" LIKE '%' AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
		// consulta ejemplo buscando por nombre visualizado en los contactos agregados
		Cursor c =this.getContentResolver().query(uri, projection, selection, null, sortOrder);
		while(c.moveToNext()) {
			String nombre = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor telefonoCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

			String telefono = "";
			while (telefonoCur.moveToNext()) {
				telefono = telefonoCur.getString( telefonoCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				Log.d("TEST-ARR", telefono);
				telefonoCur.close();
				break;
			}

			String email = "";
			Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id},null);
			while (emailCur.moveToNext()) {
				email = emailCur.getString( emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				Log.d("TEST-ARR", email);
				emailCur.close();
				break;
			}
			contactos.add(new Usuario(null, nombre, email, telefono));
		}
		c.close();
	}
}