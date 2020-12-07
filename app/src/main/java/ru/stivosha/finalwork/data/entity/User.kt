package ru.stivosha.finalwork.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    override val id: Int,
    val first_last_name: String,
    val photo_url: String,
    val followersCount: Int,
    val status: String? = null,
    val educationPlaceName: String? = null,
    val cityName: String? = null,
) : Parcelable, ProfileDetailObject