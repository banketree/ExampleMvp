package com.example.base_fun.kandroid

import com.google.gson.Gson

inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)