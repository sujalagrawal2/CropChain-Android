package com.hexagraph.cropchain.data.web3j

import android.content.Context
import com.hexagraph.cropchain.R
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import javax.inject.Inject

class Web3j @Inject constructor(context: Context) {
    val web3: Web3j =
        Web3j.build(HttpService(context.getString(R.string.PROVIDER_URL)))


}