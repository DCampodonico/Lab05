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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class BuscarTareaDesvio extends AppCompatActivity implements View.OnClickListener {
    EditText editTextMinutosDesvio;
    Switch switchTareaTerminada;
    TextView textViewResultadoBusqueda;
    Button buttonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_tarea_desvio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextMinutosDesvio = (EditText) findViewById(R.id.editTextMinutosDesvio);
        switchTareaTerminada = (Switch) findViewById(R.id.switchTareaTerminada);
        textViewResultadoBusqueda = (TextView) findViewById(R.id.textViewResultadoBusqueda);
        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        String s = validar();
        if(s.isEmpty()){
            ProyectoDAO proyectoDAO = new ProyectoDAO(this);
            String res = proyectoDAO.buscarDesvios(Integer.parseInt(editTextMinutosDesvio.getText().toString()), (switchTareaTerminada.isChecked()? 1 : 0));
            textViewResultadoBusqueda.setText(res);
        }
        else{
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        }
    }

    private String validar() {
        String resultado = "";
        try{
            Integer.parseInt(editTextMinutosDesvio.getText().toString().trim());
        }
        catch (Exception e){
            resultado += getString(R.string.error_minutos_desvio);
        }
        return resultado;
    }
}
