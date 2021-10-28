package com.example.filmsearch.ui.main.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FilmDTO(

//    @SerializedName("genres")
//    val genres: List<Genres>,

    @SerializedName("id")
    val id: Long?,

    @SerializedName("title")
    val title: String?,

//    @SerializedName("original_language")
//    val originalLanguage: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("poster_path")
    val posterPath: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(releaseDate)
        parcel.writeString(overview)
        parcel.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilmDTO> {
        override fun createFromParcel(parcel: Parcel): FilmDTO {
            return FilmDTO(parcel)
        }

        override fun newArray(size: Int): Array<FilmDTO?> {
            return arrayOfNulls(size)
        }
    }
}
