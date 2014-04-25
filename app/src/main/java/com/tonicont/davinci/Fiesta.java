package com.tonicont.davinci;

import android.util.Log;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tonicont on 11/03/14.
 */
public class Fiesta
{
    private String titulo;
    private String descripcion;
    private Date fecha;

    public Fiesta()
    {    }

    public Fiesta(String titulo, String descripcion, Date fecha)
    {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Fiesta> getFiestas()
    {
        ArrayList<Fiesta> fiestas = new ArrayList<Fiesta>();
        JSONArray jsonArray =  null;
        try
        {
            String data;
            data = httpGetData("http://davinci.hostinazo.com/davinci/androidControl.php?accion=2");
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
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = formatoDelTexto.parse(jsonObject.getString("fecha"));
                Fiesta fiesta = new Fiesta(jsonObject.getString("titulo"), jsonObject.getString("descripcion"), fecha);
                fiestas.add(fiesta);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return fiestas;
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
