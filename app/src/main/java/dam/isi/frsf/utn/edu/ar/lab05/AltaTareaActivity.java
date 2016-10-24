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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextDescripcion, editTextHorasEstimadas;
    Spinner spinnerResponsable;
    SeekBar seekBarPrioridad;
    Button btnGuardar, btnCancelar;
    ProyectoDAO proyectoDAO;
    Integer idTarea;

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
        proyectoDAO = new ProyectoDAO(this);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        Intent intent = getIntent();
        idTarea = intent.getIntExtra("ID_TAREA", 0);
        if(idTarea != 0){
            cargarDatos(idTarea);
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
                t.setPrioridad(proyectoDAO.obtenerPrioridad(seekBarPrioridad.getProgress()));
                //TODO t.setProyecto();
                t.setProyecto(new Proyecto(1, ""));
                //TODO t.setResponsable();
                t.setResponsable(new Usuario(1, "", ""));
                proyectoDAO.nuevaTarea(t);
                if(idTarea == null){
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

        if(seekBarPrioridad.getProgress()<1 || seekBarPrioridad.getProgress()>4){
            res += (res.isEmpty()? "" : "\n") + getString(R.string.error_prioridad);
        }

        //TODO validar spinner

        return res;
    }
}
