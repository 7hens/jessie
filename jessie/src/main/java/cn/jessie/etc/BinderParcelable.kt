package cn.jessie.etc

import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable

internal class BinderParcelable(val binder: IBinder) : Parcelable {
    constructor(source: Parcel) : this(source.readStrongBinder())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStrongBinder(binder)
    }

    companion object CREATOR : Parcelable.Creator<BinderParcelable> {
        override fun createFromParcel(source: Parcel): BinderParcelable {
            return BinderParcelable(source)
        }

        override fun newArray(size: Int): Array<BinderParcelable?> {
            return arrayOfNulls(size)
        }
    }
}