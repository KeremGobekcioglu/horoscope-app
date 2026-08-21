package com.kg.yildizname.core.util

object AppLinks {
    const val LANDING_PAGE = "https://yildiznamepage.vercel.app"
}

/** Where "Share app" should send people to install it. Android is live on Play Store; iOS
 *  isn't published yet, so it still points at the landing page until it is. */
expect val AppLinks.installUrl: String
