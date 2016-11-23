
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

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity implements View.OnClickListener {
	private EditText editTextDescripcion, editTextHorasEstimadas;
	private Spinner spinnerResponsable;
	private SeekBar seekBarPrioridad;
	private TextView textViewPrioridad;
	private Button btnGuardar, btnCancelar;
	private ProyectoDAO proyectoDAO;
	private Integer idTarea;
	private ArrayList<Prioridad> prioridades;
    private Toolbar toolbar;

    private boolean flagPermisoPedido;
	private static final int PERMISSION_REQUEST_CONTACT =999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alta_tarea);
		setParametros();

		setSupportActionBar(toolbar);
		if(getSupportActionBar()!=null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		askForContactPermission();

		proyectoDAO = new ProyectoDAO(this);

		prioridades = new ArrayList<>();
		prioridades.addAll(proyectoDAO.listarPrioridades());
		Collections.sort(prioridades, new Comparator<Prioridad>() {
			@Override
			public int compare(Prioridad prioridad1, Prioridad prioridad2)
			{
				return  prioridad1.getId().compareTo(prioridad2.getId());
			}
		});
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

    private void setParametros(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        editTextHorasEstimadas = (EditText) findViewById(R.id.editTextHorasEstimadas);
        spinnerResponsable = (Spinner) findViewById(R.id.spinnerReponsable);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCanelar);
        textViewPrioridad = (TextView) findViewById(R.id.textViewPrioridad);
        seekBarPrioridad = (SeekBar) findViewById(R.id.seekBarPrioridad);
    }

	private void cargarDatos(Integer idTarea) {
		Tarea t = proyectoDAO.obtenerTarea(idTarea);
		editTextDescripcion.setText(t.getDescripcion());
		String horasEstimadas = t.getHorasEstimadas().toString();
		editTextHorasEstimadas.setText(horasEstimadas);
		seekBarPrioridad.setProgress(t.getPrioridad().getId());
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCanelar:
                finish();
                break;
            case R.id.btnGuardar:
                String errores = validar();
                if(errores.equals("")){
                    Tarea t = new Tarea();
                    t.setDescripcion(editTextDescripcion.getText().toString().trim());
                    t.setFinalizada(false);
                    t.setHorasEstimadas(Integer.parseInt(editTextHorasEstimadas.getText().toString().trim()));
                    t.setMinutosTrabajados(0);
                    t.setPrioridad(prioridades.get(seekBarPrioridad.getProgress()));
                    t.setProyecto(proyectoDAO.obtenerProyecto(1));
                    t.setResponsable(((Usuario) spinnerResponsable.getSelectedItem()));
                    if(idTarea != 0) {
                        t.setId(idTarea);
                    }
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
                break;
        }
	}

	private String validar() {
		String res = "";
		if(editTextDescripcion.getText().toString().trim().equals("")){
			res += getString(R.string.error_descripcion);
		}

		try{
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(editTextHorasEstimadas.getText().toString().trim());
		}
		catch (Exception e){
			res += (res.isEmpty()? "" : "\n") + getString(R.string.error_horas_estimadas);
		}

		if(seekBarPrioridad.getProgress()<0 || seekBarPrioridad.getProgress()>3){
			res += (res.isEmpty()? "" : "\n") + getString(R.string.error_prioridad);
		}
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

	public void askForContactPermission(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(AltaTareaActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(AltaTareaActivity.this,
						Manifest.permission.CALL_PHONE)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AltaTareaActivity.this);
					builder.setTitle("Permisos Peligrosos!!!");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("Puedo acceder a un permiso peligroso???");
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							flagPermisoPedido=true;
							requestPermissions(
									new String[]
											{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
									, PERMISSION_REQUEST_CONTACT);
						}
					});
					builder.show();
				} else {
					flagPermisoPedido=true;
					ActivityCompat.requestPermissions(AltaTareaActivity.this,
							new String[]
									{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
							, PERMISSION_REQUEST_CONTACT);
				}

			}
		}
		if(!flagPermisoPedido){
			buscarContactos();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
		Log.d("ESCRIBIR_JSON","req code"+requestCode+ " "+ Arrays.toString(permissions)+" ** "+Arrays.toString(grantResults));
		switch (requestCode) {
			case AltaTareaActivity.PERMISSION_REQUEST_CONTACT: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					buscarContactos();
				} else {
					finish();
					Toast.makeText(AltaTareaActivity.this, "No permission for contacts", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	public void buscarContactos(){
        ArrayList<Usuario> contactos = new ArrayList<>();
		Uri uri =  ContactsContract.Contacts.CONTENT_URI;
		String sortOrder =  ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		final String[] projection = new String[] {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
		};
		String selection = ContactsContract.Contacts.DISPLAY_NAME+" LIKE '%' AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
		// consulta ejemplo buscando por nombre visualizado en los contactos agregados
		Cursor c = this.getContentResolver().query(uri, projection, selection, null, sortOrder);
        if(c!=null) {
            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor telefonoCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                String telefono = "";
                if (telefonoCur != null) {
                    while (telefonoCur.moveToNext()) {
                        telefono = telefonoCur.getString(telefonoCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.d("TEST-ARR", telefono);
                    }
                    telefonoCur.close();
                }

                Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);

                String email = "";
                if (emailCur != null) {
                    while (emailCur.moveToNext()) {
                        email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.d("TEST-ARR", email);
                    }
                    emailCur.close();
                }

                contactos.add(new Usuario(null, nombre, email, telefono));
            }
            c.close();
        }

		ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, contactos);
		spinnerResponsable.setAdapter(adapter);
	}
}