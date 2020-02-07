package com.kiptechie.diceroller.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Categories implements Parcelable {
    private ArrayList<Object> categories = null;
    private String created_at;
    private String iconUrl;
    private String id;
    private String updated_at;
    private String url;
    private String value;

    public Categories() {
    }


    public Categories(ArrayList<Object> categories, String created_at, String iconUrl, String id, String updated_at, String url, String value) {
        this.categories = categories;
        this.created_at = created_at;
        this.iconUrl = iconUrl;
        this.id = id;
        this.updated_at = updated_at;
        this.url = url;
        this.value = value;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Object> categories) {
        this.categories = categories;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.categories);
        dest.writeString(this.created_at);
        dest.writeString(this.iconUrl);
        dest.writeString(this.id);
        dest.writeString(this.updated_at);
        dest.writeString(this.url);
        dest.writeString(this.value);
    }

    protected Categories(Parcel in) {
        this.categories = new ArrayList<Object>();
        in.readList(this.categories, Object.class.getClassLoader());
        this.created_at = in.readString();
        this.iconUrl = in.readString();
        this.id = in.readString();
        this.updated_at = in.readString();
        this.url = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel source) {
            return new Categories(source);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
