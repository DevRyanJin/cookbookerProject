package com.cookbooker.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbooker.R;

import java.util.List;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItemModel> {

    Context context;
    List<ShoppingListItemModel> list;

    public ShoppingListAdapter(@NonNull Context context, int resource, List<ShoppingListItemModel> list) {

        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ModelHolder holder = new ModelHolder();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, null);

            holder.checkBox = convertView.findViewById(R.id.check_box);
            holder.textView = convertView.findViewById(R.id.shopping_input);
            holder.removeButton = convertView.findViewById(R.id.remove_button);
            holder.checkBox.setOnCheckedChangeListener((ShoppingListActivity) context);
            convertView.setTag(holder);

        } else {
            holder = (ModelHolder) convertView.getTag();
        }


        if (list.get(position).getSelected())
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

        ShoppingListItemModel models = list.get(position);
        holder.textView.setText(models.getShoppingInput());
        holder.checkBox.setTag(list);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShoppingListItemModel> ingredients = ShoppingListActivity.getmIngredients();
                List<String> ingredientName = ShoppingListActivity.getIngredientName();
                ShoppingListAdapter shoppingListAdapter = ShoppingListActivity.getShoppingListAdapter();
                Toast toast = Toast.makeText(getContext(), "\'" + ingredientName.get(position) + "\' has been removed", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                shoppingListAdapter.remove(ingredients.get(position));
                ingredientName.remove(position);
            }
        });

        return convertView;
    }

    public static class ModelHolder {
        public CheckBox checkBox;
        public TextView textView;
        public Button removeButton;
    }
}


