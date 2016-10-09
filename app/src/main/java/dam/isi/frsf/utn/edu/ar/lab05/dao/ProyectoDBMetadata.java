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

import android.provider.BaseColumns;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDBMetadata {

    public static final int VERSION_DB = 1;
    public static final String NOMBRE_DB= "lab05.db";
    public static final String TABLA_PROYECTO= "PROYECTO";
    public static final String TABLA_PROYECTO_ALIAS= " pry";
    public static final String TABLA_USUARIOS= "USUARIOS";
    public static final String TABLA_USUARIOS_ALIAS= " usr";
    public static final String TABLA_TAREAS= "TAREA";
    public static final String TABLA_TAREAS_ALIAS= " tar";
    public static final String TABLA_PRIORIDAD= "PRIORIDAD";
    public static final String TABLA_PRIORIDAD_ALIAS= " pri";

    public static class TablaProyectoMetadata implements BaseColumns{
        public static final String TITULO ="TITULO";
        public static final String TIT_PROY_ALIAS ="TITULO_PROYECTO";
    }

    public static class TablaUsuariosMetadata implements BaseColumns{
        public static final String USUARIO ="NOMBRE";
        public static final String USUARIO_ALIAS ="NOMBRE_USUARIO";
        public static final String MAIL ="CORREO_ELECTRONICO";
    }

    public static class TablaTareasMetadata implements BaseColumns{
        public static final String TAREA  ="DESCRIPCION";
        public static final String HORAS_PLANIFICADAS ="HORAS_PLANIFICADAS";
        public static final String MINUTOS_TRABAJADOS ="MINUTOS_TRABAJDOS";
        public static final String PRIORIDAD="ID_PRIORIDAD";
        public static final String RESPONSABLE ="ID_RESPONSABLE";
        public static final String PROYECTO ="ID_PROYECTO";
        public static final String FINALIZADA ="FINALIZADA";
    }

    public static class TablaPrioridadMetadata implements BaseColumns{
        public static final String PRIORIDAD ="TITULO";
        public static final String PRIORIDAD_ALIAS ="NOMBRE_PRIORIDAD";
    }

}
