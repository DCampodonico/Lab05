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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

public class AltaTareaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextDescripcion, editTextHorasEstimadas;
    Spinner spinnerResponsable;
    SeekBar seekBarPrioridad;
    Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);
        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        editTextHorasEstimadas = (EditText) findViewById(R.id.editTextHorasEstimadas);
        spinnerResponsable = (Spinner) findViewById(R.id.spinnerReponsable);
        seekBarPrioridad = (SeekBar) findViewById(R.id.seekBarPrioridad);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCanelar);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == btnCancelar.getId()){

        }
        else if(id == btnGuardar.getId()){
//            if(validar() == ""){

  //          }
    //        else{

      //      }
        }
    }

    //TODO
    //private String validar() {

    //}
}
