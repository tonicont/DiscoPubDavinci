package com.tonicont.davinci;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;



public class FiestasActivity extends Activity implements View.OnClickListener {
    private ArrayList<String> grupos;
    private ArrayList<ArrayList<ArrayList<String>>> hijos;
    private ArrayList<Fiesta> fiestas;
    private ProgressDialog pDialog;
    private ExpandableListView l;
    private miExpandableAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fiestas);
        ImageButton btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        btn_atras.setOnClickListener(this);
        new DownloadFiestas().execute();
        l = (ExpandableListView) findViewById(R.id.expan_fiestas);
        fiestas = new ArrayList<Fiesta>();




    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_atras:{
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
            }
        }
    }

    public class miExpandableAdapter extends BaseExpandableListAdapter {

        private ArrayList<String> groups;

        private ArrayList<ArrayList<ArrayList<String>>> children;

        private Context context;

        public miExpandableAdapter(Context context, ArrayList<String> groups, ArrayList<ArrayList<ArrayList<String>>> children) {
            this.context = context;
            this.groups = grupos;
            this.children = hijos;
        }


        @Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }


        @Override
        public ArrayList<String> getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View convertView, ViewGroup parent) {

            String hijo = (String) ((ArrayList<String>)getChild(groupPosition, childPosition)).get(0);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandablelistview_hijo, null);
            }

            TextView hijotxt = (TextView) convertView.findViewById(R.id.TextViewHijo01);

            hijotxt.setText(hijo);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            String group = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandablelistview_grupo, null);
            }

            TextView grouptxt = (TextView) convertView.findViewById(R.id.TextViewGrupo);
            TextView fecha = (TextView) convertView.findViewById(R.id.txt_fecha);

            fecha.setText(fiestas.get(groupPosition).getFecha().toString());
            grouptxt.setText(group);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    private void cargarDatos(){
        grupos= new ArrayList<String>();
        hijos= new ArrayList<ArrayList<ArrayList<String>>>();

        for(int i=0;i<fiestas.size();i++)
        {
            grupos.add(fiestas.get(i).getTitulo());
            hijos.add(new ArrayList<ArrayList<String>>());
            hijos.get(i).add(new ArrayList<String>());
            hijos.get(i).get(0).add(fiestas.get(i).getDescripcion());
        }


        /*
        grupos.add("Fiesta de la Primavera");
        grupos.add("Romería del Encinar");
        grupos.add("Otra Fiestecilla");

        hijos.add(new ArrayList<ArrayList<String>>());
        hijos.get(0).add(new ArrayList<String>());
        hijos.get(0).get(0).add("Descripción de la fiesta de la primavera");


        hijos.add(new ArrayList<ArrayList<String>>());
        hijos.get(1).add(new ArrayList<String>());
        hijos.get(1).get(0).add("Descripción de la Romería del Encinar");


        hijos.add(new ArrayList<ArrayList<String>>());
        hijos.get(2).add(new ArrayList<String>());
        hijos.get(2).get(0).add("Descripción de la otra fiestecilla buena.");
        */
    }

    private class DownloadFiestas extends AsyncTask<String, String, String>
    {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FiestasActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Fiesta f = new Fiesta();
            fiestas = f.getFiestas();
            cargarDatos();
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adaptador = new miExpandableAdapter(FiestasActivity.this, grupos, hijos);
                    l.setAdapter(adaptador);
                }
            });
        }

    }
}
