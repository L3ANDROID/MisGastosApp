package com.lmorocho.misgastosapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private TextView viewTotal;

    private static final int REGISTER_FORM_REQUEST = 100;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("veterinario", 200));
        data.add(new ValueDataEntry("tecsup", 1200));
        data.add(new ValueDataEntry("casa", 500));

        pie.data(data);

        anyChartView.setChart(pie);

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        list = (ListView) findViewById(R.id.list);
        viewTotal = findViewById(R.id.total);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        list.setAdapter(adapter);
    }

    private void initData() {
        GastoRepository gastoRepository = GastoRepository.getInstance();

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        List<Gasto> gastos = gastoRepository.getGastos();
        for (Gasto gasto : gastos) {
            listDataHeader.add(gasto.getDetalle());
        }
        listDataHeader.add("veterinario");
        listDataHeader.add("tecsup");
        listDataHeader.add("impuesto");

//        List<ArrayList<String>> contenedor = new ArrayList<>();
//        List<Gasto> gastos = gastoRepository.getGastos();
//        for (Gasto gasto : gastos) {
//            listDataHeader.add(gasto.getDetalle());
//            List<String> lista = new ArrayList<>();
//            lista.add(gasto.toString());
//            contenedor.add((ArrayList<String>) lista);
//        }
//
//        for(int i=0; i < listDataHeader.size(); i++){
//            listHash.put(listDataHeader.get(i),contenedor.get(i));
//        }


        List<String> lista1 = new ArrayList<>();
        lista1.add("hecho por: usuario 1");
        lista1.add("descripcion: descripcion 1");
        lista1.add("costo: 100.00");


        List<String> lista2 = new ArrayList<>();
        lista2.add("hecho por: usuario 2");
        lista2.add("descripcion: pago mensual");
        lista2.add("costo: 1200.00");

        List<String> lista3 = new ArrayList<>();
        lista3.add("hecho por: usuario 3");
        lista3.add("descripcion:pago de impuesto");
        lista3.add("costo: 500.00");


        listHash.put(listDataHeader.get(0),lista1);
        listHash.put(listDataHeader.get(1),lista2);
        listHash.put(listDataHeader.get(2),lista3);
    }

    public void addItem(View view){
        startActivityForResult(new Intent(this, RegisterActivity.class), REGISTER_FORM_REQUEST);
    }

    // return from RegisterActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // refresh data
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)list.getAdapter();

        adapter.clear();

        GastoRepository gastoRepository = GastoRepository.getInstance();

        double total = 0;
        List<Gasto> gastos = gastoRepository.getGastos();
        for (Gasto gasto : gastos) {
            adapter.add(gasto.getDetalle());
            total = total + gasto.getMonto();
        }
        viewTotal.setText("S/. "+total);
        adapter.notifyDataSetChanged();

    }
}

