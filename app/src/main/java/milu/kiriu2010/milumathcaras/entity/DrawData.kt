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
    // サイクロイド曲線
    ID_CYCLOID_01(1),
    // ハイポサイクロイド曲線
    ID_HYPO_CYCLOID_02(2);

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
    val title: String,
    // 静止画のときの初期位置
    val initPos: Float
):  Parcelable {
    constructor(parcel: Parcel) : this(
        // 描画データの識別子
        parcel.readParcelable(DrawDataID::class.java.classLoader),
        // タイトル
        parcel.readString(),
        // 静止画のときの初期位置
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            // 描画データの識別子
            it.writeParcelable(id,flags)
            // タイトル
            it.writeString(title)
            // 静止画のときの初期位置
            it.writeFloat(initPos)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DrawData> {
        override fun createFromParcel(parcel: Parcel): DrawData {
            return DrawData(parcel)
        }

        override fun newArray(size: Int): Array<DrawData?> {
            return arrayOfNulls(size)
        }
    }

}
