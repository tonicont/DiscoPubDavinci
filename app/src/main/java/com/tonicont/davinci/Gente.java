package com.tonicont.davinci;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tonicont on 11/03/14.
 */
public class Gente {
    private String nombre;
    private String apellidos;
    private String descripcion;
    private String tiwtter;
    private String imagen;

    public Gente() {}

    public Gente(String nombre, String apellidos, String descripcion, String tiwtter, String imagen) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.descripcion = descripcion;
        this.tiwtter = tiwtter;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTiwtter() {
        return tiwtter;
    }

    public void setTiwtter(String tiwtter) {
        this.tiwtter = tiwtter;
    }

    public ArrayList<Gente> getGente()
    {
        ArrayList<Gente> personas = new ArrayList<Gente>();
        JSONArray jsonArray =  null;
        try
        {
            String data;
            data = httpGetData("http://davinci.hostinazo.com/davinci/androidControl.php?accion=3");
            if(data.length() > 1)
            {
                jsonArray = new JSONArray(data);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error recuperando la informacion del servidor, verifique su conexion a internet y vuelva a intentarlo.", 1000).show();
        }

        try
        {
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Gente gente = new Gente(jsonObject.getString("nombre"), jsonObject.getString("apellidos"), jsonObject.getString("descripcion"), jsonObject.getString("twitter"), jsonObject.getString("imagen"));
                personas.add(gente);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return personas;
    }

    public String httpGetData(String mURL) {
        String response="";
        mURL=mURL.replace(" ", "%20");
        Log.i("LocAndroid Response HTTP Threas", "Ejecutando get 0: " + mURL);
        HttpClient httpclient = new DefaultHttpClient();

        Log.i("LocAndroid Response HTTP Thread","Ejecutando get 1");
        HttpGet httppost = new HttpGet(mURL);
        Log.i("LocAndroid Response HTTP Thread","Ejecutando get 2");
        try {


            Log.i("LocAndroid Response HTTP","Ejecutando get");
            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            response = httpclient.execute(httppost,responseHandler);
            Log.i("LocAndroid Response HTTP",response);
        } catch (ClientProtocolException e) {
            Log.i("LocAndroid Response HTTP ERROR 1",e.getMessage());
            // TODO Auto-generated catch block
        } catch (IOException e) {

            Log.i("LocAndroid Response HTTP ERROR 2",e.getMessage());
            // TODO Auto-generated catch block
        }
        return response;
    }


}
