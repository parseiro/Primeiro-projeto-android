package com.vilelapinheiro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vilelapinheiro.model.Patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PacienteAdapter extends BaseAdapter  {

    private final Context context;
    private final List<Patient> patients = new ArrayList<>();

    private static class PacienteHolder {
        public TextView name, sex, convenio, pesquisas;
    }

    public PacienteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return patients.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        PacienteHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_patient, viewGroup, false);

            holder = new PacienteHolder();

            holder.name = view.findViewById(R.id.name);
            holder.sex = view.findViewById(R.id.sex);
            holder.convenio = view.findViewById(R.id.medicalPlan);
            holder.pesquisas = view.findViewById(R.id.pesquisas);

            view.setTag(holder);
        } else {
            holder = (PacienteHolder) view.getTag();
        }

        Patient patient = patients.get(position);
        holder.name.setText(patient.getNomeCompleto());
        holder.sex.setText(patient.getSexoShortString());
        holder.convenio.setText(patient.getConvenio().toString());
        holder.pesquisas.setText(patient.isAgreesWithResearch() ? context.getString(R.string.agrees) : context.getString(R.string.doesn_agree));

        return view;
    }

    public void removeRow(int position) {
        patients.remove(position);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(Collection<Patient> collection) {
        patients.clear();
        patients.addAll(collection);
        notifyDataSetChanged();
    }
}
