package a45858000w.filebasetodo;

/**
 * Created by 45858000w on 17/02/17.
 */

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GridViewImageAdapter extends BaseAdapter {

    private Context context;

    public GridViewImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Imagen.ITEMS.length;
    }

    @Override
    public Imagen getItem(int position) {
        return Imagen.ITEMS[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }



    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.imagen_grid, viewGroup, false);
        }

        ImageView imagenCoche = (ImageView) view.findViewById(R.id.imagenItem);

        final Imagen item = getItem(position);
        Glide.with(imagenCoche.getContext())
                .load(item.getIdDrawable())
                .into(imagenCoche);


        return view;
    }


}