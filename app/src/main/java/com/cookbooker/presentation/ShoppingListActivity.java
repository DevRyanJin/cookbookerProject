package com.cookbooker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cookbooker.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ListView listView;
    private EditText mItemEdit;
    private Button mAddButton;
    private Button mResultButton;
    private static List<ShoppingListItemModel> mIngredients;
    private static ArrayList<String> ingredientName;
    private static ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = findViewById(R.id.shopListToolbar);
        setSupportActionBar(toolbar);

        mItemEdit = findViewById(R.id.shopSearchText);
        mAddButton = findViewById(R.id.shopAddButton);
        mResultButton = findViewById(R.id.shopSearchButton);
        mIngredients = new ArrayList<>();
        ingredientName = new ArrayList<>();
        listView = findViewById(R.id.shopSearchResult);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                if (!item.isEmpty()) {
                    ingredientName.add(item);
                    mIngredients.add(new ShoppingListItemModel(item));
                    shoppingListAdapter = new ShoppingListAdapter(ShoppingListActivity.this, R.layout.item_list, mIngredients);
                    listView.setAdapter(shoppingListAdapter);

                }
                mItemEdit.setText("");
            }
        });


        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> ingredients = new ArrayList<>();

                for (int i = 0; i < mIngredients.size(); i++) {
                    if (mIngredients.get(i).getSelected())
                        ingredients.add(mIngredients.get(i).getShoppingInput());
                }

                Intent intent = new Intent(ShoppingListActivity.this, MainActivity.class);
                intent.putExtra("cookbooker_" + R.string.shopping_list_query, ingredients);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {

        int position = listView.getPositionForView(compoundButton);

        if (position != ListView.INVALID_POSITION) {

            ShoppingListItemModel shopItem = mIngredients.get(position);
            mIngredients.get(position).setSelected(isSelected);
            shopItem.setSelected(isSelected);

            if (isSelected)
                Toast.makeText(this, "Selected : " + shopItem.getShoppingInput(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Deselected : " + shopItem.getShoppingInput(), Toast.LENGTH_SHORT).show();

        }

    }

    public static ArrayList<String> getIngredientName() {
        return ingredientName;
    }

    public static List<ShoppingListItemModel> getmIngredients() {
        return mIngredients;
    }

    public static ShoppingListAdapter getShoppingListAdapter() {
        return shoppingListAdapter;
    }
}
