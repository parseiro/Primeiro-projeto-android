package com.vilelapinheiro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vilelapinheiro.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Patient> patients = new ArrayList<>();

    private static class PatientHolder {
        public TextView name, sex, convenio, pesquisas;
    }

    public PatientListAdapter(Context context) {
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
        return patients.get(position).getPatientId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        PatientHolder holder;

        if (convertView == null) {
            convertView = createView(viewGroup);

            holder = new PatientHolder();

            holder.name = convertView.findViewById(R.id.name);
            holder.sex = convertView.findViewById(R.id.sex);
            holder.convenio = convertView.findViewById(R.id.medicalPlan);
            holder.pesquisas = convertView.findViewById(R.id.pesquisas);

            convertView.setTag(holder);
        } else {
            holder = (PatientHolder) convertView.getTag();
        }

        Patient patient = patients.get(position);
        holder.name.setText(patient.getNomeCompleto());
        holder.sex.setText(patient.getSexoShortString());
        holder.convenio.setText(patient.getConvenio().toString());
        holder.pesquisas.setText(patient.isAgreesWithResearch() ? context.getString(R.string.agrees) : context.getString(R.string.doesn_agree));

        return convertView;
    }

    private View createView(ViewGroup viewGroup) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.item_patient, viewGroup, false);
    }

    public void removeRow(int position) {
        patients.remove(position);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<Patient> collection, boolean filteredByAgreed) {
        patients.clear();
        if (!filteredByAgreed) {
            patients.addAll(collection);
        } else {

            for (int i = 0; i < collection.size(); i++) {
                Patient item = collection.get(i);
                if (item.isAgreesWithResearch() == true) {
                    patients.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
