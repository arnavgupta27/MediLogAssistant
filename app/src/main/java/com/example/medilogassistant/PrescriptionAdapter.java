package com.example.medilogassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

public class PrescriptionAdapter extends ArrayAdapter<String> {
    private final int[] iconResIds = {
            R.drawable.ic_capsule1,
            R.drawable.ic_capsule2,
            R.drawable.ic_capsule3,
            R.drawable.ic_capsule4
            // Add more if you have more icons
    };
    private final Random random = new Random();

    public PrescriptionAdapter(Context context, List<String> prescriptions) {
        super(context, 0, prescriptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prescription, parent, false);
        }
        ImageView icon = convertView.findViewById(R.id.icon_capsule);
        TextView text = convertView.findViewById(R.id.text_prescription);

        // Assign a random icon for each entry
        int iconIndex = random.nextInt(iconResIds.length);
        icon.setImageResource(iconResIds[iconIndex]);

        text.setText(getItem(position));
        return convertView;
    }
}
