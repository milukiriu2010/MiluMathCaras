package milu.kiriu2010.milumathcaras.entity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

// ---------------------------------------------------
// 描画データの識別子
// ---------------------------------------------------
// EnumのParcelable
// https://qiita.com/hkusu/items/bf0029283c1032054119
// ---------------------------------------------------
enum class DrawDataID(val id: Int): Parcelable {
    // サイクロイド
    ID_CYCLOID_01(1);

    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DrawDataID> {
        override fun createFromParcel(parcel: Parcel): DrawDataID {
            //return DrawDataID(parcel)
            return DrawDataID.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<DrawDataID?> {
            return arrayOfNulls(size)
        }
    }
}

// ------------------
// 描画データ
// ------------------
data class DrawData(
    // 描画データの識別子
    val id: DrawDataID,
    // タイトル
    val title: String
    // サムネイル
    //var thumbNail: Drawable
):  Parcelable {
    constructor(parcel: Parcel) : this(
        // 描画データの識別子
        parcel.readParcelable(DrawDataID::class.java.classLoader),
        // タイトル
        parcel.readString()
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            // 描画データの識別子
            it.writeParcelable(id,flags)
            // タイトル
            it.writeString(title)
        }
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<DrawData> {
        override fun createFromParcel(parcel: Parcel): DrawData {
            return DrawData(parcel)
        }

        override fun newArray(size: Int): Array<DrawData?> {
            return arrayOfNulls(size)
        }
    }

}
