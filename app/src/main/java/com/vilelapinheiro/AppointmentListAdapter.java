package com.vilelapinheiro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Patient;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Appointment> appointments = new ArrayList<>();

    private static class AppointmentHolder {
        public TextView patientNameTextView, dateTextView, timeText;
    }

    public AppointmentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appointments.get(position).getAppointmentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AppointmentHolder holder;

        if (convertView == null) {
            convertView = createView(viewGroup);

            holder = new AppointmentHolder();

            holder.patientNameTextView = convertView.findViewById(R.id.patientNameTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            holder.timeText = convertView.findViewById(R.id.timeText);


            convertView.setTag(holder);
        } else {
            holder = (AppointmentHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);
        Patient patient = AgendaApplication.getPatientDAO().findById(appointment.getPatientId());
//        Log.i("Leonardo", "Appointment: " + appointment + " Patient: " + patient);

        holder.patientNameTextView.setText(patient.getNomeCompleto());

        {
            String dia = UtilsDate.formatDate(convertView.getContext(), appointment.getHorario());
            holder.dateTextView.setText(dia);
        }
        {
            Date date1 = Date.from(appointment.getHorario().atZone(ZoneId.systemDefault()).toInstant());

            String hora = UtilsDate.formatTime(convertView.getContext(), date1);
            holder.timeText.setText(hora);
        }


//        holder.name.setText(appointment.getNomeCompleto());
//        holder.sex.setText(appointment.getSexoShortString());
//        holder.convenio.setText(appointment.getConvenio().toString());
//        holder.pesquisas.setText(appointment.isAgreesWithResearch() ? context.getString(R.string.agrees) : context.getString(R.string.doesn_agree));

        return convertView;
    }

    private View createView(ViewGroup viewGroup) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.item_appointment, viewGroup, false);
    }

    public void removeRow(int position) {
        appointments.remove(position);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<Appointment> collection) {
        appointments.clear();
        appointments.addAll(collection);
        notifyDataSetChanged();
    }
}
