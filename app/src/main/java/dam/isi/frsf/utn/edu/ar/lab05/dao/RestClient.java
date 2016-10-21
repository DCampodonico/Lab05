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

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class RestClient {

    private final String IP_SERVER = "10.0.2.2";
    private final String PORT_SERVER = "4000";
    private final String TAG_LOG = "LAB06";
    public JSONObject getById(Integer id,String path) {
        JSONObject resultado = null;
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path+"/"+id);
            Log.d("TAG_LOG",url.getPath()+ " --> "+url.toString());
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            Log.d("TAG_LOG",url.getPath()+ " --> "+sb.toString());
            resultado = new JSONObject(sb.toString());
        }
        catch (IOException e) {
            Log.e("TEST-ARR",e.getMessage(),e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return resultado;
    }

    public JSONArray getByAll(Integer id,String path) {
        JSONArray resultado = null;
        return resultado;
    }
    public void crear(JSONObject objeto,String path) {
    }
    public void actualizar(JSONObject objeto,String path) {
    }

    public void borrar(Integer id,String path) {
    }
}
