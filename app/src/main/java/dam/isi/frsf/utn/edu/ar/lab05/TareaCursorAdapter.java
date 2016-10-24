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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDBMetadata;

/**
 * Created by mdominguez on 06/10/16.
 */
public class TareaCursorAdapter extends CursorAdapter {
    private LayoutInflater inflador;
    private ProyectoDAO myDao;
    private Context contexto;

    public TareaCursorAdapter(Context contexto, Cursor c, ProyectoDAO dao) {
        super(contexto, c, false);
        myDao = dao;
        this.contexto = contexto;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        inflador = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.fila_tarea, viewGroup, false);
        return vista;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //obtener la posicion de la fila actual y asignarla a los botones y checkboxes
        int pos = cursor.getPosition();

        // Referencias UI.
        TextView nombre = (TextView) view.findViewById(R.id.tareaTitulo);
        TextView tiempoAsignado = (TextView) view.findViewById(R.id.tareaMinutosAsignados);
        TextView tiempoTrabajado = (TextView) view.findViewById(R.id.tareaMinutosTrabajados);
        TextView prioridad = (TextView) view.findViewById(R.id.tareaPrioridad);
        TextView responsable = (TextView) view.findViewById(R.id.tareaResponsable);
        CheckBox finalizada = (CheckBox) view.findViewById(R.id.tareaFinalizada);

        final Button btnFinalizar = (Button) view.findViewById(R.id.tareaBtnFinalizada);
        final Button btnEditar = (Button) view.findViewById(R.id.tareaBtnEditarDatos);
        final ToggleButton btnEstado = (ToggleButton) view.findViewById(R.id.tareaBtnTrabajando);

        nombre.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
        Integer horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
        tiempoAsignado.setText(horasAsigandas * 60 + "");

        Integer minutosAsigandos = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
        tiempoTrabajado.setText(minutosAsigandos + "");
        String p = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS));
        prioridad.setText(p);
        responsable.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));
        finalizada.setChecked(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA)) == 1);
        finalizada.setTextIsSelectable(false);

        btnEditar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea = (Integer) view.getTag();
                Intent intEditarAct = new Intent(contexto, AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA", idTarea);
                context.startActivity(intEditarAct);

            }
        });

        btnFinalizar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea = (Integer) view.getTag();
                Thread backGroundUpdate = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LAB05-MAIN", "finalizar tarea : --- " + idTarea);
                        myDao.finalizar(idTarea);
                        handlerRefresh.sendEmptyMessage(1);
                    }
                });
                backGroundUpdate.start();
            }
        });

        btnEstado.setTag(R.id.TAG_ONLINE_ID, cursor.getInt(cursor.getColumnIndex("_id")));
        btnEstado.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Integer idTarea = (Integer) buttonView.getTag(R.id.TAG_ONLINE_ID);
                if (isChecked) {
                    btnEstado.setTag(R.id.TAG_ONLINE_tiempoComienzo, System.currentTimeMillis());
                    Log.d("LAB05-MAIN", "comenzar a trabajar : --- " + idTarea);
                } else {
                    final Integer minutosTranscurridos = (int) (long) ((System.currentTimeMillis() - (Long) btnEstado.getTag(R.id.TAG_ONLINE_tiempoComienzo)) / 1000 * 12 / 60);
                    Thread backGroundUpdate = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LAB05-MAIN", "terminar de trabajar : --- " + idTarea);
                            myDao.actualizarMinutosTarea(idTarea, minutosTranscurridos);
                            handlerRefresh.sendEmptyMessage(1);
                        }
                    });
                    backGroundUpdate.start();
                }
            }
        });

        view.setTag(R.id.TAG_ONLINE_DESCRIPCION, cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
        view.setTag(R.id.TAG_ONLINE_ID, cursor.getInt(cursor.getColumnIndex("_id")));
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
// Crear un builder y vincularlo a la actividad que lo mostrará
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//Configurar las características
                String s = v.getResources().getString(R.string.dialog_message);
                s = String.format(s, v.getTag(R.id.TAG_ONLINE_DESCRIPCION).toString());
                builder.setMessage(s)
                        .setTitle(R.string.dialog_title)
//Obtener una instancia de cuadro de dialogo
                        .setPositiveButton(R.string.dialog_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDao.borrarTarea((Integer) v.getTag(R.id.TAG_ONLINE_ID));
                        Toast.makeText(v.getContext(), R.string.exito_eliminar_tarea, Toast.LENGTH_LONG).show();
                        handlerRefresh.sendEmptyMessage(1);
                    }
                })
                        .setNegativeButton(R.string.dialog_cancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
//Mostrarlo
                dialog.show();
                return false;
            }
        });
    }

    Handler handlerRefresh = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            TareaCursorAdapter.this.changeCursor(myDao.listaTareas(1));
        }
    };
}