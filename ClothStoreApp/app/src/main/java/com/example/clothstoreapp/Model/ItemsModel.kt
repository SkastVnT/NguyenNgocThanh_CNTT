package com.example.clothstoreapp.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var productId: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var categoryId: String = "",
    var colors: ArrayList<String> = ArrayList(),
    var images: ArrayList<String> = ArrayList(),
    var sizes: ArrayList<String> = ArrayList(),
    var stock: Int = 0,
    var numberInCart: Int= 0
):Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readInt(),

    )

    override fun describeContents(): Int {
       return 0
    }

    override fun writeToParcel(dest: Parcel, flag: Int) {
        dest.writeString(productId)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeDouble(price)
        dest.writeString(categoryId)
        dest.writeStringList(colors)
        dest.writeStringList(images)
        dest.writeStringList(sizes)
        dest.writeInt(stock)

    }

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }

}

