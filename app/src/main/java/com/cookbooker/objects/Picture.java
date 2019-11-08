package com.cookbooker.objects;

public class Picture {
    private long id;
    private byte[] data;

    public Picture(long id) {
        this.id = id;
        this.data = new byte[0];
    }

    public Picture(long id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return this.id;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Picture:" +
                "\n\tId: " + this.id +
                "\n\tData:" + this.data.length;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Picture)
            return this.id == ((Picture) o).getId();
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.id + "");
    }

}