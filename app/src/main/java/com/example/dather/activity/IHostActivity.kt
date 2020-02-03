package com.example.dather.activity

import com.example.dather.datasource.IWRepository

interface IHostActivity : IWRepository {
    var latitude: Double
    var longitude: Double
}