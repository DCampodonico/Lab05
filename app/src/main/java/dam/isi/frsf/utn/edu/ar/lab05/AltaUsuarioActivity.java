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
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText mName;
	private EditText mEmailAddress;
	private EditText mPhoneNumber;
	private Button buttonAltaUsuarioGuardar, buttonAltaUsuarioCancelar;
	private ProyectoApiRest proyectoApiRest = new ProyectoApiRest();
	private Usuario usuario;
    private Toolbar toolbar;

	private boolean flagPermisoPedido;
	private static final int PERMISSION_REQUEST_CONTACT =999;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_usuario);
        setParametros();

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

	    buttonAltaUsuarioGuardar.setOnClickListener(this);
	    buttonAltaUsuarioCancelar.setOnClickListener(this);
    }

    private void setParametros(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mName = (EditText) findViewById(R.id.editName);
        mEmailAddress = (EditText) findViewById(R.id.editEmail);
        mPhoneNumber = (EditText) findViewById(R.id.editPhone);
        buttonAltaUsuarioGuardar = (Button) findViewById(R.id.buttonAltaUsuarioGuardar);
        buttonAltaUsuarioCancelar = (Button) findViewById(R.id.buttonAltaUsuarioCancelar);
    }

    private void agregarUsuarioADBLocal(Usuario usuario) {
		ProyectoDAO proyectoDAO = new ProyectoDAO(this);
	    usuario.setId(proyectoDAO.nuevoUsuario(usuario));
    }

    private void pushUsuario(Usuario usuario) {
	    proyectoApiRest.crearUsuario(usuario);
    }

	public void askForContactPermission(final Usuario usuario){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(AltaUsuarioActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(AltaUsuarioActivity.this,
						Manifest.permission.CALL_PHONE)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AltaUsuarioActivity.this);
					builder.setTitle("Permisos Peligrosos!!!");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setMessage("Puedo acceder a un permiso peligroso???");
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@TargetApi(Build.VERSION_CODES.M)
						@Override
						public void onDismiss(DialogInterface dialog) {
							flagPermisoPedido=true;
							AltaUsuarioActivity.this.usuario = usuario;
							requestPermissions(
									new String[]
											{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
									, PERMISSION_REQUEST_CONTACT);
						}
					});
					builder.show();
				} else {
					flagPermisoPedido=true;
					ActivityCompat.requestPermissions(AltaUsuarioActivity.this,
							new String[]
									{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
							, PERMISSION_REQUEST_CONTACT);
				}

			}
		}
		if(!flagPermisoPedido) guardarContacto(usuario);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
		Log.d("ESCRIBIR_JSON","req code"+requestCode+ " "+ Arrays.toString(permissions)+" ** "+Arrays.toString(grantResults));
		switch (requestCode) {
			case AltaUsuarioActivity.PERMISSION_REQUEST_CONTACT: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    guardarContacto(usuario);
				} else {
					finish();
					Toast.makeText(AltaUsuarioActivity.this, "No permission for contacts", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
    private void agregarUsuarioAContactos(Usuario usuario){
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

    private void guardarContacto(Usuario usuario){
        this.agregarUsuarioAContactos(usuario);
        this.agregarUsuarioADBLocal(usuario);
        this.pushUsuario(usuario);
        Toast.makeText(this, R.string.Alta_usuario_exito, Toast.LENGTH_LONG).show();
        finish();
    }

	@Override
	public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonAltaUsuarioCancelar:
            finish();
            break;

            case R.id.buttonAltaUsuarioGuardar:
            String errores = validar();
            if(errores.equals("")){
                Usuario usuario = new Usuario(null, mName.getText().toString().trim(), mEmailAddress.getText().toString().trim(), mPhoneNumber.getText().toString().trim());
                askForContactPermission(usuario);
            }
            else{
                Toast.makeText(this, errores, Toast.LENGTH_LONG).show();
            }
            break;
        }
   }

	private String validar() {
        String res = "";
        if(mName.getText().toString().trim().equals("")){
            res += getString(R.string.error_nombre_usuario);
        }
        if(mEmailAddress.getText().toString().trim().equals("")){
            res += getString(R.string.error_email_usuario);
        }
        if(mPhoneNumber.getText().toString().trim().equals("")){
            res += getString(R.string.error_telefono_usuario);
        }

        return res;
	}
}

