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

package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;

    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        cursorPry.close();
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public void crearOActualizarTarea(Tarea t){
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS, t.getMinutosTrabajados());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA, t.getFinalizada());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS, t.getHorasEstimadas());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD, t.getPrioridad().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO, t.getProyecto().getId());
	    t.getResponsable().setId(this.buscarOCrearUsuario(t.getResponsable()));
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE, t.getResponsable().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA, t.getDescripcion());
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
	    if(t.getId() == null){
		    mydb.insert(ProyectoDBMetadata.TABLA_TAREAS, ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS, valores);
	    }
	    else{
		    mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores,  "_ID = ?", new String[]{t.getId().toString()});
	    }        mydb.close();
    }

	private Integer buscarOCrearUsuario(Usuario usuario) {
		if(usuario.getId() != null){
			return usuario.getId();
		}
		SQLiteDatabase mydb =dbHelper.getReadableDatabase();
		Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_USUARIOS, null, ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO + " =?", new String[]{usuario.getNombre()}, null, null, null, null);
		if(cursor.getCount() == 0){
			return this.nuevoUsuario(usuario);
		}
		else {
			cursor.moveToFirst();
			return cursor.getInt(cursor.getColumnIndex("_ID"));
		}
	}

	public Integer nuevoUsuario(Usuario usuario){
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO,usuario.getNombre());
        valores.put(ProyectoDBMetadata.TablaUsuariosMetadata.MAIL,usuario.getCorreoElectronico());
        valores.put(ProyectoDBMetadata.TablaUsuariosMetadata.TELEFONO, usuario.getTelefono());
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        Long id = mydb.insert(ProyectoDBMetadata.TABLA_USUARIOS, ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO,valores);
        mydb.close();
		return id.intValue();
    }

    public void borrarTarea(Integer idTarea){
        db.delete(ProyectoDBMetadata.TABLA_TAREAS, ProyectoDBMetadata.TablaTareasMetadata._ID + " = ?", new String[]{idTarea.toString()});
    }

    public List<Prioridad> listarPrioridades(){
        List<Prioridad> prioridades = new ArrayList<>();
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_PRIORIDAD, null, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            Prioridad prioridad = new Prioridad();
            prioridad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
            prioridad.setPrioridad(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD)));
            prioridades.add(prioridad);
        }
        cursor.close();
        mydb.close();
        return prioridades;
    }

    public List<Usuario> listarUsuarios(){
        return null;
    }

    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, ProyectoDBMetadata.TablaTareasMetadata._ID + " = ?", new String[]{idTarea.toString()});
    }

    public void actualizarMinutosTarea(Integer idTarea, int minutosAdicionales ){

        String[] columns = {"_id", ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS};
        SQLiteDatabase mydb = dbHelper.getWritableDatabase();
        Cursor result = mydb.query(ProyectoDBMetadata.TABLA_TAREAS, columns, ProyectoDBMetadata.TablaTareasMetadata._ID + " = ?", new String[]{idTarea.toString()}, null, null, null);

        result.moveToFirst();
        int minutosTrabajados = result.getInt(1);
        Log.d("LAB05-MAIN", "minutosTrabajados: " + minutosTrabajados + " minutosAdicionales: "+ minutosAdicionales);

        minutosTrabajados += minutosAdicionales;
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS, minutosTrabajados);

        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, ProyectoDBMetadata.TablaTareasMetadata._ID + " = ?", new String[]{idTarea.toString()});
    }

    public Prioridad obtenerPrioridad(Integer id){
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_PRIORIDAD, null, "_id=?", new String[]{id.toString()}, null, null, null, null);
        Prioridad prioridad = new Prioridad();
        prioridad.setId(id);
        cursor.moveToFirst();
        prioridad.setPrioridad(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD)));
        cursor.close();
        mydb.close();
        return prioridad;
    }

    public Tarea obtenerTarea(Integer id){
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_TAREAS, null, "_id=?", new String[]{id.toString()}, null, null, null, null);
        Tarea tarea = new Tarea();
        tarea.setId(id);
        cursor.moveToFirst();
        tarea.setHorasEstimadas(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS)));
        tarea.setDescripcion(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
        tarea.setProyecto(this.obtenerProyecto(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO))));
        tarea.setResponsable(this.obtenerUsuario(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE))));
        tarea.setPrioridad(this.obtenerPrioridad(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD))));
        tarea.setMinutosTrabajados(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS)));
        tarea.setFinalizada(new Boolean(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA)) != 0));
        cursor.close();
        mydb.close();
        return tarea;
    }

    public Proyecto obtenerProyecto(Integer id){
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_PROYECTO, null, "_id=?", new String[]{id.toString()}, null, null, null, null);
        Proyecto proyecto = new Proyecto();
        proyecto.setId(id);
        cursor.moveToFirst();
        proyecto.setNombre(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaProyectoMetadata.TITULO)));
        cursor.close();
        mydb.close();
        return proyecto;
    }

    public Usuario obtenerUsuario(Integer id){
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.query(true, ProyectoDBMetadata.TABLA_USUARIOS, null, "_id=?", new String[]{id.toString()}, null, null, null, null);
        Usuario usuario = new Usuario();
        usuario.setId(id);
        cursor.moveToFirst();
        usuario.setNombre(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO)));
        usuario.setCorreoElectronico(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.MAIL)));
        usuario.setTelefono(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.TELEFONO)));
	    cursor.close();
        mydb.close();
        return usuario;
    }

    public String buscarDesvios(int minutosDesvio, Integer soloTerminados) {
        String tareasEncontradas = "";
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT *"+ " FROM "+ProyectoDBMetadata.TABLA_TAREAS + " WHERE "
                + minutosDesvio + " <= abs(" + ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS + " * 60 - "  + ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
                ") AND " + ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA + " >= " + soloTerminados , null);


        Log.d("SQL", "SELECT *"+ " FROM "+ProyectoDBMetadata.TABLA_TAREAS + " WHERE "
		        + minutosDesvio + " <= abs(" + ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS + " * 60 - "  + ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
		        ") AND " + ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA + " >= " + soloTerminados);
        while(cursor.moveToNext()){
            tareasEncontradas += (tareasEncontradas.isEmpty()? "" : "\n") + cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA));
        }
        cursor.close();
        mydb.close();
        return tareasEncontradas;
    }

    public Integer crearOActualizarProyecto(Proyecto proyecto) {
	    ContentValues valores = new ContentValues();
	    valores.put(ProyectoDBMetadata.TablaProyectoMetadata.TITULO, proyecto.getNombre());
	    SQLiteDatabase mydb = dbHelper.getWritableDatabase();
	    if(proyecto.getId() == null){
		    Long id = mydb.insert(ProyectoDBMetadata.TABLA_PROYECTO, ProyectoDBMetadata.TablaProyectoMetadata.TITULO, valores);
	        mydb.close();
		    return id.intValue();
	    }
	    else{
		    mydb.update(ProyectoDBMetadata.TABLA_PROYECTO, valores, "_ID = ?", new String[]{proyecto.getId().toString()});
		    mydb.close();
		    return proyecto.getId();
	    }
    }
}
