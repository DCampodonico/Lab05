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

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mName;
    EditText mEmailAddress;
    EditText mPhoneNumber;
	Button buttonAltaUsuarioGuardar, buttonAltaUsuarioCancelar;
	ProyectoDAO proyectoDAO = new ProyectoDAO(this);
	ProyectoApiRest proyectoApiRest = new ProyectoApiRest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (EditText) findViewById(R.id.editName);
        mEmailAddress = (EditText) findViewById(R.id.editEmail);
        mPhoneNumber = (EditText) findViewById(R.id.editPhone);
	    buttonAltaUsuarioGuardar = (Button) findViewById(R.id.buttonAltaUsuarioGuardar);
	    buttonAltaUsuarioCancelar = (Button) findViewById(R.id.buttonAltaUsuarioCancelar);
	    buttonAltaUsuarioGuardar.setOnClickListener(this);
	    buttonAltaUsuarioCancelar.setOnClickListener(this);
    }

    private void agregarUsuarioADBLocal(Usuario usuario) {
	    proyectoDAO = new ProyectoDAO(this);
	    usuario.setId(proyectoDAO.nuevoUsuario(usuario));
    }

    private void pushUsuario(Usuario usuario) {
	    proyectoApiRest.crearUsuario(usuario);
    }

    private void agregarUsuarioAContactos(Usuario usuario){
	    //TODO pedir permisos
        String accountType =null;
        String accountName =null;
        ContentValues values = new ContentValues();
        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType);
        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, accountName);
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, usuario.getNombre());
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.DATA, usuario.getCorreoElectronico());
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.DATA, usuario.getTelefono());
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        Log.d("INFO", "Se creo un nuevo contacto");
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == buttonAltaUsuarioCancelar.getId()){
			finish();
		}
		else if(id == buttonAltaUsuarioGuardar.getId()){
			String errores = validar();
			if(errores.equals("")){
				Usuario usuario = new Usuario(null, mName.getText().toString().trim(), mEmailAddress.getText().toString().trim(), mPhoneNumber.getText().toString().trim());
				this.agregarUsuarioAContactos(usuario);
				this.agregarUsuarioADBLocal(usuario);
				this.pushUsuario(usuario);
				Toast.makeText(this, R.string.Alta_usuario_exito, Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				Toast.makeText(this, errores, Toast.LENGTH_LONG).show();
			}
		}
		}

	private String validar() {
		//TODO validar alta usuario
		return "";
	}
}

