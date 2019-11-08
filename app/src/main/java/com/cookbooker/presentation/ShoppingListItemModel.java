package com.cookbooker.presentation;

class ShoppingListItemModel {

    private boolean isSelected;
    private String shoppingInput;

    public ShoppingListItemModel(String input) {
        this.shoppingInput = input;
        isSelected = false;
    }

    public String getShoppingInput() {
        return shoppingInput;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
